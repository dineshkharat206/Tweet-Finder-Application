package com.tweetFinder.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * This is a POJO class which has an @Entity annotation which is useful for
 * mapping the class to the database table.
 * 
 * @author dineshkh
 *
 */

@Entity
public class Tweet {
	
	// This variable has an @Id annotation which acts as a primary key for the Database table.
	@Id
	private String id;
	
	// This is the tweet text corresponding to the hashTag.
	@Column(name = "TEXT", nullable = false, length = 1000)
	private String text;
	
	// This is the hashTag.
	private String hashTag;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getHashTag() {
		return hashTag;
	}
	public void setHashTag(String hashTag) {
		this.hashTag = hashTag;
	}
	@Override
	public String toString() {
		return "Tweet [id=" + id + ", text=" + text + ", hashTag=" + hashTag + "]";
	}
	
}

