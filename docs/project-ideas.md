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