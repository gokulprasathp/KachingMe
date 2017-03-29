package com.wifin.kachingme.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.util.Constant;

import java.util.ArrayList;

public class HLVAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context context;

    ArrayList<String> alName;
    ArrayList<Integer> alImage;

    public HLVAdapter(Context context, ArrayList<String> alName, ArrayList<Integer> alImage) {

        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.alName = alName;
        this.alImage = alImage;
    }

    @Override
    public int getCount() {
        return alName.size();
    }

    @Override
    public Object getItem(int position) {
        return alName.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder holder;
        if (convertView == null) {
            view = mInflater.inflate(R.layout.hlv_inflate, parent, false);
            holder = new ViewHolder();
            holder.imgThumbnail = (ImageView) view.findViewById(R.id.img_thumbnail);
            holder.tvSpecies = (TextView) view.findViewById(R.id.tv_species);
            Constant.typeFace(context,holder.tvSpecies);

            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        holder.tvSpecies.setText(alName.get(position));
        holder.imgThumbnail.setImageResource(alImage.get(position));


        RelativeLayout.LayoutParams buttonParams = new RelativeLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
         buttonParams.width = Constant.screenWidth * 15 / 100;
        buttonParams.height = (int) (Constant.screenHeight * 7 / 100);
        buttonParams.topMargin = (int) (Constant.screenHeight * 1 / 100);
        buttonParams.bottomMargin = (int) (Constant.screenHeight * 1 / 100);
        holder.imgThumbnail.setLayoutParams(buttonParams);
//        holder.mKonsText.setGravity(Gravity.CENTER);

        return view;
    }

    private class ViewHolder {
        public ImageView imgThumbnail;
        public TextView tvSpecies;

    }
}
