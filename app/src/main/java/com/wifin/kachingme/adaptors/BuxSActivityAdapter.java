package com.wifin.kachingme.adaptors;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.pojo.BuxMasterDto;
import com.wifin.kachingme.pojo.BuxsEarnedDto;
import com.wifin.kachingme.util.Constant;

import java.util.ArrayList;

/**
 * Created by Wifin on 27-03-2017.
 */
public class BuxSActivityAdapter extends BaseAdapter {

    int height, width;
    ArrayList<BuxMasterDto> mList = new ArrayList<BuxMasterDto>();
    SharedPreferences sp;
    private Context context;
    private LayoutInflater inflater;

    public BuxSActivityAdapter(Context context, ArrayList mFilmNameTextList) {
        // TODO Auto-generated constructor stub
        Constant.printMsg("inside adapter");
        this.context = context;
        this.mList = mFilmNameTextList;
        sp = PreferenceManager.getDefaultSharedPreferences(context);
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
        holder = new ViewHolder();
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.my_earnings, parent, false);
        holder.mainlayout = (LinearLayout) convertView.findViewById(R.id.main);

        holder.yearstext = (TextView) convertView.findViewById(R.id.name_text);
        holder.nametext = (TextView) convertView.findViewById(R.id.date_text);
        holder.detailtext = (TextView) convertView
                .findViewById(R.id.amount_text);
        holder.mFirstView = (View) convertView
                .findViewById(R.id.earn_first_view);
        holder.mSecondView = (View) convertView
                .findViewById(R.id.earn_second_view);

        Constant.typeFace(context, holder.yearstext);
        Constant.typeFace(context, holder.nametext);
        Constant.typeFace(context, holder.detailtext);

        height = Constant.screenHeight;
        width = Constant.screenWidth;

        LinearLayout.LayoutParams head_lab = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        head_lab.height = 8 * height / 100;
        head_lab.width = width * 100 / 100;
        head_lab.gravity = Gravity.CENTER;
        holder.mainlayout.setLayoutParams(head_lab);

        LinearLayout.LayoutParams nameText = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        nameText.height = 8 * height / 100;
        nameText.width = (int) (32.0 * width / 100);
        nameText.gravity = Gravity.CENTER;
        holder.yearstext.setLayoutParams(nameText);

        LinearLayout.LayoutParams detail = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        detail.height = 8 * height / 100;
        detail.width = 32 * width / 100;
        detail.gravity = Gravity.CENTER;
        holder.detailtext.setLayoutParams(detail);

        LinearLayout.LayoutParams detail1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        detail1.height = 8 * height / 100;
        detail1.width = (int) (32.0 * width / 100);
        detail1.gravity = Gravity.CENTER;
        holder.nametext.setLayoutParams(detail1);

        LinearLayout.LayoutParams viewparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        viewparams.height = 8 * height / 100;
        viewparams.width = (int) (0.3 * width / 100);
        viewparams.gravity = Gravity.CENTER;
        holder.mFirstView.setLayoutParams(viewparams);
        holder.mSecondView.setLayoutParams(viewparams);

        convertView.setTag(holder);

        holder.yearstext.setText(mList.get(position).getActivity());
        holder.nametext.setText(String.valueOf(mList.get(position).getBuxsForActivity()));
//        String name = holder.yearstext.getText().toString();
//
//        if (name.equalsIgnoreCase("RegS")) {
//            int tpoint = sp.getInt("regpoints", 0);
//            int point = Integer.parseInt(mList.get(position).getAcheive());
//            int tot = tpoint * point;
//            holder.detailtext.setText(String.valueOf(tot));
//        } else if (name.equalsIgnoreCase("ConT Intro")) {
//            int tpoint = sp.getInt("intropoint", 0);
//            int point = Integer.parseInt(mList.get(position).getAcheive());
//            int tot = tpoint * point;
//            holder.detailtext.setText(String.valueOf(tot));
//        } else if (name.equalsIgnoreCase("ChaT")) {
//            int tpoint = sp.getInt("chatpoint", 0);
//            int point = Integer.parseInt(mList.get(position).getAcheive());
//            int tot = tpoint * point;
//            holder.detailtext.setText(String.valueOf(tot));
//        } else if (name.equalsIgnoreCase("DazZ")) {
//            int tpoint = sp.getInt("zzlepoint", 0);
//            int point = Integer.parseInt(mList.get(position).getAcheive());
//            int tot = tpoint * point;
//            holder.detailtext.setText(String.valueOf(tot));
//        } else if (name.equalsIgnoreCase("DazZ Buy")) {
//
//        } else if (name.equalsIgnoreCase("NynM")) {
//            int tpoint = sp.getInt("nympoint", 0);
//            int point = Integer.parseInt(mList.get(position).getAcheive());
//            int tot = tpoint * point;
//            holder.detailtext.setText(String.valueOf(tot));
//        } else if (name.equalsIgnoreCase("WheR")) {
//            int tpoint = sp.getInt("locpoint", 0);
//            int point = Integer.parseInt(mList.get(position).getAcheive());
//            int tot = tpoint * point;
//            holder.detailtext.setText(String.valueOf(tot));
//        } else if (name.equalsIgnoreCase("ShwT")) {
//            int tpoint = sp.getInt("imgpoint", 0);
//            int point = Integer.parseInt(mList.get(position).getAcheive());
//            int tot = tpoint * point;
//            holder.detailtext.setText(String.valueOf(tot));
//        } else if (name.equalsIgnoreCase("DeeL")) {
//            int tpoint = sp.getInt("deelpoint", 0);
//            int point = Integer.parseInt(mList.get(position).getAcheive());
//            int tot = tpoint * point;
//            holder.detailtext.setText(String.valueOf(tot));
//        } else if (name.equalsIgnoreCase("DeeL Red")) {
//
//        } else if (name.equalsIgnoreCase("Wish")) {
//            int tpoint = sp.getInt("wishpoint", 0);
//            int point = Integer.parseInt(mList.get(position).getAcheive());
//            int tot = tpoint * point;
//            holder.detailtext.setText(String.valueOf(tot));
//        } else if (name.equalsIgnoreCase("KonS")) {
//            int tpoint = sp.getInt("konpoint", 0);
//            int point = Integer.parseInt(mList.get(position).getAcheive());
//            int tot = tpoint * point;
//            holder.detailtext.setText(String.valueOf(tot));
//        } else if (name.equalsIgnoreCase("DesT")) {
//            int tpoint = sp.getInt("destpoint", 0);
//            int point = Integer.parseInt(mList.get(position).getAcheive());
//            int tot = tpoint * point;
//            holder.detailtext.setText(String.valueOf(tot));
//        }
        return convertView;
    }

    static class ViewHolder {
        LinearLayout mainlayout;
        TextView yearstext, nametext, detailtext;
        View mFirstView, mSecondView;
    }
}