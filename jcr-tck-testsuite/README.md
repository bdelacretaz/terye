# JCR TCK Test Suite

Use the included script 'run.sh' to run a selection of tests of the JCR TCK test suite.

Usage example:

    ./runs.sh solr-firsthops

You can control which tests are executed by editing the files:

* [excludes.txt](src/main/resources/excludes.txt)
* [exceptions.txt](src/main/resources/exceptions.txt)

Test specified in 'excludes.txt' are not executed, unless contained in 'exceptions.txt'. This allows e.g. to exclude a whole test case but one test of that test case. You can specify packages, test cases (classes) or single tests (methods).