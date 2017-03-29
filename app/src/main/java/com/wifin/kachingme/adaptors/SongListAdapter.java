/*
* @author Dilip
*
* @usage -  This class is used to display list of karaoke songs
*
*
* */

package com.wifin.kachingme.adaptors;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.kaching_feature.karaoke.KaraokeTab;
import com.wifin.kachingme.util.Constant;

import java.util.ArrayList;
import java.util.List;

public class SongListAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    Activity myacActivity;
    int width, height;
    List<String> mList = new ArrayList<String>();

    public SongListAdapter(Activity act, List<String> mDpList) {
        // TODO Auto-generated constructor stub

        myacActivity = act;
        mList = mDpList;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;
        final ViewHolder holder;

        if (convertView == null) {

            vi = inflater.inflate(R.layout.song_list_adapter_layout, null);
            holder = new ViewHolder();
            holder.mTxtSongName = (TextView) vi
                    .findViewById(R.id.kaching_adapter_txt);
            holder.mlinSongName = (LinearLayout) vi.findViewById(R.id.songmain);
            height = KaraokeTab.height;
            width = KaraokeTab.width;
            Constant.typeFace(myacActivity, holder.mTxtSongName);

            LinearLayout.LayoutParams txtParama = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            txtParama.height = height * 7 / 100;
            txtParama.width = width;
            holder.mTxtSongName.setLayoutParams(txtParama);
            holder.mTxtSongName.setPadding(width * 3 / 100, 0, 0, 0);
            holder.mTxtSongName.setGravity(Gravity.CENTER | Gravity.LEFT);

            if (width >= 600) {
                holder.mTxtSongName.setTextSize(16);
            } else if (width > 501 && width < 600) {
                holder.mTxtSongName.setTextSize(15);
            } else if (width > 260 && width < 500) {
                holder.mTxtSongName.setTextSize(14);
            } else if (width <= 260) {
                holder.mTxtSongName.setTextSize(13);
            }
            vi.setTag(holder);

        } else {
            holder = (ViewHolder) vi.getTag();
        }

        holder.mTxtSongName.setText(mList.get(position).trim());

        return vi;
    }

    public static class ViewHolder {
        public TextView mTxtSongName;
        public LinearLayout mlinSongName;
    }
}
