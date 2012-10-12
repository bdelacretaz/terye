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
  # install solr and the tomcat servlet container

#  package { 'solr-tomcat':
#    ensure => present,
#  }

  # grab schema.xml and solrconfig.xml from the selected drupal contrib module

#  if $drupalsolrmodule == 'apachesolr' {
#    exec { 'solr-download-drupal-module':
#      command => 'wget http://ftp.drupal.org/files/projects/apachesolr-7.x-1.0-rc3.tar.gz && tar xzf apachesolr-7.x-1.0-rc3.tar.gz',
#      cwd     => '/root',
#      creates => '/root/apachesolr',
#    }

#    $solr_schema_source = 'file:///root/apachesolr/solr-conf/schema.xml'
#    $solr_config_source = 'file:///root/apachesolr/solr-conf/solrconfig.xml'

#    file { '/etc/solr/conf/protwords.txt':
#      source  => 'file:///root/apachesolr/solr-conf/protwords.txt',
#      require => Exec['solr-download-drupal-module'],
#    }
#  } else {
#    exec { 'solr-download-drupal-module':
#      command => 'wget http://ftp.drupal.org/files/projects/search_api_solr-7.x-1.0-rc2.tar.gz && tar xzf search_api_solr-7.x-1.0-rc2.tar.gz',
#      cwd     => '/root',
#      creates => '/root/search_api_solr',
#    }

#    $solr_schema_source = 'file:///root/search_api_solr/schema.xml'#    $solr_config_source = 'file:///root/search_api_solr/solrconfig.xml'
#  }

#  file { '/etc/solr/conf/schema.xml':
#    source  => $solr_schema_source,
#    require => Exec['solr-download-drupal-module'],
#  }

#  file { '/etc/solr/conf/solrconfig.xml':
#    source  => $solr_config_source,
#    require => Exec['solr-download-drupal-module'],
#  }

  # install apache and add a proxy for solr

  class { 'apache': }
  class { 'apache::mod::proxy': }

  apache::vhost::proxy { 'solr.33.33.33.20.xip.io':
    port => '80',
    dest => 'http://localhost:8080/solr/',
  }
  exec { 'test':
    command => 'echo "test"',
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