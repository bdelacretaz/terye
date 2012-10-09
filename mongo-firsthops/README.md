# Instructions for running MongoDB inside a Vagrant VM

* Install [Vagrant] (http://vagrantup.com/)
* Install MongoDB locally (you'll need the mongo client)
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