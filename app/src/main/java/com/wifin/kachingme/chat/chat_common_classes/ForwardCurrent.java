package com.wifin.kachingme.chat.chat_common_classes;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.chat.single_chat.ChatTest;
import com.wifin.kachingme.database.DatabaseHelper;
import com.wifin.kachingme.database.Dbhelper;
import com.wifin.kachingme.pojo.MessageGetSet;
import com.wifin.kachingme.util.AlertManager;
import com.wifin.kachingme.util.AvatarManager;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.Emoji;
import com.wifin.kachingme.util.Log;
import com.wifin.kachingme.emojicons.pmc.PopupMenuCompat;

public class ForwardCurrent extends Fragment {
	ActionMode mMode;
	private SearchView mSearchView;

	ListView list;
	Cursor cursor;
	Chat_list_Adapter adapter;
	DatabaseHelper dbAdapter;

	Resources res;
	Dbhelper db;
	String status_lock = "";
	SharedPreferences sp;
	Editor ed;

	@Override
	public void onResume() {

		new FetchChat().execute();
		getActivity()
				.registerReceiver(lastseen_event, new IntentFilter("chat"));

		super.onResume();
	}

	@Override
	public void onStart() {
		new FetchChat().execute();
		/*
		 * if(get) getActivity().registerReceiver(lastseen_event, null);
		 */
		super.onStart();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.contactlist, container, false);
		setHasOptionsMenu(true);

		dbAdapter = KachingMeApplication.getDatabaseAdapter();
		list = (ListView) v.findViewById(R.id.list_experience);
		db = new Dbhelper(getActivity());
		sp = getActivity().getSharedPreferences(
				KachingMeApplication.getPereference_label(), Activity.MODE_PRIVATE);
		ed = sp.edit();
		res = getActivity().getResources();
		new FetchChat().execute();

		/* getActivity(). */registerForContextMenu(list);

		View view = new View(getActivity());
		list.addFooterView(view);

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				cursor.moveToPosition(position);

				Constant.printMsg("jid    ::::::: " + cursor.getString(7)
						+ "   " + cursor.getString(8) + "   "
						+ cursor.getString(9) + "  " + cursor.getString(10));
				String query = "select status from " + Dbhelper.TABLE_LOCK
						+ " where jid = '" + cursor.getString(8) + "'";
				Constant.printMsg("bhaththam " + query);
				lock_status(query);
				// if (sp.contains(cursor.getString(8) + "_lock")) {

