package com.wiredave.imoto;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eu.janmuller.android.simplecropimage.CropImage;


public class SignupActivity extends Activity {

    public static final String TEMP_PHOTO_FILE_NAME = "crop_image.jpg";
    public static final int REQUEST_CODE_GALLERY = 0x1;
    public static final int REQUEST_CODE_TAKE_PICTURE = 0x2;
    public static final int REQUEST_CODE_CROP_IMAGE = 0x3;
    private final int PICK_FROM_CAMERA = 0;
    private final int PICK_FROM_FILE = 1;
    ArrayList<String> StateArray = new ArrayList<String>();
    ArrayList<String> GroupArray = new ArrayList<String>();
    ArrayList<String> StateCode = new ArrayList<String>();
    ArrayList<String> GroupID = new ArrayList<String>();
    private LinearLayout llShopperGroup, llState, llUploadPhoto;
    private EditText edtEmail, edtPassword, edtCity,  edtCellPhone, edtAgentEmail, edtSecondaryEmail, edtWebsite,
             edtAgentName, edtConfirmPassword, edtCompany, edtOfficeBranch, edtMailingAddress, edtMailingZip;
    Spinner edtShopperGroup,spState;

    private ImageView ivUserIcon, ivPasswordIcon, ivProfile;
    private ProgressDialog pd;
    private ProgressDialog pd1;
    private File mFileTemp;
    private AlertDialog dialogs;
    private AlertDialog.Builder add_alert;
    private Button btnCreateAccount;
    ArrayAdapter<String> spinnerArrayAdapter;
    private String responseString, AgentName = "", AgentEmail = "", Password = "", cell_phone = "", shopper_groups = "",
            office_branch = "", mailing_address = "", mailing_zip = "", city = "", state = "", company = "", secondary_email = "", website = "", ConfirmPassword = "";

