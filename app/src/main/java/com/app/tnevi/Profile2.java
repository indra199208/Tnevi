package com.app.tnevi;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.tnevi.Adapters.ReviewAdapter;
import com.app.tnevi.Allurl.Allurl;
import com.app.tnevi.internet.CheckConnectivity;
import com.app.tnevi.model.ReviewModel;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Profile2 extends AppCompatActivity {

    LinearLayoutCompat btnHome, btnSearch, btnWallet, btnProf, btnSettings, btnEvent;
    ImageView iconProf, imgPrf;
    private static final String SHARED_PREFS = "sharedPrefs";
    String username, useremail, token, msg, about;
    private static final String TAG = "Myapp";
    TextView tvUsername, tvAboutus;
    RecyclerView rv_reviews;
    ReviewAdapter reviewAdapter;
    private ArrayList<ReviewModel> reviewModelArrayList;
    String userid = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile2);
        Intent intent = getIntent();
        userid = intent.getStringExtra("userid");
        btnSearch = findViewById(R.id.btnSearch);
        btnWallet = findViewById(R.id.btnWallet);
        btnProf = findViewById(R.id.btnProf);
        btnSettings = findViewById(R.id.btnSettings);
        btnEvent = findViewById(R.id.btnEvent);
        btnHome = findViewById(R.id.btnHome);
        iconProf = findViewById(R.id.iconProf);
        imgPrf = findViewById(R.id.imgPrf);
        tvUsername = findViewById(R.id.tvUsername);
        rv_reviews = findViewById(R.id.rv_reviews);
        tvAboutus = findViewById(R.id.tvAboutus);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            iconProf.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.colorAccent));
        }
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        username = sharedPreferences.getString("name", "");
        useremail = sharedPreferences.getString("email", "");
        token = sharedPreferences.getString("token", "");

        onClick();
        getAccount();
