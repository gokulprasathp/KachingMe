/*
* @author Gokul
*
* @usage -  This class is used to display the choosen freebee
*
*
* */

package com.wifin.kachingme.adaptors;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.chat_home.MainActivity;
import com.wifin.kachingme.database.Dbhelper;
import com.wifin.kachingme.deals.OfferSummaryActivity;
import com.wifin.kachingme.pojo.CartAdapDto;
import com.wifin.kachingme.pojo.CartDetailsDto;
import com.wifin.kachingme.registration_and_login.FreebieActivity;
import com.wifin.kachingme.util.Connectivity;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.HttpConfig;
import com.wifin.kachingme.util.KachingMeConfig;

import java.util.ArrayList;
import java.util.List;

public class OfferSummaryAdaptor extends BaseAdapter {

    private static LayoutInflater inflater = null;
    Activity myacActivity;
    Context mContext = OfferSummaryActivity.getContext();
    List<CartAdapDto> cartlist = Constant.cartadap;
    int width = Constant.screenWidth;
    int height = Constant.screenHeight;
    String data, phone;
    int pos;
    Dbhelper db;
    SharedPreferences sp;
    ArrayList<String> mClicked = new ArrayList<String>();
    Button offerAddBtn;
    int clickedPos, count = 1;
    private ImageLoadingListener animateFirstListener = new FreebieActivity.AnimateFirstDisplayListener();
    private DisplayImageOptions options;

    public OfferSummaryAdaptor(Activity act, List<CartAdapDto> crlist) {
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
        db = new Dbhelper(mContext);
        sp = PreferenceManager.getDefaultSharedPreferences(myacActivity);

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
        ViewHolder holder;
        width = Constant.screenWidth;
        height = Constant.screenHeight;

        if (convertView == null) {

            vi = inflater.inflate(R.layout.offersummary_adap, null);
            holder = new ViewHolder();

            holder.prname = (TextView) vi.findViewById(R.id.sum_imagetext);
            holder.offprodname = (TextView) vi.findViewById(R.id.sum_offerprod);
            holder.fxtext = (TextView) vi.findViewById(R.id.sum_fixed_name);
            holder.fxedit = (TextView) vi.findViewById(R.id.sum_fixed_prz);
            holder.oftext = (TextView) vi.findViewById(R.id.sum_offer_name);
            holder.ofedit = (TextView) vi.findViewById(R.id.sum_offer_prz);
            holder.bxtext = (TextView) vi.findViewById(R.id.sum_bux_name);
            holder.bxedit = (TextView) vi.findViewById(R.id.sum_bux_prz);
            holder.img = (ImageView) vi.findViewById(R.id.sum_imag);
            holder.iglay = (LinearLayout) vi.findViewById(R.id.sum_image_lay);
            holder.oflay = (LinearLayout) vi.findViewById(R.id.sum_off_lay);
            holder.flay = (LinearLayout) vi.findViewById(R.id.sum_fixed_prz_lay);
            holder.olay = (LinearLayout) vi.findViewById(R.id.sum_offer_prz_lay);
            holder.blay = (LinearLayout) vi.findViewById(R.id.sum_bux_lay);
            holder.add = (Button) vi.findViewById(R.id.sum_addcart);

            try {
                Constant.typeFace(mContext, holder.prname);
                Constant.typeFace(mContext, holder.offprodname);
                Constant.typeFace(mContext, holder.fxtext);
                Constant.typeFace(mContext, holder.fxedit);
                Constant.typeFace(mContext, holder.oftext);
                Constant.typeFace(mContext, holder.ofedit);
                Constant.typeFace(mContext, holder.bxtext);
                Constant.typeFace(mContext, holder.bxedit);
            } catch (Exception e) {
                e.printStackTrace();
            }

            LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            buttonParams.width = width * 40 / 100;
            buttonParams.height = height * 37 / 100;
            holder.iglay.setLayoutParams(buttonParams);

            LinearLayout.LayoutParams offerpar = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            offerpar.width = width * 60 / 100;
            offerpar.height = height * 37 / 100;
            holder.oflay.setLayoutParams(offerpar);

            LinearLayout.LayoutParams fly = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            fly.width = width * 60 / 100;
            fly.height = height * 6 / 100;
            holder.flay.setLayoutParams(fly);
            holder.olay.setLayoutParams(fly);
            holder.blay.setLayoutParams(fly);

            LinearLayout.LayoutParams buttonParamsimg = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            buttonParamsimg.width = width * 40 / 100;
            buttonParamsimg.height = height * 22 / 100;
            buttonParamsimg.setMargins(width * 1 / 100, height * 1 / 100,
                    width * 1 / 100, height * 1 / 100);
            holder.img.setLayoutParams(buttonParamsimg);

            LinearLayout.LayoutParams buttonParamstext = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            buttonParamstext.width = width * 40 / 100;
            buttonParamstext.height = height * 9 / 100;
            buttonParamstext.setMargins(width * 1 / 100, height * 1 / 100,
                    width * 1 / 100, height * 1 / 100);
            holder.prname.setLayoutParams(buttonParamstext);
            holder.prname.setGravity(Gravity.CENTER);

            LinearLayout.LayoutParams buttonParamstx = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            buttonParamstx.width = width * 30 / 100;
            buttonParamstx.height = height * 8 / 100;
            buttonParamstx.setMargins(0, height * 1 / 100, 0, height * 1 / 100);
            buttonParamstx.gravity = Gravity.CENTER;
            holder.offprodname.setLayoutParams(buttonParamstx);
            holder.add.setLayoutParams(buttonParamstx);
            holder.add.setGravity(Gravity.CENTER);

            LinearLayout.LayoutParams budes = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            budes.width = width * 30 / 100;
            budes.height = height * 5 / 100;
            budes.setMargins(width * 1 / 100, height * 1 / 100,
                    width * 1 / 100, height * 1 / 100);
            holder.fxtext.setLayoutParams(budes);
            holder.oftext.setLayoutParams(budes);
            holder.bxtext.setLayoutParams(budes);

            holder.fxedit.setLayoutParams(budes);
            holder.ofedit.setLayoutParams(budes);
            holder.bxedit.setLayoutParams(budes);

            if (width >= 600) {

                holder.fxtext.setTextSize(17);
                holder.oftext.setTextSize(17);
                holder.bxtext.setTextSize(17);

                holder.fxedit.setTextSize(17);
                holder.ofedit.setTextSize(17);
                holder.bxtext.setTextSize(17);

                holder.offprodname.setTextSize(17);
                holder.prname.setTextSize(17);

            } else if (width > 501 && width < 600) {

                holder.fxtext.setTextSize(16);
                holder.oftext.setTextSize(16);
                holder.bxtext.setTextSize(16);

                holder.fxedit.setTextSize(16);
                holder.ofedit.setTextSize(16);
                holder.bxtext.setTextSize(16);

                holder.offprodname.setTextSize(16);
                holder.prname.setTextSize(16);

            } else if (width > 260 && width < 500) {

                holder.fxtext.setTextSize(15);
                holder.oftext.setTextSize(15);
                holder.bxtext.setTextSize(15);

                holder.fxedit.setTextSize(15);
                holder.ofedit.setTextSize(15);
                holder.bxtext.setTextSize(15);

                holder.offprodname.setTextSize(15);
                holder.prname.setTextSize(15);

            } else if (width <= 260) {

                holder.fxtext.setTextSize(14);
                holder.oftext.setTextSize(14);
                holder.bxtext.setTextSize(14);

                holder.fxedit.setTextSize(14);
                holder.ofedit.setTextSize(14);
                holder.bxtext.setTextSize(14);

                holder.offprodname.setTextSize(14);
                holder.prname.setTextSize(14);
            }
            holder.add.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // // TODO Auto-generated method stub

                    if (Connectivity.isConnected(myacActivity)) {

                        String query = "select * from "
                                + Dbhelper.TABLE_CART
                                + " where deelid = '"
                                + cartlist.get(position)
                                .getDeelOfferDiscountId() + "'";

                        if (getDeelIdFromDB(query) == 0) {

                            offerAddBtn = (Button) v.findViewById(R.id.sum_addcart);

                            clickedPos = (Integer) v.getTag();

                            phone = KachingMeApplication.getjid().split("@")[0];

                            pos = position;

                            data = jsonForm();

                            new postUpdate().execute();

                        } else {
                            Toast.makeText(mContext,
                                    "This Deel Already Exists Your Cart",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(mContext,
                                "Please check your network connection",
                                Toast.LENGTH_SHORT).show();
                    }

                }

            });

            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }

