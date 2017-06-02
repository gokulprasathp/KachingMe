package com.wifin.kachingme.chat.chat_adaptors;

import android.content.Context;
import android.graphics.Bitmap;
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
import com.wifin.kachingme.pojo.Chat_list_home_GetSet;
import com.wifin.kachingme.util.Constant;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lenovo on 14-10-2016.
 */
public class GroupChatListAdapter extends RecyclerView.Adapter<GroupChatListAdapter.ChatViewHolder> {
    List<Chat_list_home_GetSet> chatList;
    int[] userProfilePic;
    Context contextChat;
    String TAG = GroupChatListAdapter.class.getSimpleName();


    public GroupChatListAdapter(Context context, List<Chat_list_home_GetSet> chatList, boolean check) {
        this.contextChat = context;
        this.chatList = chatList;

    }

    @Override
    public GroupChatListAdapter.ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewChat = LayoutInflater.from(parent.getContext()).inflate(R.layout.userchartlist_adapter, parent, false);
        GroupChatListAdapter.ChatViewHolder chatViewHolder = new GroupChatListAdapter.ChatViewHolder(viewChat);
        return chatViewHolder;
    }

    @Override
    public void onBindViewHolder(GroupChatListAdapter.ChatViewHolder holder, int position) {
        holder.imgChatUserProfile.setImageResource(userProfilePic[position]);
//        holder.tvChatUserName.setText(chatUserName.get(position));
//        holder.tvChatUserMsg.setText(chatMessage.get(position));
//        holder.tvChatTimeStamp.setText(chatTimeStamp.get(position));
    }

    @Override
    public int getItemCount() {
        return chatList == null ? 0 : chatList.size();
    }

    class ChatViewHolder extends RecyclerView.ViewHolder {
        CardView cardChatList;
        TextView tvChatUserName, tvChatUserMsg, tvChatTimeStamp;
        CircleImageView imgChatUserProfile;
        LinearLayout linearChatList, linearChatDetails, linearChatMsg;
        ImageView imgChatStatus;
        Bitmap bitmap;
        View mView;

        public ChatViewHolder(View itemView) {
            super(itemView);
            cardChatList = (CardView) itemView.findViewById(R.id.cardChatList);
            tvChatUserName = (TextView) itemView.findViewById(R.id.tvChatUserName);
            tvChatUserMsg = (TextView) itemView.findViewById(R.id.tvChatUserMsg);
            tvChatTimeStamp = (TextView) itemView.findViewById(R.id.tvChatTimeStamp);
            imgChatUserProfile = (CircleImageView) itemView.findViewById(R.id.imgChatUserProfile);
            linearChatList = (LinearLayout) itemView.findViewById(R.id.linearChatList);
            linearChatDetails = (LinearLayout) itemView.findViewById(R.id.linearChatDetails);
            linearChatMsg = (LinearLayout) itemView.findViewById(R.id.linearChatMsg);
            imgChatStatus = (ImageView) itemView.findViewById(R.id.imgChatStatus);
            mView = (View) itemView.findViewById(R.id.userchat_view);


            try {
                Constant.typeFace(contextChat,tvChatUserName);
                Constant.typeFace(contextChat,tvChatUserMsg);
                Constant.typeFace(contextChat,tvChatTimeStamp);
            } catch (Exception e) {
                e.printStackTrace();
            }
            int height = Constant.screenHeight;
            int width = Constant.screenWidth;

            CardView.LayoutParams viewParama = new CardView.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            viewParama.width = width;
            viewParama.height = height * 1 / 2 / 100;
            viewParama.gravity = Gravity.BOTTOM;
            mView.setLayoutParams(viewParama);

            CardView.LayoutParams linearListRow = new CardView.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            linearListRow.width = width;
            linearListRow.height = height * 12 / 100;
            linearChatList.setLayoutParams(linearListRow);

            LinearLayout.LayoutParams circularImage = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            circularImage.width = width * 15 / 100;
            circularImage.height = height * 10 / 100;
            circularImage.gravity = Gravity.CENTER | Gravity.LEFT;
            circularImage.leftMargin = width * 4 / 100;
            circularImage.rightMargin = width * 4 / 100;
            imgChatUserProfile.setLayoutParams(circularImage);

            LinearLayout.LayoutParams linearDetails = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            linearDetails.width = width * 52 / 100;
            linearDetails.height = height * 12 / 100;
            linearChatDetails.setLayoutParams(linearDetails);

            LinearLayout.LayoutParams userNameParama = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            userNameParama.width = width * 52 / 100;
            userNameParama.height = height * 6 / 100;
            tvChatUserName.setLayoutParams(userNameParama);
            tvChatUserName.setGravity(Gravity.CENTER | Gravity.LEFT | Gravity.BOTTOM);
            tvChatUserName.setPadding(0, 0, 0, width * 1 / 100);

            LinearLayout.LayoutParams chatMsgParama = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            chatMsgParama.width = width * 52 / 100;
            chatMsgParama.height = height * 6 / 100;
            linearChatMsg.setLayoutParams(chatMsgParama);
            linearChatMsg.setGravity(Gravity.CENTER | Gravity.LEFT | Gravity.TOP);

            LinearLayout.LayoutParams statusImageParama = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            statusImageParama.width = width * 5 / 100;
            statusImageParama.height = width * 5 / 100;
            statusImageParama.gravity = Gravity.CENTER_VERTICAL | Gravity.CENTER_VERTICAL;
            imgChatStatus.setLayoutParams(statusImageParama);

            LinearLayout.LayoutParams msgTextParama = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            msgTextParama.width = width * 45 / 100;
            msgTextParama.height = height * 6 / 100;
            msgTextParama.gravity = Gravity.CENTER_VERTICAL | Gravity.CENTER_VERTICAL;
            tvChatUserMsg.setLayoutParams(msgTextParama);
//            tvChatUserMsg.setGravity(Gravity.CENTER | Gravity.LEFT | Gravity.TOP);
            tvChatUserMsg.setPadding(width * 1 / 100, width * 1 / 100, 0, 0);

            LinearLayout.LayoutParams rightSecondsParama = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            rightSecondsParama.width = width * 25 / 100;
            rightSecondsParama.height = height * 12 / 100;
            tvChatTimeStamp.setLayoutParams(rightSecondsParama);
            tvChatTimeStamp.setGravity(Gravity.CENTER);

            if (width >= 600) {
                tvChatUserName.setTextSize(20);
                tvChatUserMsg.setTextSize(16);
                tvChatTimeStamp.setTextSize(16);
            } else if (width > 501 && width < 600) {
                tvChatUserName.setTextSize(19);
                tvChatUserMsg.setTextSize(15);
                tvChatTimeStamp.setTextSize(15);
            } else if (width > 260 && width < 500) {

                tvChatUserName.setTextSize(18);
                tvChatUserMsg.setTextSize(14);
                tvChatTimeStamp.setTextSize(14);
            } else if (width <= 260) {
                tvChatUserName.setTextSize(17);
                tvChatUserMsg.setTextSize(13);
                tvChatTimeStamp.setTextSize(13);
            }
        }
    }

}
