package com.jiggysoftware.mobile.twittersphere;

import java.util.List;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import static com.jiggysoftware.mobile.twittersphere.Constants.*;
import com.jiggysoftware.mobile.twittersphere.data.TwitterDbAdapter;
import com.jiggysoftware.mobile.twittersphere.data.TwitterService;

public class TwitterFriendsSyncTask implements Runnable {

    protected TwitterUpdateService context;
    protected Handler handler;

  
    public void run() {

	// down load friends...check to see if they exists, if they do,
	// check there status,
	try {
	    List<TwitterService.User> twitterFriends = context.getTwitterService().getFriends();
	    handler = context.getServiceMessageHandler();
	    TwitterDbAdapter dbAdapter = context.getDbAdapter();
	    for (TwitterService.User user : twitterFriends) {
		Log.d("TCSF", "processing friends: " + user.screenName);
		dbAdapter.createFriend(Integer.toString(user.id), user.screenName, user.profileImageUrl.toASCIIString(), user.name, user.description);
		Message m = new Message();
		m.getData().putInt(TASK_ID, NEWFRIEND_ADDED);
		m.getData().putString(TWITTER_ID, Integer.toString(user.id));
		m.getData().putString(AVATAR_URL, user.profileImageUrl.toASCIIString());
		m.setTarget(handler);
		m.sendToTarget();
	    }
	} catch (Exception e) {
	    Log.e("TCFS", "excception while syncing friends", e);
	}

    }
}
