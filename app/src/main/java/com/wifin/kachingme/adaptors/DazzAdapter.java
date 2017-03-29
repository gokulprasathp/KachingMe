/*
* @author Priya
*
* @usage -  This class is used to display the list of Plain dazz
*
*
* */

package com.wifin.kachingme.adaptors;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.kaching_feature.dazz.DazzActivity;
import com.wifin.kachingme.util.Constant;

import java.util.ArrayList;
import java.util.List;


public class DazzAdapter extends RecyclerView.Adapter<DazzAdapter.DazzViewHolder> {
    List<String> listDazzValues = new ArrayList();
    Context contextDazz;
    DazzActivity mDazzActivity;

    public DazzAdapter() {
    }

    public DazzAdapter(Context contextDazz, List<String> listDazzValues) {
        this.contextDazz = contextDazz;
        this.listDazzValues = listDazzValues;
        mDazzActivity = new DazzActivity();
    }

    @Override
    public DazzViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewDazz = LayoutInflater.from(parent.getContext()).inflate(R.layout.dazz_card_item, parent, false);
        DazzViewHolder dazzViewHolder = new DazzViewHolder(viewDazz);
        return dazzViewHolder;
    }

    @Override
    public void onBindViewHolder(DazzViewHolder holder, int position) {
        holder.tvAdaptDazzMsg.setText(listDazzValues.get(position));
        Constant.typeFace(contextDazz, holder.tvAdaptDazzMsg);
    }

    @Override
    public int getItemCount() {
        return listDazzValues == null ? 0 : listDazzValues.size();
    }

    class DazzViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView tvAdaptDazzMsg;
        LinearLayout linearAdaptDazz;

        public DazzViewHolder(View itemView) {
            super(itemView);
            tvAdaptDazzMsg = (TextView) itemView.findViewById(R.id.tvAdaptDazzMsg);
            linearAdaptDazz = (LinearLayout) itemView.findViewById(R.id.linearAdaptDazz);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mDazzActivity.onClick(v, getAdapterPosition(), listDazzValues.get(getAdapterPosition()), contextDazz);
        }

        @Override
        public boolean onLongClick(View v) {
            mDazzActivity.onLongClick(v, getAdapterPosition(), listDazzValues.get(getAdapterPosition()), contextDazz);
            return true;
        }
    }
}
