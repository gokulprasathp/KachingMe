/*
* @author Sivanesan
*
* @usage -  This class is used to display the cart list items
*
*
* */

package com.wifin.kachingme.adaptors;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.cart.CartActivity;
import com.wifin.kachingme.database.Dbhelper;
import com.wifin.kachingme.pojo.CartAdapDto;
import com.wifin.kachingme.registration_and_login.FreebieActivity;
import com.wifin.kachingme.util.Constant;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    Activity myacActivity;
    Context mContext = CartActivity.getContext();
    List<CartAdapDto> cartlist = new ArrayList<>();
    int pos;
    Dbhelper db;
    SharedPreferences sp;
    boolean insert = false;
    private ImageLoadingListener animateFirstListener = new FreebieActivity.AnimateFirstDisplayListener();
    private DisplayImageOptions options;

    public CartAdapter(Activity act, List<CartAdapDto> crlist) {
        // TODO Auto-generated constructor stub
        myacActivity = act;
        cartlist = crlist;

        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(mContext));

        inflater = (LayoutInflater) myacActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.stub)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.stub).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true).build();

        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(mContext));

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
        return position;
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
        vi = inflater.inflate(R.layout.cart_list_item, null);
        holder = new ViewHolder();

        holder.tvCartMerchantName = (TextView) vi.findViewById(R.id.tvCartMerchantName);
        holder.tvCartProductName = (TextView) vi.findViewById(R.id.tvCartProductName);
        holder.tvCartProductRegPrice = (TextView) vi.findViewById(R.id.tvCartProductRegPrice);
        holder.tvCartProductOffer = (TextView) vi.findViewById(R.id.tvCartProductOffer);
        holder.tvCartBuxsPrice = (TextView) vi.findViewById(R.id.tvCartBuxsPrice);

        holder.imgCartMerchant = (ImageView) vi.findViewById(R.id.imgCartMerchant);
        holder.imgCartBuxsProductImage = (ImageView) vi.findViewById(R.id.imgCartBuxsProductImage);

        holder.linearCartMerchant = (LinearLayout) vi.findViewById(R.id.linearCartMerchant);
        holder.linearCartProduct = (LinearLayout) vi.findViewById(R.id.linearCartProduct);
        holder.linearCartBuxs = (LinearLayout) vi.findViewById(R.id.linearCartBuxs);
        holder.mView1 = (View) vi.findViewById(R.id.cart_view1);
        holder.mView2 = (View) vi.findViewById(R.id.cart_view2);

        holder.imgCartBuxsProductImage.setTag(position);
        holder.imgCartMerchant.setTag(position);

        Constant.typeFace(myacActivity, holder.tvCartMerchantName);
        Constant.typeFace(myacActivity, holder.tvCartProductName);
        Constant.typeFace(myacActivity, holder.tvCartProductRegPrice);
        Constant.typeFace(myacActivity, holder.tvCartProductOffer);
        Constant.typeFace(myacActivity, holder.tvCartBuxsPrice);
        screenArrangeCard(holder);
        vi.setTag(holder);
        try {
            String merchantName = cartlist.get(position).getCompanyname();
            if (merchantName != null && !merchantName.isEmpty()) {
                String nameMerchantDisplay = merchantName.replace("Welcome to ", "");
                holder.tvCartMerchantName.setText(nameMerchantDisplay);
            }
            holder.tvCartProductName.setText(cartlist.get(position).getProductName());
            holder.tvCartProductRegPrice.setText("Reg.Price : " + cartlist.get(position).getOffer());
            holder.tvCartProductOffer.setText("Freebie");
            holder.tvCartBuxsPrice.setText(cartlist.get(position).getBuxs());
            ImageLoader.getInstance().displayImage(String.valueOf(cartlist.get(position).getMerphotoPath()).
                    replaceAll(" ", "%20"), holder.imgCartMerchant, options, animateFirstListener);
            ImageLoader.getInstance().displayImage(String.valueOf(cartlist.get(position).getPhotoPath()).
                    replaceAll(" ", "%20"), holder.imgCartBuxsProductImage, options, animateFirstListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vi;
    }


    public void insertDB(ContentValues v) {
        try {
            int a = (int) db.open().getDatabaseObj().insert(Dbhelper.TABLE_RET, null, v);

            System.out.println("No of inserted rows in ret details :::::::::" + a);
        } catch (SQLException e) {
            System.out.println("Sql exception in new shop details ::::::"
                    + e.toString());
        } finally {
            db.close();
        }
    }

    public void FetchRet() {

        Constant.redeemlist.clear();

        String mername = cartlist.get(pos).getMerchantName();
        String merbux = cartlist.get(pos).getBuxs();

        String tx, mn;
        int mid;

        Dbhelper db = new Dbhelper(mContext);
        Cursor c = null;
        try {
            c = db.open()
                    .getDatabaseObj()
                    .query(Dbhelper.TABLE_RET, null, null, null, null, null,
                            null);
            int txnm = c.getColumnIndex("name");
            int mnnm = c.getColumnIndex("bux");
            int it = c.getColumnIndex("id");

            System.out.println("The pending cart list in db ::::"
                    + c.getCount());

            if (c.getCount() > 0) {

                while (c.moveToNext()) {

                    tx = c.getString(txnm);
                    mn = c.getString(mnnm);
                    mid = c.getInt(it);
                    System.out.println("dbadd:nym:" + tx + "  " + mn + "  "
                            + mername + merbux);

                    if (tx != null) {

                        if (mername.equals(tx)) {

                            insert = true;

                            String bx = String.valueOf(Integer.valueOf(mn)
                                    + Integer.valueOf(merbux));

                            System.out.println("dbadd:nyminside:" + tx + "  "
                                    + mn + "  " + mername + "   " + merbux
                                    + "   " + bx);

                            ContentValues cv = new ContentValues();

                            cv.put("name", tx);
                            cv.put("bux", bx);

                            Update(tx, bx, mid, cv);

                            // insertDB(cv);

                        }
                    }

                }
            }
        } catch (SQLException e) {

            System.out.println("Sql exception in pending shop details ::::"
                    + e.toString());
        } finally {
            c.close();
            db.close();
        }

        if (insert == false) {

            ContentValues cv = new ContentValues();
            cv.put("name", cartlist.get(pos).getMerchantName());
            cv.put("bux", cartlist.get(pos).getBuxs());
            insertDB(cv);
        }
    }

    public void Update(String mer, String bx, int merid, ContentValues cv1) {
        // TODO Auto-generated method stub

        try {
            int a = (int) db.open().getDatabaseObj()
                    .update(Dbhelper.TABLE_RET, cv1, "id=" + merid, null);

            System.out.println("No of updated rows in ret details :::::::::"
                    + a);
        } catch (SQLException e) {
            System.out.println("Sql exception in new shop details ::::::"
                    + e.toString());
        } finally {
            db.close();
        }

    }

    public void DbDeleteDeel() {
        // TODO Auto-generated method stub

        try {
            int a = db
                    .open()
                    .getDatabaseObj()
                    .delete(Dbhelper.TABLE_CART,
                            "deelid="
                                    + cartlist.get(pos)
                                    .getDeelOfferDiscountId(), null);
            System.out
                    .println("No of deleted rows from deel data is ::::::::::::"
                            + a);

        } catch (SQLException e) {
            System.out
                    .println("Sql exception while deleting particular record for shop:::::"
                            + e.toString());
        } finally {
            db.close();
        }
    }

    public static class ViewHolder {
        public ImageView imgCartMerchant, imgCartBuxsProductImage;
        public TextView tvCartMerchantName, tvCartProductName, tvCartProductRegPrice, tvCartProductOffer, tvCartBuxsPrice;
        public LinearLayout  linearCartMerchant, linearCartProduct, linearCartBuxs;
        View mView1,mView2;
    }

    public void screenArrangeCard(ViewHolder holder) {
        // TODO Auto-generated method stub
        int height = Constant.screenHeight;
        int width = Constant.screenWidth;

        LinearLayout.LayoutParams companyLayoutParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        companyLayoutParama.width=width*30/100;
        companyLayoutParama.height=height*18/100;
        holder.linearCartMerchant.setLayoutParams(companyLayoutParama);
        holder.linearCartBuxs.setLayoutParams(companyLayoutParama);

        LinearLayout.LayoutParams merchantLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        merchantLayoutParams.width=width*39/100;
        merchantLayoutParams.height=height*18/100;
        holder.linearCartProduct.setLayoutParams(merchantLayoutParams);

        LinearLayout.LayoutParams merchantNameParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        merchantNameParams.width=width*30/100;
        merchantNameParams.height=height*4/100;
        merchantNameParams.topMargin=(int)(height*2/100);
        merchantNameParams.gravity=Gravity.CENTER;
        holder.tvCartMerchantName.setLayoutParams(merchantNameParams);
        holder.tvCartMerchantName.setGravity(Gravity.CENTER);
        holder.tvCartMerchantName.setPadding(width*1/2/100,0,0,width*1/2/100);

        LinearLayout.LayoutParams merchantImageParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        merchantImageParams.width=width*25/100;
        merchantImageParams.height=(int)(height*10/100);
        merchantImageParams.topMargin=(int)(width*1/100);
        merchantImageParams.bottomMargin=(int)(width*1/100);
        merchantImageParams.gravity=Gravity.CENTER;
        holder.imgCartMerchant.setLayoutParams(merchantImageParams);
        //holder.imgCartMerchant.setScaleType(ImageView.ScaleType.FIT_XY);

        LinearLayout.LayoutParams viewParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        viewParama.width=width*1/2/100;
        viewParama.gravity=Gravity.CENTER;
        holder.mView1.setLayoutParams(viewParama);
        holder.mView2.setLayoutParams(viewParama);

        LinearLayout.LayoutParams productNameParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        productNameParams.width=width*39/100;
        productNameParams.height=height*4/100;
        productNameParams.topMargin=height*2/100;
        productNameParams.gravity=Gravity.CENTER;
        holder.tvCartProductName.setLayoutParams(productNameParams);
        holder.tvCartProductName.setGravity(Gravity.CENTER|Gravity.LEFT);
        holder.tvCartProductName.setPadding(width*2/100,0,0,width*1/100);

        LinearLayout.LayoutParams productPriceParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        productPriceParams.width=width*39/100;
        productPriceParams.height=height*4/100;
        productPriceParams.topMargin=height*2/100;
        productPriceParams.gravity=Gravity.CENTER;
        holder.tvCartProductRegPrice.setLayoutParams(productPriceParams);
        holder.tvCartProductRegPrice.setGravity(Gravity.CENTER|Gravity.LEFT);
        holder.tvCartProductRegPrice.setPadding(width*2/100,0,0,width*1/100);

        LinearLayout.LayoutParams productOfferParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        productOfferParams.width=width*38/100;
        productOfferParams.height=height*4/100;
        productOfferParams.setMargins((int)(width*0.5/100),height*2/100,(int)(width*0.5/100),0);
        productOfferParams.gravity=Gravity.CENTER;
        holder.tvCartProductOffer.setLayoutParams(productOfferParams);
        holder.tvCartProductOffer.setGravity(Gravity.CENTER|Gravity.LEFT);
        holder.tvCartProductOffer.setPadding(width*1/2/100,0,0,width*1/2/100);

        LinearLayout.LayoutParams bugsImageParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        bugsImageParams.width=width*25/100;
        bugsImageParams.height=height*10/100;
        bugsImageParams.topMargin=height*2/100;
        bugsImageParams.gravity=Gravity.CENTER;
        holder.imgCartBuxsProductImage.setLayoutParams(bugsImageParams);
        //holder.imgCartBuxsProductImage.setScaleType(ImageView.ScaleType.FIT_XY);

        LinearLayout.LayoutParams bugsTxtParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        bugsTxtParams.width=width*29/100;
        bugsTxtParams.height=height*4/100;
        bugsTxtParams.setMargins((int)(width*0.5/100),height*2/100,(int)(width*0.5/100),0);
        bugsTxtParams.gravity=Gravity.CENTER;
        holder.tvCartBuxsPrice.setLayoutParams(bugsTxtParams);
        holder.tvCartBuxsPrice.setGravity(Gravity.CENTER);
        holder.tvCartBuxsPrice.setPadding(width*1/100,0,width*1/100,0);

        if (width >= 600) {
            holder.tvCartMerchantName.setTextSize(15);
            holder.tvCartProductName.setTextSize(15);
            holder.tvCartProductRegPrice.setTextSize(15);
            holder.tvCartProductOffer.setTextSize(15);
            holder.tvCartBuxsPrice.setTextSize(15);
            } else if (width > 501 && width < 600) {
            holder.tvCartMerchantName.setTextSize(13);
            holder.tvCartProductName.setTextSize(13);
            holder.tvCartProductRegPrice.setTextSize(13);
            holder.tvCartProductOffer.setTextSize(13);
            holder.tvCartBuxsPrice.setTextSize(13);
        } else if (width > 260 && width < 500) {
            holder.tvCartMerchantName.setTextSize(12);
            holder.tvCartProductName.setTextSize(12);
            holder.tvCartProductRegPrice.setTextSize(12);
            holder.tvCartProductOffer.setTextSize(12);
            holder.tvCartBuxsPrice.setTextSize(12);
        } else if (width <= 260) {
            holder.tvCartMerchantName.setTextSize(11);
            holder.tvCartProductName.setTextSize(11);
            holder.tvCartProductRegPrice.setTextSize(11);
            holder.tvCartProductOffer.setTextSize(11);
            holder.tvCartBuxsPrice.setTextSize(11);
        }
    }
}