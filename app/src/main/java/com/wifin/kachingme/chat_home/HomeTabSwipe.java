/*
* @author Gokul
*
* @usage -  This class is used to display the Home screen tabs like chat,group and contacts
*
*
* */

package com.wifin.kachingme.chat_home;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.fragments.FavouriteContacts;
import com.wifin.kachingme.fragments.GroupChatList;
import com.wifin.kachingme.fragments.UserChatList;
import com.wifin.kachingme.util.Constant;

public class HomeTabSwipe extends FragmentPagerAdapter {
    public static int[] imageResId = {
            R.drawable.chat_tab,
            R.drawable.group_tab,
            R.drawable.contacts_tab
    };
    //integer to count number of tabs
    int tabCount;
    Context context;

    //Constructor to the class
    public HomeTabSwipe(Context context, FragmentManager fm, int tabCount) {
        super(fm);
        //Initializing tab count
        this.tabCount = tabCount;
        this.context = context;
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                UserChatList mChatHomeTabFragment = new UserChatList();
                return mChatHomeTabFragment;
            case 1:
                GroupChatList mGroupHomeTabFragment = new GroupChatList();
                return mGroupHomeTabFragment;
            case 2:
                FavouriteContacts mContactHomeTabFragment = new FavouriteContacts();
                return mContactHomeTabFragment;
            default:
                return null;
        }
    }

    public View getTabView(int position) {

        String tabTitles[] = new String[]{"CHATS", "GROUPS", "CONTACTS"};

        View v = LayoutInflater.from(context).inflate(R.layout.home_tab_hedder, null);
        TextView tv = (TextView) v.findViewById(R.id.textView);
        tv.setText(tabTitles[position]);
        Constant.typeFace(context, tv);
        ImageView img = (ImageView) v.findViewById(R.id.imgView);
        img.setImageResource(imageResId[position]);
        /*try {
            totalChatCount = (TextView) v.findViewById(R.id.total_chat_count);

            LinearLayout.LayoutParams mImageParams1 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            mImageParams1.width = Constant.screenWidth * 7 / 100;
            mImageParams1.height = Constant.screenWidth * 7 / 100;
            totalChatCount.setLayoutParams(mImageParams1);

            totalChatCount.setText(Constant.mChatCounts.get(position));
        } catch (Exception e) {

        }*/

        LinearLayout.LayoutParams mImageParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mImageParams.width = Constant.screenWidth * 9 / 100;
        mImageParams.height = Constant.screenWidth * 9 / 100;
        img.setLayoutParams(mImageParams);

        LinearLayout.LayoutParams txtParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        txtParams.gravity = Gravity.CENTER;
        txtParams.topMargin = Constant.screenHeight * 1 / 2 / 100;
        tv.setLayoutParams(txtParams);
//        totalChatCount.setLayoutParams(txtParams);

        return v;
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        Drawable image = ContextCompat.getDrawable(context, imageResId[position]);
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        SpannableString sb = new SpannableString(" ");
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }

    @Override
    public int getItemPosition(Object object) {
        // POSITION_NONE makes it possible to reload the PagerAdapter
        return POSITION_NONE;
    }
}

