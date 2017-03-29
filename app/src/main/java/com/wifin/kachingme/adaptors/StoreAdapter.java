/*
* @author Sivanesan
*
* @usage -  This class is used to display List of nearest available Redeemer
*
*
* */

package com.wifin.kachingme.adaptors;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.deals.OfferSummaryActivity;
import com.wifin.kachingme.deals.StoreActivity;
import com.wifin.kachingme.pojo.DicDto;
import com.wifin.kachingme.registration_and_login.FreebieActivity;
import com.wifin.kachingme.util.Constant;

import java.util.List;

public class StoreAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    Activity myacActivity;
    Context mContext = StoreActivity.getContext();
    List<DicDto> cartlist = Constant.dictlist;
    int width = Constant.screenWidth;
    int height = Constant.screenHeight;
    private ImageLoadingListener animateFirstListener = new FreebieActivity.AnimateFirstDisplayListener();
    private DisplayImageOptions options;

    public StoreAdapter(Activity act, List<DicDto> crlist) {
        // TODO Auto-generated constructor stub
        myacActivity = act;
        cartlist = crlist;
        ImageLoader.getInstance().init(
                ImageLoaderConfiguration.createDefault(mContext));
        inflater = (LayoutInflater) myacActivity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.stub)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_launcher).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true).build();

        ImageLoader.getInstance().init(
                ImageLoaderConfiguration.createDefault(mContext));

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return cartlist.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return cartlist.get(position);
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

            vi = inflater.inflate(R.layout.offeradapter, null);
            holder = new ViewHolder();
            holder.text = (TextView) vi.findViewById(R.id.ka_offer_name);
            holder.desc = (TextView) vi.findViewById(R.id.ka_offer_desc);
            holder.img = (ImageView) vi.findViewById(R.id.ka_offer_img);
            holder.lay = (LinearLayout) vi.findViewById(R.id.ka_offer_lay);
            holder.deel = (TextView) vi.findViewById(R.id.ka_offer_deelno);

            Constant.typeFace(myacActivity, holder.text);
            Constant.typeFace(myacActivity, holder.desc);
            Constant.typeFace(myacActivity, holder.deel);

            LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            buttonParams.width = width * 58 / 100;
            buttonParams.height = height * 14 / 100;
            holder.lay.setLayoutParams(buttonParams);

            LinearLayout.LayoutParams buttonParamsimg = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            buttonParamsimg.width = width * 15 / 100;
            buttonParamsimg.height = height * 14 / 100;
            buttonParamsimg.setMargins(width * 1 / 100, height * 1 / 100,
                    width * 1 / 100, height * 1 / 100);
            holder.img.setLayoutParams(buttonParamsimg);

            LinearLayout.LayoutParams buttonParamstx = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            buttonParamstx.width = width * 58 / 100;
            buttonParamstx.height = height * 7 / 100;
            buttonParamstx.gravity = Gravity.CENTER;
            holder.text.setLayoutParams(buttonParamstx);
            holder.text.setGravity(Gravity.CENTER);

            LinearLayout.LayoutParams budes = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            budes.width = width * 58 / 100;
            budes.height = height * 7 / 100;
            holder.desc.setLayoutParams(budes);
            holder.desc.setGravity(Gravity.CENTER);

            LinearLayout.LayoutParams deelnou = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            deelnou.width = width * 20 / 100;
            deelnou.height = height * 14 / 100;
            holder.deel.setLayoutParams(deelnou);
            holder.deel.setGravity(Gravity.CENTER);

            if (width >= 600) {

                holder.text.setTextSize(17);
                holder.desc.setTextSize(17);
                holder.deel.setTextSize(17);

            } else if (width > 501 && width < 600) {

                holder.text.setTextSize(16);
                holder.desc.setTextSize(16);
                holder.deel.setTextSize(16);

            } else if (width > 260 && width < 500) {

                holder.text.setTextSize(15);
                holder.desc.setTextSize(15);
                holder.deel.setTextSize(15);

            } else if (width <= 260) {

                holder.text.setTextSize(14);
                holder.desc.setTextSize(14);
                holder.deel.setTextSize(14);

            }

            vi.setTag(holder);

        } else {
            holder = (ViewHolder) vi.getTag();
        }

        holder.text.setText(cartlist.get(position).getFirstName());

        holder.desc.setText(cartlist.get(position).getWebsite());

        holder.desc.setPaintFlags(holder.desc.getPaintFlags()
                | Paint.UNDERLINE_TEXT_FLAG);

        holder.deel.setText(cartlist.get(position).getDeelCounts() + " Deel");

        ImageLoader.getInstance().displayImage(
                String.valueOf(cartlist.get(position).getCompanyLogoPath())
                        .replaceAll(" ", "%20"), holder.img, options,
                animateFirstListener);

        vi.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Constant.merchantid = cartlist.get(position).getMerchantId();

                Intent intent = new Intent(mContext, OfferSummaryActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                myacActivity.startActivity(intent);
                myacActivity.finish();

            }
        });

        holder.desc.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
                        .parse("http://" + cartlist.get(position).getWebsite()));
                myacActivity.startActivity(browserIntent);

            }
        });

        return vi;
    }

    public static class ViewHolder {

        public TextView text;
        public TextView desc, deel;
        public ImageView img;
        public LinearLayout lay;

    }
}