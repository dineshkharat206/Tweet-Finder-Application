package com.tweetFinder.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.aspectj.apache.bcel.generic.MULTIANEWARRAY;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.tweetFinder.model.Data;
import com.tweetFinder.model.Tweet;
import com.tweetFinder.model.TweetData;
import com.tweetFinder.services.KafkaSender;
import com.tweetFinder.services.TweetServices;

@RestController
@RequestMapping("/restTweetFinder")
public class TweetFinderRestController {
	@Autowired
	TweetServices service;
	@Autowired
	KafkaSender kafkaSender;
	
	@Value("${authorization}")
	String authorization;
	
	
	/**
	 * This method is responsible for displaying
	 * all the tweets available in the H2 database
	 * 
	 * @param model
	 * @return returns a view which is resolved to AfterLogin.html
	 */
	
	@RequestMapping("/tweet/")
	public List<Tweet> viewAllTweets() {
		List<Tweet> listTweet = service.listAll();
		return listTweet;
	}
	
	/**
	 * This method reads the input hashTag.
	 * A call is made to the twitter API endpoint
	 * and tweets are fetched and sent to kafka topic, then from Kafka topics the  
	 * data is consumed and saved in the Database and then tweets are rendered as a model
	 * 
	 * @param model
	 * @param hashTag : This is the hashtag provided by the user
	 * @return Returns a view which is resolved to index.html
	 */
	
	@RequestMapping("/tweet/{hashtag}")
	public ResponseEntity<List<Tweet>> searchTweets(@PathVariable("hashtag") String hashtag) {
		List<Tweet> listTweets = service.listByHashTag(hashtag);
		
		String url = "https://api.twitter.com/2/tweets/search/recent?query=";
		url = url + hashtag;
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+authorization);
		HttpEntity request = new HttpEntity(headers);
		ResponseEntity<LinkedHashMap> responseEntity = restTemplate.exchange(url, HttpMethod.GET, request,
				LinkedHashMap.class);
		LinkedHashMap<String, List> tweets = responseEntity.getBody();

		String jsonString = new JSONObject(tweets).toString();
		
		Data data = new Gson().fromJson(jsonString, Data.class);
		
		List<TweetData> tweetData = data.getData();
		listTweets = new ArrayList<>();
		for(int i = 0; i<tweetData.size(); i++) {
			String id = tweetData.get(i).getId();
			String text = tweetData.get(i).getText();
			Tweet t = new Tweet();
			t.setId(id);
			t.setText(text);
			t.setHashTag(hashtag);
			kafkaSender.send(t);
			listTweets.add(t);
		} 

		return ResponseEntity.ok(listTweets);

	}
}
