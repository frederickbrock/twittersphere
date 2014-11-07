package com.jiggysoftware.mobile.twittersphere;

import static com.jiggysoftware.mobile.twittersphere.Constants.AVATAR_DOWNLOAD_BEGIN;
import static com.jiggysoftware.mobile.twittersphere.Constants.AVATAR_TOTAL_READ;
import static com.jiggysoftware.mobile.twittersphere.Constants.AVATAR_TOTAL_SIZE;
import static com.jiggysoftware.mobile.twittersphere.Constants.AVATAR_URL;
import static com.jiggysoftware.mobile.twittersphere.Constants.DOWNLOAD_AVATAR;
import static com.jiggysoftware.mobile.twittersphere.Constants.NEWFRIEND_ADDED;
import static com.jiggysoftware.mobile.twittersphere.Constants.TASK_ID;
import static com.jiggysoftware.mobile.twittersphere.Constants.TASK_STAGE;
import static com.jiggysoftware.mobile.twittersphere.Constants.TWEET;
import static com.jiggysoftware.mobile.twittersphere.Constants.TWEET_REC;
import static com.jiggysoftware.mobile.twittersphere.Constants.TWITTER_ID;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReadWriteLock;

import com.jiggysoftware.mobile.twittersphere.om.Tweet;
import com.jiggysoftware.mobile.twittersphere.service.TwitterContentService;
import com.jiggysoftware.mobile.twittersphere.service.TwitterContentServiceCallback;

import android.app.Notification;
import android.app.NotificationManager;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

public class ServiceMessageHandler extends Handler {

	private TwitterUpdateService context;
	private Semaphore lock = new Semaphore(1);
	private static final String TAG = ServiceMessageHandler.class
			.getSimpleName();

	public ServiceMessageHandler(TwitterUpdateService service) {
		this.context = service;
	}

	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		Log.d("HANDLER", "in handleMessage");
		int taskId = msg.getData().getInt(TASK_ID);
		RemoteCallbackList<TwitterContentServiceCallback> listeners = context.getListeners();

		switch (taskId) {

		case DOWNLOAD_AVATAR:

			String twitter_id = msg.getData().getString(TWITTER_ID);
			String urlStr = msg.getData().getString(AVATAR_URL);
			int totalSize = msg.getData().getInt(AVATAR_TOTAL_SIZE);
			int totalRead = msg.getData().getInt(AVATAR_TOTAL_READ);

			try {
				int totalNumberOfListeners = listeners.beginBroadcast();
				for (int idx = 0; idx < totalNumberOfListeners; ++idx) {
					TwitterContentServiceCallback cb = listeners.getBroadcastItem(idx);
					if (cb != null) {
						cb.onAvatarDownloadProgress(twitter_id,
								AVATAR_DOWNLOAD_BEGIN, urlStr, totalSize,
								totalRead);
					}
				}
			} catch (RemoteException e) {
				if (Log.isLoggable(TAG, Log.ERROR)) {
					Log.e(TAG, "remote exception");
				}
			} finally {
				listeners.finishBroadcast();
			}
			break;
		case NEWFRIEND_ADDED:

			twitter_id = msg.getData().getString(TWITTER_ID);
			try {
				int totalNumberOfListeners = listeners.beginBroadcast();
				for (int idx = 0; idx < totalNumberOfListeners; ++idx) {
					TwitterContentServiceCallback cb = listeners
							.getBroadcastItem(idx);
					cb.onNewFriendAdded(twitter_id);
				}
			} catch (RemoteException e) {
				if (Log.isLoggable("TCS", Log.ERROR)) {
					Log.e("TCS", "remote exception", e);
				}
			} finally {
				listeners.finishBroadcast();
			}

			break;
		case TWEET_REC:
			Log.d("HANDLER", "tweet recieved");
			Tweet tweet = (Tweet) msg.getData().get(TWEET);
			try {
				int totalNumberOfListeners = listeners.beginBroadcast();
				for (int idx = 0; idx < totalNumberOfListeners; ++idx) {
					TwitterContentServiceCallback cb = listeners.getBroadcastItem(idx);
					lock.acquire();
					if (!context.hasTweet(tweet)) {
						context.push(tweet);
						cb.onNewTweetRecieved(tweet);
						context.getAvatarDownloadQueue().offer(tweet);
					}
					lock.release();
				}
			} catch (RemoteException e) {
				if (Log.isLoggable("TCS", Log.ERROR)) {
					Log.e("TCS", "remote exception", e);
				}
			} catch (InterruptedException e) {
				Log.e("TCS", "interrupted exception while attempting to lock!",
						e);
			} finally {
				listeners.finishBroadcast();
			}

			break;
		default:
			break;
		}
	}
}
