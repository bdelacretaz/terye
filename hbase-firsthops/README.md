# Terye/HBase first hops

Minimal (and very incomplete in terms of API coverage) implementation of a JCR content repository which uses HBase for persistence and which can be used as the underlying repository of Apache Sling.

* The artifact 'sling-hbase-repository' implements a subset of the JCR API and uses HBase for persistent storage
* The artifact 'hbase-bundle' bundles all HBase and Hadoop sources. When built, the resulting bundle contains the code of HBase 0.94.2 and Hadoop 1.0.4 and also includes all of their dependencies (transitively). The bundle is loaded into the Sling OSGi framework and exposes the necessary packages upon which 'sling-hbase-repository' depend.
* The artifact 'sling-hbase-launchpad' can be used to start an instance of Sling that uses 'sling-hbase-repository' as its underlying repository.

# Instructions

* Build hbase-firsthops (this builds all contained artifacts):
 * mvn clean install
* If you get an out-of-memory error, increase the memory allocated to the process:
 * MAVEN_OPTS="-Xmx512M -XX:MaxPermSize=512M" mvn clean install
* Start the hbase Vagrant box: [Instructions](https://github.com/bdelacretaz/terye/blob/master/vagrant/hbase/README.md)
* Start Sling
 * cd /sling-hbase-launchpad
 * java -jar target/sling-hbase-launchpad-1.0-SNAPSHOT-standalone.jar
* Sling should now run at [http://localhost:8080](http://localhost:8080)
* You can now execute the [Sling getting started guide](http://sling.apache.org/site/discover-sling-in-15-minutes.html).
