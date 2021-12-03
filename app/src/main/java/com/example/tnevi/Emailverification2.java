package com.example.tnevi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.tnevi.Allurl.Allurl;
import com.example.tnevi.internet.CheckConnectivity;

import org.json.JSONException;
import org.json.JSONObject;

public class Emailverification2 extends AppCompatActivity {

    ImageView btn_back;
    EditText etOtp;
    TextView etResendotp, tvSubtitle;
    Button btnVerify;
    String email, OTP;

    private static final String TAG = "Myapp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emailverification2);
        Intent intent = getIntent();
        email = intent.getStringExtra("email");


        btn_back = findViewById(R.id.btn_back);
        etOtp = findViewById(R.id.etOtp);
        etResendotp = findViewById(R.id.etResendotp);
        btnVerify = findViewById(R.id.btnVerify);
        tvSubtitle = findViewById(R.id.tvSubtitle);

        tvSubtitle.setText("Please enter the 4 digit code sent to "+email);


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });


        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                checkblank();


            }
        });


        etResendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resendOTP();

            }
        });

    }


    public void checkblank() {

        OTP = etOtp.getText().toString();

        if (OTP.length() == 0) {

            Toast.makeText(this, "Please enter 4 Digit OTP", Toast.LENGTH_SHORT).show();

        } else if (OTP.length() < 4) {

            Toast.makeText(this, "Please enter 4 Digit OTP", Toast.LENGTH_SHORT).show();


        } else {

            OTPverification();
        }
    }


    public void OTPverification() {

        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {


            showProgressDialog();

            JSONObject params = new JSONObject();

            try {
                params.put("emailid", email);
                params.put("otp_code", OTP);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Allurl.ForgotPassword, params, response -> {

                Log.i("Response-->", String.valueOf(response));

                try {
                    JSONObject result = new JSONObject(String.valueOf(response));
                    String msg = result.getString("message");
                    Log.d(TAG, "msg-->" + msg);
                    String stat = result.getString("stat");
                    if (stat.equals("succ")) {

                        Toast.makeText(Emailverification2.this, msg, Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(Emailverification2.this, Passwordchange.class);
                        intent.putExtra("email", email);
                        intent.putExtra("otp", OTP);
                        startActivity(intent);

                        hideProgressDialog();
                        Log.d(TAG, "unsuccessfull - " + "Error");
                    } else {

                        Toast.makeText(Emailverification2.this, msg, Toast.LENGTH_SHORT).show();


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideProgressDialog();

                //TODO: handle success
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(Emailverification2.this, "invalid", Toast.LENGTH_SHORT).show();

                    error.printStackTrace();
                    //TODO: handle failure
                }
            });

            Volley.newRequestQueue(this).add(jsonRequest);

        } else {

            Toast.makeText(getApplicationContext(), "Ooops! Internet Connection Error", Toast.LENGTH_SHORT).show();


        }

    }


    public void resendOTP() {


        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {

            showProgressDialog();

            JSONObject params = new JSONObject();

            try {
                params.put("emailid", email);


            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Allurl.ResendOTP, params, response -> {

                Log.i("Response-->", String.valueOf(response));

                try {
                    JSONObject result = new JSONObject(String.valueOf(response));
                    String msg = result.getString("message");
                    Log.d(TAG, "msg-->" + msg);
                    String stat = result.getString("stat");
                    if (stat.equals("succ")) {

                        Toast.makeText(Emailverification2.this, msg, Toast.LENGTH_SHORT).show();
                        hideProgressDialog();
                        Log.d(TAG, "unsuccessfull - " + "Error");


                    } else {
                        Toast.makeText(Emailverification2.this, msg, Toast.LENGTH_SHORT).show();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideProgressDialog();

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(Emailverification2.this, "invalid", Toast.LENGTH_SHORT).show();

                    error.printStackTrace();
                    //TODO: handle failure
                }
            });

            Volley.newRequestQueue(this).add(jsonRequest);

        } else {

            Toast.makeText(getApplicationContext(), "Ooops! Internet Connection Error", Toast.LENGTH_SHORT).show();


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