package com.wifin.kachingme.database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.wifin.kachingme.pojo.Chat_list_GetSet;
import com.wifin.kachingme.pojo.Chat_list_home_GetSet;
import com.wifin.kachingme.pojo.LoginGetSet;
import com.wifin.kachingme.pojo.MessageGetSet;
import com.wifin.kachingme.pojo.ContactsGetSet;

public class DatabaseHelper_Backup extends SQLiteOpenHelper {

	private static String DB_PATH = "";
	// private static final String DB_NAME = "niftychat_backup.db";
	private static final String DB_NAME = "kachingme_backup.db";
	private static final String TABLE_CONTACTS = "contacts";
	private static final String TABLE_CONVERSATION = "conversation";
	private static final String TABLE_LOGIN = "login";
	private static final String TABLE_MESSAGES = "messages";
	private static final String TABLE_CHAT_LIST = "chat_list";
	private static final String TABLE_ROOMCONVERSATION = "roomconversation";
	private static final String TABLE_BUDDY_REQUESTS = "buddy_requests";
	private static final String TABLE_GROUPS = "groups";
	static int version_val = 1;

	private static final String TABLE_PRODUCTS = "tbl_products";
	private static DatabaseHelper_Backup mDBConnection;
	private SQLiteDatabase myDataBase;
	private final Context myContext;

	public DatabaseHelper_Backup(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);

