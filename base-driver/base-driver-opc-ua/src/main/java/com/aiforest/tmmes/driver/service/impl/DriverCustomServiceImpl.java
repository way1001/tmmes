/*
 * Copyright 2016-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.aiforest.tmmes.driver.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.aiforest.tmmes.common.entity.driver.AttributeInfo;
import com.aiforest.tmmes.common.enums.DeviceStatusEnum;
import com.aiforest.tmmes.common.enums.PointTypeFlagEnum;
import com.aiforest.tmmes.common.exception.ConnectorException;
import com.aiforest.tmmes.common.exception.ReadPointException;
import com.aiforest.tmmes.common.exception.WritePointException;
import com.aiforest.tmmes.common.model.Device;
import com.aiforest.tmmes.common.model.Point;
import com.aiforest.tmmes.common.utils.JsonUtil;
import com.aiforest.tmmes.driver.key.KeyLoader;
import com.aiforest.tmmes.driver.sdk.DriverContext;
import com.aiforest.tmmes.driver.sdk.service.DriverCustomService;
import com.aiforest.tmmes.driver.sdk.service.DriverSenderService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.identity.AnonymousProvider;
import org.eclipse.milo.opcua.stack.client.security.DefaultClientCertificateValidator;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.DefaultTrustListManager;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.*;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.*;

import static com.aiforest.tmmes.driver.sdk.utils.DriverUtil.attribute;
import static com.aiforest.tmmes.driver.sdk.utils.DriverUtil.value;
import static org.eclipse.milo.opcua.stack.core.Identifiers.EndpointDescription;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

/**
 * @author pnoker
 * @since 2022.1.0
 */
@Slf4j
@Service
public class DriverCustomServiceImpl implements DriverCustomService {

    @Resource
    private DriverContext driverContext;
    @Resource
    private DriverSenderService driverSenderService;

    private Map<String, OpcUaClient> connectMap;

    private DefaultTrustListManager trustListManager;


    @Override
    public void initial() {
        /*
        !!! 提示：此处逻辑仅供参考，请务必结合实际应用场景。!!!
        !!!
        你可以在此处执行一些特定的初始化逻辑，驱动在启动的时候会自动执行该方法。
        */
        connectMap = new ConcurrentHashMap<>(16);
    }

    @Override
    public void schedule() {
        /*
        !!! 提示：此处逻辑仅供参考，请务必结合实际应用场景。!!!
        !!!
        上传设备状态，可自行灵活拓展，不一定非要在schedule()接口中实现，你可以：
        - 在read中实现设备状态的判断；
        - 在自定义定时任务中实现设备状态的判断；
        - 通过某种判断机制实现设备状态的判断。

        最后通过 driverSenderService.deviceStatusSender(deviceId,deviceStatus) 接口将设备状态交给SDK管理，其中设备状态（StatusEnum）：
        - ONLINE:在线
        - OFFLINE:离线
        - MAINTAIN:维护
        - FAULT:故障
         */
        driverContext.getDriverMetadata().getDeviceMap().keySet().forEach(id -> driverSenderService.deviceStatusSender(id, DeviceStatusEnum.ONLINE));
    }

