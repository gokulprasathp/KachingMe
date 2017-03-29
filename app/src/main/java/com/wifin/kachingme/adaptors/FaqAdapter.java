package com.wifin.kachingme.adaptors;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.util.Constant;

import java.util.List;

public class FaqAdapter extends RecyclerView.Adapter<FaqAdapter.FaqViewHolder>
{
    List<String> listFaqContent;
    Context contextFaq;
    String TAG = FaqAdapter.class.getSimpleName();

    public FaqAdapter()
    {
        Log.e(TAG, "FAQ Adapter");
    }

    public FaqAdapter(Context contextFaq, List listFaqContent)
    {
        this.contextFaq = contextFaq;
        this.listFaqContent = listFaqContent;
    }

    @Override
    public FaqViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View faqView = LayoutInflater.from(parent.getContext()).inflate(R.layout.faq_card_item, parent, false);
        FaqViewHolder faqViewHolder = new FaqViewHolder(faqView);
        return faqViewHolder;
    }

    @Override
    public void onBindViewHolder(FaqViewHolder holder, int position)
    {
        CardView.LayoutParams usageLinear = new CardView.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        usageLinear.width = (int) Constant.width;
        usageLinear.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        holder.linearFaqContent.setLayoutParams(usageLinear);

        LinearLayout.LayoutParams linearConversationItem = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        linearConversationItem.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        linearConversationItem.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        linearConversationItem.gravity = Gravity.CENTER_VERTICAL;
        int margin = Constant.width * 2 / 100;
        int topmar = Constant.width * 3 / 100;
        linearConversationItem.setMargins(margin, topmar, margin, topmar);
        holder.linearFaqItems.setLayoutParams(linearConversationItem);

        LinearLayout.LayoutParams notifyConverText = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        notifyConverText.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        notifyConverText.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        notifyConverText.gravity = Gravity.CENTER_VERTICAL;
        holder.tvFaqContent.setLayoutParams(notifyConverText);

        if (Constant.width >= 600)
        {
            holder.tvFaqContent.setTextSize(18);
        }
        else if (Constant.width > 501 && Constant.width < 600)
        {
            holder.tvFaqContent.setTextSize(17);
        }
        else if (Constant.width > 260 && Constant.width < 500)
        {
            holder.tvFaqContent.setTextSize(16);
        }
        else if (Constant.width <= 260)
        {
            holder.tvFaqContent.setTextSize(15);
        }

        holder.tvFaqContent.setText(listFaqContent.get(position));
    }

    @Override
    public int getItemCount()
    {
        return listFaqContent == null ? 0 : listFaqContent.size();
    }

    class FaqViewHolder extends RecyclerView.ViewHolder
    {
        CardView cardFaq;
        LinearLayout linearFaqContent, linearFaqItems;
        TextView tvFaqContent;

        public FaqViewHolder(View itemView)
        {
            super(itemView);

            cardFaq = (CardView) itemView.findViewById(R.id.cardFaq);
            linearFaqContent = (LinearLayout) itemView.findViewById(R.id.linearFaqContent);
            linearFaqItems = (LinearLayout) itemView.findViewById(R.id.linearFaqItems);
            tvFaqContent = (TextView) itemView.findViewById(R.id.tvFaqContent);
            Constant.typeFace(contextFaq, tvFaqContent);

        }
    }
}
