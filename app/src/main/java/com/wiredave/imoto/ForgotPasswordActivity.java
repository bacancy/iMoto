package com.wiredave.imoto;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgotPasswordActivity extends Activity {

    ProgressDialog pd;
    private LinearLayout llEmail;
    private EditText edtEmail;
    private ImageView ivUserIcon;
    private Button btnLogin;

    // validating email id
    public static boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);

        llEmail = (LinearLayout) findViewById(R.id.llEmail);

        edtEmail = (EditText) findViewById(R.id.edtEmail);
        ivUserIcon = (ImageView) findViewById(R.id.ivUserIcon);
        btnLogin = (Button) findViewById(R.id.btnLogin);





        edtEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    ivUserIcon.setBackgroundResource(R.drawable.icon_login_user);
                    llEmail.setBackgroundColor(Color.parseColor("#ffffff"));
                    edtEmail.setTextColor(Color.parseColor("#000000"));
                    edtEmail.setHintTextColor(Color.parseColor("#000000"));
                } else {
                    ivUserIcon.setBackgroundResource(R.drawable.icon_login_user_black);
                    llEmail.setBackgroundResource(R.color.transferent);
                    edtEmail.setTextColor(Color.parseColor("#ffffff"));
                }
            }
        });



        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtEmail.getText().toString().trim().equals("") || edtEmail.getText().toString().trim().equals(null)) {
                    Toast.makeText(getApplicationContext(), "Please enter Email!", Toast.LENGTH_SHORT).show();
                } else if (!isValidEmail(edtEmail.getText().toString().trim())) {
                    Toast.makeText(getApplicationContext(), "Please enter valid Email!", Toast.LENGTH_SHORT).show();
                } else {
                    forgotPassword(edtEmail.getText().toString().trim());
                }
            }
        });
    }

    private void forgotPassword(String email) {
        pd = new ProgressDialog(ForgotPasswordActivity.this);
        pd.setMessage("Please wait...");
        pd.setCancelable(false);
        pd.show();

        final String url = getResources().getString(R.string.api_url)
                + "forgot_password";

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("agent_email", email);
        client.addHeader("Content-Type", "application/x-www-form-urlencoded");
        client.post(url, params, new TextHttpResponseHandler() {

            @Override
            public void onFailure(int arg0, org.apache.http.Header[] arg1,
                                  String arg2, Throwable arg3) {
                // TODO Auto-generated method stub

                Log.v("http code", ";" + arg0);
                Log.v("arg2", ";" + arg2);
                Log.v("arg3", "" + arg3);

                pd.dismiss();

            }

            @Override
            public void onSuccess(int arg0, org.apache.http.Header[] arg1,
                                  String arg2) {
                // TODO Auto-generated method stub
                Log.v("http code s", ";" + arg0);
                Log.v("arg2 data", arg2);


                try {
                    String name;
                    JSONObject jsonObject = new JSONObject(arg2);
                    boolean status = jsonObject.getBoolean("success");
                    String message = jsonObject.getString("message");

                    if (status) {
                        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                pd.dismiss();
            }

        });
    }
}



