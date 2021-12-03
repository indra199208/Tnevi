package com.example.tnevi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tnevi.Allurl.Allurl;
import com.example.tnevi.model.CurrencyModel;
import com.example.tnevi.session.SessionManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Eventdescription extends AppCompatActivity {


    LinearLayout btnEventcost;
    String eventname, venuename, venueaddress, phone, startdate,
            eventdate, enddate, description, eventLat, eventLong, catid, eventdate2, address2;
    EditText etDescription;
    ImageView btn_back;
    Spinner spCurrency;
    private static final String TAG = "myapp";
    String currencyid = "";
    ArrayList<String> currency = new ArrayList<>();
    ArrayList<CurrencyModel> setcurrency = new ArrayList<>();
    String msg;
    int MY_SOCKET_TIMEOUT_MS = 90000;
    private static final String SHARED_PREFS = "sharedPrefs";
    SessionManager sessionManager;
    String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventdescription);
        btnEventcost = findViewById(R.id.btnEventcost);
        etDescription = findViewById(R.id.etDescription);
        spCurrency = findViewById(R.id.spCurrency);
        Intent intent = getIntent();
        eventname = intent.getStringExtra("eventname");
        venuename = intent.getStringExtra("venuename");
        venueaddress = intent.getStringExtra("venueaddress");
        phone = intent.getStringExtra("phone");
        startdate = intent.getStringExtra("startdate");
        eventdate = intent.getStringExtra("eventdate");
        enddate = intent.getStringExtra("enddate");
        eventLat = intent.getStringExtra("eventLat");
        eventLong = intent.getStringExtra("eventLong");
        catid = intent.getStringExtra("id");
        eventdate2 = intent.getStringExtra("eventdate2");
        address2 = intent.getStringExtra("address2");
        btn_back = findViewById(R.id.btn_back);

        sessionManager = new SessionManager(getApplicationContext());
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");

        spCurrency();

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                onBackPressed();
            }
        });


        btnEventcost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gotoEventcost();


            }
        });


        spCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i > 0) {
                    currencyid = setcurrency.get(i - 1).getCurrencyId();
//                    customerName = setcustomer.get(i-1).getCustomerName();
                    Log.d(TAG, "value --->" + currencyid);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }



    private void spCurrency() {

        showProgressDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Allurl.GetCurrency,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("CurrencyResponse-->", response);
                        currency.clear();
                        setcurrency.clear();
                        currency.add("Select Currrency");

                        try {

                            JSONObject object = new JSONObject(response);
                            msg = object.getString("message");
                            String stat = object.getString("stat");
                            if (stat.equals("succ")) {
                                JSONArray jsonArray = object.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject currencyObj = jsonArray.getJSONObject(i);
                                    String id = currencyObj.getString("id");
                                    String currency_name = currencyObj.getString("currency_name");
                                    currency.add(currency_name);
                                    CurrencyModel currencyModel = new CurrencyModel(currency_name, id);
                                    setcurrency.add(currencyModel);
                                }

                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                        (Eventdescription.this, android.R.layout.simple_spinner_dropdown_item, currency);
                                //selected item will look like a spinner set from XML
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spCurrency.setAdapter(spinnerArrayAdapter);
//                                spCurrency.setBackground(getResources().getDrawable(R.drawable.spinner_shape));
//                                selectCustomer.setPadding(30, 0, 40, 0);


                            } else {

                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(Eventdescription.this, "Volly Error", Toast.LENGTH_LONG).show();

                        }

                        hideProgressDialog();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                hideProgressDialog();
                Toast.makeText(Eventdescription.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", token);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(Eventdescription.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }



    public void gotoEventcost() {

        description = etDescription.getText().toString();
        if (description.length() == 0) {

            Toast.makeText(getApplicationContext(), "Enter Description", Toast.LENGTH_SHORT).show();


        }else if (currencyid.matches("")){

            Toast.makeText(getApplicationContext(), "Select Currency", Toast.LENGTH_LONG).show();

        } else {


            Intent intent = new Intent(Eventdescription.this, Arrangeseats.class);
            intent.putExtra("eventname", eventname);
            intent.putExtra("venuename", venuename);
            intent.putExtra("venueaddress", venueaddress);
            intent.putExtra("phone", phone);
            intent.putExtra("currency", currencyid);
            intent.putExtra("startdate", startdate);
            intent.putExtra("eventdate", eventdate);
            intent.putExtra("enddate", enddate);
            intent.putExtra("description", description);
            intent.putExtra("eventLat", eventLat);
            intent.putExtra("eventLong", eventLong);
            intent.putExtra("id", catid);
            intent.putExtra("eventdate2", eventdate2);
            intent.putExtra("address2", address2);

            startActivity(intent);
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