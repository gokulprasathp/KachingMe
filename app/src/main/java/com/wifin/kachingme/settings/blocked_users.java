package com.wifin.kachingme.settings;

import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.privacy.PrivacyListManager;
import org.jivesoftware.smackx.privacy.packet.PrivacyItem;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.SearchView;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.applications.SherlockBaseActivity;
import com.wifin.kachingme.database.DatabaseHelper;
import com.wifin.kachingme.pojo.ContactsGetSet;
import com.wifin.kachingme.services.TempConnectionService;
import com.wifin.kachingme.util.AlertUtils;
import com.wifin.kachingme.util.AvatarManager;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.Log;
import com.wifin.kaching.me.ui.R;

public class blocked_users extends SherlockBaseActivity {
	ActionMode mMode;
	private SearchView mSearchView;
	DatabaseHelper dbAdapter;
	ArrayList<ContactsGetSet> users;
	ListView lstview;
	Button unblock_all;

	Handler h;
	Thread refresh;
	EditText search_edit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_lock);

		// doBindService();

		lstview = (ListView) findViewById(R.id.list_experience);
		unblock_all = (Button) findViewById(R.id.unblock_btn);
        Constant.typeFace(this,unblock_all);
		dbAdapter = KachingMeApplication.getDatabaseAdapter();
		users = new ArrayList<ContactsGetSet>();
