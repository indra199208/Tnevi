package com.ivan.tnevi;

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
import com.ivan.tnevi.Allurl.Allurl;
import com.example.tnevi.R;
import com.ivan.tnevi.internet.CheckConnectivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Checkoutaddbankacc extends AppCompatActivity {

    ImageView  btn_back;
    EditText etIfsc, etAccount, etBank, etBranch;
    LinearLayout btnSave;
    String username, useremail, token, msg, about ;
    private static final String TAG = "Myapp";
    private static final String SHARED_PREFS = "sharedPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkoutaddbankacc);
        btn_back = findViewById(R.id.btn_back);
        etIfsc = findViewById(R.id.etIfsc);
        etAccount = findViewById(R.id.etAccount);
        etBank = findViewById(R.id.etBank);
        etBranch = findViewById(R.id.etBranch);
        btnSave = findViewById(R.id.btnSave);
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

                if (etIfsc.getText().toString().length()==0){
                    Toast.makeText(getApplicationContext(), "Enter IFSC", Toast.LENGTH_LONG).show();
                }else if(etBranch.getText().toString().length()==0){
                    Toast.makeText(getApplicationContext(), "Enter Branch Name", Toast.LENGTH_LONG).show();
                }else if (etAccount.getText().toString().length()==0){
                    Toast.makeText(getApplicationContext(), "Enter Account", Toast.LENGTH_LONG).show();
                }else if (etBank.getText().toString().length()==0){
                    Toast.makeText(getApplicationContext(), "Enter Bank", Toast.LENGTH_LONG).show();
                }else {
                    submitAccount();
                }

            }
        });

        getAccount();

    }

    public void submitAccount(){

        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {


            showProgressDialog();

            JSONObject params = new JSONObject();

            try {
                params.put("updateBankInformation", "1");
                params.put("ifsc", etIfsc.getText().toString());
                params.put("acct_no", etAccount.getText().toString());
                params.put("bank_name", etBank.getText().toString());
                params.put("branch_name", etBranch.getText().toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Allurl.UpdateAccount, params, response -> {

                Log.d("Response4-->", String.valueOf(response));

                hideProgressDialog();
                Intent intent = new Intent(Checkoutaddbankacc.this, Checkoutbankinfo.class);
                startActivity(intent);

                //TODO: handle success
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    hideProgressDialog();
                    Toast.makeText(Checkoutaddbankacc.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", token);
                    return params;
                }

            };

            Volley.newRequestQueue(Checkoutaddbankacc.this).add(jsonRequest);


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
                                    JSONObject bankObj = userdeatisObj.getJSONObject("bank_details");
                                    String acc_no = bankObj.getString("acct_no");
                                    String ifsc = bankObj.getString("ifsc");
                                    String bank_name = bankObj.getString("bank_name");
                                    String branch_name = bankObj.getString("branch_name");
                                    etAccount.setText(acc_no);
                                    etBank.setText(bank_name);
                                    etBranch.setText(branch_name);
                                    etIfsc.setText(ifsc);

                                } else {

                                    hideProgressDialog();
                                    Log.d(TAG, "unsuccessfull - " + "Error");
                                    Toast.makeText(Checkoutaddbankacc.this, msg, Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(Checkoutaddbankacc.this, error.toString(), Toast.LENGTH_SHORT).show();
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