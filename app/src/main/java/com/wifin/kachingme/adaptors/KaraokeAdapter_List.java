package com.wifin.kachingme.adaptors;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.kaching_feature.karaoke.ImageVolleyLoader;
import com.wifin.kachingme.kaching_feature.karaoke.KaraokeListActivity;
import com.wifin.kachingme.util.Constant;

import java.util.ArrayList;
import java.util.List;

public class KaraokeAdapter_List extends RecyclerView.Adapter<KaraokeAdapter_List.VersionViewHolder> {
    public static List<String> homeActivitiesList = new ArrayList<>();
    public static List<String> homeActivitiesSubList = new ArrayList<>();
    public static String VIDEO_URL_FRONT = "http://img.youtube.com/vi/%1$s/0.jpg";
    List<KaraokeListActivity.KaraokeSongsList> versionModels;
    Boolean isHomeList = false;
    Context context;
    RelativeLayout relativeItem;
    ImageLoader mImageLoader;
    KaraokeListActivity mKaraokeActivity;

    public KaraokeAdapter_List(List<KaraokeListActivity.KaraokeSongsList> versionModels, Context context) {
        this.context = context;
        isHomeList = false;
        this.versionModels = versionModels;
        mKaraokeActivity = (KaraokeListActivity) context;
    }

    @Override
    public VersionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.karaoke_card_item, viewGroup, false);
        VersionViewHolder viewHolder = new VersionViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(VersionViewHolder versionViewHolder, final int i) {
        if (isHomeList) {
            versionViewHolder.title.setText(homeActivitiesList.get(i));
            versionViewHolder.subTitle.setText(homeActivitiesSubList.get(i));
        } else {
            versionViewHolder.title.setText(versionModels.get(i).getSongName());
            mImageLoader = ImageVolleyLoader.getInstance(context)
                    .getImageLoader();
            //Image URL - This can point to any image file supported by Android
            mImageLoader.get(String.format(VIDEO_URL_FRONT, versionModels.get(i).getUrl()), ImageLoader.getImageListener(versionViewHolder.imgView,
                    R.drawable.stub, R.drawable
                            .stub));
            versionViewHolder.imgView.setImageUrl(String.format(VIDEO_URL_FRONT, versionModels.get(i).getUrl()), mImageLoader);
        }
        versionViewHolder.cardItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mKaraokeActivity.onClick(v, i);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (isHomeList)
            return homeActivitiesList == null ? 0 : homeActivitiesList.size();
        else
            return versionModels == null ? 0 : versionModels.size();
    }

    public void mScreenArrangement(VersionViewHolder vrsHolder) {
        double height = Constant.height;
        double width =  Constant.width;

        CardView.LayoutParams rel_params = new CardView.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        rel_params.height = (int) Constant.height * 14 / 100;
        relativeItem.setPadding(2, 2, 2, 2);
        relativeItem.setLayoutParams(rel_params);

        RelativeLayout.LayoutParams linear_img_params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        linear_img_params.height = (int) height * 50 / 100;
        linear_img_params.width = (int) width * 20 / 100;
        linear_img_params.leftMargin = (int) width * 1 / 100;
        linear_img_params.topMargin = (int) width * 1 / 100;
        linear_img_params.bottomMargin = (int) width * 1 / 100;

        vrsHolder.linearLayout_img.setLayoutParams(linear_img_params);
        vrsHolder.linearLayout_img.setPadding(1, 1, 1, 1);

        vrsHolder.title.setPadding(20, 3, 0, 2);
        vrsHolder.subTitle.setPadding(20, 2, 0, 0);


        if (width >= 750) {
            vrsHolder.title.setTextSize(20);

        } else if (width > 600 && width <= 750) {
            vrsHolder.title.setTextSize(17);

        } else if (width < 600 && width >= 480) {
            vrsHolder.title.setTextSize(15);

        } else if (width < 480 && width >= 320) {
            vrsHolder.title.setTextSize(13);

        } else if (width < 320) {
            vrsHolder.title.setTextSize(11);

        }
    }

    class VersionViewHolder extends RecyclerView.ViewHolder {
        CardView cardItemLayout;
        TextView title;
        TextView subTitle;
        NetworkImageView imgView;
        LinearLayout linearLayout_img;

        public VersionViewHolder(View itemView) {
            super(itemView);

            relativeItem = (RelativeLayout) itemView.findViewById(R.id.karaoke_relativeHeader);
            cardItemLayout = (CardView) itemView.findViewById(R.id.cardlist_item);
            title = (TextView) itemView.findViewById(R.id.title);
            subTitle = (TextView) itemView.findViewById(R.id.artist);
            imgView = (NetworkImageView) itemView.findViewById(R.id.list_image);
            linearLayout_img = (LinearLayout) itemView.findViewById(R.id.thumbnail);

            Constant.typeFace(context, title);
            Constant.typeFace(context, subTitle);

            mScreenArrangement(this);
        }
    }
}