package com.app.tnevi;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.app.tnevi.model.SeatModel;
import com.app.tnevi.model.SeatModel2;
import com.bumptech.glide.Glide;
import com.app.tnevi.Allurl.Allurl;

import com.app.tnevi.internet.CheckConnectivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Gallerybooking extends AppCompatActivity {

    private static final String TAG = "myapp";
    LinearLayout btnDone;
    ImageView btn_back, ivIncrease, ivDecrese, venueImg;
    String eventname, address, date, response2, maxprice, event_id, token, postedby, latval, longval, currencyid, fees, venueimage;
    TextView tvEventname, tvDate, tvAddress, tvQuantity, tvGrandtotal;
    Spinner spSection, spRow;
    int count = 0;
    private static final String SHARED_PREFS = "sharedPrefs";
    String rowid = "";
    ArrayList<String> row = new ArrayList<>();
    ArrayList<SeatModel> setrow = new ArrayList<>();
    ArrayList<SeatModel> setprice = new ArrayList<>();
    String blockid = "";
    ArrayList<String> block = new ArrayList<>();
    ArrayList<SeatModel2> setblock = new ArrayList<>();
    String seatprice = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallerybooking);
        Intent intent = getIntent();
        eventname = intent.getStringExtra("eventname");
        date = intent.getStringExtra("date");
        address = intent.getStringExtra("address");
        response2 = intent.getStringExtra("response2");
        maxprice = intent.getStringExtra("maxprice");
        event_id = intent.getStringExtra("event_id");
        postedby = intent.getStringExtra("name");
        latval = intent.getStringExtra("latvalue");
        longval = intent.getStringExtra("lonvalue");
        currencyid = intent.getStringExtra("currencyid");
        fees = intent.getStringExtra("fees");
        venueimage = intent.getStringExtra("venueimage");
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        tvEventname = findViewById(R.id.tvEventname);
        tvDate = findViewById(R.id.tvDate);
        tvAddress = findViewById(R.id.tvAddress);
        btn_back = findViewById(R.id.btn_back);
        btnDone = findViewById(R.id.btnDone);
        spSection = findViewById(R.id.spSection);
        ivIncrease = findViewById(R.id.ivIncrease);
        ivDecrese = findViewById(R.id.ivDecrese);
        tvQuantity = findViewById(R.id.tvQuantity);
        tvGrandtotal = findViewById(R.id.tvGrandtotal);
        venueImg = findViewById(R.id.venueImg);

        spRow = findViewById(R.id.spRow);
        tvEventname.setText(eventname);
        tvAddress.setText(address);
        tvDate.setText(date);

        Glide.with(this)
                .load("http://dev8.ivantechnology.in/tnevi/storage/app/" + venueimage)
                .placeholder(R.drawable.gallery)
                .into(venueImg);

        onClick();
        spsection();
        sprow(-1);
    }


    public void onClick() {

        spRow.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i > 0) {
                    rowid = setrow.get(i - 1).getRow_id();
                    seatprice = setprice.get(i-1).getSeat_price();
                    Log.d(TAG, "rowid-->" + rowid);
                    Log.d(TAG, "price-->" + seatprice);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        spSection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int correctPos = i - 1;
                if (i > 0) {
                    blockid = setblock.get(correctPos).getBlock_id();
                    count=0;
                    tvQuantity.setText("" + count);
                    tvGrandtotal.setText("$0.00");
                    Log.d(TAG, "blockid-->" + blockid);

                    sprow(correctPos);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (spSection.getSelectedItem().toString().equals("Select")) {
                    Toast.makeText(getApplicationContext(), "Select Section", Toast.LENGTH_LONG).show();
                } else if (spRow.getSelectedItem().toString().equals("Select")) {
                    Toast.makeText(getApplicationContext(), "Select Row", Toast.LENGTH_LONG).show();
                } else if (count == 0) {
                    Toast.makeText(getApplicationContext(), "Select Quantity", Toast.LENGTH_LONG).show();
                } else {

                    seatAviliblity();
                }
            }
        });


        ivIncrease.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {

                count = count + 1;
                if (count > 0) {
                    tvQuantity.setText("" + count);
                    double price= Double.parseDouble(seatprice);
                    double sum1 = count * price;
                    tvGrandtotal.setText("$" + sum1);
                } else {
                    count = 0;
                    tvQuantity.setText("" + 0);
                    tvGrandtotal.setText("$0");
                }
            }
        });

        ivDecrese.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {

                count--;
                if (count > 0) {
                    tvQuantity.setText("" + count);
                    double price= Double.parseDouble(seatprice);
                    double sum = count * price;
                    tvGrandtotal.setText("$" + sum);
                } else {
                    count = 0;
                    tvQuantity.setText("" + 0);
                    tvGrandtotal.setText("$0");
                }

            }
        });


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });


    }


    public void spsection() {
        block.clear();
        setblock.clear();
        block.add("Select");
        try {
            JSONObject result = new JSONObject(response2);
            JSONObject userdeatisObj = result.getJSONObject("data");
            JSONObject venueObj = userdeatisObj.getJSONObject("venue");
            JSONArray blockarray = venueObj.getJSONArray("blocks");
            for (int i = 0; i < blockarray.length(); i++) {
                JSONObject blockobj = blockarray.getJSONObject(i);
                String block_name = blockobj.getString("block_name");
                String block_id = blockobj.getString("id");
                block.add(block_name);
                SeatModel2 seatModel2 = new SeatModel2(block_name, block_id);
                setblock.add(seatModel2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, block);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSection.setAdapter(arrayAdapter);

    }


    private void sprow(int correctPos) {

        row.clear();
        setrow.clear();
        setprice.clear();
        row.add("Select");
        if (correctPos > -1) {
            try {
                JSONObject result = new JSONObject(response2);
                JSONObject userdeatisObj = result.getJSONObject("data");
                JSONObject venueObj = userdeatisObj.getJSONObject("venue");
                JSONArray blockarray = venueObj.getJSONArray("blocks");
                // for (int i = 0; i < blockarray.length(); i++) {
                JSONObject blockobj = blockarray.getJSONObject(correctPos);
                String row_name = "";
                String row_id = "";
                String seat_price = "";
                JSONArray blockrowsarray = blockobj.getJSONArray("blockrows");
                for (int j = 0; j < blockrowsarray.length(); j++) {
                    JSONObject blockrowsobj = blockrowsarray.getJSONObject(j);
                    row_name = blockrowsobj.getString("row_name");
                    row_id = blockrowsobj.getString("id");
                    row.add(row_name);
                    JSONArray seatarray = blockrowsobj.getJSONArray("seat");
                    for (int k = 0; k<seatarray.length(); k++){
                        JSONObject seatobj = seatarray.getJSONObject(k);
                        seat_price = seatobj.getString("seat_price");
                        SeatModel seatModel = new SeatModel(row_name, row_id, seat_price);
                        setrow.add(seatModel);
                        setprice.add(seatModel);
                    }

                }

                // }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, row);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRow.setAdapter(arrayAdapter);


    }


    public void seatAviliblity() {


//        Intent intent = new Intent(Gallerybooking.this, Pricedetails.class);
//        intent.putExtra("eventname", eventname);
//        intent.putExtra("date", date);
//        intent.putExtra("address", address);
//        intent.putExtra("selectedrow",  spRow.getSelectedItem().toString());
//        intent.putExtra("spSection",  spSection.getSelectedItem().toString());
//        intent.putExtra("seatnumber",  ""+count);
//        intent.putExtra("total", ""+Integer.parseInt("12") * count);
//        startActivity(intent);

        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {
            showProgressDialog();
            JSONObject params = new JSONObject();

            try {
                params.put("event_id", event_id);
                params.put("block_id", blockid);
                params.put("row_id", rowid);
                params.put("no_of_seat", count);


            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Allurl.SeatAvibility, params, response -> {

                Log.i("Response-->", String.valueOf(response));

                try {
                    JSONObject result = new JSONObject(String.valueOf(response));
                    String msg = result.getString("message");
                    String stat = result.getString("stat");
                    if (stat.equals("succ")) {

                        JSONObject response_data = result.getJSONObject("data");
                        String Tax = response_data.getString("tax_rate");
                        double price= Double.parseDouble(seatprice);
                        double sum = count * price;
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Gallerybooking.this, Pricedetails.class);
                        intent.putExtra("eventname", eventname);
                        intent.putExtra("date", date);
                        intent.putExtra("address", address);
                        intent.putExtra("selectedrow", spRow.getSelectedItem().toString());
                        intent.putExtra("spSection", spSection.getSelectedItem().toString());
                        intent.putExtra("seatnumber", "" + count);
                        intent.putExtra("total", "" + sum);
                        intent.putExtra("name", postedby);
                        intent.putExtra("rowid", rowid);
                        intent.putExtra("blockid", blockid);
                        intent.putExtra("eventid", event_id);
                        intent.putExtra("latvalue", latval);
                        intent.putExtra("lonvalue", longval);
                        intent.putExtra("currencyid", currencyid);
                        intent.putExtra("fees", fees);
                        intent.putExtra("tax", Tax);
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
                    Toast.makeText(Gallerybooking.this, error.toString(), Toast.LENGTH_SHORT).show();

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