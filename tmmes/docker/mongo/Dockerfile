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

FROM mongo
MAINTAINER aiforest <aiforest.icloud.com>

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && mkdir -p /tmmes-mongo/config/

WORKDIR /tmmes-mongo

ENV CACHE_SIZE_GB 2

COPY ./tmmes.js  ./config/
COPY ./launch.sh   ./

RUN apt update \
    && apt install tofrodos \
    && apt clean \
    && fromdos ./launch.sh \
    && chmod +x ./launch.sh

EXPOSE 27017

CMD [ "/tmmes-mongo/launch.sh"]