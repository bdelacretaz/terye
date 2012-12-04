#!/bin/sh

which java > /dev/null 2>&1
if [ $? -gt 0 ]; then

	echo "Configuring Ubuntu"
	apt-get -qq update > /dev/null 2>&1
	apt-get -qq install vim > /dev/null 2>&1
	# replace /etc/hosts
	cp -f /files/hosts /etc/hosts
	# passwordless ssh
	mkdir /root/.ssh
	echo "Host *
    StrictHostKeyChecking no" > ~/.ssh/config
 	ssh-keygen -q -t dsa -P '' -f ~/.ssh/id_dsa
	cat ~/.ssh/id_dsa.pub >> ~/.ssh/authorized_keys

	echo "Disabling IPv6"
	echo "net.ipv6.conf.all.disable_ipv6 = 1" | sudo tee -a /etc/sysctl.conf > /dev/null 2>&1
	echo "net.ipv6.conf.default.disable_ipv6 = 1" | sudo tee -a /etc/sysctl.conf > /dev/null 2>&1
	echo "net.ipv6.conf.lo.disable_ipv6 = 1" | sudo tee -a /etc/sysctl.conf > /dev/null 2>&1
	/sbin/sysctl -p > /dev/null 2>&1

	echo "Installing Oracle Java Runtime Environment"
	apt-get -qq update > /dev/null 2>&1
	wget -c --no-cookies --header "Cookie: gpw_e24=http%3A%2F%2Fwww.oracle.com%2F" "http://download.oracle.com/otn-pub/java/jdk/6u34-b04/jre-6u34-linux-x64.bin" --output-document="jre-6u34-linux-x64.bin" > /dev/null 2>&1
	chmod u+x jre-6u34-linux-x64.bin
	./jre-6u34-linux-x64.bin > /dev/null 2>&1
	mkdir -p /usr/lib/jvm
	mv jre1.6.0_34 /usr/lib/jvm/
	update-alternatives --install "/usr/bin/java" "java" "/usr/lib/jvm/jre1.6.0_34/bin/java" 1 > /dev/null 2>&1
	update-alternatives --install "/usr/bin/javaws" "javaws" "/usr/lib/jvm/jre1.6.0_34/bin/javaws" 1 > /dev/null 2>&1

	echo "Downloading Apache Hadoop 1.0.4 from http://mirror.switch.ch/mirror/apache/dist/"
	wget -q  http://mirror.switch.ch/mirror/apache/dist/hadoop/common/hadoop-1.0.4/hadoop-1.0.4.tar.gz
	echo "Untaring Hadoop tarball"
	tar -zxf hadoop-1.0.4.tar.gz
	echo "Configuring Hadoop"
	mkdir -p /data/hadoop
	# overwrite conf files
	cp -rf /files/hadoop-conf/* /home/vagrant/hadoop-1.0.4/conf/ > /dev/null 2>&1
	# format namenode dir
	./hadoop-1.0.4/bin/hadoop namenode -format  > /dev/null 2>&1
	rm hadoop-1.0.4.tar.gz
	echo "Starting Hadoop"
	./hadoop-1.0.4/bin/start-all.sh

	echo "Downloading Apache HBase 0.94.2 from http://mirror.switch.ch/mirror/apache/dist/"
	wget -q http://mirror.switch.ch/mirror/apache/dist/hbase/hbase-0.94.2/hbase-0.94.2.tar.gz
	echo "Untaring HBase tarball"
	tar -zxf hbase-0.94.2.tar.gz
	echo "Configuring HBase"
	mkdir /data/hbase
	mkdir /data/zookeeper
	# overwrite conf files
	cp -rf /files/hbase-conf/* /home/vagrant/hbase-0.94.2/conf/ > /dev/null 2>&1
	rm hbase-0.94.2.tar.gz
	echo "Starting HBase"
	./hbase-0.94.2/bin/start-hbase.sh
fi

echo "Ready!"
