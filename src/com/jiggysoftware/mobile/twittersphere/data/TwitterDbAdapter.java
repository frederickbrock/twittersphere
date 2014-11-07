package com.jiggysoftware.mobile.twittersphere.data;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.jiggysoftware.mobile.twittersphere.Constants;
import com.jiggysoftware.mobile.twittersphere.om.Friend;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class TwitterDbAdapter extends SQLiteOpenHelper {

    public static final String DB_NAME = "SnaggMeDb";
    public static final int DBVERSION = 2;

    public static final String TAG = "snaggmegb";
    public static final String TABLE_PROFILECREDENTIALS = "ProfileCredentials";
    public static final String TABLE_TWITTERFRIENDS = "TwitterFriends";
    public static final String TABLE_TWEETS = "Tweets";

    private TwitterService twitterService = null;

    public TwitterDbAdapter(Context context) {
	super(context, DB_NAME, null, 2);
    }

    public boolean hasFriend(String twitter_id) {
	Cursor s = null;
	Log.d(Constants.TAG, "entering hasFriend!");
	try {
	    SQLiteDatabase db = getReadableDatabase();
	    s = db.query(TABLE_TWITTERFRIENDS, 
		    	 new String[] { "twitter_id" }, 
		    	 "(twitter_id = ?)", 
		    	 new String[] { twitter_id.trim() }, 
		    	 null, 
		    	 null, 
		    	 null);

	    return (s.getCount() > 0);
	} catch (SQLiteException e) {
	    Log.e(TAG, "exception while looking for friend: " + twitter_id, e);
	} finally {
	    if (s != null) {
		s.close();
	    }
	}

	return (false);
    }

    public void updateFriendStatus(String twitter_id, String status_id, Date created_at, String status_text) {
	Log.d(Constants.TAG, "updating status for: " + twitter_id);
	SQLiteDatabase db = getWritableDatabase();
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
	String createdAtStr = df.format(created_at);

	StringBuilder builder = new StringBuilder();
	builder.append("insert into ");
	builder.append(TABLE_TWEETS);
	builder.append("(twitter_id,status_id,status_text,created_at) VALUES(?,?,?,?)");
	SQLiteStatement stmt = db.compileStatement(builder.toString());
	try {
	    stmt.bindString(1, twitter_id);
	    stmt.bindString(2, status_id);
	    stmt.bindString(3, status_text);
	    stmt.bindString(3, createdAtStr);
	    stmt.execute();
	} catch (SQLiteException e) {
	    Log.e(Constants.TAG, "exception while updating internal status: ", e);
	} finally {
	    if (stmt != null) {
		try {
		    stmt.close();
		} catch (Exception e) {
		}
	    }
	}
    }

    public TwitterService getTwitterService() {
	SQLiteDatabase db = getReadableDatabase();
	Cursor crsLogin = null;

	if (twitterService != null) {
	    return (twitterService);
	}

	try {
	    crsLogin = db.query(TABLE_PROFILECREDENTIALS, 
		    		new String[] { "userName", "password" }, 
		    		"(provider = 'twitter')", 
		    		null, 
		    		null, 
		    		null, 
		    		null);

	    if (crsLogin.getCount() > 0) {
		crsLogin.moveToFirst();
		String twitterUserName = crsLogin.getString(0);
		String twitterPassword = crsLogin.getString(1);
		this.twitterService = new TwitterService(twitterUserName, twitterPassword);
		return (this.twitterService);
	    }
	} catch (Exception e) {
	    Log.e(TAG, "exception while attempting to login in to twitter", e);
	} finally {
	    if (crsLogin != null) {
		try {
		    crsLogin.close();
		} catch (Exception e) {
		}
	    }
	}

	return (this.twitterService);
    }

    public void createFriend(String twitter_id, String screen_name, String image_url, String name, String description) {

	SQLiteDatabase db = getWritableDatabase();
	StringBuilder builder = new StringBuilder();
	builder.append("insert into ");
	builder.append(TABLE_TWITTERFRIENDS);
	builder.append("(twitter_id,screen_name,name,description,image_url,protected,following)");
	builder.append(" values(?,?,?,?,?,1,1)");

	// Grab the twitterService and if valid attempt to create the friend if
	// it doesn't exist
	if (getTwitterService() != null) {
	    SQLiteStatement stmt = db.compileStatement(builder.toString());
	    try {
		if (!hasFriend(twitter_id)) {
		    Log.d(TAG, "adding friend: " + screen_name + " did not exist, adding to db");
		    stmt.bindString(1, twitter_id.trim());
		    stmt.bindString(2, screen_name);
		    stmt.bindString(3, name);
		    stmt.bindString(4, description);
		    stmt.bindString(5, image_url);
		    stmt.execute();
		    stmt.clearBindings();
		}

	    } catch (Exception e) {
		Log.e(TAG, "exception while creating",e);
	    } finally {
		if (stmt != null) {
		    try {
			stmt.close();
		    } catch (Exception e) {
		    }
		}
	    }
	}
    }

    public boolean hasServiceConfigured(String provider) {
	Cursor credentials = null;
	try {
	    getReadableDatabase().query(TABLE_PROFILECREDENTIALS,
		    			new String[] { "userName" }, 
		    			"(provider = ?)", 
		    			new String[] { provider }, 
		    			null, 
		    			null, 
		    			null);
	    
	    return ((credentials != null) && (credentials.getCount() > 0));
	} catch (Exception e) {
	    Log.e(TAG, "exception checking for service configuration: " + e.toString(), e);
	} finally {
	    if (credentials != null) {
		credentials.close();
	    }
	}

	return (false);
    }

    public void saveServiceProviderCredentials(final String userName, final String password, final String service) {
	StringBuilder builder = new StringBuilder();
	builder.append("insert into ");
	builder.append(TABLE_PROFILECREDENTIALS);
	builder.append(" (username,password,provider)");
	builder.append(" values(?,?,'twitter')");
	SQLiteStatement stmt = getWritableDatabase().compileStatement(builder.toString());
	stmt.bindString(1, userName);
	stmt.bindString(2, password);
	stmt.execute();
	stmt.close();

    }

    public Cursor retrieveFriends(String provider) throws Exception {
	Cursor cr = null;
	try {
	    SQLiteDatabase db = getReadableDatabase();
	    String[] columns = {"twitter_id","status_id","created_at","status_text"};
	    cr = db.query(TABLE_TWEETS, 
		    	  columns, 
		    	  null, 
		    	  null, 
		    	  null, // ununsed
		    	  null, // unused
		    	  null);
	} catch (Exception e) {
	    Log.e(TAG, "exception while attempting to retrieve a list of friends");
	    throw e;
	}

	return (cr);
    }
    
    
  public Friend getFriend(String twitter_id) throws Exception {
	
	Cursor cr = null;
	
	try {
	    SQLiteDatabase db = getReadableDatabase();
	    String[] columns = {"_ID","twitter_id","screen_name","name","image_url"};
	    String criteria = "(twitter_id = ?)";
	    cr = db.query(TABLE_TWITTERFRIENDS, 
		    	  columns, 
		    	  criteria, 
		    	  new String[]{twitter_id}, 
		    	  null, // ununsed
		    	  null, // unused
		    	  null);
	    
	    if(cr.getCount() == 0){ return(null); } //shouldn't happen
	    
	    cr.moveToFirst();
	    Friend friend = new Friend();
	    friend.pid = cr.getString(1);
	    friend.displayName = cr.getString(2);
	    friend.name = cr.getString(3);
	    friend.profileImageUrl = cr.getString(4);
	    
	    return(friend);
	} catch (Exception e) {
	    Log.e(TAG, "exception while attempting to retrieve a list of friends",e);
	    throw e;
	} finally{
	    if(cr != null){ try{ cr.close(); }catch(Exception e){}}
	}
  }



    private String getProfileTableSQL() {
	StringBuilder sql = new StringBuilder();
	sql.append("create table ");
	sql.append(TABLE_PROFILECREDENTIALS);
	sql.append(" ( ID integer primary key autoincrement,");
	sql.append("username TEXT, ");
	sql.append("password TEXT, ");
	sql.append("provider TEXT);");
	return (sql.toString());
    }

    private String getFriendsTableSQL() {
	StringBuilder sql = new StringBuilder();
	sql.append("create table " + TABLE_TWITTERFRIENDS + "(");
	sql.append(" _ID integer primary key autoincrement,");
	sql.append(" twitter_id TEXT,");
	sql.append(" screen_name TEXT,");
	sql.append(" name TEXT,");
	sql.append(" description TEXT,");
	sql.append(" image_url TEXT,");
	sql.append(" avatar BLOB,");
	sql.append(" avatar_type TEXT not null,");
	sql.append(" protected INTEGER,");
	sql.append(" following INTEGER,");
	sql.append(" follower INTEGER);");
	return (sql.toString());
    }

    private String getTweetTableSQL() {
	StringBuilder sql = new StringBuilder();
	sql.append("create table ");
	sql.append(TABLE_TWEETS);
	sql.append("( twitter_id TEXT,");
	sql.append("  status_id TEXT,");
	sql.append("  created_at DATETIME,");
	sql.append("  status_text TEXT);");
	return (sql.toString());
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
	db.execSQL(getProfileTableSQL());
	db.execSQL(this.getFriendsTableSQL());
	db.execSQL(getTweetTableSQL());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	StringBuilder sql = new StringBuilder();
	sql.append("drop table ");
	sql.append(TABLE_TWITTERFRIENDS);
	sql.append(";");
	db.execSQL(sql.toString());
	sql = new StringBuilder();
	sql.append("drop table ");
	sql.append(TABLE_TWEETS);
	sql.append(";");
	db.execSQL(sql.toString());
	sql = new StringBuilder();
	sql.append("drop table ");
	sql.append(TABLE_PROFILECREDENTIALS);
	sql.append(";");
	db.execSQL(sql.toString());
	sql = null;
    }

}
