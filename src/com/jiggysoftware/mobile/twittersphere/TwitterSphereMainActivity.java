package com.jiggysoftware.mobile.twittersphere;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.jiggysoftware.mobile.twittersphere.data.TwitterDbAdapter;
import com.jiggysoftware.mobile.twittersphere.om.Tweet;
import com.jiggysoftware.mobile.twittersphere.service.TwitterContentService;
import com.jiggysoftware.mobile.twittersphere.service.TwitterContentServiceCallback;
import com.jiggysoftware.mobile.twittersphere.ui.TwitterStatusAdapter;

public class TwitterSphereMainActivity extends Activity {
	/** Called when the activity is first created. */
	public static final String TAG = "twitters";
	private TwitterContentService twitterContentService;
	// private TwitterDbAdapter twitterDbAdapter;
	private TwitterStatusAdapter adapter;
	private ListView mainView;

	public static final int CB_MESSAGE_TYPE = 1000;
	public static final int CB_MESSAGE_SETTWEETS = 1000;
	public static final int CB_MESSAGE_NEWTWEET = 2000;

	public static final int MENU_PREFS = Menu.FIRST;

	private TwitterContentServiceCallback twitterCallback = new TwitterContentServiceCallback.Stub() {

		public void onAvatarDownloadProgress(String twitter_id,
				int progressType, String url, int totalSize, int totalRead)
				throws RemoteException {

		}

		public void onNewFriendAdded(String twitter_id) throws RemoteException {

		}

		public void onNewTweetRecieved(Tweet t) throws RemoteException {
			Log.d("SERVICECB", "recieved new tweet");
			Message m = new Message();
			m.getData().putParcelable(Constants.TWEET, t);
			m.getData().putInt(Constants.CB_MESSAGE_TYPE,
					Constants.CB_MESSAGE_NEWTWEET);
			m.setTarget(cbHandler);
			m.sendToTarget();
		}

		public void onTwitterUpdateBegin() throws RemoteException {
			// TODO Auto-generated method stub

		}

		public void onTwitterUpdateComplete() throws RemoteException {
			// TODO Auto-generated method stub

		}

	};

	private Handler cbHandler = new Handler() {

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			int msgType = msg.getData().getInt(Constants.CB_MESSAGE_TYPE);

			switch (msgType) {
			case Constants.CB_MESSAGE_NEWTWEET:
				Log.d("cbHandler", "recieved new tweet");
				Tweet t = (Tweet) msg.getData().get(Constants.TWEET);
				adapter.add(t);
				break;
			case Constants.CB_MESSAGE_SETTWEETS:
				List<Tweet> tweets = (List<Tweet>) msg.getData().get(
						Constants.TWEET);
				adapter.clear();
				for (Tweet ts : tweets) {
					adapter.add(ts);
				}
				break;
			default:
				break;

			}

		}

	};

	private ServiceConnection twitterServiceConnection = new ServiceConnection() {

		public void onServiceConnected(ComponentName name, IBinder service) {
			twitterContentService = TwitterContentService.Stub
					.asInterface(service);
			try {
				twitterContentService.registerCallback(twitterCallback);
				List<Tweet> l = null;
				try {
					l = twitterContentService.getRecentTweets();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (l != null) {
					Message m = new Message();
					m.getData().putInt(Constants.CB_MESSAGE_TYPE,
							Constants.CB_MESSAGE_SETTWEETS);
					m.getData().putParcelableArrayList(Constants.TWEET,
							(ArrayList<Tweet>) l);
					m.setTarget(cbHandler);
					m.sendToTarget();
				}
			} catch (RemoteException e) {
				Log.e(TAG, "exception while registering callback");
				e.printStackTrace();
			}
		}

		public void onServiceDisconnected(ComponentName name) {

		}

	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent loginIntent = new Intent(this, LoginToProviderActivity.class);
		startActivityForResult(loginIntent, Constants.LOGIN_TO_PROVIDER);
		// twitterDbAdapter = new TwitterDbAdapter(this);
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		// We are going to create two menus. Note that we assign them
		// unique integer IDs, labels from our string resources, and
		// given them shortcuts.
		menu.add(0, MENU_PREFS, 0, R.string.mnuprefs);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);

		// Before showing the menu, we need to decide whether the clear
		// item is enabled depending on whether there is text to clear.
		menu.findItem(MENU_PREFS).setVisible(true);
		return true;
	}

	/**
	 * Called when a menu item is selected.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_PREFS:
			Intent intent = new Intent(this,TwitterSpherePreferencesActivity.class);
			startActivity(intent);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case Constants.LOGIN_TO_PROVIDER:
			if (resultCode == 0) {
				handleMain();
			}
		default:
			return;
		}
	}

	public void handleMain() {

		setContentView(R.layout.main);
		Intent twitterUpdateIntent = new Intent("com.jiggysoftware.mobile.twittersphere.TwitterUpdateService");
		startService(twitterUpdateIntent);
		bindService(twitterUpdateIntent, twitterServiceConnection, 0);
		mainView = (ListView) findViewById(R.id.ListView01);
		EditText tweetEdit = new EditText(this);
		tweetEdit.setBackgroundColor(Color.DKGRAY);
		mainView.addHeaderView(tweetEdit);
		this.adapter = new TwitterStatusAdapter(this, R.id.TxtFriendStatus,	new ArrayList<Tweet>());
		mainView.setAdapter(adapter);
	}

}