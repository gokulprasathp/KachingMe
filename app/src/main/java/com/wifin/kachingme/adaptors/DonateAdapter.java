/*
* @author Sivaprasath
*
* @usage -  This class is used to display the list of bux donated
*
*
* */

package com.wifin.kachingme.adaptors;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.pojo.DonationDto;
import com.wifin.kachingme.util.Constant;

import java.util.ArrayList;
import java.util.List;

public class DonateAdapter extends BaseAdapter {

    int height, width;
    List<DonationDto> donatelist = new ArrayList<DonationDto>();
    private Context context;
    private LayoutInflater inflater;

    public DonateAdapter(Context context, List<DonationDto> dolist) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.donatelist = dolist;
    }

    @Override
    public int getCount() {
        // TODO Auto-donatelist method stub
        return donatelist.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return donatelist.get(position);
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
            holder = new ViewHolder();
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.mydonate, parent, false);
            holder.mainlayout = (LinearLayout) convertView
                    .findViewById(R.id.maindonate);

            holder.yearstext = (TextView) convertView
                    .findViewById(R.id.name_textdonate);
            holder.nametext = (TextView) convertView
                    .findViewById(R.id.date_textdonate);
            holder.detailtext = (TextView) convertView
                    .findViewById(R.id.amount_textdonate);
            holder.tvDonateNorec = (TextView) convertView
                    .findViewById(R.id.tvDonateNorec);
            holder.mFirstView = (View) convertView
                    .findViewById(R.id.earn_first_viewdonate);
            holder.mSecondView = (View) convertView
                    .findViewById(R.id.earn_second_viewdonate);
            holder.buxsstatus = (TextView) convertView
                    .findViewById(R.id.buxs_status);
            holder.mThirdView = (View) convertView
                    .findViewById(R.id.bux_status_viewdonate);

            Constant.typeFace(context, holder.yearstext);
            Constant.typeFace(context, holder.nametext);
            Constant.typeFace(context, holder.detailtext);
            Constant.typeFace(context, holder.buxsstatus);

            height = Constant.screenHeight;
            width = Constant.screenWidth;

            LinearLayout.LayoutParams head_lab = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            head_lab.height = 8 * height / 100;
            head_lab.gravity = Gravity.CENTER;
            holder.mainlayout.setLayoutParams(head_lab);

            LinearLayout.LayoutParams nameText = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            nameText.height = 8 * height / 100;
            nameText.width = 45 * width / 100;
            nameText.gravity = Gravity.CENTER;
            holder.nametext.setLayoutParams(nameText);

            LinearLayout.LayoutParams detail = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            detail.height = 8 * height / 100;
            detail.width = 18 * width / 100;
            detail.gravity = Gravity.CENTER;
            holder.yearstext.setLayoutParams(detail);
            holder.detailtext.setLayoutParams(detail);
            holder.buxsstatus.setLayoutParams(detail);

            LinearLayout.LayoutParams viewparams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            viewparams.height = 8 * height / 100;
            viewparams.width = (int) (0.3 * width / 100);
            viewparams.gravity = Gravity.CENTER;
            holder.mFirstView.setLayoutParams(viewparams);
            holder.mSecondView.setLayoutParams(viewparams);
            holder.mThirdView.setLayoutParams(viewparams);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.nametext.setText(donatelist.get(position).getName());
        holder.yearstext.setText(donatelist.get(position).getDate());

        holder.detailtext.setText(donatelist.get(position).getPoint());
        holder.tvDonateNorec.setText("No Records Available");

        if (donatelist.get(position).getStatus() != null) {
            if (donatelist.get(position).getStatus().equalsIgnoreCase("credit")) {
                holder.buxsstatus.setText(context.getResources().getString(R.string.buxs_recived));

            } else {
                holder.buxsstatus.setText(context.getResources().getString(R.string.buxs_donated));

            }

        }

        return convertView;
    }

    static class ViewHolder {
        LinearLayout mainlayout;
        TextView yearstext, nametext, detailtext, buxsstatus, tvDonateNorec;
        View mFirstView, mSecondView, mThirdView;
    }
}
