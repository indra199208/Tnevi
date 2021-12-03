package com.example.tnevi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.tnevi.Adapters.CategoryAdapter;
import com.example.tnevi.Adapters.FaqAdapter;
import com.example.tnevi.Adapters.FavAdapter;
import com.example.tnevi.Allurl.Allurl;
import com.example.tnevi.internet.CheckConnectivity;
import com.example.tnevi.model.FaqModel;
import com.example.tnevi.model.GeteventModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FAQ extends AppCompatActivity {

    ImageView btn_back;
    String token;
    private static final String SHARED_PREFS = "sharedPrefs";
    private ArrayList<FaqModel> faqModelArrayList;
    FaqAdapter faqAdapter;
    RecyclerView rvFAQ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        btn_back = findViewById(R.id.btn_back);
        rvFAQ = findViewById(R.id.rvFAQ);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });

        faqlist();
    }


    public void faqlist(){


        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {


            showProgressDialog();


            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Allurl.FAQ, null, response -> {

                Log.i("Response-->", String.valueOf(response));

                try {
                    JSONObject result = new JSONObject(String.valueOf(response));
                    String msg = result.getString("message");
                    String stat = result.getString("stat");
                    if (stat.equals("succ")) {

                        faqModelArrayList = new ArrayList<>();
                        JSONArray response_data = result.getJSONArray("data");
                        for (int i = 0; i < response_data.length(); i++) {

                            FaqModel faqModel = new FaqModel();
                            JSONObject responseobj = response_data.getJSONObject(i);
                            faqModel.setId(responseobj.getString("id"));
                            faqModel.setQuestion(responseobj.getString("question"));
                            faqModel.setAnswer(responseobj.getString("answer"));
                            faqModelArrayList.add(faqModel);

                        }

                        setupRecycler();

                    } else {

                        Toast.makeText(FAQ.this, "invalid", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideProgressDialog();

                //TODO: handle success
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(FAQ.this, error.toString(), Toast.LENGTH_SHORT).show();

                }
            });

            Volley.newRequestQueue(this).add(jsonRequest);

        } else {

            Toast.makeText(getApplicationContext(), "Ooops! Internet Connection Error", Toast.LENGTH_SHORT).show();

        }

    }


    private void setupRecycler() {

        faqAdapter = new FaqAdapter(this, faqModelArrayList);
        rvFAQ.setAdapter(faqAdapter);
        rvFAQ.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

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