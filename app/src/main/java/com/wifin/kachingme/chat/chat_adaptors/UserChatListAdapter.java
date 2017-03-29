package com.wifin.kachingme.chat.chat_adaptors;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.chat.broadcast_chat.BroadCastTest;
import com.wifin.kachingme.chat.muc_chat.MUCTest;
import com.wifin.kachingme.chat.single_chat.ChatTest;
import com.wifin.kachingme.chat_home.SliderTesting;
import com.wifin.kachingme.database.Dbhelper;
import com.wifin.kachingme.fragments.GroupChatList;
import com.wifin.kachingme.fragments.UserChatList;
import com.wifin.kachingme.pojo.Chat_list_home_GetSet;
import com.wifin.kachingme.util.AlertManager;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.Emoji;
import com.wifin.kachingme.util.Log;
import com.wifin.kachingme.util.ProfileRoundImg;
import com.wifin.kachingme.util.RoundImage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lenovo on 14-10-2016.
 */
public class UserChatListAdapter extends RecyclerView.Adapter<UserChatListAdapter.ChatViewHolder> {
    Context context;
    String TAG = UserChatListAdapter.class.getSimpleName();
    SharedPreferences sharedPreference;
    SharedPreferences.Editor editor;
    List<Chat_list_home_GetSet> chatList;
    String status_lock = "check";
    Dbhelper dbHelper;
    String Normallist = "";
    int totalcount = 1;
    ArrayList<String> chat_count = new ArrayList<String>();
    String strChatAndGroup;
    int mPosition;
    Bitmap mProfileIcon = null;

    public UserChatListAdapter(Context context, ArrayList<Chat_list_home_GetSet> chatList, boolean autoRequery, String strChat) {
        this.context = context;
        this.chatList = chatList;
        strChatAndGroup = strChat;
        sharedPreference = context.getSharedPreferences(
                KachingMeApplication.getPereference_label(),
                Activity.MODE_PRIVATE);
        editor = sharedPreference.edit();
        dbHelper = new Dbhelper(context);
        UserChatList.mTempProfileImagesList = new ArrayList<Bitmap>();
        //   UserChatList.mProfileImagesList = UserChatList.mTempProfileImagesList;
        GroupChatList.mTempProfileImagesList = new ArrayList<Bitmap>();
    }

    public UserChatListAdapter() {

    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewChat = LayoutInflater.from(parent.getContext()).inflate(R.layout.userchartlist_adapter, parent, false);
        ChatViewHolder chatViewHolder = new ChatViewHolder(viewChat);
        Constant.printMsg("siva inside.......onCreateViewHolder.....");

        return chatViewHolder;
    }

