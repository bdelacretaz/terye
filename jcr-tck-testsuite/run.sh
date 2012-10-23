#!/bin/bash

# make sure we're in the script's folder
cd $(dirname $0)
dir=`pwd`

if [ $# -eq 0 -o ! -d ../$1 ]; then
    echo "Usage: ./run.sh [terye_artifact]"
else
	# compile jcr-tck-testsuite
	mvn compile
	# add class files to classpath
	classpath=./target/classes
	# generate dependencies classpath of jcr-tck-testsuite...
	mvn -f ./pom.xml dependency:build-classpath -Dmdep.outputFile=.cp
	# ... and add it to the classpath
	classpath=$classpath:`cat .cp`

	# package (i.e. compile etc) the artifact to be tested
	mvn -f ../$1/pom.xml package
	# add jar of the artifact to be tested
	classpath=$classpath:../$1/target/$1-1.0-SNAPSHOT.jar
	# generate the artifact's dependencies classpath...
	mvn -f ../$1/pom.xml dependency:build-classpath  -Dmdep.outputFile=$dir/.cp
	# ... and add it to the classpath
	classpath=$classpath:`cat .cp`

	# remove temporary file
	rm .cp

	# run tests
	java -classpath $classpath junit.swingui.TestRunner ch.x42.terye.test.FilteredTestSuite
fi
