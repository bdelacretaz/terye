#!/bin/bash

# install tomcat
apt-get update
apt-get -y install tomcat6
apt-get -y install tomcat6-admin
apt-get -y install tomcat6-user

# copy tomcat user configuration file
cp /files/tomcat-users.xml /etc/tomcat6/tomcat-users.xml

# copy solr .war and example files
mkdir /opt/solr
mkdir /opt/solr/example/
cp -R /files/solr/ /opt/solr/example/solr/
cp /files/apache-solr-3.6.1.war /opt/solr/example/solr/solr.war

# copy context file for tomcat to pick up
cp /files/solr-example.xml /etc/tomcat6/Catalina/localhost/solr-example.xml

# restart tomcat
service tomcat6 restart
