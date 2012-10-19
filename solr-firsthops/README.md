# Terye/Solr first hops

Minimal (and very incomplete in terms of API coverage) implementation 
of a JCR content repository using MongoDB as its backend and integrating Solr for basic indexing and querying.

The JUnit tests of this module require a MongoDB server at localhost/127.0.0.1:27018 and a Solr server at localhost/127.0.0.1:1234/solr-example/,
by default. Furthermore, the Solr server must be configured to use solr-conf/schema.xml as the schema file.

# Setup

In order to run the tests it is simplest to use the Vagrant VM contained in the folder ../vagrant/mongodb-solr. Refer to the README in that folder for instructions on how to use it.

# Queries

Basic queries can be issued using the following syntax:

* property:expression
* -property:expression (negation)

Here are some example queries:

* Property 'message' contains term 'hello':
  * message:hello
* Property 'message' does not contain term 'hello':
  * -message:hello
* Property 'message' contains terms 'hello' and 'bye':
  * message:(hello AND bye)
* Property 'message' contains "some text":
  * message:"some text"
* Property 'message' contains "...ildcar...":
  * message:*ildcar*
* Property 'amount' equals 100:
  * amount:100
* Property 'rating' equals 3.9:
  * rating:3.9
* Property 'date' equals '2012-10-10T22:00:00Z':
  * date:"2012-10-10T22:00:00Z"
* Property 'published' is false:
  * published:false
* Some string property contains term 'hello':
  * any:hello

Not supported are:

* multi-condition queries
* range queries