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

export product_name=${PWD##*/}

function echoDim () {
    if [ -z "$2" ]; then
        echo $'\e[2m'"${1}"$'\e[0m'
    else
        echo -n $'\e[2m'"${1}"$'\e[0m'
    fi
}

function echoError () {
    echo $'\e[1;31m'"${1}"$'\e[0m'
}

function echoSuccess () {
    echo $'\e[1;32m'"${1}"$'\e[0m'
}

function echoDot () {
    echoDim "." "append"
}

function echoBold () {
    echo $'\e[1m'"${1}"$'\e[0m'
}

function askBold () {
    echo -n $'\e[1m'"${1}"$'\e[0m'
}

function deploy() {
    marathon_endpoint=$1
    marathon_app_file_path=$2
    echoBold "Deploying ${marathon_app_file_path}"
    curl -X POST -H "Content-Type: application/json" -d@${marathon_app_file_path} -i "${marathon_endpoint}/apps"
    echoSuccess "Deployment completed!"
}

function undeploy() {
    marathon_endpoint=$1
    marathon_app_id=$2
    echoBold "Deploying ${marathon_app_id}"
    curl -X DELETE -H "Content-Type: application/json" -i "${marathon_endpoint}/apps/${marathon_app_id}"
    echoSuccess "Un-deployment completed!"
}

function showUsageAndExitDistributed () {
    echoBold "Usage: ./deploy.sh [OPTIONS]"
    echo
    echo "Deploy Marathon application for $(echo $product_name | awk '{print toupper($0)}')"
    echo

    echoBold "Options:"
    echo
    echo -e " \t-d  - [OPTIONAL] Deploy distributed pattern"
    echo -e " \t-h  - Show usage"
    echo

    echoBold "Ex: ./deploy.sh"
    echoBold "Ex: ./deploy.sh -d"
    echo
    exit 1
}

function showUsageAndExitDefault () {
    echoBold "Usage: ./deploy.sh [OPTIONS]"
    echo
    echo "Deploy Marathon application for $(echo $product_name | awk '{print toupper($0)}')"
    echo

    echoBold "Options:"
    echo -e " \t-h  - Show usage"
    echo

    echoBold "Ex: ./deploy.sh"
    exit 1
}
