package com.example.tnevi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Checkoutaddress extends AppCompatActivity {
    ImageView btn_back;
    LinearLayout btnCheckout;
    TextView tvEventname, tvDate, tvAddress, tvTotal;
    String eventname, date, address, total, postedby, rowid, blockid, eventid, latvalue, lonvalue, currencyid, fees, seatnumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkoutaddress);
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

        btn_back = findViewById(R.id.btn_back);
        btnCheckout = findViewById(R.id.btnCheckout);
        tvEventname = findViewById(R.id.tvEventname);
        tvDate = findViewById(R.id.tvDate);
        tvAddress = findViewById(R.id.tvAddress);
        tvTotal = findViewById(R.id.tvTotal);
        tvEventname.setText(eventname);
        tvAddress.setText(address);
        tvDate.setText(date);
        tvTotal.setText("$"+total);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Checkoutaddress.this, Checkoutpaymentinfo.class);
                intent.putExtra("eventname", eventname);
                intent.putExtra("date", date);
                intent.putExtra("address", address);
                intent.putExtra("total",total);
                intent.putExtra("name", postedby);
                intent.putExtra("rowid", rowid);
                intent.putExtra("blockid", blockid);
                intent.putExtra("eventid", eventid);
                intent.putExtra("latvalue", latvalue);
                intent.putExtra("lonvalue", lonvalue);
                intent.putExtra("currencyid", currencyid);
                intent.putExtra("fees", fees);
                intent.putExtra("seatnumber", seatnumber);
                startActivity(intent);
            }
        });

    }
}