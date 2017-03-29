package com.wifin.kachingme.database;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.util.Log;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;
import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.pojo.ADV_group_GetSet;
import com.wifin.kachingme.pojo.Chat_list_GetSet;
import com.wifin.kachingme.pojo.ContactsGetSet;
import com.wifin.kachingme.pojo.GroupParticipantGetSet;
import com.wifin.kachingme.pojo.LoginGetSet;
import com.wifin.kachingme.pojo.MessageGetSet;
import com.wifin.kachingme.pojo.UserContactDto;
import com.wifin.kachingme.services.TempConnectionService;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.Utils;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.packet.Presence;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "kachingme.db";
    private static final String TABLE_CONTACTS = "contacts";
    private static final String TABLE_LOGIN = "login";
    private static final String TABLE_MESSAGES = "messages";
    private static final String TABLE_CHAT_LIST = "chat_list";
    private static final String TABLE_ADV_GROUP = "adv_group";
    private static final String TABLE_GROUP_PARTICIPANTS = "group_participants";
    public static boolean isUpdate = false;
    public static int version_val = 4;
    private static String DB_PATH = "";
    private static DatabaseHelper mDBConnection;
    private final Context myContext;
    private String TAG = DatabaseHelper.class.getSimpleName();
    private SQLiteDatabase myDataBase;

    public DatabaseHelper(Context context, String name, CursorFactory factory,
                          int version) {
        super(context, name, factory, version);
        Constant.printMsg("DatabaseHelper Constructer version:" + version);
        this.myContext = context;
        DB_PATH = "/data/data/"
                + context.getApplicationContext().getPackageName()
                + "/databases/";
        isUpdate = false;
    }

    public static synchronized DatabaseHelper getDBAdapterInstance(
            Context context) {
        Constant.printMsg("getDBAdapterInstance");
        if (mDBConnection == null) {
            Constant.printMsg("instanse : Null");

            mDBConnection = new DatabaseHelper(context, DB_NAME, null,
                    version_val);
        }
        return mDBConnection;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Constant.printMsg("oncreate");
        copyDataBase(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        Constant.printMsg("onUpgrade OldVersion : " + oldVersion
                + " NewVersion : " + newVersion);
        if (oldVersion < newVersion) {// db.execSQL("delete from tbl_products");
            Constant.printMsg("onUpgrade TRUE");
            try {
                // db.execSQL("ALTER TABLE contacts ADD Column `is_in_contact_list` NUMERIC");
                db.execSQL("ALTER TABLE messages ADD Column `flag` INTEGER");
                isUpdate = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void UpdateIsinContactList() {
        Constant.printMsg("UpdateIsinContactList");

        ContentResolver cr = myContext.getContentResolver();
        String country_code = KachingMeApplication.getsharedpreferences()
                .getString(Constant.COUNTRY_CODE_LABEL, "");
        Cursor phones = cr.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,
                null, null);
        ArrayList<String> listContactBook = new ArrayList<String>();

        while (phones.moveToNext()) {
            String phoneNumber = phones
                    .getString(phones
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            String phoneNumber1 = phoneNumber.replaceAll("[^0-9+]", "");

            if (Utils.check_prefix(phoneNumber1, "00", 2)
                    || Utils.check_prefix(phoneNumber1, "+", 1)) {

                if (Utils.check_prefix(phoneNumber1, "00", 2)) {
                    phoneNumber1 = ""
                            + phoneNumber1.substring(2, phoneNumber1.length());
                } else {
                    phoneNumber1 = ""
                            + phoneNumber1.substring(1, phoneNumber1.length());
                }
            } else {
                if (Utils.check_prefix(phoneNumber1, "0", 1)) {
                    phoneNumber1 = country_code
                            + phoneNumber1.substring(1, phoneNumber1.length());
                } else {
                    phoneNumber1 = country_code + phoneNumber1;
                }

            }
            listContactBook.add(phoneNumber1 + KachingMeApplication.getHost());

        }

        ArrayList<ContactsGetSet> contactinDatabase = getAllContacts();
        for (ContactsGetSet contacts : contactinDatabase) {

            if (checkNumberInContactList(listContactBook, contacts.getJid())) {
                setUpdateContactInList(contacts.getJid(), 0);
            } else {
                setUpdateContactInList(contacts.getJid(), 1);
            }
        }

    }

    private boolean checkNumberInContactList(ArrayList<String> list, String JID) {
        boolean flag = false;

        for (String string : list) {

            if (string.equals(JID))
                return true;
        }

        return flag;
    }

    public long setUpdateContactInList(String jid, int status) {

        ContentValues values = new ContentValues();

        values.put("is_in_contact_list", status);

        String wheres = "jid=?";
        String IDS = String.valueOf(jid);
        String[] whereArgs = {IDS.toString()};
        long rawId = myDataBase.update(TABLE_CONTACTS, values, wheres,
                whereArgs);
        return rawId;

    }


    public long setUpdateDestMsgData(String msg_id, String data) {

        ContentValues values = new ContentValues();

        values.put("data", data);

        String wheres = "_id=?";
        String IDS = String.valueOf(msg_id);
        String[] whereArgs = {IDS.toString()};
        long rawId = myDataBase.update(TABLE_MESSAGES, values, wheres,
                whereArgs);
        return rawId;

    }

    public long setUpdateDestMsgDataForBroad(String msg_id, String data) {

        ContentValues values = new ContentValues();

        values.put("data", data);

        String wheres = "key_id=?";
        String IDS = String.valueOf(msg_id);
        String[] whereArgs = {IDS.toString()};
        long rawId = myDataBase.update(TABLE_MESSAGES, values, wheres,
                whereArgs);
        return rawId;

    }

    public synchronized int openDataBase() throws SQLException {
        Constant.printMsg("openDataBase");
        if (myDataBase != null) {
            Constant.printMsg("TRUE");
            myDataBase.close();
        }
        myDataBase = mDBConnection.getWritableDatabase();

        return myDataBase.getVersion();
    }

    @Override
    public synchronized void close() {
        if (myDataBase != null)
            myDataBase.close();
        super.close();
    }

    @Override
    public synchronized SQLiteDatabase getWritableDatabase() {
        Constant.printMsg("getWritableDatabase");

        try {
            super.getWritableDatabase();
        } catch (Exception e) {
            // TODO: handle exception
        }

        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null,
                SQLiteDatabase.NO_LOCALIZED_COLLATORS
                        | SQLiteDatabase.OPEN_READWRITE);
        myDataBase.setVersion(version_val);

        return myDataBase;
    }

    private void copyDataBase(SQLiteDatabase db) {
        try {

            InputStream myInput = myContext.getAssets().open(DB_NAME);
            File file = new File(DB_PATH + DB_NAME);
            file.delete();

            String outFileName = DB_PATH + DB_NAME;
            OutputStream myOutput = new FileOutputStream(outFileName);
            myOutput.flush();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public ArrayList<LoginGetSet> getLogin() {

        ArrayList<LoginGetSet> list = new ArrayList<LoginGetSet>();

        Cursor cursor = myDataBase.query(TABLE_LOGIN,
                new String[]{"id", "user", "pass", "status", "nifty_name",
                        "nifty_email", "photo"}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                LoginGetSet usd = new LoginGetSet();
                usd.setId(cursor.getInt(0));
                usd.setUserName(cursor.getString(1));
                usd.setPassword(cursor.getString(2));
                usd.setStatus(cursor.getString(3));
                usd.setNifty_name(cursor.getString(4));
                usd.setNifty_email(cursor.getString(5));
                usd.setAvatar(cursor.getBlob(6));
                list.add(usd);
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return list;
    }

    public void insertLogin(String User, String pass, String status,
                            String name, String email, byte[] image) {

        Constant.printMsg("GGGGGOPOGPGPGOPGOG");

        ContentValues values = new ContentValues();
        values.put("user", User);
        values.put("pass", pass);
        values.put("status", status);
        values.put("nifty_name", name);
        values.put("nifty_email", email);
        values.put("photo", image);

        myDataBase.insert("login", null, values);
    }

    public void insertADV_group(ADV_group_GetSet advgroup) {
        ContentValues values = new ContentValues();

        values.put("jid", advgroup.getJid());
        values.put("member_jid", advgroup.getMember_id());
        values.put("group_type", advgroup.getGroup_type());
        values.put("answer", advgroup.getAnswer());

        myDataBase.insert(TABLE_ADV_GROUP, null, values);
    }

    public ArrayList<ContactsGetSet> get_Select_Group_Contacts(String gjid) {
        ArrayList<ContactsGetSet> list = new ArrayList<ContactsGetSet>();
        Cursor cursor = myDataBase
                .rawQuery(
                        "Select * from contacts where is_niftychat_user=1 and is_in_contact_list=1 and raw_contact_id!='0' and jid not in (select jid from group_participants where gjid='"
                                + gjid + "' ) order by display_name ", null);
        if (cursor.moveToFirst()) {
            do {
                ContactsGetSet usd = new ContactsGetSet();
                usd.setId(cursor.getInt(0));
                usd.setDisplay_name(cursor.getString(6));
                usd.setJid(cursor.getString(1));
                usd.setNumber(cursor.getString(4));
                usd.setPhone_label(cursor.getString(8));
                usd.setPhone_type(cursor.getString(7));
                usd.setRaw_contact_id(cursor.getString(5));
                usd.setStatus(cursor.getString(3));
                usd.setUnseen_msg_count(cursor.getInt(9));
                usd.setPhoto_ts(cursor.getBlob(10));
                usd.setNifty_name(cursor.getString(11));
                usd.setNifty_email(cursor.getString(12));

                list.add(usd);
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return list;
    }

    public ArrayList<ContactsGetSet> getContactsFilter(String value) {
        ArrayList<ContactsGetSet> list = new ArrayList<ContactsGetSet>();
        Cursor cursor = myDataBase
                .rawQuery(
                        "Select * from contacts where display_name LIKE '"
                                + value
                                + "%' AND is_niftychat_user=1 and raw_contact_id!='0' order by display_name ",
                        null);
        if (cursor.moveToFirst()) {
            do {
                ContactsGetSet usd = new ContactsGetSet();
                usd.setId(cursor.getInt(0));
                usd.setDisplay_name(cursor.getString(6));
                usd.setJid(cursor.getString(1));
                usd.setIs_niftychat_user(cursor.getInt(2));
                usd.setNumber(cursor.getString(4));
                usd.setPhone_label(cursor.getString(8));
                usd.setPhone_type(cursor.getString(7));
                usd.setRaw_contact_id(cursor.getString(5));
                usd.setStatus(cursor.getString(3));
                usd.setUnseen_msg_count(cursor.getInt(9));
                usd.setPhoto_ts(cursor.getBlob(10));
                usd.setNifty_name(cursor.getString(11));
                usd.setNifty_email(cursor.getString(12));
                if (cursor.getBlob(10) != null) {
                    Bitmap bmp = BitmapFactory.decodeByteArray(cursor.getBlob(10), 0,
                            cursor.getBlob(10).length);
                    usd.setPhoto_bitmap(bmp);

                }
                list.add(usd);
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return list;
    }

    public ArrayList<ContactsGetSet> getContactsFilternot(String value) {
        ArrayList<ContactsGetSet> list = new ArrayList<ContactsGetSet>();
        Cursor cursor = myDataBase
                .rawQuery(
                        "Select * from contacts where display_name LIKE '"
                                + value
                                + "%' AND is_niftychat_user=0 and raw_contact_id!='0' order by display_name ",
                        null);
        if (cursor.moveToFirst()) {
            do {
                ContactsGetSet usd = new ContactsGetSet();
                usd.setId(cursor.getInt(0));
                usd.setDisplay_name(cursor.getString(6));
                usd.setJid(cursor.getString(1));
                usd.setIs_niftychat_user(cursor.getInt(2));
                usd.setNumber(cursor.getString(4));
                usd.setPhone_label(cursor.getString(8));
                usd.setPhone_type(cursor.getString(7));
                usd.setRaw_contact_id(cursor.getString(5));
                usd.setStatus(cursor.getString(3));
                usd.setUnseen_msg_count(cursor.getInt(9));
                usd.setPhoto_ts(cursor.getBlob(10));
                usd.setNifty_name(cursor.getString(11));
                usd.setNifty_email(cursor.getString(12));

                list.add(usd);
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return list;
    }

    public ArrayList<ADV_group_GetSet> getADV_Group(String jid) {
        ArrayList<ADV_group_GetSet> list = new ArrayList<ADV_group_GetSet>();
        Cursor cursor = myDataBase.rawQuery("Select * from " + TABLE_ADV_GROUP
                + " where jid ='" + jid + "'", null);
        if (cursor.moveToFirst()) {
            do {
                ADV_group_GetSet usd = new ADV_group_GetSet();
                usd.set_ID(cursor.getInt(0));
                usd.setJid(cursor.getString(1));
                usd.setMember_id(cursor.getString(2));
                usd.setGroup_type(cursor.getInt(3));
                usd.setAnswer(cursor.getString(4));
                list.add(usd);
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return list;
    }

    public int getADV_Group_by_and(String jid, String ans) {
        int count = 0;

        Cursor cursor = myDataBase.rawQuery("Select * from " + TABLE_ADV_GROUP
                + " where jid ='" + jid + "' and answer='" + ans + "'", null);
        if (cursor.moveToFirst()) {
            count = cursor.getCount();
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return count;
    }

    public String[] getADV_Group_answers(String jid) {
        String size[];
        Cursor cursor = myDataBase.rawQuery("Select answer from "
                + TABLE_ADV_GROUP + " where jid ='" + jid + "' ", null);
        size = new String[cursor.getCount()];
        if (cursor.moveToFirst()) {
            do {
                size[(cursor.getPosition())] = cursor.getString(0);
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return size;
    }

    public String getADV_Group_ans_multi(String jid, String member_id) {
        String size = "";
        Cursor cursor = myDataBase.rawQuery("Select answer from "
                + TABLE_ADV_GROUP + " where jid ='" + jid
                + "' and member_jid='" + member_id + "'", null);
        if (cursor.moveToFirst()) {
            size = cursor.getString(0);
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return size;
    }

    public int getADV_Group_ans(String jid, String member_id) {
        int size = -1;
        Cursor cursor = myDataBase.rawQuery("Select answer from "
                + TABLE_ADV_GROUP + " where jid ='" + jid
                + "' and member_jid='" + member_id + "'", null);
        if (cursor.moveToFirst()) {
            size = cursor.getInt(0);
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return size;
    }

    public int getADV_Group_is_exist(String jid, String member_id) {
        int size;
        Cursor cursor = myDataBase.rawQuery("Select * from " + TABLE_ADV_GROUP
                + " where jid ='" + jid + "' and member_jid='" + member_id
                + "'", null);
        size = cursor.getCount();
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return size;
    }

    public ArrayList<ContactsGetSet> getAllContacts() {
        ArrayList<ContactsGetSet> list = new ArrayList<ContactsGetSet>();
        Cursor cursor = myDataBase
                .rawQuery(
                        "Select * from contacts where raw_contact_id!='0' order by display_name ",
                        null);
        if (cursor.moveToFirst()) {
            do {
                ContactsGetSet usd = new ContactsGetSet();
                usd.setId(cursor.getInt(0));
                usd.setDisplay_name(cursor.getString(6));
                usd.setJid(cursor.getString(1));
                usd.setNumber(cursor.getString(4));
                usd.setPhone_label(cursor.getString(8));
                usd.setPhone_type(cursor.getString(7));
                usd.setRaw_contact_id(cursor.getString(5));
                usd.setStatus(cursor.getString(3));
                usd.setUnseen_msg_count(cursor.getInt(9));
                usd.setPhoto_ts(cursor.getBlob(10));
                usd.setNifty_name(cursor.getString(11));
                usd.setNifty_email(cursor.getString(12));
                usd.setIs_niftychat_user(cursor.getInt(2));
                list.add(usd);
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return list;
    }

    public ArrayList<ContactsGetSet> getAllContacts_Fileter(String value) {

        ArrayList<ContactsGetSet> list = new ArrayList<ContactsGetSet>();
        Cursor cursor = myDataBase.rawQuery(
                "Select * from contacts where display_name LIKE '" + value
                        + "%' AND raw_contact_id!='0' order by display_name ",
                null);
        if (cursor.moveToFirst()) {
            do {
                ContactsGetSet usd = new ContactsGetSet();
                usd.setId(cursor.getInt(0));
                usd.setDisplay_name(cursor.getString(6));
                usd.setJid(cursor.getString(1));
                usd.setNumber(cursor.getString(4));
                usd.setPhone_label(cursor.getString(8));
                usd.setPhone_type(cursor.getString(7));
                usd.setRaw_contact_id(cursor.getString(5));
                usd.setStatus(cursor.getString(3));
                usd.setUnseen_msg_count(cursor.getInt(9));
                usd.setPhoto_ts(cursor.getBlob(10));
                usd.setNifty_name(cursor.getString(11));
                usd.setNifty_email(cursor.getString(12));
                usd.setIs_niftychat_user(cursor.getInt(2));
                list.add(usd);
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return list;
    }

    public ArrayList<ContactsGetSet> getContacts_selected(String jids) {
        ArrayList<ContactsGetSet> list = new ArrayList<ContactsGetSet>();
        Cursor cursor = myDataBase.rawQuery(
                "Select * from contacts where is_niftychat_user=1 and jid in("
                        + jids + ")", null);
        if (cursor.moveToFirst()) {
            do {
                ContactsGetSet usd = new ContactsGetSet();
                usd.setId(cursor.getInt(0));
                usd.setDisplay_name(cursor.getString(6));
                usd.setJid(cursor.getString(1));
                usd.setNumber(cursor.getString(4));
                usd.setPhone_label(cursor.getString(8));
                usd.setPhone_type(cursor.getString(7));
                usd.setRaw_contact_id(cursor.getString(5));
                usd.setStatus(cursor.getString(3));
                usd.setUnseen_msg_count(cursor.getInt(9));
                usd.setPhoto_ts(cursor.getBlob(10));
                usd.setNifty_name(cursor.getString(11));
                usd.setNifty_email(cursor.getString(12));

                list.add(usd);
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return list;
    }

    public ContactsGetSet getContact(String jid) {

        Cursor cursor = myDataBase.rawQuery(
                "Select * from contacts where jid='" + jid + "'", null);
        ContactsGetSet usd = new ContactsGetSet();
        if (cursor.moveToFirst()) {
            do {

                usd.setId(cursor.getInt(0));
                usd.setDisplay_name(cursor.getString(6));
                usd.setJid(cursor.getString(1));
                usd.setNumber(cursor.getString(4));
                usd.setPhone_label(cursor.getString(8));
                usd.setPhone_type(cursor.getString(7));
                usd.setRaw_contact_id(cursor.getString(5));
                usd.setStatus(cursor.getString(3));
                usd.setUnseen_msg_count(cursor.getInt(9));
                usd.setPhoto_ts(cursor.getBlob(10));
                usd.setNifty_name(cursor.getString(11));
                usd.setNifty_email(cursor.getString(12));
                usd.setIs_niftychat_user(cursor.getInt(2));
                if (cursor.getBlob(10) != null) {
                    Bitmap bmp = BitmapFactory.decodeByteArray(cursor.getBlob(10), 0,
                            cursor.getBlob(10).length);
                    usd.setPhoto_bitmap(bmp);
                }

            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return usd;
    }

    public ContactsGetSet getContact_grp(String jid) {

        Cursor cursor = myDataBase.rawQuery(
                "Select * from contacts where jid='" + jid + "'", null);

        ContactsGetSet usd = null;
        if (cursor.moveToFirst()) {
            do {
                usd = new ContactsGetSet();
                usd.setId(cursor.getInt(0));
                usd.setDisplay_name(cursor.getString(6));
                usd.setJid(cursor.getString(1));
                usd.setNumber(cursor.getString(4));
                usd.setPhone_label(cursor.getString(8));
                usd.setPhone_type(cursor.getString(7));
                usd.setRaw_contact_id(cursor.getString(5));
                usd.setStatus(cursor.getString(3));
                usd.setUnseen_msg_count(cursor.getInt(9));
                usd.setPhoto_ts(cursor.getBlob(10));
                usd.setNifty_name(cursor.getString(11));
                usd.setNifty_email(cursor.getString(12));
                usd.setIs_niftychat_user(cursor.getInt(2));
                usd.setIsInContactList(cursor.getInt(13));
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return usd;
    }

    public ContactsGetSet getContact_2(String jid) {

        Cursor cursor = myDataBase.rawQuery("Select * from contacts where jid="
                + jid + "", null);
        ContactsGetSet usd = null;
        if (cursor.moveToFirst()) {
            do {
                usd = new ContactsGetSet();

                usd.setId(cursor.getInt(0));
                usd.setDisplay_name(cursor.getString(6));
                usd.setJid(cursor.getString(1));
                usd.setNumber(cursor.getString(4));
                usd.setPhone_label(cursor.getString(8));
                usd.setPhone_type(cursor.getString(7));
                usd.setRaw_contact_id(cursor.getString(5));
                usd.setStatus(cursor.getString(3));
                usd.setUnseen_msg_count(cursor.getInt(9));
                usd.setPhoto_ts(cursor.getBlob(10));
                usd.setNifty_name(cursor.getString(11));
                usd.setNifty_email(cursor.getString(12));
                usd.setIs_niftychat_user(cursor.getInt(2));

            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return usd;
    }

    public long setUpdateStatus(String jid, String status) {

        ContentValues values = new ContentValues();

        values.put("status", status);

        String wheres = "jid=?";
        String IDS = String.valueOf(jid);
        String[] whereArgs = {IDS.toString()};
        long rawId = myDataBase.update(TABLE_CONTACTS, values, wheres,
                whereArgs);
        return rawId;

    }

    public long setUpdateUserStatus(String jid, String status) {

        ContentValues values = new ContentValues();

        values.put("status", status);

        String wheres = "user=?";
        String IDS = String.valueOf(jid);
        String[] whereArgs = {IDS.toString()};
        long rawId = myDataBase.update(TABLE_LOGIN, values, wheres, whereArgs);
        return rawId;

    }

    public long setInsertMessages(MessageGetSet message) {

        Constant.printMsg("message.getKey_remote_jid() "
                + message.getKey_remote_jid());
        long l = 0;
        ContentValues values = new ContentValues();

        values.put("data", message.getData());
        values.put("key_id", message.getKey_id());
        values.put("key_remote_jid", message.getKey_remote_jid());
        values.put("key_from_me", message.getKey_from_me());
        values.put("needs_push", message.getNeeds_push());
        values.put("receipt_device_timestamp",
                message.getReceipt_device_timestamp());
        values.put("receipt_server_timestamp",
                message.getReceipt_server_timestamp());
        values.put("received_timestamp", message.getReceived_timestamp());
        values.put("send_timestamp", message.getSend_timestamp());
        values.put("status", message.getStatus());
        values.put("timestamp", message.getTimestamp());
        values.put("media_size", message.getMedia_size());
        values.put("remote_resource", message.getRemote_resource());
        values.put("media_name", message.getMedia_name());
        values.put("media_wa_type", message.getMedia_wa_type());
        values.put("media_url", message.getMedia_url());
        values.put("media_mime_type", message.getMedia_mime_type());
        values.put("media_duration", message.getMedia_duration());
        values.put("latitude", message.getLatitude());
        values.put("longitude", message.getLongitude());
        values.put("origin", message.getOrigin());
        values.put("is_sec_chat", message.getIs_sec_chat());
        values.put("self_des_time", message.getSelf_des_time());
        values.put("is_owner", message.getIs_owner());

        values.put("raw_data", message.getRow_data());
        values.put("flag", 0);
        l = myDataBase.insert(TABLE_MESSAGES, null, values);

        return l;
    }


    public ArrayList<MessageGetSet> getMessages(String jid) {
        ArrayList<MessageGetSet> list = new ArrayList<MessageGetSet>();
        Cursor cursor = myDataBase.rawQuery(
                "Select * from messages where key_remote_jid=" + jid, null);

        if (cursor.moveToFirst()) {
            do {
                MessageGetSet usd = new MessageGetSet();
                usd.set_id(cursor.getInt(0));
                usd.setKey_remote_jid(cursor.getString(1));
                usd.setKey_from_me(cursor.getInt(2));
                usd.setKey_id(cursor.getString(3));
                usd.setStatus(cursor.getInt(4));
                usd.setNeeds_push(cursor.getInt(5));
                usd.setData(cursor.getString(6));
                usd.setTimestamp(cursor.getLong(7));
                usd.setReceived_timestamp(cursor.getLong(18));
                usd.setSend_timestamp(cursor.getLong(19));
                usd.setReceipt_server_timestamp(cursor.getLong(20));
                usd.setReceipt_device_timestamp(cursor.getLong(21));

            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return list;
    }

    public MessageGetSet getMessages_by_msg_id(String id) {

        Cursor cursor = myDataBase.rawQuery("Select * from messages WHERE _id="
                + id, null);
        MessageGetSet usd = new MessageGetSet();
        if (cursor.moveToFirst()) {
            do {

                usd.set_id(cursor.getInt(0));
                usd.setData(cursor.getString(6));
                usd.setKey_from_me(cursor.getInt(2));
                usd.setKey_id(cursor.getString(3));
                usd.setKey_remote_jid(cursor.getString(1));
                usd.setLatitude(cursor.getDouble(14));
                usd.setLongitude(cursor.getDouble(15));
                usd.setMedia_duration(cursor.getInt(24));
                usd.setMedia_hash(cursor.getString(13));
                usd.setMedia_mime_type(cursor.getString(9));
                usd.setMedia_name(cursor.getString(12));
                usd.setMedia_size(cursor.getInt(11));
                usd.setMedia_url(cursor.getString(8));
                usd.setMedia_wa_type(cursor.getString(10));
                usd.setNeeds_push(cursor.getInt(5));
                usd.setOrigin(cursor.getInt(25));
                usd.setReceipt_device_timestamp(cursor.getLong(21));
                usd.setReceipt_server_timestamp(cursor.getLong(20));
                usd.setReceived_timestamp(cursor.getLong(18));
                usd.setRemote_resource(cursor.getString(17));
                usd.setRow_data(cursor.getBlob(22));
                usd.setSend_timestamp(cursor.getLong(19));
                usd.setStatus(cursor.getInt(4));
                usd.setThumb_image(cursor.getBlob(16));
                usd.setTimestamp(cursor.getLong(7));
                usd.setIs_sec_chat(cursor.getInt(26));
                usd.setSelf_des_time(cursor.getLong(27));

            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return usd;
    }

    public ArrayList<MessageGetSet> getUnsendMessages() {
        ArrayList<MessageGetSet> list = new ArrayList<MessageGetSet>();
        Cursor cursor = myDataBase
                .rawQuery(
                        "select * from messages where status=3 and key_from_me=0",
                        null);

        if (cursor.moveToFirst()) {
            do {

                MessageGetSet usd = new MessageGetSet();
                usd.set_id(cursor.getInt(0));
                usd.setData(cursor.getString(6));
                usd.setKey_from_me(cursor.getInt(2));
                usd.setKey_id(cursor.getString(3));
                usd.setKey_remote_jid(cursor.getString(1));
                usd.setLatitude(cursor.getDouble(14));
                usd.setLongitude(cursor.getDouble(15));
                usd.setMedia_duration(cursor.getInt(24));
                usd.setMedia_hash(cursor.getString(13));
                usd.setMedia_mime_type(cursor.getString(9));
                usd.setMedia_name(cursor.getString(12));
                usd.setMedia_size(cursor.getInt(11));
                usd.setMedia_url(cursor.getString(8));
                usd.setMedia_wa_type(cursor.getString(10));
                usd.setNeeds_push(cursor.getInt(5));
                usd.setOrigin(cursor.getInt(25));
                usd.setReceipt_device_timestamp(cursor.getLong(21));
                usd.setReceipt_server_timestamp(cursor.getLong(20));
                usd.setReceived_timestamp(cursor.getLong(18));
                usd.setRemote_resource(cursor.getString(17));
                usd.setRow_data(cursor.getBlob(22));
                usd.setSend_timestamp(cursor.getLong(19));
                usd.setStatus(cursor.getInt(4));
                usd.setThumb_image(cursor.getBlob(16));
                usd.setTimestamp(cursor.getLong(7));
                usd.setIs_sec_chat(cursor.getInt(26));
                usd.setSelf_des_time(cursor.getLong(27));
                list.add(usd);

            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return list;
    }

    public ArrayList<MessageGetSet> getMessages_by_jid(String id) {
        ArrayList<MessageGetSet> list = new ArrayList<MessageGetSet>();
        Cursor cursor = myDataBase.rawQuery(
                "Select * from messages WHERE key_remote_jid='" + id + "'",
                null);

        if (cursor.moveToFirst()) {
            do {
                MessageGetSet usd = new MessageGetSet();

                usd.set_id(cursor.getInt(0));
                usd.setData(cursor.getString(6));
                usd.setKey_from_me(cursor.getInt(2));
                usd.setKey_id(cursor.getString(3));
                usd.setKey_remote_jid(cursor.getString(1));
                usd.setLatitude(cursor.getDouble(14));
                usd.setLongitude(cursor.getDouble(15));
                usd.setMedia_duration(cursor.getInt(24));
                usd.setMedia_hash(cursor.getString(13));
                usd.setMedia_mime_type(cursor.getString(9));
                usd.setMedia_name(cursor.getString(12));
                usd.setMedia_size(cursor.getInt(11));
                usd.setMedia_url(cursor.getString(8));
                usd.setMedia_wa_type(cursor.getString(10));
                usd.setNeeds_push(cursor.getInt(5));
                usd.setOrigin(cursor.getInt(25));
                usd.setReceipt_device_timestamp(cursor.getLong(21));
                usd.setReceipt_server_timestamp(cursor.getLong(20));
                usd.setReceived_timestamp(cursor.getLong(18));
                usd.setRemote_resource(cursor.getString(17));
                usd.setRow_data(cursor.getBlob(22));
                usd.setSend_timestamp(cursor.getLong(19));
                usd.setStatus(cursor.getInt(4));
                usd.setThumb_image(cursor.getBlob(16));
                usd.setTimestamp(cursor.getLong(7));
                usd.setIs_sec_chat(cursor.getInt(26));
                usd.setSelf_des_time(cursor.getLong(27));

                list.add(usd);
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return list;
    }

    public String getMessageDataById(String msg_id) {
        String data = null;
        Cursor cursor = myDataBase.rawQuery(
                "Select data from messages WHERE _id='" + msg_id + "'", null);

        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    data = cursor.getString(cursor.getColumnIndex("data"));
                }
            } catch (Exception e) {

            } finally {
                cursor.close();
            }


        }


        return data;
    }

    public String getMessageDataByIdForBroad(String msg_id) {
        String data = null;
        Cursor cursor = myDataBase.rawQuery(
                "Select data from messages WHERE key_id='" + msg_id + "'", null);

        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    data = cursor.getString(cursor.getColumnIndex("data"));
                }
            } catch (Exception e) {

            } finally {
                cursor.close();
            }


        }


        return data;
    }

    public MessageGetSet getMessages_by_key_id(String id) {

        Cursor cursor = myDataBase.rawQuery(
                "Select * from messages WHERE key_id='" + id + "'", null);
        MessageGetSet usd = new MessageGetSet();
        if (cursor.moveToFirst()) {
            do {

                usd.set_id(cursor.getInt(0));
                usd.setData(cursor.getString(6));
                usd.setKey_from_me(cursor.getInt(2));
                usd.setKey_id(cursor.getString(3));
                usd.setKey_remote_jid(cursor.getString(1));
                usd.setLatitude(cursor.getDouble(14));
                usd.setLongitude(cursor.getDouble(15));
                usd.setMedia_duration(cursor.getInt(24));
                usd.setMedia_hash(cursor.getString(13));
                usd.setMedia_mime_type(cursor.getString(9));
                usd.setMedia_name(cursor.getString(12));
                usd.setMedia_size(cursor.getInt(11));
                usd.setMedia_url(cursor.getString(8));
                usd.setMedia_wa_type(cursor.getString(10));
                usd.setNeeds_push(cursor.getInt(5));
                usd.setOrigin(cursor.getInt(25));
                usd.setReceipt_device_timestamp(cursor.getLong(21));
                usd.setReceipt_server_timestamp(cursor.getLong(20));
                usd.setReceived_timestamp(cursor.getLong(18));
                usd.setRemote_resource(cursor.getString(17));
                usd.setRow_data(cursor.getBlob(22));
                usd.setSend_timestamp(cursor.getLong(19));
                usd.setStatus(cursor.getInt(4));
                usd.setThumb_image(cursor.getBlob(16));
                usd.setTimestamp(cursor.getLong(7));
                usd.setIs_sec_chat(cursor.getInt(26));
                usd.setSelf_des_time(cursor.getLong(27));

            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return usd;
    }

    public ArrayList<MessageGetSet> getMessages_group_by_jid() {
        ArrayList<MessageGetSet> list = new ArrayList<MessageGetSet>();
        Cursor cursor = myDataBase.rawQuery(
                "Select * from messages group by key_remote_jid", null);

        if (cursor.moveToFirst()) {
            do {
                MessageGetSet usd = new MessageGetSet();
                usd.set_id(cursor.getInt(0));
                usd.setData(cursor.getString(6));
                usd.setKey_from_me(cursor.getInt(2));
                usd.setKey_id(cursor.getString(3));
                usd.setKey_remote_jid(cursor.getString(1));
                usd.setLatitude(cursor.getDouble(14));
                usd.setLongitude(cursor.getDouble(15));
                usd.setMedia_duration(cursor.getInt(24));
                usd.setMedia_hash(cursor.getString(13));
                usd.setMedia_mime_type(cursor.getString(9));
                usd.setMedia_name(cursor.getString(12));
                usd.setMedia_size(cursor.getInt(11));
                usd.setMedia_url(cursor.getString(8));
                usd.setMedia_wa_type(cursor.getString(10));
                usd.setNeeds_push(cursor.getInt(5));
                usd.setOrigin(cursor.getInt(25));
                usd.setReceipt_device_timestamp(cursor.getLong(21));
                usd.setReceipt_server_timestamp(cursor.getLong(20));
                usd.setReceived_timestamp(cursor.getLong(18));
                usd.setRemote_resource(cursor.getString(17));
                usd.setRow_data(cursor.getBlob(22));
                usd.setSend_timestamp(cursor.getLong(19));
                usd.setStatus(cursor.getInt(4));
                usd.setThumb_image(cursor.getBlob(16));
                usd.setTimestamp(cursor.getLong(7));
                usd.setIs_sec_chat(cursor.getInt(26));
                usd.setSelf_des_time(cursor.getLong(27));
                list.add(usd);

            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return list;
    }

    public Cursor getMessagesCursor_Chat(String jid, int is_sec_chat) {

        Cursor cursor = myDataBase.rawQuery(
                "Select * from messages where key_remote_jid='" + jid
                        + "' and is_sec_chat=" + is_sec_chat + "", null);

        return cursor;
    }

    public Cursor getMessagesCursor(String jid) {

        Cursor cursor = myDataBase.rawQuery(
                "Select * from messages where key_remote_jid='" + jid + "'",
                null);

        return cursor;
    }

    public ArrayList<MessageGetSet> getUnreadMessages(String jid) {
        ArrayList<MessageGetSet> list = new ArrayList<MessageGetSet>();
        Cursor cursor = myDataBase.rawQuery(
                "Select * from messages where key_remote_jid='" + jid
                        + "' and status!=-1 and key_from_me=1", null);

        if (cursor.moveToFirst()) {
            do {
                MessageGetSet usd = new MessageGetSet();
                usd.set_id(cursor.getInt(0));
                usd.setKey_remote_jid(cursor.getString(1));
                usd.setKey_from_me(cursor.getInt(2));
                usd.setKey_id(cursor.getString(3));
                usd.setStatus(cursor.getInt(4));
                usd.setNeeds_push(cursor.getInt(5));
                usd.setData(cursor.getString(6));
                usd.setTimestamp(cursor.getLong(7));
                usd.setReceived_timestamp(cursor.getLong(18));
                usd.setSend_timestamp(cursor.getLong(19));
                usd.setReceipt_server_timestamp(cursor.getLong(20));
                usd.setReceipt_device_timestamp(cursor.getLong(21));
                usd.setMedia_size(cursor.getInt(11));
                usd.setRemote_resource(cursor.getString(17));

                list.add(usd);
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return list;
    }

    public long setUpdateAdv_group_and(ADV_group_GetSet advgetset) {

        ContentValues values = new ContentValues();

        values.put("answer", advgetset.getAnswer());

        String wheres = "jid=? and member_jid=?";
        String IDS = String.valueOf(advgetset.getJid());
        String[] whereArgs = {IDS.toString(), advgetset.getMember_id()};
        long rawId = myDataBase.update(TABLE_ADV_GROUP, values, wheres,
                whereArgs);
        return rawId;

    }

    public long setUpdateMessage_need_push(String key_id, int status) {

        ContentValues values = new ContentValues();

        values.put("needs_push", status);

        String wheres = "key_id=?";

        String[] whereArgs = {key_id};
        long rawId = myDataBase.update(TABLE_MESSAGES, values, wheres,
                whereArgs);
        return rawId;

    }

    public long setUpdateMessage_need_pushById(String id, int status) {

        ContentValues values = new ContentValues();

        values.put("needs_push", status);

        String wheres = "_id=?";

        String[] whereArgs = {id};
        long rawId = myDataBase.update(TABLE_MESSAGES, values, wheres,
                whereArgs);
        return rawId;

    }

    public long setUpdateMessage_display(String jid, String key_id) {

        ContentValues values = new ContentValues();

        values.put("status", 0);
        values.put("needs_push", 0);

        String wheres = "key_remote_jid=? and key_id=?";
        String IDS = String.valueOf(jid);
        String[] whereArgs = {IDS.toString(), key_id};
        long rawId = myDataBase.update(TABLE_MESSAGES, values, wheres,
                whereArgs);
        return rawId;

    }

    public long setUpdate_unseen_message_count(String jid, int is_sec_chat) {

        ContentValues values = new ContentValues();

        values.put("unseen_msg_count", 0);

        String wheres = "key_remote_jid=? and is_sec_chat=?";
        String IDS = String.valueOf(jid);
        String sec = String.valueOf(is_sec_chat);
        String[] whereArgs = {IDS.toString(), sec};
        long rawId = myDataBase.update(TABLE_CHAT_LIST, values, wheres,
                whereArgs);
        return rawId;

    }

    public ArrayList<MessageGetSet> getPushMessages() {
        ArrayList<MessageGetSet> list = new ArrayList<MessageGetSet>();
        Cursor cursor = myDataBase
                .rawQuery(
                        "Select * from messages where status=1 and key_from_me=1 and needs_push=1",
                        null);

        if (cursor.moveToFirst()) {
            do {
                MessageGetSet usd = new MessageGetSet();
                usd.set_id(cursor.getInt(0));
                usd.setKey_remote_jid(cursor.getString(1));
                usd.setKey_from_me(cursor.getInt(2));
                usd.setKey_id(cursor.getString(3));
                usd.setStatus(cursor.getInt(4));
                usd.setNeeds_push(cursor.getInt(5));
                usd.setData(cursor.getString(6));
                usd.setTimestamp(cursor.getLong(7));
                usd.setReceived_timestamp(cursor.getLong(18));
                usd.setSend_timestamp(cursor.getLong(19));
                usd.setReceipt_server_timestamp(cursor.getLong(20));
                usd.setReceipt_device_timestamp(cursor.getLong(21));

                list.add(usd);
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return list;
    }

    public Chat_list_GetSet getChat_List(String jid) {
        Chat_list_GetSet chat_list = null;
        Cursor cursor = myDataBase.rawQuery(
                "Select * from chat_list where key_remote_jid='" + jid + "'",
                null);

        if (cursor.moveToFirst()) {
            do {
                chat_list = new Chat_list_GetSet();
                chat_list.set_id(cursor.getInt(0));
                chat_list.setKey_remote_jid(cursor.getString(1));
                chat_list.setMessage_table_id(cursor.getInt(2));
                chat_list.setIs_sec_chat(cursor.getInt(3));
                chat_list.setUnseen_msg_count(cursor.getInt(4));
                chat_list.setTimestamp(cursor.getLong(5));

            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return chat_list;
    }

    public Boolean isExistinChatList(String jid) {
        Boolean bool = false;

        Cursor cursor = myDataBase.rawQuery(
                "Select * from chat_list where key_remote_jid='" + jid + "' ",
                null);

        if (cursor.getCount() > 0) {
            bool = true;
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return bool;

    }

    public Boolean isContactExist(String jid) {
        Boolean bool = false;

        Cursor cursor = myDataBase.rawQuery(
                "Select * from contacts where jid='" + jid + "' ", null);

        if (cursor.getCount() > 0) {
            bool = true;
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return bool;

    }

    public Boolean isExistinChatList_chat(String jid, int sec) {
        Boolean bool = false;

        Cursor cursor = myDataBase.rawQuery(
                "Select * from chat_list where key_remote_jid='" + jid
                        + "' and is_sec_chat=" + sec + "", null);

        if (cursor.getCount() > 0) {
            bool = true;
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return bool;

    }

    public int getLastMsgid(String jid) {
        int id = 0;

        Cursor cursor = myDataBase.rawQuery(
                "Select max(_id) from messages where key_remote_jid='" + jid
                        + "'", null);

        if (cursor.moveToFirst()) {
            do {

                id = cursor.getInt(0);

            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return id;
    }

    public int getLastMsgid_chat(String jid, int sec) {
        int id = 0;

        Cursor cursor = myDataBase.rawQuery(
                "Select max(_id) from messages where key_remote_jid='" + jid
                        + "' and is_sec_chat=" + sec + "", null);

        if (cursor.moveToFirst()) {
            do {

                id = cursor.getInt(0);

            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return id;
    }

    public int getMessegeCount(String jid, int sec) {

        Cursor cursor = myDataBase.rawQuery(
                "Select * from messages where key_remote_jid='" + jid
                        + "' and is_sec_chat=" + sec + "", null);
        if (cursor != null) {
            return cursor.getCount();
        } else {
            return 0;
        }

    }

    public int getLastMsgid_chat_grp(String jid) {
        int id = 0;

        Cursor cursor = myDataBase.rawQuery(
                "Select max(_id) from messages where key_remote_jid='" + jid
                        + "' ", null);

        if (cursor.moveToFirst()) {
            do {

                id = cursor.getInt(0);

            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return id;
    }

    public int getunseen_msg(String jid) {
        int count = 0;

        Cursor cursor = myDataBase
                .rawQuery(
                        "Select _id from messages where status=1 and key_from_me=1 and key_remote_jid='"
                                + jid + "'", null);

        count = cursor.getCount();
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return count;
    }

    public int getunseen_msg(String jid, int sec) {
        int count = 0;

        Cursor cursor = myDataBase
                .rawQuery(
                        "Select _id from messages where status=1 and key_from_me=1 and key_remote_jid='"
                                + jid + "' and is_sec_chat=" + sec + " ", null);

        count = cursor.getCount();
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return count;
    }

    public long setUpdateChat_lits(String jid, int msg_id) {

        ContentValues values = new ContentValues();

        values.put("message_table_id", msg_id);

        String wheres = "key_remote_jid=?";
        String IDS = String.valueOf(jid);
        String[] whereArgs = {IDS.toString()};
        long rawId = myDataBase.update(TABLE_CHAT_LIST, values, wheres,
                whereArgs);
        return rawId;

    }

    public long setUpdateChat_lits_timestamp(String jid, String timestamp) {

        ContentValues values = new ContentValues();

        values.put("creation_time", timestamp);

        String wheres = "key_remote_jid=?";
        String IDS = String.valueOf(jid);
        String[] whereArgs = {IDS.toString()};
        long rawId = myDataBase.update(TABLE_CHAT_LIST, values, wheres,
                whereArgs);
        return rawId;

    }

    public long setUpdateChat_lits_chat(String jid, int msg_id, int sec) {

        ContentValues values = new ContentValues();

        values.put("message_table_id", msg_id);

        String wheres = "key_remote_jid=? and is_sec_chat=?";
        String IDS = String.valueOf(jid);
        String is_sec = String.valueOf(sec);
        String[] whereArgs = {IDS.toString(), is_sec};
        long rawId = myDataBase.update(TABLE_CHAT_LIST, values, wheres,
                whereArgs);
        return rawId;

    }

    public long setUpdateContact_unseen_msg(String jid, int msg) {

        ContentValues values = new ContentValues();

        values.put("unseen_msg_count", msg);

        String wheres = "jid=?";
        String IDS = String.valueOf(jid);
        String[] whereArgs = {IDS.toString()};
        long rawId = myDataBase.update(TABLE_CONTACTS, values, wheres,
                whereArgs);
        return rawId;

    }

    public long setUpdateContact_unseen_msg_chat(String jid, int msg, int sec) {

        ContentValues values = new ContentValues();

        values.put("unseen_msg_count", msg);

        String wheres = "key_remote_jid=? and is_sec_chat=?";
        String IDS = String.valueOf(jid);
        String is_sec = String.valueOf(sec);
        String[] whereArgs = {IDS.toString(), is_sec};
        long rawId = myDataBase.update(TABLE_CHAT_LIST, values, wheres,
                whereArgs);
        return rawId;

    }

    public long setUpdateContact_unseen_msg_chat_grp(String jid, int msg) {

        ContentValues values = new ContentValues();

        values.put("unseen_msg_count", msg);

        String wheres = "key_remote_jid=?";
        String IDS = String.valueOf(jid);

        String[] whereArgs = {IDS.toString()};
        long rawId = myDataBase.update(TABLE_CHAT_LIST, values, wheres,
                whereArgs);
        return rawId;

    }

    public Cursor getChat_list() {

        Cursor cursor = myDataBase
                .rawQuery(
                        "Select m._id,c.display_name,c.photo_ts,m.data,m.timestamp,m.status,m.needs_push,ch.unseen_msg_count,c.jid,m.key_from_me ,m.media_wa_type,m.is_sec_chat,m.is_owner from messages m,contacts c,chat_list ch where c.jid=m.key_remote_jid and c.raw_contact_id !='0' and m._id=ch.message_table_id group by m.is_sec_chat, m.key_remote_jid order by m.timestamp desc ",
                        null);
        return cursor;
    }

    public long setUpdateArchive(String jid) {
        Constant.printMsg("test jid in db::" + jid);
        ContentValues values = new ContentValues();

        values.put("flag", 1);

        String wheres = "key_remote_jid=?";
        String IDS = String.valueOf(jid);
        String[] whereArgs = {IDS.toString()};
        long rawId = myDataBase.update(TABLE_MESSAGES, values, wheres,
                whereArgs);
        Constant.printMsg("test updated:: flag::" + values + wheres
                + whereArgs);
        return rawId;

    }


    public long setUpdateFlagForHomeScreen(String jid) {
        Constant.printMsg("test jid in db::" + jid);
        ContentValues values = new ContentValues();

        values.put("flag", 2);

        String wheres = "flag=='3'";

        long rawId = myDataBase.update(TABLE_MESSAGES, values, wheres,
                null);
        Constant.printMsg("test updated:: flag::" + values + wheres
        );
        return rawId;

    }

    public long setUpdateFlagForHomeScreen_ListItems() {
        ContentValues values = new ContentValues();

        values.put("flag", 3);

        String qry = "Select m.key_remote_jid from chat_list ch," +
                "contacts c,messages m where" +
                " m._id=ch.message_table_id " +
                "and  ch.key_remote_jid =c.jid " +
                "or c.jid=m.key_remote_jid" +
                " and " +
                "c.raw_contact_id !='0' " +
                "and m._id=ch.message_table_id " +
                "and m.flag =='0' " +
                "group by m.is_sec_chat, m.key_remote_jid " +
                "order by m.timestamp desc ";


        String wheres = "flag=(?)";

        String IDS = String.valueOf(qry);
        String[] whereArgs = {IDS.toString()};

        long rawId = myDataBase.update(TABLE_MESSAGES, values, wheres,
                whereArgs);
        Constant.printMsg("test updated:: flag::" + values + wheres + whereArgs + "--" + rawId
        );
        return rawId;

    }

    public long setUpdateGroupArchive(String jid) {
        Constant.printMsg("test jid in db::" + jid);
        ContentValues values = new ContentValues();

        values.put("flag", 1);

        String wheres = "key_remote_jid=?";
        String IDS = String.valueOf(jid);
        String[] whereArgs = {IDS.toString()};
        long rawId = myDataBase.update(TABLE_MESSAGES, values, wheres,
                whereArgs);
        Constant.printMsg("test updated:: flag::" + values + wheres
                + whereArgs);
        return rawId;

    }

    // added archive
    // public Cursor getChat_list_searchview(String value) {
    //
    // Cursor cursor = null;
    // if (value == null) {
    // cursor = myDataBase
    // .rawQuery(
    // "Select m._id,c.display_name,c.photo_ts,m.data,m.timestamp,m.status,m.needs_push,ch.unseen_msg_count,c.jid,m.key_from_me ,m.media_wa_type,m.is_sec_chat,m.is_owner,c.is_niftychat_user from messages m,contacts c,chat_list ch where c.jid=m.key_remote_jid and c.raw_contact_id !='0' and m._id=ch.message_table_id and m.flag =='0' group by m.is_sec_chat, m.key_remote_jid order by m.timestamp desc ",
    // null);
    // String qry =
    // "Select m._id,c.display_name,c.photo_ts,m.data,m.timestamp,m.status,m.needs_push,ch.unseen_msg_count,c.jid,m.key_from_me ,m.media_wa_type,m.is_sec_chat,m.is_owner,c.is_niftychat_user from messages m,contacts c,chat_list ch where c.jid=m.key_remote_jid and c.raw_contact_id !='0' and m._id=ch.message_table_id and m.flag =='0' group by m.is_sec_chat, m.key_remote_jid order by m.timestamp desc ";
    // CommonMethods
    // .prtMsg("string qry is in datbase query:::in if" + qry);
    // } else {
    // cursor = myDataBase
    // .rawQuery(
    // "Select m._id,c.display_name,c.photo_ts,m.data,m.timestamp,m.status,m.needs_push,ch.unseen_msg_count,c.jid,m.key_from_me ,m.media_wa_type,m.is_sec_chat,m.is_owner,c.is_niftychat_user from messages m,contacts c,chat_list ch where c.display_name like '"
    // + value
    // +
    // "%' and c.jid=m.key_remote_jid and c.raw_contact_id !='0' and m._id=ch.message_table_id and flag =='0' group by m.is_sec_chat, m.key_remote_jid  order by m.timestamp desc ",
    //
    // null);
    // String qry =
    // "Select m._id,c.display_name,c.photo_ts,m.data,m.timestamp,m.status,m.needs_push,ch.unseen_msg_count,c.jid,m.key_from_me ,m.media_wa_type,m.is_sec_chat,m.is_owner,c.is_niftychat_user from messages m,contacts c,chat_list ch where c.display_name like '"
    // + value
    // +
    // "%' and c.jid=m.key_remote_jid and c.raw_contact_id !='0' and m._id=ch.message_table_id and flag =='0' group by m.is_sec_chat, m.key_remote_jid  order by m.timestamp desc ";
    //
    // Constant.printMsg("string qry is in datbase query:::" + qry);
    // }
    //
    // return cursor;
    // }
    public Cursor getChat_list_searchview(String value) {

        Cursor cursor = null;
        if (value == null) {
            cursor = myDataBase
                    .rawQuery(
                            "Select ch._id,c.display_name,c.photo_ts,m.data,m.timestamp,m.status,m.needs_push,ch.unseen_msg_count," +
                                    " c.jid,m.key_from_me ,m.media_wa_type,m.is_sec_chat,m.is_owner,c.is_niftychat_user,m.flag from chat_list ch," +
                                    "contacts c,messages m where" +
                                    " m._id=ch.message_table_id " +
                                    "and  ch.key_remote_jid =c.jid " +
                                    "or c.jid=m.key_remote_jid" +
                                    " and " +
                                    "c.raw_contact_id !='0' " +
                                    "and m._id=ch.message_table_id " +
                                    "and m.flag =='0' " +
                                    "group by m.is_sec_chat, m.key_remote_jid " +
                                    "order by m.timestamp desc ",
                            null);

            String qry = "Select ch._id,c.display_name,c.photo_ts,m.data,m.timestamp,m.status,m.needs_push,ch.unseen_msg_count,c.jid,m.key_from_me ,m.media_wa_type,m.is_sec_chat,m.is_owner,c.is_niftychat_user from chat_list ch,contacts c,messages m where m._id=ch.message_table_id or m._id=ch.message_table_id-1 and  ch.key_remote_jid =c.jid or c.jid=m.key_remote_jid and c.raw_contact_id !='0' and m._id=ch.message_table_id or m._id=ch.message_table_id-1 and m.flag =='0' group by m.is_sec_chat, m.key_remote_jid order by m.timestamp desc ";
            Constant.printMsg("string qry is in datbase query:::in if" + qry);
        } else {
            cursor = myDataBase
                    .rawQuery("Select ch._id,c.display_name,c.photo_ts,m.data,m.timestamp,m.status,m.needs_push,ch.unseen_msg_count," +
                                    "c.jid,m.key_from_me ,m.media_wa_type,m.is_sec_chat,m.is_owner,c.is_niftychat_user from chat_list ch," +
                                    "contacts c,messages m where c.display_name like '"
                                    + value + "%' and m._id=ch.message_table_id and  ch.key_remote_jid =c.jid "
                                    + "OR c.number like '%"
                                    + value + "%' and m._id=ch.message_table_id and  ch.key_remote_jid =c.jid",
                            null);
            String qry = "Select ch._id,c.display_name,c.photo_ts,m.data,m.timestamp,m.status,m.needs_push,ch.unseen_msg_count," +
                    "c.jid,m.key_from_me ,m.media_wa_type,m.is_sec_chat,m.is_owner,c.is_niftychat_user from chat_list ch," +
                    "contacts c,messages m where  c.display_name like '"
                    + value + "%' and m._id=ch.message_table_id and  ch.key_remote_jid =c.jid";

            Constant.printMsg("string qry is in datbase query:::" + qry);
        }

        Constant.printMsg("Coooooo:::" + cursor.getCount());
        return cursor;
    }

    public void getSingleChatData(String value) {

        Cursor cursor = null;
        cursor = myDataBase
                .rawQuery("Select ch._id,c.display_name,c.photo_ts,m.data,m.timestamp,m.status,m.needs_push,ch.unseen_msg_count," +
                                "c.jid,m.key_from_me ,m.media_wa_type,m.is_sec_chat,m.is_owner,c.is_niftychat_user from chat_list ch," +
                                "contacts c,messages m where c.jid like '"
                                + value + "%' and m._id=ch.message_table_id and  ch.key_remote_jid =c.jid "
                                + "OR c.number like '%"
                                + value + "%' and m._id=ch.message_table_id and  ch.key_remote_jid =c.jid",
                        null);
    }

    // public Cursor getChat_list_searchview(String value) {
    //
    // Cursor cursor = null;
    // if (value == null) {
    // cursor = myDataBase
    // .rawQuery(
    // "Select m._id,c.display_name,c.photo_ts,m.data,m.timestamp,m.status,m.needs_push,ch.unseen_msg_count,c.jid,m.key_from_me ,m.media_wa_type,m.is_sec_chat,m.is_owner,c.is_niftychat_user from messages m,contacts c,chat_list ch where c.jid=m.key_remote_jid and c.raw_contact_id !='0' and m._id=ch.message_table_id group by m.is_sec_chat, m.key_remote_jid order by m.timestamp desc ",
    // null);
    // } else {
    // cursor = myDataBase
    // .rawQuery(
    // "Select m._id,c.display_name,c.photo_ts,m.data,m.timestamp,m.status,m.needs_push,ch.unseen_msg_count,c.jid,m.key_from_me ,m.media_wa_type,m.is_sec_chat,m.is_owner,c.is_niftychat_user from messages m,contacts c,chat_list ch where c.display_name like '"
    // + value
    // +
    // "%' and c.jid=m.key_remote_jid and c.raw_contact_id !='0' and m._id=ch.message_table_id group by m.is_sec_chat, m.key_remote_jid  order by m.timestamp desc ",
    // null);
    // }
    //
    // return cursor;
    // }

    public Cursor fetchCountriesByName1(String inputText) {

        Cursor mCursor = null;
        if (inputText == null || inputText.length() == 0) {
            mCursor = myDataBase
                    .rawQuery(
                            " Select m._id,c.display_name,c.photo_ts,m.data,m.timestamp,m.status,m.needs_push,ch.unseen_msg_count,c.jid,m.key_from_me ,m.media_wa_type,m.is_sec_chat,m.is_owner from messages m,contacts c,chat_list ch where c.jid=m.key_remote_jid and c.raw_contact_id !='0' and m._id=ch.message_table_id group by m.is_sec_chat, m.key_remote_jid order by m.timestamp desc ",
                            null);

        } else {
            mCursor = myDataBase
                    .rawQuery(
                            "Select m._id,c.display_name,c.photo_ts,m.data,m.timestamp,m.status,m.needs_push,ch.unseen_msg_count,c.jid,m.key_from_me ,m.media_wa_type,m.is_sec_chat,m.is_owner from messages m,contacts c,chat_list ch where c.jid=m.key_remote_jid and c.raw_contact_id !='0' and m._id=ch.message_table_id group by m.is_sec_chat, m.key_remote_jid and c.display_name LIKE ? order by m.timestamp desc ",
                            new String[]{inputText + "%"});
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    public Cursor getMUCChat_list() {

        Cursor cursor = myDataBase
                .rawQuery(
                        "Select c._id,con.display_name,con.photo_ts,m.data,m.timestamp,m.status,m.needs_push,con.unseen_msg_count,con.jid,m.key_from_me,m.media_wa_type,m.remote_resource from chat_list c,messages m,contacts con where m._id=c.message_table_id and  c.key_remote_jid =con.jid  and m.remote_resource!=''",
                        null);

        return cursor;
    }

    public Cursor getMUCChat_list_Filter(String value) {

        Cursor cursor = null;
        if (value == null) {
            cursor = myDataBase
                    .rawQuery(
                            "Select c._id,con.display_name,con.photo_ts,m.data,m.timestamp,m.status,m.needs_push,con.unseen_msg_count,con.jid,m.key_from_me,m.media_wa_type,m.remote_resource,m.flag from chat_list c,messages m,contacts con where m._id=c.message_table_id and m.flag =='0' and  c.key_remote_jid =con.jid  and m.remote_resource!=''and con.is_niftychat_user !='3' order by m.timestamp desc",
                            null);
        } else {
            cursor = myDataBase
                    .rawQuery(
                            "Select c._id,con.display_name,con.photo_ts,m.data,m.timestamp,m.status,m.needs_push,con.unseen_msg_count,con.jid,m.key_from_me,m.media_wa_type,m.remote_resource from chat_list c,messages m,contacts con where con.display_name LIKE '"
                                    + value
                                    + "%' and m._id=c.message_table_id and flag =='0' and  c.key_remote_jid =con.jid  and m.remote_resource!='' and con.is_niftychat_user !='3' order by m.timestamp desc ",
                            null);
        }

        return cursor;
    }

    public ArrayList<String> get_All_MUC_list_jid() {
        ArrayList<String> list = new ArrayList<String>();

        Cursor cursor = null;

        cursor = myDataBase
                .rawQuery(
                        "Select con.jid from chat_list c,messages m,contacts con where m._id=c.message_table_id and m.flag =='0' and  c.key_remote_jid =con.jid  and m.remote_resource!=''and con.is_niftychat_user !='3' order by m.timestamp desc",
                        null);
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    String s = cursor.getString(cursor.getColumnIndex("jid"));
                    list.add(s);

                } while (cursor.moveToNext());
            }

        }

        cursor.close();

        return list;
    }

    public long setInsertChat_list(String jid, int msg_id) {
        long l = 0;
        ContentValues values = new ContentValues();

        values.put("key_remote_jid", jid);
        values.put("message_table_id", msg_id);

        l = myDataBase.insert(TABLE_CHAT_LIST, null, values);

        return l;
    }

    public long setInsertChat_list(String jid, int msg_id, String creation_time) {
        long l = 0;
        ContentValues values = new ContentValues();

        values.put("key_remote_jid", jid);
        values.put("message_table_id", msg_id);
        values.put("creation_time", creation_time);

        l = myDataBase.insert(TABLE_CHAT_LIST, null, values);

        return l;
    }

    public long setInsertChat_list_chat(String jid, int msg_id, int sec) {
        long l = 0;
        ContentValues values = new ContentValues();

        values.put("key_remote_jid", jid);
        values.put("message_table_id", msg_id);
        values.put("is_sec_chat", sec);
        values.put("unseen_msg_count", 0);

        l = myDataBase.insert(TABLE_CHAT_LIST, null, values);

        return l;
    }

    public long setInsertChat_list_chat_all(String jid, int msg_id, int sec,
                                            int unseen_msg_count) {
        long l = 0;
        ContentValues values = new ContentValues();

        values.put("key_remote_jid", jid);
        values.put("message_table_id", msg_id);
        values.put("is_sec_chat", sec);
        values.put("unseen_msg_count", unseen_msg_count);

        l = myDataBase.insert(TABLE_CHAT_LIST, null, values);

        return l;
    }

    public Boolean isjidExist(String jid) {
        Boolean bool = false;
        Cursor cursor = myDataBase.rawQuery(
                "Select id from contacts where jid='" + jid + "'", null);
        if (cursor.getCount() > 0) {
            bool = true;
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return bool;

    }

    public Boolean isNiftyChatUser(String jid) {
        Boolean bool = false;
        Cursor cursor = myDataBase.rawQuery(
                "Select id from contacts where jid='" + jid
                        + "' and is_niftychat_user=1", null);

        if (cursor.getCount() > 0) {
            bool = true;
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return bool;

    }

    public Boolean isPhoneNoExist(String jid) {
        Boolean bool = false;
        Cursor cursor = myDataBase.rawQuery(
                "Select id from contacts where jid='" + jid
                        + "' and is_niftychat_user=1", null);

        if (cursor.getCount() > 0) {
            bool = true;
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return bool;

    }

    public long setDeleteContact(String jid) {

        String wheres = "jid=?";
        String IDS = String.valueOf(jid);
        String[] whereArgs = {IDS.toString()};
        long rawId = myDataBase.delete(TABLE_CONTACTS, wheres, whereArgs);
        return rawId;

    }

    public long setDeleteContactTable() {

        // String wheres = "jid=?";
        // String IDS = String.valueOf(jid);
        // String[] whereArgs = { IDS.toString() };
        long rawId = myDataBase.delete(TABLE_CONTACTS, null, null);
        return rawId;

    }

    public long setDeleteMessages(String jid) {
        Constant.printMsg("group dest :::::::::12" + jid);

        String wheres = " key_remote_jid=?";
        String IDS = String.valueOf(jid);
        String[] whereArgs = {IDS.toString()};
        long rawId = myDataBase.delete(TABLE_MESSAGES, wheres, whereArgs);
        return rawId;

    }

    public long setGroupDeleteMessages(String jid) {
        Constant.printMsg("group dest :::::::::13" + jid);

        String wheres = " key_remote_jid=? and media_wa_type != '9'";
        String IDS = String.valueOf(jid);
        String[] whereArgs = {IDS.toString()};
        long rawId = myDataBase.delete(TABLE_MESSAGES, wheres, whereArgs);
        return rawId;

    }

    public long setDeleteMessages_Non_secret_chat(String jid) {
        Constant.printMsg("group dest :::::::::123" + jid);

        String wheres = " key_remote_jid=? and is_sec_chat==1";
        String IDS = String.valueOf(jid);
        String[] whereArgs = {IDS.toString()};
        long rawId = myDataBase.delete(TABLE_MESSAGES, wheres, whereArgs);
        return rawId;

    }

    public long setDeleteChatList(String jid) {
        Constant.printMsg("group dest :::::::::12v" + jid);

        String wheres = " key_remote_jid=?";
        String IDS = String.valueOf(jid);
        String[] whereArgs = {IDS.toString()};
        long rawId = myDataBase.delete(TABLE_CHAT_LIST, wheres, whereArgs);
        return rawId;

    }

    public long setDelete_All_Messages() {
        Constant.printMsg("group dest :::::::::12n");

        String wheres = "media_wa_type IN (?,?,?,?,?,?,?)";
        String[] whereArgs = {"0", "1", "2", "3", "4", "5", "6"};
        long rawId = myDataBase.delete(TABLE_MESSAGES, wheres, whereArgs);
        return rawId;

    }

    public long setDeleteMessages_by_msgid(String id) {
        Constant.printMsg("group dest :::::::::" + id);
        String wheres = "_id=? and media_wa_type!='7' ";

        String IDS = String.valueOf(id);
        String[] whereArgs = {IDS.toString()};
        long rawId = myDataBase.delete(TABLE_MESSAGES, wheres, whereArgs);
        return rawId;

    }

    public long setDeleteMessages_by_msgid_inBroad(String id) {
        Constant.printMsg("group dest :::::::::" + id);
        String wheres = "key_id=? and media_wa_type!='7' ";

        String IDS = String.valueOf(id);
        String[] whereArgs = {IDS.toString()};
        long rawId = myDataBase.delete(TABLE_MESSAGES, wheres, whereArgs);
        return rawId;

    }

    public long setDelete_Sec_chat(String jid) {
        Constant.printMsg("group dest :::::::::1" + jid);

        String wheres = "key_remote_jid=? and is_sec_chat==0 ";

        String[] whereArgs = {jid};
        long rawId = myDataBase.delete(TABLE_MESSAGES, wheres, whereArgs);
        return rawId;

    }

	/*
     * public long setDelete_Sec_chat_messages(String jid) {
	 * 
	 * String wheres = "key_remote_jid=? and is_sec_chat==0 ";
	 * 
	 * 
	 * String[] whereArgs = {jid}; long rawId =
	 * myDataBase.delete(TABLE_MESSAGES, wheres, whereArgs); return rawId;
	 * 
	 * }
	 */

    public Cursor getGroupChat_list() {

        Cursor cursor = myDataBase
                .rawQuery(
                        "Select ch._id,c.jid,c.display_name from contacts c,chat_list ch where c.jid=ch.key_remote_jid and c.is_niftychat_user=1 and raw_contact_id='0'",
                        null);

        return cursor;
    }

    public Boolean isMessageExist(String key_id) {
        Boolean bool = false;

        Cursor cursor = myDataBase.rawQuery(
                "Select * from messages where key_id='" + key_id + "'", null);

        if (cursor.getCount() > 0) {
            bool = true;
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return bool;

    }

    public String getDisplay_name(String jid) {
        String name = null;

        Cursor cursor = myDataBase.rawQuery(
                "Select * from contacts where jid='" + jid + "'", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            name = cursor.getString(6);
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return name;

    }

    public long setUpdateSubject(String jid, String subject) {

        ContentValues values = new ContentValues();

        values.put("display_name", subject);

        String wheres = "jid=?";
        String IDS = String.valueOf(jid);
        String[] whereArgs = {IDS.toString()};
        long rawId = myDataBase.update(TABLE_CONTACTS, values, wheres,
                whereArgs);
        return rawId;

    }

    public long setUpdateContact_onob(String jid, String name, String number) {

        ContentValues values = new ContentValues();

        values.put("display_name", name);
        values.put("number", number);
        values.put("is_in_contact_list", 0);
        String wheres = "jid=?";
        String IDS = String.valueOf(jid);
        String[] whereArgs = {IDS.toString()};
        long rawId = myDataBase.update(TABLE_CONTACTS, values, wheres,
                whereArgs);
        return rawId;

    }

    public long setUpdate_Contact_List(ContactsGetSet contact) {

        ContentValues values = new ContentValues();

        values.put("status", contact.getStatus());
        values.put("nifty_email", contact.getNifty_email());
        values.put("nifty_name", contact.getNifty_name());
        values.put("photo_ts", contact.getPhoto_ts());
        values.put("is_niftychat_user", contact.getIs_niftychat_user());

        String wheres = "jid=?";
        String IDS = String.valueOf(contact.getJid());
        String[] whereArgs = {IDS.toString()};
        long rawId = myDataBase.update(TABLE_CONTACTS, values, wheres,
                whereArgs);
        return rawId;

    }

    public long setUpdateVcard(ContactsGetSet contact) {

        ContentValues values = new ContentValues();

        values.put("status", contact.getStatus());
        values.put("nifty_email", contact.getNifty_email());
        values.put("nifty_name", contact.getNifty_name());
        values.put("photo_ts", contact.getPhoto_ts());

        String wheres = "jid=?";
        String IDS = String.valueOf(contact.getJid());
        String[] whereArgs = {IDS.toString()};
        long rawId = myDataBase.update(TABLE_CONTACTS, values, wheres,
                whereArgs);
        return rawId;

    }

    public long setUpdateLogin(LoginGetSet login) {
        ContentValues values = new ContentValues();
        if (login.getNifty_name() != null && !login.getNifty_name().isEmpty()) {
            values.put("nifty_name", login.getNifty_name());
        }
        if (login.getNifty_email() != null && !login.getNifty_email().isEmpty()) {
            values.put("nifty_email", login.getNifty_email());
        }
        if (login.getStatus() != null && !login.getStatus().isEmpty()) {
            values.put("status", login.getStatus());
        }
        if (login.getAvatar() != null && login.getAvatar().length > 0) {
            values.put("photo", login.getAvatar());
        }
        String wheres = "user=?";
        String IDS = String.valueOf(login.getUserName());
        String[] whereArgs = {IDS.toString()};
        long rawId = myDataBase.update(TABLE_LOGIN, values, wheres, whereArgs);
        return rawId;
    }

    public long setUpdateLogin_status(String uid, String status) {

        ContentValues values = new ContentValues();

        values.put("status", status);

        String wheres = "user=?";
        String IDS = String.valueOf(uid);
        String[] whereArgs = {IDS.toString()};
        long rawId = myDataBase.update(TABLE_LOGIN, values, wheres, whereArgs);
        return rawId;
    }

    public long setUpdateMessage_status(String jid, String key_id, int status) {

        ContentValues values = new ContentValues();

        values.put("status", status);
        values.put("needs_push", 0);

        String wheres = "key_remote_jid=? and key_id=?";
        String IDS = String.valueOf(jid);
        String[] whereArgs = {IDS.toString(), key_id};
        long rawId = myDataBase.update(TABLE_MESSAGES, values, wheres,
                whereArgs);
        Log.d(TAG, "RowId " + rawId);
        return rawId;

    }

    public long setUpdateMessage_statusDelivered(String jid, String key_id) {

        ContentValues values = new ContentValues();

        values.put("status", 0);
        values.put("needs_push", 0);

        String wheres = "key_remote_jid=? and key_id=? and status!='-1'";
        String IDS = String.valueOf(jid);
        String[] whereArgs = {IDS.toString(), key_id};
        long rawId = myDataBase.update(TABLE_MESSAGES, values, wheres,
                whereArgs);
        Log.d("setUpdateMessage_statusDelivered", "RowId " + rawId);
        return rawId;

    }

    public long setUpdateMessage_statusDisplay(String jid, String key_id) {

        ContentValues values = new ContentValues();

        values.put("status", -1);
        values.put("needs_push", 0);

        String wheres = "key_remote_jid=? and key_id=?";
        String IDS = String.valueOf(jid);
        String[] whereArgs = {IDS.toString(), key_id};
        long rawId = myDataBase.update(TABLE_MESSAGES, values, wheres,
                whereArgs);
        Log.d("setUpdateMessage_statusDisplay", "RowId " + rawId);
        return rawId;

    }

    public long setUpdateMessage_statusOffline(String jid, String key_id) {

        ContentValues values = new ContentValues();

        values.put("status", 1);
        values.put("needs_push", 0);

        String wheres = "key_remote_jid=? and key_id=? and status!='-1' and status!='0'";
        String IDS = String.valueOf(jid);
        String[] whereArgs = {IDS.toString(), key_id};
        long rawId = myDataBase.update(TABLE_MESSAGES, values, wheres,
                whereArgs);
        Log.d("setUpdateMessage_statusOffline", "RowId " + rawId);
        return rawId;

    }

    public long setUpdateMessage_status(String key_id, int status) {

        ContentValues values = new ContentValues();

        values.put("status", status);
        values.put("needs_push", 1);

        String wheres = "key_id=?";

        String[] whereArgs = {key_id};
        long rawId = myDataBase.update(TABLE_MESSAGES, values, wheres,
                whereArgs);
        return rawId;

    }


    public long setUpdateMessage_Media_url(String key_id, String url) {

        ContentValues values = new ContentValues();

        values.put("media_url", url);

        String wheres = "_id=?";

        String[] whereArgs = {key_id};
        long rawId = myDataBase.update(TABLE_MESSAGES, values, wheres,
                whereArgs);
        return rawId;

    }

    public long setUpdateMessage_Origin(String _id, int origin) {

        ContentValues values = new ContentValues();

        values.put("origin", origin);

        String wheres = "_id=?";

        String[] whereArgs = {_id};
        long rawId = myDataBase.update(TABLE_MESSAGES, values, wheres,
                whereArgs);
        return rawId;

    }

    public long setUpdateMessage(String k, String origin) {
        long l = 0;
        ContentValues values = new ContentValues();

        values.put("data", origin);

        String wheres = "key_id=?";

        String[] whereArgs = {k};
        long rawId = myDataBase.update(TABLE_MESSAGES, values, wheres,
                whereArgs);
        return rawId;

    }

    public long setUpdateMessage_MediaName(String key_id, String media_name) {

        ContentValues values = new ContentValues();
        values.put("media_name", media_name);
        String wheres = "key_id=?";

        String[] whereArgs = {key_id};
        long rawId = myDataBase.update(TABLE_MESSAGES, values, wheres,
                whereArgs);
        return rawId;

    }

    public long addGroupMembers(GroupParticipantGetSet member) {
        ContentValues values = new ContentValues();
        values.put("gjid", member.getGjid());
        values.put("jid", member.getJid());
        values.put("admin", member.getAdmin());
        long rawId = myDataBase.insert(TABLE_GROUP_PARTICIPANTS, null, values);
        return rawId;
    }

    public long deleteGroupMembers(String gjid, String jid) {
        String wheres = "gjid==? and jid==?";
        String[] whereArgs = {gjid, jid};
        long rawId = myDataBase.delete(TABLE_GROUP_PARTICIPANTS, wheres,
                whereArgs);
        return rawId;
    }

    public long updateGroupMembers(String gjid, String jid, int admin) {

        ContentValues values = new ContentValues();
        values.put("admin", admin);
        String wheres = "gjid==? and jid==?";
        String[] whereArgs = {gjid, jid};
        long rawId = myDataBase.update(TABLE_GROUP_PARTICIPANTS, values,
                wheres, whereArgs);
        return rawId;

    }

    public long deleteGroup(String gjid) {
        String wheres = "gjid==?";
        String[] whereArgs = {gjid};
        long rawId = myDataBase.delete(TABLE_GROUP_PARTICIPANTS, wheres,
                whereArgs);
        return rawId;
    }

    public ArrayList<GroupParticipantGetSet> getGroupMembers(String gjid) {
        ArrayList<GroupParticipantGetSet> list = new ArrayList<GroupParticipantGetSet>();
        Cursor cursor = myDataBase.rawQuery(
                "Select * from group_participants where gjid=='" + gjid + "'",
                null);

        if (cursor.moveToFirst()) {
            do {
                GroupParticipantGetSet usd = new GroupParticipantGetSet();
                usd.set_id(cursor.getInt(0));
                usd.setGjid(cursor.getString(1));
                usd.setJid(cursor.getString(2));
                usd.setAdmin(cursor.getInt(3));
                usd.setRegister_name(cursor.getString(5));
                list.add(usd);
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return list;
    }

    public long getGroupMembersAdmin_count(String gjid) {
        long val = 1;
        Cursor cursor = myDataBase.rawQuery(
                "Select * from group_participants where gjid=='" + gjid + "' and admin == 1",
                null);

        if (cursor != null) {
            if (cursor.getCount() > 1) {
                val = cursor.getCount();
            }
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return val;
    }


    public ArrayList<GroupParticipantGetSet> getGroupMembersOnly(String gjid) {
        ArrayList<GroupParticipantGetSet> list = new ArrayList<GroupParticipantGetSet>();
        Cursor cursor = myDataBase.rawQuery(
                "Select * from group_participants where gjid=='" + gjid
                        + "' and admin==0", null);

        if (cursor.moveToFirst()) {
            do {
                GroupParticipantGetSet usd = new GroupParticipantGetSet();
                usd.set_id(cursor.getInt(0));
                usd.setGjid(cursor.getString(1));
                usd.setJid(cursor.getString(2));
                usd.setAdmin(cursor.getInt(3));
                list.add(usd);
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return list;
    }

    /**
     * For sending typing msg to other grp members in MUC_Chat
     *
     * @param gjid
     * @return
     */
    public ArrayList<String> getGroupMembersForTyping(String gjid, String jid) {
        ArrayList<String> list = new ArrayList<String>();
        Cursor cursor = myDataBase.rawQuery(
                "Select jid from group_participants where gjid=='" + gjid
                        + "' and jid NOT LIKE '" + jid + "'", null);

        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    String usd = cursor.getString(cursor.getColumnIndex("jid"));

                    list.add(usd);
                } while (cursor.moveToNext());
            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return list;
    }

    public boolean getGroupAdmin_Info(String gjid, String jid) {
        Cursor cursor = myDataBase.rawQuery(
                "Select * from group_participants where gjid=='" + gjid
                        + "' and jid=='" + jid + "' and admin==1", null);

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                return true;
            }
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return false;

    }

    public GroupParticipantGetSet getGroupAdmin(String gjid) {
        GroupParticipantGetSet usd = new GroupParticipantGetSet();
        Cursor cursor = myDataBase.rawQuery(
                "Select * from group_participants where gjid=='" + gjid
                        + "' and admin==1", null);

        if (cursor.moveToFirst()) {
            do {

                usd.set_id(cursor.getInt(0));
                usd.setGjid(cursor.getString(1));
                usd.setJid(cursor.getString(2));
                usd.setAdmin(cursor.getInt(3));

            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return usd;
    }

    public Boolean isGroupMember(String gjid, String jid) {
        Boolean is_member = true;
        Cursor cursor = myDataBase.rawQuery(
                "Select * from group_participants where gjid=='" + gjid
                        + "' and jid=='" + jid + "'", null);

        if (cursor != null) {
            if (cursor.getCount() <= 0)

                is_member = false;

            Constant.printMsg("DDDDDDDDDD" + is_member);
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return is_member;
    }

    public ArrayList<ContactsGetSet> getGroupMembersContact(String gjid) {
        ArrayList<ContactsGetSet> list = new ArrayList<ContactsGetSet>();
        Cursor cursor = myDataBase
                .rawQuery(
                        "Select * from contacts where jid in (Select * from group_participants where gjid=='"
                                + gjid + "')", null);

        ContactsGetSet usd = null;
        if (cursor.moveToFirst()) {
            do {
                usd = new ContactsGetSet();

                usd.setId(cursor.getInt(0));
                usd.setDisplay_name(cursor.getString(6));
                usd.setJid(cursor.getString(1));
                usd.setNumber(cursor.getString(4));
                usd.setPhone_label(cursor.getString(8));
                usd.setPhone_type(cursor.getString(7));
                usd.setRaw_contact_id(cursor.getString(5));
                usd.setStatus(cursor.getString(3));
                usd.setUnseen_msg_count(cursor.getInt(9));
                usd.setPhoto_ts(cursor.getBlob(10));
                usd.setNifty_name(cursor.getString(11));
                usd.setNifty_email(cursor.getString(12));
                usd.setIs_niftychat_user(cursor.getInt(2));
                list.add(usd);
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return list;
    }

    public long setDeleteLogin(String jid) {

        // String wheres = " key_remote_jid=?";
        // String IDS = String.valueOf(jid);
        // String[] whereArgs = { IDS.toString() };
        long rawId = myDataBase.delete(TABLE_LOGIN, null, null);
        return rawId;

    }

    /**
     * Added by sivanesan for contac sync
     */
    public long insertContacts(ContactsGetSet contect) {
        long l = 0;
        ContentValues values = new ContentValues();
        values.put("jid", contect.getJid());
        values.put("is_niftychat_user", contect.getIs_niftychat_user());
        values.put("status", contect.getStatus());
        values.put("number", contect.getNumber());
        values.put("display_name", contect.getDisplay_name());
        values.put("phone_type", contect.getPhone_type());
        values.put("phone_label", contect.getPhone_label());
        values.put("unseen_msg_count", contect.getUnseen_msg_count());
        values.put("photo_ts", contect.getPhoto_ts());
        values.put("raw_contact_id", contect.getRaw_contact_id());
        values.put("nifty_name", contect.getNifty_name());
        values.put("nifty_email", contect.getNifty_email());
        values.put("is_in_contact_list", contect.getIsInContactList());
        l = myDataBase.insert("contacts", null, values);
        Constant.printMsg("no fo inserted row::" + "  " + l + "  "
                + contect.getNumber());
        return l;
    }

    public long updateInsertedPhotoByte(String jid, byte[] photo) {
        ContentValues values = new ContentValues();
        if (photo != null) {
            values.put("photo_ts", photo);
        }
        String wheres = "jid=?";
        String IDS = String.valueOf(jid);
        String[] whereArgs = {IDS.toString()};
        long rawId = myDataBase.update(TABLE_CONTACTS, values, wheres,
                whereArgs);
        Constant.printMsg("siva Updaete photomethod in lastsync...." + rawId);
        return rawId;
    }

    public long updateInsertedContacts(String jid, String primaryNumber, String photoUrl, String status, String email) {
        // String
        // qury="UPDATE "+TABLE_CONTACTS+" SET is_niftychat_user='1' WHERE number='"
        // + primaryNumber+ "'";
        // sql.execSQL(qry);
        ContentValues values = new ContentValues();
        values.put("is_niftychat_user", 1);
        values.put("number", primaryNumber);
//        if (photo != null) {
//            values.put("photo_ts", photo);
//        }
        if (photoUrl != null) {
            values.put("phone_label", photoUrl);
            try {
                MemoryCacheUtils.removeFromCache(photoUrl, ImageLoader.getInstance().getMemoryCache());
                DiskCacheUtils.removeFromCache(photoUrl, ImageLoader.getInstance().getDiscCache());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (status != null && !status.isEmpty()) {
            values.put("status", status);
        }
        if (email != null && !email.isEmpty()) {
            values.put("nifty_email", email);
        }
        // values.put("jid",primaryNumber+KachingMeApplication
        // .getHost());
        String wheres = "jid=?";
        String IDS = String.valueOf(jid);
        String[] whereArgs = {IDS.toString()};
        long rawId = myDataBase.update(TABLE_CONTACTS, values, wheres,
                whereArgs);
        Constant.printMsg("siva Updaete method in lastsync...." + rawId);

        // Registing presence for kaching me users for lastseen status....
        if (TempConnectionService.connection != null) {
            try {
                if (TempConnectionService.connection.isAuthenticated()) {
                    Presence presencePacket = new Presence(Presence.Type.subscribe);
                    presencePacket.setTo(jid);
                    try {
                        TempConnectionService.connection.sendStanza(presencePacket);
                    } catch (SmackException.NotConnectedException e1) {
                        e1.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return rawId;
    }

    public long setDeleteTableContact() {
        long rawId = myDataBase.delete(TABLE_CONTACTS, null, null);
        return rawId;
    }

    public void checkDbForRemoveContact(ArrayList<UserContactDto> contactlist) {
        // TODO Auto-generated method stub
        Constant.printMsg("siva onchange check ids db insiode......");
//		ArrayList<contactsGetSet> list = new ArrayList<contactsGetSet>();
        Cursor cursor = myDataBase.rawQuery("Select * from " + TABLE_CONTACTS,
                null);
        Constant.printMsg("siva onchange check ids db insiode after query......"
                + cursor.getCount());
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Constant.printMsg("siva onchange check ids......"
                        + cursor.getString(1));
                boolean isPresent = false;
                for (int i = 0; i < contactlist.size(); i++) {
                    Constant.printMsg("siva onchange check ids of get con......"
                            + contactlist.get(i).getuId() + "........"
                            + cursor.getString(1));
                    if (cursor.getString(1).equalsIgnoreCase(
                            contactlist.get(i).getuId())) {
                        isPresent = true;
                        Constant.printMsg("siva onchange check ids true......"
                                + contactlist.get(i).getuId());
                        break;
                    }
                }
                if (!isPresent) {
                    Constant.printMsg("siva onchange delete ids true....."
                            + cursor.getString(1));
                    if (getJidIdUserOrNot(cursor.getString(1)) != 0) {
                        updateIsInContactList(cursor.getString(1), 0, cursor.getString(1).split("@")[0]);
                    } else {
                        setDeleteContact(cursor.getString(1));
                    }
                }

            }
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }

    /*
    Checking whether new contact is saved in phone book storage..
     */
    public int getIsInContactList(String jid) {

        int val = 0;
        Cursor cursor = myDataBase
                .rawQuery(
                        "Select is_in_contact_list from contacts where jid =='"
                                + jid + "'", null);

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst())
                    val = cursor.getInt(cursor.getColumnIndex("is_in_contact_list"));

            }

        }

        return val;
    }

    /*
     Updating isContactlist coloum once contact saved in phone..
     */
//    public long updateIsInContactList(String jid, int val) {
//
//        ContentValues values = new ContentValues();
//
//        values.put("is_in_contact_list", val);
//
//        String wheres = "jid=?";
//        String IDS = String.valueOf(jid);
//        String[] whereArgs = {IDS.toString()};
//        long rawId = myDataBase.update(TABLE_CONTACTS, values, wheres,
//                whereArgs);
//        return rawId;
//
//    }
    public long updateIsInContactList(String jid, int val, String name) {
        ContentValues values = new ContentValues();
        values.put("is_in_contact_list", val);
        if (name != null && !name.isEmpty()) {
            values.put("display_name", name);
        }
        String wheres = "jid=?";
        String IDS = String.valueOf(jid);
        String[] whereArgs = {IDS.toString()};
        long rawId = myDataBase.update(TABLE_CONTACTS, values, wheres,
                whereArgs);
        return rawId;

    }

    public String getcontactName(String jidId) {
        //Cursor cursor = myDataBase.rawQuery("Select display_name from "
        //		+ TABLE_CONTACTS + " where jid='" + jidId + "'", null);
        Cursor cursor = myDataBase.rawQuery("Select * from "
                + TABLE_CONTACTS + " where jid='" + jidId + "'", null);
        String name = null;
        Constant.printMsg("siva last duty:name.. test..."
                + cursor.getCount());
        int index_contactName = cursor.getColumnIndex("display_name");
        if (cursor.getCount() > 0) {

            try {

                while (cursor.moveToNext()) {
                    name = cursor.getString(6);
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }

        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return name;
    }

    public long contactNameUpdate(String jid, String name) {
        ContentValues values = new ContentValues();
        values.put("display_name", name);

        String wheres = "jid=?";
        String IDS = String.valueOf(jid);
        String[] whereArgs = {IDS.toString()};
        long rawId = myDataBase.update(TABLE_CONTACTS, values, wheres,
                whereArgs);
        return rawId;
    }

    // public int getContactsIsPresentOrNot(String number) {
    // int present;
    // Cursor cursor = myDataBase
    // .rawQuery(
    // "Select * from "+TABLE_CONTACTS+" where number='"+number+"'",
    // null);
    // present=cursor.getCount();
    // if (cursor != null && !cursor.isClosed()) {
    // cursor.close();
    // }
    //
    // return present;
    // }

    public int getJidIdPresentOrNot(String jidId) {
        int present = 0;
        Cursor cursor = myDataBase.rawQuery("Select * from " + TABLE_CONTACTS
                + " where jid='" + jidId + "'", null);
        present = cursor.getCount();
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return present;
    }

    public int getJidIdUserOrNot(String jidId) {
        int present = 0;
        Cursor cursor = myDataBase.rawQuery("Select * from " + TABLE_CONTACTS
                + " where jid='" + jidId + "' and is_niftychat_user=1 and is_in_contact_list=1 and raw_contact_id!='0'", null);
        present = cursor.getCount();
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return present;
    }

    public ArrayList<ContactsGetSet> getKachingUserContacts() {
        ArrayList<ContactsGetSet> list = new ArrayList<ContactsGetSet>();
        Cursor cursor = myDataBase
                .rawQuery(
                        "Select * from "
                                + TABLE_CONTACTS
                                + " where is_niftychat_user=1 and is_in_contact_list=1 and raw_contact_id!='0' order by display_name COLLATE NOCASE ASC;",
                        null);
        if (cursor.getCount() > 0) {
            try {

                while (cursor.moveToNext()) {
                    ContactsGetSet usd = new ContactsGetSet();
                    usd.setId(cursor.getInt(0));
                    usd.setJid(cursor.getString(1));
                    usd.setIs_niftychat_user(cursor.getInt(2));
                    usd.setStatus(cursor.getString(3));
                    usd.setNumber(cursor.getString(4));
                    usd.setRaw_contact_id(cursor.getString(5));
                    usd.setDisplay_name(cursor.getString(6));
                    usd.setPhone_label(cursor.getString(8));
                    usd.setPhone_type(cursor.getString(7));
                    usd.setUnseen_msg_count(cursor.getInt(9));
                    usd.setPhoto_ts(cursor.getBlob(10));
                    usd.setNifty_name(cursor.getString(11));
                    usd.setNifty_email(cursor.getString(12));
                    if (cursor.getBlob(10) != null) {

                        Bitmap bmp = null;

                        System.gc();
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = false;
                        options.inPreferredConfig = Bitmap.Config.RGB_565;
                        options.inDither = true;
                        options.inSampleSize = 2;
                        try {
                            bmp = BitmapFactory.decodeByteArray(cursor.getBlob(10), 0,
                                    cursor.getBlob(10).length, options);

                            usd.setPhoto_bitmap(bmp);

                        } catch (OutOfMemoryError e) {
                            Log.e("Map", "Tile Loader (241) Out Of Memory Error " + e.getLocalizedMessage());
                            System.gc();
                        }


                    }
                    list.add(usd);
                }

            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return list;
    }

    public ArrayList<ContactsGetSet> getContacts() {
        ArrayList<ContactsGetSet> list = new ArrayList<ContactsGetSet>();
        Cursor cursor = myDataBase
                .rawQuery(
                        "Select * from "
                                + TABLE_CONTACTS
                                + " where is_niftychat_user=1 and is_in_contact_list=1 and raw_contact_id!='0' order by display_name COLLATE NOCASE ASC;",
                        null);
        if (cursor.getCount() > 0) {
            try {

                while (cursor.moveToNext()) {
                    ContactsGetSet usd = new ContactsGetSet();
                    usd.setId(cursor.getInt(0));
                    usd.setJid(cursor.getString(1));
                    usd.setIs_niftychat_user(cursor.getInt(2));
                    usd.setStatus(cursor.getString(3));
                    usd.setNumber(cursor.getString(4));
                    usd.setRaw_contact_id(cursor.getString(5));
                    usd.setDisplay_name(cursor.getString(6));
                    usd.setPhone_label(cursor.getString(8));
                    usd.setPhone_type(cursor.getString(7));
                    usd.setUnseen_msg_count(cursor.getInt(9));
                    usd.setPhoto_ts(cursor.getBlob(10));
                    usd.setNifty_name(cursor.getString(11));
                    usd.setNifty_email(cursor.getString(12));
                    if (cursor.getBlob(10) != null) {
                        Bitmap bmp = null;

                        System.gc();
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = false;
                        options.inPreferredConfig = Bitmap.Config.RGB_565;
                        options.inDither = true;
                        options.inSampleSize = 2;
                        try {
                            bmp = BitmapFactory.decodeByteArray(cursor.getBlob(10), 0,
                                    cursor.getBlob(10).length, options);

                        } catch (OutOfMemoryError e) {
                            android.util.Log.e("Map", "Tile Loader (241) Out Of Memory Error " + e.getLocalizedMessage());
                            System.gc();
                        }
                        usd.setPhoto_bitmap(bmp);

                    }
                    list.add(usd);
                }

            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return list;
    }

    public long setUpdateRegisterName(String jid, String status, String gjid) {

        long rawId = 0;
        try {
            ContentValues values = new ContentValues();

            values.put("register_name", status);

            String wheres = " jid like ? and gjid like ?";
            String IDS = String.valueOf(jid);
            String[] whereArgs = {jid.toString(), gjid.toString()};
            rawId = myDataBase.update(TABLE_GROUP_PARTICIPANTS, values, wheres,
                    whereArgs);

        } catch (Exception e) {
            Constant.printMsg("MUC Info Profile names EEEE::::::: "
                    + e.toString());
        }
        return rawId;
    }

    public ArrayList<ContactsGetSet> getContactsnotuser() {
        ArrayList<ContactsGetSet> list = new ArrayList<ContactsGetSet>();
        Cursor cursor = myDataBase
                .rawQuery(
                        "Select * from "
                                + TABLE_CONTACTS
                                + " where is_niftychat_user=0 and is_in_contact_list=1 and raw_contact_id!='0' order by display_name COLLATE NOCASE ASC;",
                        null);
        if (cursor.getCount() > 0) {
            //int index_id=cursor.getColumnIndex("jid");
//			int index_jid=cursor.getColumnIndex("jid");
//			int index_isNiftyUser=cursor.getColumnIndex("is_niftychat_user");
//			int index_status=cursor.getColumnIndex("status");
//			int index_number=cursor.getColumnIndex("number");
//			int index_rawContactId=cursor.getColumnIndex("raw_contact_id");
            int index_diaplayName = cursor.getColumnIndex("display_name");
//			int index_phoneType=cursor.getColumnIndex("phone_type");
//			int index_phoneLable=cursor.getColumnIndex("phone_label");
//			int index_unSeenMsg=cursor.getColumnIndex("unseen_msg_count");
//			int index_photoTs=cursor.getColumnIndex("photo_ts");
//			int index_niftyName=cursor.getColumnIndex("nifty_name");
//			int index_niftyMail=cursor.getColumnIndex("nifty_email");
//			
            Constant.printMsg("siva get user+display_name index::::::::"
                    + index_diaplayName);

            while (cursor.moveToNext()) {
//				contactsGetSet usd = new contactsGetSet();
//				//usd.setId(cursor.getInt(0));
//				usd.setJid(cursor.getString(index_jid));
//				usd.setIs_niftychat_user(cursor.getInt(index_isNiftyUser));
//				usd.setStatus(cursor.getString(index_status));
//				usd.setNumber(cursor.getString(index_number));
//				usd.setRaw_contact_id(cursor.getString(index_rawContactId));
//				usd.setDisplay_name(cursor.getString(index_diaplayName));
//				usd.setPhone_type(cursor.getString(index_phoneType));
//				usd.setPhone_label(cursor.getString(index_phoneLable));
//				usd.setUnseen_msg_count(cursor.getInt(index_unSeenMsg));
//				usd.setPhoto_ts(cursor.getBlob(index_photoTs));
//				usd.setNifty_name(cursor.getString(index_niftyName));
//				usd.setNifty_email(cursor.getString(index_niftyMail));
//				CommonMethods
//						.prtMsg("siva phone no::not" + cursor.getString(index_diaplayName));
                ContactsGetSet usd = new ContactsGetSet();
                usd.setId(cursor.getInt(0));
                usd.setJid(cursor.getString(1));
                usd.setIs_niftychat_user(cursor.getInt(2));
                usd.setStatus(cursor.getString(3));
                usd.setNumber(cursor.getString(4));
                usd.setRaw_contact_id(cursor.getString(5));
                usd.setDisplay_name(cursor.getString(6));
                usd.setPhone_label(cursor.getString(8));
                usd.setPhone_type(cursor.getString(7));
                usd.setUnseen_msg_count(cursor.getInt(9));
                usd.setPhoto_ts(cursor.getBlob(10));
                usd.setNifty_name(cursor.getString(11));
                usd.setNifty_email(cursor.getString(12));
                if (cursor.getBlob(10) != null) {
                    Bitmap bmp = BitmapFactory.decodeByteArray(cursor.getBlob(10), 0,
                            cursor.getBlob(10).length);
                    usd.setPhoto_bitmap(bmp);

                }
                list.add(usd);
            }
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return list;
    }


    public int getMessageStatus(String jid, String key_id) {
        int status = 5;
        Cursor mCursor = null;
        mCursor = myDataBase
                .rawQuery(
                        "Select status from messages where key_remote_jid like ? and key_id like ?",
                        new String[]{jid, key_id});

        if (mCursor != null) {
            if (mCursor.moveToFirst())

                status = mCursor.getInt(mCursor.getColumnIndex("status"));
        }

        return status;

    }


    public int getMessageStatusById(String id) {
        int status = 5;
        Cursor mCursor = null;
        mCursor = myDataBase
                .rawQuery(
                        "Select status from messages where _id = ?",
                        new String[]{ id});

        if (mCursor != null) {
            if (mCursor.moveToFirst())

                status = mCursor.getInt(mCursor.getColumnIndex("status"));
        }

        return status;

    }

    public ArrayList<ContactsGetSet> getContactsFilterNumber(String value) {
        ArrayList<ContactsGetSet> list = new ArrayList<ContactsGetSet>();
        Cursor cursor = myDataBase
                .rawQuery("Select * from contacts where number LIKE '%"
                                + value + "%' AND is_niftychat_user=1 AND raw_contact_id!='0' OR display_name LIKE '"
                                + value + "%' AND is_niftychat_user=1 AND raw_contact_id!='0' order by display_name ",
                        null);
        Constant.printMsg("db check contacts getContactsFilterNumber....." + cursor.getCount());
        if (cursor.moveToFirst()) {
            do {
                ContactsGetSet usd = new ContactsGetSet();
                usd.setId(cursor.getInt(0));
                usd.setDisplay_name(cursor.getString(6));
                usd.setJid(cursor.getString(1));
                usd.setIs_niftychat_user(cursor.getInt(2));
                usd.setNumber(cursor.getString(4));
                usd.setPhone_label(cursor.getString(8));
                usd.setPhone_type(cursor.getString(7));
                usd.setRaw_contact_id(cursor.getString(5));
                usd.setStatus(cursor.getString(3));
                usd.setUnseen_msg_count(cursor.getInt(9));
                usd.setPhoto_ts(cursor.getBlob(10));
                usd.setNifty_name(cursor.getString(11));
                usd.setNifty_email(cursor.getString(12));
                if (cursor.getBlob(10) != null) {
                    Bitmap bmp = BitmapFactory.decodeByteArray(cursor.getBlob(10), 0,
                            cursor.getBlob(10).length);
                    usd.setPhoto_bitmap(bmp);

                }
                list.add(usd);
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return list;
    }

    public ArrayList<ContactsGetSet> getContactsFilternotNumber(String value) {
        ArrayList<ContactsGetSet> list = new ArrayList<ContactsGetSet>();
        Cursor cursor = myDataBase
                .rawQuery("Select * from contacts where number LIKE '%"
                        + value + "%' AND is_niftychat_user=0 AND raw_contact_id!='0' OR display_name LIKE '"
                        + value + "%' AND is_niftychat_user=0 AND raw_contact_id!='0' order by display_name ", null);
        Constant.printMsg("db check contacts getContactsFilternotNumber....." + cursor.getCount());
        if (cursor.moveToFirst()) {
            do {
                ContactsGetSet usd = new ContactsGetSet();
                usd.setId(cursor.getInt(0));
                usd.setDisplay_name(cursor.getString(6));
                usd.setJid(cursor.getString(1));
                usd.setIs_niftychat_user(cursor.getInt(2));
                usd.setNumber(cursor.getString(4));
                usd.setPhone_label(cursor.getString(8));
                usd.setPhone_type(cursor.getString(7));
                usd.setRaw_contact_id(cursor.getString(5));
                usd.setStatus(cursor.getString(3));
                usd.setUnseen_msg_count(cursor.getInt(9));
                usd.setPhoto_ts(cursor.getBlob(10));
                usd.setNifty_name(cursor.getString(11));
                usd.setNifty_email(cursor.getString(12));

                list.add(usd);
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return list;
    }

    public void dumpDatabase() {
        myDataBase.execSQL("delete from " + TABLE_CONTACTS);
        myDataBase.execSQL("delete from " + TABLE_LOGIN);
        myDataBase.execSQL("delete from " + TABLE_MESSAGES);
        myDataBase.execSQL("delete from " + TABLE_CHAT_LIST);
        myDataBase.execSQL("delete from " + TABLE_ADV_GROUP);
        myDataBase.execSQL("delete from " + TABLE_GROUP_PARTICIPANTS);
    }

    public void deletelogin() {
        myDataBase.execSQL("delete from " + TABLE_LOGIN);
    }

}
