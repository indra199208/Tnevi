package com.app.tnevi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.app.tnevi.Adapters.PlanAdapter;
import com.app.tnevi.model.PlanModel;
import com.app.tnevi.session.SessionManager;
import com.app.tnevi.Allurl.Allurl;

import com.app.tnevi.internet.CheckConnectivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Featuread extends AppCompatActivity {

    private static final String TAG = "myapp";
    private static final String SHARED_PREFS = "sharedPrefs";
    SessionManager sessionManager;
    String useremail, username, token, eventId;
    ArrayList<PlanModel> planModelArrayList;
    private PlanAdapter planAdapter;
    private RecyclerView rv_plans;
    private TextView tv_price;
    LinearLayout btnBuy;
    String amount = "";
    String selectedplanid = "";
    String selectedplantype="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_featuread);
        Intent intent = getIntent();
        eventId = intent.getStringExtra("eventId");
        rv_plans = findViewById(R.id.rv_plans);
        tv_price = findViewById(R.id.tv_price);
        btnBuy = findViewById(R.id.btnBuy);

        sessionManager = new SessionManager(getApplicationContext());
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        username = sharedPreferences.getString("name", "");
        useremail = sharedPreferences.getString("email", "");

        getPlans();

        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (amount.length()==0){
                    Toast.makeText(Featuread.this,"Select any option",Toast.LENGTH_SHORT).show();
                }else {

                    Intent intent = new Intent(Featuread.this, Plancheckout.class);
                    intent.putExtra("amount", amount);
                    intent.putExtra("selectedplanid", selectedplanid);
                    intent.putExtra("selectedplantype", selectedplantype);
                    intent.putExtra("eventId", eventId);
                    startActivity(intent);


                }
            }
        });


    }


    public void getPlans() {


        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {


            showProgressDialog();

            JSONObject params = new JSONObject();

            try {
                params.put("page_no", "1");
            } catch (JSONException e) {
                e.printStackTrace();
            }


            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Allurl.GetPlans, params, response -> {

                Log.i("Response-->", String.valueOf(response));

                try {
                    JSONObject result = new JSONObject(String.valueOf(response));
                    String msg = result.getString("message");
                    Log.d(TAG, "msg-->" + msg);
                    String stat = result.getString("stat");
                    if (stat.equals("succ")) {

                        planModelArrayList = new ArrayList<>();
                        JSONArray response_data = result.getJSONArray("data");
                        for (int i = 0; i < response_data.length(); i++) {

                            PlanModel planModel = new PlanModel();
                            JSONObject responseobj = response_data.getJSONObject(i);
                            planModel.setId(responseobj.getString("id"));
                            planModel.setPlan_name(responseobj.getString("plan_name"));
                            planModel.setType(responseobj.getString("type"));
                            planModel.setTagline(responseobj.getString("tagline"));
                            planModel.setDescription(responseobj.getString("description"));
                            planModel.setPlan_price(responseobj.getString("plan_price"));
                            planModel.setPlan_length(responseobj.getString("plan_length"));
                            planModelArrayList.add(planModel);

                        }

                        setupRecycler();

                    } else {

                        Log.d(TAG, "unsuccessfull - " + "Error");
                        Toast.makeText(Featuread.this, "invalid", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideProgressDialog();

                //TODO: handle success
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Featuread.this, error.toString(), Toast.LENGTH_SHORT).show();

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

        planAdapter = new PlanAdapter(this, planModelArrayList);
        rv_plans.setAdapter(planAdapter);
        rv_plans.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

    }

    public void updatePriceText(String price, String id, String type) {
        tv_price.setText("Pay $" + price);
        amount = price;
        selectedplanid = id;
        selectedplantype = type;
        planAdapter.notifyDataSetChanged();
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