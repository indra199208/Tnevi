package com.ivan.tnevi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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

public class Forgetpassword extends AppCompatActivity {

    EditText etEmail;
    Button btnSend;
    String email;
    private static final String TAG = "Myapp";
    ImageView btn_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpassword);
        etEmail = findViewById(R.id.etEmail);
        btnSend = findViewById(R.id.btnSend);
        btn_back = findViewById(R.id.btn_back);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                checkblank();


            }
        });
    }

    public void checkblank() {

        email = etEmail.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (email.length()==0){

            Toast.makeText(getApplicationContext(),"Invalid email address",Toast.LENGTH_SHORT).show();

        }else {

            forgotpassword();
        }

//        etEmail .addTextChangedListener(new TextWatcher() {
//            public void afterTextChanged(Editable s) {
//
//                if (email.matches(emailPattern) && s.length() > 0)
//                {
//
//                    forgotpassword();
//                }
//                else
//                {
//                    Toast.makeText(getApplicationContext(),"Invalid email address",Toast.LENGTH_SHORT).show();
//                }
//            }
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                // other stuffs
//            }
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                // other stuffs
//            }
//        });

    }


    public void forgotpassword(){

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

                        Toast.makeText(Forgetpassword.this, msg, Toast.LENGTH_SHORT).show();
                        hideProgressDialog();
                        Intent intent = new Intent(Forgetpassword.this, Emailverification2.class);
                        intent.putExtra("email", email);
                        startActivity(intent);
                        Log.d(TAG, "unsuccessfull - " + "Error");


                    } else {
                        Toast.makeText(Forgetpassword.this, msg, Toast.LENGTH_SHORT).show();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideProgressDialog();

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(Forgetpassword.this, error.toString(), Toast.LENGTH_SHORT).show();

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