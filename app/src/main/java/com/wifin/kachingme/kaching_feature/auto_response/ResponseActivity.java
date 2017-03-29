package com.wifin.kachingme.kaching_feature.auto_response;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.chat_home.SliderTesting;
import com.wifin.kachingme.util.Log;
import com.wifin.kachingme.util.Utils;

import java.util.ArrayList;

public class ResponseActivity extends AppCompatActivity {
    SharedPreferences prefAutoResp;
    SharedPreferences.Editor editorAuto;
    ListView listAutoResponse;
    ArrayList<String> autoResponseValues = new ArrayList<>();
    String[] valueAuto = new String[]{};
    ArrayAdapter<String> adapterAuto;
    TextView tvSelectedResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_response);
        prefAutoResp = getSharedPreferences("auto_response", Context.MODE_PRIVATE);
        listAutoResponse = (ListView) findViewById(R.id.listAutoResponse);
        tvSelectedResponse = (TextView) findViewById(R.id.tvSelectedResponse);
        valueAuto = getResources().getStringArray(R.array.auto_response_msg);
        for (int auto = 0; auto < valueAuto.length; auto++) {
            autoResponseValues.add(valueAuto[auto]);
        }
        adapterAuto = new ArrayAdapter<>(getApplicationContext(), R.layout.auto_response_text, autoResponseValues);
        listAutoResponse.setAdapter(adapterAuto);

        listAutoResponse.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedResponse = parent.getItemAtPosition(position).toString();
                Log.e("Response", selectedResponse);
                tvSelectedResponse.setText(selectedResponse);
            }
        });

        if (Utils.autoResponse == false) {
            AlertDialog.Builder alertAutoResp = new AlertDialog.Builder(this);
            alertAutoResp.create();
            alertAutoResp.setTitle("Auto Response");
            alertAutoResp.setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    editorAuto = prefAutoResp.edit();
                    editorAuto.putBoolean("auto_resp", true);
                    editorAuto.commit();
                    Utils.autoResponse = true;
                    dialog.dismiss();
                }
            });
            alertAutoResp.setNegativeButton("Disable", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Utils.autoResponse = false;
                    editorAuto = prefAutoResp.edit();
                    editorAuto.putBoolean("auto_resp", false);
                    editorAuto.commit();
                    startActivity(new Intent(ResponseActivity.this, SliderTesting.class));
                    finish();
                    dialog.dismiss();
                }
            });
            alertAutoResp.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(ResponseActivity.this, SliderTesting.class));
                    finish();
                    dialog.dismiss();
                }
            });
            alertAutoResp.setCancelable(false);
            alertAutoResp.show();
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ResponseActivity.this, SliderTesting.class));
        finish();
    }
}
