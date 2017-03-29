package com.wifin.kachingme.settings;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.SQLException;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.v7.widget.Toolbar;

import com.wifin.kachingme.applications.BasePreferenceActivity;
import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.database.DatabaseHelper;
import com.wifin.kachingme.database.Dbhelper;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.Log;
import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.registration_and_login.VerificationActivity;

public class delete_account_settings extends BasePreferenceActivity {

	SharedPreferences sp;
	SharedPreferences.Editor editor;
	String db_data;
	SharedPreferences pref;
	Context context;
	Dbhelper db;
	DatabaseHelper dbadapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// addPreferencesFromResource(R.xml.setting_delete_account);
		final Toolbar toolbar = getToolbar();
		toolbar.setTitle(R.string.account);
		sp = getSharedPreferences(KachingMeApplication.getPereference_label(),
				Activity.MODE_PRIVATE);
		editor = sp.edit();
		pref = PreferenceManager.getDefaultSharedPreferences(this);
		context = this.getApplicationContext();
		db = new Dbhelper(getApplicationContext());
		dbadapter = KachingMeApplication.getDatabaseAdapter();

	}

	@Override
	protected int getPreferencesXmlId() {
		// TODO Auto-generated method stub
		return R.xml.setting_delete_account;
	}

	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {
		// TODO Auto-generated method stub

		if (preference.getKey().equals("delete_account")) {

			Log.d("delete_account", "Delete Account Clicked...");
			Intent intent = new Intent(this, Delete_Account_Confirm.class);
			startActivity(intent);

			return true;
		} else if (preference.getKey().equals("addnumber")) {

			Constant.settings = true;
			Constant.mFromsettingVerfication = true;
			Intent intent = new Intent(this, VerificationActivity.class);
			startActivity(intent);

			return true;
		} else if (preference.getKey().equals("privacy")) {

			Intent intent = new Intent(this, Privacy.class);
			startActivity(intent);

			return true;
		}
		// else if (preference.getKey().equals("logout")) {
		//
		// Constant.freelistmain.clear();
		// Constant.printMsg("deleted db::beforeeeeee");
		// deletemethod();
		//
		// // context.deleteDatabase("kachingme.db");
		// Constant.printMsg("deleted db::");
		// // DatabaseHelper db = null;
		// // db.close();
		// // clearApplicationData();
		// // MyApplication.getInstance().clearApplicationData();
		// // File file = new File("/data/data/"
		// // + getApplicationContext().getPackageName()
		// // + "/databases/kachingme.db");
		// // Constant.printMsg("fileeeee::::::::" + file);
		// // Boolean deleted = file.delete();
		// //
		// // Constant.printMsg("boole::" + deleted);
		// //
		// // if (deleted) {
		// insertValueAgain();
		// editor.remove("pin");
		// editor.commit();
		// finish();
		// // }
		//
		this.finish();
		//
		// Intent intent = new Intent(this, slideshow.class);
		// intent.addCategory(Intent.CATEGORY_HOME);
		// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
		// | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		// startActivity(intent);
		// // System.exit(0);
		// }
		return super.onPreferenceTreeClick(preferenceScreen, preference);

	}

	private void deletemethod() {
		// TODO Auto-generated method stub
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
		dbadapter.setDeleteLogin(null);
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

	public void clearApplicationData() {
		File cache = getCacheDir();
		File appDir = new File(cache.getParent());
		if (appDir.exists()) {
			String[] children = appDir.list();
			for (String s : children) {
				if (!s.equals("lib")) {
					deleteDir(new File(appDir, s));
					Log.i("TAG",
							"**************** File /data/data/APP_PACKAGE/" + s
									+ " DELETED *******************");
				}
			}
		}
	}

	public static boolean deleteDir(File dir) {
		if (dir != null && dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}

		return dir.delete();
	}

	private void insertValueAgain() {
		// TODO Auto-generated method stub
		db_data = "null";
		updateDBvalue(db_data);
	}

	public void updateDBvalue(String code) {
		Editor e = pref.edit();
		e.putString("db_data", code);
		Constant.printMsg("updateDBvalue:while logout" + code);
		e.commit();
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

}