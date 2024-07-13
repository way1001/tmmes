/*
 * Copyright 2024-present AiForest All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      https://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

db = db.getSiblingDB("admin");
db = db.getSiblingDB("authorization");
if (!db.getUser("admin")) {
    db.createUser({
        user: "admin",
        pwd: "tmmes",
        roles: [
            {
                role: "readWrite",
                db: "authorization",
            },
        ],
    });
}

db = db.getSiblingDB("admin");
if (!db.getUser("admin")) {
    db.createUser({
        user: "admin",
        pwd: "tmmes",
        roles: [
            {
                role: "readWrite",
                db: "admin",
            },
        ],
    });
}

db = db.getSiblingDB("tmmes");
if (!db.getUser("tmmes")) {
    db.createUser({
        user: "tmmes",
        pwd: "tmmes",
        roles: [
            {
                role: "readWrite",
                db: "tmmes",
            },
        ],
    });
}

if (db.createCollection("point_value")) {
    db.pointValue.createIndex(
        {
            deviceId: 1,
            pointId: 1,
        },
        {
            name: "IX_device_point_id",
            unique: false,
            background: true,
        }
    );
    db.pointValue.createIndex(
        {
            createTime: 1,
        },
        {
            name: "IX_create_time",
            unique: false,
            background: true,
        }
    );
}

if (db.createCollection("driver_event")) {
    db.driverEvent.createIndex(
        {
            driverId: 1,
        },
        {
            name: "IX_driver_id",
            unique: false,
            background: true,
        }
    );
    db.driverEvent.createIndex(
        {
            createTime: 1,
        },
        {
            name: "IX_create_time",
            unique: false,
            background: true,
        }
    );
}

if (db.createCollection("device_event")) {
    db.deviceEvent.createIndex(
        {
            deviceId: 1,
            pointId: 1,
        },
        {
            name: "IX_device_point_id",
            unique: false,
            background: true,
        }
    );
    db.deviceEvent.createIndex(
        {
            createTime: 1,
        },
        {
            name: "IX_create_time",
            unique: false,
            background: true,
        }
    );
}
