package com.app.tnevi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.tnevi.Allurl.Allurl;
import com.app.tnevi.internet.CheckConnectivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Editpaypal extends AppCompatActivity {

    ImageView btn_back;
    LinearLayout btnSave;
    EditText etName, etEmail, etPhone;
    private static final String TAG = "Myapp";
    private static final String SHARED_PREFS = "sharedPrefs";
    String username, useremail, token, msg, about;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editpaypal);
        btn_back = findViewById(R.id.btn_back);
        btnSave = findViewById(R.id.btnSave);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        username = sharedPreferences.getString("name", "");
        useremail = sharedPreferences.getString("email", "");
        token = sharedPreferences.getString("token", "");

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (etName.getText().toString().length() == 0) {

                    Toast.makeText(getApplicationContext(), "Enter Account Holder Name", Toast.LENGTH_LONG).show();

                } else if (etPhone.getText().toString().length() == 0) {

                    Toast.makeText(getApplicationContext(), "Enter Phone Number", Toast.LENGTH_LONG).show();

                } else if (etEmail.getText().toString().length() == 0) {

                    Toast.makeText(getApplicationContext(), "Enter Email", Toast.LENGTH_LONG).show();

                } else {

                    paypalsave();
                }
            }
        });

        getAccount();
    }

    public void paypalsave() {

        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {


            showProgressDialog();

            JSONObject params = new JSONObject();

            try {
                params.put("updateBankInformation", "1");
                params.put("type", "1");
                params.put("full_name", etName.getText().toString());
                params.put("email_address", etEmail.getText().toString());
                params.put("phone_no", etPhone.getText().toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Allurl.UpdateAccount, params, response -> {

                Log.d("Response4-->", String.valueOf(response));

                hideProgressDialog();
                Intent intent = new Intent(Editpaypal.this, Checkoutbankinfo.class);
                startActivity(intent);

                //TODO: handle success
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    hideProgressDialog();
                    Toast.makeText(Editpaypal.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", token);
                    return params;
                }

            };

            Volley.newRequestQueue(Editpaypal.this).add(jsonRequest);


        } else {

            Toast.makeText(getApplicationContext(), "Ooops! Internet Connection Error", Toast.LENGTH_SHORT).show();

        }
    }


    public void getAccount() {

        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {

            showProgressDialog();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, Allurl.GetAccount,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.i("Response3-->", String.valueOf(response));

                            try {
                                JSONObject result = new JSONObject(String.valueOf(response));
                                msg = result.getString("message");
                                Log.d(TAG, "msg-->" + msg);
                                String stat = result.getString("stat");
                                if (stat.equals("succ")) {
                                    JSONObject userdeatisObj = result.getJSONObject("data");
                                    String fname = userdeatisObj.getString("name");
                                    String full_name = "", email_address = "", phone_no = "";
                                    JSONArray bankarray = userdeatisObj.getJSONArray("bank_details");
                                    for (int i = 0; i < bankarray.length(); i++) {
                                        JSONObject bankObj = bankarray.getJSONObject(0);
                                        if (bankObj.getString("type").equals("1")) {
                                            full_name = !bankObj.isNull("full_name") ? bankObj.getString("full_name") : "";
                                            email_address = !bankObj.isNull("email_address") ? bankObj.getString("email_address") : "";
                                            phone_no = !bankObj.isNull("phone_no") ? bankObj.getString("phone_no") : "";
                                            if (bankarray.length() > 1) {
                                                JSONObject paypalObj = bankarray.getJSONObject(1);
                                                full_name = !paypalObj.isNull("full_name") ? paypalObj.getString("full_name") : "";
                                                email_address = !paypalObj.isNull("email_address") ? paypalObj.getString("email_address") : "";
                                                phone_no = !paypalObj.isNull("phone_no") ? paypalObj.getString("phone_no") : "";

                                            }

                                            etName.setText(full_name);
                                            etEmail.setText(email_address);
                                            etPhone.setText(phone_no);

                                        }else {

                                            if (bankarray.length() > 1) {
                                                JSONObject paypalObj = bankarray.getJSONObject(1);
                                                full_name = !paypalObj.isNull("full_name") ? paypalObj.getString("full_name") : "";
                                                email_address = !paypalObj.isNull("email_address") ? paypalObj.getString("email_address") : "";
                                                phone_no = !paypalObj.isNull("phone_no") ? paypalObj.getString("phone_no") : "";

                                            }

                                            etName.setText(full_name);
                                            etEmail.setText(email_address);
                                            etPhone.setText(phone_no);

                                        }


                                    }

                                } else {

                                    hideProgressDialog();
                                    Log.d(TAG, "unsuccessfull - " + "Error");
                                    Toast.makeText(Editpaypal.this, msg, Toast.LENGTH_SHORT).show();
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            hideProgressDialog();


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            hideProgressDialog();
                            Toast.makeText(Editpaypal.this, error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", token);
                    return params;
                }

            };

            Volley.newRequestQueue(this).add(stringRequest);

        } else {

            Toast.makeText(getApplicationContext(), "OOPS! No Internet Connection", Toast.LENGTH_SHORT).show();

        }


    }


    public ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}