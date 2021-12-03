package com.example.tnevi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.tnevi.Adapters.CommissionReportAdapter;
import com.example.tnevi.Adapters.TicketReportAdapter;
import com.example.tnevi.Allurl.Allurl;
import com.example.tnevi.internet.CheckConnectivity;
import com.example.tnevi.model.TicketReportModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Commissionsearned extends AppCompatActivity {

    LinearLayout btnViewdeails, btnViewdeails2;
    ImageView btn_back;
    RecyclerView rv_commissionreport;
    CommissionReportAdapter commissionReportAdapter;
    private ArrayList<TicketReportModel> ticketReportModelArrayList;
    String token;
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String TAG = "myapp";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commissionsearned);
        rv_commissionreport = findViewById(R.id.rv_commissionreport);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });

//        btnViewdeails.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(Commissionsearned.this, Comissiondetails.class);
//                startActivity(intent);
//            }
//        });


//        btnViewdeails2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(Commissionsearned.this, Comissiondetails.class);
//                startActivity(intent);
//            }
//        });

        commissionList();


    }


    public void commissionList(){

        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {


            showProgressDialog();

            JSONObject params = new JSONObject();

            try {
                params.put("page_no", "1");

            } catch (JSONException e) {
                e.printStackTrace();
            }


            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Allurl.CommissionList, params, response -> {

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
                            if (!responseobj.isNull("total_refering_commision")) {
                                ticketReportModel.setTotal_sale(responseobj.getString("total_refering_commision"));
                            } else {
                                ticketReportModel.setTotal_sale("0");
                            }
                            ticketReportModel.setTotal_ticket(responseobj.getString("ticket_cash_out"));
                            ticketReportModel.setEvent_image(responseobj.getString("event_image"));
                            ticketReportModelArrayList.add(ticketReportModel);

                        }

                        setupRecycler();

                    } else {

                        Toast.makeText(Commissionsearned.this, "invalid", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideProgressDialog();

                //TODO: handle success
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Commissionsearned.this, error.toString(), Toast.LENGTH_SHORT).show();

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

        commissionReportAdapter = new CommissionReportAdapter(this, ticketReportModelArrayList);
        rv_commissionreport.setAdapter(commissionReportAdapter);
        rv_commissionreport.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

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
                        Toast.makeText(Commissionsearned.this, msg, Toast.LENGTH_SHORT).show();

                    } else {

                        hideProgressDialog();
                        Toast.makeText(Commissionsearned.this, msg, Toast.LENGTH_SHORT).show();

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
                    Toast.makeText(Commissionsearned.this, error.toString(), Toast.LENGTH_SHORT).show();

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