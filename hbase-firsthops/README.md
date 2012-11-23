# Terye/HBase first hops

Minimal (and very incomplete in terms of API coverage) implementation of a JCR content repository which uses HBase for persistence and which can be used as the underlying repository of Apache Sling.

# Instructions

* Build hbase-firsthops:
 * mvn clean install
* If you get an out-of-memory error, increase the memory allocated to the process:
 * MAVEN_OPTS="-Xmx512M -XX:MaxPermSize=512M" mvn clean install
* Package the launchpad:
 * mvn clean package
* Start Sling
 * cd /sling-hbase-launchpad
 * java -jar target/sling-hbase-launchpad-1.0-SNAPSHOT-standalone.jar
* You can now execute the [Sling getting started guide](http://sling.apache.org/site/discover-sling-in-15-minutes.html).
