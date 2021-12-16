package com.app.tnevi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.app.tnevi.Adapters.CommissionDetailsAdapter;
import com.app.tnevi.Allurl.Allurl;

import com.app.tnevi.internet.CheckConnectivity;
import com.app.tnevi.model.CommissionReportDetailsModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Comissiondetails extends AppCompatActivity {

    ImageView btn_back;
    String eventname, date, totalsold, id;
    private static final String TAG = "myapp";
    private static final String SHARED_PREFS = "sharedPrefs";
    String useremail, username, token;
    RecyclerView rv_commissionreportDetails;
    TextView tvEventName, tvDate, tvEventName2, tvDate2, tvTotalCommission;
    private ArrayList<CommissionReportDetailsModel> commissionReportDetailsModelArrayList;
    CommissionDetailsAdapter commissionDetailsAdapter;
    LinearLayout btnCashout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comissiondetails);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        btn_back = findViewById(R.id.btn_back);
        rv_commissionreportDetails = findViewById(R.id.rv_commissionreportDetails);
        tvEventName = findViewById(R.id.tvEventName);
        tvDate = findViewById(R.id.tvDate);
        tvEventName2 = findViewById(R.id.tvEventName2);
        tvDate2 = findViewById(R.id.tvDate2);
        tvTotalCommission = findViewById(R.id.tvTotalCommission);
        btnCashout = findViewById(R.id.btnCashout);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        username = sharedPreferences.getString("name", "");
        useremail = sharedPreferences.getString("email", "");


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });

        btnCashout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cashout();
            }
        });

        commissionreportDetails();
    }

    public void commissionreportDetails(){


        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {


            showProgressDialog();

            JSONObject params = new JSONObject();

            try {
                params.put("event_id", id);

            } catch (JSONException e) {
                e.printStackTrace();
            }


            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Allurl.commissionReportDetails, params, response -> {

                Log.i("Response-->", String.valueOf(response));

                try {
                    JSONObject result = new JSONObject(String.valueOf(response));
                    String msg = result.getString("message");
                    Log.d(TAG, "msg-->" + msg);
                    String stat = result.getString("stat");
                    if (stat.equals("succ")) {
                        commissionReportDetailsModelArrayList = new ArrayList<>();
                        JSONArray datarray = result.getJSONArray("data");
                        JSONArray totalearningarray = result.getJSONArray("total_earning");
                        for (int i = 0; i < datarray.length(); i++) {

                            CommissionReportDetailsModel commissionReportDetailsModel = new CommissionReportDetailsModel();
                            JSONObject responseobj = datarray.getJSONObject(i);
                            commissionReportDetailsModel.setDate(responseobj.getString("date"));
                            commissionReportDetailsModel.setTotal_sale_datewise(responseobj.getString("total_sale_datewise"));
                            commissionReportDetailsModelArrayList.add(commissionReportDetailsModel);
                        }

                        for (int j = 0; j <totalearningarray.length(); j++){
                            JSONObject earningobj = totalearningarray.getJSONObject(0);
                            String id = earningobj.getString("id");
                            String event_name = earningobj.getString("event_name");
                            String event_date = earningobj.getString("event_date");
                            String total_sale = earningobj.getString("total_sale");
                            String total_ticket = earningobj.getString("total_ticket");
                            String total_commission = earningobj.getString("total_commission");
                            tvEventName.setText(event_name);
                            tvEventName2.setText(event_name);
                            tvDate.setText(event_date);
                            tvDate2.setText(event_date);
                            tvTotalCommission.setText(total_commission);
                        }

                        commissionDetailsAdapter = new CommissionDetailsAdapter(this, commissionReportDetailsModelArrayList);
                        rv_commissionreportDetails.setAdapter(commissionDetailsAdapter);
                        rv_commissionreportDetails.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

                    } else {

                        Log.d(TAG, "unsuccessfull - " + "Error");
                        Toast.makeText(Comissiondetails.this, msg, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideProgressDialog();

                //TODO: handle success
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Comissiondetails.this, error.toString(), Toast.LENGTH_SHORT).show();

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


    public void cashout(){

        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {
            showProgressDialog();
            JSONObject params = new JSONObject();

            try {
                params.put("event_id", id);

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
                        Toast.makeText(Comissiondetails.this, msg, Toast.LENGTH_SHORT).show();

                    } else {

                        hideProgressDialog();
                        Toast.makeText(Comissiondetails.this, msg, Toast.LENGTH_SHORT).show();

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
                    Toast.makeText(Comissiondetails.this, error.toString(), Toast.LENGTH_SHORT).show();

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