package com.wifin.kachingme.kaching_feature.nynms;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.l4digital.fastscroll.FastScroller;
import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.util.Constant;

import java.util.List;

/**
 * Created by Wifin on 12/10/2016.
 */

public class NynmAdapter extends RecyclerView.Adapter<NynmAdapter.NynmViewHolder>
        implements FastScroller.SectionIndexer{
    List<String> nynmMessage;
    Context contextNynm;
    String TAG = NynmAdapter.class.getSimpleName();
    NynmActivity mNymActivity;
    public NynmAdapter(){
        Log.e(TAG, "Adapter Started");
    }

    public NynmAdapter(Context contextNynm, List<String> nynmMessage)
    {
        this.contextNynm = contextNynm;
        this.nynmMessage = nynmMessage;
        mNymActivity =(NynmActivity) contextNynm;
    }

    @Override
    public NynmViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View nynmView = LayoutInflater.from(parent.getContext()).inflate(R.layout.nynm_card_item, parent, false);
        NynmViewHolder nynmViewHolder = new NynmViewHolder(nynmView);
        return nynmViewHolder;
    }

    @Override
    public void onBindViewHolder(NynmViewHolder holder, int position){
        holder.tvAdaptNynmMessage.setText(nynmMessage.get(position));
    }

    @Override
    public int getItemCount(){
        return nynmMessage == null ? 0 : nynmMessage.size();
    }

    @Override
    public String getSectionText(int pos) {
        if (pos < 0 || pos >= nynmMessage.size())
            return null;

        String name = nynmMessage.get(pos);
        if (name == null || name.length() < 1)
            return null;

        return nynmMessage.get(pos).substring(0, 1);
    }

    class NynmViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvAdaptNynmMessage;
        LinearLayout linearAdaptNynmMessage;
        CardView cardAdaptNynmList;

        public NynmViewHolder(View itemView){
            super(itemView);

            tvAdaptNynmMessage = (TextView) itemView.findViewById(R.id.tvAdaptNynmMessage);
            linearAdaptNynmMessage = (LinearLayout) itemView.findViewById(R.id.linearAdaptNynmMessage);
            cardAdaptNynmList = (CardView) itemView.findViewById(R.id.cardAdaptNynmList);

            Constant.typeFace(contextNynm,tvAdaptNynmMessage);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            mNymActivity.onClick(v,getAdapterPosition());
//            String value = parent.getItemAtPosition(position).toString();
//            int idNym = ((Integer) NynmActivity.this.idNymText
//                    .get(position)).intValue();
//            Constant.NymPosition = idNym;
//            NynmActivity.this
//                    .queryPosition("SELECT * FROM nym WHERE id = " + idNym);
//            NynmActivity.this.initiatePopupWindow(value, idNym);
        }
    }
}
