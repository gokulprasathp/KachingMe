/*
* @author Rajsekar
*
* @usage -  This class is used to display app setting options
*
*
* */


package com.wifin.kachingme.adaptors;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.settings.AppInfo;
import com.wifin.kachingme.settings.NotificationSettings;
import com.wifin.kachingme.settings.UsageActivity;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.NetworkUtil;

import java.util.List;

public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.SettingViewHolder> {
    List<String> listSettingContent;
    int[] listSettingIcon;
    Context contextSettings;

    public SettingAdapter() {

    }

    public SettingAdapter(Context contextSettings, List listSettingContent, int[] listSettingIcon) {
        this.contextSettings = contextSettings;
        this.listSettingContent = listSettingContent;
        this.listSettingIcon = listSettingIcon;
    }

    @Override
    public SettingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View settingView = LayoutInflater.from(parent.getContext()).inflate(R.layout.setting_card_item, parent, false);
        SettingViewHolder settingViewHolder = new SettingViewHolder(settingView);
        return settingViewHolder;
    }

    @Override
    public void onBindViewHolder(final SettingViewHolder holder, final int position) {
        CardView.LayoutParams usageLinear = new CardView.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        usageLinear.width = (int) Constant.width;
        usageLinear.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        holder.linearSettingAdapt.setLayoutParams(usageLinear);

        LinearLayout.LayoutParams linearConversationItem = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        linearConversationItem.width = LinearLayout.LayoutParams.MATCH_PARENT;
        linearConversationItem.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        linearConversationItem.gravity = Gravity.CENTER_VERTICAL;
        int margin = Constant.width * 1 / 100;
        int marginside = Constant.width * 3 / 100;
        linearConversationItem.setMargins(marginside, margin, marginside, margin);
        holder.linearSettingAdaptItem.setLayoutParams(linearConversationItem);

        LinearLayout.LayoutParams notifyConverText = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        notifyConverText.width = LinearLayout.LayoutParams.MATCH_PARENT;
        notifyConverText.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        notifyConverText.gravity = Gravity.CENTER | Gravity.CENTER_VERTICAL;
        notifyConverText.setMargins(margin, margin, margin, margin);
        holder.tvSettingContent.setLayoutParams(notifyConverText);

        LinearLayout.LayoutParams socialIcon = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        socialIcon.width = 10 * Constant.width / 100;
        socialIcon.height = 10 * Constant.width / 100;
        socialIcon.gravity = Gravity.CENTER_VERTICAL;
        socialIcon.setMargins(marginside, margin, marginside, margin);
        holder.imgSettingIcon.setLayoutParams(socialIcon);

        if (Constant.width >= 600) {
            holder.tvSettingContent.setTextSize(18);
        } else if (Constant.width > 501 && Constant.width < 600) {
            holder.tvSettingContent.setTextSize(17);
        } else if (Constant.width > 260 && Constant.width < 500) {
            holder.tvSettingContent.setTextSize(16);
        } else if (Constant.width <= 260) {
            holder.tvSettingContent.setTextSize(15);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSettingAdapterItemClick(position);
            }
        });

        holder.tvSettingContent.setText(listSettingContent.get(position));

        holder.imgSettingIcon.setImageResource(listSettingIcon[position]);

        if (position == 4) {
            if (NetworkUtil.getConnectivityStatusString(contextSettings) == true) {
                holder.tvSettingContent.setText("Network Status - Connected");
            } else {
                holder.tvSettingContent.setText("Network Status - Not Connected");
            }
        }
    }

    @Override
    public int getItemCount() {
        return listSettingContent == null ? 0 : listSettingContent.size();
    }

    public void onSettingAdapterItemClick(final int position) {
        if (position == 0) {
            // App Info (or) About Screen
            contextSettings.startActivity(new Intent(contextSettings, AppInfo.class));
            ((Activity) contextSettings).finish();
        } else if (position == 1) {
            // User Account
            contextSettings.startActivity(new Intent(contextSettings, UsageActivity.class));
            ((Activity) contextSettings).finish();
        } else if (position == 2) {
            // Chat Settings
            contextSettings.startActivity(new Intent(contextSettings, NotificationSettings.class).putExtra("TAG", "Chat"));
            ((Activity) contextSettings).finish();
        } else if (position == 3) {
            // Notification/
            contextSettings.startActivity(new Intent(contextSettings, NotificationSettings.class).putExtra("TAG", "Notify"));
            ((Activity) contextSettings).finish();
        } else if (position == 4) {
            // Network Status
        } else {
        }
    }

    class SettingViewHolder extends RecyclerView.ViewHolder {
        CardView cardSettingContent;
        LinearLayout linearSettingAdapt, linearSettingAdaptItem;
        TextView tvSettingContent;
        ImageView imgSettingIcon;

        public SettingViewHolder(View itemView) {
            super(itemView);
            cardSettingContent = (CardView) itemView.findViewById(R.id.cardSettingContent);
            linearSettingAdapt = (LinearLayout) itemView.findViewById(R.id.linearSettingAdapt);
            linearSettingAdaptItem = (LinearLayout) itemView.findViewById(R.id.linearSettingAdaptItem);
            tvSettingContent = (TextView) itemView.findViewById(R.id.tvSettingTitle);
            imgSettingIcon = (ImageView) itemView.findViewById(R.id.imgSettingIcon);
            Constant.typeFace(contextSettings, tvSettingContent);
        }
    }
}
