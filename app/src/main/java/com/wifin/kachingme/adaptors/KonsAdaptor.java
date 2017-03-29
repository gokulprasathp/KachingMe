/*
* @author Priya
*
* @usage -  This class is used to display the list of created kons
*
*
* */

package com.wifin.kachingme.adaptors;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.kaching_feature.kons.KonsHomeScreen;
import com.wifin.kachingme.pojo.KonesCheckPojo;
import com.wifin.kachingme.pojo.KonsDto;
import com.wifin.kachingme.util.Constant;

import java.util.ArrayList;

public class KonsAdaptor extends RecyclerView.Adapter<KonsAdaptor.KonsViewHolder> {


    Activity activity;
    ArrayList<KonsDto> mListValue = new ArrayList<KonsDto>();

    Context context;
    String mState;
    KonsHomeScreen mKonsHomeScreen;
    String mShape, mColor;

    public KonsAdaptor(Activity activity, ArrayList mListValue, String state) {

        this.activity = activity;
        this.mListValue = mListValue;
        this.mState = state;
        mKonsHomeScreen = (KonsHomeScreen) activity;

    }

    @Override
    public KonsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.kons_card_item, parent, false);
        KonsViewHolder viewHolder = new KonsViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(KonsViewHolder holder, int position) {

        holder.ImageTest.setImageBitmap(mListValue.get(position).getImage());
        holder.mKonsItemTxt.setText(mListValue.get(position).getText());

        if (mState.equalsIgnoreCase("longpress")) {
            holder.mSelectImg.setVisibility(View.VISIBLE);
            KonesCheckPojo k = new KonesCheckPojo();
            k.setMessage(mListValue.get(position).getText());
            if (isAlreadyPresent(k)) {
                holder.mSelectImg.setImageResource(R.drawable.select);
            } else {
                holder.mSelectImg.setImageResource(R.drawable.deselect);

            }
        } else {
            holder.mSelectImg.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {

        if (mListValue.size() > 0) {

            return mListValue.size();
        } else {
            return 0;
        }
    }

    public boolean isAlreadyPresent(KonesCheckPojo a) {
        boolean isAvail = false;
        for (int i = 0; i < KonsHomeScreen.mKonsCheckList.size(); i++) {

            if (KonsHomeScreen.mKonsCheckList.get(i).getMessage()
                    .equalsIgnoreCase(a.getMessage())) {
                isAvail = true;
            }
        }
        return isAvail;

    }

    public void insertData(KonesCheckPojo a, boolean isChecked, int pos) {
        if (isChecked) {
            if (KonsHomeScreen.mKonsCheckList.size() > 0) {
                if (isAlreadyPresent(a)) {
                    for (int i = 0; i < KonsHomeScreen.mKonsCheckList.size(); i++) {
                        KonsHomeScreen.mKonsCheckList.remove(i);
                    }
                    KonesCheckPojo j = new KonesCheckPojo();
                    j.setMessage(a.getMessage());
                    j.setColor(mColor);
                    j.setBackground(mShape);
                    j.setChecked(isChecked);
                    KonsHomeScreen.mKonsCheckList.add(j);

                } else {
                    KonesCheckPojo j = new KonesCheckPojo();
                    j.setMessage(a.getMessage());
                    j.setColor(mColor);
                    j.setBackground(mShape);
                    j.setChecked(isChecked);
                    KonsHomeScreen.mKonsCheckList.add(j);
                }
            } else {
                KonesCheckPojo j = new KonesCheckPojo();
                j.setMessage(a.getMessage());
                j.setColor(mColor);
                j.setBackground(mShape);
                j.setChecked(isChecked);
                KonsHomeScreen.mKonsCheckList.add(j);
            }
        } else {
            if (KonsHomeScreen.mKonsCheckList.size() > 0) {
                for (int i = 0; i < KonsHomeScreen.mKonsCheckList.size(); i++) {
                    if (KonsHomeScreen.mKonsCheckList.get(i).getMessage()
                            .equalsIgnoreCase(a.getMessage())) {
                        KonsHomeScreen.mKonsCheckList.remove(i);
                    }
                }
            }
        }
    }

    class KonsViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {

        CardView mKonsCardVIew;
        FrameLayout mKonsItemLayout;
        com.wifin.kachingme.emojicons.emojicon.EmojiconTextView mKonsItemTxt;
        ImageView mSelectImg;
        ImageView ImageTest;

        public KonsViewHolder(View itemView) {
            super(itemView);

            mKonsCardVIew = (CardView) itemView.findViewById(R.id.kons_cardview);
            mKonsItemLayout = (FrameLayout) itemView.findViewById(R.id.kons_item_layout);
            mKonsItemTxt = (com.wifin.kachingme.emojicons.emojicon.EmojiconTextView) itemView.findViewById(R.id.kons_item_text);

            try {
                Constant.typeFaceKons(mKonsHomeScreen, mKonsItemTxt);
            } catch (Exception e) {
                e.printStackTrace();
            }

            ImageTest = (ImageView) itemView.findViewById(R.id.ImageTest);
            mSelectImg = (ImageView) itemView.findViewById(R.id.select_btn);
            mKonsCardVIew.setCardElevation(0);
            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);

            FrameLayout.LayoutParams mCardViewParams = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT);
            mCardViewParams.width = (int) (Constant.screenWidth * 33.33 / 100);
            mCardViewParams.height = (int) Constant.screenHeight * 14 / 100;
            mCardViewParams.gravity = Gravity.CENTER;
            mKonsCardVIew.setLayoutParams(mCardViewParams);

            FrameLayout.LayoutParams mkonsParams = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT);
            mkonsParams.width = (int) Constant.screenWidth * 33 / 100;
            mkonsParams.height = (int) Constant.screenHeight * 10 / 100;
            mkonsParams.gravity = Gravity.CENTER;
            mKonsItemLayout.setLayoutParams(mkonsParams);

            FrameLayout.LayoutParams mkonsImgParams = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT);
            mkonsImgParams.width = (int) Constant.screenWidth * 30 / 100;
            mkonsImgParams.height = (int) Constant.screenHeight * 10 / 100;
            mkonsImgParams.gravity = Gravity.CENTER;
            ImageTest.setLayoutParams(mkonsImgParams);

            FrameLayout.LayoutParams imgParama = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT);
            imgParama.width = Constant.screenWidth * 7 / 100;
            imgParama.height = Constant.screenWidth * 7 / 100;
            imgParama.gravity = Gravity.TOP | Gravity.RIGHT;
            mSelectImg.setLayoutParams(imgParama);

            if (mState.equalsIgnoreCase("longpress")) {
                mSelectImg.setVisibility(View.VISIBLE);
            } else {
                mSelectImg.setVisibility(View.GONE);
            }

            if (Constant.mDelete == true) {
                mSelectImg.setVisibility(View.VISIBLE);
            }

            if (Constant.screenWidth >= 600) {

                mKonsItemTxt.setTextSize(21);
                mKonsItemTxt.setEmojiconSize(40);

            } else if (Constant.screenWidth > 501 && Constant.screenWidth < 600) {

                mKonsItemTxt.setTextSize(19);
                mKonsItemTxt.setEmojiconSize(39);

            } else if (Constant.screenWidth > 331 && Constant.screenWidth < 500) {

                mKonsItemTxt.setTextSize(17);
                mKonsItemTxt.setEmojiconSize(38);

            } else if (Constant.screenWidth > 260 && Constant.screenWidth < 330) {

                mKonsItemTxt.setTextSize(16);
                mKonsItemTxt.setEmojiconSize(36);

            } else if (Constant.screenWidth <= 260) {

                mKonsItemTxt.setTextSize(16);
                mKonsItemTxt.setEmojiconSize(36);

            }


        }


        @Override
        public boolean onLongClick(View v) {
            Constant.mDelete = true;
            mKonsHomeScreen.onLongClick(v, getAdapterPosition());
            return true;
        }

        @Override
        public void onClick(View v) {
            if (Constant.mDelete == true) {
                if (mListValue.get(getAdapterPosition()).isChecked()) {
                    mListValue.get(getAdapterPosition()).setChecked(false);
                    mSelectImg.setImageResource(R.drawable.deselect);

                    KonesCheckPojo k = new KonesCheckPojo();
                    k.setChecked(false);
                    k.setMessage(mListValue.get(getAdapterPosition()).getText());
                    k.setBackground(mListValue.get(getAdapterPosition()).getBackground());
                    k.setColor(mListValue.get(getAdapterPosition()).getColor());

                    mShape = mListValue.get(getAdapterPosition()).getBackground().toString().trim();
                    mColor = mListValue.get(getAdapterPosition()).getColor().toString().trim();

                    insertData(k, false, getAdapterPosition());

                } else {
                    mListValue.get(getAdapterPosition()).setChecked(true);
                    mSelectImg.setImageResource(R.drawable.select);
                    mShape = mListValue.get(getAdapterPosition()).getBackground().toString().trim();
                    mColor = mListValue.get(getAdapterPosition()).getColor().toString().trim();
                    KonesCheckPojo k = new KonesCheckPojo();
                    k.setChecked(true);
                    k.setMessage(mListValue.get(getAdapterPosition()).getText());
                    insertData(k, true, getAdapterPosition());

                }

            } else {

                Toast.makeText(activity, "Long Press to select Kons",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}

