package com.wifin.kachingme.adaptors;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.settings.Contact;
import com.wifin.kachingme.settings.ContactUsActivity;
import com.wifin.kachingme.util.Constant;

import java.util.ArrayList;

/**
 * Created by Wifin on 25/10/2016.
 */

public class ContactAdapter extends BaseAdapter
{
    Activity myacActivity;
    Context mContext = Contact.context;
    private static LayoutInflater inflater = null;
    ArrayList<String> mList = new ArrayList<>();

    public ContactAdapter(Contact contactAdapter, ArrayList<String> mList2)
    {
        // TODO Auto-generated constructor stub

        this.myacActivity = contactAdapter;
        this.mList = mList2;
        inflater = (LayoutInflater) myacActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount()
    {
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
        ViewHolder holder;
        // View vi = convertView;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.contact_us_item, null);
            holder = new ViewHolder();
            holder.add_img = (ImageView) convertView
                    .findViewById(R.id.img_grid);
            holder.delete_img = (ImageView) convertView
                    .findViewById(R.id.img_grid_delete);
            holder.frame_layout = (FrameLayout) convertView
                    .findViewById(R.id.frame_layout);
            holder.main_layout = (LinearLayout) convertView
                    .findViewById(R.id.main_layout);

            int size = mList.size();
//			if (position == size - 1) {
//				holder.delete_img.setVisibility(View.GONE);
//			} else {
            holder.delete_img.setVisibility(View.VISIBLE);

//			}

            // byte[] by_new = mList.get(position).getBytes();
            // for(int i=0; i<by_new.length; i++)
            // Constant.printMsg("by1[" + i + "] >> " + by_new[i]);

            LinearLayout.LayoutParams layoutMenuq1 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutMenuq1.height = Constant.screenHeight * 20 / 100;
            layoutMenuq1.width = (int) (Constant.screenWidth * 31.0 / 100);
            holder.frame_layout.setLayoutParams(layoutMenuq1);
            holder.main_layout.setLayoutParams(layoutMenuq1);

            FrameLayout.LayoutParams layoutMenuq = new FrameLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutMenuq.height = Constant.screenHeight * 20 / 100;
            layoutMenuq.width = (int) (Constant.screenWidth * 31.0 / 100);
            holder.add_img.setLayoutParams(layoutMenuq);

            FrameLayout.LayoutParams layoutdelete = new FrameLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutdelete.height = Constant.screenWidth * 7 / 100;
            layoutdelete.width = Constant.screenWidth * 7 / 100;
            holder.delete_img.setLayoutParams(layoutdelete);

            holder.delete_img.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    mList.remove(position);
                    Intent intent = new Intent(myacActivity, Contact.class);
                    myacActivity.startActivity(intent);

                }
            });
            if (mList.get(position).toString().equalsIgnoreCase("1")) {

            } else {
                Bitmap bitmap;
                try {
                    byte[] encodeByte = Base64.decode(mList.get(position)
                            .toString(), Base64.DEFAULT);
                    bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
                            encodeByte.length);
                } catch (Exception e) {
                    e.getMessage();
                    return null;
                }
                holder.add_img.setImageBitmap(bitmap);
            }
        }
        return convertView;
    }

    class ViewHolder {
        public ImageView add_img, delete_img;
        public FrameLayout frame_layout;
        public LinearLayout main_layout;
    }
}

