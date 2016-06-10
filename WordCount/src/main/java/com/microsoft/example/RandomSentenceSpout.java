package com.microsoft.example;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

import java.util.Map;
import java.util.Random;

/**
 * 
 * Spout emits random sentences
 *
 */
@SuppressWarnings("serial")
public class RandomSentenceSpout extends BaseRichSpout {
  private SpoutOutputCollector collector;
  private Random randomGenerator;
  private static String[] sentences = new String[]{ "the cow jumped over the moon", "an apple a day keeps the doctor away",
          "four score and seven years ago", "snow white and the seven dwarfs", "i am at two with nature", "" };

  //Open is called when an instance of the class is created
  @Override
  public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
    this.collector = collector;
    randomGenerator = new Random();
  }

  //Emit data to the stream
  @Override
  public void nextTuple() {
      Utils.sleep(50);      
      String sentence = getRandomSentence();
      if (sentence != "") {
        WordCountLogger.EVENT("EMITTING-SENTENCE", sentence);
        collector.emit(new Values(sentence));
      } else {
        WordCountLogger.ERROR("EMPTY-SENTENCE");
      }
  }
  
  private String getRandomSentence() {
	  return sentences[randomGenerator.nextInt(sentences.length)];
  }
  
  //Ack is not implemented since this is a basic example
  @Override
  public void ack(Object id) {
  }

  @Override
  public void fail(Object id) {
  }
  
  //Fail is not implemented since this is a basic example
  @Override
  public void declareOutputFields(OutputFieldsDeclarer declarer) {
    declarer.declare(new Fields("sentence"));
  }
}