# Terye/Sling first hops

Minimal (and very incomplete in terms of API coverage) in-memory implementation of a JCR content repository that can be used as the underlying repository of Apache Sling.

# Instructions

* Install and launch Sling
* If not present add a OSGi shell to Sling, e.g. download following bundles from [here](http://felix.apache.org/site/downloads.cgi) and install them using the [web console](http://localhost:8080/system/console/bundles):
 * Gogo Runtime
 * Gogo Shell
 * Gogo Command
 * Shell
 * Shell Text UI
* Build the artifact: 'mvn clean install'
* In the Gogo Shell, type
 * install file:/path/to/sling-firsthops/target/sling-firsthops-1.0-SNAPSHOT.jar
 * stop 72 [assuming 72 is the bundle id of the bundle 'Apache Sling Jackrabbit Embedded Repository']
 * start 99 [assuming 99 is the bundle id of the newly installed sling-firsthops bundle]
* Restart Sling
* You can now execute the [Sling getting started guide](http://sling.apache.org/site/discover-sling-in-15-minutes.html).

Sling does not invalidate its cache (probably since observation is not implemented), therefore updates to content nodes (e.g. HTML files) are not taken into account. For the last part of the guide, either restart Sling (loosing all the content) or create different nodes.