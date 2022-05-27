package com.tweetFinder.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tweetFinder.model.Tweet;

/**
 * This is interface which extends JpaRepository<T,V>.
 * So it contains API for basic CRUD operations.
 * 
 * @author dineshkh
 *
 */

public interface TweetFinderRepository extends JpaRepository<Tweet, Integer>{

	/**
	 * This method declaration has a Query annotation which is used to define a
	 * query to search the tweets with matching hashTag.
	 * 
	 * @param HashTag : This is the hashTag for which the tweets needs
	 * to be fetched from the database.
	 *  
	 * @return List<Tweet> : this method declaraton has a return type of List of Tweet.
	 */
	@Query("SELECT t FROM Tweet t WHERE t.hashTag = ?1")
	List<Tweet> findTweetByHashTag(String HashTag);
	
}
