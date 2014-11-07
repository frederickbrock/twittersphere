package com.jiggysoftware.mobile.twittersphere;

import java.util.List;
import java.util.concurrent.TimeUnit;

import android.os.Handler;
import android.os.RemoteException;

import com.jiggysoftware.mobile.twittersphere.data.TwitterException;
import com.jiggysoftware.mobile.twittersphere.om.Tweet;
import com.jiggysoftware.mobile.twittersphere.service.TwitterContentService;
import com.jiggysoftware.mobile.twittersphere.service.TwitterContentServiceCallback;

public class TwitterUpdateServiceStubImpl extends TwitterContentService.Stub {

	protected TwitterUpdateService service;
	private TwitterFriendsSyncTask friendSyncTask = new TwitterFriendsSyncTask();
	private TweetSyncTask tweetSyncTask = new TweetSyncTask();
	protected Handler handler;

	protected TwitterUpdateServiceStubImpl(TwitterUpdateService service) {
		this.service = service;
		this.handler = service.getServiceMessageHandler();
	}

	public void follow(String screen_name) throws RemoteException {
		try {
			service.getTwitterService().befriend(screen_name);
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void registerCallback(TwitterContentServiceCallback callback)
			throws RemoteException {
		if (callback != null) {
			service.getListeners().register(callback);
		}
	}

	public void unregisterCallback(TwitterContentServiceCallback callback)
			throws RemoteException {
		if (callback != null) {
			service.getListeners().unregister(callback);
		}
	}

	public void remove(String screen_name) throws RemoteException {
		try {
			service.getTwitterService().breakFriendship(screen_name);
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public void updateFriendsList() throws RemoteException {
		// TODO Auto-generated method stub
		friendSyncTask.context = this.service;
		friendSyncTask.handler = this.handler;
		service.getTaskQueue().schedule(friendSyncTask, 1, TimeUnit.SECONDS);
	}

	
	public void updateRecentTweets() throws RemoteException {
		tweetSyncTask.updateService = service;
		tweetSyncTask.handler = this.handler;
		service.getTaskQueue().schedule(tweetSyncTask, 1, TimeUnit.SECONDS);
	}


	public void clearAndRefreshCache() throws RemoteException {
		synchronized (service.getRecentTweets()) {
			service.getRecentTweets().clear();
		}
	}


	public List<Tweet> getRecentTweets() throws RemoteException {
		return(service.getRecentTweets());
	}
}
