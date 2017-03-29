package com.wifin.kachingme.chat.chat_adaptors;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wifin.kachingme.util.ImageLoader;
import com.wifin.kachingme.pojo.Location_getset;
import com.wifin.kachingme.util.Log;
import com.wifin.kaching.me.ui.R;

public class LocationAdapter extends ArrayAdapter<Location_getset> {
    ArrayList<Location_getset> list;
    Context context;
    ImageLoader imageLoader;
    private int resource;
    private LayoutInflater layoutInflater;

    public LocationAdapter(Context context, int textViewResourceId,
                           ArrayList<Location_getset> objects) {
        super(context, textViewResourceId, objects);
        // TODO Auto-generated constructor stub
        this.resource = textViewResourceId;
        layoutInflater = LayoutInflater.from(context);
        list = objects;
        this.context = context;
        imageLoader = new ImageLoader(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(context).inflate(
                R.layout.item_places, null);
        Location_getset user = list.get(position);
        // View container = inflater.inflate(R.layout.location_share, parent,
        // false);
        // set name

        TextView name = (TextView) convertView.findViewById(R.id.txt_name);
        TextView vicinity = (TextView) convertView
                .findViewById(R.id.txt_vicinity);
        ImageView icon = (ImageView) convertView
                .findViewById(R.id.location_icon);
        ImageView status_icon = (ImageView) convertView
                .findViewById(R.id.img_loc_selected_status);

        try {
            name.setText(user.getName());
            if (user.getVicinity().length() > 31) {
                vicinity.setText(user.getVicinity().toString()
                        .subSequence(0, 30)
                        + "....");
            }
        } catch (Exception e) {
            // ACRA.getErrorReporter().handleException(e);
            // TODO: handle exception
        }
        if (user.getIs_selected() == true) {
            status_icon.setImageDrawable(context.getResources().getDrawable(
                    R.drawable.location_red));
            Log.d("Location",
                    "Location::" + user.getName() + " " + user.getIs_selected());
        }

        try {
            icon.setTag(user.getIcon_path());
            // //Constant.printMsg("Call" + user.getIcon_path());
            //imageLoader.DisplayImage(user.getIcon_path(), context, icon);
            //siva
            imageLoader.DisplayImage(user.getIcon_path(), icon);

        } catch (Exception e) {
            // ACRA.getErrorReporter().handleException(e);
            // TODO: handle exception
        }

        return convertView;
    }

}