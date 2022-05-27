package com.tweetFinder.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tweetFinder.model.Tweet;
import com.tweetFinder.repository.TweetFinderRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * This class acts as a service class which deals with sending the data to the
 * database by calling the save function of JpaRepository.
 * 
 * @author dineshkh
 *
 */

@Service
@Slf4j
public class KafkaConsumerService {

	@Autowired
    ObjectMapper objectMapper;
	
	@Autowired
    private TweetFinderRepository tweetFinderRepo;
	
	/**
	 * This method sends the data to the database by calling the save()
	 * method from the JpaRepository.
	 * 
	 * @param record
	 * @throws JsonProcessingException
	 */
	public void process(Tweet record) throws JsonProcessingException {
        log.info("emp event" + record);
        tweetFinderRepo.save(record);
    }
	
}
