package com.example.tnevi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.tnevi.Allurl.Allurl;
import com.example.tnevi.internet.CheckConnectivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Contactsupport extends AppCompatActivity {

    EditText etName, etEmail, etPhone, etSub, etMessage;
    Button btnSubmit;
    ImageView btn_back;
    String token;
    private static final String SHARED_PREFS = "sharedPrefs";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactsupport);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etSub = findViewById(R.id.etSub);
        etMessage = findViewById(R.id.etMessage);
        btnSubmit = findViewById(R.id.btnSubmit);
        btn_back = findViewById(R.id.btn_back);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (etName.getText().toString().length()==0){
                    Toast.makeText(getApplicationContext(), "Please enter Name", Toast.LENGTH_SHORT).show();
                }else if (etEmail.getText().toString().length()==0){
                    Toast.makeText(getApplicationContext(), "Please enter Email", Toast.LENGTH_SHORT).show();
                }else if (etPhone.getText().toString().length()==0){
                    Toast.makeText(getApplicationContext(), "Please enter Phone number", Toast.LENGTH_SHORT).show();
                }else if (etMessage.getText().toString().length()==0){
                    Toast.makeText(getApplicationContext(), "Please enter your message", Toast.LENGTH_SHORT).show();
                }else if (etSub.getText().toString().length()==0){
                    Toast.makeText(getApplicationContext(), "Please enter your Subject", Toast.LENGTH_SHORT).show();
                }else {
                    submitcontact();
                }
            }
        });
    }

    public void submitcontact(){

        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {
            showProgressDialog();
            JSONObject params = new JSONObject();

            try {
                params.put("name", etName.getText().toString());
                params.put("email", etEmail.getText().toString());
                params.put("phone_no", etPhone.getText().toString());
                params.put("subject", etSub.getText().toString());
                params.put("message", etMessage.getText().toString());


            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Allurl.ContactSupport, params, response -> {

                Log.i("Response-->", String.valueOf(response));

                try {
                    JSONObject result = new JSONObject(String.valueOf(response));
                    String msg = result.getString("message");
                    String stat = result.getString("stat");
                    if (stat.equals("succ")) {

                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Contactsupport.this, Settings.class);
                        startActivity(intent);

                    } else {

                        hideProgressDialog();
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                hideProgressDialog();

                //TODO: handle success
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    hideProgressDialog();
                    Toast.makeText(Contactsupport.this, error.toString(), Toast.LENGTH_SHORT).show();

                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", token);
                    return params;
                }
            };

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