package com.jiggysoftware.mobile.twittersphere.ui;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiggysoftware.mobile.twittersphere.R;
import com.jiggysoftware.mobile.twittersphere.data.TwitterDbAdapter;
import com.jiggysoftware.mobile.twittersphere.om.Tweet;

public class TwitterStatusAdapter extends ArrayAdapter<Tweet> {

	public TwitterStatusAdapter(Context context, int textViewResourceId,
			List<Tweet> objects) {
		super(context, textViewResourceId, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View returnedView = null;

		if ((convertView == null)
				|| (convertView.getId() != R.layout.friendlistentry)) {
			LayoutInflater layoutInflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			returnedView = layoutInflater.inflate(R.layout.friendlistentry,
					null);
		} else {
			returnedView = convertView;
		}

		ImageView imgImage = (ImageView) returnedView
				.findViewById(R.id.ImgFriendAvatar);
		TextView txtStatus = (TextView) returnedView
				.findViewById(R.id.TxtFriendStatus);
		TextView txtFriendName = (TextView) returnedView
				.findViewById(R.id.TxtFriendTitleLine);
		TextView txtTweetTimestamp = (TextView)returnedView.findViewById(R.id.TxtFriendTweetTime);
		
		Tweet tweet = (Tweet) getItem(position);

		try {
			String userName = tweet.getScreen_name();
			String userStatus = tweet.getText();
			String avatar_url = tweet.getAvatar_url();
			String tweetTime = tweet.getCreatedAt();
			String path = getContext().getFilesDir().getAbsolutePath();

			if (avatar_url != null) {
				path += "/" + getAvatarPath(avatar_url);
			}

			Log.d("ADAPTER", "final path is: " + path);
			File f = new File(path);

			if ((f.exists()) && (f.canRead()) && (!f.isDirectory())) {
				Log.d("ADAPTER", "file exist, attempting to load up!");
				FileInputStream inputStream = new FileInputStream(f);
				Bitmap avatar = BitmapFactory.decodeStream(inputStream);
				imgImage.setImageBitmap(avatar);
				imgImage.invalidate();
			} else {
				// Simple Log the Error for now.
				Log.d("ADAPTER", "cannot read the avatar");
			}

			txtFriendName.setTypeface(Typeface.DEFAULT_BOLD);
			txtFriendName.setText(userName);
			txtStatus.setText(userStatus);
			Linkify.addLinks(txtStatus, Linkify.WEB_URLS);

			Calendar now = Calendar.getInstance();
			now.setTime(new Date()); //current Time
			
			txtTweetTimestamp.setText(tweetTime);
			
			
			
			
		
		
		} catch (Exception e) {
			Log.e("ADAPTER", "exception while configuring view", e);
		}

		returnedView.invalidate();

		return returnedView;
	}

	@Override
	public int getCount() {
		return super.getCount();
	}

	private String getAvatarPath(String url) {
		return (url.substring(url.lastIndexOf("/") + 1));
	}

}
