package com.app.tnevi;

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
import com.app.tnevi.Allurl.Allurl;

import com.app.tnevi.internet.CheckConnectivity;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.stripe.android.PaymentConfiguration;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Checkoutpaymentinfo extends AppCompatActivity {

    ImageView btn_back;
    TextView tvEventname, tvDate, tvAddress, tvSubtotal, tvGrandtotal, tvDiscount, tvTax;
    LinearLayout btnDone;
    String eventname, date, address, total, postedby, rowid, blockid, eventid, latvalue,
            lonvalue , currencyid, fees, seatnumber, token, dis_amount , tax, state,  zip;
    private static final String SHARED_PREFS = "sharedPrefs";
    String ultimatePrice = "";

//    public static final String clientKey = "AXtUmNzJQFSZ_SmHv0nBBQ7tcrRMRplPK0C1ozdrytJeDEImYlN5OBSOD0fXUUp2ce9_MtQFVreVQqPI";
//    public static final int PAYPAL_REQUEST_CODE = 123;
//
//    // Paypal Configuration Object
//    private static PayPalConfiguration config = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
//            .clientId(clientKey);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkoutpaymentinfo);
//        PaymentConfiguration.init(
//                getApplicationContext(),
//                "pk_test_51K9MAmDWSUf4dQGJudEZMh1qkrQ9AIsVrvauopYN1ec05IN78YUg8uQ2RQAP231S6uYRDgd83rO2KkeCQvy7OjW600IwIiwpbU"
//        );
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
        dis_amount = intent.getStringExtra("dis_amount");
        tax = intent.getStringExtra("tax");
        state = intent.getStringExtra("state");
        zip = intent.getStringExtra("zip");
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        tvEventname = findViewById(R.id.tvEventname);
        tvDate = findViewById(R.id.tvDate);
        tvAddress = findViewById(R.id.tvAddress);
        btn_back = findViewById(R.id.btn_back);
        btnDone = findViewById(R.id.btnDone);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvGrandtotal = findViewById(R.id.tvGrandtotal);
        tvDiscount = findViewById(R.id.tvDiscount);
        tvTax = findViewById(R.id.tvTax);
        tvEventname.setText(eventname);
        tvDate.setText(date);
        tvAddress.setText(address);
        tvSubtotal.setText("$"+total);
        tvTax.setText("$"+tax);
        if (dis_amount==null){
            double sum= Double.parseDouble(total);
            double taxsum = Double.parseDouble(tax);
            ultimatePrice = String.valueOf(sum + taxsum);
            tvDiscount.setText("$0.00");
            tvGrandtotal.setText("$"+ultimatePrice);
        }else {
            double sum= Double.parseDouble(total);
            double taxsum = Double.parseDouble(tax);
            double discount = Double.parseDouble(dis_amount);
            ultimatePrice = String.valueOf((sum + taxsum) - discount);
            tvDiscount.setText("$"+dis_amount);
            tvGrandtotal.setText("$"+ultimatePrice);
        }


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


        Intent intent = new Intent(Checkoutpaymentinfo.this, activity_checkout.class);
        intent.putExtra("eventname", eventname);
        intent.putExtra("date", date);
        intent.putExtra("address", address);
        intent.putExtra("total",ultimatePrice);
        intent.putExtra("name", postedby);
        intent.putExtra("rowid", rowid);
        intent.putExtra("blockid", blockid);
        intent.putExtra("eventid", eventid);
        intent.putExtra("latvalue", latvalue);
        intent.putExtra("lonvalue", lonvalue);
        intent.putExtra("currencyid", currencyid);
        intent.putExtra("fees", fees);
        intent.putExtra("tax", tax);
        intent.putExtra("seatnumber", seatnumber);
        intent.putExtra("dis_amount", dis_amount);
        intent.putExtra("zip", zip);
        intent.putExtra("state", state);
        startActivity(intent);

//        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(ultimatePrice)), "USD", "Ticket Amount",
//                PayPalPayment.PAYMENT_INTENT_SALE);
//        Intent intent = new Intent(this, PaymentActivity.class);
//        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
//        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
//        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == PAYPAL_REQUEST_CODE) {
//
//            if (resultCode == Activity.RESULT_OK) {
//                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
//                if (confirm != null) {
//                    try {
//                        String paymentDetails = confirm.toJSONObject().toString(4);
//                        JSONObject payObj = new JSONObject(paymentDetails);
//                        String payID = payObj.getJSONObject("response").getString("id");
//                        String state = payObj.getJSONObject("response").getString("state");
//                        Toast.makeText(Checkoutpaymentinfo.this, "Payment " + state + "\n with payment id is " + payID, Toast.LENGTH_SHORT).show();
//
//                        bookticket();
//
//
//                    } catch (JSONException e) {
//                        Log.e("Error", "an extremely unlikely failure occurred: ", e);
//                    }
//                }
//            } else if (resultCode == Activity.RESULT_CANCELED) {
//                Log.i("paymentExample", "The user canceled.");
//            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
//                Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
//            }
//        }
//    }
//
//    @Override
//    public void onDestroy() {
//        stopService(new Intent(this, PayPalService.class));
//        super.onDestroy();
//    }


//    public void bookticket(){
//
//
//        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {
//            showProgressDialog();
//            JSONObject params = new JSONObject();
//
//            try {
//                params.put("event_id", eventid);
//                params.put("block_id", blockid);
//                params.put("row_id", rowid);
//                params.put("no_of_seat", seatnumber);
//                params.put("name_on_ticket", postedby);
//                params.put("full_name", postedby);
//                params.put("book_address", address);
//                params.put("book_state", "WB");
//                params.put("book_zipcode", "700074");
//                params.put("book_lat_val", latvalue);
//                params.put("book_long_val", lonvalue);
//                params.put("currency_id", currencyid);
//                params.put("fees", fees);
//                params.put("grand_total", ultimatePrice);
//                params.put("tran_id", "tran123");
//                params.put("inv_id", "inv123");
//                params.put("payer_email", "debasis@ppayment.com");
//                params.put("payment_amount ", ultimatePrice);
//                params.put("apply_epoints ", "");
//                params.put("epoints_discount ", "");
//                params.put("commission_discount ", "");
//                params.put("ticket_discount", "");
//                params.put("coupon_code", "");
//                params.put("coupon_discount", "");
//                params.put("total_discount", "");
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Allurl.BookTicket, params, response -> {
//
//                Log.i("Response-->", String.valueOf(response));
//
//                try {
//                    JSONObject result = new JSONObject(String.valueOf(response));
//                    String msg = result.getString("message");
//                    String stat = result.getString("stat");
//                    if (stat.equals("succ")) {
//
//                        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(Checkoutpaymentinfo.this, MainActivity.class);
//                        startActivity(intent);
//
//                    } else {
//
//                        hideProgressDialog();
//                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
//                    }
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//
//                hideProgressDialog();
//
//                //TODO: handle success
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    hideProgressDialog();
//                    Toast.makeText(Checkoutpaymentinfo.this, error.toString(), Toast.LENGTH_SHORT).show();
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
//    }


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