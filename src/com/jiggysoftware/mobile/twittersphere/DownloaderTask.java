package com.jiggysoftware.mobile.twittersphere;

import static com.jiggysoftware.mobile.twittersphere.Constants.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import com.jiggysoftware.mobile.twittersphere.om.Tweet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class DownloaderTask extends Thread {

	public String baseContextPath;
	public TwitterUpdateService context;
	public static String TAG = "DOWNLOADER";
	public static HashMap<String, Bitmap.CompressFormat> mimeTypeMapping = new HashMap<String, Bitmap.CompressFormat>();

	static {
		mimeTypeMapping.put("jpg", Bitmap.CompressFormat.JPEG);
		mimeTypeMapping.put("jpeg", CompressFormat.PNG);
		mimeTypeMapping.put("png", Bitmap.CompressFormat.PNG);
	}

	@Override
	public void run() {
		Tweet tweet = null;
		baseContextPath = context.getFilesDir().getAbsolutePath();

		do {
			while (tweet == null) {
				try {
					tweet = context.getAvatarDownloadQueue().poll(1,
							TimeUnit.SECONDS);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					Log
							.e(
									TAG,
									"interrupted exception while waiting for next tweet",
									e);
				}
			}

			downloadAvatar(tweet);
			tweet = null;
		} while (true); // need some better way to exit, should check to see if
		// the service is bound to a client, if not skip this.
	}

	/**
	 * downloads the avatar by creating a bitmap to compress the stream.
	 * 
	 * @param tweet
	 */

	private void downloadAvatar(Tweet tweet) {
		Handler h = context.getServiceMessageHandler();
		FileOutputStream rawStream = null;
		InputStream imageStream = null;
		int totalRead = 0;
		try {
			Log.d("DOWNLOADER", "getting path for new avatar for:"
							+ tweet.getScreen_name() + " url: "
							+ tweet.getAvatar_url());
			if (tweet.getAvatar_url() == null) {
				Log.d("DOWNLOADER", "avatar url is null on tweet");
				return;
			}

			String newAvatarFileName = tweet.getAvatar_url().substring(
					tweet.getAvatar_url().lastIndexOf('/') + 1);
			Log.d("DOWNLOADER", "new avatar name: " + newAvatarFileName);
			File f = new File(baseContextPath + newAvatarFileName);
			if (f.exists()) {
				Log.d("DOWNLOADER", "path: " + f.getAbsolutePath() + " exists");
			} else {
				Log.d("DOWNLOADER", "path: " + f.getAbsolutePath()
						+ " does NOT exists");
				Message m = new Message();
				m.setTarget(h);
				Bundle messageData = m.getData();
				messageData.putInt(TASK_ID, DOWNLOAD_AVATAR);
				messageData.putString(TWITTER_ID, tweet.getTwitter_id());
				messageData.putInt(AVATAR_TOTAL_SIZE, 0);
				messageData.putInt(AVATAR_TOTAL_READ, 0);
				m.setData(messageData);
				m.sendToTarget();

				Log.d(TAG, "downloading url: " + tweet.getAvatar_url());

				URL url = new URL(tweet.getAvatar_url());
				HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
				httpConnection.setRequestMethod("GET");
				httpConnection.connect();

				int contentLength = httpConnection.getContentLength();
				String contentType = httpConnection.getContentType();
				imageStream = httpConnection.getInputStream();
				Log.d("DOWNLOADER", "content length: " + contentLength	+ " content type: " + contentType);
				
				rawStream = context.openFileOutput(newAvatarFileName,Context.MODE_WORLD_READABLE);
				byte[] buffer = new byte[contentLength];
				imageStream.read(buffer, 0, buffer.length);
				ByteArrayInputStream bis = new ByteArrayInputStream(buffer);
				BitmapDrawable bDrawable = new BitmapDrawable(bis);
				boolean compressSuccess = bDrawable.getBitmap().compress(getCompressFormat(contentType), 100, rawStream);
				if (compressSuccess != false) {
					Log.d("DOWNLOADER", "avatar bitmap compression is success");
				}

				m = new Message();
				m.getData().putInt(TASK_ID, DOWNLOAD_AVATAR);
				m.getData().putString(TWITTER_ID, tweet.getTwitter_id());
				m.getData().putInt(AVATAR_TOTAL_SIZE, 0);
				m.getData().putInt(AVATAR_TOTAL_READ, 0);
				m.getData().putInt(TASK_STAGE, AVATAR_DOWNLOAD_COMPLETE);
				m.getData().putInt(AVATAR_TOTAL_READ, totalRead);
				m.setTarget(h);
				m.sendToTarget();
			}

		} catch (Exception e) {
			Log.e("DOWNLOADER", "exception while downloading avatar", e);
		} finally {
			if (rawStream != null) {
				try {
					rawStream.close();
				} catch (IOException e) {
				}
			}

		}
	}

	private Bitmap.CompressFormat getCompressFormat(String contentType) {
		if ((contentType.indexOf("jpg") > -1)
				|| ((contentType.indexOf("jpeg") > -1))) {
			return Bitmap.CompressFormat.JPEG;
		} else if (contentType.indexOf("png") > -1) {
			return Bitmap.CompressFormat.PNG;
		}
		return (CompressFormat.JPEG);
	}

}
