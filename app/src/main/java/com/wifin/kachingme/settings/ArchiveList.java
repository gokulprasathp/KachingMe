package com.wifin.kachingme.settings;

import java.util.ArrayList;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.database.DatabaseHelper;
import com.wifin.kaching.me.ui.R;

public class ArchiveList extends Activity {

	ListView list_archive;
	ArrayList<String> archive_list = new ArrayList<String>();
	Cursor cursor;
	DatabaseHelper dbAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lock_list);
		Initiate();
		dbAdapter = KachingMeApplication.getDatabaseAdapter();

		// cursor = dbAdapter.getArchive_list(null);
		// for (int i = 0; i < cursor.getCount(); i++) {
		// archive_list.add(cursor.getString(1));
		// Constant.printMsg("cursor.getString(1)    " + cursor.getCount()
		// + "   " + cursor.getString(1));
		// }

		if (archive_list.size() > 0) {
			ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, archive_list);
			list_archive.setAdapter(arrayAdapter);
		}

	}

	private void Initiate() {
		// TODO Auto-generated method stub
		list_archive = (ListView) findViewById(R.id.list);
	}

}