        holder.prname.setText(cartlist.get(position).getProductName());

        holder.offprodname.setText(cartlist.get(position).getItem());

        holder.fxedit.setText(cartlist.get(position).getOffer());

        holder.ofedit.setText(cartlist.get(position).getDiscount());

        holder.bxedit.setText(cartlist.get(position).getBuxs());

        ImageLoader.getInstance().displayImage(
                String.valueOf(cartlist.get(position).getPhotoPath())
                        .replaceAll(" ", "%20"), holder.img, options,
                animateFirstListener);

        MainActivity.cartno.setText(String.valueOf(Constant.cartfinal.size()));

        if (mClicked.size() > 0) {
            if (mClicked.contains(String.valueOf(position))) {

                for (int j = 0; j < mClicked.size(); j++) {
                    if (mClicked.get(j).equals(String.valueOf(position))) {
                        holder.add.setText("Added");

                        holder.add.setEnabled(false);

                        holder.add.setTextColor(Color.BLACK);

                        holder.add.setBackgroundColor(Color.rgb(238, 233, 233));
                    }
                }
            } else {
                holder.add.setText("Add to cart");

                holder.add.setEnabled(true);

                holder.add.setTextColor(Color.WHITE);

                holder.add.setBackgroundResource(R.drawable.selectorforclick);
            }
        }
        holder.add.setTag(position);

