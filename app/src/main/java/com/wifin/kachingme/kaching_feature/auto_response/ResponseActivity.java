package com.wifin.kachingme.kaching_feature.auto_response;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.chat_home.SliderTesting;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.Log;

import java.util.HashSet;

@SuppressWarnings("deprecation")
public class ResponseActivity extends AppCompatActivity
{
    ListView listAutoResponse;
    String[] valueAuto;
    ArrayAdapter<String> adapterAuto;
    TextView tvSelectedResponse;
    Context contextAutoResp;
    ImageButton btAutoResponse;

    SharedPreferences prefResponse;
    SharedPreferences.Editor editResponse;
    String preferenceResp = "auto_response", responseMsg = "";
    boolean response_state = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_response);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Auto Response");

        contextAutoResp = this;

        prefResponse = contextAutoResp.getSharedPreferences(preferenceResp, MODE_PRIVATE);
        editResponse = prefResponse.edit();

        response_state = prefResponse.getBoolean("status_auto", false);
        responseMsg = prefResponse.getString("status_msg", null);

        Constant.printMsg("Preference Status :: " + response_state + " Msg :: " + responseMsg);

        listAutoResponse = (ListView) findViewById(R.id.listAutoResponse);
        tvSelectedResponse = (TextView) findViewById(R.id.tvSelectedResponse);
        btAutoResponse = (ImageButton) findViewById(R.id.btAutoResponse);
        valueAuto = getResources().getStringArray(R.array.auto_response_msg);

        if (responseMsg.equalsIgnoreCase(""))
        {
            responseMsg = valueAuto[0].toString();

            tvSelectedResponse.setText(responseMsg);
        }
        else
        {
            tvSelectedResponse.setText(responseMsg);
        }

        Constant.printMsg("Auto Response Selected ::: " + Constant.autoResponseSelected);

        if (Constant.autoResponseDefault.size() == 0)
        {
            for (int auto = 0; auto < valueAuto.length; auto++)
            {
                Constant.autoResponseDefault.add(valueAuto[auto]);
            }
        }

        adapterAuto = new ArrayAdapter<>(getApplicationContext(), R.layout.auto_response_text, Constant.autoResponseDefault);

        listAutoResponse.setAdapter(adapterAuto);

        btAutoResponse.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (response_state)
                {
                    showAddAlert(tvSelectedResponse.getText().toString());
                }
                else
                {
                    Toast.makeText(contextAutoResp, "Enable Auto Response To Edit", Toast.LENGTH_SHORT).show();
                }
            }
        });

        listAutoResponse.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if (response_state)
                {
                    String selectedResponse = parent.getItemAtPosition(position).toString();
                    Log.e("Response", selectedResponse);
                    responseMsg = selectedResponse;
                    tvSelectedResponse.setText(responseMsg);
                }
                else
                {
                    Toast.makeText(contextAutoResp, "Enable Auto Response To Select", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(ResponseActivity.this, SliderTesting.class));
        finish();
    }

    public void showAddAlert(final String inputResponse)
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ResponseActivity.this);
        final LayoutInflater inflater = (LayoutInflater) contextAutoResp.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewAlert = inflater.inflate(R.layout.alert_auto_response, null);
        alertDialog.setView(viewAlert);
        final AlertDialog dialogAutoResp = alertDialog.create();

        final EditText etAutoResponseMsg = (EditText) viewAlert.findViewById(R.id.etAutoResponseMsg);
        TextView tvAutoRespDismiss = (TextView) viewAlert.findViewById(R.id.tvAutoRespDismiss);
        TextView tvAutoRespProceed = (TextView) viewAlert.findViewById(R.id.tvAutoRespProceed);
        final HashSet<String> autoRespValues = new HashSet<>();

        if (inputResponse.equalsIgnoreCase(""))
        {
            etAutoResponseMsg.setHint("Enter Auto Response Text");
        }
        else
        {
            etAutoResponseMsg.setText(inputResponse);
        }

        tvAutoRespProceed.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String responseEdit = etAutoResponseMsg.getText().toString();

                if (responseEdit.equalsIgnoreCase("") || responseEdit.equalsIgnoreCase(" ") || responseEdit.trim().isEmpty())
                {
                    Toast.makeText(contextAutoResp, "Enter Response to Proceed", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Constant.autoResponseDefault.add(etAutoResponseMsg.getText().toString());
                    autoRespValues.addAll(Constant.autoResponseDefault);
                    Constant.autoResponseDefault.clear();
                    Constant.autoResponseDefault.addAll(autoRespValues);
                    Constant.autoResponseSelected = etAutoResponseMsg.getText().toString();
                    responseMsg = etAutoResponseMsg.getText().toString();
                    tvSelectedResponse.setText(responseMsg);
                    adapterAuto.notifyDataSetChanged();
                    dialogAutoResp.dismiss();
                }
            }
        });

        tvAutoRespDismiss.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialogAutoResp.dismiss();
            }
        });

        dialogAutoResp.setCancelable(false);
        dialogAutoResp.show();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu)
    {
        getMenuInflater().inflate(R.menu.auto_resp_add, menu);

        if (response_state)
        {
            menu.findItem(R.id.disable_auto_resp).setTitle("Disable");
        }
        else
        {
            menu.findItem(R.id.disable_auto_resp).setTitle("Enable");
        }

        menu.findItem(R.id.disable_auto_resp).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem item)
            {
                AlertDialog.Builder alertDisable = new AlertDialog.Builder(contextAutoResp);
                alertDisable.setTitle("Auto Response");
                if (item.getTitle().equals("Disable"))
                {
                    alertDisable.setMessage("Are You Sure To Disable");
                    alertDisable.setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            Constant.autoResponse = false;

                            editResponse.putBoolean("status_auto", false).apply();

                            editResponse.putString("status_msg", responseMsg).apply();

                            menu.findItem(R.id.disable_auto_resp).setTitle("Enable");

                            dialog.dismiss();
                        }
                    });
                }
                else if (item.getTitle().equals("Enable"))
                {
                    alertDisable.setMessage("Enable Auto Response");
                    alertDisable.setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            Constant.autoResponse = true;

                            editResponse.putBoolean("status_auto", true).apply();

                            editResponse.putString("status_msg", responseMsg).apply();

                            menu.findItem(R.id.disable_auto_resp).setTitle("Disable");

                            Toast.makeText(contextAutoResp, responseMsg, Toast.LENGTH_SHORT).show();

                            dialog.dismiss();
                        }
                    });
                }
                alertDisable.setNegativeButton("No", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                });
                alertDisable.setCancelable(false);
                alertDisable.show();
                return true;
            }
        });

        menu.findItem(R.id.add_auto_resp).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem item)
            {
                showAddAlert("");

                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        switch (id)
        {
            case android.R.id.home:

                onBackPressed();

                break;
        }
        return true;
    }
}
