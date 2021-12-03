package com.example.tnevi;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.android.volley.toolbox.Volley;
import com.example.tnevi.Adapters.MyeventAdapter;
import com.example.tnevi.Adapters.PlanAdapter;
import com.example.tnevi.Allurl.Allurl;
import com.example.tnevi.Utils.AutoResizeTextView;
import com.example.tnevi.Utils.SwipeActivityClass;
import com.example.tnevi.internet.CheckConnectivity;
import com.example.tnevi.model.MyeventModel;
import com.example.tnevi.retrofit.ApiClient;
import com.example.tnevi.retrofit.ApiInterface;
import com.example.tnevi.session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;


public class Events extends SwipeActivityClass {

    LinearLayoutCompat btnMytickets, btnSold, btnFav, btnSelling, btnMysearch;
    LinearLayoutCompat btnHome, btnSearch, btnWallet, btnProf, btnSettings, btnEvent;
    AutoResizeTextView myEvents;
    ImageView iconEvent;
    RelativeLayout relGenerate;
    private static final String TAG = "myapp";
    private static final String SHARED_PREFS = "sharedPrefs";
    SessionManager sessionManager;
    String useremail, username, token;
    private RecyclerView rvMyevents;
    private MyeventAdapter myeventAdapter;
    ArrayList<MyeventModel> myeventModelArrayList;
    View myTicketU, myEventU, myFavU, mySellingU, mySoldU;
    LinearLayout ll_event;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        btnMytickets = (LinearLayoutCompat) findViewById(R.id.btnMytickets);
        btnSold = (LinearLayoutCompat) findViewById(R.id.btnSold);
        btnFav = (LinearLayoutCompat) findViewById(R.id.btnFav);
        btnSelling = (LinearLayoutCompat) findViewById(R.id.btnSelling);
        btnMysearch = (LinearLayoutCompat) findViewById(R.id.btnMysearch);
        btnSearch = (LinearLayoutCompat) findViewById(R.id.btnSearch);
        btnWallet = (LinearLayoutCompat) findViewById(R.id.btnWallet);
        btnProf = (LinearLayoutCompat) findViewById(R.id.btnProf);
        btnSettings = (LinearLayoutCompat) findViewById(R.id.btnSettings);
        btnEvent = (LinearLayoutCompat) findViewById(R.id.btnEvent);
        btnHome = (LinearLayoutCompat) findViewById(R.id.btnHome);
        myEvents = findViewById(R.id.myEvents);
        iconEvent = findViewById(R.id.iconEvent);
        relGenerate = findViewById(R.id.relGenerate);
        rvMyevents = findViewById(R.id.rvMyevents);
        ll_event = findViewById(R.id.ll_event);

        myTicketU = findViewById(R.id.myTicketU);
        myEventU = findViewById(R.id.myEventU);
        myFavU = findViewById(R.id.myFavU);
        mySellingU = findViewById(R.id.mySellingU);
        mySoldU = findViewById(R.id.mySoldU);


        myEvents.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
        myEventU.setVisibility(View.VISIBLE);
        myTicketU.setVisibility(View.INVISIBLE);
        myFavU.setVisibility(View.INVISIBLE);
        mySellingU.setVisibility(View.INVISIBLE);
        mySoldU.setVisibility(View.INVISIBLE);


