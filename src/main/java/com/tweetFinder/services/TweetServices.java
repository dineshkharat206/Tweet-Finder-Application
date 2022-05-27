package com.tweetFinder.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tweetFinder.model.Tweet;
import com.tweetFinder.repository.TweetFinderRepository;

/**
 * This class acts as a service class it has methods ehich is used for
 * sending the data to the database using the method of
 * TwitterFinderRepository.
 * 
 * @author dineshkh
 *
 */

@Service
public class TweetServices {

	@Autowired
	private TweetFinderRepository tweetFinderRepo;
	
	/**
	 * This method is used to fetch all the tweets
	 * from the database by calling the findAll() method of
	 * JpaRepository.
	 * 
	 * @return List<Tweet> : returns the List of Tweet from the database.
	 */
	
	public List<Tweet> listAll(){
		return tweetFinderRepo.findAll();
	}
	
	/**
	 * This method is used to fetch the user provided hashTag from the database.
	 * Using the method findTweetByHashTag() declared in the TweetFinderRepository interface.
	 * 
	 * @param hashTag
	 * @return
	 */
	
	public List<Tweet> listByHashTag(String hashTag){
		
		return tweetFinderRepo.findTweetByHashTag(hashTag);
	}
	
}