    // validating email id
    public static boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();

    }

    private boolean isValidUrl(String url) {
        Pattern p = Patterns.WEB_URL;
        Matcher m = p.matcher(url);
        if (m.matches())
            return true;
        else
            return false;
    }


    public static void copyStream(InputStream input, OutputStream output)
            throws IOException {

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtCity = (EditText) findViewById(R.id.edtCity);
        spState = (Spinner) findViewById(R.id.spState);
        edtCellPhone = (EditText) findViewById(R.id.edtCellPhone);
        edtAgentEmail = (EditText) findViewById(R.id.edtAgentEmail);
        edtSecondaryEmail = (EditText) findViewById(R.id.edtSecondaryEmail);
        edtWebsite = (EditText) findViewById(R.id.edtWebsite);
        edtShopperGroup = (Spinner) findViewById(R.id.edtShopperGroup);
        edtAgentName = (EditText) findViewById(R.id.edtAgentName);
        edtConfirmPassword = (EditText) findViewById(R.id.edtConfirmPassword);
        edtCompany = (EditText) findViewById(R.id.edtCompany);
        edtOfficeBranch = (EditText) findViewById(R.id.edtOfficeBranch);
        edtMailingAddress = (EditText) findViewById(R.id.edtMailingAddress);
        edtMailingZip = (EditText) findViewById(R.id.edtMailingZip);
        btnCreateAccount = (Button) findViewById(R.id.btnCreateAccount);

        ivUserIcon = (ImageView) findViewById(R.id.ivUserIcon);
        ivPasswordIcon = (ImageView) findViewById(R.id.ivPasswordIcon);


        llUploadPhoto = (LinearLayout) findViewById(R.id.llUploadPhoto);
        ivProfile = (ImageView) findViewById(R.id.ivProfile);

        edtPassword.setTypeface(Typeface.DEFAULT);
        edtPassword.setTransformationMethod(new PasswordTransformationMethod());

        edtConfirmPassword.setTypeface(Typeface.DEFAULT);
        edtConfirmPassword.setTransformationMethod(new PasswordTransformationMethod());



        getGroup();
        getState();


        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AgentName = edtAgentName.getText().toString().trim();
                AgentEmail = edtAgentEmail.getText().toString().trim();
                Password = edtPassword.getText().toString().trim();
                ConfirmPassword = edtConfirmPassword.getText().toString().trim();
                cell_phone = edtCellPhone.getText().toString().trim();
                office_branch = edtOfficeBranch.getText().toString().trim();
                mailing_address = edtMailingAddress.getText().toString().trim();
                mailing_zip = edtMailingZip.getText().toString().trim();
                city = edtCity.getText().toString().trim();
                company = edtCompany.getText().toString().trim();
                secondary_email = edtSecondaryEmail.getText().toString().trim();
                website = edtWebsite.getText().toString().trim();

                if (AgentName.equals("") || AgentName.equals(null)) {
                    Toast.makeText(getApplicationContext(), "Please enter Agent Name!", Toast.LENGTH_SHORT).show();
                } else if (Password.equals("") || Password.equals(null)) {
                    Toast.makeText(getApplicationContext(), "Please enter Password!", Toast.LENGTH_SHORT).show();
                } else if (ConfirmPassword.equals("") || ConfirmPassword.equals(null)) {
                    Toast.makeText(getApplicationContext(), "Please enter Confirm Password!", Toast.LENGTH_SHORT).show();
                } else if (!Password.equals(ConfirmPassword)) {
                    Toast.makeText(getApplicationContext(), "Password and Confirm Password does not match", Toast.LENGTH_SHORT).show();
                } else if (company.equals("") || company.equals(null)) {
                    Toast.makeText(getApplicationContext(), "Please enter Company!", Toast.LENGTH_SHORT).show();
                } else if (shopper_groups.equals("") || shopper_groups.equals(null)) {
                    Toast.makeText(getApplicationContext(), "Please enter Ahopper group!", Toast.LENGTH_SHORT).show();
                } else if (office_branch.equals("") || office_branch.equals(null)) {
                    Toast.makeText(getApplicationContext(), "Please enter Office Branch!", Toast.LENGTH_SHORT).show();
                } else if (mailing_address.equals("") || mailing_address.equals(null)) {
                    Toast.makeText(getApplicationContext(), "Please enter Mailing Address!", Toast.LENGTH_SHORT).show();
                } else if (mailing_zip.equals("") || mailing_zip.equals(null)) {
                    Toast.makeText(getApplicationContext(), "Please enter Mailing Zip!", Toast.LENGTH_SHORT).show();
                } else if (city.equals("") || city.equals(null)) {
                    Toast.makeText(getApplicationContext(), "Please enter City!", Toast.LENGTH_SHORT).show();
                } else if (state.equals("") || state.equals(null)) {
                    Toast.makeText(getApplicationContext(), "Please enter State!", Toast.LENGTH_SHORT).show();
                } else if (cell_phone.equals("") || cell_phone.equals(null)) {
                    Toast.makeText(getApplicationContext(), "Please enter Cellphone!", Toast.LENGTH_SHORT).show();
                } else if (AgentEmail.equals("") || AgentEmail.equals(null)) {
                    Toast.makeText(getApplicationContext(), "Please enter Agent Email!", Toast.LENGTH_SHORT).show();
                } else if (!isValidEmail(AgentEmail)) {
                    Toast.makeText(getApplicationContext(), "Please enter valid Email!", Toast.LENGTH_SHORT).show();
                } else if (secondary_email.equals("") || secondary_email.equals(null)) {
                    Toast.makeText(getApplicationContext(), "Please enter Secondary Email!", Toast.LENGTH_SHORT).show();
                } else if (!isValidEmail(secondary_email)) {
                    Toast.makeText(getApplicationContext(), "Please enter vlid secondary Email!", Toast.LENGTH_SHORT).show();
                } else if (website.equals("") || website.equals(null)) {
                    Toast.makeText(getApplicationContext(), "Please enter Website!", Toast.LENGTH_SHORT).show();
                } else if (!isValidUrl(website)) {
                    Toast.makeText(getApplicationContext(), "Please enter Website!", Toast.LENGTH_SHORT).show();
                } else {
                    GetSignUp getSignUp = new GetSignUp();
                    getSignUp.execute();
                }
            }
        });

        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            mFileTemp = new File(Environment.getExternalStorageDirectory(),
                    TEMP_PHOTO_FILE_NAME);
        } else {
            mFileTemp = new File(getFilesDir(), TEMP_PHOTO_FILE_NAME);
        }

        add_alert = new AlertDialog.Builder(this);
        add_alert.setTitle("Photo");
        String itm[] = {"Camera", "Photo album"};
        add_alert.setItems(itm, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int which) {
                if (which == PICK_FROM_CAMERA) {

                    dialogs.cancel();
                    takePicture();

                }
                if (which == PICK_FROM_FILE) {
                    dialogs.cancel();
                    openGallery();
                }

            }
        });

        dialogs = add_alert.create();

        llUploadPhoto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialogs.show();

            }
        });


    }

    public static void SelectSpinnerItemByValue(Spinner spnr, long value)
    {
        SimpleCursorAdapter adapter = (SimpleCursorAdapter) spnr.getAdapter();
        for (int position = 0; position < adapter.getCount(); position++)
        {
            if(adapter.getItemId(position) == value)
            {
                spnr.setSelection(position);
                return;
            }
        }
    }

    private void takePicture() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {
            Uri mImageCaptureUri = null;
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                mImageCaptureUri = Uri.fromFile(mFileTemp);
            } else {
                mImageCaptureUri = InternalStorageContentProvider.CONTENT_URI;
            }
            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
                    mImageCaptureUri);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);
        } catch (ActivityNotFoundException e) {

            Log.d("sign up", "cannot take picture", e);
        }
    }

    private void openGallery() {

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_CODE_GALLERY);
    }

    private void startCropImage() {

        Intent intent = new Intent(this, CropImage.class);
        intent.putExtra(CropImage.IMAGE_PATH, mFileTemp.getPath());
        intent.putExtra(CropImage.SCALE, true);

        intent.putExtra(CropImage.ASPECT_X, 2);
        intent.putExtra(CropImage.ASPECT_Y, 2);

        startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_OK) {

            return;
        }

        Bitmap bitmap;

        switch (requestCode) {

            case REQUEST_CODE_GALLERY:

                try {

                    InputStream inputStream = getContentResolver().openInputStream(
                            data.getData());
                    FileOutputStream fileOutputStream = new FileOutputStream(
                            mFileTemp);
                    copyStream(inputStream, fileOutputStream);
                    fileOutputStream.close();
                    inputStream.close();

                    startCropImage();

                } catch (Exception e) {

                    Log.e("Signup", "Error while creating temp file", e);
                }

                break;
            case REQUEST_CODE_TAKE_PICTURE:

                startCropImage();
                break;
            case REQUEST_CODE_CROP_IMAGE:

                String path = data.getStringExtra(CropImage.IMAGE_PATH);
                if (path == null) {

                    return;
                }

                bitmap = BitmapFactory.decodeFile(mFileTemp.getPath());
                ivProfile.setTag(mFileTemp.getPath());
                ivProfile.setImageBitmap(bitmap);
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getGroup() {
        pd = new ProgressDialog(SignupActivity.this);
        pd.setMessage("Please wait...");
        // pd.setCancelable(false);
        pd.show();

        final String url = getResources().getString(R.string.api_url)
                + "get_shopper_group_list";

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

                pd.dismiss();

            }

            @Override
            public void onSuccess(int arg0, org.apache.http.Header[] arg1,
                                  String arg2) {
                // TODO Auto-generated method stub
                Log.v("http code s", ";" + arg0);
                Log.v("arg2 data", arg2);

                pd.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(arg2);
                    boolean status = jsonObject.getBoolean("success");
                    String message = jsonObject.getString("message");

                    if (status) {
                        JSONArray response_data = jsonObject.getJSONArray("response_data");
                        for (int i = 0; i < response_data.length(); i++) {
                            JSONObject jsonObject1 = response_data.getJSONObject(i);
                            GroupArray.add(jsonObject1.getString("shopper_group_name"));
                            GroupID.add(jsonObject1.getString("shopper_group_id"));
                        }
                        Log.v("GroupArray", "-->" + GroupArray.toString());
                        if (GroupArray.size() > 0) {
                          spinnerArrayAdapter = new ArrayAdapter<String>(SignupActivity.this,R.layout.in_spinner_item, GroupArray);
                            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            edtShopperGroup.setAdapter(spinnerArrayAdapter);
                        }

                        edtShopperGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                shopper_groups = GroupID.get(position);

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });


                    } else {

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        });
    }

    private void getState() {
        pd1 = new ProgressDialog(SignupActivity.this);
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
                            StateCode.add(jsonObject1.getString("state_2_code"));
                        }
                        Log.v("StateArray", "-->" + StateArray.toString());
                        if (StateArray.size() > 0) {
                            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(SignupActivity.this, R.layout.in_spinner_item, StateArray);
                            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spState.setAdapter(spinnerArrayAdapter);
                        }

                        spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                state = StateCode.get(position);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        });
    }

    private class GetSignUp extends AsyncTask<Object, Integer, Object> {

        ProgressDialog dialog1;

        protected void onPreExecute() {
            super.onPreExecute();
            dialog1 = new ProgressDialog(SignupActivity.this);
            dialog1.setMessage("Please wait....");
            dialog1.setCanceledOnTouchOutside(false);
            dialog1.show();
        }

        protected Object doInBackground(Object... params) {

            String sendProfileData = getResources().getString(R.string.api_url) + "signup".trim();

            DefaultHttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(sendProfileData);

            MultipartEntity mpEntity = new MultipartEntity();


            File file = new File(mFileTemp.getPath());
            ContentBody cbFile = new FileBody(file, "image/*");
            mpEntity.addPart("profile_picture", cbFile);
            Log.v("file path", "avilable");


            try {
                mpEntity.addPart("name", new StringBody(AgentName));
                mpEntity.addPart("agent_email", new StringBody(AgentEmail));
                mpEntity.addPart("password", new StringBody(Password));
                mpEntity.addPart("cell_phone", new StringBody(cell_phone));
                mpEntity.addPart("shopper_groups", new StringBody(shopper_groups));
                mpEntity.addPart("office_branch", new StringBody(office_branch));
                mpEntity.addPart("mailing_address", new StringBody(mailing_address));
                mpEntity.addPart("mailing_zip", new StringBody(mailing_zip));
                mpEntity.addPart("city", new StringBody(city));
                mpEntity.addPart("state", new StringBody(state));
                mpEntity.addPart("company", new StringBody(company));
                mpEntity.addPart("secondary_email", new StringBody(secondary_email));
                mpEntity.addPart("website", new StringBody(website));

            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {

                post.setEntity(mpEntity);
                HttpResponse response = client.execute(post);
                HttpEntity responseEntity = response.getEntity();
                responseString = EntityUtils.toString(responseEntity);

                Log.d("update pro response", "" + responseString);

            } catch (Exception e) {
                // TODO: handle exception
            }
            return mpEntity;

        }

        protected void onPostExecute(Object result) {

            super.onPostExecute(result);
            try {
                dialog1.cancel();
                if (!responseString.equalsIgnoreCase("")
                        || responseString.length() > 0) {
                    JSONObject jsonObject = new JSONObject(responseString);
                    boolean success = jsonObject.getBoolean("success");
                    String message = jsonObject.getString("message");

                    if (success) {
                        Toast.makeText(SignupActivity.this, message, Toast.LENGTH_LONG)
                                .show();
                        Intent i = new Intent(SignupActivity.this, HomeActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
                    }
                } else {

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
