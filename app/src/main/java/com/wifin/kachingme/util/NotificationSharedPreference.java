package com.wifin.kachingme.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by comp on 5/3/2017.
 */
public class NotificationSharedPreference {
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "NotificationSharedPreference_#";

    public static final String KEY_MESSAGE_COUNT = "MSG_count";
    public static final String KEY_MESSAGE_GET_COUNT = "MSG_get_count";

    public static final String KEY_MESSAGE_SENDER_ONE = "MSG_one";
    public static final String KEY_MESSAGE_GET_ONE = "MSG_get_one";

    public static final String KEY_MESSAGE_SENDER_TWO = "MSG_two";
    public static final String KEY_MESSAGE_GET_TWO = "MSG_get_two";

    public static final String KEY_MESSAGE_SENDER_THREE = "MSG_three";
    public static final String KEY_MESSAGE_GET_THREE = "MSG_get_three";

    public static final String KEY_MESSAGE_MEB_DATA = "MSG_MEM_count";



    // Constructor
    public NotificationSharedPreference(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    // Set Msg count
    public void setMsg_Count(int val) {

        editor.putInt(KEY_MESSAGE_COUNT, val);

        editor.commit();
    }

    public void setMsg_Data_One(String val) {

        editor.putString(KEY_MESSAGE_SENDER_ONE, val);

        editor.commit();
    }

    public void setMsg_Data_Two(String val) {

        editor.putString(KEY_MESSAGE_SENDER_TWO, val);

        editor.commit();
    }


    public void setMsg_Data_Three(String val) {

        editor.putString(KEY_MESSAGE_SENDER_THREE, val);

        editor.commit();
    }

    // Set Message sent details
    public void setMsg_SenderData(String  val) {

        editor.putString(KEY_MESSAGE_MEB_DATA, val);

        editor.commit();
    }


    public int getMsg_Count() {

        return pref.getInt(KEY_MESSAGE_COUNT, 0);
    }

    public String getMsg_Data_One() {

        return  pref.getString(KEY_MESSAGE_SENDER_ONE, null);

    }

    public String getMsg_Data_Two() {

        return  pref.getString(KEY_MESSAGE_SENDER_TWO, null);

    }

    public String getMsg_Data_Three() {

        return  pref.getString(KEY_MESSAGE_SENDER_THREE, null);

    }

    public String getMsg_SentDetails() {

        return pref.getString(KEY_MESSAGE_MEB_DATA, null);

    }




    public void clearDataNewtork() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

    }

}