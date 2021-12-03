package com.example.tnevi;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tnevi.Adapters.CategoryAdapter;
import com.example.tnevi.Adapters.CategoryAdapter2;
import com.example.tnevi.Allurl.Allurl;
import com.example.tnevi.Utils.ItemOffsetDecoration;
import com.example.tnevi.internet.CheckConnectivity;
import com.example.tnevi.model.CategoryModel;
import com.example.tnevi.session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Allcategory extends AppCompatActivity {


    LinearLayout btnAddevent, btnShowdetails, ll_buttons;
    ImageView btn_back;
    private static final String SHARED_PREFS = "sharedPrefs";
    SessionManager sessionManager;
    private static final String TAG = "Myapp";
    String username, useremail, token, msg;
    LinearLayoutCompat btnReligious, btnFreeticket, btnConcert, btnClub, btnSports, btnParty, btnKids, btnCarnival, btnTheatre, btnTrade,
            btnFestival, btnConf, btnBanquet, btnRetreat, btnSocial, btnAward, btnTravel;

    LinearLayout checkReligious, checkfreeEvent, checkConcert, checkClub, checkSport, checkParty, checkKids, checkCarnival, checkThearter,
            checkTrade, checkFestival, checkConf, checkBanquet, checkRetreat, checkSocial, checkAwards, checkTravel;

    String category = "";
    RecyclerView rv_category;
    private ArrayList<CategoryModel> categoryModelArrayList;
    private CategoryAdapter2 categoryAdapter2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allcategory);
//        btnAddevent = findViewById(R.id.btnAddevent);
        btn_back =findViewById(R.id.btn_back);
        rv_category = findViewById(R.id.rv_category);

//        btnReligious = findViewById(R.id.btnReligious);
//        btnFreeticket = findViewById(R.id.btnFreeticket);
//        btnConcert = findViewById(R.id.btnConcert);
//        btnClub = findViewById(R.id.btnClub);
//        btnSports = findViewById(R.id.btnSports);
//        btnParty = findViewById(R.id.btnParty);
//        btnKids = findViewById(R.id.btnKids);
//        btnTheatre = findViewById(R.id.btnTheatre);
//        btnCarnival = findViewById(R.id.btnCarnival);
//        btnTrade = findViewById(R.id.btnTrade);
//        btnFestival = findViewById(R.id.btnFestival);
//        btnConf = findViewById(R.id.btnConf);
//        btnBanquet = findViewById(R.id.btnBanquet);
//        btnRetreat = findViewById(R.id.btnRetreat);
//        btnSocial = findViewById(R.id.btnSocial);
//        btnAward = findViewById(R.id.btnAward);
//        btnTravel = findViewById(R.id.btnTravel);
//        ll_buttons = findViewById(R.id.ll_buttons);
//
//
//        checkReligious = findViewById(R.id.checkReligious);
//        checkfreeEvent = findViewById(R.id.checkfreeEvent);
//        checkConcert = findViewById(R.id.checkConcert);
//        checkClub = findViewById(R.id.checkClub);
//        checkSport = findViewById(R.id.checkSport);
//        checkParty = findViewById(R.id.checkParty);
//        checkKids = findViewById(R.id.checkKids);
//        checkCarnival = findViewById(R.id.checkCarnival);
//        checkThearter = findViewById(R.id.checkThearter);
//        checkTrade = findViewById(R.id.checkTrade);
//        checkFestival = findViewById(R.id.checkFestival);
//        checkConf = findViewById(R.id.checkConf);
//        checkBanquet = findViewById(R.id.checkBanquet);
//        checkRetreat = findViewById(R.id.checkRetreat);
//        checkSocial = findViewById(R.id.checkSocial);
//        checkAwards = findViewById(R.id.checkAwards);
//        checkTravel = findViewById(R.id.checkTravel);





        sessionManager = new SessionManager(getApplicationContext());
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        username = sharedPreferences.getString("name", "");
        useremail = sharedPreferences.getString("email", "");
        token = sharedPreferences.getString("token", "");


        getAllcategories();
        onClick();
    }

    public void onClick(){





        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });


