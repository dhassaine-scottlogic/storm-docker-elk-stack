package com.microsoft.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import backtype.storm.metric.api.CountMetric;
import backtype.storm.task.TopologyContext;

@SuppressWarnings("serial")
public class WordCount extends BaseBasicBolt {
	private static final Logger LOG = LoggerFactory.getLogger(WordCount.class);

	transient private CountMetric countMetric; // used for summary statistics
												// reported in Nimbus UI console

	Map<String, Integer> counts = new HashMap<String, Integer>();

	@Override
	public void execute(Tuple tuple, BasicOutputCollector collector) {
		countMetric.incr(); // log call
		String word = tuple.getString(0);
		int count = counts.merge(word, 1, (oldValue, increment) -> oldValue + increment);

		WordCountLogger.EVENT("WORD-COUNTER", word, count);
		collector.emit(new Values(word, count));
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("word", "count"));
	}

	@Override
	public void prepare(Map conf, TopologyContext context) {
		countMetric = new CountMetric();
		context.registerMetric("WORD_COUNT", countMetric, 60);
	}
}