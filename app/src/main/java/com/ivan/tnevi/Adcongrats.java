package com.ivan.tnevi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tnevi.R;

public class Adcongrats extends AppCompatActivity {

    LinearLayout btnFeaturead, btnPreview, btnShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adcongrats);
        btnFeaturead = findViewById(R.id.btnFeaturead);
        btnPreview = findViewById(R.id.btnPreview);
        btnShare = findViewById(R.id.btnShare);


        btnFeaturead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Adcongrats.this, Featuread.class);
                startActivity(intent);

            }
        });


        btnPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Adcongrats.this, Events.class);
                startActivity(intent);
            }
        });


        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "https://bit.ly/3hr6kz8";
                String shareSub = "Your subject here";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSub);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share using"));
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(Adcongrats.this, Allcategory.class);
        startActivity(intent);

    }
}