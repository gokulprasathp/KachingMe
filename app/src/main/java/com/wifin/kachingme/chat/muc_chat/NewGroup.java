package com.wifin.kachingme.chat.muc_chat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;
import org.jivesoftware.smackx.bookmarks.BookmarkManager;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.xdata.Form;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.applications.SherlockBaseActivity;
import com.wifin.kachingme.database.DatabaseHelper;
import com.wifin.kachingme.pojo.ContactsGetSet;
import com.wifin.kachingme.pojo.MessageGetSet;
import com.wifin.kachingme.services.TempConnectionService;
import com.wifin.kachingme.util.AlertManager;
import com.wifin.kachingme.util.AlertUtils;
import com.wifin.kachingme.util.AvatarManager;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.Log;
import com.wifin.kachingme.util.RoundImage;
import com.wifin.kachingme.util.Utils;
import com.wifin.kachingme.util.MultiselectMembers;

public class NewGroup extends SherlockBaseActivity {

	AutoCompleteTextView txt_auto;
	ListView list;
	TextView txt_members;

	DatabaseHelper dbAdapter;
	ArrayList<ContactsGetSet> users;
	ArrayList<ContactsGetSet> selected_users;
	UserListAdapter select_adapter;

	Handler h;
	Thread refresh;
	MultiUserChat muc;

	byte[] avatar = null;
	String room_subject, grp_id;

