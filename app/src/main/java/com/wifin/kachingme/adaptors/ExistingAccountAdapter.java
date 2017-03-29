/*
* @author Sivanesan
*
* @usage -  This class is used to display the list of existing redeemer.
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.pojo.ShopDetailsDto;
import com.wifin.kachingme.util.Constant;

import java.util.ArrayList;
import java.util.List;

public class ExistingAccountAdapter extends BaseAdapter {

    int height, width;
    Activity myacActivity;
    private List<ShopDetailsDto> mCartList = new ArrayList<ShopDetailsDto>();
    private LayoutInflater inflater;

    public ExistingAccountAdapter(Activity context, List<ShopDetailsDto> mList) {
        myacActivity = context;
        this.mCartList = mList;
        this.inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mCartList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mCartList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        final ViewHolder holder;
        if (convertView == null) {
            vi = inflater.inflate(R.layout.existing_account_adapter, null);
            holder = new ViewHolder();
            holder.$CompanyName = (TextView) vi
                    .findViewById(R.id.existingAccAdapter_comapanyName);
            holder.$ComapanyCode = (TextView) vi
                    .findViewById(R.id.existingAccAdapter_companyCode);
            holder.$Address = (TextView) vi
                    .findViewById(R.id.existingAccAdapter_address);
            holder.$EmailId = (TextView) vi
                    .findViewById(R.id.existingAccAdapter_email);
            holder.$View = (TextView) vi
                    .findViewById(R.id.existingAccAdapter_view);
            holder.$CheckImage = (ImageView) vi
                    .findViewById(R.id.existingAccAdapter_checkImage);
            holder.$UnCheckImage = (ImageView) vi
                    .findViewById(R.id.existingAccAdapter_unCheckImage);
            height = Constant.screenHeight;
            width = Constant.screenWidth;


            Constant.typeFace(myacActivity, holder.$CompanyName);
            Constant.typeFace(myacActivity, holder.$ComapanyCode);
            Constant.typeFace(myacActivity, holder.$Address);
            Constant.typeFace(myacActivity, holder.$EmailId);
            Constant.typeFace(myacActivity, holder.$View);

            LinearLayout.LayoutParams nameParama = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            nameParama.width = width * 55 / 100;
            nameParama.leftMargin = width * 5 / 100;
            nameParama.bottomMargin = height * 1 / 100;
            nameParama.topMargin = height * 1 / 100;
            nameParama.gravity = Gravity.CENTER | Gravity.LEFT;
            holder.$CompanyName.setLayoutParams(nameParama);
            holder.$CompanyName.setGravity(Gravity.CENTER | Gravity.LEFT);

            LinearLayout.LayoutParams viewParama = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            viewParama.width = width * 1 / 100;
            viewParama.topMargin = height * 1 / 100;
            holder.$View.setLayoutParams(viewParama);

            LinearLayout.LayoutParams checkimageParama = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            checkimageParama.height = height * 10 / 100;
            checkimageParama.width = width * 8 / 100;
            checkimageParama.gravity = Gravity.CENTER | Gravity.LEFT;
            checkimageParama.topMargin = height * 1 / 100;
            checkimageParama.bottomMargin = height * 1 / 100;
            checkimageParama.leftMargin = width * 2 / 100;
            holder.$CheckImage.setLayoutParams(checkimageParama);
            holder.$UnCheckImage.setLayoutParams(checkimageParama);

            LinearLayout.LayoutParams comapnyCodeParama = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            comapnyCodeParama.width = width * 35 / 100;
            comapnyCodeParama.topMargin = height * 1 / 100;
            comapnyCodeParama.bottomMargin = height * 1 / 100;
            comapnyCodeParama.leftMargin = width * 2 / 100;
            comapnyCodeParama.rightMargin = width * 3 / 100;
            comapnyCodeParama.gravity = Gravity.CENTER | Gravity.LEFT;
            holder.$ComapanyCode.setLayoutParams(comapnyCodeParama);
            holder.$ComapanyCode.setGravity(Gravity.CENTER);
            holder.$ComapanyCode.setPadding((int) (width * 0.5 / 100),
                    (int) (height * 0.5 / 100), (int) (width * 0.5 / 100),
                    (int) (height * 0.5 / 100));

            LinearLayout.LayoutParams addressParama = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            addressParama.width = width * 92 / 100;
            addressParama.topMargin = height * 1 / 100;
            addressParama.leftMargin = width * 5 / 100;
            holder.$Address.setLayoutParams(addressParama);
            holder.$Address.setGravity(Gravity.CENTER | Gravity.LEFT);

            LinearLayout.LayoutParams mailParama = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            mailParama.width = width * 92 / 100;
            mailParama.gravity = Gravity.CENTER | Gravity.LEFT;
            mailParama.topMargin = height * 1 / 100;
            mailParama.bottomMargin = height * 2 / 100;
            mailParama.leftMargin = width * 5 / 100;
            holder.$EmailId.setLayoutParams(mailParama);
            holder.$EmailId.setGravity(Gravity.CENTER | Gravity.LEFT);

            if (width >= 600) {
                holder.$CompanyName.setTextSize(18);
                holder.$ComapanyCode.setTextSize(18);
                holder.$Address.setTextSize(18);
                holder.$EmailId.setTextSize(18);
            } else if (width > 501 && width < 600) {
                holder.$CompanyName.setTextSize(17);
                holder.$ComapanyCode.setTextSize(17);
                holder.$Address.setTextSize(17);
                holder.$EmailId.setTextSize(17);
            } else if (width > 260 && width < 500) {
                holder.$CompanyName.setTextSize(16);
                holder.$ComapanyCode.setTextSize(16);
                holder.$Address.setTextSize(16);
                holder.$EmailId.setTextSize(16);
            } else if (width <= 260) {
                holder.$CompanyName.setTextSize(15);
                holder.$ComapanyCode.setTextSize(15);
                holder.$Address.setTextSize(15);
                holder.$EmailId.setTextSize(15);
            }

            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }

        holder.$CompanyName.setText(mCartList.get(position).getName());
        holder.$ComapanyCode.setText(String.valueOf(mCartList.get(position)
                .getShopId()));
        holder.$Address.setText(mCartList.get(position).getAddress());
        holder.$EmailId.setText(mCartList.get(position).getEmail());
        holder.$CheckImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                holder.$CheckImage.setVisibility(View.GONE);
                holder.$UnCheckImage.setVisibility(View.VISIBLE);

            }
        });

        holder.$UnCheckImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                holder.$UnCheckImage.setVisibility(View.GONE);
                holder.$CheckImage.setVisibility(View.VISIBLE);
            }
        });

        return vi;
    }

    public static class ViewHolder {
        TextView $CompanyName, $ComapanyCode, $Address, $EmailId, $View;
        ImageView $CheckImage, $UnCheckImage;
    }
}