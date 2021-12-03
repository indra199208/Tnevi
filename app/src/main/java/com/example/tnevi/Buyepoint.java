package com.example.tnevi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.example.tnevi.Adapters.PickerAdapter;

import java.util.ArrayList;
import java.util.List;

public class Buyepoint extends AppCompatActivity {

    Button btnBuynow;
    LinearLayout btnShare;
    ImageView btn_back;
    RecyclerView rv_buyepoints;
    PickerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyepoint);
        btnBuynow = findViewById(R.id.btnBuynow);
        btnShare = findViewById(R.id.btnShare);
        btn_back = findViewById(R.id.btn_back);
        rv_buyepoints = findViewById(R.id.rv_buyepoints);


        adapter = new PickerAdapter(this, getData(100), rv_buyepoints);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(rv_buyepoints);
        rv_buyepoints.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        rv_buyepoints.setAdapter(adapter);




        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Hi……I use the Tnevi app to buy all my event tickets. I receive cash and point using this app. Use my link - https://bit.ly/3hr6kz8 to download and try";
                String shareSub = "";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSub);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share using"));

            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });
    }

    public List<String> getData(int count) {
        List<String> data = new ArrayList<>();
        data.add("2.00");
        data.add("4.11");
        data.add("2.57");
        data.add("3.89");
        data.add("1.22");
        data.add("7.11");
        data.add("3.11");
        data.add("1.71");
        data.add("4.55");
        data.add("5.31");
        data.add("8.71");

        return data;

    }
}