	ProgressDialog progressdialog;
	ImageButton btn_multi_select;
	TextView txt_subject;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newgroup_list);

		/*
		 * Bundle bundle=getIntent().getExtras(); if(bundle!=null) {
		 * room_subject=bundle.getString("subject");
		 * avatar=bundle.getByteArray("avatar"); }
		 */
		txt_auto = (AutoCompleteTextView) findViewById(R.id.txt_autocomplete);
		txt_members = (TextView) findViewById(R.id.txt_members);
		list = (ListView) findViewById(R.id.list_experience);
		btn_multi_select = (ImageButton) findViewById(R.id.btn_multi_select);
		txt_subject = (TextView) findViewById(R.id.txt_grp_name);
        try {
            Constant.typeFace(this,txt_auto);
            Constant.typeFace(this,txt_members);
            Constant.typeFace(this,txt_subject);
        } catch (Exception e) {
            e.printStackTrace();
        }

        dbAdapter = KachingMeApplication.getDatabaseAdapter();
		users = new ArrayList<ContactsGetSet>();
		selected_users = new ArrayList<ContactsGetSet>();
		users = dbAdapter.getContacts();
		AutoAdapter adapter = new AutoAdapter(this, R.layout.contactlistitem,
				users);
		select_adapter = new UserListAdapter(this, R.layout.add_grm_mem_item,
				selected_users);
		list.setAdapter(select_adapter);
		txt_auto.setAdapter(adapter);

		View view = new View(this);
		list.addFooterView(view);

		btn_multi_select.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent in = new Intent(NewGroup.this, MultiselectMembers.class);
				startActivityForResult(in, 12);
			}
		});

		txt_auto.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				if (selected_users.size() < 200) {
					boolean is_selected = false;
					for (int j = 0; j < selected_users.size(); j++) {

						if (selected_users.get(j).getJid()
								.equals(users.get(position).getJid())) {
							is_selected = true;
							// Log.d("multi_select","Is Selected true");
						}
					}

					if (is_selected == false) {
						for (int j = 0; j < users.size(); j++) {

							if (users.get(j).getJid()
									.equals(users.get(position))) {
								selected_users.add(users.get(j));
								select_adapter.notifyDataSetChanged();
							}
						}
					}

					selected_users.add(users.get(position));
					select_adapter.notifyDataSetChanged();
					txt_auto.setText("");
					txt_members.setText(selected_users.size()
							+ " "
							+ getResources().getString(
									R.string.out_of_200_members));
				} else {
					new AlertManager().showAlertDialog(
							NewGroup.this,
							getResources().getString(
									R.string.only_200_members_are_allowed),
							true);
				}
			}
		});
		txt_members.setText(getResources().getString(
				R.string.out_of_200_members));
	}

	// private ServiceConnection mConnection = new ServiceConnection() {
	// public void onServiceConnected(ComponentName className, IBinder service)
	// {
	//
	// mBoundService = ((KaChingMeService.LocalBinder) service)
	// .getService();
	// connection = mBoundService.getConnection();
	//
	// }
	//
	// public void onServiceDisconnected(ComponentName className) {
	//
	// mBoundService = null;
	// }
	// };
	//
	// void doBindService() {
	//
	// bindService(new Intent(NewGroup.this, KaChingMeService.class),
	// mConnection, Context.BIND_AUTO_CREATE);
	// isBound = true;
	// }
	//
	// void doUnbindService() {
	// if (isBound) {
	//
	// unbindService(mConnection);
	// isBound = false;
	// }
	// }

	@Override
	protected void onDestroy() {
		// doUnbindService();
		super.onDestroy();
	}

	@Override
	protected void onStart() {
		// doBindService();
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(getResources().getString(R.string.create))

		.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (txt_subject.getText().toString().equals("")) {
			new AlertManager().showAlertDialog(this,
					getResources()
							.getString(R.string.please_enter_room_subject),
					true);
		} else if (selected_users.size() == 0) {
			new AlertManager().showAlertDialog(
					this,
					getResources().getString(
							R.string.please_select_room_members), true);
		} else if (KachingMeApplication.getIsNetAvailable()) {
			new CraeteRoom().execute();
		} else {
			new AlertUtils().Toast_call(this,
					getResources().getString(R.string.no_internet_connection));
		}
		return super.onOptionsItemSelected(item);
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
            TextView name = (TextView) container.findViewById(R.id.name);
            ImageView img = (ImageView) container.findViewById(R.id.avtarimg);
            ImageView img_remove = (ImageView) container.findViewById(R.id.img_remove);
            LinearLayout grmLayout = (LinearLayout) container.findViewById(R.id.add_grmLayout);

            int width = Constant.screenWidth;
            int height = Constant.screenHeight;

            LinearLayout.LayoutParams addGrmLayout = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            addGrmLayout.width = width ;
            addGrmLayout.height = (int) (height * 9 / 100);
            addGrmLayout.gravity= Gravity.CENTER;
            grmLayout.setLayoutParams(addGrmLayout);
            grmLayout.setGravity(Gravity.CENTER | Gravity.LEFT);

            FrameLayout.LayoutParams circularImage = new FrameLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            circularImage.width = width * 15 / 100;
            circularImage.height = height * 8 / 100;
            circularImage.setMargins(width * 5 / 100,height*1/2*100,width * 1 / 100,height*1/2*100);
            circularImage.gravity = Gravity.CENTER|Gravity.LEFT;
            img.setLayoutParams(circularImage);

            LinearLayout.LayoutParams chatMsgParama = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            chatMsgParama.width = width * 63 / 100;
            chatMsgParama.height = (int) (height * 9 / 100);
            name.setLayoutParams(chatMsgParama);
            name.setGravity(Gravity.CENTER | Gravity.LEFT);
            name.setPadding(width*3/100, 0, 0, 0);
            name.setGravity(Gravity.CENTER|Gravity.LEFT);

            LinearLayout.LayoutParams removeParama = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            removeParama.width = width * 15 / 100;
            removeParama.height = height * 7 / 100;
            removeParama.setMargins(width * 1 / 100,height * 1 / 100,width * 1 / 100,height * 1 / 100);
            removeParama.gravity = Gravity.CENTER|Gravity.LEFT;
            img_remove.setLayoutParams(removeParama);

            if (width >= 600) {
                name.setTextSize(19);
            } else if (width > 501 && width < 600) {
                name.setTextSize(17);
            } else if (width > 260 && width < 500) {
                name.setTextSize(15);
            } else if (width <= 260) {
                name.setTextSize(13);
            }
			// set name


			name.setText(user.getDisplay_name());

//			try {
//
//				Bitmap bmp = BitmapFactory.decodeByteArray(user.getPhoto_ts(),
//						0, user.getPhoto_ts().length);
//				img.setImageBitmap(new AvatarManager().roundCornerImage(bmp,
//						180));
//			} catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
//				// TODO: handle exception
//			}

            Bitmap bmp = null;

            System.gc();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inDither = true;
            options.inSampleSize = 2;
            try {
                bmp = BitmapFactory.decodeByteArray(user.getPhoto_ts(),
                        0, user.getPhoto_ts().length,options);
                RoundImage mSenderImage = new RoundImage(bmp);
                img.setImageDrawable(mSenderImage);

            } catch (OutOfMemoryError e) {
                android.util.Log.e("Map", "Tile Loader (241) Out Of Memory Error " + e.getLocalizedMessage());
                System.gc();
            }catch (Exception e){

            }


            img_remove.setTag(position);
			img_remove.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					int position = Integer.parseInt(v.getTag().toString());

					selected_users.remove(position);
					select_adapter.notifyDataSetChanged();

					txt_members.setText(selected_users.size()
							+ " "
							+ getResources().getString(
									R.string.out_of_200_members));
				}
			});

			return container;
		}

	}

	private class AutoAdapter extends ArrayAdapter<ContactsGetSet> {
		private ArrayList<ContactsGetSet> items;
		private ArrayList<ContactsGetSet> itemsAll;
		private ArrayList<ContactsGetSet> suggestions;
		private int viewResourceId;

		@SuppressWarnings("unchecked")
		public AutoAdapter(Context context, int viewResourceId,
				ArrayList<ContactsGetSet> items) {
			super(context, viewResourceId, items);
			this.items = items;
			this.itemsAll = (ArrayList<ContactsGetSet>) items.clone();
			this.suggestions = new ArrayList<ContactsGetSet>();
			this.viewResourceId = viewResourceId;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(viewResourceId, null);
			}
			ContactsGetSet product = items.get(position);
			if (product != null) {
				TextView productLabel = (TextView) v.findViewById(R.id.name);
				if (productLabel != null) {
					productLabel.setText(product.getDisplay_name());
				}

				try {
					ImageView img = (ImageView) v.findViewById(R.id.avtarimg);
					Bitmap bmp = BitmapFactory.decodeByteArray(
							product.getPhoto_ts(), 0,
							product.getPhoto_ts().length);
					img.setImageBitmap(new AvatarManager().roundCornerImage(
							bmp, 180));
				} catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
					// TODO: handle exception
				}
				// set status
				TextView status = (TextView) v.findViewById(R.id.status);

				try {
					status.setText(product.getStatus());
				} catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
					// TODO: handle exceptionas
					e.printStackTrace();
				}
			}
			return v;
		}

		@Override
		public Filter getFilter() {
			return nameFilter;
		}

		Filter nameFilter = new Filter() {
			public String convertResultToString(Object resultValue) {
				String str = ((ContactsGetSet) (resultValue)).getDisplay_name();
				return str;
			}

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				if (constraint != null) {
					suggestions.clear();
					for (ContactsGetSet product : itemsAll) {
						if (product
								.getDisplay_name()
								.toLowerCase()
								.startsWith(constraint.toString().toLowerCase())) {
							suggestions.add(product);
						}
					}
					FilterResults filterResults = new FilterResults();
					filterResults.values = suggestions;
					filterResults.count = suggestions.size();
					return filterResults;
				} else {
					return new FilterResults();
				}
			}

			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				@SuppressWarnings("unchecked")
				ArrayList<ContactsGetSet> filteredList = (ArrayList<ContactsGetSet>) results.values;
				if (results != null && results.count > 0) {
					clear();
					for (ContactsGetSet c : filteredList) {
						add(c);
					}
					notifyDataSetChanged();
				}
			}
		};

	}

	private class CraeteRoom extends AsyncTask<String, String, String> {

		String mem_list;

		@Override
		protected void onPreExecute() {
			progressdialog = new ProgressDialog(NewGroup.this);
			progressdialog.setMessage(getResources()
					.getString(R.string.loading));
			progressdialog.show();

			room_subject = txt_subject.getText().toString();
			super.onPreExecute();

		}

		@Override
		protected String doInBackground(String... params) {
			Date timestamp = new Date();
			grp_id = KachingMeApplication.getUserID() + "-" + timestamp.getTime()
					+ "@conference." + KachingMeApplication.getHost_12();

			muc = TempConnectionService.MUC_MANAGER.getMultiUserChat(grp_id);

			// muc=new
			// MultiMUC(connection,txt_grp_name.getText()+"_"+KachingMeApplication.getUserID()+"@conference.pc");

			// Constant.printMsg("BM Group ID::"+grp_id+" Subject::"+txt_subject.getText().toString());

			try {
				// muc.join("user");
				// muc2.join("user");
				muc.create(txt_subject.getText().toString());

				Form form = muc.getConfigurationForm();
				Form submitForm = form.createAnswerForm();
				submitForm.setAnswer("muc#roomconfig_roomname", txt_subject
						.getText().toString());
				submitForm.setAnswer("muc#roomconfig_persistentroom", true);
				submitForm.setAnswer("muc#roomconfig_membersonly", true);
				submitForm.setAnswer("muc#roomconfig_allowinvites", true);
				submitForm.setAnswer("muc#roomconfig_moderatedroom", true);
				submitForm.setAnswer("muc#roomconfig_changesubject", true);
				// submitForm.setAnswer("muc#roomconfig_maxusers","200");
				submitForm.setAnswer("allow_query_users", true);
				submitForm.setAnswer("public_list", true);
				List<String> admin = new ArrayList<String>();
				admin.add(KachingMeApplication.getUserID()
						+ KachingMeApplication.getHost());
				/*
				 * submitForm.setAnswer("muc#roomconfig_captcha_whitelist",admin)
				 * ;
				 */
				submitForm.setAnswer(
						"muc#roomconfig_roomdesc",
						KachingMeApplication.getUserID()
								+ KachingMeApplication.getHost());

				muc.sendConfigurationForm(submitForm);
				// muc.addMessageListener(listener);

				/*
				 * Form f1=muc.getConfigurationForm();
				 * 
				 * for (Iterator fields = f1.getFields(); fields.hasNext();) {
				 * FormField field = (FormField) fields.next();
				 * 
				 * // Sets the default value as the answer //
				 * submitForm.setDefaultAnswer(field.getVariable());
				 * 
				 * String s = "";
				 * 
				 * Iterator<String> am=field.getValues();
				 * 
				 * while (am.hasNext()) { s+=","+am.next();
				 * 
				 * }
				 * 
				 * //Constant.printMsg("Form Field::"+field.getLabel()+"::"+field
				 * .getVariable()+"::"+s); }
				 */

				// muc.grantOwnership("temp@pc");
				muc.join(KachingMeApplication.getUserID()
						+ KachingMeApplication.getHost());
				// muc.changeSubject(room_subject);

				BookmarkManager bm = BookmarkManager
						.getBookmarkManager(TempConnectionService.connection);
				bm.addBookmarkedConference(txt_subject.getText().toString(),
						grp_id, true, "", "");

			} catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
				e.printStackTrace();
			}

			try {
				if (muc.isJoined()) {
					Log.d("Room",
							"Selected Member size::" + selected_users.size());

					for (int j = 0; j < selected_users.size(); j++) {

						if (j == 0) {
							mem_list = selected_users.get(j).getJid();
						} else {
							mem_list = mem_list + ","
									+ selected_users.get(j).getJid();
						}

						MessageGetSet msg = new MessageGetSet();
						msg.setData("");
						msg.setKey_from_me(0);
						msg.setKey_id("" + new Date().getTime());
						msg.setKey_remote_jid(grp_id);
						msg.setNeeds_push(0);
						msg.setStatus(0);
						msg.setTimestamp(new Date().getTime());
						msg.setRemote_resource(selected_users.get(j).getJid());
						msg.setMedia_wa_type("" + 7);
						dbAdapter.setInsertMessages(msg);

						muc.grantOwnership(selected_users.get(j).getJid()
								.toString());
					}

					for (int i = 0; i < selected_users.size(); i++) {

						try {

							muc.invite(selected_users.get(i).getJid(), "");
							Log.d("MUC Create", "Invited Memebrs::"
									+ selected_users.get(i).getJid());
						} catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
							e.printStackTrace();
							// TODO: handle exception
						}

					}

				}

			} catch (Exception e) {

				e.printStackTrace();

			}

			try {
				Message msg = new Message(grp_id, Type.groupchat);
				msg.setSubject(txt_subject.getText().toString());
				muc.sendMessage(msg);
			} catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
				// TODO: handle exception
			}

			SharedPreferences sp = getSharedPreferences(
					KachingMeApplication.getPereference_label(),
					Activity.MODE_PRIVATE);
			SharedPreferences.Editor editor = sp.edit();
			editor.putString(grp_id,
					mem_list + "," + KachingMeApplication.getUserID()
							+ KachingMeApplication.getHost());
			editor.putString(grp_id + "_admin", KachingMeApplication.getUserID()
					+ KachingMeApplication.getHost());
			editor.commit();

			// mBoundService.setMUC_Listeners(muc);

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			DatabaseHelper dbadapter = KachingMeApplication.getDatabaseAdapter();
			int msg_id = dbadapter.getLastMsgid_chat_grp(grp_id);
			try {

				MessageGetSet msg = new MessageGetSet();
				msg.setData("");
				msg.setKey_from_me(0);
				msg.setKey_id("" + new Date().getTime());
				msg.setKey_remote_jid(grp_id);
				msg.setMedia_wa_type("10");
				msg.setNeeds_push(0);
				msg.setStatus(0);

				msg.setTimestamp(new Date().getTime());
				msg.setRemote_resource(KachingMeApplication.getUserID()
						+ KachingMeApplication.getHost());

				dbadapter.setInsertMessages(msg);

				if (!dbadapter.isjidExist(grp_id)) {
					ContactsGetSet contact = new ContactsGetSet();
					contact.setIs_niftychat_user(1);
					contact.setJid(grp_id);
					contact.setPhone_label(txt_subject.getText().toString());
					contact.setDisplay_name(txt_subject.getText().toString());
					contact.setPhoto_ts(new Utils()
							.getGroupRandomeIcon(NewGroup.this));
					dbadapter.insertContacts(contact);
					dbadapter.setInsertChat_list(grp_id, msg_id);
				}
			} catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
				// TODO: handle exception
			}

			Intent login_broadcast = new Intent("chat");
			login_broadcast.putExtra("jid", "" + grp_id);
			getApplicationContext().sendBroadcast(login_broadcast);

			progressdialog.cancel();
			/*
			 * Intent intent=new Intent(NewGroup.this,Chatlist.class);
			 * intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			 * startActivity(intent); finish();
			 */
			onBackPressed();
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 12) {

			if (data != null) {
				users.clear();
				users = dbAdapter.getContacts();
				String jid = data.getStringExtra("jid");

				Log.d("NewwGroup", "Selected user::" + jid
						+ " selected user size::" + selected_users.size());

				String selected[] = jid.split(",");

				for (int i = 0; i < selected.length; i++) {
					boolean is_selected = false;
					if (selected_users.size() > 0) {
						for (int j = 0; j < selected_users.size(); j++) {

							if (selected_users.get(j).getJid()
									.equals(selected[i])) {
								is_selected = true;
							}
						}
						Log.d("NewGrop", "Is selectd::" + is_selected);
						if (is_selected == false) {
							for (int k = 0; k < users.size(); k++) {

								Log.d("NewGroup_List", users.get(k).getJid()
										+ "::" + selected[i]);

								if (users.get(k).getJid().equals(selected[i])) {
									Log.d("NewGroup", "User inserted !!!!");
									selected_users.add(users.get(k));
									select_adapter = new UserListAdapter(this,
											R.layout.add_grm_mem_item,
											selected_users);
									list.setAdapter(select_adapter);
									select_adapter.notifyDataSetChanged();
								}
							}
						}
					} else {
						for (int j = 0; j < users.size(); j++) {

							if (users.get(j).getJid().equals(selected[i])) {
								selected_users.add(users.get(j));
								select_adapter.notifyDataSetChanged();
								select_adapter = new UserListAdapter(this,
										R.layout.add_grm_mem_item,
										selected_users);
								list.setAdapter(select_adapter);
								select_adapter.notifyDataSetChanged();
							}
						}
					}
				}
				txt_members
						.setText(selected_users.size()
								+ " "
								+ getResources().getString(
										R.string.out_of_200_members));

				/*
				 * if(selected_users.size()<200) {
				 * selected_users.add(users.get(position));
				 * select_adapter.notifyDataSetChanged(); txt_auto.setText("");
				 * txt_members.setText(selected_users.size()+" of 200 Members");
				 * } else { new
				 * AlertManager().showAlertDialog(NewGroup_List.this
				 * ,"Only 200 Members are allowed on room.", true); }
				 */
			}

		}
	}

}
