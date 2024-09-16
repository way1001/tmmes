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

package com.aiforest.tmmes.driver.key;

import com.aiforest.tmmes.common.utils.HostUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * @author pnoker
 * @since 2022.1.0
 */
@Slf4j
public class KeyLoader {
    @Getter
    private X509Certificate clientCertificate;
    @Getter
    private KeyPair clientKeyPair;

    private X509Certificate[] clientCertificateChain;

    private static final Pattern IP_ADDR_PATTERN = Pattern.compile("^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
    private static final char[] PASSWORD = "password".toCharArray();
    private static final String CLIENT_ALIAS = "client-ai";


    public KeyLoader load(Path baseDir) throws Exception {
        // 创建一个使用`PKCS12`加密标准的KeyStore。KeyStore在后面将作为读取和生成证书的对象。
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        // PKCS12的加密标准的文件后缀是.pfx，其中包含了公钥和私钥。
        // 而其他如.der等的格式只包含公钥，私钥在另外的文件中。
        Path serverKeyStore = baseDir.resolve("tm-client.pfx");

        // 如果文件不存在则创建.pfx证书文件。
        if (!Files.exists(serverKeyStore)) {
            keyStore.load(null, PASSWORD);
            // 用2048位的RAS算法。`SelfSignedCertificateGenerator`为Milo库的对象。
            KeyPair keyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
            // `SelfSignedCertificateBuilder`也是Milo库的对象，用来生成证书。
            // 中间所设置的证书属性可以自行修改。
            SelfSignedCertificateBuilder builder = new SelfSignedCertificateBuilder(keyPair)
                    .setCommonName("TM Client")
                    .setOrganization("tmfj")
                    .setOrganizationalUnit("dev")
                    .setLocalityName("TianMen")
                    .setStateName("TM")
                    .setCountryCode("CN")
                    .setApplicationUri("urn:tm:opc:ua:client")
                    .addDnsName("localhost")
                    .addIpAddress("127.0.0.1");

            // Get as many hostnames and IP addresses as we can listed in the certificate.
            for (String hostname : HostUtil.getHostNames("0.0.0.0")) {
                if (IP_ADDR_PATTERN.matcher(hostname).matches()) {
                    builder.addIpAddress(hostname);
                } else {
                    builder.addDnsName(hostname);
                }
            }
            // 设置对应私钥的别名，密码，证书链
            X509Certificate certificate = builder.build();
            keyStore.setKeyEntry(CLIENT_ALIAS, keyPair.getPrivate(), PASSWORD, new X509Certificate[]{certificate});
            try (OutputStream out = Files.newOutputStream(serverKeyStore)) {
                // 保存证书到输出流
                keyStore.store(out, PASSWORD);
            }
        } else {
            try (InputStream in = Files.newInputStream(serverKeyStore)) {
                // 如果文件存在则读取
                keyStore.load(in, PASSWORD);
            }
        }
        // 用密码获取对应别名的私钥。
        Key serverPrivateKey = keyStore.getKey(CLIENT_ALIAS, PASSWORD);
        if (serverPrivateKey instanceof PrivateKey) {
            // 获取对应别名的证书对象。
            clientCertificate = (X509Certificate) keyStore.getCertificate(CLIENT_ALIAS);
            clientCertificateChain = Arrays.stream(keyStore.getCertificateChain(CLIENT_ALIAS))
                    .map(X509Certificate.class::cast)
                    .toArray(X509Certificate[]::new);
            // 获取公钥
            PublicKey serverPublicKey = clientCertificate.getPublicKey();
            // 创建Keypair对象。
            clientKeyPair = new KeyPair(serverPublicKey, (PrivateKey) serverPrivateKey);
        }

        return this;
    }

    public X509Certificate[] getClientCertificateChain() {
        return clientCertificateChain;
    }
}
