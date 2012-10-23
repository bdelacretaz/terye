#!/bin/bash

if [ $# -eq 0 ]; then
    echo "Usage: ./test [terye_artifact]"
else
	mvn -f "../$1/pom.xml" install
	# add all jar files in current folder
	classpath="./*"
	# add jar of the artifact to be tested
	classpath=$classpath:"/Users/obrist/.m2/repository/ch/x42/terye/$1/1.0-SNAPSHOT/$1-1.0-SNAPSHOT.jar"
	# use maven to create the classpath for the artifact...
	mvn -f "../$1/pom.xml" dependency:build-classpath  -Dmdep.outputFile=../jcr-tck/.cp
	# ... and add it to the classpath
	classpath=$classpath:`cat .cp`
	# run tests
	java -Djavax.jcr.tck.properties=repositoryStubImpl.properties -classpath $classpath junit.swingui.TestRunner org.apache.jackrabbit.test.TestAll
fi
