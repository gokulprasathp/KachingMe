/*
* @author Priya
*
* @usage -  This class is used to display available wallpaper
*
*
* */

package com.wifin.kachingme.adaptors;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.settings.Wallpaper_Activity;
import com.wifin.kachingme.util.Constant;

import java.util.ArrayList;

public class WallpaperAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    Activity myacActivity;
    Context mContext = Wallpaper_Activity.getContext();
    ArrayList<Integer> mList = new ArrayList<Integer>();

    public WallpaperAdapter(Wallpaper_Activity wallpaper_Activity,
                            ArrayList<Integer> list) {
        // TODO Auto-generated constructor stub
        myacActivity = wallpaper_Activity;
        mList = list;
        inflater = (LayoutInflater) myacActivity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.wallpaper_item, null);
            holder = new ViewHolder();
            holder.img = (ImageView) convertView.findViewById(R.id.image_item);

            LinearLayout.LayoutParams layoutMenuq1 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutMenuq1.height = Constant.screenWidth * 30 / 100;
            layoutMenuq1.width = Constant.screenWidth * 30 / 100;
            layoutMenuq1.topMargin = Constant.screenWidth * 3 / 100;
            holder.img.setLayoutParams(layoutMenuq1);

            for (int i = 0; i < mList.size(); i++) {
                holder.img.setImageResource(mList.get(position));
                KachingMeApplication.getsharedpreferences_Editor()
                        .putString("wallpaper", mList.get(position).toString())
                        .commit();
            }
        }

        return convertView;
    }

    class ViewHolder {
        public ImageView img;
    }

}
