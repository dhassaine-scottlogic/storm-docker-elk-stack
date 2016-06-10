# Storm docker ELK stack example

This is a skeleton project derived from [azure.microsoft.com](https://azure.microsoft.com/en-us/documentation/articles/hdinsight-storm-develop-java-topology/) running the tutorial storm topology WordCount on docker machines. It integrates with the ELK stack to display logs from the storm topology in Kibana.

The ELK stack is composed of the following three tools:
* ElasticSearch is a distributed database.
* Logstash collects data, filters, transforms, parses it into json documents and saves it to an Elasticsearch cluster.
* Kibana is a broswer-based anlytics and search interface for Elasticsearch.

## Prerequisites
To run this project you need maven installed to package the Java 8 and Vagrant and Virtual box installed and configured.

To set up vagrant and virtual box you can follow this guide https://www.vagrantup.com/docs/getting-started/

## Set up
1. clone this repository
2. Navigate to WordCount and run `mvn clean package` to build the topology jar with dependencies

## Cluster launch
3. from the root folder run `vagrant up`
4. If the topology fails to start run vagrant provision until it does (needs fixing)
5. Once all the docker containers are successfully running on the VM you can import the dashboard configurations from /config/kibana/dashboard.json

## Local launch
To quckly see a locally launched topology, from the Command line run:
```
mvn compile exec:java -Dstorm.topology=com.microsoft.example.WordCountTopology
```
Alternatively if you have imported the maven project into Eclipse, you can right click on WordCountTopology and
Run as->Java Application

## Topology
This example program emits sentences randomly chosen from a small dataset which are then piped to a word splitting process and then finally to a word counting process.
![Overview](https://acom.azurecomcdn.net/80C57D/cdn/mediahandler/docarticles/dpsmedia-prod/azure.microsoft.com/en-us/documentation/articles/hdinsight-storm-develop-java-topology/20160415071221/wordcount-topology.png)


## Services
Vargant creates the virtual cloud on it owns private network 10.10.20.10
# Storm UI
This gives the overview of the storm processes and can be accessed via: http://10.10.20.10:8080/
The following link help explain the UI: [Storm UI explained](http://www.malinga.me/reading-and-understanding-the-storm-ui-storm-ui-explained/)

## Glossary
* Storm: a distributed and fault tolerant computation framework for event stream processing.
* Topology: A directed acyclic graph (DAG) of nodes and links showing the flow of computation, e.g. the data sources and how that data flows through other nodes.
* Node: A set of operations to perform on incoming data.
* Link: Indicates data passing between two nodes.

## Team members
* Alexander Cheshire
* Djamel Hassaine