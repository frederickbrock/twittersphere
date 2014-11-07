package com.jiggysoftware.mobile.twittersphere.service;

import com.jiggysoftware.mobile.twittersphere.service.TwitterContentServiceCallback;
import com.jiggysoftware.mobile.twittersphere.om.Tweet;

interface TwitterContentService {

	void updateRecentTweets();
	void updateFriendsList();
	void follow(String screen_name);
	void remove(String screen_name);
	void registerCallback(TwitterContentServiceCallback callback);
	void unregisterCallback(TwitterContentServiceCallback callback);
	void clearAndRefreshCache();
	List<Tweet> getRecentTweets();
}


