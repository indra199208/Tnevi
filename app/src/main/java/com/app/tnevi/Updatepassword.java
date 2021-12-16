package com.app.tnevi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.app.tnevi.session.SessionManager;
import com.app.tnevi.Allurl.Allurl;

import com.app.tnevi.internet.CheckConnectivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Updatepassword extends AppCompatActivity {

    EditText etOldPassword, etNewPassword, etConfirmPassword;

    Button btnUpdate;
    private static final String SHARED_PREFS = "sharedPrefs";
    SessionManager sessionManager;
    String token;
    private static final String TAG = "Myapp";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatepassword);
        etOldPassword = findViewById(R.id.etOldPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnUpdate = findViewById(R.id.btnUpdate);

        sessionManager = new SessionManager(getApplicationContext());
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                checkblank();
            }
        });


    }

    public void checkblank() {

        if (etOldPassword.getText().toString().length() == 0) {

            Toast.makeText(this, "Please enter Old Password", Toast.LENGTH_SHORT).show();

        } else if (etNewPassword.getText().toString().length() == 0) {

            Toast.makeText(this, "Please enter New Password", Toast.LENGTH_SHORT).show();

        } else if (etConfirmPassword.getText().toString().length() == 0) {

            Toast.makeText(this, "Please enter Confirm Password", Toast.LENGTH_SHORT).show();


        } else if (!etNewPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {

            Toast.makeText(this, "Please Check Password and match", Toast.LENGTH_SHORT).show();
        } else {

            updatePassword();
        }

    }


    public void updatePassword() {

        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {


            showProgressDialog();

            JSONObject params = new JSONObject();
            try {
                params.put("password", etConfirmPassword.getText().toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Allurl.ChangePassword, params, response -> {

                Log.i("Response-->", String.valueOf(response));

                try {
                    JSONObject result = new JSONObject(String.valueOf(response));
                    String msg = result.getString("message");
                    Log.d(TAG, "msg-->" + msg);
                    String stat = result.getString("stat");
                    if (stat.equals("succ")) {

                        Toast.makeText(Updatepassword.this, msg, Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(Updatepassword.this, Settings.class);
                        startActivity(intent);

                        hideProgressDialog();
                        Log.d(TAG, "unsuccessfull - " + "Error");
                    } else {

                        Toast.makeText(Updatepassword.this, msg, Toast.LENGTH_SHORT).show();


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
                    Toast.makeText(Updatepassword.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", token);
                    return params;
                }

            };

            Volley.newRequestQueue(Updatepassword.this).add(jsonRequest);

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