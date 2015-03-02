package com.interestfriend.contentprovider;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.interestfriend.data.ChatUserDao;
import com.interestfriend.db.DBUtils;
import com.interestfriend.utils.Utils;

public class MyUserFriendContentProvider extends ContentProvider {

	private static HashMap<String, String> sMyCirclesProjectionMap;

	private static final int CIRCLES = 1;
	private static final int CIRCLES_ID = 2;

	private static final UriMatcher sUriMatcher;

	@Override
	public boolean onCreate() {
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(ChatUserDao.TABLE_NAME);

		switch (sUriMatcher.match(uri)) {
		case CIRCLES:
			qb.setProjectionMap(sMyCirclesProjectionMap);
			break;

		case CIRCLES_ID:
			qb.setProjectionMap(sMyCirclesProjectionMap);
			qb.appendWhere(MyUserFriendProvider.MyUserFriendlumns._ID + "="
					+ uri.getPathSegments().get(1));
			break;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		// Get the database and run the query
		SQLiteDatabase db = DBUtils.getDBsa(1);
		Cursor c = qb.query(db, projection, selection, selectionArgs, null,
				null, null);

		// Tell the cursor what uri to watch, so it knows when its source data
		// changes
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	@Override
	public String getType(Uri uri) {
		switch (sUriMatcher.match(uri)) {
		case CIRCLES:
			return MyUserFriendProvider.CONTENT_TYPE;
		case CIRCLES_ID:
			return MyUserFriendProvider.CONTENT_ITEM_TYPE;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		// Validate the requested uri
		if (sUriMatcher.match(uri) != CIRCLES) {
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		ContentValues values;
		if (initialValues != null) {
			values = new ContentValues(initialValues);
		} else {
			values = new ContentValues();
		}

		// Make sure that the fields are all set
		if (values.containsKey(ChatUserDao.COLUMN_USER_AVATAR) == false) {
			values.put(ChatUserDao.COLUMN_USER_AVATAR, "");
		}

		if (values.containsKey(ChatUserDao.COLUMN_USER_ID) == false) {
			values.put(ChatUserDao.COLUMN_USER_ID, 0);
		}

		SQLiteDatabase db = DBUtils.getDBsa(2);
		long rowId = db.insert(ChatUserDao.TABLE_NAME,
				ChatUserDao.COLUMN_USER_ID, values);
		if (rowId > 0) {
			Uri noteUri = ContentUris.withAppendedId(
					MyUserFriendProvider.MyUserFriendlumns.CONTENT_URI, rowId);
			getContext().getContentResolver().notifyChange(noteUri, null);
			return noteUri;
		}
		Utils.print("error::::::::::::::::::::" + uri);
		throw new SQLException("Failed to insert row into " + uri);
	}

	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		SQLiteDatabase db = DBUtils.getDBsa(2);
		int count;
		switch (sUriMatcher.match(uri)) {
		case CIRCLES:
			count = db.delete(ChatUserDao.TABLE_NAME, where, whereArgs);
			break;

		case CIRCLES_ID:
			String noteId = uri.getPathSegments().get(1);
			count = db.delete(ChatUserDao.TABLE_NAME,
					MyUserFriendProvider.MyUserFriendlumns._ID
							+ "="
							+ noteId
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String where,
			String[] whereArgs) {
		SQLiteDatabase db = DBUtils.getDBsa(2);
		int count;
		switch (sUriMatcher.match(uri)) {
		case CIRCLES:
			count = db.update(ChatUserDao.TABLE_NAME, values, where, whereArgs);
			break;

		case CIRCLES_ID:
			String noteId = uri.getPathSegments().get(1);
			count = db.update(ChatUserDao.TABLE_NAME, values,
					MyUserFriendProvider.MyUserFriendlumns._ID
							+ "="
							+ noteId
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		// 这个地方的members要和CircleMemberColumns.CONTENT_URI中最后面的一个Segment一致
		sUriMatcher.addURI(MyUserFriendProvider.AUTHORITY, "friends", CIRCLES);
		sUriMatcher.addURI(MyUserFriendProvider.AUTHORITY, "friends/#",
				CIRCLES_ID);

		sMyCirclesProjectionMap = new HashMap<String, String>();
		sMyCirclesProjectionMap.put(MyUserFriendProvider.MyUserFriendlumns._ID,
				MyUserFriendProvider.MyUserFriendlumns._ID);
		sMyCirclesProjectionMap.put(ChatUserDao.COLUMN_USER_AVATAR,
				ChatUserDao.COLUMN_USER_AVATAR);
		sMyCirclesProjectionMap.put(ChatUserDao.COLUMN_USER_CHAT_ID,
				ChatUserDao.COLUMN_USER_CHAT_ID);
		sMyCirclesProjectionMap.put(ChatUserDao.COLUMN_USER_FROM_CIRCLE,
				ChatUserDao.COLUMN_USER_FROM_CIRCLE);
		sMyCirclesProjectionMap.put(ChatUserDao.COLUMN_USER_ID,
				ChatUserDao.COLUMN_USER_ID);
		sMyCirclesProjectionMap.put(ChatUserDao.COLUMN_USER_NAME,
				ChatUserDao.COLUMN_USER_NAME);

	}
}
