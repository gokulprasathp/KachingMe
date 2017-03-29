package com.wifin.kachingme.util;

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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.applications.SherlockBaseActivity;
import com.wifin.kachingme.chat.broadcast_chat.Broadcast_info;
import com.wifin.kachingme.chat_home.SliderTesting;
import com.wifin.kachingme.database.DatabaseHelper;
import com.wifin.kachingme.pojo.ContactsGetSet;

import java.util.ArrayList;

public class SelectContactList extends SherlockBaseActivity {
    ActionMode mMode;
    DatabaseHelper dbAdapter;
    ArrayList<ContactsGetSet> users;
    ListView lstview;
    Handler h;
    Thread refresh;
    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multiselect_memebers);

        lstview = (ListView) findViewById(R.id.list_experience);
        lstview.setVisibility(View.VISIBLE);
        dbAdapter = KachingMeApplication.getDatabaseAdapter();
        users = new ArrayList<ContactsGetSet>();
        users = dbAdapter.getContacts();

        try {
            for (int i = 0; i < Broadcast_info.contact_list.size(); i++) {

                for (int j = 0; j < users.size(); j++) {

                    if (users.get(j).getJid().equalsIgnoreCase(Broadcast_info.contact_list.get(i).getJid())) {
                        users.remove(j);
                    }

                }

            }
        } catch (Exception e) {

        }

        Constant.printMsg("dskjfjf" + users.size());
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

                return false;
            }
        });
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchableInfo info = searchManager
                .getSearchableInfo(getComponentName());
        searchView.setSearchableInfo(info);
        MenuItem item_search = menu.findItem(R.id.menu_search);
//		item_search = MenuItemCompat.setActionView(item_search, searchView);
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

        Intent i = new Intent(SelectContactList.this, SliderTesting.class);
        startActivity(i);
        finish();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
//        Intent i = new Intent(SelectContactList.this, SliderTesting.class);
//        startActivity(i);
        finish();
    }

    private final class AnActionModeOfEpicProportions implements
            ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {

            MenuInflater inflater = new MenuInflater(SelectContactList.this);

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
        ArrayList<ContactsGetSet> list;
        Context context;
        private int resource;
        private LayoutInflater layoutInflater;

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
            int height=Constant.screenHeight;
            int width=Constant.screenWidth;

            TextView name = (TextView) container.findViewById(R.id.name);
            ImageView img = (ImageView) container.findViewById(R.id.avtarimg);
            TextView status = (TextView) container.findViewById(R.id.status);
            try {
                Constant.typeFace(SelectContactList.this, name);
            } catch (Exception e) {
                e.printStackTrace();
            }
            LinearLayout.LayoutParams textParama = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            textParama.height = height * 10 / 100;
            textParama.gravity = Gravity.CENTER |Gravity.LEFT;
            name.setLayoutParams(textParama);
            name.setGravity(Gravity.CENTER|Gravity.LEFT);
            name.setPadding(width*3/100,0,0,0);

            LinearLayout.LayoutParams circularImage = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            circularImage.width = width * 17 / 100;
            circularImage.height = height * 9 / 100;
            circularImage.gravity = Gravity.CENTER;
            circularImage.setMargins(width*3/100,height*1/2/100,width*2/100,height*1/2/100);
            img.setLayoutParams(circularImage);

            com.github.siyamed.shapeimageview.CircularImageView imgeView = (com.github.siyamed.shapeimageview.CircularImageView)
                    container.findViewById(R.id.btn_more);
            imgeView.setVisibility(View.GONE);
            name.setText(user.getDisplay_name());
            try {
                status.setText(user.getStatus());
            } catch (Exception e) {
                // ACRA.getErrorReporter().handleException(e);
                // TODO: handle exceptionas
                e.printStackTrace();
            }
            try {
                if (user.getPhoto_ts() != null) {
                    System.gc();
                    Bitmap bitmap;
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = false;
                    options.inPreferredConfig = Bitmap.Config.RGB_565;
                    options.inDither = true;
                    options.inSampleSize = 2;
                    try {
                        bitmap = BitmapFactory.decodeByteArray(user.getPhoto_ts(),
                                0, user.getPhoto_ts().length, options);
                        ProfileRoundImg mSenderImage = new ProfileRoundImg(bitmap);
                        img.setImageDrawable(mSenderImage);
                    } catch (OutOfMemoryError e) {
                        android.util.Log.e("Map", "Tile Loader (241) Out Of Memory Error " + e.getLocalizedMessage());
                        System.gc();
                    } catch (Exception e) {
                    }
                } else {
                    img.setImageDrawable(getResources().getDrawable(
                            R.drawable.avtar));
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
            return container;
        }
    }
}
