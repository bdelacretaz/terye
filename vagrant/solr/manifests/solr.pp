# basic site manifest

# define global paths and file ownership
Exec { path => '/usr/sbin/:/sbin:/usr/bin:/bin' }
File { owner => 'root', group => 'root' }

# create a stage to make sure apt-get update is run before all other tasks
stage { 'bootstrap': before => Stage['main'] }

class solr::bootstrap {
  # we need an updated list of sources before we can apply the configuration
  exec { 'solr_apt_update':
    command => '/usr/bin/apt-get update',
  }
}

class solr::install {

  package { 'tomcat6':
    ensure => present,
  }

  package { 'tomcat6-user':
    ensure => present,
    require => Package['tomcat6'],
  }
 
  package { 'tomcat6-admin':
    ensure => present,
    require => Package['tomcat6-user'],
  }

  file { "/etc/tomcat6/tomcat-users.xml":
    owner => 'root',
    group => 'root',
    mode => '755',
    source => '/shared/tomcat-users.xml',
    require => Package['tomcat6-admin'],
  }

  file { "/var/lib/tomcat6/webapps/solr.war":
    owner => 'root',
    group => 'root',
    mode => '755',
    source => '/shared/apache-solr-3.6.1.war',
    require => File['/etc/tomcat6/tomcat-users.xml'],
  }

  file { "/opt/solr/example.zip":
    owner => 'root',
    group => 'root',
    mode => '755',
    source => '/shared/example.zip',
    require => File['/var/lib/tomcat6/webapps/solr.war'],
  }

#  service { 'tomcat6':
#    ensure => running,
#    require => File["/var/lib/tomcat6/webapps/soler.war"],
#  }

# export JAVA_OPTS="$JAVA_OPTS -Dsolr.solr.home=/opt/solr/examplesssss"
  exec { "start":
    command => 'service tomcat6 restart',
    require => File["/opt/solr/example.zip"],
  }

}

class solr::go {
  class { 'solr::bootstrap':
    stage => 'bootstrap',
  }
  class { 'solr::install': }
}

include solr::go
#include mongodb