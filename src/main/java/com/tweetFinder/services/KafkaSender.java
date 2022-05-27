package com.tweetFinder.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.tweetFinder.model.Tweet;

/**
 * This class acts as a kafka producer that helps in sending the outgoing
 * records to topic of a kafka cluster. 
 * 
 * @author dineshkh
 *
 */
@Service
public class KafkaSender {
	
	// Search kafka template and write the desciption at kafka template.
	@Autowired
	private KafkaTemplate<String, Tweet> kafkaTemplate;
	
	//This is the topic name which is declared in application.properties.
	@Value("${spring.kafka.topics}")
	String kafkaTopic;
	
	/**
	 * This is the method which calls the .send() method of Kakfa template
	 * which sends the Tweet record to kafka topic.
	 * 
	 * @param tweet
	 */
	public void send(Tweet tweet) {
	    
	    kafkaTemplate.send(kafkaTopic, tweet);
	}
}