        sessionManager = new SessionManager(getApplicationContext());
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        username = sharedPreferences.getString("name", "");
        useremail = sharedPreferences.getString("email", "");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            iconEvent.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.colorAccent));
        }


        onclick();
        myEvents();


    }

    @Override
    protected void onSwipeRight() {
        Intent intent = new Intent(Events.this, Mytickets.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    @Override
    protected void onSwipeLeft() {
        Intent intent = new Intent(Events.this, Favticket.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }



    public void onclick() {

        btnMytickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(Events.this, Mytickets.class);
                startActivity(intent);
            }
        });


        btnSold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Events.this, Soldticket.class);
                startActivity(intent);
            }
        });


        btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Events.this, Favticket.class);
                startActivity(intent);

            }
        });

        btnSelling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Events.this, Sellingticket.class);
                startActivity(intent);

            }
        });


        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Events.this, MainActivity.class);
                startActivity(intent);

            }
        });


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Events.this, Search.class);
                startActivity(intent);
            }
        });


        btnProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Events.this, Profile.class);
                startActivity(intent);

            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Events.this, Settings.class);
                startActivity(intent);


            }
        });

        btnWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(Events.this, Wallet.class);
                startActivity(intent);

            }
        });





    }


    public void myEvents() {

        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {


            showProgressDialog();

            JSONObject params = new JSONObject();

            try {
                params.put("page_no", "3");

            } catch (JSONException e) {
                e.printStackTrace();
            }


            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Allurl.GetMyEvents, params, response -> {

                Log.i("Response-->", String.valueOf(response));

                try {
                    JSONObject result = new JSONObject(String.valueOf(response));
                    String msg = result.getString("message");
                    Log.d(TAG, "msg-->" + msg);
                    String stat = result.getString("stat");
                    if (stat.equals("succ")) {

                        myeventModelArrayList = new ArrayList<>();
                        JSONArray response_data = result.getJSONArray("data");
                        for (int i = 0; i < response_data.length(); i++) {

                            MyeventModel myeventModel = new MyeventModel();
                            JSONObject responseobj = response_data.getJSONObject(i);
                            myeventModel.setId(responseobj.getString("id"));
                            myeventModel.setEvent_name(responseobj.getString("event_name"));
                            if (!responseobj.isNull("event_image")) {
                                myeventModel.setEvent_image(responseobj.getString("event_image"));
                            } else {
                                myeventModel.setEvent_image("");
                            }
                            myeventModel.setFree_stat(responseobj.getString("free_stat"));
                            myeventModel.setCurrency_id(responseobj.getInt("currency_id"));
                            myeventModel.setEvent_date(responseobj.getString("event_date"));
                            myeventModel.setMax_price(responseobj.getString("max_price"));
                            myeventModel.setMin_price(responseobj.getString("min_price"));
                            myeventModel.setEvent_address(responseobj.getString("event_address"));
                            myeventModel.setEvent_commission(responseobj.getString("event_commission"));
                            myeventModel.setStatus(responseobj.getString("status"));
                            myeventModel.setFav_status(responseobj.getString("fav_status"));
                            myeventModelArrayList.add(myeventModel);

                        }

                        setupRecycler();

                    } else {

                        Log.d(TAG, "unsuccessfull - " + "Error");
                        Toast.makeText(Events.this, "invalid", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideProgressDialog();

                //TODO: handle success
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Events.this, error.toString(), Toast.LENGTH_SHORT).show();

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


    private void setupRecycler() {

        myeventAdapter = new MyeventAdapter(this, myeventModelArrayList);
        rvMyevents.setAdapter(myeventAdapter);
        rvMyevents.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

    }


    public void addRemovefav(MyeventModel myeventModel){


        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {

            showProgressDialog();

            JSONObject params = new JSONObject();

            try {
                params.put("event_id", myeventModel.getId());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Allurl.AddRemoveFav, params, response -> {

                Log.i("Response-->", String.valueOf(response));
                try {
                    JSONObject result = new JSONObject(String.valueOf(response));
                    String msg = result.getString("message");
                    Log.d(TAG, "msg-->" + msg);
                    String stat = result.getString("stat");
                    if (stat.equals("succ")) {

                        Toast.makeText(Events.this, msg, Toast.LENGTH_SHORT).show();

                    } else {

                        Log.d(TAG, "unsuccessfull - " + "Error");
                        Toast.makeText(Events.this, "Add to Favourite not successfull", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideProgressDialog();

                //TODO: handle success
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Events.this, error.toString(), Toast.LENGTH_SHORT).show();

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


    public void deleteEvent(MyeventModel myeventModel, int pos) {

        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {

            showProgressDialog();
            HashMap data = new HashMap();
            data.put("event_id", myeventModel.getId());
            Map<String, String> params = new HashMap<String, String>();
            params.put("event_id", myeventModel.getId());
            ApiInterface uploadAPIs = ApiClient.getRetrofitInstance().create(ApiInterface.class);
            Call<ResponseBody> call = uploadAPIs.deleteObject(token, params);

            call.enqueue(new Callback<ResponseBody>() {


                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {

                    try {
                        if (response.body() != null) {
                            String responsetsr = response.body().string();
                            Log.v("Response", responsetsr);
                            Toast.makeText(Events.this, "Successfully delete your event", Toast.LENGTH_SHORT).show();
                            myeventModelArrayList.remove(pos);
                            myeventAdapter.notifyDataSetChanged();
//                            myEvents();

                        } else {
                            if (response.errorBody() != null) {
                                String errorResponse = response.errorBody().string();
                                Log.v("errorResponse", errorResponse);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    hideProgressDialog();

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                    Log.v("faul", t.getMessage());
                    hideProgressDialog();

                }

            });

        }

    }

    public void MarkSold(MyeventModel myeventModel){

        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {

            showProgressDialog();

            JSONObject params = new JSONObject();

            try {
                params.put("event_id", myeventModel.getId());
                params.put("mark_sold", "1");


            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Allurl.MarkSold, params, response -> {

                Log.i("Response-->", String.valueOf(response));
                try {
                    JSONObject result = new JSONObject(String.valueOf(response));
                    String msg = result.getString("message");
                    Log.d(TAG, "msg-->" + msg);
                    String stat = result.getString("stat");
                    if (stat.equals("succ")) {

                        Toast.makeText(Events.this, "Event Mark as Sold!", Toast.LENGTH_SHORT).show();
                        myEvents();

                    } else {

                        Log.d(TAG, "unsuccessfull - " + "Error");
                        Toast.makeText(Events.this, msg, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideProgressDialog();

                //TODO: handle success
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Events.this, error.toString(), Toast.LENGTH_SHORT).show();

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


    @Override
    public void onBackPressed() {

        Intent intent = new Intent(Events.this, MainActivity.class);
        startActivity(intent);

    }
}