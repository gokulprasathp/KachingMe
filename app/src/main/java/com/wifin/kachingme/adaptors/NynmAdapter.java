/*
* @author Gokul
*
* @usage -  This class is used to display the list of created Nynm
*
*
* */

package com.wifin.kachingme.adaptors;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.util.Constant;

import java.util.ArrayList;

public class NynmAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    ArrayList<String> mList = new ArrayList<String>();
    Activity activity;

    public NynmAdapter(Activity act, ArrayList<String> mList) {
        // TODO Auto-generated constructor stub
        inflater = (LayoutInflater) act
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mList = mList;
        activity = act;
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

    @SuppressLint({"ResourceAsColor", "DefaultLocale"})
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;
        ViewHolder holder = null;
        vi = inflater.inflate(R.layout.list_meaning, null);
        holder = new ViewHolder();

        holder.nymText = (TextView) vi.findViewById(R.id.label);
        try {
            Constant.typeFace(activity, holder.nymText);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.nymText.setText(displayNym(mList.get(position)), BufferType.SPANNABLE);
        vi.setTag(holder);
        return vi;
    }

    public SpannableStringBuilder displayNym(String value) {
        ArrayList<String> replacePosition = new ArrayList<String>();

        for (int i = 0; i < value.toString().length(); i++) {
            char tempCharecter = value.toString().charAt(i);
            if (tempCharecter == Constant.mNynmsSpecialCharacter) {
                replacePosition.add(String.valueOf(i));


            }

        }

        SpannableString redSpannable = new SpannableString(value);
        for (int i = 0; i < replacePosition.size(); i++) {
            redSpannable.setSpan(
                    new ForegroundColorSpan(Color.parseColor("#00123654")),
                    Integer.valueOf(replacePosition.get(i).toString()),
                    Integer.valueOf(replacePosition.get(i).toString()) + 1,
                    33);

        }
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(redSpannable);


        return builder;
    }

    public static class ViewHolder {

        public TextView nymText;

    }


}