Random ideas and thoughts for the Terye project.

# Candidate tools and components for back-end functionality implementation

## Storage

### Apache HBase
http://hbase.apache.org/ - distributed, scalable big data store.

LilyCMS (http://www.lilyproject.org/) has added messaging/indexing functionality
that might be useful to implement JCR indexing and observation.  

### Apache CouchDB
http://couchdb.apache.org/ - document-oriented database.

### Apache Blur
Search server on top of Hadoop and Lucene.

No website yet? http://www.nearinfinity.com/blogs/aaron_mccurry/an_introduction_to_blur.html
has some info.

### MongoDB
http://www.mongodb.org/ - scalable, high performance NoSQL database.

## Indexing

### Apache Solr
http://lucene.apache.org/solr/ - full-text and structured search server.

### elasticsearch
http://www.elasticsearch.org/ - distributed RESTful search engine.

## Coordination and consensus
http://zookeeper.apache.org/ - server for highly reliable distributed coordination

## Messaging
We might need a messaging tool for observation and cluster coordination.

### Apache ActiveMQ
http://activemq.apache.org/ - popular and powerful messaging and integration patterns server.

### RabbitMQ
http://www.rabbitmq.com/ - "messaging that just works"

# Which part of JCR are we interested in?

Here's a rough list of APIs that we want to tackle first.

From Repository and Session: initially a minimum subset that allows us to get the root Node.

## Storage and tree management

From http://www.day.com/maven/jsr170/javadocs/jcr-2.0/index.html?javax/jcr/Node.html
* addNode (node types == later)
* getNode(path)
* getNodes()  (only this variant so far)
* getProperties() (only this variant so far, no name patterns)
* getProperty(path)
* hasNode(path)
* hasNodes()
* hasProperty(path)
* hasProperties()
* setProperty(...) (most variants?)

From parent interface http://www.day.com/maven/jsr170/javadocs/jcr-2.0/javax/jcr/Item.html
* getParent()
* getName()
* getSession()
* isNode()
* getPath()
* remove()

From http://www.day.com/maven/jsr170/javadocs/jcr-2.0/javax/jcr/Property.html
* getXXX() starting with getString()
* getValue() (slightly later)
* isMultiple(later - multi-value properties)
* setXXX()

From http://www.day.com/maven/jsr170/javadocs/jcr-2.0/javax/jcr/Session.html
* getItem(path)
* getNode(path)
* getProperty(path)
* getRepository()
* getRootNode()
* hasPendingChanges(later: transient space)
* itemExists()
* propertyExists()
* nodeExists()
* move(path, path) (slightly later)
* save(later: transient space)

## Later
Observation
From http://www.day.com/maven/jsr170/javadocs/jcr-2.0/javax/jcr/observation/ObservationManager.html

Indexing and search

Locking

## Maybe...
Node types (mostly mixins), 

Versioning

Security

Orderable nodes

## Probably not...
Workspaces

Retention

Shareable nodes

Lifecycle management

Transactions

Same-name siblings
 
 