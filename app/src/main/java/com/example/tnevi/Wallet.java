package com.example.tnevi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.tnevi.Adapters.TicketReportAdapter;
import com.example.tnevi.Adapters.TicketsalesAdapter;
import com.example.tnevi.Allurl.Allurl;
import com.example.tnevi.internet.CheckConnectivity;
import com.example.tnevi.model.TicketReportModel;
import com.example.tnevi.session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Wallet extends AppCompatActivity {

    LinearLayoutCompat btnHome, btnSearch, btnWallet, btnProf, btnSettings, btnEvent;
    ImageView iconWallet, tsArrow, ticketImg, comissionImg, carrow, Imgstar, rewardsImg;
    Button btnViewreport, btnBuynow, btnTicketreport, btnComissionreport;
    LinearLayout btnViewrewards, btnShare, btnTicketsell, btncashoutHistory, btnEditbankinfo;
    private static final String SHARED_PREFS = "sharedPrefs";
    SessionManager sessionManager;
    private static final String TAG = "Myapp";
    String username, useremail, token, msg;
    View rewardsUnderline;
    TextView tvTicketsale, tvCommission, tvEpoints, tvTicketsales, tvAvailablecash, tvComi, tvCommissioncashout, tvep, tvRewards;
    RelativeLayout rl_epoints,rl_commission,rl_ticketsales;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        btnSearch = findViewById(R.id.btnSearch);
        btnWallet = findViewById(R.id.btnWallet);
        btnProf = findViewById(R.id.btnProf);
        btnSettings = findViewById(R.id.btnSettings);
        btnEvent = findViewById(R.id.btnEvent);
        btnHome = findViewById(R.id.btnHome);
        iconWallet = findViewById(R.id.iconWallet);
        btnViewreport = findViewById(R.id.btnViewreport);
        btnBuynow = findViewById(R.id.btnBuynow);
        btnViewrewards = findViewById(R.id.btnViewrewards);
        btnTicketsell = findViewById(R.id.btnTicketsell);
        tvTicketsale = findViewById(R.id.tvTicketsale);
        tvCommission = findViewById(R.id.tvCommission);
        tvEpoints = findViewById(R.id.tvEpoints);
        btnShare = findViewById(R.id.btnShare);
        btncashoutHistory = findViewById(R.id.btncashoutHistory);
        btnTicketreport = findViewById(R.id.btnTicketreport);
        btnComissionreport = findViewById(R.id.btnComissionreport);
        btnEditbankinfo = findViewById(R.id.btnEditbankinfo);


        rl_epoints = findViewById(R.id.rl_epoints);
        rl_commission = findViewById(R.id.rl_commission);
        rl_ticketsales = findViewById(R.id.rl_ticketsales);
        tsArrow = findViewById(R.id.tsArrow);
        ticketImg = findViewById(R.id.ticketImg);
        comissionImg = findViewById(R.id.comissionImg);
        carrow = findViewById(R.id.carrow);
        Imgstar = findViewById(R.id.Imgstar);
        rewardsImg = findViewById(R.id.rewardsImg);
        tvTicketsales = findViewById(R.id.tvTicketsales);
        tvAvailablecash = findViewById(R.id.tvAvailablecash);
        tvComi = findViewById(R.id.tvComi);
        tvCommissioncashout = findViewById(R.id.tvCommissioncashout);
        tvep = findViewById(R.id.tvep);
        tvRewards = findViewById(R.id.tvRewards);
        rewardsUnderline = findViewById(R.id.rewardsUnderline);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            iconWallet.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.colorAccent));
        }

        sessionManager = new SessionManager(getApplicationContext());
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        username = sharedPreferences.getString("name", "");
        useremail = sharedPreferences.getString("email", "");
        token = sharedPreferences.getString("token", "");


        getAccount();

        onClick();


    }

    public void onClick() {

        btnEditbankinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Wallet.this, Checkoutbankinfo.class);
                startActivity(intent);
            }
        });

        btnComissionreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(Wallet.this, Commissionsearned.class);
                startActivity(intent);

            }
        });

        btnTicketreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Wallet.this, Ticketsalesreport.class);
                startActivity(intent);

            }
        });



        rl_ticketsales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                rl_commission.setBackgroundResource(R.drawable.bg3);
                tvCommission.setTextColor(ContextCompat.getColor(Wallet.this, R.color.quantum_black_100));
                tvComi.setTextColor(ContextCompat.getColor(Wallet.this, R.color.quantum_black_100));
                comissionImg.setColorFilter(getApplication().getResources().getColor(R.color.colorAccent));
                tvCommissioncashout.setTextColor(ContextCompat.getColor(Wallet.this, R.color.quantum_black_100));
                carrow.setColorFilter(getApplication().getResources().getColor(R.color.colorAccent));

                rl_ticketsales.setBackgroundResource(R.drawable.bg1);
                tvTicketsale.setTextColor(ContextCompat.getColor(Wallet.this, R.color.quantum_white_100));
                ticketImg.setColorFilter(getApplication().getResources().getColor(R.color.quantum_yellow));
                tvTicketsales.setTextColor(ContextCompat.getColor(Wallet.this, R.color.quantum_white_100));
                tvAvailablecash.setTextColor(ContextCompat.getColor(Wallet.this, R.color.quantum_white_100));
                tsArrow.setColorFilter(getApplication().getResources().getColor(R.color.quantum_white_100));


                rl_epoints.setBackgroundResource(R.drawable.bg3);
                tvEpoints.setTextColor(ContextCompat.getColor(Wallet.this, R.color.quantum_black_100));
                Imgstar.setColorFilter(getApplication().getResources().getColor(R.color.colorAccent));
                tvep.setTextColor(ContextCompat.getColor(Wallet.this, R.color.quantum_black_100));
                rewardsImg.setColorFilter(getApplication().getResources().getColor(R.color.colorAccent));
                tvRewards.setTextColor(ContextCompat.getColor(Wallet.this, R.color.colorAccent));

                Intent intent = new Intent(Wallet.this, Ticketsales.class);
                startActivity(intent);

            }
        });


        rl_commission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                rl_commission.setBackgroundResource(R.drawable.bg1);
                tvCommission.setTextColor(ContextCompat.getColor(Wallet.this, R.color.quantum_white_100));
                tvComi.setTextColor(ContextCompat.getColor(Wallet.this, R.color.quantum_white_100));
                comissionImg.setColorFilter(getApplication().getResources().getColor(R.color.quantum_yellow));
                tvCommissioncashout.setTextColor(ContextCompat.getColor(Wallet.this, R.color.quantum_white_100));
                carrow.setColorFilter(getApplication().getResources().getColor(R.color.quantum_white_100));

                rl_ticketsales.setBackgroundResource(R.drawable.bg3);
                tvTicketsale.setTextColor(ContextCompat.getColor(Wallet.this, R.color.quantum_black_100));
                ticketImg.setColorFilter(getApplication().getResources().getColor(R.color.colorAccent));
                tvTicketsales.setTextColor(ContextCompat.getColor(Wallet.this, R.color.quantum_black_100));
                tvAvailablecash.setTextColor(ContextCompat.getColor(Wallet.this, R.color.quantum_black_100));
                tsArrow.setColorFilter(getApplication().getResources().getColor(R.color.colorAccent));

                rl_epoints.setBackgroundResource(R.drawable.bg3);
                tvEpoints.setTextColor(ContextCompat.getColor(Wallet.this, R.color.quantum_black_100));
                Imgstar.setColorFilter(getApplication().getResources().getColor(R.color.colorAccent));
                tvep.setTextColor(ContextCompat.getColor(Wallet.this, R.color.quantum_black_100));
                rewardsImg.setColorFilter(getApplication().getResources().getColor(R.color.colorAccent));
                tvRewards.setTextColor(ContextCompat.getColor(Wallet.this, R.color.colorAccent));

                Intent intent = new Intent(Wallet.this, Comissionsales.class);
                startActivity(intent);

            }
        });

        rl_epoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                rl_ticketsales.setBackgroundResource(R.drawable.bg3);
                tvTicketsale.setTextColor(ContextCompat.getColor(Wallet.this, R.color.quantum_black_100));
                ticketImg.setColorFilter(getApplication().getResources().getColor(R.color.colorAccent));
                tvTicketsales.setTextColor(ContextCompat.getColor(Wallet.this, R.color.quantum_black_100));
                tvAvailablecash.setTextColor(ContextCompat.getColor(Wallet.this, R.color.quantum_black_100));
                tsArrow.setColorFilter(getApplication().getResources().getColor(R.color.colorAccent));


                rl_commission.setBackgroundResource(R.drawable.bg3);
                tvCommission.setTextColor(ContextCompat.getColor(Wallet.this, R.color.quantum_black_100));
                tvComi.setTextColor(ContextCompat.getColor(Wallet.this, R.color.quantum_black_100));
                comissionImg.setColorFilter(getApplication().getResources().getColor(R.color.colorAccent));
                tvCommissioncashout.setTextColor(ContextCompat.getColor(Wallet.this, R.color.quantum_black_100));
                carrow.setColorFilter(getApplication().getResources().getColor(R.color.colorAccent));

                rl_epoints.setBackgroundResource(R.drawable.bg1);
                tvEpoints.setTextColor(ContextCompat.getColor(Wallet.this, R.color.quantum_white_100));
                Imgstar.setColorFilter(getApplication().getResources().getColor(R.color.quantum_yellow));
                tvep.setTextColor(ContextCompat.getColor(Wallet.this, R.color.quantum_white_100));
                rewardsImg.setColorFilter(getApplication().getResources().getColor(R.color.quantum_white_100));
                tvRewards.setTextColor(ContextCompat.getColor(Wallet.this, R.color.quantum_white_100));




            }
        });


        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Wallet.this, MainActivity.class);
                startActivity(intent);

            }
        });


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Wallet.this, Search.class);
                startActivity(intent);
            }
        });


        btnProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Wallet.this, Profile.class);
                startActivity(intent);

            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Wallet.this, Settings.class);
                startActivity(intent);


            }
        });

        btnEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(Wallet.this, Events.class);
                startActivity(intent);

            }
        });


        btnViewreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(Wallet.this, Epointhistory.class);
                startActivity(intent);


            }
        });


        btnBuynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Wallet.this, Buyepoint.class);
                startActivity(intent);

            }
        });


        btnViewrewards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Wallet.this, Viewrewards.class);
                startActivity(intent);
            }
        });

        btnTicketsell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        btncashoutHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Hi! I use the Tnevi app to buy all my event tickets.I receive cash and point using this app. Use my link to download and try";
                String shareSub = "";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSub);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share using"));
            }
        });

    }











    public void getAccount() {

        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {

            showProgressDialog();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, Allurl.GetAccount,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.i("Response3-->", String.valueOf(response));

                            //Parse Here

                            try {
                                JSONObject result = new JSONObject(String.valueOf(response));
                                msg = result.getString("message");
                                Log.d(TAG, "msg-->" + msg);
                                String stat = result.getString("stat");
                                if (stat.equals("succ")) {

//                                    Toast.makeText(Settings.this, msg, Toast.LENGTH_SHORT).show();
                                    JSONObject userdeatisObj = result.getJSONObject("data");
                                    JSONObject walletObj = userdeatisObj.getJSONObject("wallet");
                                    String ticketsell = walletObj.getString("ticket_sell");
                                    String commission = walletObj.getString("commission");
                                    String epoints = walletObj.getString("epoints");
                                    tvTicketsale.setText("$" + ticketsell);
                                    tvCommission.setText("$" + commission);
                                    tvEpoints.setText("$" + epoints);

                                } else {

                                    hideProgressDialog();
                                    Log.d(TAG, "unsuccessfull - " + "Error");
                                    Toast.makeText(Wallet.this, msg, Toast.LENGTH_SHORT).show();
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            hideProgressDialog();


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            hideProgressDialog();
                            Toast.makeText(Wallet.this, error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", token);
                    return params;
                }

            };

            Volley.newRequestQueue(this).add(stringRequest);

        } else {

            Toast.makeText(getApplicationContext(), "OOPS! No Internet Connection", Toast.LENGTH_SHORT).show();

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