package com.wifin.kachingme.settings;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.http.Header;
//import cz.msebera.android.httpclient.Header;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;
import org.jivesoftware.smackx.bookmarks.BookmarkManager;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jivesoftware.smackx.jiveproperties.JivePropertiesManager;
import org.jivesoftware.smackx.muc.Affiliate;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jivesoftware.smackx.xdata.Form;
import org.jivesoftware.smackx.xdata.FormField;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.database.DatabaseHelper;
import com.wifin.kachingme.database.Dbhelper;
import com.wifin.kachingme.pojo.Chat_list_GetSet;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.HttpConfig;
import com.wifin.kachingme.util.KachingMeConfig;
import com.wifin.kachingme.services.TempConnectionService;
import com.wifin.kachingme.util.Log;
import com.wifin.kachingme.util.Select_MUC_Admin;
import com.loopj.android.http.*;
//import com.loopj.android.http.AsyncHttpResponseHandler;
//import com.loopj.android.http.RequestParams;
import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.registration_and_login.Slideshow;

public class Delete_Account_Confirm extends ActionBarActivity implements
		OnClickListener {

	Button btn_yes, btn_no;

	DatabaseHelper dbAdapter;

	SharedPreferences sp, sp1;
	SharedPreferences.Editor editor;
	String TAG = Delete_Account_Confirm.class.getName();
	MultiUserChat muc;
	Cursor cursor;
	ArrayList<String> ar_jid = new ArrayList<String>();
	ArrayList<String> mem_jid = new ArrayList<String>();
	ArrayAdapter<String> ar_name;
	int selected_position;
	ProgressDialog progressdialog;
	String db_data;
	DatabaseHelper dbadapter = KachingMeApplication.getDatabaseAdapter();
	Dbhelper db;

	// Editor ed;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.delete_account_confirm);
		btn_yes = (Button) findViewById(R.id.btn_yes);
		btn_no = (Button) findViewById(R.id.btn_no);
        Constant.typeFace(this,btn_yes);
        Constant.typeFace(this,btn_no);

		btn_yes.setOnClickListener(this);
		btn_no.setOnClickListener(this);

		dbadapter = KachingMeApplication.getDatabaseAdapter();
		db = new Dbhelper(getApplicationContext());
		sp1 = PreferenceManager.getDefaultSharedPreferences(this);

		sp = getSharedPreferences(KachingMeApplication.getPereference_label(),
				Activity.MODE_PRIVATE);

		editor = sp.edit();
		dbAdapter = KachingMeApplication.getDatabaseAdapter();
		progressdialog = new ProgressDialog(this);
		progressdialog.setMessage(getResources()
				.getString(R.string.please_wait));

	}

	public void get_Group_List() {
		ar_jid.clear();
		ar_name = new ArrayAdapter<String>(Delete_Account_Confirm.this,
				android.R.layout.select_dialog_singlechoice);
		cursor = dbAdapter.getMUCChat_list();
		if (cursor.moveToFirst()) {
			do {
				Log.d(TAG, "Rooms::" + cursor.getString(8));
				String admin = sp.getString(cursor.getString(8) + "_admin", "");

				if (admin.equals(KachingMeApplication.getUserID()
						+ KachingMeApplication.getHost())
						&& (sp.getString(cursor.getString(8), "").toString()
								.split(",").length > 1)) {
					Log.d(TAG, "Room admin::" + cursor.getString(8));
					ar_jid.add(cursor.getString(8));
					ar_name.add(cursor.getString(1));
				} else {
					mem_jid.add(cursor.getString(8));
				}

			} while (cursor.moveToNext());
		}

		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}

		new left_group().execute();
	}

	public void Asign_Group_Admin() {
		AlertDialog.Builder builderSingle = new AlertDialog.Builder(
				Delete_Account_Confirm.this);
		builderSingle.setIcon(R.drawable.ic_launcher);
		builderSingle.setTitle(getResources().getString(R.string.select_admin));

		builderSingle.setNegativeButton(
				getResources().getString(R.string.cancel),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

		builderSingle.setAdapter(ar_name,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						String strName = ar_name.getItem(which);
						selected_position = which;
						String[] mm = sp.getString(ar_jid.get(which), "")
								.toString().split(",");
						String mm_list = "";
						int i = 0;
						for (String string : mm) {
							if (i == 0) {
								mm_list = "'" + string + "'";
							} else {
								mm_list = mm_list + ",'" + string + "'";
							}

							i++;
						}

						Intent intente = new Intent(
								Delete_Account_Confirm.this,
								Select_MUC_Admin.class);
						intente.putExtra("jids", ar_jid.get(which));
						startActivityForResult(intente, 2);

					}
				});
		builderSingle.show();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();

		switch (id) {
		case R.id.btn_yes:

			try {

				get_Group_List();

			} catch (Exception e) {

				Constant.printMsg("GGGGGGGGGGGGG" + e.toString());
				// TODO: handle exception
			}

			// new deleteAccount().execute();

			// if(ar_name.getCount()>0)
			// {
			// AlertDialog.Builder builder = new AlertDialog.Builder(this);
			//
			// builder.setTitle(getResources().getString(R.string.Alert));
			// builder.setMessage(getResources().getString(R.string.you_must_assign_new_admin));
			//
			// builder.setPositiveButton(getResources().getString(R.string.Ok),
			// new DialogInterface.OnClickListener() {
			//
			// public void onClick(DialogInterface dialog, int which) {
			// // Do nothing but close the dialog
			// // new delete_Async().execute();
			// Asign_Group_Admin();
			// dialog.dismiss();
			// }
			//
			// });
			//
			// builder.setNegativeButton(getResources().getString(R.string.cancel),
			// new DialogInterface.OnClickListener() {
			//
			// @Override
			// public void onClick(DialogInterface dialog, int which) {
			// // Do nothing
			// dialog.dismiss();
			// }
			// });
			//
			// AlertDialog alert = builder.create();
			// alert.show();
			//
			//
			// }
			// else
			// {
			// AlertDialog.Builder builder = new AlertDialog.Builder(this);
			//
			// builder.setTitle(getResources().getString(R.string.confirm));
			// builder.setMessage(getResources().getString(R.string.are_you_sure));
			//
			// builder.setPositiveButton(getResources().getString(R.string.yes),
			// new DialogInterface.OnClickListener() {
			//
			// public void onClick(DialogInterface dialog, int which) {
			// // Do nothing but close the dialog
			// new left_group().execute();
			//
			// dialog.dismiss();
			// }
			//
			// });
			//
			// builder.setNegativeButton(getResources().getString(R.string.No),
			// new DialogInterface.OnClickListener() {
			//
			// @Override
			// public void onClick(DialogInterface dialog, int which) {
			// // Do nothing
			// dialog.dismiss();
			// }
			// });
			//
			// AlertDialog alert = builder.create();
			// alert.show();
			// }
			break;
		case R.id.btn_no:
			finish();
			break;
		default:
			break;
		}

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
	// bindService(new Intent(this, KaChingMeService.class), mConnection,
	// Context.BIND_AUTO_CREATE);
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
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// doBindService();
	}

	private class left_group extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressdialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {

			MultiUserChatManager muc_manager = TempConnectionService.MUC_MANAGER;

			for (int j = 0; j < mem_jid.size(); j++) {
				muc = muc_manager.getMultiUserChat(mem_jid.get(j));
				try {
					muc.join(mem_jid.get(j));
				} catch (Exception e) {
					// ACRA.getErrorReporter().handleException(e);
					e.printStackTrace();
					// TODO: handle exception
				}

				try {

					muc.revokeOwnership(KachingMeApplication.getjid());
					String mem_list = null;
					Collection<Affiliate> owner = muc.getOwners();

					int i = 0;
					for (Affiliate affiliate : owner) {
						if (i == 0) {
							mem_list = affiliate.getJid();
						} else {
							mem_list = mem_list + "," + affiliate.getJid();
						}
						i++;
						Log.d("MUC_info", "Owner::" + affiliate.getJid());
					}

					Message msg = new Message(mem_jid.get(j), Type.groupchat);

					// msg.setSubject("Remove");
					msg.setBody(mem_list);

					JivePropertiesManager.addProperty(msg, "ID", 2);
					JivePropertiesManager.addProperty(msg, "Removed_member",
							KachingMeApplication.getjid());
					msg.setPacketID(Constant.MEMBERREMOVEMESSAGE
							+ new Date().getTime());
					JivePropertiesManager
							.addProperty(msg, "media_wa_type", "0");
					muc.sendMessage(msg);

					// Delete_Local();

				} catch (Exception e) {
					// ACRA.getErrorReporter().handleException(e);
					e.printStackTrace();
					// TODO: handle exception
				}

			}

			return null;
		}

		@Override
		protected void onPostExecute(Void v) {
			// TODO Auto-generated method stub

			new delete_Async().execute();

		}
	}

	private class delete_Async extends AsyncTask<String, String, String> {
		boolean deleted = false;
		boolean deleted1 = false;

		@Override
		protected String doInBackground(String... params) {
			try {

				Log.d(TAG, "Before account delete");
				dbAdapter.close();
				AccountManager account = AccountManager
						.getInstance(TempConnectionService.connection);
				account.deleteAccount();

				File file = new File("/data/data/"
						+ getApplicationContext().getPackageName()
						+ "/databases/kachingme.db");
				deleted = file.delete();

				File file_1 = new File("/data/data/"
						+ getApplicationContext().getPackageName()
						+ "/shared_prefs/niftychat_pereferences.xml");
				deleted1 = file_1.delete();

				Log.d(TAG, "IS DELETE DB::" + deleted + " IS DELETE SP::"
						+ deleted1);

			} catch (Exception e) {
				// ACRA.getErrorReporter().handleException(e);
				e.printStackTrace();
				// TODO: handle exception
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (deleted && deleted1) {
				Log.d(TAG, "Before account delete .... after");

				AsyncHttpClient client = new AsyncHttpClient();
				RequestParams request_params = new RequestParams();
				request_params.add("jid", KachingMeApplication.getjid());
				client.post(KachingMeConfig.DELETE_USER_PHP,
						request_params, new AsyncHttpResponseHandler() {
							@Override
							public void onFinish() {
								// TODO Auto-generated method stub
								Log.d("Remove_user", "User_Delete_Finish");
								editor.remove("pin");
								editor.commit();
								finish();
								super.onFinish();
							}

							@Override
							public void onStart() {
								// TODO Auto-generated method stub
								Log.d("Remove_user", "User_Delete_Sart");
								super.onStart();
							}

							@Override
							public void onFailure(int arg0, Header[] arg1,
									byte[] arg2, Throwable arg3) {
								// TODO Auto-generated method stub
								Log.d("Remove_user", "User_Delete_Failure::"
										+ new String(arg2));
							}

							@Override
							public void onSuccess(int arg0, Header[] arg1,
									byte[] arg2) {
								// TODO Auto-generated method stub
								Log.d("Remove_user", "User_Delete::"
										+ new String(arg2));
								editor.remove("pin");
								editor.commit();
								finish();
							}

						});

			}
			// System.exit(0);
			new deleteAccount().execute();

		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

	}

	/*
	 * public void Admin_exit(String new_admin,String jid) { try { muc=new
	 * MultiUserChat(connection, jid); try { muc.join(jid); }catch (Exception e)
	 * { e.printStackTrace(); // TODO: handle exception }
	 * 
	 * Form f1=muc.getConfigurationForm(); List<String> admin=new
	 * ArrayList<String>(); admin.add(new_admin); Form submitForm =
	 * f1.createAnswerForm();
	 * 
	 * submitForm.setAnswer("muc#roomconfig_roomdesc",admin);
	 * muc.sendConfigurationForm(submitForm);
	 * 
	 * try {
	 * 
	 * muc.revokeOwnership(KachingMeApplication.getUserID()+KachingMeApplication.getHost(
	 * ));
	 * 
	 * muc.sendConfigurationForm(f1); String mem_list = null;
	 * Collection<Affiliate>owner=muc.getOwners();
	 * 
	 * 
	 * int i=0; for (Affiliate affiliate : owner) { if(i==0) {
	 * mem_list=affiliate.getJid(); }else {
	 * mem_list=mem_list+","+affiliate.getJid(); } i++;
	 * Log.d("MUC_info","Owner::"+affiliate.getJid()); }
	 * 
	 * Message msg=new Message(jid,Type.groupchat); //msg.setSubject("Destroy");
	 * 
	 * msg.setBody(mem_list); msg.setProperty("ID",3);
	 * msg.setProperty("New_admin",new_admin);
	 * msg.setProperty("Left_Member",KachingMeApplication
	 * .getUserID()+KachingMeApplication.getHost()); msg.setPacketID(""+new
	 * Date().getTime()); msg.setProperty("media_wa_type","0");
	 * muc.sendMessage(msg);
	 * 
	 * 
	 * editor.remove(jid+"_admin"); editor.remove(jid); editor.commit();
	 * get_Group_List();
	 * 
	 * if(ar_jid.size()>0) { Asign_Group_Admin(); } // Delete_Local();
	 * 
	 * }catch (Exception e) { e.printStackTrace(); // TODO: handle exception }
	 * 
	 * 
	 * }catch (Exception e) { e.printStackTrace(); } }
	 */

	public void Admin_exit(String new_admin, String jid) {
		try {
			MultiUserChatManager muc_manager = TempConnectionService.MUC_MANAGER;
			muc = muc_manager.getMultiUserChat(jid);
			try {
				muc.join(jid);
			} catch (Exception e) {
				// ACRA.getErrorReporter().handleException(e);
				e.printStackTrace();
				// TODO: handle exception
			}

			dbAdapter = KachingMeApplication.getDatabaseAdapter();

			Chat_list_GetSet chat_list = dbAdapter.getChat_List(muc.getRoom());
			Form f1 = muc.getConfigurationForm();
			List<String> admin = new ArrayList<String>();
			admin.add(new_admin);
			Form submitForm = f1.createAnswerForm();
			JSONObject main_job = new JSONObject();

			for (Iterator fields = f1.getFields().iterator(); fields.hasNext();) {
				FormField field = (FormField) fields.next();

				String s = "";

				Iterator<String> am = field.getValues().iterator();

				while (am.hasNext()) {
					s = am.next();

				}

				if (field.getVariable().equals("muc#roomconfig_roomdesc")) {

					JSONObject json = new JSONObject(s);
					JSONObject j_obj = json.getJSONObject("data");
					j_obj.put(Constant.GROUP_ADMIN, new_admin);
					j_obj.put(Constant.GROUP_TYPE,
							sp.getString(muc.getRoom() + "_group_type", "none"));
					j_obj.put(Constant.GROUP_TOPIC, sp.getString(muc.getRoom()
							+ "_group_question", "none"));
					j_obj.put(
							Constant.TOPIC_OPTION,
							sp.getString(muc.getRoom()
									+ "_group_question_options", "none"));
					j_obj.put(Constant.TIMESTAMP,
							"" + chat_list.getTimestamp());
					main_job.put("data", j_obj);
					// Log.d("Muc_invitation", "Room Admin::" + room_admin);
				}
				// //Constant.printMsg("Form Field::"+field.getLabel()+"::"+field.getVariable()+"::"+s);
			}

			Log.d(TAG, "New Room Configuration::" + main_job.toString());
			submitForm
					.setAnswer("muc#roomconfig_roomdesc", main_job.toString());
			muc.sendConfigurationForm(submitForm);

			try {

				muc.revokeOwnership(KachingMeApplication.getUserID()
						+ KachingMeApplication.getHost());

				/* muc.sendConfigurationForm(f1); */
				String mem_list = null;
				Collection<Affiliate> owner = muc.getOwners();

				int i = 0;
				for (Affiliate affiliate : owner) {
					if (i == 0) {
						mem_list = affiliate.getJid();
					} else {
						mem_list = mem_list + "," + affiliate.getJid();
					}
					i++;
					Log.d("MUC_info", "Owner::" + affiliate.getJid());
				}

				Message msg = new Message(muc.getRoom(), Type.groupchat);
				// msg.setSubject(muc.getSubject());

				msg.setBody(mem_list);
				/*
				 * msg.setProperty("ID",3);
				 * msg.setProperty("New_admin",new_admin);
				 * msg.setProperty("Left_Member"
				 * ,KachingMeApplication.getUserID()+KachingMeApplication.getHost());
				 * msg.setPacketID(""+new Date().getTime());
				 */
				JivePropertiesManager.addProperty(msg, "ID", 2);
				JivePropertiesManager.addProperty(msg, "Removed_member",
						KachingMeApplication.getjid());
				msg.setPacketID(Constant.MEMBERREMOVEMESSAGE
						+ new Date().getTime());
				JivePropertiesManager.addProperty(msg, "media_wa_type", "0");
				muc.sendMessage(msg);

				editor.remove(jid + "_admin");
				editor.remove(jid);
				editor.commit();

				Delete_Local(jid);

				get_Group_List();

				if (ar_jid.size() > 0) {
					Asign_Group_Admin();
				}

			} catch (Exception e) {
				// ACRA.getErrorReporter().handleException(e);
				e.printStackTrace();
				// TODO: handle exception
			}

		} catch (Exception e) {
			// ACRA.getErrorReporter().handleException(e);
			e.printStackTrace();
		}
	}

	public void Delete_Local(String jid) {
		editor.remove(jid);
		editor.remove(jid + "_admin");

		editor.commit();

		dbAdapter.setDeleteContact(jid);
		dbAdapter.setDeleteMessages(jid);
		dbAdapter.setDeleteChatList(jid);
		try {
			BookmarkManager bm = BookmarkManager
					.getBookmarkManager(TempConnectionService.connection);
			bm.removeBookmarkedConference(muc.getRoom());
		} catch (Exception e) {
			// ACRA.getErrorReporter().handleException(e);
			// TODO: handle exception
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 2) {
			if (resultCode == 2) {
				if (data != null) {
					String jid = data.getStringExtra("jid");
					Log.d("Activity Result", "Selected Admin jid::" + jid);
					if (jid != null && jid != "") {
						Admin_exit(jid, ar_jid.get(selected_position));
					}
					// Add_Meber(jid);
				}
			}
		}

	}

	/*
	 * public void Delete_Local(String jid) { editor.remove(jid);
	 * editor.remove(jid+"_admin");
	 * 
	 * editor.commit();
	 * 
	 * dbAdapter.setDeleteContact(jid); dbAdapter.setDeleteMessages(jid);
	 * dbAdapter.setDeleteChatList(jid); try { BookmarkManager
	 * bm=BookmarkManager.getBookmarkManager(connection);
	 * bm.removeBookmarkedConference(jid); }catch (Exception e) { // TODO:
	 * handle exception } }
	 */

	public class deleteAccount extends AsyncTask<String, String, String> {
		// ProgressDialog progressDialog;

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String result = null;
			HttpConfig ht = new HttpConfig();

			result = ht.httpget(KachingMeConfig.Delete_Account + "primaryNumber="
					+ KachingMeApplication.getjid().split("@")[0]);
			// Toast.makeText(getApplicationContext(), "result " + result,
			// Toast.LENGTH_LONG).show();
			Constant.printMsg("result " + result
					+ KachingMeApplication.getjid().split("@")[0]);

			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			// progressDialog.dismiss();
			Constant.printMsg("result ::::::: >>>>>> " + result);
			Toast.makeText(
					getApplicationContext(),
					"result " + result
							+ KachingMeApplication.getjid().split("@")[0],
					Toast.LENGTH_LONG).show();
			if (result != null && result.length() > 0) {

				if (result.equalsIgnoreCase("Account Deleted")) {

					com.wifin.kachingme.util.Constant.freelistmain.clear();
					Constant.printMsg("deleted db::beforeeeeee");
					// deletemethod();

					insertValueAgain();
					editor.remove("pin");
					editor.commit();

					stopService(new Intent(getApplicationContext(),
							TempConnectionService.class));

					com.wifin.kachingme.util.Constant.isNiftyApplicationRunning = true;
					Intent intent = new Intent(Delete_Account_Confirm.this,
							Slideshow.class);
					intent.addCategory(Intent.CATEGORY_HOME);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
							| Intent.FLAG_ACTIVITY_CLEAR_TASK);
					startActivity(intent);
					finish();

					// finish();
				} else {
					Toast.makeText(getApplicationContext(), "not exist",
							Toast.LENGTH_LONG).show();
				}

			}
			progressdialog.cancel();
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

		}

	}

	public void deletemethod() {
		// TODO Auto-generated method stub

		try {
			DbDelete();
			DbDeleteDeel();
			DbDeleteNym();
			DbDeleteBux();
			DbDeleteDonation();
			DbDeleteZZle();
			DbDeleteWish();
			DbDeleteFreeBie();
			DbDeleteMer();
			DbDeletekons();
			DbdeletePrimary();
			dbadapter.setDeleteLogin("");
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public void DbDelete() {
		// TODO Auto-generated method stub

		try {
			int a = db.open().getDatabaseObj()
					.delete(Dbhelper.TABLE_BUX, null, null);
			System.out
					.println("No of deleted rows from bux data is ::::::::::::"
							+ a);

		} catch (SQLException e) {
			System.out
					.println("Sql exception while deleting particular record for shop:::::"
							+ e.toString());
		} finally {
			db.close();
		}
	}

	public void DbDeleteDeel() {
		// TODO Auto-generated method stub

		try {
			int a = db.open().getDatabaseObj()
					.delete(Dbhelper.TABLE_CART, null, null);
			System.out
					.println("No of deleted rows from deel data is ::::::::::::"
							+ a);

		} catch (SQLException e) {
			System.out
					.println("Sql exception while deleting particular record for shop:::::"
							+ e.toString());
		} finally {
			db.close();
		}
	}

	public void DbDeleteNym() {
		// TODO Auto-generated method stub

		try {
			int a = db.open().getDatabaseObj()
					.delete(Dbhelper.TABLE_NYM, null, null);
			System.out
					.println("No of deleted rows from deel data is ::::::::::::"
							+ a);

		} catch (SQLException e) {
			System.out
					.println("Sql exception while deleting particular record for shop:::::"
							+ e.toString());
		} finally {
			db.close();
		}
	}

	public void DbDeleteBux() {
		// TODO Auto-generated method stub

		try {
			int a = db.open().getDatabaseObj()
					.delete(Dbhelper.TABLE_BUX, null, null);
			System.out
					.println("No of deleted rows from deel data is ::::::::::::"
							+ a);

		} catch (SQLException e) {
			System.out
					.println("Sql exception while deleting particular record for shop:::::"
							+ e.toString());
		} finally {
			db.close();
		}
	}

	public void DbDeleteZZle() {
		// TODO Auto-generated method stub

		try {
			int a = db.open().getDatabaseObj()
					.delete(Dbhelper.TABLE_ZZLE, null, null);
			System.out
					.println("No of deleted rows from deel data is ::::::::::::"
							+ a);

		} catch (SQLException e) {
			System.out
					.println("Sql exception while deleting particular record for shop:::::"
							+ e.toString());
		} finally {
			db.close();
		}
	}

	public void DbDeleteWish() {
		// TODO Auto-generated method stub

		try {
			int a = db.open().getDatabaseObj()
					.delete(Dbhelper.TABLE_WISH, null, null);
			System.out
					.println("No of deleted rows from deel data is ::::::::::::"
							+ a);

		} catch (SQLException e) {
			System.out
					.println("Sql exception while deleting particular record for shop:::::"
							+ e.toString());
		} finally {
			db.close();
		}
	}

	public void DbDeleteFreeBie() {
		// TODO Auto-generated method stub

		try {
			int a = db.open().getDatabaseObj()
					.delete(Dbhelper.TABLE_FREEBIE, null, null);
			System.out
					.println("No of deleted rows from freebie data is ::::::::::::"
							+ a);

		} catch (SQLException e) {
			System.out
					.println("Sql exception while deleting particular record for shop:::::"
							+ e.toString());
		} finally {
			db.close();
		}
	}

	public void DbDeleteMer() {
		// TODO Auto-generated method stub

		try {
			int a = db.open().getDatabaseObj()
					.delete(Dbhelper.TABLE_RET, null, null);
			System.out
					.println("No of deleted rows from retmetr data is ::::::::::::"
							+ a);

		} catch (SQLException e) {
			System.out
					.println("Sql exception while deleting particular record for shop:::::"
							+ e.toString());
		} finally {
			db.close();
		}
	}

	public void DbdeletePrimary() {
		// TODO Auto-generated method stub

		try {
			int a = db.open().getDatabaseObj()
					.delete(Dbhelper.TABLE_PRIMENUMBER, null, null);
			System.out
					.println("No of deleted rows from primary data is ::::::::::::"
							+ a);

		} catch (SQLException e) {
			System.out
					.println("Sql exception while deleting particular primary for shop:::::"
							+ e.toString());
		} finally {
			db.close();
		}
	}

	public void DbDeleteDonation() {
		// TODO Auto-generated method stub

		try {
			int a = db.open().getDatabaseObj()
					.delete(Dbhelper.TABLE_DONATE, null, null);
			System.out
					.println("No of deleted rows from deel data is ::::::::::::"
							+ a);

		} catch (SQLException e) {
			System.out
					.println("Sql exception while deleting particular record for shop:::::"
							+ e.toString());
		} finally {
			db.close();
		}
	}

	public void DbDeletekons() {
		// TODO Auto-generated method stub

		try {
			int a = db.open().getDatabaseObj()
					.delete(Dbhelper.TABLE_KONS, null, null);
			System.out
					.println("No of deleted rows from retmetr data is ::::::::::::"
							+ a);

		} catch (SQLException e) {
			System.out
					.println("Sql exception while deleting particular record for shop:::::"
							+ e.toString());
		} finally {
			db.close();
		}
	}

	private void insertValueAgain() {
		// TODO Auto-generated method stub
		db_data = "null";
		updateDBvalue(db_data);
	}

	public void updateDBvalue(String code) {
		Editor e = sp.edit();
		e.putString("db_data", code);
		Constant.printMsg("updateDBvalue:while logout" + code);
		e.commit();
	}
}
