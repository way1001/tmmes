#
# Copyright 2016-present the original author or authors.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

# runtime
#FROM registry.cn-beijing.aliyuncs.com/dc3/alpine-java:dragonwell-8.13.14 AS runtime
#MAINTAINER pnoker <pnokers.icloud.com>

FROM openjdk:8

ENV JAVA_OPS -server \
             -Xms128m \
             -Xmx1024m \
             -Djava.security.egd=file:/dev/./urandom \
             -XX:CompressedClassSpaceSize=128m \
             -XX:MetaspaceSize=200m \
             -XX:MaxMetaspaceSize=200m
ENV GC_LOG   -XX:+PrintGCDetails \
             -XX:+PrintGCDateStamps \
             -XX:+PrintTenuringDistribution \
             -XX:+PrintHeapAtGC \
             -XX:+PrintReferenceGC \
             -XX:+PrintGCApplicationStoppedTime \
             -XX:+UseGCLogFileRotation \
             -XX:NumberOfGCLogFiles=10 \
             -XX:GCLogFileSize=10M \
             -Xloggc:tmmes/logs/center/data/gc/base-center-data.log

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

WORKDIR /base-center/base-center-data

ADD ./target/base-center-data.jar ./

EXPOSE 8500
VOLUME /base-center/base-center-data/dc3/logs

CMD mkdir -p /base-center/base-center-data/tmmes/logs/center/data/gc \
    && java ${JAVA_OPS} ${GC_LOG} -jar base-center-data.jar
