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
[[ ${BASE_INCLUDED:-} -eq 1 ]] && return || readonly BASE_INCLUDED=1

function echoDim() {
  if [ -z "$2" ]; then
    echo $'\e[2m'"${1}"$'\e[0m'
  else
    echo -n $'\e[2m'"${1}"$'\e[0m'
  fi
}

function echoError() {
  echo $'\e[1;31m'"${1}"$'\e[0m'
}

function echoSuccess() {
  echo $'\e[1;32m'"${1}"$'\e[0m'
}

function echoDot() {
  echoDim "." "append"
}

function echoBold() {
  echo -e "\033[1m${1}\033[0m"
}

function askBold() {
  echo -n $'\e[1m'"${1}"$'\e[0m'
}

function validateDCOSCLI() {
# Check whether Mesos CLI is installed and configured. We need that to deploy artifacts securely
  command -v dcos >/dev/null 2>&1 || { echoError >&2 "Mesos DCOS CLI is not installed in this system. Aborting..."; return 1; }
  dcos config validate >/dev/null 2>&1 || { echoError >&2 "Mesos DCOS CLI is not configured. Aborting..."; return 1; }
  return 0
}

# Deploy a Marathon app via DCOS CLI
# $1 - Marathon application ID
# $2 - Marathon application resource
function deploy() {
  echoBold "Deploying ${1}..."
  if ! validateDCOSCLI; then
    echoError "Failed to deploy ${1}. DCOS CLI validation failed"
    echoError "Please check whether DCOS CLI is properly installed and configured in your system"
    return 1
  fi
  echo "Checking whether ${1} is already deployed"
  if dcos marathon app show $1 >/dev/null 2>&1; then
    echo "${1} is already deployed"
    return 0
  fi
  echoBold "Adding Marathon application resource at ${2}"
  if ! dcos marathon app add $2; then
    echoError "Failed to deploy ${1}. Non-zero exit code returned from DCOS CLI"
    return 1
  fi
  echoSuccess "Successfully deployed ${1}"
  return 0
}

# Undeploy a Marathon app via DCOS CLI
# $1 - Marathon application ID
function undeploy() {
  echoBold "Undeploying ${1}..."
  if ! dcos marathon app show $1 >/dev/null 2>&1; then
    echo "${1} is already undeployed"
    return 0
  fi
  echoBold "Removing Marathon application: ${1}..."
  if ! dcos marathon app remove $1; then
    echoError "Failed to undeploy ${1}. Non-zero exit code returned from DCOS CLI"
    return 1
  fi
  echoSuccess "Successfully undeployed ${1}"
  return 0
}

# Check whether given service port is open
# $1 -Marathon application id
# $2 -service port
function waitUntilServiceIsActive() {
  while ! dcos marathon app show $1 | python $mesos_artifacts_home/common/scripts/get-host-ip.py $1; do
    echoBold "Waiting to get host ip for ${1}"
    sleep 5s
  done
  host_ip=$(dcos marathon app show $1 | python $mesos_artifacts_home/common/scripts/get-host-ip.py $1)
  while ! python $mesos_artifacts_home/common/scripts/check-service.py $host_ip $2; do
    echoBold "Waiting for ${1} to launch on ${host_ip}:${2}..."
    sleep 10s
  done
  echoSuccess "Successfully started ${1}"
  return 0
}

function showUsageAndExitDistributed() {
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

function showUsageAndExitDefault() {
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

function deploy_common_service()
{
  if ! bash ${mesos_artifacts_home}/common/${1}/deploy.sh; then
    echoError "Non-zero exit code returned when deploying ${1}"
    exit 1
  fi
}

function deploy_service()
{
  if ! deploy ${1} $self_path/${1}.json; then
    echoError "Non-zero exit code returned when deploying ${1}"
    exit 1
  fi
  if ! waitUntilServiceIsActive ${1} ${2}; then
    exit 1
  fi
}

function deploy_common_services() {
  deploy_common_service 'marathon-lb'
  deploy_common_service 'wso2-shared-dbs'
}
