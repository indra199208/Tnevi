package com.app.tnevi;

import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.URLUtil;
import android.widget.EditText;
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
import com.app.tnevi.Adapters.TicketAdapter;
import com.app.tnevi.Allurl.Allurl;

import com.app.tnevi.internet.CheckConnectivity;
import com.app.tnevi.model.TicketModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Ticketdetails extends AppCompatActivity {

    private static final String TAG = "myapp";
    private static final String SHARED_PREFS = "sharedPrefs";
    String token, eventId;
    RecyclerView rvTicket;
    TicketAdapter ticketAdapter;
    private ArrayList<TicketModel> ticketModelArrayList;
    String seatno = "";
    String row_name = "";
    String block_name = "";
    String status = "";
    String id, booking_no, user_id, event_id, event_name, event_date, event_address, no_of_seat, each_ticket_price, total_ticket_price,
            book_by, name_on_ticket, epoints_earning;
    String bookingid = "";
    LinearLayout btnViewbalance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticketdetails);
        rvTicket = findViewById(R.id.rvTicket);
        btnViewbalance = findViewById(R.id.btnViewbalance);


        Intent intent = getIntent();
        eventId = intent.getStringExtra("eventId");
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");

        btnViewbalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent1 = new Intent(Ticketdetails.this, Wallet.class);
                startActivity(intent1);

            }
        });

        ticketdetails();


    }


    public void ViewTickets(TicketModel ticketModel) {


        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {
            showProgressDialog();
            bookingid = ticketModel.getId();
            JSONObject params = new JSONObject();

            try {
                params.put("booking_id", bookingid);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Allurl.ViewTickets, params, response -> {

                Log.i("Response-->", String.valueOf(response));

                try {
                    JSONObject result = new JSONObject(String.valueOf(response));
                    String stat = result.getString("stat");
                    String msg = result.getString("message");
                    if (stat.equals("succ")) {
                        JSONArray viewticetarray = result.getJSONArray("data");
                        for (int i = 0; i < viewticetarray.length(); i++) {
                            JSONObject ticketobj = viewticetarray.getJSONObject(i);
                            id = ticketobj.getString("id");
                            booking_no = ticketobj.getString("booking_no");
                            user_id = ticketobj.getString("user_id");
                            event_id = ticketobj.getString("event_id");
                            event_name = ticketobj.getString("event_name");
                            event_date = ticketobj.getString("event_date");
                            event_address = ticketobj.getString("event_address");
                            no_of_seat = ticketobj.getString("no_of_seat");
                            each_ticket_price = ticketobj.getString("each_ticket_price");
                            total_ticket_price = ticketobj.getString("total_ticket_price");
                            book_by = ticketobj.getString("book_by");
                            name_on_ticket = ticketobj.getString("name_on_ticket");
                            epoints_earning = ticketobj.getString("epoints_earning");

                            JSONArray seatsdata = ticketobj.getJSONArray("seats");
                            for (int j = 0; j < seatsdata.length(); j++) {
                                JSONObject seatobj = seatsdata.getJSONObject(j);
                                JSONObject seatobj1 = seatsdata.getJSONObject(0);
                                row_name = seatobj1.getString("row_name");
                                block_name = seatobj1.getString("block_name");
                                seatno = seatno + seatobj.getString("seat_no") + ",";
                                status = seatobj.getString("status");
                            }

                        }

                        showDialog();


                    } else {

                        hideProgressDialog();
                        Toast.makeText(Ticketdetails.this, msg, Toast.LENGTH_SHORT).show();

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
                    Toast.makeText(Ticketdetails.this, error.toString(), Toast.LENGTH_SHORT).show();

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


    public void showDialog() {
        final TextView tvEventname, tvEventTitle, tvEventdate, tvEventaddress, tvTicketno, tvSeats, tvBookingno, tvEachTicketprice,
                tvTotalticketPrice, tvTicketbookedby, tvNameonTicket, tvEpoints;
        ImageView btn_back, btnEmail;
        EditText etEmail;
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        dialog.setContentView(R.layout.dialog_ticket);
        params.copyFrom(dialog.getWindow().getAttributes());
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(params);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        tvEventname = (TextView) dialog.findViewById(R.id.tvEventname);
        tvEventTitle = (TextView) dialog.findViewById(R.id.tvEventTitle);
        tvEventdate = (TextView) dialog.findViewById(R.id.tvEventdate);
        tvEventaddress = (TextView) dialog.findViewById(R.id.tvEventaddress);
        tvTicketno = (TextView) dialog.findViewById(R.id.tvTicketno);
        tvSeats = (TextView) dialog.findViewById(R.id.tvSeats);
        tvBookingno = (TextView) dialog.findViewById(R.id.tvBookingno);
        tvEachTicketprice = (TextView) dialog.findViewById(R.id.tvEachTicketprice);
        tvTotalticketPrice = (TextView) dialog.findViewById(R.id.tvTotalticketPrice);
        tvTicketbookedby = (TextView) dialog.findViewById(R.id.tvTicketbookedby);
        tvNameonTicket = (TextView) dialog.findViewById(R.id.tvNameonTicket);
        tvEpoints = (TextView) dialog.findViewById(R.id.tvEpoints);
        btn_back = (ImageView) dialog.findViewById(R.id.btn_back);
        etEmail = (EditText) dialog.findViewById(R.id.etEmail);
        btnEmail = (ImageView) dialog.findViewById(R.id.btnEmail);

        tvEventname.setText(event_name);
        tvEventTitle.setText(event_name);
        tvEventdate.setText(event_date);
        tvEventaddress.setText(event_address);
        tvTicketno.setText(no_of_seat);
        tvSeats.setText(seatno);
        tvBookingno.setText(booking_no);
        tvEachTicketprice.setText("$" + each_ticket_price);
        tvTotalticketPrice.setText("$" + total_ticket_price);
        tvTicketbookedby.setText(book_by);
        tvNameonTicket.setText(name_on_ticket);
        tvEpoints.setText(epoints_earning);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });

        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("".equals(bookingid)) {
                    etEmail.setError("Booking id not selected!");
                } else if (etEmail.getText().toString().length() == 0) {
                    etEmail.setError("Enter Email");
                } else {
                    if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {
                        showProgressDialog();
                        JSONObject params = new JSONObject();

                        try {
                            params.put("booking_id", bookingid);
                            params.put("email_address", etEmail.getText().toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Allurl.EmailTicketSend, params, response -> {

                            Log.i("Response-->", String.valueOf(response));

                            try {
                                JSONObject result = new JSONObject(String.valueOf(response));
                                String stat = result.getString("stat");
                                String msg = result.getString("message");
                                if (stat.equals("succ")) {

                                    Toast.makeText(Ticketdetails.this, msg, Toast.LENGTH_SHORT).show();


                                } else {

                                    hideProgressDialog();
                                    Toast.makeText(Ticketdetails.this, msg, Toast.LENGTH_SHORT).show();

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
                                Toast.makeText(Ticketdetails.this, error.toString(), Toast.LENGTH_SHORT).show();

                            }
                        }) {
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("Authorization", token);
                                return params;
                            }
                        };

                        Volley.newRequestQueue(Ticketdetails.this).add(jsonRequest);

                    } else {

                        Toast.makeText(getApplicationContext(), "Ooops! Internet Connection Error", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });
    }


    public void downloadTicket(TicketModel ticketModel) {

        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {
            showProgressDialog();
            JSONObject params = new JSONObject();

            try {
                params.put("booking_id", ticketModel.getId());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Allurl.DownloadTicket, params, response -> {

                Log.i("Response-->", String.valueOf(response));

                try {
                    JSONObject result = new JSONObject(String.valueOf(response));
                    String stat = result.getString("stat");
                    String msg = result.getString("message");
                    String data = result.getString("data");
                    if (stat.equals("succ")) {
                        String url = "http://dev8.ivantechnology.in/tnevi/storage/app/" + data;
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
                        Toast.makeText(Ticketdetails.this, msg, Toast.LENGTH_SHORT).show();

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
                    Toast.makeText(Ticketdetails.this, error.toString(), Toast.LENGTH_SHORT).show();

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


    public void ticketdetails() {


        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {


            showProgressDialog();

            JSONObject params = new JSONObject();

            try {
                params.put("event_id", eventId);

            } catch (JSONException e) {
                e.printStackTrace();
            }


            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Allurl.EventTicket, params, response -> {

                Log.i("Response-->", String.valueOf(response));

                try {
                    JSONObject result = new JSONObject(String.valueOf(response));
                    String msg = result.getString("message");
                    Log.d(TAG, "msg-->" + msg);
                    String stat = result.getString("stat");
                    if (stat.equals("succ")) {

                        ticketModelArrayList = new ArrayList<>();
                        JSONArray response_data = result.getJSONArray("data");
                        for (int i = 0; i < response_data.length(); i++) {

                            TicketModel ticketModel = new TicketModel();
                            JSONObject responseobj = response_data.getJSONObject(i);
                            JSONArray seatsdata = responseobj.getJSONArray("seats");
                            String seatno = "";
                            String row_name = "";
                            String block_name = "";
                            String qrcode = "";
                            String status = "";
                            String multiqr = "";
                            for (int j = 0; j < seatsdata.length(); j++) {
                                JSONObject seatobj = seatsdata.getJSONObject(j);
                                JSONObject seatobj1 = seatsdata.getJSONObject(0);
                                row_name = seatobj1.getString("row_name");
                                block_name = seatobj1.getString("block_name");
                                seatno = seatno + seatobj.getString("seat_no") + ",";
                                qrcode = seatobj.getString("qr_id");
                                multiqr = qrcode + seatobj.getString("qr_id") + ",";
                                status = seatobj.getString("status");
                                Log.d(TAG, "multiqr-->" + multiqr);
                                Log.d(TAG, "qr-->" + multiqr);

                            }
                            ticketModel.setSeat_no(seatno);
                            ticketModel.setBlock_name(block_name);
                            ticketModel.setRow_name(row_name);
                            ticketModel.setQr_id(qrcode);
                            ticketModel.setStatus(status);
                            ticketModel.setEvent_name(responseobj.getString("event_name"));
                            ticketModel.setEvent_date(responseobj.getString("event_date"));
                            ticketModel.setEvent_address(responseobj.getString("event_address"));
                            ticketModel.setId(responseobj.getString("id"));
                            ticketModelArrayList.add(ticketModel);

                        }

                        setupRecycler();

                    } else {

                        Toast.makeText(Ticketdetails.this, "invalid", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideProgressDialog();

                //TODO: handle success
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Ticketdetails.this, error.toString(), Toast.LENGTH_SHORT).show();

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

        ticketAdapter = new TicketAdapter(this, ticketModelArrayList);
        rvTicket.setAdapter(ticketAdapter);
        rvTicket.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

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