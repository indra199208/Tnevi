package com.app.tnevi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import com.app.tnevi.Allurl.Allurl;
import com.app.tnevi.internet.CheckConnectivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Checkoutbankinfo extends AppCompatActivity {


    ImageView btnEdit, btn_back, btnEditpaypal;
    String username, useremail, token, msg, about;
    private static final String TAG = "Myapp";
    private static final String SHARED_PREFS = "sharedPrefs";
    TextView tvUser, tvAccountnumber, tvBranchno, tvBranchAddress, tvTandC, tvpaypalUsername, tvEmail, tvPhone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkoutbankinfo);
        btnEdit = findViewById(R.id.btnEdit);
        btn_back = findViewById(R.id.btn_back);
        tvUser = findViewById(R.id.tvUser);
        tvAccountnumber = findViewById(R.id.tvAccountnumber);
        tvBranchno = findViewById(R.id.tvBranchno);
        tvBranchAddress = findViewById(R.id.tvBranchAddress);
        tvpaypalUsername = findViewById(R.id.tvpaypalUsername);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhone);
        tvTandC = findViewById(R.id.tvTandC);
        btnEditpaypal = findViewById(R.id.btnEditpaypal);

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

        tvTandC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String url = "http://www.tnevi.com/privacypolicy";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Checkoutbankinfo.this, Checkoutaddbankacc.class);
                startActivity(intent);
            }
        });

        btnEditpaypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(Checkoutbankinfo.this, Editpaypal.class);
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
                                    String full_name = "", branch_no = "", account_no = "", bank_address = "", email_address = "",
                                            phone_no = "", paypaluser = "";
                                    JSONArray bankarray = userdeatisObj.getJSONArray("bank_details");
                                    for (int i = 0; i < bankarray.length(); i++) {
                                        JSONObject bankObj = bankarray.getJSONObject(0);
                                        if (bankObj.getString("type").equals("2")) {
                                            if (!bankObj.isNull("full_name")) {
                                                full_name = bankObj.getString("full_name");
                                            } else {
                                                full_name = "";
                                            }

                                            if (!bankObj.isNull("branch_no")) {
                                                branch_no = bankObj.getString("branch_no");
                                            } else {
                                                branch_no = "";
                                            }

                                            if (!bankObj.isNull("account_no")) {
                                                account_no = bankObj.getString("account_no");
                                            } else {
                                                account_no = "";
                                            }

                                            if (!bankObj.isNull("bank_address")) {
                                                bank_address = bankObj.getString("bank_address");
                                            } else {
                                                bank_address = "";
                                            }

                                            if (bankarray.length() > 1) {
                                                JSONObject bankObj2 = bankarray.getJSONObject(1);
                                                if (!bankObj2.isNull("email_address")) {
                                                    email_address = bankObj2.getString("email_address");
                                                } else {
                                                    email_address = "";
                                                }
                                                if (!bankObj2.isNull("phone_no")) {
                                                    phone_no = bankObj2.getString("phone_no");
                                                } else {
                                                    phone_no = "";
                                                }
                                                if (!bankObj2.isNull("full_name")) {
                                                    paypaluser = bankObj.getString("full_name");
                                                } else {
                                                    paypaluser = "";
                                                }

                                            }

                                            tvAccountnumber.setText(account_no);
                                            tvBranchAddress.setText(bank_address);
                                            tvBranchno.setText(branch_no);
                                            tvUser.setText(full_name);
                                            tvEmail.setText(email_address);
                                            tvPhone.setText(phone_no);
                                            tvpaypalUsername.setText(paypaluser);

                                        } else {
                                            if (!bankObj.isNull("email_address")) {
                                                email_address = bankObj.getString("email_address");
                                            } else {
                                                email_address = "";
                                            }
                                            if (!bankObj.isNull("phone_no")) {
                                                phone_no = bankObj.getString("phone_no");
                                            } else {
                                                phone_no = "";
                                            }
                                            if (!bankObj.isNull("full_name")) {
                                                paypaluser = bankObj.getString("full_name");
                                            } else {
                                                paypaluser = "";
                                            }
                                            if (bankarray.length() > 1) {
                                                JSONObject bankObj2 = bankarray.getJSONObject(1);
                                                if (!bankObj2.isNull("full_name")) {
                                                    full_name = bankObj2.getString("full_name");
                                                } else {
                                                    full_name = "";
                                                }

                                                if (!bankObj2.isNull("branch_no")) {
                                                    branch_no = bankObj2.getString("branch_no");
                                                } else {
                                                    branch_no = "";
                                                }

                                                if (!bankObj2.isNull("account_no")) {
                                                    account_no = bankObj2.getString("account_no");
                                                } else {
                                                    account_no = "";
                                                }

                                                if (!bankObj2.isNull("bank_address")) {
                                                    bank_address = bankObj2.getString("bank_address");
                                                } else {
                                                    bank_address = "";
                                                }
                                            }

                                            tvAccountnumber.setText(account_no);
                                            tvBranchAddress.setText(bank_address);
                                            tvBranchno.setText(branch_no);
                                            tvUser.setText(full_name);
                                            tvEmail.setText(email_address);
                                            tvPhone.setText(phone_no);
                                            tvpaypalUsername.setText(paypaluser);

                                        }

                                    }

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