package com.app.tnevi;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
import com.app.tnevi.Adapters.SellingAdapter;
import com.app.tnevi.Utils.AutoResizeTextView;
import com.app.tnevi.internet.CheckConnectivity;
import com.app.tnevi.model.GeteventModel;
import com.app.tnevi.session.SessionManager;
import com.app.tnevi.Allurl.Allurl;

import com.app.tnevi.Utils.SwipeActivityClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Sellingticket extends SwipeActivityClass {

    LinearLayoutCompat btnMytickets, btnSold, btnFav, btnSelling, btnMysearch;
    LinearLayoutCompat btnHome, btnSearch, btnWallet, btnProf, btnSettings, btnEvent;
    AutoResizeTextView tvSelling;
    RelativeLayout relGenerate;
    private static final String TAG = "myapp";
    private static final String SHARED_PREFS = "sharedPrefs";
    SessionManager sessionManager;
    String useremail, username, token;
    private RecyclerView rvSelling;
    ArrayList<GeteventModel> geteventModelArrayList;
    private SellingAdapter sellingAdapter;
    View myTicketU, myEventU, myFavU, mySellingU, mySoldU;
    LinearLayout ll_selling;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sellingticket);
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
        tvSelling =  findViewById(R.id.tvSelling);
        relGenerate = findViewById(R.id.relGenerate);
        rvSelling = findViewById(R.id.rvSelling);
        ll_selling = findViewById(R.id.ll_selling);

        myTicketU = findViewById(R.id.myTicketU);
        myEventU = findViewById(R.id.myEventU);
        myFavU = findViewById(R.id.myFavU);
        mySellingU = findViewById(R.id.mySellingU);
        mySoldU = findViewById(R.id.mySoldU);



        tvSelling.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
        myEventU.setVisibility(View.INVISIBLE);
        myTicketU.setVisibility(View.INVISIBLE);
        myFavU.setVisibility(View.INVISIBLE);
        mySellingU.setVisibility(View.VISIBLE);
        mySoldU.setVisibility(View.INVISIBLE);

        sessionManager = new SessionManager(getApplicationContext());
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        username = sharedPreferences.getString("name", "");
        useremail = sharedPreferences.getString("email", "");



        onClick();
        getEvent();



    }

    @Override
    protected void onSwipeRight() {
        Intent intent = new Intent(Sellingticket.this, Favticket.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    @Override
    protected void onSwipeLeft() {
        Intent intent = new Intent(Sellingticket.this, Soldticket.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }



    public void onClick(){

        btnMysearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(Sellingticket.this, Events.class);
                startActivity(intent);
            }
        });


        btnSold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Sellingticket.this, Soldticket.class);
                startActivity(intent);
            }
        });


        btnMytickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Sellingticket.this, Mytickets.class);
                startActivity(intent);

            }
        });

        btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Sellingticket.this, Favticket.class);
                startActivity(intent);

            }
        });


        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Sellingticket.this, MainActivity.class);
                startActivity(intent);

            }
        });


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Sellingticket.this, Search.class);
                startActivity(intent);
            }
        });


        btnProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Sellingticket.this, Profile.class);
                startActivity(intent);

            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Sellingticket.this, Settings.class);
                startActivity(intent);


            }
        });

        btnWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(Sellingticket.this, Wallet.class);
                startActivity(intent);

            }
        });


        btnEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(Sellingticket.this, Events.class);
                startActivity(intent);

            }
        });


    }

    public void getEvent(){

        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {

            showProgressDialog();
            JSONObject params = new JSONObject();

            try {
                params.put("page_no", "1");
                params.put("commission", "");
                params.put("search_address ", "");
                params.put("categories", "");
                params.put("search_date", "");
                params.put("keyword", "");
                params.put("featured_event", "");
                params.put("fev_list", "");
                params.put("soldout_stat", "");
                params.put("highlight_event", "");
                params.put("sort_by_name", "");
                params.put("top_events", "");
                params.put("my_ticket", "");
                params.put("selling_stat", "1");
                params.put("sort_by_date", "");
                params.put("latitude", "22.4752712");
                params.put("longitude", "88.4033014");

            } catch (JSONException e) {
                e.printStackTrace();
            }


            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Allurl.GetEvent, params, response -> {

                Log.i("Response-->", String.valueOf(response));

                try {
                    JSONObject result = new JSONObject(String.valueOf(response));
                    String msg = result.getString("message");
                    Log.d(TAG, "msg-->" + msg);
                    String stat = result.getString("stat");
                    if (stat.equals("succ")) {

                        geteventModelArrayList = new ArrayList<>();
                        JSONArray response_data = result.getJSONArray("data");
                        for (int i = 0; i < response_data.length(); i++) {

                            GeteventModel geteventModel = new GeteventModel();
                            JSONObject responseobj = response_data.getJSONObject(i);
                            geteventModel.setId(responseobj.getString("id"));
                            geteventModel.setEvent_name(responseobj.getString("event_name"));
                            if (!responseobj.isNull("event_image")) {
                                geteventModel.setEvent_image(responseobj.getString("event_image"));
                            } else {
                                geteventModel.setEvent_image("");
                            }
                            geteventModel.setFree_stat(responseobj.getString("free_stat"));
                            geteventModel.setCurrency_id(responseobj.getString("currency_id"));
                            geteventModel.setEvent_date(responseobj.getString("event_date"));
                            geteventModel.setMax_price(responseobj.getString("max_price"));
                            geteventModel.setMin_price(responseobj.getString("min_price"));
                            geteventModel.setEvent_address(responseobj.getString("event_address"));
                            geteventModel.setStatus(responseobj.getString("status"));
                            geteventModel.setEvent_commission(responseobj.getString("event_commission"));
                            geteventModel.setTicket_stat(responseobj.getString("ticket_stat"));
                            geteventModel.setFav_status(responseobj.getString("fav_status"));
                            geteventModelArrayList.add(geteventModel);

                        }

                        setupRecycler();

                    } else {

                        Log.d(TAG, "unsuccessfull - " + "Error");
                        Toast.makeText(Sellingticket.this, "invalid", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideProgressDialog();

                //TODO: handle success
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Sellingticket.this, error.toString(), Toast.LENGTH_SHORT).show();

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

        sellingAdapter = new SellingAdapter(this, geteventModelArrayList);
        rvSelling.setAdapter(sellingAdapter);
        rvSelling.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

    }


    public void addRemovefav(GeteventModel geteventModel){


        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {

            showProgressDialog();

            JSONObject params = new JSONObject();

            try {
                params.put("event_id", geteventModel.getId());

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

                        Toast.makeText(Sellingticket.this, msg, Toast.LENGTH_SHORT).show();

                    } else {

                        Log.d(TAG, "unsuccessfull - " + "Error");
                        Toast.makeText(Sellingticket.this, "Add to Favourite not successfull", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideProgressDialog();

                //TODO: handle success
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Sellingticket.this, error.toString(), Toast.LENGTH_SHORT).show();

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

        Intent intent = new Intent(Sellingticket.this, Events.class);
        startActivity(intent);

    }
}