        return vi;
    }

    private void Update() {
        // TODO Auto-generated method stub

        offerAddBtn.setText("Added");

        offerAddBtn.setEnabled(false);

        offerAddBtn.setTextColor(Color.BLACK);

        offerAddBtn.setBackgroundColor(Color.rgb(238, 233, 233));

        mClicked.add(String.valueOf(clickedPos));

        CartAdapDto ca = new CartAdapDto();
        ca.setBuxs(cartlist.get(pos).getBuxs());
        ca.setDeelOfferDiscountId(cartlist.get(pos).getDeelOfferDiscountId());
        ca.setDiscount(cartlist.get(pos).getDiscount());
        ca.setItem(cartlist.get(pos).getItem());
        ca.setMerchantId(cartlist.get(pos).getMerchantId());
        ca.setOffer(cartlist.get(pos).getOffer());
        ca.setPhotoPath(cartlist.get(pos).getPhotoPath());
        ca.setProductName(cartlist.get(pos).getProductName());
        ca.setQrCodePath(cartlist.get(pos).getQrCodePath());
        ca.setType("1");
        ca.setMerchantName(cartlist.get(pos).getMerchantName());
        ca.setMerphotoPath(cartlist.get(pos).getMerphotoPath());
        Constant.cartfinal.add(ca);

        String phone = KachingMeApplication.getjid().split("@")[0];

        ContentValues cv = new ContentValues();

        cv.put("phonenumber", phone);
        cv.put("deelid", cartlist.get(pos).getDeelOfferDiscountId());
        cv.put("type", "1");
        cv.put("desc", "Deels");
        cv.put("photopath", cartlist.get(pos).getPhotoPath());
        cv.put("bux", cartlist.get(pos).getBuxs());
        cv.put("qrpath", cartlist.get(pos).getQrCodePath());
        cv.put("discount", cartlist.get(pos).getDiscount());
        cv.put("item", cartlist.get(pos).getItem());
        cv.put("offer", cartlist.get(pos).getOffer());
        cv.put("prodname", cartlist.get(pos).getProductName());
        cv.put("merchantname", cartlist.get(pos).getMerchantName());
        cv.put("merchantid", cartlist.get(pos).getMerchantId());
        insertDB(cv);

        MainActivity.cartno.setText(String.valueOf(Constant.cartfinal.size()));

    }

    private String jsonForm() {
        // TODO Auto-generated method stub
        String d = null;

        try {
            Long buxval = Constant.bux + Constant.deelpoints;
            Constant.bux = buxval;

            int point = sp.getInt("deelpoint", 0);

            Constant.totaldeel = point;

            Constant.totaldeel = count + Constant.totaldeel;

            Editor e2 = sp.edit();
            e2.putInt("deelpoint", Constant.totaldeel);
            e2.commit();

            Editor e = sp.edit();
            e.putLong("buxvalue", buxval);
            e.putLong("uservalue", Constant.userId);
            e.commit();

            CartDetailsDto l = new CartDetailsDto();
            l.setBux(buxval);
            l.setType(1);
            l.setPhoneNumber(phone);
            l.setDescription(cartlist.get(pos).getProductName());
            l.setOfferId(Long.parseLong(cartlist.get(pos).getDeelOfferDiscountId()));

            d = new Gson().toJson(l);
        } catch (NumberFormatException e1) {
        }

        return d;
    }

    public void insertDB(ContentValues v) {

        try {
            int a = (int) db.open().getDatabaseObj()
                    .insert(Dbhelper.TABLE_CART, null, v);

        } catch (SQLException e) {
        } finally {
            db.close();
        }

    }

    protected int getDeelIdFromDB(String qry) {
        // TODO Auto-generated method stub

        int a = 0;
        Cursor c = null;
        try {
            c = db.open().getDatabaseObj().rawQuery(qry, null);
            if (c.getCount() > 0) {
                while (c.moveToNext()) {
                    a = Integer.parseInt(c.getString(0));
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        return a;
    }

    public static class ViewHolder {

        public TextView prname, offprodname;
        public TextView fxtext, fxedit, oftext, ofedit, bxtext, bxedit;
        public ImageView img;
        public LinearLayout iglay, oflay, flay, olay, blay;
        public Button add;

    }

    public class postUpdate extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        protected void onPreExecute() {
            // TODO Auto-generated method stub

            super.onPreExecute();

            progressDialog = new ProgressDialog(myacActivity,
                    AlertDialog.THEME_HOLO_LIGHT);
            progressDialog.setMessage("Adding to your cart");
            progressDialog.setProgressDrawable(new ColorDrawable(
                    android.graphics.Color.BLUE));
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String result = null;
            HttpConfig ht = new HttpConfig();

            result = ht.doPostMobizee(data, KachingMeConfig.CART_URL);

            return result;
        }

        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            progressDialog.dismiss();

            if (result != null && result.length() > 0) {

                if (result.toString().trim().equals("true")) {

                    Update();

                } else {

                    Toast.makeText(mContext, "Try Again Later",
                            Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(mContext,
                        "Network Error!Please try again later!",
                        Toast.LENGTH_SHORT).show();
            }

        }
    }

}