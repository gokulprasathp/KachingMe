/*
* @author Dilip
*
* @usage -  This class is used to display list of data usage and network usage
*
*
* */


package com.wifin.kachingme.adaptors;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.TrafficStats;
import android.preference.PreferenceManager;
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
import com.wifin.kachingme.settings.DataUsage;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.NetworkSharedPreference;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class DataUsageAdapter extends RecyclerView.Adapter<DataUsageAdapter.DataUsageView> {
    private static final double BYTE = 1024, KB = BYTE, MB = KB * BYTE, GB = MB * BYTE;
    static DataUsageView holder = null;
    private static DecimalFormat twoDecimalForm = new DecimalFormat("#.##");
    List<String> listSent, listReceived, listValues;
    Context contextUsageAdapter;
    String TAG = DataUsageAdapter.class.getSimpleName();
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    int uid;

    private NetworkSharedPreference mNewtSharPref;

    public DataUsageAdapter() {
        Log.e(TAG, "Data Usage");
    }

    public DataUsageAdapter(Context contextUsageAdapter, List listSent, List listReceived, List listValues) {
        this.contextUsageAdapter = contextUsageAdapter;
        this.listSent = listSent;
        this.listReceived = listReceived;
        this.listValues = listValues;
    }

    // Converting the bytes into MB, GB, KB
    public static String convertBytesToSuitableUnit(String bytess) {
        try {
            if (bytess != null) {
                bytess = trimTrailingZeros(bytess);
                long bytes = Long.parseLong(bytess);
                String bytesToSuitableUnit = bytes + " B";

                if (bytes >= GB) {
                    double tempBytes = bytes / GB;
                    bytesToSuitableUnit = twoDecimalForm.format(tempBytes) + " GB";
                    return bytesToSuitableUnit;
                }
                if (bytes >= MB) {
                    double tempBytes = bytes / MB;
                    bytesToSuitableUnit = twoDecimalForm.format(tempBytes) + " MB";
                    return bytesToSuitableUnit;
                }
                if (bytes >= KB) {
                    double tempBytes = bytes / KB;
                    bytesToSuitableUnit = twoDecimalForm.format(tempBytes) + " KB";
                    return bytesToSuitableUnit;
                }
                return bytesToSuitableUnit;
            } else {
                return "0";
            }
        } catch (Exception e) {
            return "0";
        }
    }

    private static String trimTrailingZeros(String number) {
        if (!number.contains(".")) {
            return number;
        }

        return number.replaceAll("\\.?0*$", "");
    }

    @Override
    public DataUsageView onCreateViewHolder(ViewGroup parent, int viewType) {
        View dataUsage = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_card_item, parent, false);
        DataUsageView dataUsageView = new DataUsageView(dataUsage);
        holder = new DataUsageView(dataUsage);
        return dataUsageView;
    }

    @Override
    public void onBindViewHolder(DataUsageView holder, int position) {
        CardView.LayoutParams usageLinear = new CardView.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        usageLinear.width = (int) Constant.width;
        usageLinear.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        holder.linearCardUsage.setLayoutParams(usageLinear);

        LinearLayout.LayoutParams linearConversationItem = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        linearConversationItem.width = LinearLayout.LayoutParams.MATCH_PARENT;
        linearConversationItem.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        linearConversationItem.gravity = Gravity.CENTER_VERTICAL;
        int margin = Constant.width * 4 / 100;
        int margins = Constant.width * 3 / 100;
        linearConversationItem.setMargins(margins, margin, margins, margin);
        holder.linearCardUsageItem.setLayoutParams(linearConversationItem);

        LinearLayout.LayoutParams linearUsageValue = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        linearUsageValue.width = LinearLayout.LayoutParams.MATCH_PARENT;
        linearUsageValue.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        linearUsageValue.gravity = Gravity.CENTER_VERTICAL;
        holder.linearUsageSent.setLayoutParams(linearUsageValue);
        holder.linearUsageReceived.setLayoutParams(linearUsageValue);

        LinearLayout.LayoutParams notifyConverText = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        notifyConverText.width = LinearLayout.LayoutParams.MATCH_PARENT;
        notifyConverText.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        notifyConverText.gravity = Gravity.CENTER_VERTICAL;
        notifyConverText.weight = 2;
        holder.tvUsageSentTitle.setLayoutParams(notifyConverText);
        holder.tvUsageReceivedTitle.setLayoutParams(notifyConverText);

        LinearLayout.LayoutParams usageValues = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        usageValues.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        usageValues.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        usageValues.gravity = Gravity.CENTER_VERTICAL;
        holder.tvUsageSentValue.setLayoutParams(usageValues);
        holder.tvUsageReceivedValue.setLayoutParams(usageValues);

        if (Constant.width >= 600) {
            holder.tvUsageSentTitle.setTextSize(18);
            holder.tvUsageReceivedTitle.setTextSize(18);
            holder.tvUsageSentValue.setTextSize(18);
            holder.tvUsageReceivedValue.setTextSize(18);
        } else if (Constant.width > 501 && Constant.width < 600) {
            holder.tvUsageSentTitle.setTextSize(17);
            holder.tvUsageReceivedTitle.setTextSize(17);
            holder.tvUsageSentValue.setTextSize(17);
            holder.tvUsageReceivedValue.setTextSize(17);
        } else if (Constant.width > 260 && Constant.width < 500) {
            holder.tvUsageSentTitle.setTextSize(16);
            holder.tvUsageReceivedTitle.setTextSize(16);
            holder.tvUsageSentValue.setTextSize(16);
            holder.tvUsageReceivedValue.setTextSize(16);
        } else if (Constant.width <= 260) {
            holder.tvUsageSentTitle.setTextSize(15);
            holder.tvUsageReceivedTitle.setTextSize(15);
            holder.tvUsageSentValue.setTextSize(15);
            holder.tvUsageReceivedValue.setTextSize(15);
        }

        holder.tvUsageSentTitle.setText(listSent.get(position));
        holder.tvUsageReceivedTitle.setText(listReceived.get(position));
        holder.tvUsageSentValue.setText(listValues.get(position));
        holder.tvUsageReceivedValue.setText(listValues.get(position));

        pref = PreferenceManager.getDefaultSharedPreferences(contextUsageAdapter);
        int received_msg_count = pref.getInt("received_msg_count", 0);
        int sent_msg_count = pref.getInt("sent_msg_count", 0);

        if (holder.getAdapterPosition() == 0) {
            holder.tvUsageSentValue.setText(String.valueOf(sent_msg_count));

            holder.tvUsageReceivedValue.setText(String.valueOf(received_msg_count));
        }

        getNetworkUsageData(holder);
    }

    @Override
    public int getItemCount() {
        return listSent == null ? 0 : listSent.size();
    }

    public void setLastTotalData() {
        mNewtSharPref.setTotalData_Sent(String.valueOf(TrafficStats.getUidTxBytes(uid)));
        mNewtSharPref.setTotalData_Receive(String.valueOf(TrafficStats.getUidRxBytes(uid)));
    }

    public void showConfirmationDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(contextUsageAdapter);

        alert.setTitle("Do you want to reset ?");

        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                mNewtSharPref.clearDataNewtork();

                setLastTotalData();

                Calendar c = Calendar.getInstance();
                Constant.printMsg("Current time => " + c.getTime());
                SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                String formattedDate = df.format(c.getTime());

                editor = pref.edit();
                editor.putString("last_reset", formattedDate);
                editor.commit();

                DataUsage.tvDataLastReset.setText("Last Reset" + "\n" + formattedDate);
                holder.tvUsageReceivedValue.setText("0");
                holder.tvUsageSentValue.setText("0");

                editor = pref.edit();
                editor.putInt("received_msg_count", 0);
                editor.putInt("sent_msg_count", 0);
                editor.commit();

                getNetworkUsageData(holder);

                DataUsage.recyclerDataUsage.getAdapter().notifyDataSetChanged();
            }
        });

        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });

        alert.show();
    }

    public void getNetworkUsageData(final DataUsageView holder) {
        long total_sent = 0;
        long total_recev = 0;

        // get user data from session
        HashMap<String, String> user = mNewtSharPref.getAll_Details();

        String media_sent = user.get(NetworkSharedPreference.KEY_MEDIA_GET_SX);
        String media_recev = user.get(NetworkSharedPreference.KEY_MEDIA_GET_RX);
        String msg_sent = user.get(NetworkSharedPreference.KEY_MESSAGE_GET_SX);
        String msg_recev = user.get(NetworkSharedPreference.KEY_MESSAGE_GET_RX);

        if (holder.getAdapterPosition() == 1) {

            holder.tvUsageSentValue.setText(convertBytesToSuitableUnit(media_sent)); // Media Sent Value

            holder.tvUsageReceivedValue.setText(convertBytesToSuitableUnit(media_recev)); // Media Receive Value
        }

        if (holder.getAdapterPosition() == 2) {
            holder.tvUsageSentValue.setText(convertBytesToSuitableUnit(msg_sent)); // Message Sent Value

            holder.tvUsageReceivedValue.setText(convertBytesToSuitableUnit(msg_recev)); // Message Received Value
        }

        try {
            if (media_sent != null) {
                total_sent = total_sent + Long.parseLong(media_sent);
            }

            if (media_recev != null) {
                total_recev = total_recev + Long.parseLong(media_recev);
            }

            if (msg_sent != null) {
                total_sent = total_sent + Long.parseLong(msg_sent);
            }

            if (msg_recev != null) {
                total_recev = total_recev + Long.parseLong(msg_recev);
            }
        } catch (Exception e) {

        }

        if (holder.getAdapterPosition() == 3) {
            holder.tvUsageSentValue.setText(convertBytesToSuitableUnit(String.valueOf(total_sent))); // Total Sent Value

            holder.tvUsageReceivedValue.setText(convertBytesToSuitableUnit(String.valueOf(total_recev))); // Total Receive Value
        }
    }

    class DataUsageView extends RecyclerView.ViewHolder {
        CardView cardDataUsageItem;
        LinearLayout linearCardUsage, linearCardUsageItem, linearUsageSent, linearUsageReceived;
        TextView tvUsageSentTitle, tvUsageSentValue, tvUsageReceivedTitle, tvUsageReceivedValue;

        public DataUsageView(View itemView) {
            super(itemView);

            cardDataUsageItem = (CardView) itemView.findViewById(R.id.cardDataUsageItem);
            linearCardUsage = (LinearLayout) itemView.findViewById(R.id.linearCardUsage);
            linearCardUsageItem = (LinearLayout) itemView.findViewById(R.id.linearCardUsageItem);
            linearUsageSent = (LinearLayout) itemView.findViewById(R.id.linearUsageSent);
            linearUsageReceived = (LinearLayout) itemView.findViewById(R.id.linearUsageReceived);
            tvUsageSentTitle = (TextView) itemView.findViewById(R.id.tvUsageSentTitle);
            tvUsageSentValue = (TextView) itemView.findViewById(R.id.tvUsageSentValue);
            tvUsageReceivedTitle = (TextView) itemView.findViewById(R.id.tvUsageReceivedTitle);
            tvUsageReceivedValue = (TextView) itemView.findViewById(R.id.tvUsageReceivedValue);


            Constant.typeFace(contextUsageAdapter, tvUsageSentTitle);
            Constant.typeFace(contextUsageAdapter, tvUsageSentValue);
            Constant.typeFace(contextUsageAdapter, tvUsageReceivedTitle);
            Constant.typeFace(contextUsageAdapter, tvUsageReceivedValue);

            mNewtSharPref = new NetworkSharedPreference(contextUsageAdapter);
        }
    }
}
