#
# Copyright 2024-present AiForest All Rights Reserved
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#     http://www.apache.org/licenses/LICENSE-2.0
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

FROM rabbitmq
MAINTAINER aiforest <aiforest.icloud.com>

COPY ./rabbitmq.conf /etc/rabbitmq/rabbitmq.conf
COPY ./keys /data/rabbitmq/tmmes/keys

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && rm -f /etc/rabbitmq/conf.d/management_agent.disable_metrics_collector.conf \
    && rabbitmq-plugins enable --offline rabbitmq_management rabbitmq_mqtt rabbitmq_stomp