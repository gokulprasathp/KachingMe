package com.wifin.kachingme.util;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.SearchView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.applications.SherlockBaseActivity;
import com.wifin.kachingme.chat_home.SliderTesting;
import com.wifin.kachingme.database.DatabaseHelper;
import com.wifin.kachingme.fragments.GroupChatList;
import com.wifin.kachingme.pojo.ContactsGetSet;
import com.wifin.kachingme.pojo.GroupParticipantGetSet;
import com.wifin.kaching.me.ui.R;

public class Select_MUC_Admin extends SherlockBaseActivity {
	ActionMode mMode;
	private SearchView mSearchView;
	DatabaseHelper dbAdapter;
	ArrayList<ContactsGetSet> users;
	ListView lstview;

	Handler h;
	Thread refresh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.multiselect_memebers);

		lstview = (ListView) findViewById(R.id.list_experience);
        lstview.setVisibility(View.VISIBLE);

		String jids = null;
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			jids = bundle.getString("jids");
		}

		Log.d("Select_MUC_Admin", "JIDS::" + jids);

		dbAdapter = KachingMeApplication.getDatabaseAdapter();
		users = new ArrayList<ContactsGetSet>();
		// users=dbAdapter.getContacts_selected(jids);

		ArrayList<GroupParticipantGetSet> list = new ArrayList<GroupParticipantGetSet>();
		list = dbAdapter.getGroupMembersOnly(jids);


        Constant.printMsg("Select MUC admin  list size : " + list);
		for (GroupParticipantGetSet groupParticipantGetSet : list) {

			ContactsGetSet contact = dbAdapter
					.getContact(groupParticipantGetSet.getJid());

			if (contact == null) {
				contact = new ContactsGetSet();
				if (!groupParticipantGetSet.getJid().equals(
						KachingMeApplication.getjid())) {
					/*
					 * contact.setDisplay_name("You"); } else {
					 */
					contact.setDisplay_name(groupParticipantGetSet.getJid()
							.split("@")[0]);
				}
				contact.setJid(groupParticipantGetSet.getJid());

				users.add(contact);
			}

			/*
			 * if(groupParticipantGetSet.getJid().equals(my_jid)) {
			 * contact.setDisplay_name("You");
			 * contact.setPhoto_ts(KachingMeApplication.getAvatar());
			 * contact.setJid(my_jid); }
			 * 
			 * if(Is_Admin) { contact_list.add(contact); }
			 */
			else {
				// Log.d("","Contact jid::"+contact.getJid()+" group_admin::"+group_admin);
				/*
				 * if(int_group_type>0 ) {
				 * if(contact.getJid().equals(group_admin)) {
				 * contact_list.add(contact); } } else {
				 */

                Constant.printMsg("Select MUC admin  contact display : " + contact.getDisplay_name());

				users.add(contact);
				/* } */
			}
		}

        Constant.printMsg("Select MUC admin  list size 2 "
                +users.size());

		/*
		 * for (GroupParticipantGetSet groupParticipantGetSet : list) {
		 * 
		 * 
		 * 
		 * }
		 */

		UserListAdapter adapter = new UserListAdapter(this,
				 users);
		lstview.setAdapter(adapter);

		lstview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {


                GroupChatList.grpChatListObj.Admin_exit(users.get(position).getJid());


                Intent intent = new Intent(Select_MUC_Admin.this, SliderTesting.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("SliderIntent", "SliderTesting");
                startActivity(intent);
                finish();


			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// getMenuInflater().inflate(R.menu.add_new_menu, menu);

		/*
		 * SearchView searchView = new
		 * SearchView(getSupportActionBar().getThemedContext());
		 * searchView.setQueryHint("Search for countries");
		 * searchView.setIconified(true); searchView.setOnQueryTextListener(new
		 * SearchView.OnQueryTextListener() {
		 * 
		 * @Override public boolean onQueryTextSubmit(String s) { return false;
		 * }
		 * 
		 * @Override public boolean onQueryTextChange(String s) {
		 * //Constant.printMsg("Text:" + s); int textlength = s.length();
		 * 
		 * return false; } }); SearchManager searchManager =
		 * (SearchManager)getSystemService(Context.SEARCH_SERVICE);
		 * SearchableInfo info =
		 * searchManager.getSearchableInfo(getComponentName());
		 * searchView.setSearchableInfo(info); MenuItem item_search =
		 * menu.findItem(R.id.menu_search);
		 * item_search.setActionView(searchView);
		 */

		// getMenuInflater().inflate(R.menu.main_menu, menu);
		/*
		 * SearchManager searchManager = (SearchManager)
		 * getSystemService(Context.SEARCH_SERVICE); MenuItem searchItem =
		 * menu.findItem(R.id.menu_search); SearchView searchView = (SearchView)
		 * MenuItemCompat.getActionView(searchItem);
		 * searchView.setSearchableInfo(
		 * searchManager.getSearchableInfo(getComponentName()));
		 * searchView.setQueryHint
		 * (getResources().getString(R.string.search_contacts));
		 * searchView.setIconified(true); AutoCompleteTextView searchText =
		 * (AutoCompleteTextView) searchView.findViewById(R.id.search_src_text);
		 * searchText.setHintTextColor(getResources().getColor(color.white));
		 * searchText.setTextColor(getResources().getColor(color.white));
		 * 
		 * searchView.setOnQueryTextListener(new
		 * SearchView.OnQueryTextListener() {
		 * 
		 * @Override public boolean onQueryTextSubmit(String s) { return false;
		 * }
		 * 
		 * @Override public boolean onQueryTextChange(String s) {
		 * //Constant.printMsg("Text:" + s); int textlength = s.length(); //
		 * new FetchChat().execute(s);
		 * 
		 * return false; } });
		 */

		return true;
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

			MenuInflater inflater = new MenuInflater(Select_MUC_Admin.this);

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

	private class UserListAdapter extends BaseAdapter {
		ArrayList<ContactsGetSet> list;
		Context context;
        private  LayoutInflater inflater=null;

      	public UserListAdapter(Context context,
				ArrayList<ContactsGetSet> objects) {
			// TODO Auto-generated constructor stub
			list = objects;
			this.context = context;
            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            Constant.printMsg("Adapter select admin " + list.size());


		}

        @Override
        public int getCount() {
            Constant.printMsg("Adapter select admin11 " + list.size());
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
		public View getView(int position, View convertView, ViewGroup parent) {



            View vi=convertView;
            if(convertView==null)
                vi = inflater.inflate(R.layout.contactlistitem, null);

           ContactsGetSet user = list.get(position);
            TextView name = (TextView) vi.findViewById(R.id.name);
            name.setText(user.getDisplay_name());
            ImageView img = (ImageView) vi.findViewById(R.id.avtarimg);
            ImageView img_more = (ImageView) vi.findViewById(R.id.btn_more);
            img_more.setVisibility(View.GONE);
            LinearLayout.LayoutParams circularImage = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            circularImage.width = Constant.screenWidth * 15 / 100;
            circularImage.height = Constant.screenHeight * 10 / 100;
            circularImage.gravity = Gravity.CENTER | Gravity.LEFT;
            circularImage.leftMargin = Constant.screenWidth * 4 / 100;
            circularImage.rightMargin = Constant.screenWidth * 4 / 100;
            img.setLayoutParams(circularImage);

            Constant.printMsg("CCCCCCCCCCAL"+user.getDisplay_name());
            Bitmap bmp = null;

            System.gc();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inDither = true;
            options.inSampleSize = 2;
            try {
                bmp = BitmapFactory.decodeByteArray( user.getPhoto_ts(), 0, user.getPhoto_ts().length, options);
                ProfileRoundImg mSenderImage = new ProfileRoundImg(bmp);
                img.setImageDrawable(mSenderImage);

            } catch (OutOfMemoryError e) {
                android.util.Log.e("Map", "Tile Loader (241) Out Of Memory Error " + e.getLocalizedMessage());
                System.gc();
            } catch (Exception e) {

            }

            // set status
            TextView status = (TextView) vi.findViewById(R.id.status);

            try {
                status.setText(user.getStatus());
            } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
                // TODO: handle exceptionas
                e.printStackTrace();
            }


            return vi;




			}

	}

}
