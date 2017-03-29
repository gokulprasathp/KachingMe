package com.wifin.kachingme.chat.chat_adaptors;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.pojo.MessageGetSet;

import java.util.ArrayList;

public class
        Cheking extends
        RecyclerView.Adapter<Cheking.MUCChatViewHolder> {

    ArrayList<MessageGetSet> msg_list;
    String jid;

    public Cheking(Context context, ArrayList<MessageGetSet> msg_list, String jid) {


    }

    public Cheking() {

    }

    @Override
    public MUCChatViewHolder onCreateViewHolder(
            ViewGroup parent, int arg1) {
        // TODO Auto-generated method stub


        View viewChat = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.muc_test_listitem, parent, false);
        MUCChatViewHolder chatViewHolder = new MUCChatViewHolder(
                viewChat);
        return chatViewHolder;
    }

    @Override
    public void onBindViewHolder(final MUCChatViewHolder arg0, final int arg1) {


    }


    @Override
    public int getItemCount() {

        return 0;
    }


    class MUCChatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {


        public MUCChatViewHolder(View view) {
            super(view);
            view.setTag(view);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);


        }

        @Override
        public void onClick(View v) {


        }

        @Override
        public boolean onLongClick(View v) {

            return true;
        }
    }


}
