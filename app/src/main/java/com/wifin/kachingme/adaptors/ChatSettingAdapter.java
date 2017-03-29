/*
* @author Rajsekar
*
* @usage -  This class is used to display the list of items in chat se4ttings screen
*
*
* */



package com.wifin.kachingme.adaptors;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.chat.chat_common_classes.ForwardList;
import com.wifin.kachingme.database.Dbhelper;
import com.wifin.kachingme.settings.ChatSettings;
import com.wifin.kachingme.settings.LockScreen;
import com.wifin.kachingme.settings.MediaAutoDownload;
import com.wifin.kachingme.settings.Wallpaper_Activity;
import com.wifin.kachingme.settings.blocked_users;
import com.wifin.kachingme.util.AlertUtils;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.LastModifiedFileComparator;
import com.wifin.kachingme.util.encry_decry;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ChatSettingAdapter extends RecyclerView.Adapter<ChatSettingAdapter.ChatSettingView> {
    public static int positionSelected = 0;
    List<String> listChatTitle, listChatSubs;
    Context contextChatSetting;
    String TAG = ChatSettingAdapter.class.getSimpleName();
    Dbhelper db;
    CustomListAdapter customListAdapter = new CustomListAdapter();
    String[] listItemValues;
    List<String> listValues = new ArrayList();
    SharedPreferences sp;
    SharedPreferences.Editor ed;
    ListView listItems;

    public ChatSettingAdapter() {

    }

    public ChatSettingAdapter(Context contextChatSetting, List listChatTitle, List listChatSubs) {
        this.contextChatSetting = contextChatSetting;
        this.listChatTitle = listChatTitle;
        this.listChatSubs = listChatSubs;
    }

    @Override
    public ChatSettingView onCreateViewHolder(ViewGroup parent, int viewType) {
        View chatSettView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatset_card_item, parent, false);
        ChatSettingView chatSettingView = new ChatSettingView(chatSettView);
        return chatSettingView;
    }

    @Override
    public void onBindViewHolder(final ChatSettingView holder, final int position) {
        CardView.LayoutParams chatSettingCard = new CardView.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        chatSettingCard.width = (int) Constant.width;
        chatSettingCard.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        holder.linearCardChatset.setLayoutParams(chatSettingCard);

        LinearLayout.LayoutParams linearContentChatset = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        linearContentChatset.width = LinearLayout.LayoutParams.MATCH_PARENT;
        linearContentChatset.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        linearContentChatset.gravity = Gravity.CENTER_VERTICAL;
        int margin = Constant.width * 3 / 100;
        linearContentChatset.setMargins(margin, margin, margin, margin);
        holder.linearCardChatsetContent.setLayoutParams(linearContentChatset);

        LinearLayout.LayoutParams linearChatsetItems = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        linearChatsetItems.width = LinearLayout.LayoutParams.MATCH_PARENT;
        linearChatsetItems.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        linearChatsetItems.gravity = Gravity.CENTER_VERTICAL;
        linearChatsetItems.weight = 2;
        holder.linearCardChatsetItems.setLayoutParams(linearChatsetItems);

        LinearLayout.LayoutParams textChatSet = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        textChatSet.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        textChatSet.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        textChatSet.gravity = Gravity.CENTER_VERTICAL;
        holder.tvChatsetTitle.setLayoutParams(textChatSet);
        holder.tvChatsetSubs.setLayoutParams(textChatSet);

        LinearLayout.LayoutParams cbChatset = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        cbChatset.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        cbChatset.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        int cbmargin = Constant.width * 1 / 100;
        cbChatset.setMargins(cbmargin, cbmargin, cbmargin, cbmargin);
        cbChatset.gravity = Gravity.CENTER_VERTICAL;
        holder.cbChatset.setLayoutParams(cbChatset);

        if (Constant.width >= 600) {
            holder.tvChatsetTitle.setTextSize(18);
            holder.tvChatsetSubs.setTextSize(18);
        } else if (Constant.width > 501 && Constant.width < 600) {
            holder.tvChatsetTitle.setTextSize(17);
            holder.tvChatsetSubs.setTextSize(17);
        } else if (Constant.width > 260 && Constant.width < 500) {
            holder.tvChatsetTitle.setTextSize(16);
            holder.tvChatsetSubs.setTextSize(16);
        } else if (Constant.width <= 260) {
            holder.tvChatsetTitle.setTextSize(15);
            holder.tvChatsetSubs.setTextSize(15);
        }

        holder.tvChatsetTitle.setText(listChatTitle.get(position));

        if (position != 0 && position != 1) {
            holder.cbChatset.setVisibility(View.GONE);
        } else {
            holder.cbChatset.setVisibility(View.VISIBLE);
        }

        if (listChatSubs.get(position).contains("null")) {
            holder.tvChatsetSubs.setVisibility(View.GONE);
        } else {
            holder.tvChatsetSubs.setText(listChatSubs.get(position));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemSelectedChat(holder, position);
            }
        });

        getLastBackupDate(holder);

        if (position == 0) {
            holder.linearCardChatsetItems.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.cbChatset.isChecked() == true) {
                        holder.cbChatset.setChecked(false);

                        sp = PreferenceManager.getDefaultSharedPreferences(contextChatSetting);
                        ed = sp.edit();
                        ed.putBoolean("enter_is_send", false);
                        ed.commit();
                        Toast.makeText(contextChatSetting, "Unchecked", Toast.LENGTH_SHORT).show();
                    } else if (holder.cbChatset.isChecked() == false) {
                        holder.cbChatset.setChecked(true);

                        sp = PreferenceManager.getDefaultSharedPreferences(contextChatSetting);
                        ed = sp.edit();
                        ed.putBoolean("enter_is_send", true);
                        ed.commit();

                        Toast.makeText(contextChatSetting, "Checked", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            // Enter Key Setting With Check Box

            holder.cbChatset.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked == true) {
                        sp = PreferenceManager.getDefaultSharedPreferences(contextChatSetting);
                        ed = sp.edit();
                        ed.putBoolean("enter_is_send", true);
                        ed.commit();

                        Toast.makeText(contextChatSetting, "Checked", Toast.LENGTH_SHORT).show();
                    } else {
                        sp = PreferenceManager.getDefaultSharedPreferences(contextChatSetting);
                        ed = sp.edit();
                        ed.putBoolean("enter_is_send", false);
                        ed.commit();
                        Toast.makeText(contextChatSetting, "Unchecked", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else if (position == 1) {
            // Default Scroll With Check Box

            holder.linearCardChatsetItems.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.cbChatset.isChecked() == true) {
                        holder.cbChatset.setChecked(false);

                        Constant.mDefaultScroll = false;

                        sp = PreferenceManager.getDefaultSharedPreferences(contextChatSetting);
                        ed = sp.edit();
                        ed.putBoolean("defualt_scroll", false);
                        ed.commit();

                        holder.tvChatsetSubs.setText("Off");


                    } else if (holder.cbChatset.isChecked() == false) {
                        holder.cbChatset.setChecked(true);

                        Constant.mDefaultScroll = true;

                        sp = PreferenceManager.getDefaultSharedPreferences(contextChatSetting);
                        ed = sp.edit();
                        ed.putBoolean("defualt_scroll", true);
                        ed.commit();

                        holder.tvChatsetSubs.setText("On");
                    }

                    ContentValues cv = new ContentValues();
                    cv.put("status", Constant.mDefaultScroll);
                    Constant.printMsg("Constant.mDefaultScroll" + Constant.mDefaultScroll);
                    insertToDB(cv);
                }
            });

            holder.cbChatset.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked == true) {
                        Constant.mDefaultScroll = true;

                        sp = PreferenceManager.getDefaultSharedPreferences(contextChatSetting);
                        ed = sp.edit();
                        ed.putBoolean("defualt_scroll", true);
                        ed.commit();

                        holder.tvChatsetSubs.setText("On");
                    } else {
                        Constant.mDefaultScroll = false;

                        sp = PreferenceManager.getDefaultSharedPreferences(contextChatSetting);
                        ed = sp.edit();
                        ed.putBoolean("defualt_scroll", false);
                        ed.commit();

                        holder.tvChatsetSubs.setText("Off");
                    }

                    ContentValues cv = new ContentValues();
                    cv.put("status", Constant.mDefaultScroll);
                    Constant.printMsg("Constant.mDefaultScroll" + Constant.mDefaultScroll);
                    insertToDB(cv);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listChatTitle == null ? 0 : listChatTitle.size();
    }

    public void alertListItem(String listTitle) {
        AlertDialog.Builder alertDialogList = new AlertDialog.Builder(contextChatSetting);
        final View viewDialogList = LayoutInflater.from(contextChatSetting).inflate(R.layout.alert_list_view, null);
        alertDialogList.setView(viewDialogList);
        final AlertDialog dialogList = alertDialogList.create();
        listItems = (ListView) viewDialogList.findViewById(R.id.listItemsAlert);
        TextView tvListAlertTitle = (TextView) viewDialogList.findViewById(R.id.tvListAlertTitle);
        Button btListAlertCancel = (Button) viewDialogList.findViewById(R.id.btListAlertCancel);
        Button btListAlertProceed = (Button) viewDialogList.findViewById(R.id.btListAlertProceed);
        tvListAlertTitle.setText(listTitle);
        listItemValues = contextChatSetting.getResources().getStringArray(R.array.syncFrequency);
        listValues.clear();
        for (int val = 0; val < listItemValues.length; val++) {
            listValues.add(listItemValues[val]);
        }
        customListAdapter = new CustomListAdapter(contextChatSetting, listValues, positionSelected);
        listItems.setAdapter(customListAdapter);
        listItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listItems = (ListView) view.findViewById(R.id.listItemsAlert);

                customListAdapter = new CustomListAdapter(contextChatSetting, listValues, position);

                listItems.setAdapter(customListAdapter);

                Toast.makeText(contextChatSetting, "Position" + position + "Selected" + positionSelected, Toast.LENGTH_SHORT).show();

            }
        });
        btListAlertProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogList.dismiss();
            }
        });
        btListAlertCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogList.dismiss();
            }
        });
        dialogList.setCancelable(true);
        dialogList.show();
    }

    public void onItemSelectedChat(final ChatSettingView holder, int position) {
        if (position == 2) {
            // Font Size

            alertListItem("Font Size");
        } else if (position == 3) {
            // Media Auto Download

            contextChatSetting.startActivity(new Intent(contextChatSetting, MediaAutoDownload.class));
        } else if (position == 4) {
            // Wall Paper

            onWallpaper();
        } else if (position == 5) {
            // Email Conversation

            Intent intent = new Intent(contextChatSetting, ForwardList.class);
            intent.putExtra("email", "email");
            contextChatSetting.startActivity(intent);
        } else if (position == 6) {
            // Back Up Conversation with Last Back Up

            Log.e(TAG, "Backup Conver Started");

            new encry_decry(contextChatSetting).saveFile();

            getLastBackupDate(holder);

            Toast.makeText(contextChatSetting, contextChatSetting.getResources().getString(R.string.backup_data_successfully), Toast.LENGTH_SHORT).show();
        } else if (position == 7) {
            // Email Backup Conversation

            if (new File(getLastBackupDate(holder)).exists()) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);

                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Conversation backup");
                emailIntent.setType("message/rfc822");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(getLastBackupDate(holder))));

                Log.e(TAG, Uri.fromFile(new File(getLastBackupDate(holder))).toString());

                try {
                    contextChatSetting.startActivity(emailIntent);
                } catch (ActivityNotFoundException e) {
                    AlertDialog.Builder b = new AlertDialog.Builder(contextChatSetting);
                    b.setMessage(contextChatSetting.getResources().getString(R.string.it_seems_like_no_email)).setCancelable(false);
                    b.setNegativeButton(contextChatSetting.getResources().getString(R.string.Ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

                    AlertDialog alert = b.create();
                    alert.show();
                }
            } else {
                new AlertUtils().Toast_call(contextChatSetting, contextChatSetting.getResources().getString(R.string.no_backup_found_in_sdcard));
            }
        } else if (position == 8) {
            // Cloud Backup Conversation

            Toast.makeText(contextChatSetting, "Cloud Backup", Toast.LENGTH_SHORT).show();
        } else if (position == 9) {
            // Lock List

            contextChatSetting.startActivity(new Intent(contextChatSetting, LockScreen.class));
        } else if (position == 10) {
            // Block List

            contextChatSetting.startActivity(new Intent(contextChatSetting, blocked_users.class));
        }
    }

    public String getLastBackupDate(ChatSettingView holder) {
        String path = "";

        long size = 0;

        try {
            File dir = new File(Constant.local_database_dir);

            File[] files = dir.listFiles();

            Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_COMPARATOR);

            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                System.out.printf("File %s - %2$tm %2$te,%2$tY%n= ", file.getName(), file.lastModified());
                SimpleDateFormat date_format = new SimpleDateFormat("hh:mma ,dd/MM/yyyy");
                Date dt = new Date(file.lastModified());
                String date = date_format.format(dt);
                if (holder.getAdapterPosition() == 6) {
                    holder.tvChatsetSubs.setText(contextChatSetting.getResources().getString(R.string.last_backup) + " " + date);
                }
                path = file.getAbsolutePath();

                size = file.length();
            }
        } catch (Exception e) {
            Log.e(TAG, "\n" + e.toString());
        }

        Log.e(TAG, "Last Backup Path - " + path.toString() + "\n" + "File Size - " + size);

        return path;
    }

    protected void insertToDB(ContentValues cv) {
        try {
            int a = (int) db.open().getDatabaseObj().insert(Dbhelper.TABLE_DEFAULT_STATUS, null, cv);

            Constant.printMsg("No of inserted rows in zzle seen:::::::::" + a);
        } catch (SQLException e) {
            Constant.printMsg("Sql exception in ecpl details seen ::::::" + e.toString());
        } finally {
            db.close();
        }
    }

    public void onWallpaper() {
        final CharSequence[] options = {contextChatSetting.getResources().getString(R.string.set_wallpaper),
                contextChatSetting.getResources().getString(R.string.no_wallpaper),
                contextChatSetting.getResources().getString(R.string.default_wallpaper)};
        AlertDialog.Builder builder = new AlertDialog.Builder(contextChatSetting);
        builder.setTitle(contextChatSetting.getResources().getString(R.string.wallpaper));
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (item == 0) {
                    String data = "file";

                    sp = PreferenceManager.getDefaultSharedPreferences(contextChatSetting);
                    ed = sp.edit();
                    ed.putString("wallpaper_type", data);
                    ed.commit();

                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    ((ChatSettings) contextChatSetting).startActivityForResult(photoPickerIntent, 0);
                } else if (item == 1) {
                    if (KachingMeApplication.getsharedpreferences().contains("wallpaper"))
                        KachingMeApplication.getsharedpreferences_Editor().remove("wallpaper").commit();
                } else if (item == 2) {
                    String data = "image";

                    sp = PreferenceManager.getDefaultSharedPreferences(contextChatSetting);
                    ed = sp.edit();
                    ed.putString("wallpaper_type", data);
                    ed.commit();

                    Intent i = new Intent(contextChatSetting, Wallpaper_Activity.class);
                    contextChatSetting.startActivity(i);
                }
            }
        });
        builder.show();
    }

    class ChatSettingView extends RecyclerView.ViewHolder {
        CardView cardChatSetting;
        LinearLayout linearCardChatset, linearCardChatsetItems, linearCardChatsetContent;
        TextView tvChatsetTitle, tvChatsetSubs;
        CheckBox cbChatset;

        public ChatSettingView(View itemView) {
            super(itemView);

            cardChatSetting = (CardView) itemView.findViewById(R.id.cardChatSetting);
            linearCardChatset = (LinearLayout) itemView.findViewById(R.id.linearCardChatset);
            linearCardChatsetItems = (LinearLayout) itemView.findViewById(R.id.linearCardChatsetItems);
            linearCardChatsetContent = (LinearLayout) itemView.findViewById(R.id.linearCardChatsetContent);
            tvChatsetTitle = (TextView) itemView.findViewById(R.id.tvChatsetTitle);
            tvChatsetSubs = (TextView) itemView.findViewById(R.id.tvChatsetSubs);
            cbChatset = (CheckBox) itemView.findViewById(R.id.cbCardset);

            Constant.typeFace(contextChatSetting, tvChatsetTitle);
            Constant.typeFace(contextChatSetting, tvChatsetSubs);

            db = new Dbhelper(contextChatSetting);
            sp = PreferenceManager.getDefaultSharedPreferences(contextChatSetting);
        }
    }
}
