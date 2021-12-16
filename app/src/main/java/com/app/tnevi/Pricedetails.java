package com.app.tnevi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.tnevi.Allurl.Allurl;

import com.app.tnevi.internet.CheckConnectivity;
import com.app.tnevi.model.CouponModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Pricedetails extends AppCompatActivity {
    private static final String TAG = "Myapp";
    ImageView btn_back;
    LinearLayout btnCheckout, btnCashApply, btnEpointsApply;
    TextView tvEventname, tvDate, tvAddress, tvSectionRow, tvSeats, tvSubtotal,
            tvGrandtotal, tvDiscount, tvCash, tvEpoints, tvAvailablecommission;
    String eventname, date, address, selectedrow, spSection, seatnumber, total, postedby, rowid, blockid, eventid, latvalue, lonvalue, currencyid, fees;
    String GrandTotal = "";
    private ArrayList<CouponModel> couponModelArrayList = new ArrayList<>();
    ArrayList<String> coupon = new ArrayList<>();
    String couponprice = "";
    String selectedCoupon = "";
    String token, epoints, dis_amount, commission, wallet_available_commission;
    private static final String SHARED_PREFS = "sharedPrefs";
    Spinner spCoupon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pricedetails);
        init();
        getAccount();
//        couponList();
        OnClick();

    }

    public void init() {
        Intent intent = getIntent();
        eventname = intent.getStringExtra("eventname");
        date = intent.getStringExtra("date");
        address = intent.getStringExtra("address");
        selectedrow = intent.getStringExtra("selectedrow");
        spSection = intent.getStringExtra("spSection");
        total = intent.getStringExtra("total");
        seatnumber = intent.getStringExtra("seatnumber");
        postedby = intent.getStringExtra("name");
        rowid = intent.getStringExtra("rowid");
        blockid = intent.getStringExtra("blockid");
        eventid = intent.getStringExtra("eventid");
        latvalue = intent.getStringExtra("latvalue");
        lonvalue = intent.getStringExtra("lonvalue");
        currencyid = intent.getStringExtra("currencyid");
        fees = intent.getStringExtra("fees");
//        rvCoupon = findViewById(R.id.rvCoupon);
//        spCoupon = findViewById(R.id.spCoupon);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");

        btn_back = findViewById(R.id.btn_back);
        btnCheckout = findViewById(R.id.btnCheckout);
        tvEventname = findViewById(R.id.tvEventname);
        tvDate = findViewById(R.id.tvDate);
        tvAddress = findViewById(R.id.tvAddress);
        tvSectionRow = findViewById(R.id.tvSectionRow);
        tvSeats = findViewById(R.id.tvSeats);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvGrandtotal = findViewById(R.id.tvGrandtotal);
        tvDiscount = findViewById(R.id.tvDiscount);
//        tvCash = findViewById(R.id.tvCash);
        btnCashApply = findViewById(R.id.btnCashApply);
        btnEpointsApply = findViewById(R.id.btnEpointsApply);
        tvEpoints = findViewById(R.id.tvEpoints);
        tvAvailablecommission = findViewById(R.id.tvAvailablecommission);

        tvEventname.setText(eventname);
        tvDate.setText(date);
        tvAddress.setText(address);
        tvSeats.setText(seatnumber);
        tvSectionRow.setText(spSection + ", Row " + selectedrow);
        tvSubtotal.setText("$" + total);
        tvGrandtotal.setText("$" + total);
        GrandTotal = total;
    }

    public void OnClick() {


//        spCoupon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//                if (position > 0) {
//                    couponprice = couponModelArrayList.get(position-1).getCoupon_amount();
//                    selectedCoupon = couponModelArrayList.get(position-1).getCoupon_code();
//                    tvCash.setText("$"+couponprice);
//
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });

        btnCashApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                applyCash();
            }
        });

        btnEpointsApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                applyEpoints();

            }
        });

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Pricedetails.this, Checkoutaddress.class);
                intent.putExtra("eventname", eventname);
                intent.putExtra("date", date);
                intent.putExtra("address", address);
                intent.putExtra("total", total);
                intent.putExtra("name", postedby);
                intent.putExtra("rowid", rowid);
                intent.putExtra("blockid", blockid);
                intent.putExtra("eventid", eventid);
                intent.putExtra("latvalue", latvalue);
                intent.putExtra("lonvalue", lonvalue);
                intent.putExtra("currencyid", currencyid);
                intent.putExtra("fees", fees);
                intent.putExtra("seatnumber", seatnumber);
                intent.putExtra("dis_amount", dis_amount);
                startActivity(intent);
            }
        });
    }

