#!/bin/bash
# ------------------------------------------------------------------------
#
# Copyright 2016 WSO2, Inc. (http://wso2.com)
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License

# ------------------------------------------------------------------------

set -e
self_path=$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)
mesos_artifacts_home="${self_path}/../../.."
source "${mesos_artifacts_home}/common/scripts/base.sh"
mysql_gov_db_host_port=10000
mysql_user_db_host_port=10001

echo "Deploying WSO2 shared databases..."

deploy_gov_db="deploy mysql-gov-db ${self_path}/mysql-gov-db.json"
deploy_user_db="deploy mysql-user-db ${self_path}/mysql-user-db.json"

if ! ($deploy_gov_db && $deploy_user_db); then
  echoError "Failed to deploy WSO2 shared databases"
  exit 1
fi
waitUntilServiceIsActive 'mysql-gov-db'
waitUntilServiceIsActive 'mysql-user-db'
echoSuccess "Successfully deployed WSO2 shared databases"
