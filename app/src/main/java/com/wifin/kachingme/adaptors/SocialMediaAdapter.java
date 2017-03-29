/*
* @author Gokul
*
* @usage -  This class is used to show available social media in mobile
*
*
* */

package com.wifin.kachingme.adaptors;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.wifin.kachingme.social_media.WebViewActivity;
import com.wifin.kachingme.util.Constant;

import java.util.List;

public class SocialMediaAdapter extends RecyclerView.Adapter<SocialMediaAdapter.socialViewHolder> {
    List<String> socialItems;
    int[] socialImages;
    Context contextSocial;

    public SocialMediaAdapter() {

    }

    public SocialMediaAdapter(Context contextSocial, List socialItems, int[] socialImages) {
        this.contextSocial = contextSocial;
        this.socialItems = socialItems;
        this.socialImages = socialImages;
    }

    @Override
    public socialViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View socialView = LayoutInflater.from(parent.getContext()).inflate(R.layout.social_card_item, parent, false);
        socialViewHolder socialViewHolder = new socialViewHolder(socialView);
        return socialViewHolder;
    }

    @Override
    public void onBindViewHolder(socialViewHolder holder, final int position) {
        CardView.LayoutParams usageLinear = new CardView.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        usageLinear.width = (int) Constant.width;
        usageLinear.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        holder.linearSocial.setLayoutParams(usageLinear);

        LinearLayout.LayoutParams linearConversationItem = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        linearConversationItem.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        linearConversationItem.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        linearConversationItem.gravity = Gravity.CENTER_VERTICAL;
        int margin = Constant.width * 2 / 100;
        int marginside = Constant.width * 3 / 100;
        linearConversationItem.setMargins(marginside, margin, marginside, margin);
        holder.linearSocialItem.setLayoutParams(linearConversationItem);

        LinearLayout.LayoutParams notifyConverText = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        notifyConverText.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        notifyConverText.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        notifyConverText.gravity = Gravity.CENTER_VERTICAL;
        notifyConverText.setMargins(margin, margin, margin, margin);
        holder.tvSocialItem.setLayoutParams(notifyConverText);

        LinearLayout.LayoutParams socialIcon = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        socialIcon.width = 8 * Constant.width / 100;
        socialIcon.height = 8 * Constant.width / 100;
        socialIcon.gravity = Gravity.CENTER_VERTICAL;
        socialIcon.setMargins(marginside, margin, marginside, margin);
        holder.imgSocialIcon.setLayoutParams(socialIcon);

        if (Constant.width >= 600) {
            holder.tvSocialItem.setTextSize(18);
        } else if (Constant.width > 501 && Constant.width < 600) {
            holder.tvSocialItem.setTextSize(17);
        } else if (Constant.width > 260 && Constant.width < 500) {
            holder.tvSocialItem.setTextSize(16);
        } else if (Constant.width <= 260) {
            holder.tvSocialItem.setTextSize(15);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemSelected(position);
            }
        });

        holder.tvSocialItem.setText(socialItems.get(position));
        holder.imgSocialIcon.setImageResource(socialImages[position]);
    }

    @Override
    public int getItemCount() {
        return socialItems == null ? 0 : socialItems.size();
    }

    public void onItemSelected(int position) {
        if (position == 0) {
            Intent mailer = new Intent(Intent.ACTION_SENDTO);
            mailer.setType("text/plain");
            mailer.setData(Uri.parse("mailto:"));
            mailer.putExtra(
                    "sms_body",
                    "Check out Kaching.Me Messanger for your smartphone.\n Download it from Google play store.");

            mailer.putExtra(Intent.EXTRA_SUBJECT, "KaChing.me");
            mailer.putExtra(
                    Intent.EXTRA_TEXT,
                    "Hey,\n\n"
                            + "I just downloaded Kaching.Me Messanger on my Android.\n\n"
                            + "It is a smartphone messanger which replaces SMS. This app event lets me send pictures,video and other multimedia.\n\n"
                            + "Kaching.Me is available for Android and there is no PIN or username to remember - it work just like SMS and uses your internet data plan.\n\n"
                            + "Get it now from Google play and say good-bye to SMS!");

            contextSocial.startActivity(Intent.createChooser(mailer, "Send email..."));
        } else if (position == 1) {
            Intent mailer = new Intent(Intent.ACTION_VIEW);
            // mailer.setType("text/plain");
            mailer.setType("vnd.android-dir/mms-sms");
            mailer.putExtra(
                    "sms_body",
                    "Hey,\n\n"
                            + "I just downloaded Kaching.Me Messanger on my Android.\n\n"
                            + "It is a smartphone messanger which replaces SMS. This app event lets me send pictures,video and other multimedia.\n\n"
                            + "Kaching.Me is available for Android and there is no PIN or username to remember - it work just like SMS and uses your internet data plan.\n\n"
                            + "Get it now from Google play and say good-bye to SMS!");

            mailer.putExtra(Intent.EXTRA_SUBJECT, "KaChing.me");

            contextSocial.startActivity(Intent.createChooser(mailer, "Send Message..."));
        } else if (position == 2) {
            Constant.fb = false;

            contextSocial.startActivity(new Intent(contextSocial, WebViewActivity.class));
        } else if (position == 3) {
            Constant.fb = true;

            contextSocial.startActivity(new Intent(contextSocial, WebViewActivity.class));
        }
    }

    class socialViewHolder extends RecyclerView.ViewHolder {
        CardView cardSocialItem;
        LinearLayout linearSocialItem, linearSocial;
        TextView tvSocialItem;
        ImageView imgSocialIcon;

        public socialViewHolder(View itemView) {
            super(itemView);

            cardSocialItem = (CardView) itemView.findViewById(R.id.cardSocialItem);
            linearSocial = (LinearLayout) itemView.findViewById(R.id.linearSocial);
            linearSocialItem = (LinearLayout) itemView.findViewById(R.id.linearSocialItem);
            tvSocialItem = (TextView) itemView.findViewById(R.id.tvSocialItem);
            imgSocialIcon = (ImageView) itemView.findViewById(R.id.imgSocialIcon);
            Constant.typeFace(contextSocial, tvSocialItem);

        }
    }
}
