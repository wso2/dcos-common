# DC/OS Membership Scheme for WSO2 Carbon Platform

DC/OS Membership Scheme is a WSO2 Carbon Kernel extension for automatically 
discoverying WSO2 server clusters on [DC/OS](https://github.com/dcos/dcos) 
for WSO2 components which require Carbon clustering. The members in the cluster 
can be discovered in two ways:
   
   1. Using Mesos DNS API
   2. Using Marathon API

## How it works

Under ```portMappings``` section of Marathon application definition for all WSO2 
components which require clustering, the first entry is reserved for Hazelcast 
communication. Since Hazelcast creates point-to-point connections and advertises 
its own member port configured in axis2.xml file to other members which is used to 
create those connections, the port being advertised and the local port must be 
the same. This is achieved using  
[DC/OS Service Endpoints](https://docs.mesosphere.com/1.10/deploying-services/service-endpoints/).

First, when a WSO2 server container is started it will check for the clustering 
configurationgiven in the axis2.xml file. If clustering is enabled, and 
```membershipSchemeClassName``` is set to ```org.wso2.carbon.clustering.mesos.MesosMembershipScheme``` 
DC/OS membership scheme will get engaged. Afterwards, it will read the configuration 
parameters. According to the given configuration, the membership scheme will lookup 
the members available in the cluster either via the Mesos DNS API or Marathon API 
and initialize the clustering agent.

### Mesos-DNS
This will query Mesos-DNS REST API to get a list of DNS records for given list of clusters (a Carbon cluster maps to a Mesos service). It will wait for a given timeout (default: 10s) until 
DNS records are updated. [Mesos-DNS](https://mesosphere.github.io/mesos-dns/) periodically queries the master and generates DNS records for all services. 
Therefore, there might be a delay to reflect the latest state.

### Marathon REST API
During the Carbon server startup it will query a list of container IP addresses in the given list of clusters via Mesos Marathon API for a given task.
Thereafter Hazelcast network configuration will be updated with the retrieved IP addresses allowing that Carbon instance to connect with all the other members in the cluster.
Similarly once a new member is added to the cluster, all existing members will connect to the new member.

## Configuration

| Parameter                     | Required | Description |
|-------------------------------|----------|-------------|
| MESOS_MEMBER_DISCOVERY_SCHEME | Yes      | The cluster discovery scheme to be used: MesosDNS or Marathon. |
| MARATHON_ENDPOINT             | Yes if the value of MESOS_MEMBER_DISCOVERY_SCHEME is set to Marathon | The URL of the Marathon API. |
| MESOS_DNS_ENDPOINT            | Yes if the value of MESOS_MEMBER_DISCOVERY_SCHEME is set to MesosDNS | The URL of the Mesos DNS API. |
| MARATHON_APPLICATIONS         | Yes      | A comma separated list of Marathon application names to be used for adding members to the WSO2 Carbon cluster. |
| ENABLE_MARATHON_BASIC_AUTH    | Yes if the value of MESOS_MEMBER_DISCOVERY_SCHEME is set to Marathon | Set value to true for enabling basic authentication for Marathon discovery scheme. If not set it to false. |
| MARATHON_USERNAME             | Yes if the value of ENABLE_MARATHON_BASIC_AUTH is set to true | The username for Marathon API authentication process. |
| MARATHON_PASSWORD             | Yes if the value of ENABLE_MARATHON_BASIC_AUTH is set to true | The password for Marathon API authentication process. |

## Installation

1. Apply Carbon kernel patch
      - For Carbon 4.2.0 based products: patch0012
      - Carbon 4.4.x based products: patch001 and patch0005
   Refer to [WSO2 Carbon release matrix](http://wso2.com/products/carbon/release-matrix/) to find out
   the Carbon kernel version of your product. This includes a modification in the Carbon Core component for a custom
   membership scheme implementation to be plugged-in to the kernel.

2. Copy following JAR file to <CARBON_HOME>repository/components/dropins directory.

    ```
    dcos-membership-scheme-<version>.jar
    ```

3. Update axis2.xml with the following configuration:

```
   <clustering class="org.wso2.carbon.core.clustering.hazelcast.HazelcastClusteringAgent" enable="true">
      <parameter name="membershipSchemeClassName">org.wso2.carbon.clustering.mesos.MesosMembershipScheme</parameter>
      <parameter name="membershipScheme">mesos</parameter>

      <!-- Set discovery scheme to either `Marathon` which is the default if none provided, or `MesosDNS` -->
      <parameter name="MESOS_MEMBER_DISCOVERY_SCHEME">Marathon</parameter>

      <!-- Mesos Marathon REST API endpoint to be used with Marathon discovery scheme -->
      <parameter name="MARATHON_ENDPOINT">http://marathon.mesos:8080</parameter>

      <!-- Mesos-DNS REST API endpoint to be used with MesosDNS discovery scheme -->
      <parameter name="MESOS_DNS_ENDPOINT">http://marathon.mesos:8123</parameter>

      <!-- List of Marathon application ids to form a Carbon cluster. Use comma separated values for specifying multiple values.
           Carbon server will connect to all the members deployed under given Marathon tasks via Hazelcast. If no value is provided then it will default to its own Marathon task -->
      <parameter name="MARATHON_APPLICATIONS">wso2esb-manager,wso2esb-worker</parameter>

      <!-- This option flags the membership scheme to use Basic Access Authentication . By default this is set to false -->
      <parameter name="ENABLE_MARATHON_BASIC_AUTH">true</parameter>

      <!-- Username of secured Marathon REST API -->
      <parameter name="MARATHON_USERNAME">username</parameter>

      <!-- Password of secured Marathon REST API -->
      <parameter name="MARATHON_PASSWORD">password</parameter>
   </clustering>
```
