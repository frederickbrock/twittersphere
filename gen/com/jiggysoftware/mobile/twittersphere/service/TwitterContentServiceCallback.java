/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: C:\\Users\\fredsdesktop\\projects\\android\\android-twittersphere-1.0\\src\\com\\jiggysoftware\\mobile\\twittersphere\\service\\TwitterContentServiceCallback.aidl
 */
package com.jiggysoftware.mobile.twittersphere.service;
import java.lang.String;
import android.os.RemoteException;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Binder;
import android.os.Parcel;
import com.jiggysoftware.mobile.twittersphere.om.Tweet;
public interface TwitterContentServiceCallback extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.jiggysoftware.mobile.twittersphere.service.TwitterContentServiceCallback
{
private static final java.lang.String DESCRIPTOR = "com.jiggysoftware.mobile.twittersphere.service.TwitterContentServiceCallback";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an TwitterContentServiceCallback interface,
 * generating a proxy if needed.
 */
public static com.jiggysoftware.mobile.twittersphere.service.TwitterContentServiceCallback asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = (android.os.IInterface)obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.jiggysoftware.mobile.twittersphere.service.TwitterContentServiceCallback))) {
return ((com.jiggysoftware.mobile.twittersphere.service.TwitterContentServiceCallback)iin);
}
return new com.jiggysoftware.mobile.twittersphere.service.TwitterContentServiceCallback.Stub.Proxy(obj);
}
public android.os.IBinder asBinder()
{
return this;
}
public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_onAvatarDownloadProgress:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
int _arg1;
_arg1 = data.readInt();
java.lang.String _arg2;
_arg2 = data.readString();
int _arg3;
_arg3 = data.readInt();
int _arg4;
_arg4 = data.readInt();
this.onAvatarDownloadProgress(_arg0, _arg1, _arg2, _arg3, _arg4);
reply.writeNoException();
return true;
}
case TRANSACTION_onNewTweetRecieved:
{
data.enforceInterface(DESCRIPTOR);
com.jiggysoftware.mobile.twittersphere.om.Tweet _arg0;
if ((0!=data.readInt())) {
_arg0 = com.jiggysoftware.mobile.twittersphere.om.Tweet.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
this.onNewTweetRecieved(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onNewFriendAdded:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.onNewFriendAdded(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onTwitterUpdateBegin:
{
data.enforceInterface(DESCRIPTOR);
this.onTwitterUpdateBegin();
reply.writeNoException();
return true;
}
case TRANSACTION_onTwitterUpdateComplete:
{
data.enforceInterface(DESCRIPTOR);
this.onTwitterUpdateComplete();
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.jiggysoftware.mobile.twittersphere.service.TwitterContentServiceCallback
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
public void onAvatarDownloadProgress(java.lang.String twitter_id, int progressType, java.lang.String url, int totalSize, int totalRead) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(twitter_id);
_data.writeInt(progressType);
_data.writeString(url);
_data.writeInt(totalSize);
_data.writeInt(totalRead);
mRemote.transact(Stub.TRANSACTION_onAvatarDownloadProgress, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void onNewTweetRecieved(com.jiggysoftware.mobile.twittersphere.om.Tweet tweet) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((tweet!=null)) {
_data.writeInt(1);
tweet.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_onNewTweetRecieved, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void onNewFriendAdded(java.lang.String twitter_id) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(twitter_id);
mRemote.transact(Stub.TRANSACTION_onNewFriendAdded, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void onTwitterUpdateBegin() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_onTwitterUpdateBegin, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void onTwitterUpdateComplete() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_onTwitterUpdateComplete, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_onAvatarDownloadProgress = (IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_onNewTweetRecieved = (IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_onNewFriendAdded = (IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_onTwitterUpdateBegin = (IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_onTwitterUpdateComplete = (IBinder.FIRST_CALL_TRANSACTION + 4);
}
public void onAvatarDownloadProgress(java.lang.String twitter_id, int progressType, java.lang.String url, int totalSize, int totalRead) throws android.os.RemoteException;
public void onNewTweetRecieved(com.jiggysoftware.mobile.twittersphere.om.Tweet tweet) throws android.os.RemoteException;
public void onNewFriendAdded(java.lang.String twitter_id) throws android.os.RemoteException;
public void onTwitterUpdateBegin() throws android.os.RemoteException;
public void onTwitterUpdateComplete() throws android.os.RemoteException;
}
