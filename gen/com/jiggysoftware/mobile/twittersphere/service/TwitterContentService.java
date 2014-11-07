/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: C:\\Users\\fredsdesktop\\projects\\android\\android-twittersphere-1.0\\src\\com\\jiggysoftware\\mobile\\twittersphere\\service\\TwitterContentService.aidl
 */
package com.jiggysoftware.mobile.twittersphere.service;
import java.lang.String;
import android.os.RemoteException;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Binder;
import android.os.Parcel;
import java.util.List;
public interface TwitterContentService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.jiggysoftware.mobile.twittersphere.service.TwitterContentService
{
private static final java.lang.String DESCRIPTOR = "com.jiggysoftware.mobile.twittersphere.service.TwitterContentService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an TwitterContentService interface,
 * generating a proxy if needed.
 */
public static com.jiggysoftware.mobile.twittersphere.service.TwitterContentService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = (android.os.IInterface)obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.jiggysoftware.mobile.twittersphere.service.TwitterContentService))) {
return ((com.jiggysoftware.mobile.twittersphere.service.TwitterContentService)iin);
}
return new com.jiggysoftware.mobile.twittersphere.service.TwitterContentService.Stub.Proxy(obj);
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
case TRANSACTION_updateRecentTweets:
{
data.enforceInterface(DESCRIPTOR);
this.updateRecentTweets();
reply.writeNoException();
return true;
}
case TRANSACTION_updateFriendsList:
{
data.enforceInterface(DESCRIPTOR);
this.updateFriendsList();
reply.writeNoException();
return true;
}
case TRANSACTION_follow:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.follow(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_remove:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.remove(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_registerCallback:
{
data.enforceInterface(DESCRIPTOR);
com.jiggysoftware.mobile.twittersphere.service.TwitterContentServiceCallback _arg0;
_arg0 = com.jiggysoftware.mobile.twittersphere.service.TwitterContentServiceCallback.Stub.asInterface(data.readStrongBinder());
this.registerCallback(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_unregisterCallback:
{
data.enforceInterface(DESCRIPTOR);
com.jiggysoftware.mobile.twittersphere.service.TwitterContentServiceCallback _arg0;
_arg0 = com.jiggysoftware.mobile.twittersphere.service.TwitterContentServiceCallback.Stub.asInterface(data.readStrongBinder());
this.unregisterCallback(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_clearAndRefreshCache:
{
data.enforceInterface(DESCRIPTOR);
this.clearAndRefreshCache();
reply.writeNoException();
return true;
}
case TRANSACTION_getRecentTweets:
{
data.enforceInterface(DESCRIPTOR);
java.util.List<com.jiggysoftware.mobile.twittersphere.om.Tweet> _result = this.getRecentTweets();
reply.writeNoException();
reply.writeTypedList(_result);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.jiggysoftware.mobile.twittersphere.service.TwitterContentService
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
public void updateRecentTweets() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_updateRecentTweets, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void updateFriendsList() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_updateFriendsList, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void follow(java.lang.String screen_name) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(screen_name);
mRemote.transact(Stub.TRANSACTION_follow, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void remove(java.lang.String screen_name) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(screen_name);
mRemote.transact(Stub.TRANSACTION_remove, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void registerCallback(com.jiggysoftware.mobile.twittersphere.service.TwitterContentServiceCallback callback) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((callback!=null))?(callback.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_registerCallback, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void unregisterCallback(com.jiggysoftware.mobile.twittersphere.service.TwitterContentServiceCallback callback) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((callback!=null))?(callback.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_unregisterCallback, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void clearAndRefreshCache() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_clearAndRefreshCache, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public java.util.List<com.jiggysoftware.mobile.twittersphere.om.Tweet> getRecentTweets() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.util.List<com.jiggysoftware.mobile.twittersphere.om.Tweet> _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getRecentTweets, _data, _reply, 0);
_reply.readException();
_result = _reply.createTypedArrayList(com.jiggysoftware.mobile.twittersphere.om.Tweet.CREATOR);
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_updateRecentTweets = (IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_updateFriendsList = (IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_follow = (IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_remove = (IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_registerCallback = (IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_unregisterCallback = (IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_clearAndRefreshCache = (IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_getRecentTweets = (IBinder.FIRST_CALL_TRANSACTION + 7);
}
public void updateRecentTweets() throws android.os.RemoteException;
public void updateFriendsList() throws android.os.RemoteException;
public void follow(java.lang.String screen_name) throws android.os.RemoteException;
public void remove(java.lang.String screen_name) throws android.os.RemoteException;
public void registerCallback(com.jiggysoftware.mobile.twittersphere.service.TwitterContentServiceCallback callback) throws android.os.RemoteException;
public void unregisterCallback(com.jiggysoftware.mobile.twittersphere.service.TwitterContentServiceCallback callback) throws android.os.RemoteException;
public void clearAndRefreshCache() throws android.os.RemoteException;
public java.util.List<com.jiggysoftware.mobile.twittersphere.om.Tweet> getRecentTweets() throws android.os.RemoteException;
}
