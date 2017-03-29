package com.wifin.kachingme.database;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.wifin.kachingme.util.Constant;

/**
 * Created by comp on 10/18/2016.
 */
public class MessageContentProvider extends ContentProvider{


    // database
    private DatabaseHelper database;

    // used for the UriMacher
    private static final int TODOS = 10;
    private static final int TODO_ID = 20;

    private static final String AUTHORITY = "de.kachingme.android.messages.contentprovider";

    private static final String BASE_PATH = "messages";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/" + BASE_PATH);

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/todos";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/messages";

    private static final UriMatcher sURIMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, BASE_PATH, TODOS);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", TODO_ID);
    }

    @Override
    public boolean onCreate() {
        database = DatabaseHelper.getDBAdapterInstance(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // Uisng SQLiteQueryBuilder instead of query() method
        database = DatabaseHelper.getDBAdapterInstance(getContext());
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

/*
        Cursor cursor = myDataBase.rawQuery(
                "Select * from messages where key_remote_jid='" + jid
                        + "' and is_sec_chat=" + is_sec_chat + "", null);*/
        String jid = uri.getQueryParameter("jid");
       // Integer sec = Integer.parseInt(uri.getQueryParameter("sec"));
        // Set the table
        queryBuilder.setTables("messages");
         queryBuilder.appendWhere("key_remote_jid='" +  Constant.mself_jid
                 + "' and is_sec_chat=" +  1 + "");
/*

        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case TODOS:
                break;
            case TODO_ID:
                // adding the ID to the original query

                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
*/

        SQLiteDatabase db = database.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        // make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;

    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
