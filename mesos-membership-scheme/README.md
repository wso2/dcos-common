## Apache Mesos/Marathon Membership Scheme for WSO2 Carbon platform

Mesos membership scheme provides features for automatically discovering WSO2 carbon server clusters on Apache Mesos platform.

### How it works
Once a Carbon server starts it will query container IP addresses in the given cluster via Mesos Marathon API for a given task.
Thereafter Hazelcast network configuration will be updated with the above IP addresses. As a result the Hazelcast instance will
get connected all the other members in the cluster. In addition once a new member is added to the cluster, all the other members will get connected to the new member.

### Installation

1. Apply Carbon kernel patch
      - For Carbon 4.2.0 based products: patch0012
      - Carbon 4.4.x based products: patch001 and patch0005
   Refer to [WSO2 Carbon release matrix](http://wso2.com/products/carbon/release-matrix/) to find out
   the Carbon kernel version of your product. This includes a modification in the Carbon Core component for a custom
   membership scheme implementation to be plugged-in to the kernel.

2. Copy following JAR file to <CARBON_HOME>repository/components/dropins directory.

   ```
      mesos-membership-scheme-<version>.jar
   ```

3. Update axis2.xml with the following configuration:

```
   <clustering class="org.wso2.carbon.core.clustering.hazelcast.HazelcastClusteringAgent" enable="true">
      <parameter name="membershipSchemeClassName">org.wso2.carbon.clustering.mesos.MesosMembershipScheme</parameter>
      <parameter name="membershipScheme">mesos</parameter>

      <!-- Apache Mesos Marathon API endpoint -->
      <parameter name="MARATHON_ENDPOINT">http://mesos:8080</parameter>

      <!-- Marathon task id this carbon server belongs to. If no value is specified it will default to System
      environment variable MARATHON_APP_ID"
      -->
      <parameter name="MARATHON_APP_ID">wso2esb-manager</parameter>

      <!-- List of Marathon application ids to form a Carbon cluster. Use comma separated values for specifying
       multiple values. Carbon server will connect to all the members deployed under given Marathon tasks via
       Hazelcast. If no value is provided then it will default to its own Marathon task -->
      <parameter name="MARATHON_APPLICATIONS">wso2esb-manager,wso2esb-worker</parameter>

      <!-- ENABLE_BASIC_AUTH flags the membership scheme to use Basic Access Authentication over HTTPS. By default
      this is set to false -->
      <parameter name="ENABLE_BASIC_AUTH">true</parameter>

      <!-- Username of secured Marathon REST API -->
      <parameter name="MARATHON_USERNAME">username</parameter>

      !-- Password of secured Marathon REST API -->
      <parameter name="MARATHON_PASSWORD">password</parameter>
   </clustering>
```


### Build Docker images

Dockerfiles and scripts are provided to build the Docker images with minimum overhead.

1. Clone [wso2/dockerfiles](https://github.com/wso2/dockerfiles) repo and build base Docker images for WSO2 products.
2. Goto modules/mesos-membership-scheme/dockerfiles/<product_name>/
3. Run the following command

```
./deploy.sh -v <product_version> -b -E
```


4. Deploy the Marathon application
```
./deploy.sh -v 4.9.0 -d
```

