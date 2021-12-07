package com.ivan.tnevi;

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
import com.ivan.tnevi.Allurl.Allurl;
import com.example.tnevi.R;
import com.ivan.tnevi.internet.CheckConnectivity;
import com.willy.ratingbar.BaseRatingBar;
import com.willy.ratingbar.ScaleRatingBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Ratenow extends AppCompatActivity {

    private static final String TAG = "Myapp";
    ScaleRatingBar ratingBar;
    ImageView btn_back;
    String rating = "", eventId, token;
    EditText etDescription;
    Button btnSubmit;
    private static final String SHARED_PREFS = "sharedPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratenow);
        Intent intent = getIntent();
        eventId = intent.getStringExtra("eventId");
        ratingBar = findViewById(R.id.simpleRatingBar);
        btn_back = findViewById(R.id.btn_back);
        etDescription = findViewById(R.id.etDescription);
        btnSubmit = findViewById(R.id.btnSubmit);


        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");

        ratingBar.setOnRatingChangeListener(new BaseRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(BaseRatingBar baseRatingBar, float v, boolean b) {
                Log.d(TAG, "onRatingChange: " + v);
                rating = Float.toString(v);

            }

        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (rating) {
                    case "":
                        rating = "1";
                        break;
                    case "1.0":
                        rating = "1";
                        break;
                    case "2.0":
                        rating = "2";
                        break;
                    case "3.0":
                        rating = "3";
                        break;
                    case "4.0":
                        rating = "4";
                        break;
                    case "5.0":
                        rating = "5";
                        break;
                }
                Log.d(TAG, "Ratings-->"+rating);

                if (etDescription.getText().toString().length()==0){

                    Toast.makeText(Ratenow.this, "Please Enter your message", Toast.LENGTH_SHORT).show();

                }else {

                    ratingsubmit();
                }
            }
        });



    }


    public void ratingsubmit(){

        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {


            showProgressDialog();

            JSONObject params = new JSONObject();

            try {
                params.put("event_id", eventId);
                params.put("rating", rating);
                params.put("description", etDescription.getText().toString());


            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Allurl.EventReview, params, response -> {

                Log.i("Response-->", String.valueOf(response));

                try {
                    JSONObject result = new JSONObject(String.valueOf(response));
                    String msg = result.getString("message");
                    Log.d(TAG, "msg-->" + msg);
                    String stat = result.getString("stat");
                    if (stat.equals("succ")) {

                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Ratenow.this, Mytickets.class);
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
//                    Toast.makeText(Editevent.this, error.toString(), Toast.LENGTH_SHORT).show();

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