package com.wiredave.imoto;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends Activity {

    ProgressDialog pd;
    private LinearLayout llEmail, llPassword;
    private EditText edtEmail, edtPassword;
    private ImageView ivUserIcon, ivPasswordIcon;
    private TextView tvSignup,tvForgotPassword;
    private Button btnLogin;
    private SharedPreferences pref;

    // validating email id
    public static boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        pref = PreferenceManager.getDefaultSharedPreferences(this);

        llEmail = (LinearLayout) findViewById(R.id.llEmail);
        llPassword = (LinearLayout) findViewById(R.id.llPassword);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        ivUserIcon = (ImageView) findViewById(R.id.ivUserIcon);
        ivPasswordIcon = (ImageView) findViewById(R.id.ivPasswordIcon);
        tvSignup = (TextView) findViewById(R.id.tvSignup);
        tvForgotPassword= (TextView) findViewById(R.id.tvForgotPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        edtPassword.setTypeface(Typeface.DEFAULT);
        edtPassword.setTransformationMethod(new PasswordTransformationMethod());

        SpannableString ss_privacy = new SpannableString("New to application? Signup");
        ClickableSpan clickableSpanPolicy = new ClickableSpan() {


            @Override
            public void onClick(View textView) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ss_privacy.setSpan(clickableSpanPolicy, 19, 26, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss_privacy.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.app_color)), 19, 26, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        TextView textViewPolicy = (TextView) findViewById(R.id.tvSignup);
        textViewPolicy.setText(ss_privacy);
        textViewPolicy.setMovementMethod(LinkMovementMethod.getInstance());
        textViewPolicy.setHighlightColor(getResources().getColor(R.color.transferent));

        edtEmail.setFocusable(true);

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

        edtPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    ivPasswordIcon.setBackgroundResource(R.drawable.icon_login_password_white);
                    llPassword.setBackgroundColor(Color.parseColor("#ffffff"));
                    edtPassword.setTextColor(Color.parseColor("#000000"));
                    edtPassword.setHintTextColor(Color.parseColor("#000000"));
                } else {
                    ivPasswordIcon.setBackgroundResource(R.drawable.icon_login_password);
                    llPassword.setBackgroundResource(R.color.transferent);
                    edtPassword.setTextColor(Color.parseColor("#ffffff"));
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
                } else if (edtPassword.getText().toString().trim().equals("") || edtPassword.getText().toString().trim().equals(null)) {
                    Toast.makeText(getApplicationContext(), "Please enter Password!", Toast.LENGTH_SHORT).show();
                } else {
                    login(edtEmail.getText().toString().trim(), edtPassword.getText().toString().trim());
                }
            }
        });
        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(LoginActivity.this,ForgotPasswordActivity.class);
                startActivity(i);

            }
        });
    }

    private void login(String email, final String password) {
        pd = new ProgressDialog(LoginActivity.this);
        pd.setMessage("Please wait...");
        pd.setCancelable(false);
        pd.show();

        final String url = getResources().getString(R.string.api_url)
                + "signin";

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("agent_email", email);
        params.put("password", password);
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
                    String name,id;
                    JSONObject jsonObject = new JSONObject(arg2);
                    boolean status = jsonObject.getBoolean("success");
                    String message = jsonObject.getString("message");

                    if (status) {
                        JSONObject response_data = jsonObject.getJSONObject("response_data");
                        name = response_data.getString("name");
                        id=response_data.getString("id");
                        edtEmail.setText("");
                        edtPassword.setText("");

                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("id", id);
                        editor.commit();

                        Intent i = new Intent(LoginActivity.this, HomeActivity.class);
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



