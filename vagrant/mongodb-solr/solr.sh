#!/bin/bash

if [ $# -eq 0 ]; then
    echo "Usage:"
    echo " - Creates and provisions the VM (only call once). If a project name is specified"
    echo "   then [project_name]/solar-conf is copied to the VM, overwriting the Solr sample"
    echo "   configuration files with the ones contained in that folder."
    echo "    solr.sh init [project_name]"
    echo "    e.g. solr.sh init solr-firsthops"
    echo " - Halt the VM:"
    echo "    solr.sh halt"
    echo " - Resume the VM:"
    echo "    solr.sh up"
    echo " - Destroy the VM:"
    echo "    solr.sh destroy"
elif [ $1 == "init" ]; then
	if [ $# -eq 2 ]; then
		cp -rf "../../$2/solr-conf" files
		if [ $? -eq 1 ]; then
			echo "missing solar-conf folder in ../../$2/"
			exit 1
		fi
		vagrant up
		rm -rf files/solr-conf
	else
		echo "missing 2nd parameter (project name)"
	fi
elif [ $1 == "up" ]; then
	vagrant up --no-provision
elif [ $1 == "halt" ]; then
	vagrant halt
elif [ $1 == "destroy" ]; then
	vagrant destroy
else
	echo "invalid argument"
fi
