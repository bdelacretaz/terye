# Terye/Sling first hops

Minimal (and very incomplete in terms of API coverage) in-memory implementation of a JCR content repository that can be used as the underlying repository of Apache Sling.

# Instructions

* Build sling-firsthops: 'mvn clean install'
* Package the launchpad:
 * cd /launchpad/builder
 * mvn clean package
* Start Sling
 * java -jar target/sling-launchpad-1.0-SNAPSHOT-standalone.jar
* You can now execute the [Sling getting started guide](http://sling.apache.org/site/discover-sling-in-15-minutes.html).

Sling does not invalidate its cache (probably since observation is not implemented), therefore updates to content nodes (e.g. HTML files) are not taken into account. For the last part of the guide, either restart Sling (loosing all the content) or create different nodes.