//    public void couponList(){
//
//        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {
//
//            showProgressDialog();
//
//            StringRequest stringRequest = new StringRequest(Request.Method.POST, Allurl.CouponList,
//                    new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//
//                            Log.i("Response-->", String.valueOf(response));
//                            coupon.clear();
//                            couponModelArrayList.clear();
//                            coupon.add("Coupon");
//
//                            try {
//                                JSONObject result = new JSONObject(String.valueOf(response));
//                                String msg = result.getString("message");
//                                Log.d(TAG, "msg-->" + msg);
//                                String stat = result.getString("stat");
//                                if (stat.equals("succ")) {
//
//                                    couponModelArrayList = new ArrayList<>();
//                                    JSONArray response_data = result.getJSONArray("data");
//                                    for (int i = 0; i < response_data.length(); i++) {
//
//                                        JSONObject responseobj = response_data.getJSONObject(i);
//                                        String id = responseobj.getString("id");
//                                        String couponamount = responseobj.getString("coupon_amount");
//                                        String couponcode = responseobj.getString("coupon_code");
//                                        coupon.add(couponcode);
//                                        CouponModel couponModel = new CouponModel(couponcode, couponamount);
//                                        couponModelArrayList.add(couponModel);
//
//                                    }
//
//                                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
//                                            (Pricedetails.this, android.R.layout.simple_spinner_dropdown_item, coupon);
//                                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                                    spCoupon.setAdapter(spinnerArrayAdapter);
//
//                                } else {
//
//                                    Log.d(TAG, "unsuccessfull - " + "Error");
//                                    hideProgressDialog();
//                                    Toast.makeText(Pricedetails.this, msg, Toast.LENGTH_SHORT).show();
//                                }
//
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
//                            hideProgressDialog();
//
//
//                        }
//                    },
//                    new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            NetworkResponse response = error.networkResponse;
//                            if (error instanceof ServerError && response != null) {
//                                try {
//                                    String res = new String(response.data,
//                                            HttpHeaderParser.parseCharset(response.headers, "utf-8"));
//
//                                    hideProgressDialog();
//                                    Toast.makeText(Pricedetails.this, error.toString(), Toast.LENGTH_SHORT).show();
//
//                                    // Now you can use any deserializer to make sense of data
//                                    JSONObject obj = new JSONObject(res);
//                                } catch (UnsupportedEncodingException e1) {
//                                    // Couldn't properly decode data to string
//                                    e1.printStackTrace();
//                                } catch (JSONException e2) {
//                                    // returned data is not JSONObject?
//                                    e2.printStackTrace();
//                                }
//                            }
//                        }
//                    }) {
//                @Override
//                public Map<String, String> getHeaders() throws AuthFailureError {
//                    Map<String, String> params = new HashMap<String, String>();
//                    params.put("Authorization", token);
//                    return params;
//                }
//
//            };
//
//            Volley.newRequestQueue(this).add(stringRequest);
//
//        } else {
//
//            Toast.makeText(getApplicationContext(), "OOPS! No Internet Connection", Toast.LENGTH_SHORT).show();
//
//        }
//    }
//
//
//    public void applyCoupon(){
//
//        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {
//
//            showProgressDialog();
//
//            JSONObject params = new JSONObject();
//
//            try {
//                params.put("couponcode", selectedCoupon);
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Allurl.CouponApply, params, response -> {
//
//                Log.i("Response-->", String.valueOf(response));
//                try {
//                    JSONObject result = new JSONObject(String.valueOf(response));
//                    String msg = result.getString("message");
//                    Log.d(TAG, "msg-->" + msg);
//                    String stat = result.getString("stat");
//                    dis_amount = result.getString("dis_amount");
//                    String dis_type = result.getString("dis_type");
//                    if (stat.equals("succ")) {
//
//                        Toast.makeText(Pricedetails.this, msg, Toast.LENGTH_SHORT).show();
//                        tvDiscount.setText("$"+dis_amount);
//                        int final_ = Integer.parseInt(total) - Integer.parseInt(dis_amount);
//                        GrandTotal = String.valueOf(final_);
//                        tvGrandtotal.setText("$"+GrandTotal);
//
//                    } else {
//
//                        Log.d(TAG, "unsuccessfull - " + "Error");
//                        Toast.makeText(Pricedetails.this, msg, Toast.LENGTH_SHORT).show();
//                    }
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                hideProgressDialog();
//
//                //TODO: handle success
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    Toast.makeText(Pricedetails.this, error.toString(), Toast.LENGTH_SHORT).show();
//
//                }
//            }) {
//                @Override
//                public Map<String, String> getHeaders() throws AuthFailureError {
//                    Map<String, String> params = new HashMap<String, String>();
//                    params.put("Authorization", token);
//                    return params;
//                }
//            };
//
//            Volley.newRequestQueue(this).add(jsonRequest);
//
//        } else {
//
//            Toast.makeText(getApplicationContext(), "Ooops! Internet Connection Error", Toast.LENGTH_SHORT).show();
//
//        }
//
//
//
//    }

    public void getAccount() {

        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {

            showProgressDialog();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, Allurl.GetAccount,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.i("Response3-->", String.valueOf(response));

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
                                    commission = walletObj.getString("commission");
                                    epoints = walletObj.getString("epoints");
                                    String address = profileObj.getString("address");
                                    String country_code = profileObj.getString("country_code");
                                    tvEpoints.setText(epoints + " ePoints");
                                    tvAvailablecommission.setText(commission);
                                    hideProgressDialog();

                                } else {

                                    hideProgressDialog();
                                    Log.d(TAG, "unsuccessfull - " + "Error");
                                    Toast.makeText(Pricedetails.this, msg, Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(Pricedetails.this, error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", token);
                    return params;
                }

            };


            RequestQueue requestQueue = Volley.newRequestQueue(Pricedetails.this);
            requestQueue.add(stringRequest);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    9000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        } else {

            Toast.makeText(getApplicationContext(), "OOPS! No Internet Connection", Toast.LENGTH_SHORT).show();

        }


    }

    public void applyEpoints() {


        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {

            showProgressDialog();

            JSONObject params = new JSONObject();

            try {
                params.put("apply_epoints", epoints);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Allurl.ApplyEpoints, params, response -> {

                Log.i("Response-->", String.valueOf(response));
                try {
                    JSONObject result = new JSONObject(String.valueOf(response));
                    String msg = result.getString("message");
                    Log.d(TAG, "msg-->" + msg);
                    String stat = result.getString("stat");
                    String epoints_discount = result.getString("epoints_discount");
                    String wallet_epoints = result.getString("wallet_epoints");
                    if (stat.equals("succ")) {

                        Toast.makeText(Pricedetails.this, msg, Toast.LENGTH_SHORT).show();

                    } else {

                        Log.d(TAG, "unsuccessfull - " + "Error");
                        Toast.makeText(Pricedetails.this, msg, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideProgressDialog();

                //TODO: handle success
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Pricedetails.this, error.toString(), Toast.LENGTH_SHORT).show();

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

    public void applyCash() {

        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {

            showProgressDialog();

            JSONObject params = new JSONObject();

            try {
                params.put("apply_cash", commission);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Allurl.Applycash, params, response -> {

                Log.i("Response-->", String.valueOf(response));
                try {
                    JSONObject result = new JSONObject(String.valueOf(response));
                    String msg = result.getString("message");
                    Log.d(TAG, "msg-->" + msg);
                    String stat = result.getString("stat");
                    if (stat.equals("succ")) {

                        Toast.makeText(Pricedetails.this, msg, Toast.LENGTH_SHORT).show();
                        dis_amount = result.getString("commission_discount");
                        wallet_available_commission = result.getString("wallet_available_commission");
                        tvDiscount.setText("$" + dis_amount);
                        int final_ = Integer.parseInt(total) - Integer.parseInt(dis_amount);
                        GrandTotal = String.valueOf(final_);
                        tvGrandtotal.setText("$"+GrandTotal);
                        tvAvailablecommission.setText(wallet_available_commission);

                    } else {

                        Log.d(TAG, "msg-->" +msg);
                        Toast.makeText(Pricedetails.this, msg, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideProgressDialog();

                //TODO: handle success
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Pricedetails.this, error.toString(), Toast.LENGTH_SHORT).show();

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