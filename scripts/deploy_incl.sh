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

mesos_membership_scheme_jar="mesos-membership-scheme-1.0.0-SNAPSHOT.jar"
marathon_endpoint="http://mesos:8080/v2"
docker_build=false
deploy_marathon_app=false
export_docker_image=false
docker_image_export_path="/tmp/mesos-artifacts/esb"
marathon_version="0.5.13"

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

function listFiles () {
    find "${1}" -maxdepth 1 -mindepth 1 \( ! -iname ".*" \)| rev | cut -d '/' -f1 | rev | awk NF
}

function listDirectories () {
    IFS=' ' read -r -a dirs <<< `ls -l --time-style="long-iso" $1 | egrep '^d' | awk '{print $8}'`
    for dir in "${dirs[@]}"
    do
        echo "${dir}"
    done
}

function build_docker_image () {
    product_name=$1
    product_profile=$2
    product_version=$3
    dockerfile_path=$4
    echoBold "Building docker image for ${product_name}-${product_profile}:${product_version}..."
    build_cmd="docker build --no-cache=true \
        -t \"${product_name}-${product_profile}:${product_version}\" \"${dockerfile_path}\""
    eval $build_cmd
}

# Show usage and exit
function showUsageAndExit() {
    echoError "Insufficient or invalid options provided!"
    echo
    echoBold "Usage: ./build.sh -v [product-version]"
    echo
    exit 1
}

function get_opts () {
    while getopts ":v:m:bdEy" FLAG; do
        case ${FLAG} in
            b)
                docker_build=true
                ;;
            d)
                deploy_marathon_app=true
                ;;
            E)
                export_docker_image=true
                ;;
            v)
                product_version=$OPTARG
                ;;
            m)
                marathon_version=$OPTARG
                ;;
            \?)
                showUsageAndExit
                ;;
        esac
    done
}