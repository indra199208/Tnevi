package com.example.tnevi;

import static com.paypal.android.sdk.cx.i;

import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.tnevi.Allurl.Allurl;
import com.example.tnevi.internet.CheckConnectivity;
import com.example.tnevi.model.MyeventModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Eventgeneraldetails extends AppCompatActivity {

    ImageView btn_back;
    TextView tvEventName,tvEventName2, tvDate, tvDate2, tvSubtotal, tvTotalticketSold, tvTicketAvailable,
            tvOnlinesold, tvTotalRefundedTickets, tvDownloadPurchase;
    String id;
    private static final String TAG = "myapp";
    private static final String SHARED_PREFS = "sharedPrefs";
    String useremail, username, token, data;
    LinearLayout btnCashout;
    // Progress Dialog
    private ProgressDialog pDialog;
    public static final int progress_bar_type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventgeneraldetails);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        btn_back = findViewById(R.id.btn_back);
        tvEventName = findViewById(R.id.tvEventName);
        tvDate = findViewById(R.id.tvDate);
        tvDate2 = findViewById(R.id.tvDate2);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvEventName2 = findViewById(R.id.tvEventName2);
        tvTotalticketSold = findViewById(R.id.tvTotalticketSold);
        tvTicketAvailable = findViewById(R.id.tvTicketAvailable);
        tvOnlinesold = findViewById(R.id.tvOnlinesold);
        tvTotalRefundedTickets = findViewById(R.id.tvTotalRefundedTickets);
        btnCashout = findViewById(R.id.btnCashout);
        tvDownloadPurchase = findViewById(R.id.tvDownloadPurchase);

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

        tvDownloadPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                DownloadPurchase();
            }
        });

        eventreportdetails();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type: // we set this to 0
                pDialog = new ProgressDialog(this);
                pDialog.setMessage("Downloading file. Please wait...");
                pDialog.setIndeterminate(false);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setCancelable(true);
                pDialog.show();
                return pDialog;
            default:
                return null;
        }
    }


    public void DownloadPurchase(){


        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {
            showProgressDialog();
            JSONObject params = new JSONObject();

            try {
                params.put("event_id", id);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Allurl.DownloadPurchase, params, response -> {

                Log.i("Response-->", String.valueOf(response));

                try {
                    JSONObject result = new JSONObject(String.valueOf(response));
                    String stat = result.getString("stat");
                    String msg = result.getString("message");
                    data = result.getString("data");
                    if (stat.equals("succ")) {
//                        Toast.makeText(Eventgeneraldetails.this, data, Toast.LENGTH_SHORT).show();
                        String url = "http://dev8.ivantechnology.in/tnevi/storage/app/"+data;
                        new DownloadFileFromURL().execute(url);

                    } else {

                        hideProgressDialog();
                        Toast.makeText(Eventgeneraldetails.this, msg, Toast.LENGTH_SHORT).show();

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
                    Toast.makeText(Eventgeneraldetails.this, error.toString(), Toast.LENGTH_SHORT).show();

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


    class DownloadFileFromURL extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);
        }

        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                int lenghtOfFile = connection.getContentLength();
                InputStream input = new BufferedInputStream(url.openStream(),
                        8192);

                OutputStream output = new FileOutputStream(Environment
                        .getExternalStorageDirectory().toString()
                        +  "/"+Calendar.getInstance()
                        .getTimeInMillis()+ ".csv");

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        protected void onProgressUpdate(String... progress) {
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String file_url) {
            dismissDialog(progress_bar_type);

        }

    }




    public void eventreportdetails(){


        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {


            showProgressDialog();

            JSONObject params = new JSONObject();

            try {
                params.put("event_id", id);

            } catch (JSONException e) {
                e.printStackTrace();
            }


            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Allurl.TicketSalesReportDetails, params, response -> {

                Log.i("Response-->", String.valueOf(response));

                try {
                    JSONObject result = new JSONObject(String.valueOf(response));
                    String msg = result.getString("message");
                    Log.d(TAG, "msg-->" + msg);
                    String stat = result.getString("stat");
                    if (stat.equals("succ")) {
                        JSONObject responseobj = result.getJSONObject("data");
                        String id = responseobj.getString("id");
                        String event_name = responseobj.getString("event_name");
                        String event_date = responseobj.getString("event_date");
                        String status = responseobj.getString("status");
                        String total_sale = responseobj.getString("total_sale");
                        String total_cancel = responseobj.getString("total_cancel");
                        String total_book_seat = responseobj.getString("total_book_seat");
                        String total_available_seat = responseobj.getString("total_available_seat");
                        if (total_cancel.equals(null)){
                            tvTotalRefundedTickets.setText("0");
                        }else {
                            tvTotalRefundedTickets.setText(total_cancel);
                        }
                        tvTotalticketSold.setText(total_book_seat);
                        tvEventName.setText(event_name);
                        tvEventName2.setText(event_name);
                        tvDate.setText(event_date);
                        tvDate2.setText(event_date);
                        tvOnlinesold.setText(total_sale);
                        tvTicketAvailable.setText(total_available_seat);


                    } else {

                        Log.d(TAG, "unsuccessfull - " + "Error");
                        Toast.makeText(Eventgeneraldetails.this, msg, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideProgressDialog();

                //TODO: handle success
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Eventgeneraldetails.this, error.toString(), Toast.LENGTH_SHORT).show();

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
                        Toast.makeText(Eventgeneraldetails.this, msg, Toast.LENGTH_SHORT).show();

                    } else {

                        hideProgressDialog();
                        Toast.makeText(Eventgeneraldetails.this, msg, Toast.LENGTH_SHORT).show();

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
                    Toast.makeText(Eventgeneraldetails.this, error.toString(), Toast.LENGTH_SHORT).show();

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