    @Override
    public void onBindViewHolder(final ChatViewHolder holder, final int position) {
        mPosition = position;

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    if (strChatAndGroup.equalsIgnoreCase("chat")) {
                        itemChatSelectionProcess(position);
                    } else {

                        if (Constant.mDesTFromSlider) {
                            Toast.makeText(context, "Sorry you can't send DesT in group", Toast.LENGTH_LONG).show();
                        } else {
                            itemGroupSelectionProcess(position);
                        }


                    }

                } catch (Exception e) {

                }
            }
        });

        try {
            byte image_data[] = chatList.get(position).getPhoto_ts();
            if (chatList.get(position).getJidId().split("@")[0].length() > 15) {
                System.gc();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = false;
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                options.inDither = true;
                holder.bitmap = BitmapFactory
                        .decodeFile(KachingMeApplication.PROFILE_PIC_DIR
                                + chatList.get(position).getJidId().toString().split("@")[0]
                                + ".png", options);
                RoundImage mSenderImage = new RoundImage(holder.bitmap);
                holder.imgChatUserProfile.setImageDrawable(mSenderImage);

//                holder.imgChatUserProfile.setImageBitmap(holder.bitmap);

                mProfileIcon = holder.bitmap;

//                UserChatList.mTempProfileImagesList.add(holder.bitmap);
//                GroupChatList.mProfileImagesList.add(holder.bitmap);
            } else {
                if (image_data != null) {

                    Bitmap bmp = null;

                    System.gc();
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = false;
                    options.inPreferredConfig = Bitmap.Config.RGB_565;
                    options.inDither = true;
                    options.inSampleSize = 2;
                    try {
                        bmp = BitmapFactory.decodeByteArray(image_data,
                                0, image_data.length, options);
                        ProfileRoundImg mSenderImage = new ProfileRoundImg(bmp);
                        holder.imgChatUserProfile.setImageDrawable(mSenderImage);

                    } catch (OutOfMemoryError e) {
                        android.util.Log.e("Map", "Tile Loader (241) Out Of Memory Error " + e.getLocalizedMessage());
                        System.gc();
                    } catch (Exception e) {

                    }

                    //holder.imgChatUserProfile.setImageBitmap(bmp);
                    mProfileIcon = bmp;
//                    if (strChatAndGroup.equalsIgnoreCase("chat")) {
//                        UserChatList.mTempProfileImagesList.add(bmp);
//                    } else {
//                        GroupChatList.mProfileImagesList.add(bmp);
//                    }
                } else if (sharedPreference.contains(chatList.get(position).getJidId())) {
                    holder.imgChatUserProfile.setImageDrawable(context
                            .getResources().getDrawable(
                                    R.drawable.ic_broadcast));
                    Bitmap mTempIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_broadcast);

                    mProfileIcon = mTempIcon;

//                    if (strChatAndGroup.equalsIgnoreCase("chat")) {
//                        UserChatList.mTempProfileImagesList.add(mTempIcon);
//                    } else {
//                        GroupChatList.mProfileImagesList.add(mTempIcon);
//                    }

                } else {
                    holder.imgChatUserProfile.setImageDrawable(context
                            .getResources().getDrawable(R.drawable.contact_profile));
                    Bitmap mTempIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher2);
                    mProfileIcon = mTempIcon;
//                    if (strChatAndGroup.equalsIgnoreCase("chat")) {
//                        UserChatList.mTempProfileImagesList.add(mTempIcon);
//                    } else {
//                        GroupChatList.mProfileImagesList.add(mTempIcon);
//                    }

                }
            }

            if (strChatAndGroup.equalsIgnoreCase("chat")) {
                UserChatList.mTempProfileImagesList.add(mProfileIcon);
                UserChatList.mProfileImagesList = UserChatList.mTempProfileImagesList;
            } else {
                GroupChatList.mTempProfileImagesList.add(mProfileIcon);
                GroupChatList.mProfileImagesList = GroupChatList.mTempProfileImagesList;
            }
        } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
            // TODO: handle exception
        }
        String query = "";
        try {

            query = "select status from " + Dbhelper.TABLE_LOCK
                    + " where jid = '" + chatList.get(position).getJidId() + "'";
            Constant.printMsg("bhaththam " + query);
            lock_status(query);
            Constant.printMsg("bindview status ::>>> " + status_lock);
            Constant.printMsg("name:::::>>>" + chatList.get(position).getDisplay_name());
            holder.tvChatUserName.setText(chatList.get(position).getDisplay_name());

            long date_data = chatList.get(position).getTimestamp();

            Date d = new Date(System.currentTimeMillis()
                    - (1000 * 60 * 60 * 24));
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sdf_time = new SimpleDateFormat("hh:mma");
            String yesterday = sdf.format(d);
            String today = sdf.format(new Date());
            String chat_date = sdf.format(new Date(date_data));

            if (today.equals(chat_date)) {
                holder.tvChatTimeStamp.setText(sdf_time.format(new Date(date_data)));
            } else if (chat_date.equals(yesterday)) {
                holder.tvChatTimeStamp.setText("Yesterday");
            } else {
                holder.tvChatTimeStamp.setText(chat_date);
            }

        } catch (Exception e) {
            // TODO: handle exception
        }
        // totalcount = 0;

        try {
            String status = chatList.get(position).getStatus10();
            int unread_count = chatList.get(position).getUnseen_msg_count();
            int image_status = chatList.get(position).getStatus();

            Constant.printMsg("Chat user 0" + status + " " + chatList.get(position).getData());

            if (status.equals("1")) {
                String query1 = "select status from " + Dbhelper.TABLE_LOCK
                        + " where jid = '" + chatList.get(position).getJidId() + "'";
                lock_status(query1);
                if (status_lock.equalsIgnoreCase("lock")) {
                    holder.tvChatUserMsg.setVisibility(View.INVISIBLE);
                } else {
                    holder.tvChatUserMsg.setVisibility(View.VISIBLE);
                    holder.tvChatUserMsg.setText(context.getResources().getString(R.string.image));
                }
            } else if (status.equals("2")) {
                String query2 = "select status from " + Dbhelper.TABLE_LOCK
                        + " where jid = '" + chatList.get(position).getJidId() + "'";
                lock_status(query);
                if (status_lock.equalsIgnoreCase("lock")) {

                    holder.tvChatUserMsg.setVisibility(View.INVISIBLE);
                } else {
                    holder.tvChatUserMsg.setVisibility(View.VISIBLE);
                    holder.tvChatUserMsg.setText(context.getResources().getString(R.string.video));
                }
            } else if (status.equals("3")) {
                Constant.printMsg("called2::::::::"
                        + context.getResources().getString(R.string.audio));
                if (status_lock.equalsIgnoreCase("lock")) {

                    holder.tvChatUserMsg.setVisibility(View.INVISIBLE);
                } else {
                    holder.tvChatUserMsg.setVisibility(View.VISIBLE);
                    holder.tvChatUserMsg.setText(context.getResources().getString(R.string.audio));
                }
            } else if (status.equals("4")) {
                Constant.printMsg("called3::::::::"
                        + context.getResources().getString(R.string.location));
                if (status_lock.equalsIgnoreCase("lock")) {

                    holder.tvChatUserMsg.setVisibility(View.INVISIBLE);
                } else {
                    holder.tvChatUserMsg.setVisibility(View.VISIBLE);
                    holder.tvChatUserMsg.setText(context.getResources().getString(R.string.location));
                }
            } else if (status.equals("5")) {
                Constant.printMsg("called4::::::::"
                        + context.getResources().getString(R.string.contact));
                if (status_lock.equalsIgnoreCase("lock")) {
                    holder.tvChatUserMsg.setVisibility(View.INVISIBLE);
                } else {
                    holder.tvChatUserMsg.setVisibility(View.VISIBLE);
                    holder.tvChatUserMsg.setText(context.getResources().getString(R.string.contact));
                }
            } else if (status.equals("6")) {
                Constant.printMsg("called5::::::::"
                        + context.getResources().getString(R.string.file));
                if (status_lock.equalsIgnoreCase("lock")) {
                    holder.tvChatUserMsg.setVisibility(View.INVISIBLE);
                } else {
                    holder.tvChatUserMsg.setVisibility(View.VISIBLE);
                    holder.tvChatUserMsg.setText(context.getResources().getString(R.string.file));
                }
            } else if (status.equals("9")) {
                if (status_lock.equalsIgnoreCase("lock")) {
                    holder.tvChatUserMsg.setVisibility(View.INVISIBLE);
                } else {
                    holder.tvChatUserMsg.setVisibility(View.VISIBLE);

                    holder.tvChatUserMsg.setText("  ");
                }

            } else if (status.equals("11")) {
                if (status_lock.equalsIgnoreCase("lock")) {
                    holder.tvChatUserMsg.setVisibility(View.INVISIBLE);
                } else {
                    holder.tvChatUserMsg.setVisibility(View.VISIBLE);

                    Constant.printMsg("dkffwsajdasd" + chatList.get(position).getData());

                    holder.tvChatUserMsg.setText(context.getResources().getString(R.string.profile_picture));
                }
            } else if (status.equals("40")) { // clearData msg
                if (status_lock.equalsIgnoreCase("lock")) {
                    holder.tvChatUserMsg.setVisibility(View.INVISIBLE);
                } else {
                    holder.tvChatUserMsg.setVisibility(View.VISIBLE);

                    holder.tvChatUserMsg.setText(" ");
                    holder.tvChatTimeStamp.setText("");
                }
            } else {

                Spannable text = new Emoji(context).getSmiledText_List(chatList.get(position).getData().toString());
                Constant.printMsg("called8::::::::" + text.toString());
                if (status_lock.equalsIgnoreCase("lock")) {
                    holder.tvChatUserMsg.setVisibility(View.INVISIBLE);
                } else {
                    holder.tvChatUserMsg.setVisibility(View.VISIBLE);

                    if (text.length() > 3) {
                        char s = text.charAt(0);
                        char s1 = text.charAt(1);
                        char s2 = text.charAt(2);
                        Constant.printMsg("text  " + text);
                        if (s == '<') {
                            if (s1 == '-') {
                                myMethod(text.toString().substring(2));
                                holder.tvChatUserMsg.setText(
                                        addClickablePart(Normallist),
                                        TextView.BufferType.SPANNABLE);
                            }
                            if (s1 == 'b' && s2 == '>') {
                                holder.tvChatUserMsg.setText(text.toString()
                                        .substring(3));
                                holder.tvChatUserMsg
                                        .setTypeface(null, Color.BLACK);
                            } else if (s1 == 'z' && s2 == '>') {
                                holder.tvChatUserMsg.setText("DazZ");
                                holder.tvChatUserMsg
                                        .setTypeface(null, Color.BLACK);
                            } else if (s1 == 's' && s2 == '>') {
                                holder.tvChatUserMsg.setText("DesT");
                                holder.tvChatUserMsg
                                        .setTypeface(null, Color.BLACK);
                            } else if (s1 == 'l' && s2 == '>') {
                                holder.tvChatUserMsg.setText(text.toString()
                                        .substring(3));
                                holder.tvChatUserMsg
                                        .setTypeface(null, Color.BLACK);
                            } else if (s1 == 'x' && s2 == '>') {
                                Constant.printMsg("called8:::11:::::"
                                        + text.toString());
                                holder.tvChatUserMsg.setText("DazZ");
                                holder.tvChatUserMsg
                                        .setTypeface(null, Color.BLACK);
                            } else if (s1 == 'k' && s2 == '>') {
                                Constant.printMsg("called8:::11:::::"
                                        + text.toString());
                                holder.tvChatUserMsg.setText("KonS");
                            } else if (s1 == 'd' && s2 == '>') {
                                holder.tvChatUserMsg.setText("BuxS");
                                holder.tvChatUserMsg
                                        .setTypeface(null, Color.BLACK);
                            } else if (s1 == 'a' && s2 == '>') {
                                String dazzle = text.toString().substring(3);
                                String[] parts = dazzle.split("-");
                                String part1 = parts[0];
                                holder.tvChatUserMsg.setText(part1
                                        + " BuxS accepted");
                                holder.tvChatUserMsg
                                        .setTypeface(null, Color.BLACK);
                            }
                        } else {
                            Spannable set_text = new Emoji(context)
                                    .getSmiledText_List(chatList.get(position).getData().toString());
                            String mTxet;


                            if (set_text.toString().contains("@localhost")) {
                                mTxet = set_text.toString().split("@")[0];
                                holder.tvChatUserMsg.setText("Text Message");
                            } else {

                                holder.tvChatUserMsg.setText(new Emoji(context)
                                        .getSmiledText_List(chatList.get(position).getData()));
                                Constant.printMsg("lskhlaskhdklsd11221" + "      " + chatList.get(position).getStatus());
                            }
                        }
                    } else {

                        holder.tvChatUserMsg.setText(new Emoji(context)
                                .getSmiledText_List(chatList.get(position).getData()));

                    }
                }
            }


            if (unread_count > 0) {
                if (status_lock.equalsIgnoreCase("lock")) {
                    holder.imgChatStatus.setVisibility(View.INVISIBLE);
                } else {
                    holder.imgChatStatus.setVisibility(View.VISIBLE);
                    totalcount++;
                    if (chatList.size() > 0) {
                        SliderTesting.chatc.setVisibility(View.VISIBLE);
                        SliderTesting.chatc.setText(String.valueOf(totalcount));
                    } else {
                        SliderTesting.chatc.setVisibility(View.GONE);
                    }
                    if (chat_count.size() > 0) {
                        if (chat_count.contains(chatList.get(position).getDisplay_name())) {
                        } else {
                            chat_count.add(chatList.get(position).getDisplay_name());
                        }
                    } else {
                        chat_count.add(chatList.get(position).getDisplay_name());
                    }
                    SliderTesting.chatc.setText(String.valueOf(chat_count
                            .size()));
                    holder.tvChatUserMsg.setTextColor(context.getResources()
                            .getColor(R.color.app_color_blue));
                    holder.imgChatStatus
                            .setImageResource(R.drawable.ic_action_av_play_app_color_blue);
                }
            } else {
                if (status_lock.equalsIgnoreCase("lock")) {
                    holder.imgChatStatus.setVisibility(View.INVISIBLE);
                } else {
                    holder.imgChatStatus.setVisibility(View.VISIBLE);
                    if (chatList.get(position).getKeyId() == 1) {
                        holder.imgChatStatus
                                .setImageResource(R.drawable.ic_action_av_play);
                    } else {
                        if (image_status == 3) {
                            holder.imgChatStatus
                                    .setImageResource(R.drawable.message_unsent);
                        } else if (image_status == 2) {
                            holder.imgChatStatus
                                    .setImageResource(R.drawable.receipt_from_server);
                        } else if (image_status == 1 || image_status == 0) {
                            holder.imgChatStatus
                                    .setImageResource(R.drawable.receipt_from_target);
                        } else if (image_status == -1) {
                            holder.imgChatStatus
                                    .setImageResource(R.drawable.receipt_read);
                        }else if (image_status == 5) {
                            holder.imgChatStatus
                                    .setVisibility(View.INVISIBLE);
                        }

                        if (status.equalsIgnoreCase("9"))
                            holder.imgChatStatus
                                    .setVisibility(View.GONE);

                        if (status.equalsIgnoreCase("40"))
                            holder.imgChatStatus
                                    .setVisibility(View.GONE);


                    }
                }
                holder.tvChatUserMsg.setTextColor(context.getResources()
                        .getColor(R.color.app_color_dark_gray));
            }
            if (UserChatList.isChatTypingJid != null) {
                if (UserChatList.isChatTypingJid.equalsIgnoreCase(chatList.get(position).getJidId())) {
                    holder.tvChatUserMsg.setText(UserChatList.isChatTypingStatus);
                    holder.tvChatUserMsg.setTextColor(context.getResources()
                            .getColor(R.color.green));
                    holder.imgChatStatus
                            .setVisibility(View.GONE);
                }
            }

        } catch (Exception e) {

        }
    }

    private String lock_status(String query) {
        // TODO Auto-generated method stub
        Cursor c = null;
        try {
            Constant.printMsg("query  " + query);
            c = dbHelper.open().getDatabaseObj().rawQuery(query, null);
            Constant.printMsg("No of deleted rows ::::::::::" + c.getCount());
            c.moveToFirst();
            if (c.getCount() > 0) {
                Constant.printMsg("lock_status " + c.getString(0));
                status_lock = c.getString(0);
                Constant.printMsg("status_lock   " + status_lock);
            } else {
                status_lock = "not_lock";
            }

        } catch (SQLException e) {

        } finally {
            if (c != null) {
                c.close();
                dbHelper.close();
            }
            // db.close();
        }
        return status_lock;

    }

    private void myMethod(String value) {
        Constant.printMsg("Priya Test Nymn mymethod " + value);
        Normallist = "";
        String[] arr = value.split(" ");

        for (int j = 0; j < arr.length; j++) {

            String ss = arr[j];
            Constant.printMsg("Priya Test Nymn ss " + ss);
            if (ss.contains("<n>") && ss.contains("</n>") && ss.contains("<m>")
                    && ss.contains("</m>")) {

                String[] subSplits = ss.split("</n>");
                Constant.printMsg("Priya Test Nymn subSplits " + subSplits);

                for (int k = 0; k < subSplits.length; k++) {

                    String subSplitsMeaning = ss.substring(subSplits[k]
                            .length() + 4);
                    Constant.printMsg("Priya Test Nymn subSplitsMeaning "
                            + subSplitsMeaning + "  k  " + k + "  length "
                            + subSplits[k].length() + "  "
                            + ss.substring(subSplits[k].length()));

                    if (k % 2 == 0) {

                        Normallist += "["
                                + (subSplits[k].substring(3,
                                subSplits[k].length())) + "]";

                        Constant.printMsg("kkkkk"
                                + subSplitsMeaning.substring(3,
                                subSplitsMeaning.length() - 4));

                    }

                }
            } else {
                Constant.printMsg("Priya Test Nymn subSplitsMeaning else "
                        + ss);
                Normallist += " " + ss + " ";

            }

        }

        Constant.printMsg("Priya Test Nymn NormalList" + Normallist);
    }

    private SpannableStringBuilder addClickablePart(final String str) {
        Constant.printMsg("Priya Test Nymn Spannable" + str);

        SpannableStringBuilder ssb = new SpannableStringBuilder(str);
        int idx1 = str.indexOf("[");
        int idx2 = 0;
        while (idx1 != -1) {
            idx2 = str.indexOf("]", idx1) + 1;

            final String clickString = str.substring(idx1, idx2);

            ssb.setSpan(new ClickableSpan() {

                @Override
                public void onClick(View widget) {

                }
            }, idx1, idx2, 0);

            ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#999999")),
                    idx1, idx2 - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#00123654")),
                    idx2 - 2, idx2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            ssb.setSpan(new Clickable(str), idx1, idx2 - 1,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            idx1 = str.indexOf("[", idx2);
        }

        String guess = "[";
        String guess1 = "]";

        String[] words = Normallist.split("\\s+");

        for (int index = Normallist.indexOf(guess); index >= 0; index = Normallist
                .indexOf(guess, index)) {
            Normallist = removeCharAt(Normallist, index);
            ssb.replace(index, index + 1, "");
        }

        for (int index = Normallist.indexOf(guess1); index >= 0; index = Normallist
                .indexOf(guess1, index)) {

            Normallist = removeCharAt(Normallist, index);
            ssb.replace(index, index + 1, "");

        }
        Constant.printMsg("String user chat :::: " + ssb);
        return ssb;
    }

    private String removeCharAt(String str, int i) {
        // TODO Auto-generated method stub
        return str.substring(0, i) + str.substring(i + 1);
    }

    private void itemGroupSelectionProcess(int position) {

        GroupChatList.mPosition = position;
        Constant.FROM_CHAT_SCREEN = "group";

        KachingMeApplication.SELECTED_TAB = 1;
        byte[] b = null;

//            final Cursor cursor = (Cursor) list.getItemAtPosition(position);
        final String jid = chatList.get(position).getJidId();
        final String name = chatList.get(position).getDisplay_name();
        final byte[] avtar = chatList.get(position).getPhoto_ts();
        Constant.printMsg("jiddddd ::::::: " + jid);
        Constant.mgroupID = jid;

        String query = "select status from " + Dbhelper.TABLE_LOCK
                + " where jid = '" + jid + "'";
        Constant.printMsg("bhaththam " + query);
        lock_status(query);

        // if (sp.contains(jid + "_lock")) {
        if (status_lock.equalsIgnoreCase("lock")) {

            AlertDialog.Builder alert = new AlertDialog.Builder(
                    context);

            alert.setTitle(context.getResources().getString(R.string.open_chat));
            alert.setMessage(String.format(
                    context.getResources().getString(R.string.enter_password_to_open_group_chat),
                    name));
            final EditText input = new EditText(context);
            input.setInputType(InputType.TYPE_CLASS_TEXT
                    | InputType.TYPE_CLASS_NUMBER);
            input.setTransformationMethod(PasswordTransformationMethod
                    .getInstance());
            alert.setView(input);

            alert.setPositiveButton(context.getResources().getString(R.string.unlock),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            String value = input.getText().toString();
                            String query = "select password from "
                                    + Dbhelper.TABLE_LOCK
                                    + " where jid = '" + jid + "'";
                            Constant.printMsg("bhaththam value"
                                    + value);
                            lock_status(query);
                            if (value.equals(status_lock)) {

                                // if (value.equals(sp.getString("pin",
                                // ""))) {

                                if (Constant.mDesTFromSlider) {
                                    Toast.makeText(context, "Sorry you can't send DesT in group", Toast.LENGTH_LONG).show();
                                } else {

                                    Intent intent = new Intent(
                                            context, MUCTest.class);
                                    intent.putExtra("jid", jid);
                                    intent.putExtra("name", name);
                                    intent.putExtra("msg_ids", SliderTesting.msg_ids);
                                    // intent.putExtra("avatar",cursor.getBlob(2));
                                    context.startActivity(intent);
                                }

                            } else {
                                new AlertManager().showAlertDialog(
                                        context,
                                        context.getResources().getString(R.string.you_are_entered_incorrect_pin),
                                        true);

                            }
                        }
                    });

            alert.setNegativeButton(context.getResources().getString(R.string.cancel),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {

                        }
                    });

            alert.show();

        } else {
            if (Constant.mDesTFromSlider) {
                Toast.makeText(context, "Sorry you can't send DesT in group", Toast.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent(context, MUCTest.class);
                intent.putExtra("jid", jid);
                intent.putExtra("name", name);
                intent.putExtra("avatar", avtar);
                intent.putExtra("msg_ids", SliderTesting.msg_ids);
                context.startActivity(intent);
            }
        }

    }

    private void itemChatSelectionProcess(final int position) {

        UserChatList.mPosition = position;
        Constant.FROM_CHAT_SCREEN = "chat";

        final Intent intent;
        KachingMeApplication.SELECTED_TAB = 0;

        // Get the cursor, positioned to the corresponding row in the
        // result set
//            final Cursor cursor = (Cursor) chatList.getItemAtPosition(position);

        try {


            final String jid = chatList.get(position).getJidId();
            final String name = chatList.get(position).getDisplay_name();
            final int is_owner = chatList.get(position).getInt12();
            final byte[] avtar = chatList.get(position).getPhoto_ts();
            // final int is_sec_chat = 0;
            final int is_sec_chat = chatList.get(position).getInt11();
            final int is_chat = chatList.get(position).getInt13();
            Constant.mSenderName = chatList.get(position).getDisplay_name();
            Constant.printMsg("userchatList::::>>>>>>>>>   "
                    + chatList.get(position).getJidId() + "    " + chatList.get(position).getDisplay_name()
                    + "    " + chatList.get(position).getInt11() + "    "
                    + chatList.get(position).getInt12() + "    " + chatList.get(position).getPhoto_ts()
                    + "   " + chatList.get(position).getInt13());

            String query = "select status from " + Dbhelper.TABLE_LOCK
                    + " where jid = '" + jid + "'";
            Constant.printMsg("bhaththam " + query);
            lock_status(query);

            // if (sp.contains(jid + "_lock") && is_sec_chat == 1) {
            if (status_lock.equalsIgnoreCase("lock")) {

                Log.d(TAG, "LIST ITEM CALLED..");

                AlertDialog.Builder alert = new AlertDialog.Builder(
                        context);

                alert.setTitle(context.getResources().getString(R.string.open_chat));
                alert.setMessage(String.format(
                        context.getResources().getString(R.string.enter_password_to_open_chat_with),
                        name));

                final EditText input = new EditText(context);
                input.setInputType(InputType.TYPE_CLASS_TEXT
                        | InputType.TYPE_CLASS_NUMBER);
                input.setTransformationMethod(PasswordTransformationMethod
                        .getInstance());

                alert.setView(input);

                alert.setPositiveButton(context.getResources().getString(R.string.open),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {

                                String value = input.getText().toString();
                                String query = "select password from "
                                        + Dbhelper.TABLE_LOCK
                                        + " where jid = '" + jid + "'";
                                Constant.printMsg("bhaththam value"
                                        + value);
                                lock_status(query);
                                if (value.equals(status_lock)) {

                                    if (is_chat == 1) {


                                        if (jid.contains("@conference.localhost")) {
                                            if (Constant.mDonateBux == true) {
                                                Toast.makeText(context, "can't able to donate BuxS in group", Toast.LENGTH_SHORT).show();
                                                Constant.mDonateBux = false;
                                            } else if (Constant.mDesTFromSlider) {
                                                Toast.makeText(context, "Sorry you can't send DesT in group", Toast.LENGTH_LONG).show();
                                            } else {
                                                final Intent in = new Intent(
                                                        context, MUCTest.class);
                                                in.putExtra("jid", jid);
                                                in.putExtra("name", name);
                                                in.putExtra("is_owner", ""
                                                        + is_owner);
                                                in.putExtra("avatar", avtar);
                                                in.putExtra("msg_ids", SliderTesting.msg_ids);
                                                context.startActivity(in);
                                            }
                                        } else {
                                            if (jid.contains("@conference.localhost")) {
                                                if (Constant.mDonateBux == true) {
                                                    Toast.makeText(context, "can't able to donate BuxS in group", Toast.LENGTH_SHORT).show();
                                                    Constant.mDonateBux = false;
                                                } else if (Constant.mDesTFromSlider) {
                                                    Toast.makeText(context, "Sorry you can't send DesT in group", Toast.LENGTH_LONG).show();
                                                } else {
                                                    final Intent in = new Intent(
                                                            context, MUCTest.class);
                                                    in.putExtra("jid", jid);
                                                    in.putExtra("name", name);
                                                    in.putExtra("is_owner", ""
                                                            + is_owner);
                                                    in.putExtra("avatar", avtar);
                                                    in.putExtra("msg_ids", SliderTesting.msg_ids);
                                                    context.startActivity(in);
                                                }

                                            } else {

                                                final Intent in = new Intent(
                                                        context, ChatTest.class);
                                                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                in.putExtra("jid", jid);
                                                in.putExtra("name", name);
                                                in.putExtra("is_owner", ""
                                                        + is_owner);
                                                in.putExtra("msg_ids", SliderTesting.msg_ids);
                                                in.putExtra("avatar", avtar);
                                                if (is_sec_chat == 0) {
                                                    in.putExtra("IS_SECRET_CHAT",
                                                            true);
                                                } else {
                                                    in.putExtra("IS_SECRET_CHAT",
                                                            false);
                                                }
                                                Log.d(TAG, "JID 2::" + jid);
                                                context.startActivity(in);
                                            }
                                        }
                                    } else if (is_chat == 3) {
                                        Intent intent = new Intent(
                                                context,
                                                BroadCastTest.class);
                                        intent.putExtra("jid", jid);
                                        intent.putExtra("name", name);
                                        intent.putExtra("avatar", avtar);
                                        intent.putExtra("msg_ids", SliderTesting.msg_ids);
                                        context.startActivity(intent);
                                    } else {
                                        if (jid.contains("@conference.localhost")) {
                                            if (Constant.mDonateBux == true) {
                                                Toast.makeText(context, "can't able to donate BuxS in group", Toast.LENGTH_SHORT).show();
                                                Constant.mDonateBux = false;
                                            } else if (Constant.mDesTFromSlider) {
                                                Toast.makeText(context, "Sorry you can't send DesT in group", Toast.LENGTH_LONG).show();
                                            } else {
                                                final Intent in = new Intent(
                                                        context, MUCTest.class);
                                                in.putExtra("jid", jid);
                                                in.putExtra("name", name);
                                                in.putExtra("is_owner", ""
                                                        + is_owner);
                                                in.putExtra("avatar", avtar);
                                                in.putExtra("msg_ids", SliderTesting.msg_ids);
                                                context.startActivity(in);
                                            }
                                        } else {

                                            final Intent in = new Intent(
                                                    context, ChatTest.class);
                                            in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            in.putExtra("jid", jid);
                                            in.putExtra("name", name);
                                            in.putExtra("is_owner", ""
                                                    + is_owner);
                                            in.putExtra("msg_ids", SliderTesting.msg_ids);
                                            in.putExtra("avatar", avtar);
                                            if (is_sec_chat == 0) {
                                                in.putExtra("IS_SECRET_CHAT",
                                                        true);
                                            } else {
                                                in.putExtra("IS_SECRET_CHAT",
                                                        false);
                                            }
                                            Log.d(TAG, "JID 2::" + jid);
                                            context.startActivity(in);
                                        }
                                    }
                                } else {
                                    new AlertManager().showAlertDialog(
                                            context,
                                            context.getResources().getString(R.string.you_are_entered_incorrect_pin),
                                            true);

                                }
                            }
                        });

                alert.setNegativeButton(context.getResources().getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                // Canceled.

                            }
                        });

                alert.show();

            } else {

                if (chatList.get(position).getJidId().split("@")[0].length() > 15) {
                    Constant.printMsg("length of group  >>>>> "
                            + chatList.get(position).getJidId().split("@")[0].length());
                    if (Constant.mDesTFromSlider) {
                        Toast.makeText(context, "Sorry you can't send DesT in group", Toast.LENGTH_LONG).show();
                    } else {
                        Constant.fromChat = true;
                        Intent intent1 = new Intent(context,
                                MUCTest.class);
                        intent1.putExtra("jid", jid);
                        intent1.putExtra("name", name);
                        intent1.putExtra("avatar", avtar);
                        intent1.putExtra("msg_ids", SliderTesting.msg_ids);
                        context.startActivity(intent1);
                    }
                } else {
                    if (is_chat == 1) {
                        if (jid.contains("@conference.localhost")) {
                            if (Constant.mDonateBux == true) {
                                Toast.makeText(context, "can't able to donate BuxS in group", Toast.LENGTH_SHORT).show();
                                Constant.mDonateBux = false;
                            } else if (Constant.mDesTFromSlider) {
                                Toast.makeText(context, "Sorry you can't send DesT in group", Toast.LENGTH_LONG).show();
                            } else {
                                final Intent in = new Intent(
                                        context, MUCTest.class);
                                in.putExtra("jid", jid);
                                in.putExtra("name", name);
                                in.putExtra("is_owner", ""
                                        + is_owner);
                                in.putExtra("avatar", avtar);
                                in.putExtra("msg_ids", SliderTesting.msg_ids);
                                context.startActivity(in);
                            }

                        } else {
                            intent = new Intent(context, ChatTest.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("jid", jid);
                            intent.putExtra("name", name);
                            intent.putExtra("is_owner", "" + is_owner);
                            intent.putExtra("avatar", avtar);
                            intent.putExtra("msg_ids", SliderTesting.msg_ids);
                            if (is_sec_chat == 0) {
                                intent.putExtra("IS_SECRET_CHAT", true);
                            } else {
                                intent.putExtra("IS_SECRET_CHAT", false);
                            }
                            context.startActivity(intent);
                        }
                    } else if (is_chat == 3) {
                        Intent intent1 = new Intent(
                                context,
                                BroadCastTest.class);
                        intent1.putExtra("jid", jid);
                        intent1.putExtra("name", name);
                        intent1.putExtra("avatar", avtar);
                        intent1.putExtra("msg_ids", SliderTesting.msg_ids);
                        context.startActivity(intent1);
                    } else {
                        if (jid.contains("@conference.localhost")) {
                            if (Constant.mDesTFromSlider) {
                                Toast.makeText(context, "Sorry you can't send DesT in group", Toast.LENGTH_LONG).show();
                            } else {

                                if (Constant.mDonateBux == true) {
                                    Toast.makeText(context, "can't able to donate BuxS in group", Toast.LENGTH_SHORT).show();
                                    Constant.mDonateBux = false;
                                }
                                final Intent in = new Intent(
                                        context, MUCTest.class);
                                in.putExtra("jid", jid);
                                in.putExtra("name", name);
                                in.putExtra("is_owner", ""
                                        + is_owner);
                                in.putExtra("avatar", avtar);
                                in.putExtra("msg_ids", SliderTesting.msg_ids);
                                context.startActivity(in);
                            }
                        } else {

                            final Intent in = new Intent(
                                    context, ChatTest.class);
                            in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            in.putExtra("jid", jid);
                            in.putExtra("name", name);
                            in.putExtra("is_owner", ""
                                    + is_owner);
                            in.putExtra("msg_ids", SliderTesting.msg_ids);
                            in.putExtra("avatar", avtar);
                            if (is_sec_chat == 0) {
                                in.putExtra("IS_SECRET_CHAT",
                                        true);
                            } else {
                                in.putExtra("IS_SECRET_CHAT",
                                        false);
                            }
                            Log.d(TAG, "JID 2::" + jid);
                            context.startActivity(in);
                        }
                    }
                }
            }
        } catch (Exception e) {
            Constant.printMsg("ERROR" + e.toString());
        }
    }

    @Override
    public int getItemCount() {
        return chatList == null ? 0 : chatList.size();
    }

    public void setPosition(int position) {
        Constant.printMsg("positionnnnnnn " + position);
        mPosition = position;
    }

    public class Clickable extends ClickableSpan {

        String clicked;

        public Clickable(String string) {
            super();

        }

        public void onClick(View v) {

        }

        public void updateDrawState(TextPaint ds) {
            // override updateDrawState
            ds.setUnderlineText(false); // set to false to remove underline
        }
    }

    class ChatViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        CardView cardChatList;
        TextView tvChatUserName, tvChatUserMsg, tvChatTimeStamp;
        ImageView imgChatUserProfile;
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
            imgChatUserProfile = (ImageView) itemView.findViewById(R.id.imgChatUserProfile);
            linearChatList = (LinearLayout) itemView.findViewById(R.id.linearChatList);
            linearChatDetails = (LinearLayout) itemView.findViewById(R.id.linearChatDetails);
            linearChatMsg = (LinearLayout) itemView.findViewById(R.id.linearChatMsg);
            imgChatStatus = (ImageView) itemView.findViewById(R.id.imgChatStatus);
            mView = (View) itemView.findViewById(R.id.userchat_view);
            itemView.setOnCreateContextMenuListener(this);


            try {
                Constant.typeFace(context, tvChatUserName);
                Constant.typeFace(context, tvChatUserMsg);
                Constant.typeFace(context, tvChatTimeStamp);
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
            viewParama.gravity = Gravity.BOTTOM | Gravity.CENTER;
            mView.setLayoutParams(viewParama);

            CardView.LayoutParams linearListRow = new CardView.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            linearListRow.width = width;
            linearListRow.height = (int) (height * 13.5 / 100);
            linearChatList.setLayoutParams(linearListRow);
            linearChatList.setGravity(Gravity.CENTER);

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
            linearDetails.height = (int) (height * 13.5 / 100);
            linearChatDetails.setLayoutParams(linearDetails);

            LinearLayout.LayoutParams userNameParama = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            userNameParama.width = width * 52 / 100;
            userNameParama.height = (int) (height * (13.5 / 2) / 100);
            tvChatUserName.setLayoutParams(userNameParama);
            tvChatUserName.setGravity(Gravity.CENTER | Gravity.LEFT | Gravity.BOTTOM);
            tvChatUserName.setPadding(0, 0, 0, width * 1 / 100);

            LinearLayout.LayoutParams chatMsgParama = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            chatMsgParama.width = width * 52 / 100;
            chatMsgParama.height = (int) (height * (13.5 / 2) / 100);
            linearChatMsg.setLayoutParams(chatMsgParama);
            linearChatMsg.setGravity(Gravity.CENTER | Gravity.LEFT | Gravity.TOP);

            LinearLayout.LayoutParams statusImageParama = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            statusImageParama.width = width * 7 / 100;
            statusImageParama.height = width * 7 / 100;
            statusImageParama.gravity = Gravity.LEFT | Gravity.TOP;
            imgChatStatus.setLayoutParams(statusImageParama);

            LinearLayout.LayoutParams msgTextParama = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            msgTextParama.width = width * 45 / 100;
            msgTextParama.height = (int) (height * (13.5 / 2) / 100);
            msgTextParama.gravity = Gravity.LEFT;
            tvChatUserMsg.setLayoutParams(msgTextParama);
            tvChatUserMsg.setGravity(Gravity.CENTER | Gravity.LEFT | Gravity.TOP);
            tvChatUserMsg.setPadding(width * 1 / 100, width * 1 / 100, 0, 0);

            LinearLayout.LayoutParams rightSecondsParama = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            rightSecondsParama.width = width * 25 / 100;
            rightSecondsParama.height = (int) (height * 13.5 / 100);
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

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            try {
                boolean isGroupInChat = false;


                if (strChatAndGroup.equalsIgnoreCase("chat")) {
                    String jid = chatList.get(getAdapterPosition()).getJidId();
                    if (jid != null) {
                        Spannable set_text = new Emoji(context)
                                .getSmiledText_List(jid);

                        if (set_text.toString().contains("@conference")) {
                            isGroupInChat = true;
                        }
                    }

                    Constant.printMsg("Group in chat " + isGroupInChat + " " + jid);
                }

                boolean fromGroupList = false;

                if (strChatAndGroup.equalsIgnoreCase("group")) {
                    fromGroupList = true;
                }

                if (!fromGroupList && !isGroupInChat) {
                    Constant.mSlectedActivity = "chat";
                    String[] menuItems = {"View Contact", "Delete Chat", "Archive Chat"};
                    Constant.printMsg("lenghththth " + menuItems.length);
                    for (int i = 0; i < menuItems.length; i++) {

                        menu.add(Menu.NONE, (i + 10), (i + 10), menuItems[i]);
                        Constant.printMsg("menu items::" + v.getId() + "menuItems[i]::"
                                + menuItems[i] + "     " + getAdapterPosition());
                    }
                    Constant.printMsg("menu items::111" + menu + "menuItems[i]::"
                            + mPosition + "   " + chatList.get(getAdapterPosition()).getInt11());


                    if (chatList.get(getAdapterPosition()).getInt11() != 0) {

                        String query = "select status from " + Dbhelper.TABLE_LOCK
                                + " where jid = '" + chatList.get(getAdapterPosition()).getJidId() + "'";
                        Constant.printMsg("bhaththam " + query);

                        lock_status(query);
                        Constant.mJid = chatList.get(getAdapterPosition()).getJidId();
                        Constant.mChatListint11 = chatList.get(getAdapterPosition()).getInt11();
                        if (status_lock.equalsIgnoreCase("lock")) {
                            menu.add(Menu.NONE, 13, 2, R.string.unlock);
                        } else {
                            menu.add(Menu.NONE, 13, 2, R.string.lock);

                        }

                    }

                } else {
                    Constant.mSlectedActivity = "group";

                    String[] menuItems = {"Clear Chat", "Exit group", "Archive Group"};
                    Constant.mJid = chatList.get(getAdapterPosition()).getJidId();
                    Constant.mChatListint11 = chatList.get(getAdapterPosition()).getInt11();
                    for (int i = 0; i < menuItems.length; i++) {

                        menu.add(Menu.NONE, (i + 10), (i + 10), menuItems[i]);
                        Constant.printMsg("menu items::" + v.getId() + "menuItems[i]::"
                                + menuItems[i] + "     " + getAdapterPosition());
                    }
                    String query = "select status from " + Dbhelper.TABLE_LOCK
                            + " where jid = '" + chatList.get(getAdapterPosition()).getJidId() + "'";
                    Constant.printMsg("bhaththam " + query);

                    lock_status(query);
                    Constant.mJid = chatList.get(getAdapterPosition()).getJidId();
                    Constant.mChatListint11 = chatList.get(getAdapterPosition()).getInt11();
                    if (status_lock.equalsIgnoreCase("lock")) {
                        menu.add(Menu.NONE, 13, 11, R.string.unlock);
                    } else {
                        menu.add(Menu.NONE, 13, 11, R.string.lock);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
