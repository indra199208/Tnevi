package com.app.tnevi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;



public class Checkoutaddress extends AppCompatActivity {
    ImageView btn_back;
    LinearLayout btnCheckout;
    TextView tvEventname, tvDate, tvAddress, tvTotal;
    String eventname, date, address, total, postedby, rowid, blockid,
            eventid, latvalue, lonvalue, currencyid, fees, seatnumber, dis_amount, tax, maintotal;

    EditText etZip, etState, etAddress, etFullname, etNameonticket;

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
        dis_amount = intent.getStringExtra("dis_amount");
        tax = intent.getStringExtra("tax");
        maintotal = intent.getStringExtra("maintotal");

        btn_back = findViewById(R.id.btn_back);
        btnCheckout = findViewById(R.id.btnCheckout);
        tvEventname = findViewById(R.id.tvEventname);
        tvDate = findViewById(R.id.tvDate);
        tvAddress = findViewById(R.id.tvAddress);
        tvTotal = findViewById(R.id.tvTotal);
        etZip = findViewById(R.id.etZip);
        etState = findViewById(R.id.etState);
        etAddress = findViewById(R.id.etAddress);
        etFullname = findViewById(R.id.etFullname);
        etNameonticket = findViewById(R.id.etNameonticket);

        tvEventname.setText(eventname);
        tvAddress.setText(address);
        tvDate.setText(date);
        tvTotal.setText("$"+maintotal);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (etNameonticket.getText().toString().length()==0){
                    Toast.makeText(Checkoutaddress.this, "Enter Name on ticket", Toast.LENGTH_SHORT).show();
                }else if (etFullname.getText().toString().length()==0){
                    Toast.makeText(Checkoutaddress.this, "Enter Full name", Toast.LENGTH_SHORT).show();
                }else if(etAddress.getText().toString().length()==0){
                    Toast.makeText(Checkoutaddress.this, "Enter Address", Toast.LENGTH_SHORT).show();
                }else if (etState.getText().toString().length()==0){
                    Toast.makeText(Checkoutaddress.this, "Enter State", Toast.LENGTH_SHORT).show();
                }else if (etZip.getText().toString().length()==0){
                    Toast.makeText(Checkoutaddress.this, "Enter Zip code", Toast.LENGTH_SHORT).show();
                }else{

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
                    intent.putExtra("tax", tax);
                    intent.putExtra("seatnumber", seatnumber);
                    intent.putExtra("dis_amount", dis_amount);
                    startActivity(intent);

                }


            }
        });

    }
}