package com.wifin.kachingme.chat.chat_common_classes;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.chat.single_chat.ChatTest;
import com.wifin.kachingme.database.DatabaseHelper;
import com.wifin.kachingme.pojo.MessageGetSet;
import com.wifin.kachingme.pojo.ContactsGetSet;
import com.wifin.kachingme.util.AvatarManager;
import com.wifin.kachingme.util.Log;

public class ForwardContacts extends Fragment {
	ActionMode mMode;
	private SearchView mSearchView;
	DatabaseHelper dbAdapter;
	ArrayList<ContactsGetSet> users;
	ListView lstview;

	Handler h;
	Thread refresh;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.multiselect_memebers, container, false);
		setHasOptionsMenu(true);

		lstview = (ListView) v.findViewById(R.id.list_experience);
		dbAdapter = KachingMeApplication.getDatabaseAdapter();
		users = new ArrayList<ContactsGetSet>();
		users = dbAdapter.getContacts();
		UserListAdapter adapter = new UserListAdapter(getActivity(),
				R.layout.contactlistitem, users);
		lstview.setAdapter(adapter);
		View view = new View(getActivity());
		lstview.addFooterView(view);

		lstview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				if (ForwardList.IS_EMAIL) {
					Email_conversation(users.get(position).getJid());
				} else {
					// MessageGetSet
					// msg=dbAdapter.getMessages_by_msg_id(forward_list.msg_ids);
					Intent intent = new Intent(getActivity(), ChatTest.class);
					intent.putExtra("jid", users.get(position).getJid());
					intent.putExtra("name", users.get(position)
							.getDisplay_name());
					intent.putExtra("avatar", users.get(position).getPhoto_ts());
					Log.i("Forword Contacts", "Message IDS "
							+ ForwardList.msg_ids);
					intent.putExtra("msg_ids", ForwardList.msg_ids);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.putExtra("IS_SECRET_CHAT", false);
					intent.putExtra("is_owner", "1");
					startActivity(intent);
					getActivity().finish();
				}
			}
		});

		// Intent i=new Intent(FevouriteContacts.this,MainActivity.class);
		return v;

	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);

	}

	private final class AnActionModeOfEpicProportions implements
			ActionMode.Callback {
		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {

			MenuInflater inflater = new MenuInflater(getActivity());

			/* inflater.inflate(R.menu.menu_list_action, menu); */

			return true;
		}

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false;
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			/*
			 * Toast.makeText(Demo_Activity.activity, "Got click: " + item,
			 * Toast.LENGTH_SHORT).show(); mode.finish();
			 */

			switch (item.getItemId()) {

			/*
			 * case R.id.list_view: mode.finish(); startActivity(new
			 * Intent(Demo_Activity.activity, detail_view.class));
			 * 
			 * return true; case R.id.list_delete: mode.finish(); new
			 * AlertDialog.Builder(getActivity()) .setTitle("Really Delete?")
			 * .setMessage("Are you sure you want to delete?")
			 * .setNegativeButton(android.R.string.no, null)
			 * .setPositiveButton(android.R.string.yes, new
			 * DialogInterface.OnClickListener() {
			 * 
			 * public void onClick(DialogInterface arg0, int arg1) {
			 * delete(Tab1.BEAN.getId());
			 * 
			 * FILL_DATA(); } }).create().show(); return true;
			 * 
			 * case R.id.list_edit: mode.finish();
			 * 
			 * startActivity(new Intent(Demo_Activity.activity,
			 * Fragment_Activity_Edit.class));
			 * 
			 * return true;
			 */

			default:

				return true;
			}

		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
		}
	}

	private class UserListAdapter extends ArrayAdapter<ContactsGetSet> {
		private int resource;
		private LayoutInflater layoutInflater;
		ArrayList<ContactsGetSet> list;
		Context context;

		public UserListAdapter(Context context, int textViewResourceId,
				ArrayList<ContactsGetSet> objects) {
			super(context, textViewResourceId, objects);
			// TODO Auto-generated constructor stub
			this.resource = textViewResourceId;
			layoutInflater = LayoutInflater.from(context);
			list = objects;
			this.context = context;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			LinearLayout container = (LinearLayout) (inflater.inflate(resource,
					parent, false));
			ContactsGetSet user = list.get(position);

			// set name

			TextView name = (TextView) container.findViewById(R.id.name);

			name.setText(user.getDisplay_name());

			try {
				ImageView img = (ImageView) container
						.findViewById(R.id.avtarimg);
				Bitmap bmp = BitmapFactory.decodeByteArray(user.getPhoto_ts(),
						0, user.getPhoto_ts().length);
				img.setImageBitmap(new AvatarManager().roundCornerImage(bmp,
						180));
			} catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
				// TODO: handle exception
			}
			// set status
			TextView status = (TextView) container.findViewById(R.id.status);

			try {
				status.setText(user.getStatus());
			} catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
				// TODO: handle exceptionas
				e.printStackTrace();
			}

			/*
			 * if(roster.getPresence(user.getUser()).toString().contains("available"
			 * )&&!roster.getPresence(user.getUser()).toString().contains(
			 * "unavailable")) { v.setTextColor(Color.GREEN); v.setTextSize(14);
			 * }
			 */
			return container;
		}

	}

	@Override
	public void onPause() {
		getActivity().unregisterReceiver(lastseen_event);
		super.onPause();
	}

	@Override
	public void onResume() {
		getActivity().registerReceiver(lastseen_event,
				new IntentFilter("Profile_Update"));
		super.onResume();
	}

	BroadcastReceiver lastseen_event = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			if (intent.getAction().equals("Profile_Update")) {
				users = new ArrayList<ContactsGetSet>();
				users = dbAdapter.getContacts();
				UserListAdapter adapter = new UserListAdapter(getActivity(),
						R.layout.contactlistitem, users);
				lstview.setAdapter(adapter);
			}
		}

	};

	public void Email_conversation(String jid) {

		ArrayList<MessageGetSet> msg_list = dbAdapter.getMessages_by_jid(jid);
		String target_name = dbAdapter.getDisplay_name(jid);
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

			gpxfile = new File(root, "Niftychat with " + target_name + ".txt");
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
}