//        btnAddevent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (category.length()==0){
//
//                    Toast.makeText(Allcategory.this, "Please select any category", Toast.LENGTH_SHORT).show();
//                }else {
//
//                    Intent intent = new Intent(Allcategory.this, Addeventdetails.class);
//                    intent.putExtra("category", category);
//                    startActivity(intent);
//
//                }
//
//
//            }
//        });


//        btnReligious.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
////                checkReligious.setVisibility(View.VISIBLE);
////                checkfreeEvent.setVisibility(View.GONE);
////                checkConcert.setVisibility(View.GONE);
////                checkClub.setVisibility(View.GONE);
////                checkSport.setVisibility(View.GONE);
////                checkParty.setVisibility(View.GONE);
////                checkKids.setVisibility(View.GONE);
////                checkCarnival.setVisibility(View.GONE);
////                checkThearter.setVisibility(View.GONE);
////                checkTrade.setVisibility(View.GONE);
////                checkFestival.setVisibility(View.GONE);
////                checkConf.setVisibility(View.GONE);
////                checkBanquet.setVisibility(View.GONE);
////                checkRetreat.setVisibility(View.GONE);
////                checkSocial.setVisibility(View.GONE);
////                checkAwards.setVisibility(View.GONE);
////                checkTravel.setVisibility(View.GONE);
//                category = "Religious";
////                ll_buttons.setVisibility(View.VISIBLE);
//                Intent intent = new Intent(Allcategory.this, Addeventdetails.class);
//                intent.putExtra("category", category);
//                startActivity(intent);
//
//
//
//            }
//        });
//
//        btnFreeticket.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
////                checkReligious.setVisibility(View.GONE);
////                checkfreeEvent.setVisibility(View.VISIBLE);
////                checkConcert.setVisibility(View.GONE);
////                checkClub.setVisibility(View.GONE);
////                checkSport.setVisibility(View.GONE);
////                checkParty.setVisibility(View.GONE);
////                checkKids.setVisibility(View.GONE);
////                checkCarnival.setVisibility(View.GONE);
////                checkThearter.setVisibility(View.GONE);
////                checkTrade.setVisibility(View.GONE);
////                checkFestival.setVisibility(View.GONE);
////                checkConf.setVisibility(View.GONE);
////                checkBanquet.setVisibility(View.GONE);
////                checkRetreat.setVisibility(View.GONE);
////                checkSocial.setVisibility(View.GONE);
////                checkAwards.setVisibility(View.GONE);
////                checkTravel.setVisibility(View.GONE);
//                category = "Freeticket";
////                ll_buttons.setVisibility(View.VISIBLE);
//                Intent intent = new Intent(Allcategory.this, Addeventdetails.class);
//                intent.putExtra("category", category);
//                startActivity(intent);
//
//
//
//            }
//        });
//
//        btnConcert.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
////                checkReligious.setVisibility(View.GONE);
////                checkfreeEvent.setVisibility(View.GONE);
////                checkConcert.setVisibility(View.VISIBLE);
////                checkClub.setVisibility(View.GONE);
////                checkSport.setVisibility(View.GONE);
////                checkParty.setVisibility(View.GONE);
////                checkKids.setVisibility(View.GONE);
////                checkCarnival.setVisibility(View.GONE);
////                checkThearter.setVisibility(View.GONE);
////                checkTrade.setVisibility(View.GONE);
////                checkFestival.setVisibility(View.GONE);
////                checkConf.setVisibility(View.GONE);
////                checkBanquet.setVisibility(View.GONE);
////                checkRetreat.setVisibility(View.GONE);
////                checkSocial.setVisibility(View.GONE);
////                checkAwards.setVisibility(View.GONE);
////                checkTravel.setVisibility(View.GONE);
//                category = "Concert";
////                ll_buttons.setVisibility(View.VISIBLE);
//                Intent intent = new Intent(Allcategory.this, Addeventdetails.class);
//                intent.putExtra("category", category);
//                startActivity(intent);
//
//
//
//            }
//        });
//
//        btnClub.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
////                checkReligious.setVisibility(View.GONE);
////                checkfreeEvent.setVisibility(View.GONE);
////                checkConcert.setVisibility(View.GONE);
////                checkClub.setVisibility(View.VISIBLE);
////                checkSport.setVisibility(View.GONE);
////                checkParty.setVisibility(View.GONE);
////                checkKids.setVisibility(View.GONE);
////                checkCarnival.setVisibility(View.GONE);
////                checkThearter.setVisibility(View.GONE);
////                checkTrade.setVisibility(View.GONE);
////                checkFestival.setVisibility(View.GONE);
////                checkConf.setVisibility(View.GONE);
////                checkBanquet.setVisibility(View.GONE);
////                checkRetreat.setVisibility(View.GONE);
////                checkSocial.setVisibility(View.GONE);
////                checkAwards.setVisibility(View.GONE);
////                checkTravel.setVisibility(View.GONE);
//                category = "Club";
////                ll_buttons.setVisibility(View.VISIBLE);
//                Intent intent = new Intent(Allcategory.this, Addeventdetails.class);
//                intent.putExtra("category", category);
//                startActivity(intent);
//
//
//
//            }
//        });
//
//        btnSports.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
////                checkReligious.setVisibility(View.GONE);
////                checkfreeEvent.setVisibility(View.GONE);
////                checkConcert.setVisibility(View.GONE);
////                checkClub.setVisibility(View.GONE);
////                checkSport.setVisibility(View.VISIBLE);
////                checkParty.setVisibility(View.GONE);
////                checkKids.setVisibility(View.GONE);
////                checkCarnival.setVisibility(View.GONE);
////                checkThearter.setVisibility(View.GONE);
////                checkTrade.setVisibility(View.GONE);
////                checkFestival.setVisibility(View.GONE);
////                checkConf.setVisibility(View.GONE);
////                checkBanquet.setVisibility(View.GONE);
////                checkRetreat.setVisibility(View.GONE);
////                checkSocial.setVisibility(View.GONE);
////                checkAwards.setVisibility(View.GONE);
////                checkTravel.setVisibility(View.GONE);
//                category = "Sports";
////                ll_buttons.setVisibility(View.VISIBLE);
//                Intent intent = new Intent(Allcategory.this, Addeventdetails.class);
//                intent.putExtra("category", category);
//                startActivity(intent);
//
//
//            }
//        });
//
//        btnParty.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
////                checkReligious.setVisibility(View.GONE);
////                checkfreeEvent.setVisibility(View.GONE);
////                checkConcert.setVisibility(View.GONE);
////                checkClub.setVisibility(View.GONE);
////                checkSport.setVisibility(View.GONE);
////                checkParty.setVisibility(View.VISIBLE);
////                checkKids.setVisibility(View.GONE);
////                checkCarnival.setVisibility(View.GONE);
////                checkThearter.setVisibility(View.GONE);
////                checkTrade.setVisibility(View.GONE);
////                checkFestival.setVisibility(View.GONE);
////                checkConf.setVisibility(View.GONE);
////                checkBanquet.setVisibility(View.GONE);
////                checkRetreat.setVisibility(View.GONE);
////                checkSocial.setVisibility(View.GONE);
////                checkAwards.setVisibility(View.GONE);
////                checkTravel.setVisibility(View.GONE);
//                category = "Party";
////                ll_buttons.setVisibility(View.VISIBLE);
//                Intent intent = new Intent(Allcategory.this, Addeventdetails.class);
//                intent.putExtra("category", category);
//                startActivity(intent);
//
//
//            }
//        });
//
//        btnKids.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
////                checkReligious.setVisibility(View.GONE);
////                checkfreeEvent.setVisibility(View.GONE);
////                checkConcert.setVisibility(View.GONE);
////                checkClub.setVisibility(View.GONE);
////                checkSport.setVisibility(View.GONE);
////                checkParty.setVisibility(View.GONE);
////                checkKids.setVisibility(View.VISIBLE);
////                checkCarnival.setVisibility(View.GONE);
////                checkThearter.setVisibility(View.GONE);
////                checkTrade.setVisibility(View.GONE);
////                checkFestival.setVisibility(View.GONE);
////                checkConf.setVisibility(View.GONE);
////                checkBanquet.setVisibility(View.GONE);
////                checkRetreat.setVisibility(View.GONE);
////                checkSocial.setVisibility(View.GONE);
////                checkAwards.setVisibility(View.GONE);
////                checkTravel.setVisibility(View.GONE);
//                category = "Kids";
//                Intent intent = new Intent(Allcategory.this, Addeventdetails.class);
//                intent.putExtra("category", category);
//                startActivity(intent);
////                ll_buttons.setVisibility(View.VISIBLE);
//
//
//            }
//        });
//
//
//        btnTheatre.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
////                checkReligious.setVisibility(View.GONE);
////                checkfreeEvent.setVisibility(View.GONE);
////                checkConcert.setVisibility(View.GONE);
////                checkClub.setVisibility(View.GONE);
////                checkSport.setVisibility(View.GONE);
////                checkParty.setVisibility(View.GONE);
////                checkKids.setVisibility(View.GONE);
////                checkThearter.setVisibility(View.VISIBLE);
////                checkCarnival.setVisibility(View.GONE);
////                checkTrade.setVisibility(View.GONE);
////                checkFestival.setVisibility(View.GONE);
////                checkConf.setVisibility(View.GONE);
////                checkBanquet.setVisibility(View.GONE);
////                checkRetreat.setVisibility(View.GONE);
////                checkSocial.setVisibility(View.GONE);
////                checkAwards.setVisibility(View.GONE);
////                checkTravel.setVisibility(View.GONE);
//                category = "Theatre";
////                ll_buttons.setVisibility(View.VISIBLE);
//                Intent intent = new Intent(Allcategory.this, Addeventdetails.class);
//                intent.putExtra("category", category);
//                startActivity(intent);
//
//
//            }
//        });
//
//
//        btnCarnival.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
////                checkReligious.setVisibility(View.GONE);
////                checkfreeEvent.setVisibility(View.GONE);
////                checkConcert.setVisibility(View.GONE);
////                checkClub.setVisibility(View.GONE);
////                checkSport.setVisibility(View.GONE);
////                checkParty.setVisibility(View.GONE);
////                checkKids.setVisibility(View.GONE);
////                checkThearter.setVisibility(View.GONE);
////                checkCarnival.setVisibility(View.VISIBLE);
////                checkTrade.setVisibility(View.GONE);
////                checkFestival.setVisibility(View.GONE);
////                checkConf.setVisibility(View.GONE);
////                checkBanquet.setVisibility(View.GONE);
////                checkRetreat.setVisibility(View.GONE);
////                checkSocial.setVisibility(View.GONE);
////                checkAwards.setVisibility(View.GONE);
////                checkTravel.setVisibility(View.GONE);
//                category = "Carnival";
////                ll_buttons.setVisibility(View.VISIBLE);
//                Intent intent = new Intent(Allcategory.this, Addeventdetails.class);
//                intent.putExtra("category", category);
//                startActivity(intent);
//
//
//            }
//        });
//
//        btnTrade.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
////                checkReligious.setVisibility(View.GONE);
////                checkfreeEvent.setVisibility(View.GONE);
////                checkConcert.setVisibility(View.GONE);
////                checkClub.setVisibility(View.GONE);
////                checkSport.setVisibility(View.GONE);
////                checkParty.setVisibility(View.GONE);
////                checkKids.setVisibility(View.GONE);
////                checkThearter.setVisibility(View.GONE);
////                checkCarnival.setVisibility(View.GONE);
////                checkTrade.setVisibility(View.VISIBLE);
////                checkFestival.setVisibility(View.GONE);
////                checkConf.setVisibility(View.GONE);
////                checkBanquet.setVisibility(View.GONE);
////                checkRetreat.setVisibility(View.GONE);
////                checkSocial.setVisibility(View.GONE);
////                checkAwards.setVisibility(View.GONE);
////                checkTravel.setVisibility(View.GONE);
//                category = "Trade Show";
////                ll_buttons.setVisibility(View.VISIBLE);
//                Intent intent = new Intent(Allcategory.this, Addeventdetails.class);
//                intent.putExtra("category", category);
//                startActivity(intent);
//
//
//            }
//        });
//
//
//        btnFestival.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
////                checkReligious.setVisibility(View.GONE);
////                checkfreeEvent.setVisibility(View.GONE);
////                checkConcert.setVisibility(View.GONE);
////                checkClub.setVisibility(View.GONE);
////                checkSport.setVisibility(View.GONE);
////                checkParty.setVisibility(View.GONE);
////                checkKids.setVisibility(View.GONE);
////                checkThearter.setVisibility(View.GONE);
////                checkCarnival.setVisibility(View.GONE);
////                checkTrade.setVisibility(View.GONE);
////                checkFestival.setVisibility(View.VISIBLE);
////                checkConf.setVisibility(View.GONE);
////                checkBanquet.setVisibility(View.GONE);
////                checkRetreat.setVisibility(View.GONE);
////                checkSocial.setVisibility(View.GONE);
////                checkAwards.setVisibility(View.GONE);
////                checkTravel.setVisibility(View.GONE);
//                category = "Festival";
//                Intent intent = new Intent(Allcategory.this, Addeventdetails.class);
//                intent.putExtra("category", category);
//                startActivity(intent);
////                ll_buttons.setVisibility(View.VISIBLE);
//
//
//
//            }
//        });
//
//
//
//        btnConf.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
////                checkReligious.setVisibility(View.GONE);
////                checkfreeEvent.setVisibility(View.GONE);
////                checkConcert.setVisibility(View.GONE);
////                checkClub.setVisibility(View.GONE);
////                checkSport.setVisibility(View.GONE);
////                checkParty.setVisibility(View.GONE);
////                checkKids.setVisibility(View.GONE);
////                checkThearter.setVisibility(View.GONE);
////                checkCarnival.setVisibility(View.GONE);
////                checkTrade.setVisibility(View.GONE);
////                checkFestival.setVisibility(View.GONE);
////                checkConf.setVisibility(View.VISIBLE);
////                checkBanquet.setVisibility(View.GONE);
////                checkRetreat.setVisibility(View.GONE);
////                checkSocial.setVisibility(View.GONE);
////                checkAwards.setVisibility(View.GONE);
////                checkTravel.setVisibility(View.GONE);
//                category = "Conference";
//                Intent intent = new Intent(Allcategory.this, Addeventdetails.class);
//                intent.putExtra("category", category);
//                startActivity(intent);
////                ll_buttons.setVisibility(View.VISIBLE);
//
//
//            }
//        });
//
//
//        btnBanquet.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
////                checkReligious.setVisibility(View.GONE);
////                checkfreeEvent.setVisibility(View.GONE);
////                checkConcert.setVisibility(View.GONE);
////                checkClub.setVisibility(View.GONE);
////                checkSport.setVisibility(View.GONE);
////                checkParty.setVisibility(View.GONE);
////                checkKids.setVisibility(View.GONE);
////                checkThearter.setVisibility(View.GONE);
////                checkCarnival.setVisibility(View.GONE);
////                checkTrade.setVisibility(View.GONE);
////                checkFestival.setVisibility(View.GONE);
////                checkConf.setVisibility(View.GONE);
////                checkBanquet.setVisibility(View.VISIBLE);
////                checkRetreat.setVisibility(View.GONE);
////                checkSocial.setVisibility(View.GONE);
////                checkAwards.setVisibility(View.GONE);
////                checkTravel.setVisibility(View.GONE);
//                category = "Banquet";
//                Intent intent = new Intent(Allcategory.this, Addeventdetails.class);
//                intent.putExtra("category", category);
//                startActivity(intent);
////                ll_buttons.setVisibility(View.VISIBLE);
//
//
//
//            }
//        });
//
//
//        btnRetreat.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
////                checkReligious.setVisibility(View.GONE);
////                checkfreeEvent.setVisibility(View.GONE);
////                checkConcert.setVisibility(View.GONE);
////                checkClub.setVisibility(View.GONE);
////                checkSport.setVisibility(View.GONE);
////                checkParty.setVisibility(View.GONE);
////                checkKids.setVisibility(View.GONE);
////                checkThearter.setVisibility(View.GONE);
////                checkCarnival.setVisibility(View.GONE);
////                checkTrade.setVisibility(View.GONE);
////                checkFestival.setVisibility(View.GONE);
////                checkConf.setVisibility(View.GONE);
////                checkBanquet.setVisibility(View.GONE);
////                checkRetreat.setVisibility(View.VISIBLE);
////                checkSocial.setVisibility(View.GONE);
////                checkAwards.setVisibility(View.GONE);
////                checkTravel.setVisibility(View.GONE);
//                category = "Retreat";
////                ll_buttons.setVisibility(View.VISIBLE);
//                Intent intent = new Intent(Allcategory.this, Addeventdetails.class);
//                intent.putExtra("category", category);
//                startActivity(intent);
//
//
//            }
//        });
//
//
//        btnSocial.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
////                checkReligious.setVisibility(View.GONE);
////                checkfreeEvent.setVisibility(View.GONE);
////                checkConcert.setVisibility(View.GONE);
////                checkClub.setVisibility(View.GONE);
////                checkSport.setVisibility(View.GONE);
////                checkParty.setVisibility(View.GONE);
////                checkKids.setVisibility(View.GONE);
////                checkThearter.setVisibility(View.GONE);
////                checkCarnival.setVisibility(View.GONE);
////                checkTrade.setVisibility(View.GONE);
////                checkFestival.setVisibility(View.GONE);
////                checkConf.setVisibility(View.GONE);
////                checkBanquet.setVisibility(View.GONE);
////                checkRetreat.setVisibility(View.GONE);
////                checkSocial.setVisibility(View.VISIBLE);
////                checkAwards.setVisibility(View.GONE);
////                checkTravel.setVisibility(View.GONE);
//                category = "Social";
//                Intent intent = new Intent(Allcategory.this, Addeventdetails.class);
//                intent.putExtra("category", category);
//                startActivity(intent);
////                ll_buttons.setVisibility(View.VISIBLE);
//
//
//            }
//        });
//
//
//        btnAward.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
////                checkReligious.setVisibility(View.GONE);
////                checkfreeEvent.setVisibility(View.GONE);
////                checkConcert.setVisibility(View.GONE);
////                checkClub.setVisibility(View.GONE);
////                checkSport.setVisibility(View.GONE);
////                checkParty.setVisibility(View.GONE);
////                checkKids.setVisibility(View.GONE);
////                checkThearter.setVisibility(View.GONE);
////                checkCarnival.setVisibility(View.GONE);
////                checkTrade.setVisibility(View.GONE);
////                checkFestival.setVisibility(View.GONE);
////                checkConf.setVisibility(View.GONE);
////                checkBanquet.setVisibility(View.GONE);
////                checkRetreat.setVisibility(View.GONE);
////                checkSocial.setVisibility(View.GONE);
////                checkAwards.setVisibility(View.VISIBLE);
////                checkTravel.setVisibility(View.GONE);
//                category = "Award";
//                Intent intent = new Intent(Allcategory.this, Addeventdetails.class);
//                intent.putExtra("category", category);
//                startActivity(intent);
////                ll_buttons.setVisibility(View.VISIBLE);
//
//
//            }
//        });
//
//        btnTravel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
////                checkReligious.setVisibility(View.GONE);
////                checkfreeEvent.setVisibility(View.GONE);
////                checkConcert.setVisibility(View.GONE);
////                checkClub.setVisibility(View.GONE);
////                checkSport.setVisibility(View.GONE);
////                checkParty.setVisibility(View.GONE);
////                checkKids.setVisibility(View.GONE);
////                checkThearter.setVisibility(View.GONE);
////                checkCarnival.setVisibility(View.GONE);
////                checkTrade.setVisibility(View.GONE);
////                checkFestival.setVisibility(View.GONE);
////                checkConf.setVisibility(View.GONE);
////                checkBanquet.setVisibility(View.GONE);
////                checkRetreat.setVisibility(View.GONE);
////                checkSocial.setVisibility(View.GONE);
////                checkAwards.setVisibility(View.GONE);
////                checkTravel.setVisibility(View.VISIBLE);
//                category = "Travel";
//                Intent intent = new Intent(Allcategory.this, Addeventdetails.class);
//                intent.putExtra("category", category);
//                startActivity(intent);
////                ll_buttons.setVisibility(View.VISIBLE);
//
//
//            }
//        });


    }




