# Terye/MongoDB first hops

Minimal (and very incomplete in terms of API coverage) implementation 
of a JCR content repository using MongoDB as its backend.

The JUnit tests of this module require a MongoDB server at localhost/127.0.0.1:27018,
by default.

## Minimal MongoDB setup, just to run the tests
The simplest is to use a Vagrant VM. The sibling vagrant/mongodb folder contains 
a Vagrantfile with all the required settings, so you just need to run "vagrant up" 
in there to start your local MongoDB server.

## More details about running MongoDB inside a Vagrant VM
* Install [Vagrant] (http://vagrantup.com/)
* For interactive MongoDB use, install MongoDB locally to get the "mongo" client command.
* Check out the Terye repository
* Go to the folder containing the MongoDB Vagrant box:
  * cd path/to/repo/vagrant/mongodb
* In order to boot up the VM type:
  * vagrant up
* You can now connect to MongoDB in the VM:
  * mongo --port 27018
* In order to shutdown the VM type:
  * vagrant halt
* In order to destroy the VM (freeing about 1GB of disk space) type:
  * vagrant destroy

The MongoDB Vagrant module has been copied from https://github.com/bobthecow/vagrant-mongobox
