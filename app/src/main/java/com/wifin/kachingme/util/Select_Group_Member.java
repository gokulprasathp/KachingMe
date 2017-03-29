package com.wifin.kachingme.util;

import java.util.ArrayList;
import java.util.regex.Pattern;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
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

import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.applications.SherlockBaseActivity;
import com.wifin.kachingme.database.DatabaseHelper;
import com.wifin.kachingme.pojo.ContactsGetSet;
import com.wifin.kaching.me.ui.R;

public class Select_Group_Member extends SherlockBaseActivity {
	ActionMode mMode;
	private SearchView mSearchView;
	DatabaseHelper dbAdapter;
	ArrayList<ContactsGetSet> users;
	ListView lstview;

	Handler h;
	Thread refresh;
	String jid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.multiselect_memebers);

		Bundle bundle = getIntent().getExtras();

		if (bundle != null) {
			jid = bundle.getString("jid");
		}

		lstview = (ListView) findViewById(R.id.list_experience);
		dbAdapter = KachingMeApplication.getDatabaseAdapter();
		users = new ArrayList<ContactsGetSet>();
		users = dbAdapter.get_Select_Group_Contacts(jid);
		UserListAdapter adapter = new UserListAdapter(this,
				R.layout.contactlistitem, users);
		lstview.setAdapter(adapter);

		View view = new View(this);
		lstview.addFooterView(view);

		lstview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent intent = new Intent();
				intent.putExtra("jid", users.get(position).getJid());
				/*
				 * intent.putExtra("name",users.get(position).getDisplay_name());
				 * intent.putExtra("avatar",users.get(position).getPhoto_ts());
				 */
				/* startActivity(intent); */
				setResult(1, intent);
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.add_new_menu, menu);

		SearchView searchView = new SearchView(getSupportActionBar()
				.getThemedContext());
		searchView.setQueryHint("Search for countries");
		searchView.setIconified(true);
		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String s) {
				return false;
			}

			@Override
			public boolean onQueryTextChange(String s) {
				// Constant.printMsg("Text:" + s);
				int textlength = s.length();
				getSearchText(s);
				return false;
			}
		});
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchableInfo info = searchManager
				.getSearchableInfo(getComponentName());
		searchView.setSearchableInfo(info);
		MenuItem item_search = menu.findItem(R.id.menu_search);
		item_search.setActionView(searchView);
		return true;
	}

	protected void getSearchText(String ch) {
		// TODO Auto-generated method stub

		ArrayList<ContactsGetSet> list = new ArrayList<ContactsGetSet>();
		// list = null;
		if (ch != null && ch.length() > 0) {
			// Constant.printMsg("HIIIII00000");
			for (int i = 0; i < users.size(); i++) {
				ContactsGetSet dc = new ContactsGetSet();
				// if
				// (mListDailySearch.get(i).getDoctorChemistName().contains(ch))
				// {

				if (Pattern
						.compile(Pattern.quote(ch), Pattern.CASE_INSENSITIVE)
						.matcher(users.get(i).getDisplay_name()).find()) {
					// Constant.printMsg("Hiiiiiii");
					dc = users.get(i);
					list.add(dc);
				}

			}

		} else {
			users = dbAdapter.get_Select_Group_Contacts(jid);
			list = users;

		}

		// users=new ArrayList<contactsGetSet>();
		UserListAdapter adapter = new UserListAdapter(this,
				R.layout.contactlistitem, list);
		lstview.setAdapter(adapter);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		/*
		 * switch (item.getItemId()) { case R.id.menu_add_new: startActivity(new
		 * Intent(Demo_Activity.activity, Fragment_Activity_New.class)); return
		 * true; case R.id.list_view: startActivity(new
		 * Intent(Demo_Activity.activity, detail_view.class)); return true;
		 * 
		 * 
		 * default:
		 * 
		 * }
		 */
		return super.onOptionsItemSelected(item);
	}

	private final class AnActionModeOfEpicProportions implements
			ActionMode.Callback {
		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {

			MenuInflater inflater = new MenuInflater(Select_Group_Member.this);

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

			ImageView img = (ImageView) container.findViewById(R.id.avtarimg);

			try {
				if (user.getPhoto_ts() != null) {
					Bitmap bmp = BitmapFactory.decodeByteArray(
							user.getPhoto_ts(), 0, user.getPhoto_ts().length);
					img.setImageBitmap(new AvatarManager().roundCornerImage(
							bmp, 180));
				} else {
					img.setImageDrawable(getResources().getDrawable(
							R.drawable.avtar));
				}
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

			return container;
		}

	}

}
