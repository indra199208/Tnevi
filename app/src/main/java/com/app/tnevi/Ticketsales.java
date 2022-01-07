package com.app.tnevi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.tnevi.Adapters.TicketsalesAdapter;
import com.app.tnevi.Allurl.Allurl;

import com.app.tnevi.internet.CheckConnectivity;
import com.app.tnevi.model.TicketReportModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ticketsales extends AppCompatActivity {

    ImageView btn_back;
    LinearLayoutCompat btnpastEvents, btnLive, btnUpcomingevents;
    TextView tvEvents, tvLive, tvUpcoming, tvTotalsales, tvavailablecashout;
    View pasteventsU, liveU, upcomingU;
    Button btncashoutHistory, btnCashout;
    String token;
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String TAG = "myapp";
    RecyclerView rv_past, rv_live, rv_upcoming;
    TicketsalesAdapter ticketsalesAdapter;
    private ArrayList<TicketReportModel> ticketReportModelArrayList;
    String cashoutid = "";
    Spinner spSortsales;
    String bydate = "";
    String byname = "";
    String events = "";
    String bankdetails = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticketsales);
        btn_back = findViewById(R.id.btn_back);
        btnpastEvents = findViewById(R.id.btnpastEvents);
        btnLive = findViewById(R.id.btnLive);
        btnUpcomingevents = findViewById(R.id.btnUpcomingevents);
        tvEvents = findViewById(R.id.tvEvents);
        tvLive = findViewById(R.id.tvLive);
        tvUpcoming = findViewById(R.id.tvUpcoming);
        pasteventsU = findViewById(R.id.pasteventsU);
        liveU = findViewById(R.id.liveU);
        btncashoutHistory = findViewById(R.id.btncashoutHistory);
        upcomingU = findViewById(R.id.upcomingU);
        btnCashout = findViewById(R.id.btnCashout);
        rv_upcoming = findViewById(R.id.rv_upcoming);
        rv_live = findViewById(R.id.rv_live);
        rv_past = findViewById(R.id.rv_past);
        tvTotalsales = findViewById(R.id.tvTotalsales);
        tvavailablecashout = findViewById(R.id.tvavailablecashout);
        spSortsales = findViewById(R.id.spSortsales);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");


        onClick();
        getAccount();

    }

    public void onClick() {

        spSortsales.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i > 0) {

                    switch (events) {
                        case "past":
                            switch (spSortsales.getSelectedItem().toString()) {
                                case "All":
                                    pastEvent();
                                    break;
                                case "By Date":
                                    bydate = "ASC";
                                    pastEvent();
                                    break;
                                case "By Name":
                                    byname = "ASC";
                                    pastEvent();
                                    break;
                                default:
                                    pastEvent();
                                    break;
                            }
                            break;
                        case "live":

                            switch (spSortsales.getSelectedItem().toString()) {
                                case "All":
                                    liveEvent();
                                    break;
                                case "By Date":
                                    bydate = "ASC";
                                    liveEvent();
                                    break;
                                case "By Name":
                                    byname = "ASC";
                                    liveEvent();
                                    break;
                                default:
                                    liveEvent();
                                    break;
                            }

                            break;
                        default:

                            switch (spSortsales.getSelectedItem().toString()) {
                                case "All":
                                    upcomingEvents();
                                    break;
                                case "By Date":
                                    bydate = "ASC";
                                    upcomingEvents();
                                    break;
                                case "By Name":
                                    byname = "ASC";
                                    upcomingEvents();
                                    break;
                                default:
                                    upcomingEvents();
                                    break;
                            }

                            break;
                    }

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnCashout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (bankdetails.equals("1")) {

                    if (cashoutid.equals("")) {
                        Toast.makeText(Ticketsales.this, "Select any event for cashout", Toast.LENGTH_SHORT).show();
                    } else {
                        cashout();
                    }
                }else {

                    Intent intent = new Intent(Ticketsales.this, Checkoutaddbankacc.class);
                    startActivity(intent);
                }

            }
        });

        btnpastEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                events = "past";
                pastEvent();
            }
        });


        btnLive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                events = "live";
                liveEvent();
            }
        });


        btnUpcomingevents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                events = "coming";
                upcomingEvents();
            }
        });


        btncashoutHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Ticketsales.this, Cashouthistory.class);
                startActivity(intent);
            }
        });


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
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

                            //Parse Here

                            try {
                                JSONObject result = new JSONObject(String.valueOf(response));
                                String msg = result.getString("message");
                                Log.d(TAG, "msg-->" + msg);
                                String stat = result.getString("stat");
                                if (stat.equals("succ")) {

                                    JSONObject userdeatisObj = result.getJSONObject("data");
                                    JSONObject profileObj = userdeatisObj.getJSONObject("profile");
                                    JSONObject walletObj = userdeatisObj.getJSONObject("wallet");
                                    String ticketsell = walletObj.getString("ticket_sell");
                                    String commission = walletObj.getString("commission");
                                    String epoints = walletObj.getString("epoints");
                                    String address = profileObj.getString("address");
                                    String country_code = profileObj.getString("country_code");
                                    if (!userdeatisObj.isNull("bank_details")) {
                                        bankdetails = "1";
                                    } else {
                                        bankdetails = "2";
                                    }
                                    pastEvent();
                                    spSort();

                                } else {

                                    hideProgressDialog();
                                    Log.d(TAG, "unsuccessfull - " + "Error");
                                    Toast.makeText(Ticketsales.this, msg, Toast.LENGTH_SHORT).show();
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            hideProgressDialog();
                            Toast.makeText(Ticketsales.this, error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", token);
                    return params;
                }

            };


            RequestQueue requestQueue = Volley.newRequestQueue(Ticketsales.this);
            requestQueue.add(stringRequest);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    9000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        } else {

            Toast.makeText(getApplicationContext(), "OOPS! No Internet Connection", Toast.LENGTH_SHORT).show();

        }


    }


    public void cashoutselectedevent(String id) {
        cashoutid = id;
    }

    public void cashout() {

        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {
            showProgressDialog();
            JSONObject params = new JSONObject();

            try {
                params.put("event_id", cashoutid);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Allurl.Cashout, params, response -> {

                Log.i("Response-->", String.valueOf(response));

                try {
                    JSONObject result = new JSONObject(String.valueOf(response));
                    String stat = result.getString("stat");
                    String msg = result.getString("message");
                    if (stat.equals("succ")) {
                        Toast.makeText(Ticketsales.this, msg, Toast.LENGTH_SHORT).show();

                    } else {

                        hideProgressDialog();
                        Toast.makeText(Ticketsales.this, msg, Toast.LENGTH_SHORT).show();

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
                    Toast.makeText(Ticketsales.this, error.toString(), Toast.LENGTH_SHORT).show();

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


    public void pastEvent() {

        cashoutid = "";
        tvEvents.setTextColor(ContextCompat.getColor(Ticketsales.this, R.color.colorAccent));
        tvLive.setTextColor(ContextCompat.getColor(Ticketsales.this, R.color.quantum_grey));
        tvUpcoming.setTextColor(ContextCompat.getColor(Ticketsales.this, R.color.quantum_grey));
        pasteventsU.setVisibility(View.VISIBLE);
        liveU.setVisibility(View.INVISIBLE);
        upcomingU.setVisibility(View.INVISIBLE);
        rv_live.setVisibility(View.GONE);
        rv_past.setVisibility(View.VISIBLE);
        rv_upcoming.setVisibility(View.GONE);

        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {


            showProgressDialog();

            JSONObject params = new JSONObject();

            try {
                params.put("page_no", "1");
                params.put("past", "1");
                params.put("sort_by_date", bydate);
                params.put("sort_by_name", byname);


            } catch (JSONException e) {
                e.printStackTrace();
            }


            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Allurl.TicksalesList, params, response -> {

                Log.i("Response-->", String.valueOf(response));

                try {
                    JSONObject result = new JSONObject(String.valueOf(response));
                    String msg = result.getString("message");
                    Log.d(TAG, "msg-->" + msg);
                    String stat = result.getString("stat");
                    if (stat.equals("succ")) {

                        ticketReportModelArrayList = new ArrayList<>();
                        JSONArray response_data = result.getJSONArray("data");
                        for (int i = 0; i < response_data.length(); i++) {

                            TicketReportModel ticketReportModel = new TicketReportModel();
                            JSONObject responseobj = response_data.getJSONObject(i);
                            ticketReportModel.setEvent_name(responseobj.getString("event_name"));
                            ticketReportModel.setEvent_date(responseobj.getString("event_date"));
                            ticketReportModel.setId(responseobj.getString("id"));
                            ticketReportModel.setFree_stat(responseobj.getString("free_stat"));
                            ticketReportModel.setCurrency_id(responseobj.getString("currency_id"));
                            ticketReportModel.setStatus(responseobj.getString("status"));
                            ticketReportModel.setTotal_sale(responseobj.getString("total_sale"));
                            ticketReportModel.setTotal_ticket(responseobj.getString("total_ticket"));
                            ticketReportModel.setEvent_image(responseobj.getString("event_image"));
                            ticketReportModel.setEvent_address(responseobj.getString("event_address"));
                            ticketReportModel.setTicket_stat(responseobj.getString("ticket_stat"));
                            ticketReportModel.setHighlight_event(responseobj.getString("highlight_event"));
                            ticketReportModel.setTop_events(responseobj.getString("top_events"));
                            ticketReportModel.setMin_price(responseobj.getString("min_price"));
                            ticketReportModel.setMax_price(responseobj.getString("max_price"));
                            ticketReportModel.setEvent_commission(responseobj.getString("event_commission"));
                            ticketReportModelArrayList.add(ticketReportModel);

                        }

                        String totalsale = result.getString("total_sale");
                        String availableforcashout = result.getString("available_for_cash_out");
                        tvTotalsales.setText("Total Sales $" + totalsale);
                        tvavailablecashout.setText("$" + availableforcashout);

                        ticketsalesAdapter = new TicketsalesAdapter(this, ticketReportModelArrayList);
                        rv_past.setAdapter(ticketsalesAdapter);
                        rv_past.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));


                    } else {

                        Toast.makeText(Ticketsales.this, "invalid", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideProgressDialog();

                //TODO: handle success
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Ticketsales.this, error.toString(), Toast.LENGTH_SHORT).show();

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

    public void upcomingEvents() {

        cashoutid = "";
        tvUpcoming.setTextColor(ContextCompat.getColor(Ticketsales.this, R.color.colorAccent));
        tvEvents.setTextColor(ContextCompat.getColor(Ticketsales.this, R.color.quantum_grey));
        tvLive.setTextColor(ContextCompat.getColor(Ticketsales.this, R.color.quantum_grey));
        pasteventsU.setVisibility(View.INVISIBLE);
        liveU.setVisibility(View.INVISIBLE);
        upcomingU.setVisibility(View.VISIBLE);
        rv_live.setVisibility(View.GONE);
        rv_past.setVisibility(View.GONE);
        rv_upcoming.setVisibility(View.VISIBLE);

        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {


            showProgressDialog();

            JSONObject params = new JSONObject();

            try {
                params.put("page_no", "1");
                params.put("upcomming", "1");
                params.put("sort_by_date", bydate);
                params.put("sort_by_name", byname);

            } catch (JSONException e) {
                e.printStackTrace();
            }


            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Allurl.TicksalesList, params, response -> {

                Log.i("Response-->", String.valueOf(response));

                try {
                    JSONObject result = new JSONObject(String.valueOf(response));
                    String msg = result.getString("message");
                    Log.d(TAG, "msg-->" + msg);
                    String stat = result.getString("stat");
                    if (stat.equals("succ")) {

                        ticketReportModelArrayList = new ArrayList<>();
                        JSONArray response_data = result.getJSONArray("data");
                        for (int i = 0; i < response_data.length(); i++) {

                            TicketReportModel ticketReportModel = new TicketReportModel();
                            JSONObject responseobj = response_data.getJSONObject(i);
                            ticketReportModel.setEvent_name(responseobj.getString("event_name"));
                            ticketReportModel.setEvent_date(responseobj.getString("event_date"));
                            ticketReportModel.setId(responseobj.getString("id"));
                            ticketReportModel.setFree_stat(responseobj.getString("free_stat"));
                            ticketReportModel.setCurrency_id(responseobj.getString("currency_id"));
                            ticketReportModel.setStatus(responseobj.getString("status"));
                            ticketReportModel.setTotal_sale(responseobj.getString("total_sale"));
                            ticketReportModel.setTotal_ticket(responseobj.getString("total_ticket"));
                            ticketReportModel.setEvent_image(responseobj.getString("event_image"));
                            ticketReportModel.setEvent_address(responseobj.getString("event_address"));
                            ticketReportModel.setTicket_stat(responseobj.getString("ticket_stat"));
                            ticketReportModel.setHighlight_event(responseobj.getString("highlight_event"));
                            ticketReportModel.setTop_events(responseobj.getString("top_events"));
                            ticketReportModel.setMin_price(responseobj.getString("min_price"));
                            ticketReportModel.setMax_price(responseobj.getString("max_price"));
                            ticketReportModel.setEvent_commission(responseobj.getString("event_commission"));
                            ticketReportModelArrayList.add(ticketReportModel);

                        }

                        String totalsale = result.getString("total_sale");
                        String availableforcashout = result.getString("available_for_cash_out");
                        tvTotalsales.setText("Total Sales $" + totalsale);
                        tvavailablecashout.setText("$" + availableforcashout);

                        ticketsalesAdapter = new TicketsalesAdapter(this, ticketReportModelArrayList);
                        rv_upcoming.setAdapter(ticketsalesAdapter);
                        rv_upcoming.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));


                    } else {

                        Toast.makeText(Ticketsales.this, "invalid", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideProgressDialog();

                //TODO: handle success
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Ticketsales.this, error.toString(), Toast.LENGTH_SHORT).show();

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


    public void liveEvent() {

        cashoutid = "";
        tvLive.setTextColor(ContextCompat.getColor(Ticketsales.this, R.color.colorAccent));
        tvEvents.setTextColor(ContextCompat.getColor(Ticketsales.this, R.color.quantum_grey));
        tvUpcoming.setTextColor(ContextCompat.getColor(Ticketsales.this, R.color.quantum_grey));
        pasteventsU.setVisibility(View.INVISIBLE);
        liveU.setVisibility(View.VISIBLE);
        upcomingU.setVisibility(View.INVISIBLE);
        rv_live.setVisibility(View.VISIBLE);
        rv_past.setVisibility(View.GONE);
        rv_upcoming.setVisibility(View.GONE);

        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {


            showProgressDialog();

            JSONObject params = new JSONObject();

            try {
                params.put("page_no", "1");
                params.put("live", "1");
                params.put("sort_by_date", bydate);
                params.put("sort_by_name", byname);

            } catch (JSONException e) {
                e.printStackTrace();
            }


            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Allurl.TicksalesList, params, response -> {

                Log.i("Response-->", String.valueOf(response));

                try {
                    JSONObject result = new JSONObject(String.valueOf(response));
                    String msg = result.getString("message");
                    Log.d(TAG, "msg-->" + msg);
                    String stat = result.getString("stat");
                    if (stat.equals("succ")) {

                        ticketReportModelArrayList = new ArrayList<>();
                        JSONArray response_data = result.getJSONArray("data");
                        for (int i = 0; i < response_data.length(); i++) {

                            TicketReportModel ticketReportModel = new TicketReportModel();
                            JSONObject responseobj = response_data.getJSONObject(i);
                            ticketReportModel.setEvent_name(responseobj.getString("event_name"));
                            ticketReportModel.setEvent_date(responseobj.getString("event_date"));
                            ticketReportModel.setId(responseobj.getString("id"));
                            ticketReportModel.setFree_stat(responseobj.getString("free_stat"));
                            ticketReportModel.setCurrency_id(responseobj.getString("currency_id"));
                            ticketReportModel.setStatus(responseobj.getString("status"));
                            ticketReportModel.setTotal_sale(responseobj.getString("total_sale"));
                            ticketReportModel.setTotal_ticket(responseobj.getString("total_ticket"));
                            ticketReportModel.setEvent_image(responseobj.getString("event_image"));
                            ticketReportModel.setEvent_address(responseobj.getString("event_address"));
                            ticketReportModel.setTicket_stat(responseobj.getString("ticket_stat"));
                            ticketReportModel.setHighlight_event(responseobj.getString("highlight_event"));
                            ticketReportModel.setTop_events(responseobj.getString("top_events"));
                            ticketReportModel.setMin_price(responseobj.getString("min_price"));
                            ticketReportModel.setMax_price(responseobj.getString("max_price"));
                            ticketReportModel.setEvent_commission(responseobj.getString("event_commission"));
                            ticketReportModelArrayList.add(ticketReportModel);

                        }

                        String totalsale = result.getString("total_sale");
                        String availableforcashout = result.getString("available_for_cash_out");
                        tvTotalsales.setText("Total Sales $" + totalsale);
                        tvavailablecashout.setText("$" + availableforcashout);

                        ticketsalesAdapter = new TicketsalesAdapter(this, ticketReportModelArrayList);
                        rv_live.setAdapter(ticketsalesAdapter);
                        rv_live.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));


                    } else {

                        Toast.makeText(Ticketsales.this, "invalid", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideProgressDialog();

                //TODO: handle success
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Ticketsales.this, error.toString(), Toast.LENGTH_SHORT).show();

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


    private void spSort() {

        List<String> sort = new ArrayList<String>();

        sort.add("Select");
        sort.add("All");
        sort.add("By Date");
        sort.add("By Name");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sort);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSortsales.setAdapter(arrayAdapter);


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