		this.myContext = context;
		DB_PATH = "/data/data/"
				+ context.getApplicationContext().getPackageName()
				+ "/databases/";
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		copyDataBase(db);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		// String
		// query="INSERT INTO tbl_products (COLUMN_ID, COLUMN_PRODUCTNAME,  COLUMN_QUANTITY) VALUES(2,\"shorts\",50)";
		// db.execSQL(query);
		db.execSQL("delete from tbl_products");
	}

	public synchronized void openDataBase() throws SQLException {

		myDataBase = mDBConnection.getWritableDatabase();

	}

	@Override
	public synchronized void close() {
		if (myDataBase != null)
			myDataBase.close();
		super.close();
	}

	@Override
	public synchronized SQLiteDatabase getWritableDatabase() {

		super.getWritableDatabase();

		String myPath = DB_PATH + DB_NAME;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.NO_LOCALIZED_COLLATORS
						| SQLiteDatabase.OPEN_READWRITE);
		myDataBase.setVersion(version_val);

		return myDataBase;

	}

	private void copyDataBase(SQLiteDatabase db) {
		try {
			// db.close();
			InputStream myInput = myContext.getAssets().open(DB_NAME);
			File file = new File(DB_PATH + DB_NAME);
			boolean deleted = file.delete();

			if (deleted) {
				// Constant.printMsg("file is deleted");
			} else {
				// Constant.printMsg("file is not deleted");
			}

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

	public static synchronized DatabaseHelper_Backup getDBAdapterInstance(
			Context context) {
		if (mDBConnection == null) {
			mDBConnection = new DatabaseHelper_Backup(context, DB_NAME, null,
					version_val);
		}
		return mDBConnection;
	}

	public ArrayList<LoginGetSet> getLogin() {
		ArrayList<LoginGetSet> list = new ArrayList<LoginGetSet>();
		Cursor cursor = myDataBase.query(TABLE_LOGIN,
				new String[] { "id", "user", "pass", "status", "nifty_name",
						"nifty_email", "photo" }, null, null, null, null, null);
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
		ContentValues values = new ContentValues();
		values.put("user", User);
		values.put("pass", pass);
		values.put("status", status);
		values.put("nifty_name", name);
		values.put("nifty_email", email);
		values.put("photo", image);

		myDataBase.insert("login", null, values);
	}

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

		l = myDataBase.insert("contacts", null, values);

		return l;
	}

	public ArrayList<ContactsGetSet> getContacts() {
		ArrayList<ContactsGetSet> list = new ArrayList<ContactsGetSet>();
		Cursor cursor = myDataBase
				.rawQuery(
						"Select * from contacts where is_niftychat_user=1 and raw_contact_id!='0' order by display_name ",
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
				// usd.setIs_niftychat_user(cursor.getString(0));
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
				usd.setNumber(cursor.getString(4));
				usd.setPhone_label(cursor.getString(8));
				usd.setPhone_type(cursor.getString(7));
				usd.setRaw_contact_id(cursor.getString(5));
				usd.setStatus(cursor.getString(3));
				usd.setUnseen_msg_count(cursor.getInt(9));
				usd.setPhoto_ts(cursor.getBlob(10));
				usd.setNifty_name(cursor.getString(11));
				usd.setNifty_email(cursor.getString(12));
				// usd.setIs_niftychat_user(cursor.getString(0));
				list.add(usd);
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}

		return list;
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
				// usd.setIs_niftychat_user(cursor.getString(0));
				list.add(usd);
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}

		return list;
	}

	public ContactsGetSet getContact(String jid) {
		// ArrayList<contactsGetSet> list=new ArrayList<contactsGetSet>();
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
				// list.add(usd);
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}

		return usd;
	}

	public long setUpdateStatus(String jid, String status) {
		long l = 0;
		ContentValues values = new ContentValues();

		values.put("status", status);

		String wheres = "jid=?";
		String IDS = String.valueOf(jid);
		String[] whereArgs = { IDS.toString() };
		long rawId = myDataBase.update(TABLE_CONTACTS, values, wheres,
				whereArgs);
		return rawId;

	}

	public long setUpdateUserStatus(String jid, String status) {
		long l = 0;
		ContentValues values = new ContentValues();

		values.put("status", status);

		String wheres = "user=?";
		String IDS = String.valueOf(jid);
		String[] whereArgs = { IDS.toString() };
		long rawId = myDataBase.update(TABLE_LOGIN, values, wheres, whereArgs);
		return rawId;

	}

	public long setInsertMessages(MessageGetSet message) {
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

		/* values.put("thumb_image",message.getThumb_image()); */
		values.put("raw_data", message.getRow_data());

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
				// usd.setIs_niftychat_user(cursor.getString(0));
				// list.add(usd);
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}

		return list;
	}

	public ArrayList<MessageGetSet> getAllMessages() {
		ArrayList<MessageGetSet> list = new ArrayList<MessageGetSet>();
		Cursor cursor = myDataBase.rawQuery("Select * from messages", null);

		if (cursor.moveToFirst()) {
			do {
				MessageGetSet msg = new MessageGetSet();
				msg.set_id(cursor.getInt(0));
				msg.setData(cursor.getString(6));
				msg.setKey_from_me(cursor.getInt(2));
				msg.setKey_id(cursor.getString(3));
				msg.setKey_remote_jid(cursor.getString(1));
				msg.setLatitude(cursor.getDouble(14));
				msg.setLongitude(cursor.getDouble(15));
				msg.setMedia_duration(cursor.getInt(24));
				msg.setMedia_hash(cursor.getString(13));
				msg.setMedia_mime_type(cursor.getString(9));
				msg.setMedia_name(cursor.getString(12));
				msg.setMedia_size(cursor.getInt(11));
				msg.setMedia_url(cursor.getString(8));
				msg.setMedia_wa_type(cursor.getString(10));
				msg.setNeeds_push(cursor.getInt(5));
				msg.setOrigin(cursor.getInt(25));
				msg.setReceipt_device_timestamp(cursor.getLong(21));
				msg.setReceipt_server_timestamp(cursor.getLong(20));
				msg.setReceived_timestamp(cursor.getLong(18));
				msg.setRemote_resource(cursor.getString(17));
				msg.setRow_data(cursor.getBlob(22));
				msg.setSend_timestamp(cursor.getLong(19));
				msg.setStatus(cursor.getInt(4));
				msg.setThumb_image(cursor.getBlob(16));
				msg.setTimestamp(cursor.getLong(7));
				msg.setIs_sec_chat(cursor.getInt(26));
				msg.setSelf_des_time(cursor.getLong(27));
				msg.setIs_owner(cursor.getInt(28));
				list.add(msg);
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}

		return list;
	}

	public MessageGetSet getMessages_by_msg_id(String id) {
		ArrayList<MessageGetSet> list = new ArrayList<MessageGetSet>();
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
		Cursor cursor = myDataBase.rawQuery(
				"select * from messages where status=3", null);

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

	public MessageGetSet getMessages_by_key_id(String id) {
		ArrayList<MessageGetSet> list = new ArrayList<MessageGetSet>();
		Cursor cursor = myDataBase.rawQuery(
				"Select * from messages WHERE key_id=" + id, null);
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
						+ "' and status!=0 and key_from_me=1", null);

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
				// usd.setIs_niftychat_user(cursor.getString(0));
				list.add(usd);
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}

		return list;
	}

	public long setUpdateMessage_display(String jid, String key_id) {
		long l = 0;
		ContentValues values = new ContentValues();

		values.put("status", 0);
		values.put("needs_push", 0);

		String wheres = "key_remote_jid=? and key_id=?";
		String IDS = String.valueOf(jid);
		String[] whereArgs = { IDS.toString(), key_id };
		long rawId = myDataBase.update(TABLE_MESSAGES, values, wheres,
				whereArgs);
		return rawId;

	}

	public long setUpdate_unseen_message_count(String jid, int is_sec_chat) {
		long l = 0;
		ContentValues values = new ContentValues();

		values.put("unseen_msg_count", 0);

		String wheres = "key_remote_jid=? and is_sec_chat=?";
		String IDS = String.valueOf(jid);
		String sec = String.valueOf(is_sec_chat);
		String[] whereArgs = { IDS.toString(), sec };
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
				// usd.setIs_niftychat_user(cursor.getString(0));
				list.add(usd);
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}

		return list;
	}

	public Boolean isExistinChatList(String jid) {
		ArrayList<MessageGetSet> list = new ArrayList<MessageGetSet>();
		Cursor cursor = myDataBase.rawQuery(
				"Select * from chat_list where key_remote_jid='" + jid + "' ",
				null);

		if (cursor.getCount() > 0) {
			return true;
		} else {
			return false;
		}

	}

	public Boolean isExistinChatList_chat(String jid, int sec) {
		ArrayList<MessageGetSet> list = new ArrayList<MessageGetSet>();
		Cursor cursor = myDataBase.rawQuery(
				"Select * from chat_list where key_remote_jid='" + jid
						+ "' and is_sec_chat=" + sec + "", null);

		if (cursor.getCount() > 0) {
			return true;
		} else {
			return false;
		}

	}

	public int getLastMsgid(String jid) {
		int id = 0;
		ArrayList<MessageGetSet> list = new ArrayList<MessageGetSet>();
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
		ArrayList<MessageGetSet> list = new ArrayList<MessageGetSet>();
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

	public int getLastMsgid_chat_grp(String jid) {
		int id = 0;
		ArrayList<MessageGetSet> list = new ArrayList<MessageGetSet>();
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
		long l = 0;
		ContentValues values = new ContentValues();

		values.put("message_table_id", msg_id);

		String wheres = "key_remote_jid=?";
		String IDS = String.valueOf(jid);
		String[] whereArgs = { IDS.toString() };
		long rawId = myDataBase.update(TABLE_CHAT_LIST, values, wheres,
				whereArgs);
		return rawId;

	}

	public long setUpdateChat_lits_chat(String jid, int msg_id, int sec) {
		long l = 0;
		ContentValues values = new ContentValues();

		values.put("message_table_id", msg_id);

		String wheres = "key_remote_jid=? and is_sec_chat=?";
		String IDS = String.valueOf(jid);
		String is_sec = String.valueOf(sec);
		String[] whereArgs = { IDS.toString(), is_sec };
		long rawId = myDataBase.update(TABLE_CHAT_LIST, values, wheres,
				whereArgs);
		return rawId;

	}

	public long setUpdateContact_unseen_msg(String jid, int msg) {
		long l = 0;
		ContentValues values = new ContentValues();

		values.put("unseen_msg_count", msg);

		String wheres = "jid=?";
		String IDS = String.valueOf(jid);
		String[] whereArgs = { IDS.toString() };
		long rawId = myDataBase.update(TABLE_CONTACTS, values, wheres,
				whereArgs);
		return rawId;

	}

	public long setUpdateContact_unseen_msg_chat(String jid, int msg, int sec) {
		long l = 0;
		ContentValues values = new ContentValues();

		values.put("unseen_msg_count", msg);

		String wheres = "key_remote_jid=? and is_sec_chat=?";
		String IDS = String.valueOf(jid);
		String is_sec = String.valueOf(sec);
		String[] whereArgs = { IDS.toString(), is_sec };
		long rawId = myDataBase.update(TABLE_CHAT_LIST, values, wheres,
				whereArgs);
		return rawId;

	}

	public long setUpdateContact_unseen_msg_chat_grp(String jid, int msg) {
		long l = 0;
		ContentValues values = new ContentValues();

		values.put("unseen_msg_count", msg);

		String wheres = "key_remote_jid=?";
		String IDS = String.valueOf(jid);

		String[] whereArgs = { IDS.toString() };
		long rawId = myDataBase.update(TABLE_CHAT_LIST, values, wheres,
				whereArgs);
		return rawId;

	}

	public Cursor getChat_list() {
		ArrayList<Chat_list_home_GetSet> list = new ArrayList<Chat_list_home_GetSet>();
		// Cursor
		// cursor=myDataBase.rawQuery("Select c._id,con.display_name,con.photo_ts,m.data,m.timestamp,m.status,m.needs_push,con.unseen_msg_count,con.jid,m.key_from_me ,m.media_wa_type from chat_list c,messages m,contacts con where m._id=c.message_table_id and  c.key_remote_jid =con.jid and is_niftychat_user=1 and raw_contact_id!='0'",null);
		Cursor cursor = myDataBase
				.rawQuery(
						"Select m._id,c.display_name,c.photo_ts,m.data,m.timestamp,m.status,m.needs_push,ch.unseen_msg_count,c.jid,m.key_from_me ,m.media_wa_type,m.is_sec_chat,m.is_owner from messages m,contacts c,chat_list ch where c.jid=m.key_remote_jid and c.raw_contact_id !='0' and m._id=ch.message_table_id group by m.is_sec_chat, m.key_remote_jid order by m.timestamp desc ",
						null);
		/*
		 * if(cursor.moveToFirst()) { do { Chat_list_home_GetSet usd=new
		 * Chat_list_home_GetSet(); usd.set_id(cursor.getInt(0));
		 * usd.setDisplay_name(cursor.getString(1));
		 * usd.setPhoto_ts(cursor.getBlob(2)); usd.setData(cursor.getString(3));
		 * usd.setTimestamp(cursor.getLong(4)); usd.setStatus(cursor.getInt(5));
		 * usd.setNeeds_push(cursor.getInt(6));
		 * usd.setUnseen_msg_count(cursor.getInt(7));
		 * 
		 * list.add(usd); } while (cursor.moveToNext()); } if (cursor != null &&
		 * !cursor.isClosed()) { cursor.close(); }
		 */

		return cursor;
	}

	public ArrayList<Chat_list_GetSet> getAllChat_list() {
		ArrayList<Chat_list_GetSet> list = new ArrayList<Chat_list_GetSet>();

		Cursor cursor = myDataBase.rawQuery("Select * from chat_list", null);
		if (cursor.moveToFirst()) {
			do {
				Chat_list_GetSet usd = new Chat_list_GetSet();
				usd.set_id(cursor.getInt(0));
				usd.setKey_remote_jid(cursor.getString(1));
				usd.setMessage_table_id(cursor.getInt(2));
				usd.setIs_sec_chat(cursor.getInt(3));
				usd.setUnseen_msg_count(cursor.getInt(4));
				list.add(usd);
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}

		return list;
	}

	public Cursor getChat_list_searchview(String value) {
		ArrayList<Chat_list_home_GetSet> list = new ArrayList<Chat_list_home_GetSet>();
		// Cursor
		// cursor=myDataBase.rawQuery("Select c._id,con.display_name,con.photo_ts,m.data,m.timestamp,m.status,m.needs_push,con.unseen_msg_count,con.jid,m.key_from_me ,m.media_wa_type from chat_list c,messages m,contacts con where m._id=c.message_table_id and  c.key_remote_jid =con.jid and is_niftychat_user=1 and raw_contact_id!='0'",null);
		Cursor cursor = null;
		if (value == null) {
			cursor = myDataBase
					.rawQuery(
							"Select m._id,c.display_name,c.photo_ts,m.data,m.timestamp,m.status,m.needs_push,ch.unseen_msg_count,c.jid,m.key_from_me ,m.media_wa_type,m.is_sec_chat,m.is_owner from messages m,contacts c,chat_list ch where c.jid=m.key_remote_jid and c.raw_contact_id !='0' and m._id=ch.message_table_id group by m.is_sec_chat, m.key_remote_jid order by m.timestamp desc ",
							null);
		} else {
			cursor = myDataBase
					.rawQuery(
							"Select m._id,c.display_name,c.photo_ts,m.data,m.timestamp,m.status,m.needs_push,ch.unseen_msg_count,c.jid,m.key_from_me ,m.media_wa_type,m.is_sec_chat,m.is_owner from messages m,contacts c,chat_list ch where c.display_name like '"
									+ value
									+ "%' and c.jid=m.key_remote_jid and c.raw_contact_id !='0' and m._id=ch.message_table_id group by m.is_sec_chat, m.key_remote_jid  order by m.timestamp desc ",
							null);
		}
		/*
		 * if(cursor.moveToFirst()) { do { Chat_list_home_GetSet usd=new
		 * Chat_list_home_GetSet(); usd.set_id(cursor.getInt(0));
		 * usd.setDisplay_name(cursor.getString(1));
		 * usd.setPhoto_ts(cursor.getBlob(2)); usd.setData(cursor.getString(3));
		 * usd.setTimestamp(cursor.getLong(4)); usd.setStatus(cursor.getInt(5));
		 * usd.setNeeds_push(cursor.getInt(6));
		 * usd.setUnseen_msg_count(cursor.getInt(7));
		 * 
		 * list.add(usd); } while (cursor.moveToNext()); } if (cursor != null &&
		 * !cursor.isClosed()) { cursor.close(); }
		 */

		return cursor;
	}

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
							new String[] { inputText + "%" });
		}
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}

	public Cursor getMUCChat_list() {
		ArrayList<Chat_list_home_GetSet> list = new ArrayList<Chat_list_home_GetSet>();
		Cursor cursor = myDataBase
				.rawQuery(
						"Select c._id,con.display_name,con.photo_ts,m.data,m.timestamp,m.status,m.needs_push,con.unseen_msg_count,con.jid,m.key_from_me,m.media_wa_type,m.remote_resource from chat_list c,messages m,contacts con where m._id=c.message_table_id and  c.key_remote_jid =con.jid  and m.remote_resource!=''",
						null);

		/*
		 * if(cursor.moveToFirst()) { do { Chat_list_home_GetSet usd=new
		 * Chat_list_home_GetSet(); usd.set_id(cursor.getInt(0));
		 * usd.setDisplay_name(cursor.getString(1));
		 * usd.setPhoto_ts(cursor.getBlob(2)); usd.setData(cursor.getString(3));
		 * usd.setTimestamp(cursor.getLong(4)); usd.setStatus(cursor.getInt(5));
		 * usd.setNeeds_push(cursor.getInt(6));
		 * usd.setUnseen_msg_count(cursor.getInt(7));
		 * 
		 * list.add(usd); } while (cursor.moveToNext()); } if (cursor != null &&
		 * !cursor.isClosed()) { cursor.close(); }
		 */

		return cursor;
	}

	public Cursor getMUCChat_list_Filter(String value) {
		ArrayList<Chat_list_home_GetSet> list = new ArrayList<Chat_list_home_GetSet>();
		Cursor cursor = null;
		if (value == null) {
			cursor = myDataBase
					.rawQuery(
							"Select c._id,con.display_name,con.photo_ts,m.data,m.timestamp,m.status,m.needs_push,con.unseen_msg_count,con.jid,m.key_from_me,m.media_wa_type,m.remote_resource from chat_list c,messages m,contacts con where m._id=c.message_table_id and  c.key_remote_jid =con.jid  and m.remote_resource!='' order by m.timestamp desc  ",
							null);
		} else {
			cursor = myDataBase
					.rawQuery(
							"Select c._id,con.display_name,con.photo_ts,m.data,m.timestamp,m.status,m.needs_push,con.unseen_msg_count,con.jid,m.key_from_me,m.media_wa_type,m.remote_resource from chat_list c,messages m,contacts con where con.display_name LIKE '"
									+ value
									+ "%' and m._id=c.message_table_id and  c.key_remote_jid =con.jid  and m.remote_resource!='' order by m.timestamp desc ",
							null);
		}

		/*
		 * if(cursor.moveToFirst()) { do { Chat_list_home_GetSet usd=new
		 * Chat_list_home_GetSet(); usd.set_id(cursor.getInt(0));
		 * usd.setDisplay_name(cursor.getString(1));
		 * usd.setPhoto_ts(cursor.getBlob(2)); usd.setData(cursor.getString(3));
		 * usd.setTimestamp(cursor.getLong(4)); usd.setStatus(cursor.getInt(5));
		 * usd.setNeeds_push(cursor.getInt(6));
		 * usd.setUnseen_msg_count(cursor.getInt(7));
		 * 
		 * list.add(usd); } while (cursor.moveToNext()); } if (cursor != null &&
		 * !cursor.isClosed()) { cursor.close(); }
		 */

		return cursor;
	}

	public long setInsertChat_list(String jid, int msg_id) {
		long l = 0;
		ContentValues values = new ContentValues();

		values.put("key_remote_jid", jid);
		values.put("message_table_id", msg_id);

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

	public Boolean isjidExist(String jid) {
		ArrayList<MessageGetSet> list = new ArrayList<MessageGetSet>();
		Cursor cursor = myDataBase.rawQuery(
				"Select * from contacts where jid='" + jid + "'", null);

		if (cursor.getCount() > 0) {
			return true;
		} else {
			return false;
		}

	}

	public Boolean isNiftyChatUser(String jid) {
		ArrayList<MessageGetSet> list = new ArrayList<MessageGetSet>();
		Cursor cursor = myDataBase.rawQuery(
				"Select * from contacts where jid='" + jid
						+ "' and is_niftychat_user=1", null);

		if (cursor.getCount() > 0) {
			return true;
		} else {
			return false;
		}

	}

	public long setDeleteContact(String jid) {

		String wheres = "jid=?";
		String IDS = String.valueOf(jid);
		String[] whereArgs = { IDS.toString() };
		long rawId = myDataBase.delete(TABLE_CONTACTS, wheres, whereArgs);
		return rawId;

	}

	public long setDeleteMessages(String jid) {

		String wheres = " key_remote_jid=?";
		String IDS = String.valueOf(jid);
		String[] whereArgs = { IDS.toString() };
		long rawId = myDataBase.delete(TABLE_MESSAGES, wheres, whereArgs);
		return rawId;

	}

	public long setDeleteChatList(String jid) {

		String wheres = " key_remote_jid=?";
		String IDS = String.valueOf(jid);
		String[] whereArgs = { IDS.toString() };
		long rawId = myDataBase.delete(TABLE_CHAT_LIST, wheres, whereArgs);
		return rawId;

	}

	public long setDelete_All_Messages() {

		String wheres = "media_wa_type IN (?,?,?,?,?,?,?)";
		String[] whereArgs = { "0", "1", "2", "3", "4", "5", "6" };
		long rawId = myDataBase.delete(TABLE_MESSAGES, wheres, whereArgs);
		return rawId;

	}

	public long setDeleteMessages_by_msgid(String id) {

		String wheres = "_id=? and media_wa_type!='7' ";

		String IDS = String.valueOf(id);
		String[] whereArgs = { IDS.toString() };
		long rawId = myDataBase.delete(TABLE_MESSAGES, wheres, whereArgs);
		return rawId;

	}

	public long setDelete_Sec_chat(String jid) {

		String wheres = "key_remote_jid=? and is_sec_chat==0 ";

		String[] whereArgs = { jid };
		long rawId = myDataBase.delete(TABLE_MESSAGES, wheres, whereArgs);
		return rawId;

	}

	public long setDelete_Sec_chat_messages(String jid) {

		String wheres = "key_remote_jid=? and media_wa_type!='7' ";

		String[] whereArgs = { jid };
		long rawId = myDataBase.delete(TABLE_MESSAGES, wheres, whereArgs);
		return rawId;

	}

	public Cursor getGroupChat_list() {
		ArrayList<Chat_list_home_GetSet> list = new ArrayList<Chat_list_home_GetSet>();
		Cursor cursor = myDataBase
				.rawQuery(
						"Select ch._id,c.jid,c.display_name from contacts c,chat_list ch where c.jid=ch.key_remote_jid and c.is_niftychat_user=1 and raw_contact_id='0'",
						null);

		return cursor;
	}

	public Boolean isMessageExist(String key_id) {
		ArrayList<MessageGetSet> list = new ArrayList<MessageGetSet>();
		Cursor cursor = myDataBase.rawQuery(
				"Select * from messages where key_id='" + key_id + "'", null);

		if (cursor.getCount() > 0) {
			return true;
		} else {
			return false;
		}

	}

	public String getDisplay_name(String jid) {
		String name = null;
		ArrayList<MessageGetSet> list = new ArrayList<MessageGetSet>();
		Cursor cursor = myDataBase.rawQuery(
				"Select * from contacts where jid='" + jid + "'", null);
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {
			name = cursor.getString(6);
		}

		return name;

	}

	public void setUpdateSubject(String jid, String subject) {
		long l = 0;
		ContentValues values = new ContentValues();

		values.put("display_name", subject);

		String wheres = "jid=?";
		String IDS = String.valueOf(jid);
		String[] whereArgs = { IDS.toString() };
		long rawId = myDataBase.update(TABLE_CONTACTS, values, wheres,
				whereArgs);

	}

	public long setUpdateContact_onob(String row_contact_id, String name,
			String number) {
		long l = 0;
		ContentValues values = new ContentValues();

		values.put("display_name", name);
		values.put("number", number);

		String wheres = "raw_contact_id=?";
		String IDS = String.valueOf(row_contact_id);
		String[] whereArgs = { IDS.toString() };
		long rawId = myDataBase.update(TABLE_CONTACTS, values, wheres,
				whereArgs);
		return rawId;

	}

	public long setUpdate_Contact_List(ContactsGetSet contact) {
		long l = 0;
		ContentValues values = new ContentValues();

		values.put("status", contact.getStatus());
		values.put("nifty_email", contact.getNifty_email());
		values.put("nifty_name", contact.getNifty_name());
		values.put("photo_ts", contact.getPhoto_ts());
		values.put("is_niftychat_user", contact.getIs_niftychat_user());

		String wheres = "jid=?";
		String IDS = String.valueOf(contact.getJid());
		String[] whereArgs = { IDS.toString() };
		long rawId = myDataBase.update(TABLE_CONTACTS, values, wheres,
				whereArgs);
		return rawId;

	}

	public long setUpdateVcard(ContactsGetSet contact) {
		long l = 0;
		ContentValues values = new ContentValues();

		values.put("status", contact.getStatus());
		values.put("nifty_email", contact.getNifty_email());
		values.put("nifty_name", contact.getNifty_name());
		values.put("photo_ts", contact.getPhoto_ts());

		String wheres = "jid=?";
		String IDS = String.valueOf(contact.getJid());
		String[] whereArgs = { IDS.toString() };
		long rawId = myDataBase.update(TABLE_CONTACTS, values, wheres,
				whereArgs);
		return rawId;

	}

	public long setUpdateLogin(LoginGetSet login) {
		long l = 0;
		ContentValues values = new ContentValues();

		values.put("nifty_name", login.getNifty_name());
		values.put("nifty_email", login.getNifty_email());
		values.put("photo", login.getAvatar());

		String wheres = "user=?";
		String IDS = String.valueOf(login.getUserName());
		String[] whereArgs = { IDS.toString() };
		long rawId = myDataBase.update(TABLE_LOGIN, values, wheres, whereArgs);
		return rawId;
	}

	public long setUpdateLogin_status(String uid, String status) {
		long l = 0;
		ContentValues values = new ContentValues();

		values.put("status", status);

		String wheres = "user=?";
		String IDS = String.valueOf(uid);
		String[] whereArgs = { IDS.toString() };
		long rawId = myDataBase.update(TABLE_LOGIN, values, wheres, whereArgs);
		return rawId;
	}

	public long setUpdateMessage_status(String jid, String key_id, int status) {
		long l = 0;
		ContentValues values = new ContentValues();

		values.put("status", status);
		values.put("needs_push", 0);

		String wheres = "key_remote_jid=? and key_id=?";
		String IDS = String.valueOf(jid);
		String[] whereArgs = { IDS.toString(), key_id };
		long rawId = myDataBase.update(TABLE_MESSAGES, values, wheres,
				whereArgs);
		return rawId;

	}

	public long setUpdateMessage_status(String key_id, int status) {
		long l = 0;
		ContentValues values = new ContentValues();

		values.put("status", status);
		values.put("needs_push", 1);

		String wheres = "key_id=?";

		String[] whereArgs = { key_id };
		long rawId = myDataBase.update(TABLE_MESSAGES, values, wheres,
				whereArgs);
		return rawId;

	}

	public long setUpdateMessage_need_push(String key_id, int status) {
		long l = 0;
		ContentValues values = new ContentValues();

		values.put("status", status);
		values.put("needs_push", 1);

		String wheres = "key_id=?";

		String[] whereArgs = { key_id };
		long rawId = myDataBase.update(TABLE_MESSAGES, values, wheres,
				whereArgs);
		return rawId;

	}

	public long setUpdateMessage_Media_url(String key_id, String url) {
		long l = 0;
		ContentValues values = new ContentValues();

		values.put("media_url", url);

		String wheres = "_id=?";

		String[] whereArgs = { key_id };
		long rawId = myDataBase.update(TABLE_MESSAGES, values, wheres,
				whereArgs);
		return rawId;

	}

	public long setUpdateMessage_Origin(String _id, int origin) {
		long l = 0;
		ContentValues values = new ContentValues();

		values.put("origin", origin);

		String wheres = "_id=?";

		String[] whereArgs = { _id };
		long rawId = myDataBase.update(TABLE_MESSAGES, values, wheres,
				whereArgs);
		return rawId;

	}

	public long setUpdateMessage_MediaName(String key_id, String media_name) {
		long l = 0;
		ContentValues values = new ContentValues();
		values.put("media_name", media_name);
		String wheres = "key_id=?";

		String[] whereArgs = { key_id };
		long rawId = myDataBase.update(TABLE_MESSAGES, values, wheres,
				whereArgs);
		return rawId;

	}
}
