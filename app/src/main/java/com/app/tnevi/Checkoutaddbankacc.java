package com.app.tnevi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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

public class Checkoutaddbankacc extends AppCompatActivity {

    ImageView btn_back;
    EditText etName, etAddress, etBranchno, etTransitno, etAccno, etSwiftcode, etBankAddress;
    LinearLayout btnSave;
    String username, useremail, token, msg, about;
    TextView tvTandC;
    private static final String TAG = "Myapp";
    private static final String SHARED_PREFS = "sharedPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkoutaddbankacc);
        btn_back = findViewById(R.id.btn_back);
        etName = findViewById(R.id.etName);
        etAddress = findViewById(R.id.etAddress);
        etBranchno = findViewById(R.id.etBranchno);
        etTransitno = findViewById(R.id.etTransitno);
        etAccno = findViewById(R.id.etAccno);
        etSwiftcode = findViewById(R.id.etSwiftcode);
        etBankAddress = findViewById(R.id.etBankAddress);
        btnSave = findViewById(R.id.btnSave);
        tvTandC = findViewById(R.id.tvTandC);
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

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (etName.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Enter Full Name", Toast.LENGTH_LONG).show();
                } else if (etAddress.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Enter Address", Toast.LENGTH_LONG).show();
                } else if (etBranchno.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Enter Branch No.", Toast.LENGTH_LONG).show();
                } else if (etTransitno.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Enter Transit No.", Toast.LENGTH_LONG).show();
                } else if (etAccno.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Enter Account No.", Toast.LENGTH_LONG).show();
                } else if (etSwiftcode.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Enter Swift Code", Toast.LENGTH_LONG).show();
                } else if (etBankAddress.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Enter Bank Address", Toast.LENGTH_LONG).show();
                } else {
                    submitAccount();
                }

            }
        });

        getAccount();

    }

    public void submitAccount() {

        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {


            showProgressDialog();

            JSONObject params = new JSONObject();

            try {
                params.put("updateBankInformation", "1");
                params.put("type", "2");
                params.put("full_name", etName.getText().toString());
                params.put("account_no", etAccno.getText().toString());
                params.put("address", etAddress.getText().toString());
                params.put("branch_no", etBranchno.getText().toString());
                params.put("transit_no", etTransitno.getText().toString());
                params.put("swift_code", etSwiftcode.getText().toString());
                params.put("bank_address", etBankAddress.getText().toString());


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
                                    String full_name = "", branch_no = "", transit_no = "", account_no = "", swift_code = "",
                                            bank_address = "", address = "";
                                    JSONArray bankarray = userdeatisObj.getJSONArray("bank_details");
                                    for (int i = 0; i < bankarray.length(); i++) {
                                        JSONObject bankObj = bankarray.getJSONObject(0);
                                        if (bankObj.getString("type").equals("2")) {
                                            full_name = !bankObj.isNull("full_name") ? bankObj.getString("full_name") : "";
                                            branch_no = !bankObj.isNull("branch_no") ? bankObj.getString("branch_no") : "";
                                            transit_no = !bankObj.isNull("transit_no") ? bankObj.getString("transit_no") : "";
                                            account_no = !bankObj.isNull("account_no") ? bankObj.getString("account_no") : "";
                                            swift_code = !bankObj.isNull("swift_code") ? bankObj.getString("swift_code") : "";
                                            bank_address = !bankObj.isNull("bank_address") ? bankObj.getString("bank_address") : "";
                                            address = !bankObj.isNull("address") ? bankObj.getString("address") : "";

                                            if (bankarray.length() > 1) {
                                                JSONObject bankObj2 = bankarray.getJSONObject(1);
                                                full_name = !bankObj2.isNull("full_name") ? bankObj2.getString("full_name") : "";
                                                branch_no = !bankObj2.isNull("branch_no") ? bankObj2.getString("branch_no") : "";
                                                transit_no = !bankObj2.isNull("transit_no") ? bankObj2.getString("transit_no") : "";
                                                account_no = !bankObj2.isNull("account_no") ? bankObj2.getString("account_no") : "";
                                                swift_code = !bankObj2.isNull("swift_code") ? bankObj2.getString("swift_code") : "";
                                                bank_address = !bankObj2.isNull("bank_address") ? bankObj2.getString("bank_address") : "";
                                                address = !bankObj2.isNull("address") ? bankObj2.getString("address") : "";

                                            }

                                            etAccno.setText(account_no);
                                            etAddress.setText(address);
                                            etName.setText(full_name);
                                            etBranchno.setText(branch_no);
                                            etSwiftcode.setText(swift_code);
                                            etBankAddress.setText(bank_address);
                                            etTransitno.setText(transit_no);

                                        } else {

                                            if (bankarray.length() > 1) {
                                                JSONObject bankObj2 = bankarray.getJSONObject(1);
                                                full_name = !bankObj2.isNull("full_name") ? bankObj2.getString("full_name") : "";
                                                branch_no = !bankObj2.isNull("branch_no") ? bankObj2.getString("branch_no") : "";
                                                transit_no = !bankObj2.isNull("transit_no") ? bankObj2.getString("transit_no") : "";
                                                account_no = !bankObj2.isNull("account_no") ? bankObj2.getString("account_no") : "";
                                                swift_code = !bankObj2.isNull("swift_code") ? bankObj2.getString("swift_code") : "";
                                                bank_address = !bankObj2.isNull("bank_address") ? bankObj2.getString("bank_address") : "";
                                                address = !bankObj2.isNull("address") ? bankObj2.getString("address") : "";

                                            }

                                            etAccno.setText(account_no);
                                            etAddress.setText(address);
                                            etName.setText(full_name);
                                            etBranchno.setText(branch_no);
                                            etSwiftcode.setText(swift_code);
                                            etBankAddress.setText(bank_address);
                                            etTransitno.setText(transit_no);
                                        }


                                    }

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