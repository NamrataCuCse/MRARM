# MRARM
A MapReduce-Based Association Rule Mining Using Hadoop Cluster—An Application of Disease Analysis

## Cite:
Bhattacharya N., Mondal S., Khatua S. (2019) A MapReduce-Based Association Rule Mining Using Hadoop Cluster—An Application of Disease Analysis. In: Saini H., Sayal R., Govardhan A., Buyya R. (eds) Innovations in Computer Science and Engineering. Lecture Notes in Networks and Systems, vol 74. Springer, Singapore


## Prerequisites:

The scripts run on CentOS 7 on a 64Bit machine with JDK version "1.8.0_131". We have used Hadoop version 2.7.2.



### CREATE HADOOP USER :
Create a system user account on both master and slave systems to use for Hadoop installation -
```bash
# useradd hadoop
# passwd hadoop
```

### ADD FQDN MAPPING :
Edit /etc/hosts file on all master and slave servers and add following entries.
```bash
# vim /etc/hosts
10.0.0.1 NameNode
10.0.100.1 DataNode1
10.0.100.2 DataNode2 
```

### CONFIGURING KEY BASED LOGIN :
It’s required to set up Hadoop user to ssh itself without password. Use following commands to configure auto login between all Hadoop cluster servers.
```bash
# su - hadoop
$ ssh-keygen -t rsa
$ ssh-copy-id -i ~/.ssh/id_rsa.pub hadoop@NameNode
$ ssh-copy-id -i ~/.ssh/id_rsa.pub hadoop@DataNode1
$ ssh-copy-id -i ~/.ssh/id_rsa.pub hadoop@DataNode2
$ chmod 0600 ~/.ssh/authorized_keys
$ exit
```

### DOWNLOAD AND EXTRACT HADOOP SOURCE
Download Hadoop latest available version from its official site at Hadoopmaster server only.
```bash
# mkdir /opt/hadoop
# cd /opt/hadoop/
# wget http://www.us.apache.org/dist/hadoop/common/hadoop-2.7.2/hadoop-2.7.2.tar.gz
# tar -xzvf hadoop-2.7.2.tar.gz
# mv hadoop-2.7.2 hadoop
# chown -R hadoop /opt/hadoop
# cd /opt/hadoop/hadoop/
```

### CONFIGURE HADOOP:
First edit Hadoop configuration files and make following changes.

**Edit core-site.xml**
```bash
# vim conf/core-site.xml
<?xml version="1.0"?>
<!-- core-site.xml -->
<configuration>
<property>
<name>fs.defaultFS</name>
<value>hdfs://NameNode:8020/</value>
</property>
<property>
<name>io.file.buffer.size</name>
<value>131072</value>
</property>
</configuration>
```

**Edit hdfs-site.xml**
```bash
<?xml version="1.0"?>
<!-- hdfs-site.xml -->
<configuration>
<property>
<name>dfs.namenode.name.dir</name>
<value>file:/usr/local/hadoop_work/hdfs/namenode</value>
</property>
<property>
<name>dfs.datanode.data.dir</name>
<value>file:/usr/local/hadoop_work/hdfs/datanode</value>
</property>
<property>
<name>dfs.namenode.checkpoint.dir</name>
<value>file:/usr/local/hadoop_work/hdfs/namesecondary</value>
</property>
<property>
<name>dfs.replication</name>
<value>3</value>
</property>
<property>
<name>dfs.block.size</name>
<value>134217728</value>
</property>
</configuration>
```

**Edit mapred-site.xml**
```bash
<?xml version="1.0"?>
<!-- mapred-site.xml -->
<configuration>
<property>
  <name>mapreduce.framework.name</name>
<value>yarn</value>
</property>
<property>
<name>mapreduce.jobhistory.address</name>
<value>NameNode:10020</value>
</property>
<property>
<name>mapreduce.jobhistory.webapp.address</name>
<value>NameNode:19888</value>
</property>
<property>
<name>yarn.app.mapreduce.am.staging-dir</name>
<value>/user/app</value>
</property>
<property>
<name>mapred.child.java.opts</name>
<value>-Djava.security.egd=file:/dev/../dev/urandom</value>
</property>
</configuration>
```

**Configure yarn-site.xml**
```bash
<?xml version="1.0"?>
<!-- yarn-site.xml -->
<configuration>
<property>
<name>yarn.resourcemanager.hostname</name>
<value>NameNode</value>
</property>
<property>
<name>yarn.resourcemanager.bind-host</name>
<value>0.0.0.0</value>
</property>
<property>
<name>yarn.nodemanager.bind-host</name>
<value>0.0.0.0</value>
</property>
<property>
<name>yarn.nodemanager.aux-services</name>
<value>mapreduce_shuffle</value>
</property>
<property>
<name>yarn.nodemanager.aux-services.mapreduce_shuffle.class</name>
<value>org.apache.hadoop.mapred.ShuffleHandler</value>
</property>
<property>
<name>yarn.log-aggregation-enable</name>
<value>true</value>
</property>
<property>
<name>yarn.nodemanager.local-dirs</name>
<value>file:/usr/local/hadoop_work/yarn/local</value>
</property>
<property>
<name>yarn.nodemanager.log-dirs</name>
<value>file:/usr/local/hadoop_work/yarn/log</value>
</property>
<property>
<name>yarn.nodemanager.remote-app-log-dir</name>
<value>hdfs://NameNode:8020/var/log/hadoop-yarn/apps</value>
</property>
</configuration>
```

### COPY HADOOP SOURCE TO SLAVE SERVERS
After updating above configuration, we need to copy the source files to all slaves servers.
```bash
# su - hadoop
$ cd /opt/hadoop
$ scp -r hadoop DataNode1:/opt/hadoop
$ scp -r hadoop DataNode2:/opt/hadoop
```

### CONFIGURE HADOOP ON MASTER SERVER ONLY
Go to hadoop source folder on hadoop-master and do following settings.
```bash
# su - hadoop
$ cd /opt/hadoop/hadoop
$ vim conf/masters
NameNode
$ vim conf/slaves
DataNode1
DataNode2
```

### FORMAT NAME NODE ON HADOOP MASTER ONLY
```bash
# su - hadoop
$ cd /opt/hadoop/hadoop
$ bin/hadoop namenode -format
```

### START HADOOP SERVICES
Use the following command to start all Hadoop services on Hadoop-Master
```bash
$ bin/start-all.sh
```

### RUN JAR FILE
Now we run a JAR file where the input is given as-
```bash
hadoop jar jarfilename.jar packageName.ClassName PathToInputTextFile PathToOutputDirectry
```

## Usage: 
```bash
hadoop jar MRapriori.jar apriori.AprioriDriver input.csv output
```

