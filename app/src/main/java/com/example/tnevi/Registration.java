package com.example.tnevi;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.tnevi.Allurl.Allurl;
import com.example.tnevi.internet.CheckConnectivity;
import com.example.tnevi.session.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

public class Registration extends AppCompatActivity {

    TextView btnSignin;
    Button btnSignup;
    ImageView btn_back, btnInfo;

    private static final String SHARED_PREFS = "sharedPrefs";
    SessionManager sessionManager;
    private static final String TAG = "Myapp";
    EditText etName, etEmail, etPhone, etPassword, etconfirmPassword, etBusinessname;
    String name, email, phone, password, confirmpassword;
    Switch switchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        btnSignin = findViewById(R.id.btnSignin);
        btnSignup = findViewById(R.id.btnSignup);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        etconfirmPassword = findViewById(R.id.etconfirmPassword);
        btn_back = findViewById(R.id.btn_back);
        switchButton = findViewById(R.id.switchButton);
        etBusinessname = findViewById(R.id.etBusinessname);
        btnInfo = findViewById(R.id.btnInfo);
        sessionManager = new SessionManager(getApplicationContext());


        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Registration.this, Login.class);
                startActivity(intent);

            }
        });


        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                checkblank();

            }
        });


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    etBusinessname.setVisibility(View.VISIBLE);

                } else {

                    etBusinessname.setVisibility(View.GONE);
                }
            }
        });


        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog();

            }
        });


    }


    public void showDialog() {
        LinearLayout btnOk;
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        dialog.setContentView(R.layout.my_dialog);
        dialog.show();
        btnOk = dialog.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

            }
        });
    }

    public void checkblank() {

        name = etName.getText().toString();
        email = etEmail.getText().toString();
        phone = etPhone.getText().toString();
        password = etPassword.getText().toString();
        confirmpassword = etconfirmPassword.getText().toString();


        if (name.length() == 0) {
            Toast.makeText(this, "Please enter Name", Toast.LENGTH_SHORT).show();

        } else if (email.length() == 0) {

            Toast.makeText(this, "Please enter a Valid Email", Toast.LENGTH_SHORT).show();

        } else if (phone.length() == 0) {

            Toast.makeText(this, "Please enter a Phone Number", Toast.LENGTH_SHORT).show();

        } else if (password.length() == 0) {

            Toast.makeText(this, "Please enter a Password", Toast.LENGTH_SHORT).show();

        } else if (confirmpassword.length() == 0) {

            Toast.makeText(this, "Please enter a Confirm Password", Toast.LENGTH_SHORT).show();


        } else if (!password.equals(confirmpassword)) {

            Toast.makeText(this, "Please Check Password and match", Toast.LENGTH_SHORT).show();

        } else {

            Regstration();


        }


    }


    public void Regstration() {

        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {


            showProgressDialog();

            JSONObject params = new JSONObject();

            try {
                params.put("uname", name);
                params.put("emailid", email);
                params.put("phone_no", phone);
                params.put("password", confirmpassword);
                params.put("role", "customer");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Allurl.RegistrationUrl, params, response -> {

                Log.i("Response-->", String.valueOf(response));
                try {
                    JSONObject result = new JSONObject(String.valueOf(response));
                    String msg = result.getString("message");
                    Log.d(TAG, "msg-->" + msg);
                    String stat = result.getString("stat");
                    if (stat.equals("succ")) {

                        Toast.makeText(Registration.this, msg, Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(Registration.this, Emailverification.class);
                        intent.putExtra("email", email);
                        startActivity(intent);

//                        JSONObject userdeatisObj = result.getJSONObject("data");
//
//
//                        id = userdeatisObj.getInt("id");
//                        status = userdeatisObj.getString("status");
//                        role = userdeatisObj.getString("role");
//                        name = userdeatisObj.getString("name");
//                        email = userdeatisObj.getString("email");
//                        email_verification_code = userdeatisObj.getString("email_verification_code");
//                        phone_no = userdeatisObj.getString("phone_no");
//                        otp_time = userdeatisObj.getInt("otp_time");


                        hideProgressDialog();
                        Log.d(TAG, "unsuccessfull - " + "Error");
                        Toast.makeText(Registration.this, msg, Toast.LENGTH_SHORT).show();
                    }else {

                        Toast.makeText(Registration.this, msg, Toast.LENGTH_SHORT).show();


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideProgressDialog();

                //TODO: handle success
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(Registration.this, "invalid", Toast.LENGTH_SHORT).show();

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


    @Override
    public void onBackPressed() {
        //Execute your code here
        finish();

    }


}