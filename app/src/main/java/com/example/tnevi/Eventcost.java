package com.example.tnevi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tnevi.model.CurrencyModel;
import com.example.tnevi.session.SessionManager;

import java.util.ArrayList;

public class Eventcost extends AppCompatActivity {

    LinearLayout eventBannerad;
    String eventname, venuename, venueaddress, phone, startdate, eventdate, enddate, description, token,
            eventLat, eventdate2, eventLong, catid, seatObject, address2, venueimagepath;
    Spinner spCurrency, spPolicy, spFeature, spHighlight;
    private static final String TAG = "myapp";
    String currencyid = "";
    ArrayList<String> currency = new ArrayList<>();
    ArrayList<CurrencyModel> setcurrency = new ArrayList<>();
    String msg;
    int MY_SOCKET_TIMEOUT_MS = 90000;
    private static final String SHARED_PREFS = "sharedPrefs";
    SessionManager sessionManager;
    ImageView btn_back;
    EditText etTicketAmount, etQuantity, etCommission;
    String ticketamount, quantity, commission, feature, highlight;
    Switch btnMakefreeEvent;
    String freestat = "2";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventcost);
        eventBannerad = findViewById(R.id.eventBannerad);
        Intent intent = getIntent();
        eventname = intent.getStringExtra("eventname");
        venuename = intent.getStringExtra("venuename");
        venueaddress = intent.getStringExtra("venueaddress");
        phone = intent.getStringExtra("phone");
        startdate = intent.getStringExtra("startdate");
        eventdate = intent.getStringExtra("eventdate");
        enddate = intent.getStringExtra("enddate");
        description = intent.getStringExtra("description");
        currencyid = intent.getStringExtra("currency");
        eventLat = intent.getStringExtra("eventLat");
        eventLong = intent.getStringExtra("eventLong");
        catid = intent.getStringExtra("id");
        eventdate2 = intent.getStringExtra("eventdate2");
        seatObject = intent.getStringExtra("seatObject");
        address2 = intent.getStringExtra("address2");
        venueimagepath = intent.getStringExtra("venueimagepath");

//        spCurrency = findViewById(R.id.spCurrency);
        spPolicy = findViewById(R.id.spPolicy);
        btn_back = findViewById(R.id.btn_back);
//        etTicketAmount = findViewById(R.id.etTicketAmount);
//        etQuantity = findViewById(R.id.etQuantity);
        etCommission = findViewById(R.id.etCommission);
        btnMakefreeEvent = findViewById(R.id.btnMakefreeEvent);
//        spFeature = findViewById(R.id.spFeature);
//        spHighlight = findViewById(R.id.spHighlight);

        btnMakefreeEvent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    freestat = "1";

                } else {

                    freestat = "2";
                }
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                onBackPressed();
            }
        });

        sessionManager = new SessionManager(getApplicationContext());
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");


//        spCurrency();
//        spFeature();
//        spHighlight();
        onclick();


        eventBannerad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                gotoEventimagead();


            }
        });
    }


    private void onclick() {

//        spCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//
//                if (i > 0) {
//                    currencyid = setcurrency.get(i - 1).getCurrencyId();
////                    customerName = setcustomer.get(i-1).getCustomerName();
//                    Log.d(TAG, "value --->" + currencyid);
//
//                }
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });


    }

//
//    private void spFeature() {
//
//        List<String> feature = new ArrayList<String>();
//
//        feature.add("Please Select");
//        feature.add("Yes");
//        feature.add("No");
//
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, feature);
//        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spFeature.setAdapter(arrayAdapter);
//
//
//    }
//
//
//    private void spHighlight() {
//
//        List<String> highlight = new ArrayList<String>();
//
//        highlight.add("Please Select");
//        highlight.add("Yes");
//        highlight.add("No");
//
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, highlight);
//        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spHighlight.setAdapter(arrayAdapter);
//
//
//    }


//    private void spCurrency() {
//
//        showProgressDialog();
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, Allurl.GetCurrency,
//                new Response.Listener<String>() {
//
//                    @Override
//                    public void onResponse(String response) {
//                        Log.d("CurrencyResponse-->", response);
//                        currency.clear();
//                        setcurrency.clear();
//                        currency.add("Select Currrency");
//
//                        try {
//
//                            JSONObject object = new JSONObject(response);
//                            msg = object.getString("message");
//                            String stat = object.getString("stat");
//                            if (stat.equals("succ")) {
//                                JSONArray jsonArray = object.getJSONArray("data");
//                                for (int i = 0; i < jsonArray.length(); i++) {
//
//                                    JSONObject currencyObj = jsonArray.getJSONObject(i);
//                                    String id = currencyObj.getString("id");
//                                    String currency_name = currencyObj.getString("currency_name");
//                                    currency.add(currency_name);
//                                    CurrencyModel currencyModel = new CurrencyModel(currency_name, id);
//                                    setcurrency.add(currencyModel);
//                                }
//
//                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
//                                        (Eventcost.this, android.R.layout.simple_spinner_dropdown_item, currency);
//                                //selected item will look like a spinner set from XML
//                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                                spCurrency.setAdapter(spinnerArrayAdapter);
////                                spCurrency.setBackground(getResources().getDrawable(R.drawable.spinner_shape));
////                                selectCustomer.setPadding(30, 0, 40, 0);
//
//
//                            } else {
//
//                            }
//
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            Toast.makeText(Eventcost.this, "Volly Error", Toast.LENGTH_LONG).show();
//
//                        }
//
//                        hideProgressDialog();
//
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                hideProgressDialog();
//                Toast.makeText(Eventcost.this, error.toString(), Toast.LENGTH_SHORT).show();
//            }
//        }) {
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("Authorization", token);
//                return params;
//            }
//
//        };
//        RequestQueue requestQueue = Volley.newRequestQueue(Eventcost.this);
//        requestQueue.add(stringRequest);
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                MY_SOCKET_TIMEOUT_MS,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//
//    }


    public void gotoEventimagead() {

        ticketamount = "123";
        commission = etCommission.getText().toString();
//        feature = spFeature.getSelectedItem().toString();
//        highlight = spHighlight.getSelectedItem().toString();


        if (commission.length() == 0) {

            Toast.makeText(getApplicationContext(), "Enter Comission", Toast.LENGTH_LONG).show();

        } else {

            Intent intent = new Intent(Eventcost.this, Eventbaneerad.class);
            intent.putExtra("eventname", eventname);
            intent.putExtra("venuename", venuename);
            intent.putExtra("venueaddress", venueaddress);
            intent.putExtra("phone", phone);
            intent.putExtra("startdate", startdate);
            intent.putExtra("eventdate", eventdate);
            intent.putExtra("enddate", enddate);
            intent.putExtra("description", description);
            intent.putExtra("currency", currencyid);
            intent.putExtra("ticketamount", "123");
            intent.putExtra("commission", commission);
            intent.putExtra("eventLat", eventLat);
            intent.putExtra("eventLong", eventLong);
//            intent.putExtra("feature", feature);
//            intent.putExtra("highlight", highlight);
            intent.putExtra("id", catid);
            intent.putExtra("eventdate2", eventdate2);
            intent.putExtra("seatObject", seatObject);
            intent.putExtra("address2", address2);
            intent.putExtra("venueimagepath", venueimagepath);
            intent.putExtra("freestat", freestat);
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