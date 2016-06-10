package com.microsoft.example;

import java.text.BreakIterator;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

/*
 * Emits a word event for each word in an input sentence
 */
@SuppressWarnings("serial")
public class SplitSentence extends BaseBasicBolt {
	private static final Logger LOG = LoggerFactory.getLogger(SplitSentence.class);

	@Override
	public void execute(Tuple tuple, BasicOutputCollector collector) {
		String sentence = tuple.getString(0);
		Arrays.asList(sentence.split("\\s+")).forEach(word -> emitWord(collector, word));
	}

	private void emitWord(BasicOutputCollector collector, String word) {
		if (!word.equals("")) {
			collector.emit(new Values(word));
			WordCountLogger.EVENT("EMITTING-WORD", word);
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("word"));
	}
}