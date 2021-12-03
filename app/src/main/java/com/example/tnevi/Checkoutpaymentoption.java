package com.example.tnevi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Checkoutpaymentoption extends AppCompatActivity {

    ImageView btn_back;
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String TAG = "Myapp";
    String username, useremail, token, msg;
    TextView tvEmail;
    LinearLayout btnaddPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkoutpaymentoption);
        btn_back = findViewById(R.id.btn_back);
        tvEmail = findViewById(R.id.tvEmail);
        btnaddPayment = findViewById(R.id.btnaddPayment);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        username = sharedPreferences.getString("name", "");
        useremail = sharedPreferences.getString("email", "");
        token = sharedPreferences.getString("token", "");
        tvEmail.setText(useremail);


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });

        btnaddPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Checkoutpaymentoption.this, Checkoutpaymentmethod.class);
                startActivity(intent);
            }
        });
    }
}