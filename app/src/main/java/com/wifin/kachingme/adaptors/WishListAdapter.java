/*
* @author Sivaprasath
*
* @usage -  This class is used to display List items which is added by user
*
*
* */

package com.wifin.kachingme.adaptors;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.database.Dbhelper;
import com.wifin.kachingme.deals.WishActivity;
import com.wifin.kachingme.pojo.WishListDto;
import com.wifin.kachingme.util.Constant;

import java.util.List;

public class WishListAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    Activity myacActivity;
    Context mContext;
    List<WishListDto> nymList = Constant.addedwish;
    int width = Constant.screenWidth;
    int height = Constant.screenHeight;
    Dbhelper db;

    public WishListAdapter(Activity act, List<WishListDto> list) {
        // TODO Auto-generated constructor stub
        myacActivity = act;
        nymList = list;
        inflater = (LayoutInflater) myacActivity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        db = new Dbhelper(mContext);
        mContext = act.getApplicationContext();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return nymList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @SuppressLint({"ResourceAsColor", "DefaultLocale"})
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;
        final ViewHolder holder;
        width = Constant.screenWidth;
        height = Constant.screenHeight;

        if (convertView == null) {

            vi = inflater.inflate(R.layout.wish_adap, null);

            holder = new ViewHolder();
            holder.text = (TextView) vi.findViewById(R.id.wish_mess_name);
            holder.desc = (TextView) vi.findViewById(R.id.wish_abbre_desc);
            holder.lay = (LinearLayout) vi.findViewById(R.id.wish_adap_lay);
            holder.img = (ImageView) vi.findViewById(R.id.wish_edit_img);
            holder.dl = (ImageView) vi.findViewById(R.id.wish_del_img);

            Constant.typeFace(myacActivity, holder.text);
            Constant.typeFace(myacActivity, holder.desc);

            LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            buttonParams.width = width * 65 / 100;
            buttonParams.height = height * 12 / 100;
            holder.lay.setLayoutParams(buttonParams);

            LinearLayout.LayoutParams buttonParamsimg = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            buttonParamsimg.width = width * 12 / 100;
            buttonParamsimg.height = height * 5 / 100;
            buttonParamsimg.setMargins(width * 1 / 100, height * 1 / 100,
                    width * 1 / 100, height * 1 / 100);
            holder.img.setLayoutParams(buttonParamsimg);
            holder.dl.setLayoutParams(buttonParamsimg);

            LinearLayout.LayoutParams buttonParamstx = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            buttonParamstx.width = width * 26 / 100;
            buttonParamstx.height = height * 4 / 100;
            buttonParamstx.gravity = Gravity.CENTER;
            buttonParamstx.setMargins(width * 1 / 100, height * 1 / 100,
                    width * 1 / 100, height * 1 / 100);
            holder.text.setLayoutParams(buttonParamstx);
            holder.text.setGravity(Gravity.CENTER);

            LinearLayout.LayoutParams budes = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            budes.width = width * 65 / 100;
            budes.height = height * 7 / 100;
            budes.setMargins(width * 1 / 100, height * 1 / 100,
                    width * 1 / 100, height * 1 / 100);
            holder.desc.setLayoutParams(budes);
            holder.desc.setGravity(Gravity.CENTER);

            if (width >= 600) {

                holder.text.setTextSize(16);
                holder.desc.setTextSize(16);

            } else if (width > 501 && width < 600) {

                holder.text.setTextSize(15);
                holder.desc.setTextSize(15);

            } else if (width > 260 && width < 500) {

                holder.text.setTextSize(14);
                holder.desc.setTextSize(14);

            } else if (width <= 260) {

                holder.text.setTextSize(13);
                holder.desc.setTextSize(13);

            }

            vi.setTag(holder);

        } else {
            holder = (ViewHolder) vi.getTag();
        }

        holder.text.setText(nymList.get(position).getWishlistname());

        holder.desc.setText(nymList.get(position).getDate());

        holder.img.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Intent intent = new Intent(mContext, WishActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                myacActivity.startActivity(intent);
                myacActivity.finish();

            }
        });

        holder.dl.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                DbDeleteWhishListItem(position);

                WishListAdapter.this.notifyDataSetChanged();

            }
        });

        return vi;
    }

    public void DbDeleteWhishListItem(int pos) {
        // TODO Auto-generated method stub

        String dl = nymList.get(pos).getWishlistname().toString();
        Cursor c = null;
        String q = "DELETE FROM " + Dbhelper.TABLE_WISH + " WHERE name='" + dl
                + "'";
        try {
            c = db.open().getDatabaseObj().rawQuery(q, null);
            Constant.printMsg("No of deleted rows ::::::::::" + c.getCount());
        } catch (SQLException e) {

        } finally {
            c.close();
            db.close();
        }

        nymList.remove(pos);

        Constant.addedwish.remove(pos);

    }

    public static class ViewHolder {

        public TextView text;
        public TextView desc;
        public LinearLayout lay;
        public ImageView img, dl;

    }
}