//    public void getAccount() {
//
//        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {
//
//            showProgressDialog();
//
//            StringRequest stringRequest = new StringRequest(Request.Method.POST, Allurl.GetAccount,
//                    new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//
//                            Log.i("Response3-->", String.valueOf(response));
//
//                            //Parse Here
//
//                            try {
//                                JSONObject result = new JSONObject(String.valueOf(response));
//                                msg = result.getString("message");
//                                Log.d(TAG, "msg-->" + msg);
//                                String stat = result.getString("stat");
//                                if (stat.equals("succ")) {
//
////                                    Toast.makeText(Settings.this, msg, Toast.LENGTH_SHORT).show();
//                                    JSONObject userdeatisObj = result.getJSONObject("data");
//                                    JSONObject walletObj = userdeatisObj.getJSONObject("wallet");
//                                    String ticketsell = walletObj.getString("ticket_sell");
//                                    String epoints = walletObj.getString("epoints");
//                                    tvBalance.setText("€"+ticketsell);
//                                    tvEponts.setText(epoints);
//
//
//                                } else {
//
//                                    hideProgressDialog();
//                                    Log.d(TAG, "unsuccessfull - " + "Error");
//                                    Toast.makeText(Allcategory.this, msg, Toast.LENGTH_SHORT).show();
//                                }
//
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
//
//                            hideProgressDialog();
//
//
//                        }
//                    },
//                    new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//
//                            hideProgressDialog();
//                            Toast.makeText(Allcategory.this, error.toString(), Toast.LENGTH_SHORT).show();
//                        }
//                    }) {
//
//                @Override
//                public Map<String, String> getHeaders() throws AuthFailureError {
//                    Map<String, String> params = new HashMap<String, String>();
//                    params.put("Authorization", token);
//                    return params;
//                }
//
//            };
//
//            Volley.newRequestQueue(this).add(stringRequest);
//
//        } else {
//
//            Toast.makeText(getApplicationContext(), "OOPS! No Internet Connection", Toast.LENGTH_SHORT).show();
//
//        }
//
//
//    }


    public void getAllcategories(){

        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {


            showProgressDialog();

            JSONObject params = new JSONObject();

            try {
                params.put("page_no", "1");

            } catch (JSONException e) {
                e.printStackTrace();
            }


            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Allurl.AllCategory, params, response -> {

                Log.i("Response-->", String.valueOf(response));

                try {
                    JSONObject result = new JSONObject(String.valueOf(response));
                    String msg = result.getString("message");
                    Log.d(TAG, "msg-->" + msg);
                    String stat = result.getString("stat");
                    if (stat.equals("succ")) {

                        categoryModelArrayList = new ArrayList<>();
                        JSONArray response_data = result.getJSONArray("data");
                        for (int i = 0; i < response_data.length(); i++) {

                            CategoryModel categoryModel = new CategoryModel();
                            JSONObject responseobj = response_data.getJSONObject(i);
                            categoryModel.setId(responseobj.getString("id"));
                            categoryModel.setCategoryname(responseobj.getString("category_name"));
                            categoryModel.setDescription(responseobj.getString("category_description"));
                            categoryModel.setPic(responseobj.getString("category_pic"));
                            categoryModelArrayList.add(categoryModel);

                        }

                        setupRecycler();

                    } else {

                        Log.d(TAG, "unsuccessfull - " + "Error");
                        Toast.makeText(Allcategory.this, "invalid", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideProgressDialog();

                //TODO: handle success
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Allcategory.this, error.toString(), Toast.LENGTH_SHORT).show();

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

    private void setupRecycler() {

        categoryAdapter2 = new CategoryAdapter2(this, categoryModelArrayList);
        rv_category.setAdapter(categoryAdapter2);
        rv_category.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getApplicationContext(), R.dimen.photos_list_spacing);
        rv_category.addItemDecoration(itemDecoration);


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