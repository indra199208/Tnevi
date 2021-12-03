package com.example.tnevi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.tnevi.Allurl.Allurl;
import com.example.tnevi.internet.CheckConnectivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Checkoutbankinfo extends AppCompatActivity {


    ImageView btnEdit, btn_back;
    String username, useremail, token, msg, about ;
    private static final String TAG = "Myapp";
    private static final String SHARED_PREFS = "sharedPrefs";
    TextView tvBankname, tvAccountnumber, tvIFSC, tvBranchName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkoutbankinfo);
        btnEdit = findViewById(R.id.btnEdit);
        btn_back = findViewById(R.id.btn_back);
        tvBankname = findViewById(R.id.tvBankname);
        tvAccountnumber = findViewById(R.id.tvAccountnumber);
        tvIFSC = findViewById(R.id.tvIFSC);
        tvBranchName = findViewById(R.id.tvBranchName);

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
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Checkoutbankinfo.this, Checkoutaddbankacc.class);
                startActivity(intent);
            }
        });

        getAccount();
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
                                    tvAccountnumber.setText(acc_no);
                                    tvBankname.setText(bank_name);
                                    tvBranchName.setText(branch_name);
                                    tvIFSC.setText(ifsc);

                                } else {

                                    hideProgressDialog();
                                    Log.d(TAG, "unsuccessfull - " + "Error");
                                    Toast.makeText(Checkoutbankinfo.this, msg, Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(Checkoutbankinfo.this, error.toString(), Toast.LENGTH_SHORT).show();
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