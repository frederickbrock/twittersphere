package com.jiggysoftware.mobile.twittersphere.data;

public class TwitterException extends Exception {
	
	public TwitterException(){
		super();
	}
	
	public TwitterException(Exception e){
		super(e);
	}
	
	public TwitterException(String s){
		super(s);
	}

}
