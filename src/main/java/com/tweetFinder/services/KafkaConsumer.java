package com.tweetFinder.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tweetFinder.model.Tweet;

import lombok.extern.slf4j.Slf4j;

/**
 * This is a kafka Consumer class which consumes or
 * reads data from the kafka cluster via a topic.
 * 
 * @author dineshkh
 *
 */

@Component
@Slf4j
public class KafkaConsumer {
	
	@Autowired
	KafkaConsumerService consumerService;
	
	/**
	 * This method reads the data from the topic. The topic name is mentioned
	 * in the application.properties file and it is read using the @KafkaListener annotation.
	 * It sends the record to process method of KafkaConsumerService class
	 * 
	 * @param record : This contains the Tweet id,text and hashTag
	 * @throws JsonMappingException : This is used to signal fatal problems with mapping of content.
	 * @throws JsonProcessingException
	 */
	@KafkaListener(topics= "${spring.kafka.topics}")
	public void consumeTweetFromTopic(Tweet record) throws JsonMappingException, JsonProcessingException {
		 
		log.info("Consumer record" + record);
		consumerService.process(record);
		
	}
}
