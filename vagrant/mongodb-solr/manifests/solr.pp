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

  exec { "install-tomcat-solr":
    command => '/files/install_tomcat_solr.sh',
    require => Package["mongodb-stable"]
  }

}

class solr::go {
  class { 'solr::bootstrap':
    stage => 'bootstrap',
  }
  class { 'solr::install': }
}

include solr::go
include mongodb
