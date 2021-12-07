package com.ivan.tnevi;

import android.app.Activity;
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

import androidx.annotation.Nullable;
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
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Checkoutpaymentinfo extends AppCompatActivity {

    ImageView btn_back;
    TextView tvEventname, tvDate, tvAddress, tvSubtotal, tvGrandtotal;
    LinearLayout btnDone;
    String eventname, date, address, total, postedby, rowid, blockid, eventid, latvalue, lonvalue , currencyid, fees, seatnumber, token ;
    private static final String SHARED_PREFS = "sharedPrefs";

    public static final String clientKey = "AUdZashbU83Uow1ZgTUHgXL5lAXepjAd6hwymlCQi08TTEvWYfS132F91TVtt5jO0toWvTBjSDT7hc4O";
    public static final int PAYPAL_REQUEST_CODE = 123;

    // Paypal Configuration Object
    private static PayPalConfiguration config = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(clientKey);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkoutpaymentinfo);
        Intent intent = getIntent();
        eventname = intent.getStringExtra("eventname");
        date = intent.getStringExtra("date");
        address = intent.getStringExtra("address");
        total = intent.getStringExtra("total");
        postedby = intent.getStringExtra("name");
        rowid = intent.getStringExtra("rowid");
        blockid = intent.getStringExtra("blockid");
        eventid = intent.getStringExtra("eventid");
        latvalue = intent.getStringExtra("latvalue");
        lonvalue = intent.getStringExtra("lonvalue");
        currencyid = intent.getStringExtra("currencyid");
        fees = intent.getStringExtra("fees");
        seatnumber = intent.getStringExtra("seatnumber");
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        tvEventname = findViewById(R.id.tvEventname);
        tvDate = findViewById(R.id.tvDate);
        tvAddress = findViewById(R.id.tvAddress);
        btn_back = findViewById(R.id.btn_back);
        btnDone = findViewById(R.id.btnDone);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvGrandtotal = findViewById(R.id.tvGrandtotal);
        tvEventname.setText(eventname);
        tvDate.setText(date);
        tvAddress.setText(address);
        tvSubtotal.setText("$"+total);
        tvGrandtotal.setText("$"+total);


        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                getPayment();

            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });


    }




    private void getPayment() {

        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(total)), "USD", "Ticket Amount",
                PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PAYPAL_REQUEST_CODE) {

            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        String paymentDetails = confirm.toJSONObject().toString(4);
                        JSONObject payObj = new JSONObject(paymentDetails);
                        String payID = payObj.getJSONObject("response").getString("id");
                        String state = payObj.getJSONObject("response").getString("state");
                        Toast.makeText(Checkoutpaymentinfo.this, "Payment " + state + "\n with payment id is " + payID, Toast.LENGTH_SHORT).show();

                        bookticket();


                    } catch (JSONException e) {
                        Log.e("Error", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }


    public void bookticket(){


        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {
            showProgressDialog();
            JSONObject params = new JSONObject();

            try {
                params.put("event_id", eventid);
                params.put("block_id", blockid);
                params.put("row_id", rowid);
                params.put("no_of_seat", seatnumber);
                params.put("name_on_ticket", postedby);
                params.put("full_name", postedby);
                params.put("book_address", address);
                params.put("book_state", "WB");
                params.put("book_zipcode", "700074");
                params.put("book_lat_val", latvalue);
                params.put("book_long_val", lonvalue);
                params.put("currency_id", currencyid);
                params.put("fees", fees);
                params.put("grand_total", total);
                params.put("tran_id", "tran123");
                params.put("inv_id", "inv123");
                params.put("payer_email", "debasis@ppayment.com");
                params.put("payment_amount ", total);
                params.put("apply_epoints ", "");
                params.put("epoints_discount ", "");
                params.put("commission_discount ", "");
                params.put("ticket_discount", "");
                params.put("coupon_code", "");
                params.put("coupon_discount", "");
                params.put("total_discount", "");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Allurl.BookTicket, params, response -> {

                Log.i("Response-->", String.valueOf(response));

                try {
                    JSONObject result = new JSONObject(String.valueOf(response));
                    String msg = result.getString("message");
                    String stat = result.getString("stat");
                    if (stat.equals("succ")) {

                        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Checkoutpaymentinfo.this, MainActivity.class);
                        startActivity(intent);

                    } else {

                        hideProgressDialog();
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(Checkoutpaymentinfo.this, error.toString(), Toast.LENGTH_SHORT).show();

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