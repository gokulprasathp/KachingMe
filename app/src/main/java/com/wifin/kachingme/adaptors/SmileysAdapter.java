/*
* @author Gokul
*
* @usage -  This class is used to display list of smiles image
*
*
* */

package com.wifin.kachingme.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.wifin.kaching.me.ui.R;

import java.util.ArrayList;
import java.util.HashMap;

public class SmileysAdapter extends BaseAdapter {

    private ArrayList<String> arrayListSmileys = new ArrayList<String>();
    private Context context;
    private HashMap<String, Integer> emoticons = new HashMap<String, Integer>();

    public SmileysAdapter(ArrayList<String> arraylistSmileys, Context context,
                          HashMap<String, Integer> emoticons) {
        // TODO Auto-generated constructor stub

        this.arrayListSmileys = arraylistSmileys;
        this.context = context;
        this.emoticons = emoticons;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return arrayListSmileys.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return arrayListSmileys.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.row, null);
        ImageView imageView = (ImageView) convertView
                .findViewById(R.id.img_avatar);
        imageView.setBackgroundResource(emoticons.get(arrayListSmileys
                .get(position)));
        return convertView;
    }
}