				if (status_lock.equalsIgnoreCase("lock")) {
					final String jid = cursor.getString(8);
					final String name = cursor.getString(1);
					final int is_owner = cursor.getInt(12);
					final byte[] avtar = cursor.getBlob(2);
					// final int is_sec_chat = 0;

					AlertDialog.Builder alert = new AlertDialog.Builder(
							getActivity());

					alert.setTitle(res.getString(R.string.open_chat));
					alert.setMessage("Enter password to forward chat with "
							+ cursor.getString(1));

					final EditText input = new EditText(getActivity());
					input.setInputType(InputType.TYPE_CLASS_TEXT
							| InputType.TYPE_CLASS_NUMBER);
					input.setTransformationMethod(PasswordTransformationMethod
							.getInstance());

					alert.setView(input);

					alert.setPositiveButton(res.getString(R.string.open),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

									String value = input.getText().toString();
									String query = "select password from "
											+ Dbhelper.TABLE_LOCK
											+ " where jid = '" + jid + "'";
									Constant.printMsg("bhaththam value"
											+ value);
									lock_status(query);
									if (value.equals(status_lock)) {
										// if (value.equals(sp.getString("pin",
										// ""))) {
										final Intent in = new Intent(
												getActivity(), ChatTest.class);

										in.putExtra("jid", jid);
										in.putExtra("name", name);
										in.putExtra("is_owner", "" + is_owner);
										in.putExtra("avatar", avtar);
										in.putExtra("msg_ids",
												ForwardList.msg_ids);
										in.putExtra("is_owner",
												"" + cursor.getInt(12));
										// in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
										startActivity(in);
									} else {

										new AlertManager().showAlertDialog(
												getActivity(),
												res.getString(R.string.you_are_entered_incorrect_pin),
												true);
									}

								}
							});

					alert.setNegativeButton(res.getString(R.string.cancel),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									// Canceled.

								}
							});

					alert.show();

				} else {
					if (ForwardList.IS_EMAIL) {
						Email_conversation(cursor.getString(8));
					} else {
						// MessageGetSet
						// msg=dbAdapter.getMessages_by_key_id(forward_list.msg_ids);

						Intent intent = new Intent(getActivity(), ChatTest.class);
						intent.putExtra("jid", cursor.getString(8));
						intent.putExtra("name", cursor.getString(1));
						intent.putExtra("avatar", cursor.getBlob(2));
						Log.i("Forword Current", "Message IDS "
								+ ForwardList.msg_ids);
						intent.putExtra("msg_ids", ForwardList.msg_ids);
						intent.putExtra("is_owner", "" + cursor.getInt(12));
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						if (cursor.getInt(11) == 0) {
							intent.putExtra("IS_SECRET_CHAT", true);
						} else {
							intent.putExtra("IS_SECRET_CHAT", false);
						}
						startActivity(intent);
						getActivity().finish();

					}
				}

			}
		});
		return v;

	}

	BroadcastReceiver lastseen_event = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			if (intent.getAction().equals("chat")
					|| intent.getAction().equals("Profile_Update")) {
				new FetchChat().execute();
			}
		}

	};

	@Override
	public void onStop() {
		getActivity().unregisterReceiver(lastseen_event);
		/* cursor.close(); */
		super.onStop();
	}

	private class FetchChat extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			cursor = dbAdapter.getChat_list();

			Log.d("Chat", "Cursor Size::" + cursor.getCount());

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			adapter = new Chat_list_Adapter(getActivity(), cursor, false,
					new Emoji(getActivity()).getEmoticons());
			list.setAdapter(adapter);

			// updateReadMessages();
			// cursor.close();
			super.onPostExecute(result);
		}

	}

	private void updateCursor() {
		cursor = dbAdapter.getChat_list();

	}

	public class Chat_list_Adapter extends CursorAdapter {

		Context context;
		// LayoutInflater inflater;

		int font_size = 0;
		PopupMenuCompat popupMenu;

		String partner_uid;
		private HashMap<String, Integer> emoticons = new HashMap<String, Integer>();

		public Chat_list_Adapter(Context context, Cursor c,
				boolean autoRequery, HashMap<String, Integer> em) {
			super(context, c, autoRequery);
			/* Log.d("ChatAdapter","ChatAdapter Initialized...."+c.getCount()); */
			this.context = context;
			this.partner_uid = partner_uid;
			// inflater = LayoutInflater.from(context);

			emoticons = em;
		}

		@Override
		public void registerDataSetObserver(DataSetObserver observer) {
			// TODO Auto-generated method stub
			super.registerDataSetObserver(observer);

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (!mDataValid) {
				throw new IllegalStateException(
						"this should only be called when the cursor is valid");
			}
			if (!mCursor.moveToPosition(position)) {
				throw new IllegalStateException(
						"couldn't move cursor to position " + position);
			}
			View v;
			if (convertView == null) {
				v = newView(mContext, mCursor, parent);
			} else {
				v = convertView;
			}
			bindView(v, mContext, mCursor);
			return v;
		}

		@Override
		public void bindView(View convertView, Context arg1, final Cursor c) {

			ViewHolder holder = (ViewHolder) convertView.getTag();

			byte image_data[] = c.getBlob(2);
			try {
				if (image_data != null) {
					Bitmap bmp = BitmapFactory.decodeByteArray(image_data, 0,
							image_data.length);
					holder.avatar.setImageBitmap(new AvatarManager()
							.roundCornerImage(bmp, 180));
				} else {
					holder.avatar.setImageDrawable(getActivity().getResources()
							.getDrawable(R.drawable.avtar));
				}
			} catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
				// TODO: handle exception
			}

			String query1 = "select status from " + Dbhelper.TABLE_LOCK
					+ " where jid = '" + c.getString(8) + "'";
			Constant.printMsg("bhaththam " + query1);

			lock_status(query1);

			// if (sp.contains(cursor.getString(8) + "_lock")) {
			if (status_lock.equalsIgnoreCase("lock")) {
				holder.txt_status.setVisibility(View.INVISIBLE);
				holder.img_is_lock.setVisibility(View.VISIBLE);
				holder.img_status.setVisibility(View.INVISIBLE);
			} else {
				holder.txt_status.setVisibility(View.VISIBLE);
				holder.img_is_lock.setVisibility(View.GONE);
				holder.img_status.setVisibility(View.VISIBLE);

			}
			holder.txt_name.setText(c.getString(1));

			String status = c.getString(10);
			int unread_count = c.getInt(7);
			int image_status = c.getInt(5);

			if (status.equals("1")) {
				holder.txt_status.setText(res.getString(R.string.image));
			} else if (status.equals("2")) {
				holder.txt_status.setText(res.getString(R.string.video));
			} else if (status.equals("3")) {
				holder.txt_status.setText(res.getString(R.string.audio));
			} else if (status.equals("4")) {
				holder.txt_status.setText(res.getString(R.string.location));
			} else if (status.equals("5")) {
				holder.txt_status.setText(res.getString(R.string.contact));
			} else if (status.equals("6")) {
				holder.txt_status.setText(res.getString(R.string.file));
			} else if (status.equals("7")) {
				holder.txt_status
						.setText(res.getString(R.string.self_des_time));
			} else if (status.equals("9")) {

				holder.txt_status.setText(new Emoji(context)
						.getSmiledText_List(c.getString(3)));
			} else {
				holder.txt_status.setText(new Emoji(context)
						.getSmiledText_List(c.getString(3)));
			}

			if (unread_count > 0) {
				holder.txt_unread.setVisibility(View.VISIBLE);
				holder.txt_unread.setText("" + unread_count);
				holder.txt_unread.setBackgroundColor(context.getResources()
						.getColor(R.color.app_color_blue));
				holder.txt_status.setTextColor(context.getResources().getColor(
						R.color.app_color_blue));
				holder.img_status
						.setImageResource(R.drawable.ic_action_av_play_app_color_blue);
			} else {
				holder.txt_unread.setVisibility(View.GONE);
				if (c.getInt(9) == 1) {
					holder.img_status
							.setImageResource(R.drawable.ic_action_av_play);
				} else {

					if (image_status == 3 || image_status == 2) {
						holder.img_status
								.setImageResource(R.drawable.message_unsent);
					} else if (image_status == 1) {
						holder.img_status
								.setImageResource(R.drawable.receipt_from_server);
					} else if (image_status == 0) {
						holder.img_status
								.setImageResource(R.drawable.receipt_from_target);
					}

				}
				holder.txt_status.setTextColor(res
						.getColor(R.color.app_color_dark_gray));
			}

			if (c.getInt(11) == 1) {

				holder.img_is_sec.setVisibility(View.GONE);
			} else {

				holder.img_is_sec.setVisibility(View.VISIBLE);
			}

			long date_data = c.getLong(4);

			Date d = new Date(System.currentTimeMillis()
					- (1000 * 60 * 60 * 24));
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat sdf_time = new SimpleDateFormat("hh:mma");
			String yesterday = sdf.format(d);
			String today = sdf.format(new Date());
			String chat_date = sdf.format(new Date(date_data));
			// Constant.printMsg("Chat Date::"+chat_date+"Yesterday::"+yesterday+" today::"+today);
			if (today.equals(chat_date)) {
				holder.txt_date.setText(sdf_time.format(new Date(date_data)));
			} else if (chat_date.equals(yesterday)) {
				holder.txt_date.setText(getResources().getString(
						R.string.yesterday));
			} else {
				holder.txt_date.setText(chat_date);
			}
			holder.btn_more.setVisibility(View.GONE);
			holder.btn_more.setTag(c.getPosition());

		}

		@Override
		public View newView(Context arg0, Cursor c, ViewGroup parent) {

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			final View retView = inflater.inflate(R.layout.item_chats, parent,
					false);

			ViewHolder holder = new ViewHolder();

			holder.txt_status = (TextView) retView.findViewById(R.id.status);
			holder.txt_unread = (TextView) retView
					.findViewById(R.id.txt_unread_msg);
			holder.avatar = (com.github.siyamed.shapeimageview.CircularImageView) retView
					.findViewById(R.id.avtarimg);
			holder.img_is_sec = (ImageView) retView
					.findViewById(R.id.img_is_sec);
			holder.txt_name = (TextView) retView.findViewById(R.id.name);
			holder.img_status = (ImageView) retView
					.findViewById(R.id.img_status);
			holder.txt_date = (TextView) retView.findViewById(R.id.txt_date);
			holder.btn_more = (ImageButton) retView.findViewById(R.id.btn_more);
			holder.img_is_lock = (ImageView) retView
					.findViewById(R.id.img_is_lock);

			retView.setTag(holder);

			return retView;
		}

		/*
		 * @Override public int getViewTypeCount() { // TODO Auto-generated
		 * method stub return 2; }
		 *
		 *
		 * @Override public int getItemViewType(int position) { Cursor
		 * c=(Cursor)getItem(position); String fromuser=c.getString(1);
		 * if(fromuser.equals(partner_uid)){ return 0; }else{ return 1; }
		 *
		 * }
		 */

		class ViewHolder {
			TextView txt_status;
			TextView txt_unread;
			ImageView avatar;
			ImageView img_is_sec;
			TextView txt_name;
			ImageView img_status;
			TextView txt_date;
			ImageButton btn_more;
			ImageView img_is_lock;

			/*
			 * public ViewHolder(View view) {
			 *
			 * ButterKnife.inject(this,view); }
			 */
		}

	}

	public void Email_conversation(String jid) {

		ArrayList<MessageGetSet> msg_list = dbAdapter.getMessages_by_jid(jid);
		String target_name = "";
		if (dbAdapter.getDisplay_name(jid) == null) {
			target_name = jid.split("@")[0];
		} else {
			target_name = dbAdapter.getDisplay_name(jid);
		}

		String source_name = KachingMeApplication.getNifty_name();
		String conversation = "";
		for (int i = 0; i < msg_list.size(); i++) {
			if (msg_list.get(i).getMedia_wa_type().equals("0")) {
				Date date = new Date();
				date.setTime(msg_list.get(i).getTimestamp());
				SimpleDateFormat time_format = new SimpleDateFormat(
						"hh:mma, MMM dd");
				String time = time_format.format(date);
				String msg = msg_list.get(i).getData();

				Log.d("Forward_contact", "Message::" + msg);
				if (msg_list.get(i).getKey_from_me() == 0) {
					conversation = conversation + time + " - " + source_name
							+ ": " + msg + "\r\n";
				} else {
					conversation = conversation + time + " - " + target_name
							+ ": " + msg + "\r\n";
				}
			}

		}
		Log.d("forword_contact", "Text File::" + conversation);

		File gpxfile = null;
		try {

			File root = null;

			root = new File(Environment.getExternalStorageDirectory(),
					"NiftyChat");

			if (!root.exists()) {
				root.mkdirs();
			}

			gpxfile = new File(root, getResources().getString(
					R.string.niftychat_with)
					+ " " + target_name + ".txt");
			FileWriter writer = new FileWriter(gpxfile);
			writer.append(conversation);
			writer.flush();
			writer.close();

		} catch (IOException e) {// ACRA.getErrorReporter().handleException(e);
			e.printStackTrace();

		}

		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		emailIntent.setType("plain/text");

		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
				getResources().getString(R.string.niftychat_with) + " "
						+ target_name);
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, String.format(
				getResources().getString(R.string.chat_history), target_name));
		emailIntent.putExtra(android.content.Intent.EXTRA_STREAM,
				Uri.fromFile(gpxfile));
		startActivity(Intent.createChooser(emailIntent, "Send mail..."));
		getActivity().finish();
	}

	private String lock_status(String query) {
		// TODO Auto-generated method stub
		Cursor c = null;

		try {

			Constant.printMsg("query  " + query);

			c = db.open().getDatabaseObj().rawQuery(query, null);
            Constant.printMsg("No of deleted rows ::::::::::" + c.getCount());
			c.moveToFirst();
			if (c.getCount() > 0) {
				Constant.printMsg("lock_status " + c.getString(0));
				status_lock = c.getString(0);
				Constant.printMsg("status_lock   " + status_lock);
			} else {
				status_lock = "not_lock";
			}

		} catch (SQLException e) {

		} finally {
			c.close();
			db.close();
		}
		return status_lock;

	}
}