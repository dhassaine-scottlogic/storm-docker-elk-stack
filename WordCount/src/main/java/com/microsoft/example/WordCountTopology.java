package com.microsoft.example;

import java.util.Map;

import org.apache.thrift7.TException;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import backtype.storm.generated.KillOptions;
import backtype.storm.generated.Nimbus.Client;
import backtype.storm.generated.NotAliveException;
import backtype.storm.generated.StormTopology;
import backtype.storm.utils.NimbusClient;
import backtype.storm.utils.Utils;

import com.microsoft.example.MetricsConsumer;

public class WordCountTopology {
	// private
	public static void main(String[] args) throws Exception {
		if (args != null && args.length > 0) {
			launchCluster(args);
		} else {
			launchLocally();
		}
	}

	private static void launchCluster(String[] args) throws Exception {		
		  Config conf = new Config(); 
		  conf.setDebug(false);
		  conf.registerMetricsConsumer(MetricsConsumer.class, 1);
		  conf.setNumWorkers(4);
		  
		  String topologyName = args[0];
		  killExistingTopology(topologyName);
		  StormSubmitter.submitTopology(topologyName, conf, makeTopology());
		 
	}
	
	private static void killExistingTopology(String topologyName) {
		Client client = NimbusClient.getConfiguredClient(Utils.readStormConfig()).getClient(); 
		  KillOptions killOpts = new KillOptions(); 
		  killOpts.set_wait_secs(0);
		  try {
			client.killTopologyWithOpts(topologyName, killOpts);
		} catch (NotAliveException e) {
			// deliberately ignore this exception -- topology didn't exist therefore we can't kill it
		} catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void launchLocally() throws InterruptedException {
		Config conf = new Config();
		conf.setDebug(true);
		conf.setMaxTaskParallelism(3);
		LocalCluster cluster = new LocalCluster();
		String topologyName = "word-count-topology";
		cluster.submitTopology(topologyName, conf, makeTopology());
		Thread.sleep(100000);
		cluster.killTopology(topologyName);
		cluster.shutdown();
	}

	private static StormTopology makeTopology() {
		// Topology: data | split | count
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("spout", new RandomSentenceSpout());

		// shuffleGrouping(): equally distribute sentences
		builder.setBolt("split", new SplitSentence()).shuffleGrouping("spout");

		// fieldsGrouping(): associate each word with a specific bolt
		builder.setBolt("count", new WordCount()).fieldsGrouping("split", new Fields("word"));

		return builder.createTopology();
	}
}