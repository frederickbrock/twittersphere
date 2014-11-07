package com.jiggysoftware.mobile.twittersphere;

import static com.jiggysoftware.mobile.twittersphere.Constants.TASK_ID;
import static com.jiggysoftware.mobile.twittersphere.Constants.TWEET;
import static com.jiggysoftware.mobile.twittersphere.Constants.TWEET_REC;

import java.util.List;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.jiggysoftware.mobile.twittersphere.data.TwitterService;
import com.jiggysoftware.mobile.twittersphere.om.Tweet;

class TweetSyncTask implements Runnable {

	protected TwitterUpdateService updateService;
	protected Handler handler;
	protected static final String TAG = TweetSyncTask.class.getSimpleName();

	
	public void run() {
	    try {
		TwitterService twitterService = updateService.getTwitterService();
		Handler target = updateService.getServiceMessageHandler();
		List<TwitterService.Status> statusList = twitterService.getFriendsTimeline();
		for (TwitterService.Status status : statusList) {
		    Log.d(TAG, "processing status: " + status.id);
		    Tweet tweet = new Tweet();
		    tweet.setCreatedAt(status.createdAt.toGMTString());
		    tweet.setTwitter_id(Integer.toString(status.user.id));
		    tweet.setStatus_id(Long.toString(status.id));
		    tweet.setText(status.text);
		    tweet.setAvatar_url(status.getUser().profileImageUrl.toASCIIString());
		    tweet.setScreen_name(status.getUser().screenName);
		    tweet.setReplyTo(1);
		    Message message = new Message();
		    message.getData().putParcelable(TWEET, tweet);
		    message.getData().putInt(TASK_ID, TWEET_REC);
		    message.setTarget(target);
		    message.sendToTarget();
		}
	    } catch (Exception e) {
		Log.e("TCFS", "excception while syncing friends", e);
	    }

	}
}