    @Override
    public String read(Map<String, AttributeInfo> driverInfo, Map<String, AttributeInfo> pointInfo, Device device, Point point) {
        /*
        !!! 提示：此处逻辑仅供参考，请务必结合实际应用场景。!!!
         */
        OpcUaClient client = null;
        try {
            client = getConnector(device.getId(), driverInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return readValue(client, pointInfo);

    }

    @Override
    public Boolean write(Map<String, AttributeInfo> driverInfo, Map<String, AttributeInfo> pointInfo, Device device, AttributeInfo value) {
        /*
        !!! 提示：此处逻辑仅供参考，请务必结合实际应用场景。!!!
         */
        OpcUaClient client = null;
        try {
            client = getConnector(device.getId(), driverInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return writeValue(client, pointInfo, value);
    }

    /**
     * 获取 Opc Ua Client
     *
     * @param deviceId   设备ID
     * @param driverInfo 驱动信息
     * @return OpcUaClient
     */
    private OpcUaClient getConnector(String deviceId, Map<String, AttributeInfo> driverInfo) throws Exception  {
        log.debug("Opc Ua Server Connection Info {}", JsonUtil.toJsonString(driverInfo));
        OpcUaClient opcUaClient = connectMap.get(deviceId);
        if (ObjectUtil.isNull(opcUaClient)) {
            String host = attribute(driverInfo, "host");
            int port = attribute(driverInfo, "port");
            String path = attribute(driverInfo, "path");
            String url = String.format("opc.tcp://%s:%s%s", host, port, path);
            try {
                Path securityTempDir = Paths.get(System.getProperty("java.io.tmpdir"), "client", "security");
                Files.createDirectories(securityTempDir);
                if (!Files.exists(securityTempDir)) {
                    throw new Exception("unable to create security dir: " + securityTempDir);
                }
                File pkiDir = securityTempDir.resolve("pki").toFile();
                log.debug("security dir: {}", securityTempDir.toAbsolutePath());
                log.debug("security pki dir: {}", pkiDir.getAbsolutePath());
                KeyLoader loader = new KeyLoader().load(securityTempDir);

                trustListManager = new DefaultTrustListManager(pkiDir);

                DefaultClientCertificateValidator certificateValidator =
                        new DefaultClientCertificateValidator(trustListManager);

//                opcUaClient = OpcUaClient.create(url, endpoints -> endpoints.stream().findFirst(), configBuilder -> configBuilder.setIdentityProvider(new AnonymousProvider()).setRequestTimeout(uint(5000)).build());
                opcUaClient = OpcUaClient.create(
                        url,
                        endpoints ->
                                endpoints.stream()
                                        .filter(e -> SecurityPolicy.Basic256Sha256.getUri().equals(e.getSecurityPolicyUri()))
                                        .findFirst(),
                        configBuilder ->
                                configBuilder
                                        .setApplicationName(LocalizedText.english("TM Client"))
                                        .setApplicationUri("urn:tm:opc:ua:client")
                                        .setKeyPair(loader.getClientKeyPair())
                                        .setCertificate(loader.getClientCertificate())
                                        .setCertificateChain(loader.getClientCertificateChain())
//                                        .setCertificateValidator(certificateValidator)
                                        .setIdentityProvider(new AnonymousProvider())
                                        .setRequestTimeout(uint(5000))
                                        .build()
                );
                connectMap.put(deviceId, opcUaClient);
            } catch (UaException e) {
                connectMap.entrySet().removeIf(next -> next.getKey().equals(deviceId));
                log.error("Connect opc ua client error: {}", e.getMessage(), e);
                throw new ConnectorException(e.getMessage());
            }
        }
        return opcUaClient;
    }

    /**
     * 获取 Opc Ua Item
     *
     * @param pointInfo 位号信息
     * @return OpcUa Node
     */
    private NodeId getNode(Map<String, AttributeInfo> pointInfo) {
        int namespace = attribute(pointInfo, "namespace");
        String tag = attribute(pointInfo, "tag");
        return new NodeId(namespace, tag);
    }

    /**
     * 获取 OpcUa 值
     *
     * @param client    OpcUaClient
     * @param pointInfo 位号信息
     * @return Node Value
     */
    private String readValue(OpcUaClient client, Map<String, AttributeInfo> pointInfo) {
        try {
            NodeId nodeId = getNode(pointInfo);
            client.connect().get();
            CompletableFuture<String> value = new CompletableFuture<>();
            client.readValue(0.0, TimestampsToReturn.Both, nodeId).thenAccept(dataValue -> value.complete(dataValue.getValue().getValue().toString()));
            return value.get(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error("Read opc ua value error: {}", e.getMessage(), e);
            Thread.currentThread().interrupt();
            throw new ReadPointException(e.getMessage());
        } catch (ExecutionException | TimeoutException e) {
            log.error("Read opc ua value error: {}", e.getMessage(), e);
            throw new ReadPointException(e.getMessage());
        }
    }

    /**
     * 写入 OpcUa 值
     *
     * @param pointInfo 位号信息
     * @param value     写入值
     */
    private boolean writeValue(OpcUaClient client, Map<String, AttributeInfo> pointInfo, AttributeInfo value) {
        try {
            NodeId nodeId = getNode(pointInfo);

            client.connect().get();
            return writeNode(client, nodeId, value);
        } catch (InterruptedException e) {
            log.error("Write opc ua value error: {}", e.getMessage(), e);
            Thread.currentThread().interrupt();
            throw new WritePointException(e.getMessage());
        } catch (ExecutionException e) {
            log.error("Write opc ua value error: {}", e.getMessage(), e);
            throw new WritePointException(e.getMessage());
        }
    }

    /**
     * Write Opc Ua Node
     *
     * @param client OpcUaClient
     * @param nodeId OpcUa Node
     * @param value  写入值
     * @return 是否写入
     * @throws ExecutionException   ExecutionException
     * @throws InterruptedException InterruptedException
     */
    private boolean writeNode(OpcUaClient client, NodeId nodeId, AttributeInfo value) throws ExecutionException, InterruptedException {
        PointTypeFlagEnum valueType = PointTypeFlagEnum.ofCode(value.getType().getCode());
        if (ObjectUtil.isNull(valueType)) {
            throw new IllegalArgumentException("Unsupported type of " + value.getType());
        }

        CompletableFuture<StatusCode> status = new CompletableFuture<>();
        switch (valueType) {
            case INT:
                int intValue = value(value.getType().getCode(), value.getValue());
                status = client.writeValue(nodeId, new DataValue(new Variant(intValue)));
                break;
            case LONG:
                long longValue = value(value.getType().getCode(), value.getValue());
                status = client.writeValue(nodeId, new DataValue(new Variant(longValue)));
                break;
            case FLOAT:
                float floatValue = value(value.getType().getCode(), value.getValue());
                status = client.writeValue(nodeId, new DataValue(new Variant(floatValue)));
                break;
            case DOUBLE:
                double doubleValue = value(value.getType().getCode(), value.getValue());
                status = client.writeValue(nodeId, new DataValue(new Variant(doubleValue)));
                break;
            case BOOLEAN:
                boolean booleanValue = value(value.getType().getCode(), value.getValue());
                status = client.writeValue(nodeId, new DataValue(new Variant(booleanValue)));
                break;
            case STRING:
                status = client.writeValue(nodeId, new DataValue(new Variant(value.getValue())));
                break;
            default:
                break;
        }

        if (ObjectUtil.isNotNull(status) && ObjectUtil.isNotNull(status.get())) {
            return status.get().getValue() > 0;
        }
        return false;
    }

}
