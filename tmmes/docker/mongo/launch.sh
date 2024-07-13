#!/bin/sh

#
# Copyright 2024-present AiForest All Rights Reserved
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#      https://www.apache.org/licenses/LICENSE-2.0
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

set -e

mongod &

while true; do
  mongo /tmmes-mongo/config/tmmes.js && break
  sleep 5
done

mongod --shutdown && mongod --auth --bind_ip_all --storageEngine wiredTiger --wiredTigerCacheSizeGB ${CACHE_SIZE_GB} &

wait