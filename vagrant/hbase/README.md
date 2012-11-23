# README for using the hbase Vagrant box

Use the standard Vagrant commands (vagrant up / halt / ...) for running and stopping the box.

The virtual machine is assigned the following...
* static address IP: 192.168.50.4
* hostname: lucid64

The different web UIs are available at following addresses:

* Hadoop name node UI: http://192.168.50.4:50070
* Hadoop job tracker UI: http://192.168.50.4:50030
* HBase master UI: http://192.168.50.4:60010
* HBase region server UI: http://192.168.50.4:60030

Since the HBase master server reports back its hostname to connecting clients (as opposed to its IP address), a new entry must be added to the local /etc/hosts file:

    192.168.50.4  lucid64
