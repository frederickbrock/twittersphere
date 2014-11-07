package com.jiggysoftware.mobile.twittersphere.service;

import com.jiggysoftware.mobile.twittersphere.om.Tweet;

interface TwitterContentServiceCallback {

	void onAvatarDownloadProgress(String twitter_id, int progressType, String url, int totalSize,int totalRead);
	void onNewTweetRecieved(in Tweet tweet);
	void onNewFriendAdded(String twitter_id);
	void onTwitterUpdateBegin();
	void onTwitterUpdateComplete();
}

