/*
* @author Gokul
*
* @usage -  This class is used to display the chat privacy potions
*
*
* */

package com.wifin.kachingme.adaptors;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.util.Constant;

import java.util.List;

public class PrivacyAdapter extends RecyclerView.Adapter<PrivacyAdapter.PrivacyViewHolder> {
    List<String> privacyListTitle, privacyListSubs;
    Context contextPrivacy;

    public PrivacyAdapter() {
    }

    public PrivacyAdapter(Context contextPrivacy, List privacyListTitle, List privacyListSubs) {
        this.contextPrivacy = contextPrivacy;
        this.privacyListTitle = privacyListTitle;
        this.privacyListSubs = privacyListSubs;
    }

    @Override
    public PrivacyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View privacyView = LayoutInflater.from(parent.getContext()).inflate(R.layout.privacy_card_item, parent, false);
        PrivacyViewHolder privacyViewHolder = new PrivacyViewHolder(privacyView);
        return privacyViewHolder;
    }

    @Override
    public void onBindViewHolder(PrivacyViewHolder holder, final int position) {
        CardView.LayoutParams usageLinear = new CardView.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        usageLinear.width = (int) Constant.width;
        usageLinear.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        holder.linearPrivacy.setLayoutParams(usageLinear);

        LinearLayout.LayoutParams linearConversationItem = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        linearConversationItem.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        linearConversationItem.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        linearConversationItem.gravity = Gravity.CENTER_VERTICAL;
        int margin = Constant.width * 3 / 100;
        linearConversationItem.setMargins(margin, margin, margin, margin);
        holder.linearPrivacyItem.setLayoutParams(linearConversationItem);

        LinearLayout.LayoutParams notifyConverText = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        notifyConverText.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        notifyConverText.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        notifyConverText.gravity = Gravity.CENTER_VERTICAL;
        holder.tvPrivacyTitle.setLayoutParams(notifyConverText);
        holder.tvPrivacySubs.setLayoutParams(notifyConverText);

        if (Constant.width >= 600) {
            holder.tvPrivacyTitle.setTextSize(18);
            holder.tvPrivacySubs.setTextSize(18);
        } else if (Constant.width > 501 && Constant.width < 600) {
            holder.tvPrivacyTitle.setTextSize(17);
            holder.tvPrivacySubs.setTextSize(17);
        } else if (Constant.width > 260 && Constant.width < 500) {
            holder.tvPrivacyTitle.setTextSize(16);
            holder.tvPrivacySubs.setTextSize(16);
        } else if (Constant.width <= 260) {
            holder.tvPrivacyTitle.setTextSize(15);
            holder.tvPrivacySubs.setTextSize(15);
        }

        holder.tvPrivacyTitle.setText(privacyListTitle.get(position));
        holder.tvPrivacySubs.setText(privacyListSubs.get(position));
    }

    @Override
    public int getItemCount() {
        return privacyListTitle == null ? 0 : privacyListTitle.size();
    }

    class PrivacyViewHolder extends RecyclerView.ViewHolder {
        CardView cardPrivacyList;
        LinearLayout linearPrivacyItem, linearPrivacy;
        TextView tvPrivacyTitle, tvPrivacySubs;

        public PrivacyViewHolder(View itemView) {
            super(itemView);

            cardPrivacyList = (CardView) itemView.findViewById(R.id.cardPrivacyList);
            linearPrivacy = (LinearLayout) itemView.findViewById(R.id.linearPrivacy);
            linearPrivacyItem = (LinearLayout) itemView.findViewById(R.id.linearPrivacyItem);
            tvPrivacyTitle = (TextView) itemView.findViewById(R.id.tvPrivacyTitle);
            tvPrivacySubs = (TextView) itemView.findViewById(R.id.tvPrivacySubs);

            Constant.typeFace(contextPrivacy, tvPrivacyTitle);
            Constant.typeFace(contextPrivacy, tvPrivacySubs);

        }
    }
}
