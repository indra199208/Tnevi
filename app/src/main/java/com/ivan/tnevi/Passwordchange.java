package com.ivan.tnevi;

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

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ivan.tnevi.Allurl.Allurl;
import com.example.tnevi.R;
import com.ivan.tnevi.internet.CheckConnectivity;

import org.json.JSONException;
import org.json.JSONObject;

public class Passwordchange extends AppCompatActivity {

    String email, otp, newpassword, confirmpassword;
    EditText etNewPassword, etConfirmPassword;
    TextView etChangepassword;
    Button btnSave;
    ImageView btn_back;
    private static final String TAG = "Myapp";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passwordchange);
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        otp = intent.getStringExtra("otp");

        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnSave = findViewById(R.id.btnSave);
        btn_back = findViewById(R.id.btn_back);


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkblank();


            }
        });


    }

    public void checkblank() {

        newpassword = etNewPassword.getText().toString();
        confirmpassword = etConfirmPassword.getText().toString();

        if (newpassword.length() == 0) {

            Toast.makeText(this, "Please enter a Password", Toast.LENGTH_SHORT).show();

        } else if (confirmpassword.length() == 0) {

            Toast.makeText(this, "Please enter a Confirm Password", Toast.LENGTH_SHORT).show();


        } else if (!newpassword.equals(confirmpassword)) {

            Toast.makeText(this, "Please Check Password and match", Toast.LENGTH_SHORT).show();

        } else {

            changepassword();


        }


    }

    public void changepassword() {

        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {


            showProgressDialog();

            JSONObject params = new JSONObject();

            try {
                params.put("emailid", email);
                params.put("otp_code", otp);
                params.put("password", confirmpassword);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Allurl.ResetPassword, params, response -> {

                Log.i("Response-->", String.valueOf(response));

                try {
                    JSONObject result = new JSONObject(String.valueOf(response));
                    String msg = result.getString("message");
                    Log.d(TAG, "msg-->" + msg);
                    String stat = result.getString("stat");
                    if (stat.equals("succ")) {

                        Toast.makeText(Passwordchange.this, msg, Toast.LENGTH_SHORT).show();
                        hideProgressDialog();
                        Intent intent = new Intent(Passwordchange.this, Login.class);
                        startActivity(intent);
                        Log.d(TAG, "unsuccessfull - " + "Error");


                    } else {
                        Toast.makeText(Passwordchange.this, msg, Toast.LENGTH_SHORT).show();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideProgressDialog();

                //TODO: handle success
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(Passwordchange.this, "invalid", Toast.LENGTH_SHORT).show();

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