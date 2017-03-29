package com.wifin.kachingme.adaptors;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.util.Constant;


public class CustomAdapter extends BaseAdapter {
    Context context;
    int flags[];
    String[] countryNames;
    LayoutInflater inflter;

    public CustomAdapter(Context applicationContext, int[] flags, String[] countryNames) {
        this.context = applicationContext;
        this.flags = flags;
        this.countryNames = countryNames;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {

        return flags.length;
    }

    @Override
    public Object getItem(int i) {

        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.custom_spinner_items, null);
        ImageView icon = (ImageView) view.findViewById(R.id.imageView);
        TextView names = (TextView) view.findViewById(R.id.textView);
        Constant.typeFace(context, names);
        icon.setImageResource(flags[i]);
        names.setText(countryNames[i]);


        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonParams.width = Constant.screenWidth * 5 / 100;
        buttonParams.height = (int) (Constant.screenWidth * 5 / 100);
        buttonParams.topMargin = (int) (Constant.screenHeight * 1 / 100);
        buttonParams.bottomMargin = (int) (Constant.screenHeight * 1 / 100);
        buttonParams.gravity = Gravity.CENTER;
        icon.setPadding(5, 0, 0, 0);
        icon.setLayoutParams(buttonParams);

        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        textParams.width = Constant.screenWidth * 45 / 100;
        textParams.height = (int) (Constant.screenWidth * 5 / 100);
        textParams.leftMargin = Constant.screenWidth * 2 / 100;

        textParams.gravity = Gravity.CENTER;
        names.setGravity(Gravity.CENTER | Gravity.LEFT);
        names.setLayoutParams(textParams);

        return view;
    }
}
