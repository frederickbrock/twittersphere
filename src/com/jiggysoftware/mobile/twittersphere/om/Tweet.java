package com.jiggysoftware.mobile.twittersphere.om;

import android.os.Parcel;
import android.os.Parcelable;

public class Tweet implements Parcelable {

    private String createdAt;
    private String status_id;
    private String text;
    private String twitter_id;
    private String avatar_url;
    private String screen_name;
    private int replyTo;
    private String replyToStatusId;
    private boolean direct;
    
    public static Parcelable.Creator<Tweet> CREATOR = new Parcelable.Creator<Tweet>() {

	public Tweet createFromParcel(Parcel source) {
	    Tweet t = new Tweet();
	    t.twitter_id = source.readString();
	    t.screen_name = source.readString();
	    t.avatar_url = source.readString();
	    t.createdAt = source.readString();
	    t.text = source.readString();
	    t.status_id = source.readString();
	    t.replyTo = source.readInt();
	    return t;
	}

	
	public Tweet[] newArray(int size) {
	    Tweet[] ta = new Tweet[size];
	    return ta;
	}

    };

   
    public int describeContents() {
	return 0;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus_id() {
        return status_id;
    }

    public void setStatus_id(String status_id) {
        this.status_id = status_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTwitter_id() {
        return twitter_id;
    }

    public void setTwitter_id(String twitter_id) {
        this.twitter_id = twitter_id;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getScreen_name() {
        return screen_name;
    }

    public void setScreen_name(String screen_name) {
        this.screen_name = screen_name;
    }

    public int getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(int replyTo) {
        this.replyTo = replyTo;
    }

    public String getReplyToStatusId() {
        return replyToStatusId;
    }

    public void setReplyToStatusId(String replyToStatusId) {
        this.replyToStatusId = replyToStatusId;
    }

    public boolean isDirect() {
        return direct;
    }

    public void setDirect(boolean direct) {
        this.direct = direct;
    }

 
    public void writeToParcel(Parcel dest, int flags) {

	 dest.writeString(this.twitter_id);
	 dest.writeString(this.screen_name);
	 dest.writeString(this.avatar_url);
	 dest.writeString(this.createdAt);
	 dest.writeString(this.text);
	 dest.writeString(this.status_id);
	 dest.writeInt(this.replyTo);
    }
    
    /**
     * returns the file name to use for this avatar.
     * crappy that it was here.
     * @return
     */
    public String getAvatarFileName(){
	if(avatar_url != null){
	    return avatar_url.substring(avatar_url.lastIndexOf('/')+ 1);
	}
	return(null);
    }
    

   
}
