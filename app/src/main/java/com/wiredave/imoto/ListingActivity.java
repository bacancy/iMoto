package com.wiredave.imoto;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import common.BaseActivity;

/**
 * Created by Sp on 12-Oct-15.
 */
public class ListingActivity extends BaseActivity {

    private ImageView ivMenu;
    private ProgressDialog pd1;
    private Button btnContinue;
    ArrayList<String> StateArray = new ArrayList<String>();
    Spinner spState;
    public ListingActivity() {
        super(R.string.app_name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);

        spState = (Spinner) findViewById(R.id.spState);
        ivMenu=(ImageView)findViewById(R.id.ivMenu);
        btnContinue=(Button)findViewById(R.id.btnContinue);

        getSlidingMenu().setSlidingEnabled(true);
        getSlidingMenu().setMode(SlidingMenu.RIGHT);

        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSlidingMenu().toggle(true);

            }
        });
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(ListingActivity.this,ConfirmOrderActivity.class);
                startActivity(i);
            }
        });
        getState();
    }
    private void getState() {
        pd1 = new ProgressDialog(ListingActivity.this);
        pd1.setMessage("Please wait...");
        // pd.setCancelable(false);
        pd1.show();

        final String url = getResources().getString(R.string.api_url)
                + "get_state_list";

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        client.addHeader("Content-Type", "application/x-www-form-urlencoded");
        client.post(url, params, new TextHttpResponseHandler() {

            @Override
            public void onFailure(int arg0, org.apache.http.Header[] arg1,
                                  String arg2, Throwable arg3) {
                // TODO Auto-generated method stub

                Log.v("http code", ";" + arg0);
                Log.v("arg2", ";" + arg2);
                Log.v("arg3", "" + arg3);

                pd1.dismiss();

            }

            @Override
            public void onSuccess(int arg0, org.apache.http.Header[] arg1,
                                  String arg2) {
                // TODO Auto-generated method stub
                Log.v("http code s", ";" + arg0);
                Log.v("arg2 data", arg2);
                pd1.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(arg2);
                    boolean status = jsonObject.getBoolean("success");
                    String message = jsonObject.getString("message");

                    if (status) {
                        JSONArray response_data = jsonObject.getJSONArray("response_data");
                        for (int i = 0; i < response_data.length(); i++) {
                            JSONObject jsonObject1 = response_data.getJSONObject(i);
                            StateArray.add(jsonObject1.getString("state_name"));
                        }
                        Log.v("StateArray", "-->" + StateArray.toString());
                        if (StateArray.size() > 0) {
                            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(ListingActivity.this, R.layout.in_spinner_item, StateArray);
                            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spState.setAdapter(spinnerArrayAdapter);
                        }

                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        });
    }
}
