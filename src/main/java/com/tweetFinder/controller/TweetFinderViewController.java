package com.tweetFinder.controller;

import java.util.LinkedHashMap;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.tweetFinder.model.Data;
import com.tweetFinder.model.Tweet;
import com.tweetFinder.model.TweetData;
import com.tweetFinder.services.KafkaSender;
import com.tweetFinder.services.TweetServices;

/**
 * The TweetFinderController is a controller class which is responsible 
 * for processing incoming REST API requests, preparing a model and
 * return the view to be rendered
 * 
 * @author dineshkh
 *
 */

@Controller
@RequestMapping("/tweetFinder")
public class TweetFinderViewController {

	@Autowired
	TweetServices service;
	@Autowired
	KafkaSender kafkaSender;
	
	@Value("${authorization}")
	String authorization;
	
	/**
	 * This handler method is responsible for displaying
	 * the AfterLogin.html page after successfull login
	 * 
	 * @return returns a view which is resolved to AfterLogin
	 */
	
	@RequestMapping("/")
	public String login() {
		return "AfterLogin";
	}
	
	/**
	 * This method is responsible for displaying
	 * all the tweets available in the H2 database
	 * 
	 * @param model
	 * @return returns a view which is resolved to AfterLogin.html
	 */
	
	@RequestMapping("/tweet/")
	public String viewAllTweets(Model model) {
		List<Tweet> listTweet = service.listAll();
		model.addAttribute("listTweet", listTweet);
		return "index";
	}
	
	/**
	 * The /tweetFinder/tweet URL is mapped to searchTweets() method.
	 * It is invoked when the user sends a hashTag as a paramter
	 * for which the tweets needs to be fetched. This method sends a redirect to searchTweets()
	 * method defined below.
	 * 
	 * @param model
	 * @param hashTag this parameter contains the hashTag value user wants to search.
	 * @return A redirect URL /tweetFinder/tweet/{hashTag} mapped to searchTweets()
	 */
	
	@RequestMapping("/tweet")
	public String searchTweet(Model model, @RequestParam("hashTag") String hashTag) {
		
		return "redirect:/tweetFinder/tweet/"+hashTag;
		
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
	private String searchTweets(Model model, @PathVariable("hashtag") String hashtag) {
		
		String url = "https://api.twitter.com/2/tweets/search/recent?query=";
		url = url + hashtag;
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization","Bearer " + authorization);
		HttpEntity request = new HttpEntity(headers);
		ResponseEntity<LinkedHashMap> responseEntity = restTemplate.exchange(url, HttpMethod.GET, request,
				LinkedHashMap.class);
		LinkedHashMap<String, List> tweets = responseEntity.getBody();
		

		String jsonString = new JSONObject(tweets).toString();
		
		Data data = new Gson().fromJson(jsonString, Data.class);
		
		List<TweetData> tweetData = data.getData();
		for(int i = 0; i<tweetData.size(); i++) {
			String id = tweetData.get(i).getId();
			String text = tweetData.get(i).getText();
			Tweet t = new Tweet();
			t.setId(id);
			t.setText(text);
			t.setHashTag(hashtag);
			kafkaSender.send(t);
		}


		List<Tweet> listTweets = service.listByHashTag(hashtag);

		model.addAttribute("listTweet", listTweets);
		return "index";

	}
}
