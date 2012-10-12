#!/bin/bash
apt-get update
apt-get -y install tomcat6
apt-get -y install tomcat6-admin
apt-get -y install tomcat6-user
cp /files/tomcat-users.xml /etc/tomcat6/tomcat-users.xml
#cp /files/apache-solr-3.6.1.war /var/lib/tomcat6/webapps/solr.war
mkdir /opt/solr
mkdir /opt/solr/example/
cp -R /files/solr/ /opt/solr/example/solr/
cp /files/apache-solr-3.6.1.war /opt/solr/example/solr/solr.war
cp /files/solr-example.xml /etc/tomcat6/Catalina/localhost/solr-example.xml
service tomcat6 stop
export JAVA_OPTS="$JAVA_OPTS -Dsolr.solr.home=/opt/solr/example/solr"
export JAVA_OPTS="$JAVA_OPTS -Dsolr.data.dir=/opt/solr/example/solr/data"
service tomcat6 start
