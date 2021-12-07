package com.ivan.tnevi;

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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ivan.tnevi.Allurl.Allurl;
import com.example.tnevi.R;
import com.ivan.tnevi.internet.CheckConnectivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Viewrewards extends AppCompatActivity {

    ImageView btn_back, ticketMinus, ticketExpand, friendsinviteMinus, friendsinviteExpand, appDownloadMinus, appdownloadExpand;
    LinearLayout ll_ticketdetails, ll_TicketLine, ll_inviteLine, ll_invite, btnShare, ll_appdownload, ll_appdownloadLine;
    TextView tvTicketvalue, tvBtnApp, tvBtnFriend, tvBtnTicket, tvBtnApp2, tvBtnFriend2, tvBtnTicket2;
    String token;
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String TAG = "myapp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewrewards);
        btn_back = findViewById(R.id.btn_back);
        ticketMinus = findViewById(R.id.ticketMinus);
        ticketExpand = findViewById(R.id.ticketExpand);
        ll_ticketdetails = findViewById(R.id.ll_ticketdetails);
        ll_TicketLine = findViewById(R.id.ll_TicketLine);
        friendsinviteMinus = findViewById(R.id.friendsinviteMinus);
        friendsinviteExpand = findViewById(R.id.friendsinviteExpand);
        ll_inviteLine = findViewById(R.id.ll_inviteLine);
        ll_invite = findViewById(R.id.ll_invite);
        appDownloadMinus = findViewById(R.id.appDownloadMinus);
        appdownloadExpand = findViewById(R.id.appdownloadExpand);
        ll_appdownload = findViewById(R.id.ll_appdownload);
        ll_appdownloadLine = findViewById(R.id.ll_appdownloadLine);
        tvBtnFriend = findViewById(R.id.tvBtnFriend);
        tvBtnApp = findViewById(R.id.tvBtnApp);
        tvBtnTicket = findViewById(R.id.tvBtnTicket);
        tvBtnApp2 = findViewById(R.id.tvBtnApp2);
        tvBtnFriend2 = findViewById(R.id.tvBtnFriend2);
        tvBtnTicket2 = findViewById(R.id.tvBtnTicket2);



        btnShare = findViewById(R.id.btnShare);
        tvTicketvalue = findViewById(R.id.tvTicketvalue);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
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

        ticketMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ticketMinus.setVisibility(View.GONE);
                ticketExpand.setVisibility(View.VISIBLE);
                ll_ticketdetails.setVisibility(View.GONE);
                ll_TicketLine.setVisibility(View.GONE);
            }
        });

        ticketExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ticketMinus.setVisibility(View.VISIBLE);
                ticketExpand.setVisibility(View.GONE);
                ll_ticketdetails.setVisibility(View.VISIBLE);
                ll_TicketLine.setVisibility(View.VISIBLE);
            }
        });

        tvBtnTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ticketMinus.setVisibility(View.VISIBLE);
                ticketExpand.setVisibility(View.GONE);
                ll_ticketdetails.setVisibility(View.VISIBLE);
                ll_TicketLine.setVisibility(View.VISIBLE);
                tvBtnTicket2.setVisibility(View.VISIBLE);
                tvBtnTicket.setVisibility(View.GONE);

            }
        });

        tvBtnTicket2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ticketMinus.setVisibility(View.GONE);
                ticketExpand.setVisibility(View.VISIBLE);
                ll_ticketdetails.setVisibility(View.GONE);
                ll_TicketLine.setVisibility(View.GONE);
                tvBtnTicket2.setVisibility(View.GONE);
                tvBtnTicket.setVisibility(View.VISIBLE);
            }
        });


        friendsinviteMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                friendsinviteMinus.setVisibility(View.GONE);
                friendsinviteExpand.setVisibility(View.VISIBLE);
                ll_invite.setVisibility(View.GONE);
                ll_inviteLine.setVisibility(View.GONE);


            }
        });


        friendsinviteExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                friendsinviteMinus.setVisibility(View.VISIBLE);
                friendsinviteExpand.setVisibility(View.GONE);
                ll_invite.setVisibility(View.VISIBLE);
                ll_inviteLine.setVisibility(View.VISIBLE);
            }
        });

        tvBtnFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                friendsinviteMinus.setVisibility(View.VISIBLE);
                friendsinviteExpand.setVisibility(View.GONE);
                ll_invite.setVisibility(View.VISIBLE);
                ll_inviteLine.setVisibility(View.VISIBLE);
                tvBtnFriend2.setVisibility(View.VISIBLE);
                tvBtnFriend.setVisibility(View.GONE);
            }
        });

        tvBtnFriend2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                friendsinviteMinus.setVisibility(View.GONE);
                friendsinviteExpand.setVisibility(View.VISIBLE);
                ll_invite.setVisibility(View.GONE);
                ll_inviteLine.setVisibility(View.GONE);
                tvBtnFriend2.setVisibility(View.GONE);
                tvBtnFriend.setVisibility(View.VISIBLE);
            }
        });

        appDownloadMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                appDownloadMinus.setVisibility(View.GONE);
                appdownloadExpand.setVisibility(View.VISIBLE);
                ll_appdownload.setVisibility(View.GONE);
                ll_appdownloadLine.setVisibility(View.GONE);
            }
        });


        appdownloadExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                appDownloadMinus.setVisibility(View.VISIBLE);
                appdownloadExpand.setVisibility(View.GONE);
                ll_appdownload.setVisibility(View.VISIBLE);
                ll_appdownloadLine.setVisibility(View.VISIBLE);
            }
        });

        tvBtnApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                appDownloadMinus.setVisibility(View.VISIBLE);
                appdownloadExpand.setVisibility(View.GONE);
                ll_appdownload.setVisibility(View.VISIBLE);
                ll_appdownloadLine.setVisibility(View.VISIBLE);
                tvBtnApp2.setVisibility(View.VISIBLE);
                tvBtnApp.setVisibility(View.GONE);
            }
        });

        tvBtnApp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                appDownloadMinus.setVisibility(View.GONE);
                appdownloadExpand.setVisibility(View.VISIBLE);
                ll_appdownload.setVisibility(View.GONE);
                ll_appdownloadLine.setVisibility(View.GONE);
                tvBtnApp2.setVisibility(View.GONE);
                tvBtnApp.setVisibility(View.VISIBLE);
            }
        });

        content();


    }


    public void content(){


        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {


            showProgressDialog();

            JSONObject params = new JSONObject();

            try {
                params.put("page_no", "1");

            } catch (JSONException e) {
                e.printStackTrace();
            }


            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Allurl.ViewRewards, params, response -> {

                Log.i("Response-->", String.valueOf(response));

                try {
                    JSONObject result = new JSONObject(String.valueOf(response));
                    String msg = result.getString("message");
                    Log.d(TAG, "msg-->" + msg);
                    String stat = result.getString("stat");
                    if (stat.equals("succ")) {

                        JSONArray response_data = result.getJSONArray("data");
                        for (int i = 0; i < response_data.length(); i++) {

                            JSONObject responseobj = response_data.getJSONObject(0);
                            String des = responseobj.getString("reward_description");
//                            tvTicketvalue.setText(des);

                        }

                    } else {

                        Toast.makeText(Viewrewards.this, "invalid", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideProgressDialog();

                //TODO: handle success
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Viewrewards.this, error.toString(), Toast.LENGTH_SHORT).show();

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