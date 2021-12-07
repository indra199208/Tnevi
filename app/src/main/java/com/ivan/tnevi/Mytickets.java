package com.ivan.tnevi;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.ivan.tnevi.Adapters.MyticketAdapter;
import com.ivan.tnevi.Allurl.Allurl;
import com.example.tnevi.R;
import com.ivan.tnevi.Utils.SwipeActivityClass;
import com.ivan.tnevi.internet.CheckConnectivity;
import com.ivan.tnevi.model.GeteventModel;
import com.ivan.tnevi.session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Mytickets extends SwipeActivityClass {

    LinearLayoutCompat btnMytickets, btnSold, btnFav, btnSelling, btnMysearch;
    LinearLayoutCompat btnHome, btnSearch, btnWallet, btnProf, btnSettings, btnEvent;
    TextView myTickets;
    RelativeLayout relGenerate;
    private static final String TAG = "myapp";
    private static final String SHARED_PREFS = "sharedPrefs";
    SessionManager sessionManager;
    String useremail, username, token;
    private RecyclerView rvMytickets;
    private MyticketAdapter myticketAdapter;
    ArrayList<GeteventModel>geteventModelArrayList;
    View myTicketU, myEventU, myFavU, mySellingU, mySoldU;
    LinearLayout ll_mytickets;
    Spinner spSort;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mytickets);
        btnMytickets = findViewById(R.id.btnMytickets);
        btnSold = findViewById(R.id.btnSold);
        btnFav = findViewById(R.id.btnFav);
        btnSelling = findViewById(R.id.btnSelling);
        btnMysearch = findViewById(R.id.btnMysearch);
        btnSearch = findViewById(R.id.btnSearch);
        btnWallet = findViewById(R.id.btnWallet);
        btnProf = findViewById(R.id.btnProf);
        btnSettings = findViewById(R.id.btnSettings);
        btnEvent = findViewById(R.id.btnEvent);
        btnHome = findViewById(R.id.btnHome);
        myTickets = findViewById(R.id.myTickets);
        relGenerate = findViewById(R.id.relGenerate);
        rvMytickets = findViewById(R.id.rvMytickets);
        ll_mytickets = findViewById(R.id.ll_mytickets);

        myTicketU = findViewById(R.id.myTicketU);
        myEventU = findViewById(R.id.myEventU);
        myFavU = findViewById(R.id.myFavU);
        mySellingU = findViewById(R.id.mySellingU);
        mySoldU = findViewById(R.id.mySoldU);
        spSort = findViewById(R.id.spSort);


        myTickets.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
        myEventU.setVisibility(View.INVISIBLE);
        myTicketU.setVisibility(View.VISIBLE);
        myFavU.setVisibility(View.INVISIBLE);
        mySellingU.setVisibility(View.INVISIBLE);
        mySoldU.setVisibility(View.INVISIBLE);

        sessionManager = new SessionManager(getApplicationContext());
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        username = sharedPreferences.getString("name", "");
        useremail = sharedPreferences.getString("email", "");


        onClick();
        getEvent();
        spSort();



    }

    @Override
    protected void onSwipeRight() {

    }

    @Override
    protected void onSwipeLeft() {
        Intent intent = new Intent(Mytickets.this, Events.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    public void onClick(){


        spSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i > 0) {
                    switch (spSort.getSelectedItem().toString()) {
                        case "All":
                            getEvent();
                            break;
                        case "By Date":
                            getEventBydate();
                            break;
                        case "By Name":
                            getEventByname();
                            break;
                        default:
                            getEvent();
                            break;
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnMysearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(Mytickets.this, Events.class);
                startActivity(intent);
            }
        });


        btnSold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Mytickets.this, Soldticket.class);
                startActivity(intent);
            }
        });


        btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Mytickets.this, Favticket.class);
                startActivity(intent);

            }
        });

        btnSelling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Mytickets.this, Sellingticket.class);
                startActivity(intent);

            }
        });


        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Mytickets.this, MainActivity.class);
                startActivity(intent);

            }
        });


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Mytickets.this, Search.class);
                startActivity(intent);
            }
        });


        btnProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Mytickets.this, Profile.class);
                startActivity(intent);

            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Mytickets.this, Settings.class);
                startActivity(intent);


            }
        });

        btnWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(Mytickets.this, Wallet.class);
                startActivity(intent);

            }
        });


        btnEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(Mytickets.this, Events.class);
                startActivity(intent);

            }
        });

    }

    private void spSort() {

        List<String> sort = new ArrayList<String>();

        sort.add("Select");
        sort.add("All");
        sort.add("By Date");
        sort.add("By Name");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sort);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSort.setAdapter(arrayAdapter);


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
                params.put("my_ticket", "1");
                params.put("selling_stat", "");
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
                            geteventModel.setEvent_commission(responseobj.getString("event_commission"));
                            geteventModel.setStatus(responseobj.getString("status"));
                            geteventModel.setTicket_stat(responseobj.getString("ticket_stat"));
                            geteventModel.setFav_status(responseobj.getString("fav_status"));
                            geteventModelArrayList.add(geteventModel);

                        }

                        setupRecycler();

                    } else {

                        Log.d(TAG, "unsuccessfull - " + "Error");
                        Toast.makeText(Mytickets.this, "invalid", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideProgressDialog();

                //TODO: handle success
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Mytickets.this, error.toString(), Toast.LENGTH_SHORT).show();

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



    public void getEventBydate(){

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
                params.put("my_ticket", "1");
                params.put("selling_stat", "");
                params.put("sort_by_date", "ASC");
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
                            geteventModel.setTicket_stat(responseobj.getString("ticket_stat"));
                            geteventModel.setFav_status(responseobj.getString("fav_status"));
                            geteventModelArrayList.add(geteventModel);

                        }

                        setupRecycler();

                    } else {

                        Log.d(TAG, "unsuccessfull - " + "Error");
                        Toast.makeText(Mytickets.this, "invalid", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideProgressDialog();

                //TODO: handle success
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Mytickets.this, error.toString(), Toast.LENGTH_SHORT).show();

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


    public void getEventByname(){

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
                params.put("sort_by_name", "ASC");
                params.put("top_events", "");
                params.put("my_ticket", "1");
                params.put("selling_stat", "");
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
                            geteventModel.setTicket_stat(responseobj.getString("ticket_stat"));
                            geteventModel.setFav_status(responseobj.getString("fav_status"));
                            geteventModelArrayList.add(geteventModel);

                        }

                        setupRecycler();

                    } else {

                        Log.d(TAG, "unsuccessfull - " + "Error");
                        Toast.makeText(Mytickets.this, "invalid", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideProgressDialog();

                //TODO: handle success
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Mytickets.this, error.toString(), Toast.LENGTH_SHORT).show();

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

        myticketAdapter = new MyticketAdapter(this, geteventModelArrayList);
        rvMytickets.setAdapter(myticketAdapter);
        rvMytickets.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

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

                        Toast.makeText(Mytickets.this, msg, Toast.LENGTH_SHORT).show();

                    } else {

                        Log.d(TAG, "unsuccessfull - " + "Error");
                        Toast.makeText(Mytickets.this, "Add to Favourite not successfull", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideProgressDialog();

                //TODO: handle success
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Mytickets.this, error.toString(), Toast.LENGTH_SHORT).show();

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

        Intent intent = new Intent(Mytickets.this, Events.class);
        startActivity(intent);

    }
}