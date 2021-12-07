package com.ivan.tnevi;

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
import android.webkit.CookieManager;
import android.webkit.URLUtil;
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
import com.ivan.tnevi.Allurl.Allurl;
import com.example.tnevi.R;
import com.ivan.tnevi.internet.CheckConnectivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Eventgeneraldetails extends AppCompatActivity {

    ImageView btn_back;
    TextView tvEventName,tvEventName2, tvDate, tvDate2, tvSubtotal, tvTotalticketSold, tvTicketAvailable,
            tvOnlinesold, tvTotalRefundedTickets, tvDownloadPurchase, btnPayoutDownload, btnAttendancereportDownload;
    String id;
    private static final String TAG = "myapp";
    private static final String SHARED_PREFS = "sharedPrefs";
    String useremail, username, token, data;
    LinearLayout btnCashout;

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
        btnPayoutDownload = findViewById(R.id.btnPayoutDownload);
        btnAttendancereportDownload = findViewById(R.id.btnAttendancereportDownload);


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

        btnPayoutDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PayoutDownload();
            }
        });

        btnAttendancereportDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AttendanceDownload();
            }
        });

        eventreportdetails();
    }

    public void AttendanceDownload(){

        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {
            showProgressDialog();
            JSONObject params = new JSONObject();

            try {
                params.put("event_id", id);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Allurl.ReportDownload, params, response -> {

                Log.i("Response-->", String.valueOf(response));

                try {
                    JSONObject result = new JSONObject(String.valueOf(response));
                    String stat = result.getString("stat");
                    String msg = result.getString("message");
                    data = result.getString("data");
                    if (stat.equals("succ")) {
                        String url = "http://dev8.ivantechnology.in/tnevi/storage/app/"+data;
                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                        String title = URLUtil.guessFileName(url,null,null);
                        request.setTitle(title);
                        request.setDescription("Downloading File please wait...");
                        String cookie = CookieManager.getInstance().getCookie(url);
                        request.addRequestHeader("cookie", cookie);
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title);

                        DownloadManager downloadManager = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);
                        downloadManager.enqueue(request);

                        Toast.makeText(this, "Download Started.", Toast.LENGTH_SHORT).show();


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

    public void PayoutDownload(){

        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {
            showProgressDialog();
            JSONObject params = new JSONObject();

            try {
                params.put("event_id", id);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Allurl.PayoutDownload, params, response -> {

                Log.i("Response-->", String.valueOf(response));

                try {
                    JSONObject result = new JSONObject(String.valueOf(response));
                    String stat = result.getString("stat");
                    String msg = result.getString("message");
                    data = result.getString("data");
                    if (stat.equals("succ")) {
                        String url = "http://dev8.ivantechnology.in/tnevi/storage/app/"+data;
                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                        String title = URLUtil.guessFileName(url,null,null);
                        request.setTitle(title);
                        request.setDescription("Downloading File please wait...");
                        String cookie = CookieManager.getInstance().getCookie(url);
                        request.addRequestHeader("cookie", cookie);
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title);

                        DownloadManager downloadManager = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);
                        downloadManager.enqueue(request);

                        Toast.makeText(this, "Download Started.", Toast.LENGTH_SHORT).show();


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
                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                        String title = URLUtil.guessFileName(url,null,null);
                        request.setTitle(title);
                        request.setDescription("Downloading File please wait...");
                        String cookie = CookieManager.getInstance().getCookie(url);
                        request.addRequestHeader("cookie", cookie);
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title);

                        DownloadManager downloadManager = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);
                        downloadManager.enqueue(request);

                        Toast.makeText(this, "Download Started.", Toast.LENGTH_SHORT).show();


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