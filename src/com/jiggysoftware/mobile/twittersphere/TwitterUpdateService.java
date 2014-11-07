package com.jiggysoftware.mobile.twittersphere;

import java.util.ArrayList;
import java.util.Stack;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.jiggysoftware.mobile.twittersphere.om.Tweet;
import com.jiggysoftware.mobile.twittersphere.service.TwitterContentServiceCallback;
import com.jiggysoftware.mobile.twittersphere.service.TwitterContentService;
import com.jiggysoftware.mobile.twittersphere.data.TwitterDbAdapter;
import com.jiggysoftware.mobile.twittersphere.data.TwitterService;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.util.Log;

public class TwitterUpdateService extends Service {

	private static final String TAG = TwitterUpdateService.class.getSimpleName();
	/**
	 * the actual interface to twitter
	 */
	private TwitterService twitterService = null;
	/**
	 * a db adapter that I am currently not using and probally won't TODO:
	 * remove this shit.
	 */
	private TwitterDbAdapter dbAdapter = new TwitterDbAdapter(this);
	/**
	 * a task schedule queue.
	 */
	private final ScheduledExecutorService taskQueue = Executors.newScheduledThreadPool(3);
	/**
	 * a list of callback interfaces back to the land of UI. Need to change this
	 * to use the RemoteCallback<s> type in android
	 */
	private final RemoteCallbackList<TwitterContentServiceCallback> listeners = 
								new RemoteCallbackList<TwitterContentServiceCallback>();
	/**
	 * the avatar download queue..also need a job to periodically clean up
	 * unused avatars or old avatars how ever thats going to be defined.
	 */
	private final ArrayBlockingQueue<Tweet> avatarDownloadQueue = new ArrayBlockingQueue<Tweet>(50);
	/**
	 * the single download queue.
	 */
	protected final DownloaderTask downloader = new DownloaderTask();
	/**
	 * the clients view of this service;
	 */
	private TwitterContentService.Stub contentServiceImpl = new TwitterUpdateServiceStubImpl(this);
	/**
	 * the route from the service impl to the up. This impl handles pushing
	 * messages to the ui and the client interface
	 */
	private ServiceMessageHandler serviceMessageHandler = new ServiceMessageHandler(this);
	/**
	 * the recent tweet cache.
	 */
	private Stack<Tweet> recentTweets = new Stack<Tweet>();
	/**
	 * the maximum size of the tweet cache. This needs to come from shared
	 * prefs.
	 */
	private int maxCacheSize = 50;
	/**
	 * am I running. This needs to be expanded on to stop the download task.
	 */
	boolean serviceStarted;

	/**
	 * checks the tweet cache for the input tweet and returns true if it already
	 * exists!.
	 * 
	 * @param it
	 *            the tweet to look for.
	 * @return true if the tweet exist already in the cache.
	 */
	protected boolean hasTweet(Tweet it) {

		for (Tweet t : recentTweets) {
			if (t.getStatus_id().equals(it.getStatus_id())) {
				return (true);
			}
		}
		return (false);
	}

	/**
	 * checks the tweet cache and pushes the tweet on the cache if it doesn't
	 * already exist.
	 * 
	 * @param t
	 */
	protected void push(Tweet t) {
		try {
			if (!hasTweet(t)) {
				if (recentTweets.size() > maxCacheSize) {
					recentTweets.remove(recentTweets.lastElement());
					recentTweets.trimToSize();
				}
				recentTweets.push(t);
			}
		} catch (Exception e) {
			Log.e(TAG, "exception while attempting to push tweet on the stack",
					e);
		}
	}

	public ArrayList<Tweet> getRecentTweets() {
		Tweet[] rc = new Tweet[recentTweets.size()];
		rc = (Tweet[]) recentTweets.toArray(rc);
		ArrayList<Tweet> ra = new ArrayList<Tweet>();
		for (int idx = 0; idx < rc.length; ++idx) {
			ra.add(rc[idx]);
		}
		return (ra);
	}

	
	public boolean isServiceStarted() {
		return serviceStarted;
	}

	public void setServiceStarted(boolean serviceStarted) {
		this.serviceStarted = serviceStarted;
	}

	public TwitterService getTwitterService() {
		if (twitterService == null) {
			SharedPreferences prefs = getSharedPreferences("TWITTERSPHERE",	MODE_PRIVATE);
			String userName = prefs.getString("twitter.username", "");
			String password = prefs.getString("twitter.password", "");
			// if((!userName.equals("")) && (!password.equals(""))){
			// FIXME: need prefs to replace this
			if (true) {
				//need to add the auth user call to TwitterService. It simply assumes you 
				//can log in and the user does not make a mistake
				twitterService = new TwitterService("frederickbrock", "gqNw644M");
			} else {
				// TODO: Handle this better, if I can't get logged into twitter
				// then Im screwed
				// maybe the LoginActivity can be modified to continuously
				// prompt.
				Log.d("TWITTERUPDATESERVICE", "returning null");
				return (null);
			}
		}
		return twitterService;
	}

	public void setTwitterService(TwitterService twitterService) {
		this.twitterService = twitterService;
	}

	public TwitterDbAdapter getDbAdapter() {
		return dbAdapter;
	}

	public void setDbAdapter(TwitterDbAdapter dbAdapter) {
		this.dbAdapter = dbAdapter;
	}

	public ScheduledExecutorService getTaskQueue() {
		return taskQueue;
	}

	public RemoteCallbackList<TwitterContentServiceCallback> getListeners() {
		return listeners;
	}

	public ArrayBlockingQueue<Tweet> getAvatarDownloadQueue() {
		return avatarDownloadQueue;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return (contentServiceImpl.asBinder());
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(Constants.TAG, "service started");
		serviceStarted = true;
		downloader.context = this;
		downloader.start();

		TwitterFriendsSyncTask task = new TwitterFriendsSyncTask();
		task.context = this;
		task.handler = this.getServiceMessageHandler();
		getTaskQueue().scheduleAtFixedRate(task, 0, 60 * 60, TimeUnit.SECONDS);

		TweetSyncTask ts = new TweetSyncTask();
		ts.updateService = this;
		ts.handler = getServiceMessageHandler();
		getTaskQueue().scheduleAtFixedRate(ts, 5, 3 * 60, TimeUnit.SECONDS);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		downloader.stop();
		getTaskQueue().shutdown();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

	@Override
	public void onRebind(Intent intent) {
		super.onRebind(intent);
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		Log.d(Constants.TAG, "service started");
		serviceStarted = true;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		return super.onUnbind(intent);
	}

	public void setServiceMessageHandler(
			ServiceMessageHandler serviceMessageHandler) {
		this.serviceMessageHandler = serviceMessageHandler;
	}

	public ServiceMessageHandler getServiceMessageHandler() {
		return serviceMessageHandler;
	}

}