//        reviewlist();


    }


    public void onClick() {

//        btnAbout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                updateAbout();
//            }
//        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Profile2.this, MainActivity.class);
                startActivity(intent);

            }
        });


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Profile2.this, Search.class);
                startActivity(intent);
            }
        });


        btnEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Profile2.this, Events.class);
                startActivity(intent);

            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Profile2.this, Settings.class);
                startActivity(intent);


            }
        });

        btnWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(Profile2.this, Wallet.class);
                startActivity(intent);

            }
        });
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
                                    JSONObject profileObj = userdeatisObj.getJSONObject("profile");
                                    String pro_pic = profileObj.getString("pro_pic");
                                    about = profileObj.getString("about");
                                    Log.d(TAG, "profileurl-->" + pro_pic);
                                    Glide.with(Profile2.this)
                                            .load("http://dev8.ivantechnology.in/tnevi/storage/app/" + pro_pic)
                                            .circleCrop()
                                            .placeholder(R.drawable.dp2)
                                            .into(imgPrf);
                                    tvUsername.setText(fname);
                                    tvAboutus.setText(about);

                                } else {

                                    hideProgressDialog();
                                    Log.d(TAG, "unsuccessfull - " + "Error");
                                    Toast.makeText(Profile2.this, msg, Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(Profile2.this, error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }) {


                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("user_id", userid);
                    return params;
                }

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

//    public void reviewlist(){
//
//        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {
//
//
//            showProgressDialog();
//
//            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Allurl.Review, null, response -> {
//
//                Log.i("Response-->", String.valueOf(response));
//
//                try {
//                    JSONObject result = new JSONObject(String.valueOf(response));
//                    String msg = result.getString("message");
//                    Log.d(TAG, "msg-->" + msg);
//                    String stat = result.getString("stat");
//                    if (stat.equals("succ")) {
//
//                        reviewModelArrayList = new ArrayList<>();
//                        JSONArray response_data = result.getJSONArray("reviews");
//                        for (int i = 0; i < response_data.length(); i++) {
//
//                            ReviewModel reviewModel = new ReviewModel();
//                            JSONObject responseobj = response_data.getJSONObject(i);
//                            reviewModel.setId(responseobj.getString("id"));
//                            reviewModel.setRating(responseobj.getString("rating"));
//                            reviewModel.setDescription(responseobj.getString("description"));
//                            reviewModel.setEvent_name(responseobj.getString("event_name"));
//                            reviewModel.setUser_name(responseobj.getString("user_name"));
//                            reviewModel.setPro_pic(responseobj.getString("pro_pic"));
//                            reviewModel.setDate(responseobj.getString("date"));
//                            reviewModelArrayList.add(reviewModel);
//
//                        }
//
//                        setupRecycler();
//
//                    } else {
//
//                        Log.d(TAG, "unsuccessfull - " + "Error");
//                        Toast.makeText(Profile2.this, "invalid", Toast.LENGTH_SHORT).show();
//                    }
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                hideProgressDialog();
//
//                //TODO: handle success
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    Toast.makeText(Profile2.this, error.toString(), Toast.LENGTH_SHORT).show();
//
//                }
//            }) {
//                @Override
//                public Map<String, String> getHeaders() throws AuthFailureError {
//                    Map<String, String> params = new HashMap<String, String>();
//                    params.put("Authorization", token);
//                    return params;
//                }
//            };
//
//            Volley.newRequestQueue(this).add(jsonRequest);
//
//        } else {
//
//            Toast.makeText(getApplicationContext(), "Ooops! Internet Connection Error", Toast.LENGTH_SHORT).show();
//
//        }
//
//    }


//    public void updateAbout() {
//
//        final EditText aboutus;
//        Button submit;
//        final Dialog dialog = new Dialog(Profile2.this);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
//        dialog.setContentView(R.layout.dialog2);
//        params.copyFrom(dialog.getWindow().getAttributes());
//        params.height = WindowManager.LayoutParams.MATCH_PARENT;
//        params.width = WindowManager.LayoutParams.MATCH_PARENT;
//        params.gravity = Gravity.CENTER;
//        dialog.getWindow().setAttributes(params);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog.show();
//
//        aboutus = (EditText) dialog.findViewById(R.id.aboutus);
//        submit = (Button) dialog.findViewById(R.id.submit);
//        aboutus.setText(about);
//
//
//        submit.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                if (aboutus.getText().toString().isEmpty()) {
//                    aboutus.setError("Please Enter About Us");
//                } else if (aboutus.getText().toString().length() < 0) {
//                    aboutus.setError("Please enter at least 1 characters");
//
//                } else {
//                    //updating note
//
//                    if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {
//
//
//                        showProgressDialog();
//
//                        JSONObject params = new JSONObject();
//
//                        try {
//                            params.put("about", aboutus.getText().toString());
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Allurl.UpdateAccount, params, response -> {
//
//                            Log.d("Response4-->", String.valueOf(response));
//
//                            hideProgressDialog();
//                            dialog.cancel();
//                            getAccount();
//
//
//                            //TODO: handle success
//                        }, new Response.ErrorListener() {
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//
//                                hideProgressDialog();
//                                Toast.makeText(Profile2.this, error.toString(), Toast.LENGTH_SHORT).show();
//                            }
//                        }) {
//
//                            @Override
//                            public Map<String, String> getHeaders() throws AuthFailureError {
//                                Map<String, String> params = new HashMap<String, String>();
//                                params.put("Authorization", token);
//                                return params;
//                            }
//
//                        };
//
//                        Volley.newRequestQueue(Profile2.this).add(jsonRequest);
//
//
//                    } else {
//
//                        Toast.makeText(getApplicationContext(), "Ooops! Internet Connection Error", Toast.LENGTH_SHORT).show();
//
//                    }
//
//
//                }
//            }
//        });
//    }


//    private void setupRecycler() {
//
//        reviewAdapter = new ReviewAdapter(this, reviewModelArrayList);
//        rv_reviews.setAdapter(reviewAdapter);
//        rv_reviews.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
//
//    }


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