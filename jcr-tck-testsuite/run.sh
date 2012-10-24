#!/bin/bash

function show_usage {
    echo "Usage: ./run.sh [-a] [-t] [terye_artifact]"
    echo "  -a: build artifact"
    echo "  -t: build test suite"
	exit 1
}

build_artifact=false
build_testsuite=false

# parse options
while getopts ":at" opt; do
	case $opt in
		a)
			build_artifact=true
		;;
		t)
			build_testsuite=true
		;;
		\?)
			show_usage
		;;
	esac
done

# skip options
shift $(( OPTIND -1 ))

# make sure we're in the script's folder
cd $(dirname $0)
dir=`pwd`

# verify argument and corresponding folder exist
if [ $# -eq 0 -o ! -d ../$1 ]; then
	show_usage
else
	if $build_testsuite ; then
		# compile jcr-tck-testsuite
		mvn compile
	fi
	# add class files to classpath
	classpath=./target/classes
	# generate dependencies classpath of jcr-tck-testsuite...
	mvn -f ./pom.xml dependency:build-classpath -Dmdep.outputFile=.cp
	# ... and add it to the classpath
	classpath=$classpath:`cat .cp`

	if $build_artifact ; then
		# package (i.e. compile etc) the artifact to be tested
		mvn -f ../$1/pom.xml package
	fi
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