//		search_edit = (EditText) findViewById(R.id.serchEdit);
//		search_edit.setVisibility(View.GONE);
		/* users=dbAdapter.getContacts(); */
		unblock_all.setVisibility(View.GONE);

		ArrayList<String> blocked = KachingMeApplication.getBlocked_user();
		for (int i = 0; i < blocked.size(); i++) {


            if (blocked.get(i).contains("@localhost")) {
                ContactsGetSet contact = dbAdapter.getContact(blocked.get(i));
                users.add(contact);
            }
		}

		UserListAdapter adapter = new UserListAdapter(this,
				R.layout.lock_list_item, users);
		lstview.setAdapter(adapter);
		registerForContextMenu(lstview);
		unblock_all.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// ArrayList<String> blocked_list = new ArrayList<String>();
				// blocked_list.addAll(KachingMeApplication.getBlocked_user());
				// for (int index = 0; index < blocked_list.size(); index++) {
				//
				// }
				// unblock();
				alert_all();
				// unblock_all();
			}
		});
		lstview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				com.wifin.kachingme.util.Constant.selected_block_position = position;
				alert_one();

				// lstview.showContextMenuForChild(view);
				/*
				 * Intent intent=new Intent(ContactList.ACTIVITY,Chat.class);
				 * intent.putExtra("jid",users.get(position).getJid());
				 * intent.putExtra
				 * ("name",users.get(position).getDisplay_name());
				 * intent.putExtra("avatar",users.get(position).getPhoto_ts());
				 * startActivity(intent);
				 */

			}
		});

	}

	protected void alert_all() {
		// TODO Auto-generated method stub
		AlertDialog.Builder b;
		b = new AlertDialog.Builder(this);

		b.setCancelable(false);
		b.setMessage("Are you sure you want to unblock all").setCancelable(
				false);

		b.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				unblock_all();

			}
		});
		b.setPositiveButton("NO", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {

			}
		});
		b.setCancelable(true);

		AlertDialog alert = b.create();
		alert.show();
	}

	protected void alert_one() {
		// TODO Auto-generated method stub
		AlertDialog.Builder b;
		b = new AlertDialog.Builder(this);

		b.setCancelable(false);
		b.setMessage("Are you sure you want to unblock this number ?")
				.setCancelable(false);

		b.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				unblock_one();

			}
		});
		b.setPositiveButton("NO", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {

			}
		});
		b.setCancelable(true);

		AlertDialog alert = b.create();
		alert.show();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		/*
		 * String title = mylist.get(info.position).getTempName();
		 * menu.setHeaderTitle(title.toString().toUpperCase());
		 */
		menu.add(Menu.NONE, 0, 0, getResources().getString(R.string.unblock)
				+ " " + users.get(info.position).getDisplay_name());
		/*
		 * String[] menuItems =
		 * getResources().getStringArray(R.array.ar_menu_userchat); for (int i =
		 * 0; i<menuItems.length; i++) { menu.add(Menu.NONE, i, i,
		 * menuItems[i]); }
		 */
	}

	@Override
	public boolean onContextItemSelected(android.view.MenuItem item) {
		// TODO Auto-generated method stub
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		int menuItemIndex = item.getItemId();
		/*
		 * cursor.moveToPosition(info.position);
		 * Log.d("Context Menu",cursor.getString(8));
		 */
		Log.d("Context Menu", "Clicked at::" + info.position);
		// if (menuItemIndex == 0 && KachingMeApplication.getIsNetAvailable()) {
		// try {
		// ArrayList<String> list = new ArrayList<String>();
		//
		// PrivacyListManager mngr = mBoundService.getPrivacyListManager();
		// List<PrivacyItem> privacy_items = null;
		// try {
		// privacy_items = mngr.getPrivacyList(
		// KachingMeApplication.getUserID()).getItems();
		// } catch (Exception e) {
		// // ACRA.getErrorReporter().handleException(e);
		// // TODO: handle exception
		// }
		// ArrayList<PrivacyItem> privacy_items_updated = new
		// ArrayList<PrivacyItem>();
		// for (PrivacyItem privacyItem : privacy_items) {
		// if (!users.get(info.position).getJid().toString()
		// .equals(privacyItem.getValue())) {
		// list.add(privacyItem.getValue());
		// privacy_items_updated.add(privacyItem);
		// }
		//
		// }
		//
		// if (privacy_items_updated.size() == 0) {
		// mngr.deletePrivacyList(KachingMeApplication.getUserID());
		// } else {
		// mngr.updatePrivacyList(KachingMeApplication.getUserID(),
		// privacy_items_updated);
		// }
		//
		// users.clear();
		// for (int i = 0; i < privacy_items_updated.size(); i++) {
		// contactsGetSet contact = dbAdapter
		// .getContact(privacy_items_updated.get(i).getValue());
		// users.add(contact);
		// }
		//
		// UserListAdapter adapter = new UserListAdapter(this,
		// R.layout.contactlistitem, users);
		// lstview.setAdapter(adapter);
		//
		// // KachingMeApplication.setBlocked_user(list);
		// } catch (Exception e) {
		// // ACRA.getErrorReporter().handleException(e);
		// e.printStackTrace();
		// // TODO: handle exception
		// }
		// } else {
		// new AlertUtils().Toast_call(this,
		// getResources().getString(R.string.no_internet_connection));
		// }

		unblock_one();

		/*
		 * else { dbAdapter.setDeleteMessages(cursor.getString(8)); new
		 * FetchChat().execute(); }
		 */

		return super.onContextItemSelected(item);
	}

	/*
	 * private ServiceConnection mConnection = new ServiceConnection() { public
	 * void onServiceConnected(ComponentName className, IBinder service) {
	 * 
	 * mBoundService = ((KaChingMeService.LocalBinder) service) .getService();
	 * connection = mBoundService.getConnection();
	 * 
	 * Log.d("Connection", "Is connect::" + connection.isConnected()); }
	 * 
	 * public void onServiceDisconnected(ComponentName className) {
	 * 
	 * mBoundService = null; } };
	 * 
	 * void doBindService() {
	 * 
	 * bindService(new Intent(this, KaChingMeService.class), mConnection,
	 * Context.BIND_AUTO_CREATE); isBound = true; }
	 * 
	 * void doUnbindService() { if (isBound) {
	 * 
	 * unbindService(mConnection); isBound = false; } }
	 */

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
			} catch (Exception e) {
				// ACRA.getErrorReporter().handleException(e);
				// TODO: handle exception
			}
			// set status
			TextView status = (TextView) container.findViewById(R.id.status);

			try {
				status.setText(user.getStatus());
			} catch (Exception e) {
				// TODO: handle exceptionas
				// ACRA.getErrorReporter().handleException(e);
				e.printStackTrace();
			}

			return container;
		}

	}

	@Override
	public void onPause() {
		// doUnbindService();
		super.onPause();
	}

	@Override
	public void onResume() {

		// doBindService();

		super.onResume();
	}

	@Override
	protected void onStart() {
		// doBindService();
		super.onStart();
	}

	Editor ed;
	SharedPreferences sharedPrefs;
	SharedPreferences sp, pref;

	public void unblock_all() {

		sp = getSharedPreferences(KachingMeApplication.getPereference_label(),
				Activity.MODE_PRIVATE);
		ed = sp.edit();

		try {

			ArrayList<String> list = new ArrayList<String>();

			PrivacyListManager mngr = PrivacyListManager
					.getInstanceFor(TempConnectionService.connection);

			List<PrivacyItem> privacy_items = null;
			try {
				privacy_items = mngr.getPrivacyList(
						KachingMeApplication.getUserID()).getItems();
			} catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
				privacy_items = new ArrayList<PrivacyItem>();
				// TODO: handle exception
			}
			ArrayList<PrivacyItem> privacy_items_updated = new ArrayList<PrivacyItem>();
			for (PrivacyItem privacyItem : privacy_items) {
				Log.d("Privacy List", "List::" + privacyItem.getValue());

				// if (!jid.equals(privacyItem.getValue())) {
				// list.add(privacyItem.getValue());
				// privacy_items_updated.add(privacyItem);
				// }

			}

			String blocked = "";

			try {
				for (int i = 0; i < list.size(); i++) {

					if (i == 0) {
						blocked = list.get(i);
					} else {
						blocked += "," + list.get(i);
					}
				}

			} catch (Exception e) {

			}

			ed.putString(Constant.BLOCKED_USERS, blocked);
			ed.commit();

			KachingMeApplication.setBlocked_user(list);

			mngr.updatePrivacyList(KachingMeApplication.getUserID(),
					privacy_items_updated);

			Log.d("Privacy List", "List unblocked..");
			// supportInvalidateOptionsMenu();
			// UserBlocked = false;
			// if (tvBlock != null) {
			// if (UserBlocked == true)
			// tvBlock.setText(getResources().getString(R.string.unblock));
			// else
			// tvBlock.setText(getResources().getString(R.string.block));
			// }

		} catch (XMPPException e) {// ACRA.getErrorReporter().handleException(e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoResponseException e) {// ACRA.getErrorReporter().handleException(e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotConnectedException e) {// ACRA.getErrorReporter().handleException(e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		users.clear();
		ArrayList<String> blocked = KachingMeApplication.getBlocked_user();
		for (int i = 0; i < blocked.size(); i++) {
			ContactsGetSet contact = dbAdapter.getContact(blocked.get(i));
			users.add(contact);
		}

		UserListAdapter adapter = new UserListAdapter(this,
				R.layout.contactlistitem, users);
		lstview.setAdapter(adapter);

	}

	public void unblock_one() {

		sp = getSharedPreferences(KachingMeApplication.getPereference_label(),
				Activity.MODE_PRIVATE);
		ed = sp.edit();

		try {

			ArrayList<String> list = new ArrayList<String>();

			PrivacyListManager mngr = PrivacyListManager
					.getInstanceFor(TempConnectionService.connection);

			List<PrivacyItem> privacy_items = null;
			try {
				privacy_items = mngr.getPrivacyList(
						KachingMeApplication.getUserID()).getItems();
			} catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
				privacy_items = new ArrayList<PrivacyItem>();
				// TODO: handle exception
			}
			ArrayList<PrivacyItem> privacy_items_updated = new ArrayList<PrivacyItem>();
			for (PrivacyItem privacyItem : privacy_items) {
				Log.d("Privacy List", "List::" + privacyItem.getValue());

				if (!KachingMeApplication.getBlocked_user()
						.get(com.wifin.kachingme.util.Constant.selected_block_position)
						.equals(privacyItem.getValue())) {
					list.add(privacyItem.getValue());
					privacy_items_updated.add(privacyItem);
				}

			}

			String blocked = "";

			try {
				for (int i = 0; i < list.size(); i++) {

					if (i == 0) {
						blocked = list.get(i);
					} else {
						blocked += "," + list.get(i);
					}
				}

			} catch (Exception e) {

			}

			ed.putString(Constant.BLOCKED_USERS, blocked);
			ed.commit();

			KachingMeApplication.setBlocked_user(list);

			mngr.updatePrivacyList(KachingMeApplication.getUserID(),
					privacy_items_updated);

			Log.d("Privacy List", "List unblocked..");
			supportInvalidateOptionsMenu();
			// UserBlocked = false;
			// if (tvBlock != null) {
			// if (UserBlocked == true)
			// tvBlock.setText(getResources().getString(R.string.unblock));
			// else
			// tvBlock.setText(getResources().getString(R.string.block));
			// }

		} catch (XMPPException e) {// ACRA.getErrorReporter().handleException(e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoResponseException e) {// ACRA.getErrorReporter().handleException(e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotConnectedException e) {// ACRA.getErrorReporter().handleException(e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// pDialog.hide();\

		users.clear();
		ArrayList<String> blocked = KachingMeApplication.getBlocked_user();
		for (int i = 0; i < blocked.size(); i++) {
			ContactsGetSet contact = dbAdapter.getContact(blocked.get(i));
			users.add(contact);
		}

		UserListAdapter adapter = new UserListAdapter(this,
				R.layout.contactlistitem, users);
		lstview.setAdapter(adapter);

	}

	// public void unblock() {
	//
	//
	// ArrayList temp_block = new ArrayList();
	// temp_block.addAll(KachingMeApplication.getBlocked_user());
	//
	// for (int i = 0; i <temp_block.size(); i++) {
	//
	// try {
	//
	// ArrayList<String> list = new ArrayList<String>();
	//
	// PrivacyListManager mngr = PrivacyListManager
	// .getInstanceFor(connection);
	//
	//
	// List<PrivacyItem> privacy_items = null;
	// try {
	// privacy_items = mngr.getPrivacyList(
	// KachingMeApplication.getUserID()).getItems();
	// } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
	// privacy_items = new ArrayList<PrivacyItem>();
	// // TODO: handle exception
	// }
	// ArrayList<PrivacyItem> privacy_items_updated = new
	// ArrayList<PrivacyItem>();
	// for (PrivacyItem privacyItem : privacy_items) {
	// Log.d("Privacy List", "List::" + privacyItem.getValue());
	//
	// // if (!jid.equals(privacyItem.getValue())) {
	// list.add(privacyItem.getValue());
	// privacy_items_updated.add(privacyItem);
	// // }
	//
	// }
	//
	// String blocked = "";
	//
	// try {
	// for (int i1 = 0; i1 < list.size(); i1++) {
	//
	// if (i1 == 0) {
	// blocked = list.get(i1);
	// } else {
	// blocked += "," + list.get(i1);
	// }
	// }
	//
	// } catch (Exception e) {
	//
	// }
	//
	// ed.putString(Constant.BLOCKED_USERS, blocked);
	// ed.commit();
	//
	// KachingMeApplication.setBlocked_user(list);
	//
	// mngr.updatePrivacyList(KachingMeApplication.getUserID(),
	// privacy_items_updated);
	//
	// Log.d("Privacy List", "List unblocked..");
	// // supportInvalidateOptionsMenu();
	// // UserBlocked = false;
	// // if (tvBlock != null) {
	// // if (UserBlocked == true)
	// // tvBlock.setText(getResources().getString(R.string.unblock));
	// // else
	// // tvBlock.setText(getResources().getString(R.string.block));
	// // }
	//
	// } catch (XMPPException e) {// ACRA.getErrorReporter().handleException(e);
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (NoResponseException e) {//
	// ACRA.getErrorReporter().handleException(e);
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (NotConnectedException e) {//
	// ACRA.getErrorReporter().handleException(e);
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// // pDialog.hide();
	//
	// }
	//
	//
	// }

	public void mUnBlockAll() {

		// TODO Auto-generated method stub
		// AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
		// .getMenuInfo();
		// int menuItemIndex = item.getItemId();
		/*
		 * cursor.moveToPosition(info.position);
		 * Log.d("Context Menu",cursor.getString(8));
		 */

		if (KachingMeApplication.getIsNetAvailable()) {

			Constant.printMsg("sizze "
					+ KachingMeApplication.getBlocked_user().size());

			for (int i = 0; i < KachingMeApplication.getBlocked_user().size(); i++) {

				try {
					ArrayList<String> list = new ArrayList<String>();

					PrivacyListManager mngr = TempConnectionService.privacymanager;

					List<PrivacyItem> privacy_items = null;
					try {
						privacy_items = mngr.getPrivacyList(
								KachingMeApplication.getUserID()).getItems();

					} catch (Exception e) {
						// ACRA.getErrorReporter().handleException(e);
						// TODO: handle exception
					}
					ArrayList<PrivacyItem> privacy_items_updated = new ArrayList<PrivacyItem>();
					for (PrivacyItem privacyItem : privacy_items) {

						// Constant.printMsg(" blocked ::::::: 1  "
						// + users.get(info.position).getJid().toString()
						// + "    " + privacyItem.getValue());
						if (!users.get(i).getJid().toString()
								.equals(privacyItem.getValue())) {
							list.add(privacyItem.getValue());
							privacy_items_updated.add(privacyItem);
							Constant.printMsg(" blocked ::::::: 3  "
									+ privacyItem);
						}

					}
					Constant.printMsg(" blocked ::::::: 2  " + list + "    "
							+ privacy_items_updated);
					if (privacy_items_updated.size() == 0) {
						mngr.deletePrivacyList(KachingMeApplication.getUserID());
					} else {
						mngr.updatePrivacyList(KachingMeApplication.getUserID(),
								privacy_items_updated);
					}

					users.clear();
					for (int i1 = 0; i1 < privacy_items_updated.size(); i1++) {
						ContactsGetSet contact = dbAdapter
								.getContact(privacy_items_updated.get(i1)
										.getValue());
						users.add(contact);
					}

					UserListAdapter adapter = new UserListAdapter(this,
							R.layout.contactlistitem, users);
					lstview.setAdapter(adapter);

					// KachingMeApplication.setBlocked_user(list);
				} catch (Exception e) {
					// ACRA.getErrorReporter().handleException(e);
					e.printStackTrace();
					// TODO: handle exception
				}
			}
		} else {
			new AlertUtils().Toast_call(this,
					getResources().getString(R.string.no_internet_connection));
		}
	}
}
