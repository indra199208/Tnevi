package com.ivan.tnevi;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ivan.tnevi.Adapters.TicketReportAdapter;
import com.ivan.tnevi.Allurl.Allurl;
import com.example.tnevi.R;
import com.ivan.tnevi.internet.CheckConnectivity;
import com.ivan.tnevi.model.TicketReportModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Ticketsalesreport extends AppCompatActivity {

    RecyclerView rv_ticketreport;
    ImageView btn_back;
    TicketReportAdapter ticketReportAdapter;
    private ArrayList<TicketReportModel> ticketReportModelArrayList;
    String token;
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String TAG = "myapp";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticketsalesreport);
        rv_ticketreport = findViewById(R.id.rv_ticketreport);
        btn_back = findViewById(R.id.btn_back);
        rv_ticketreport = findViewById(R.id.rv_ticketreport);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });


        reportlist();
    }



    public void reportlist(){

        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {


            showProgressDialog();

            JSONObject params = new JSONObject();

            try {
                params.put("page_no", "1");

            } catch (JSONException e) {
                e.printStackTrace();
            }


            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Allurl.TicketSalesReport, params, response -> {

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
                            ticketReportModelArrayList.add(ticketReportModel);

                        }

                        setupRecycler();

                    } else {

                        Toast.makeText(Ticketsalesreport.this, "invalid", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideProgressDialog();

                //TODO: handle success
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Ticketsalesreport.this, error.toString(), Toast.LENGTH_SHORT).show();

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


    public void cashout(TicketReportModel ticketReportModel){

        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {
            showProgressDialog();
            JSONObject params = new JSONObject();

            try {
                params.put("event_id", ticketReportModel.getId());

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
                        Toast.makeText(Ticketsalesreport.this, msg, Toast.LENGTH_SHORT).show();

                    } else {

                        hideProgressDialog();
                        Toast.makeText(Ticketsalesreport.this, msg, Toast.LENGTH_SHORT).show();

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
                    Toast.makeText(Ticketsalesreport.this, error.toString(), Toast.LENGTH_SHORT).show();

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

        ticketReportAdapter = new TicketReportAdapter(this, ticketReportModelArrayList);
        rv_ticketreport.setAdapter(ticketReportAdapter);
        rv_ticketreport.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

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