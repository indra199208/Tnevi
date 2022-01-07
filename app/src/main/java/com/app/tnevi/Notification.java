package com.app.tnevi;

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
import com.app.tnevi.Adapters.NotificationAdapter;
import com.app.tnevi.Allurl.Allurl;

import com.app.tnevi.internet.CheckConnectivity;
import com.app.tnevi.model.NotificationModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Notification extends AppCompatActivity {

    ImageView btn_back;
    String token;
    private static final String SHARED_PREFS = "sharedPrefs";
    private ArrayList<NotificationModel>notificationModelArrayList;
    NotificationAdapter notificationAdapter;
    RecyclerView rvNotifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        rvNotifications = findViewById(R.id.rvNotifications);
        btn_back = findViewById(R.id.btn_back);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });

        notificationList();
    }

    public void notificationList(){

        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {


            showProgressDialog();


            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Allurl.NotificationList, null, response -> {

                Log.i("Response-->", String.valueOf(response));

                try {
                    JSONObject result = new JSONObject(String.valueOf(response));
                    String msg = result.getString("message");
                    String stat = result.getString("stat");
                    if (stat.equals("succ")) {

                        notificationModelArrayList = new ArrayList<>();
                        JSONArray response_data = result.getJSONArray("data");
                        for (int i = 0; i < response_data.length(); i++) {

                            NotificationModel notificationModel = new NotificationModel();
                            JSONObject responseobj = response_data.getJSONObject(i);
                            notificationModel.setId(responseobj.getString("id"));
                            notificationModel.setMsg(responseobj.getString("msg"));
                            notificationModel.setNotify_date(responseobj.getString("notify_date"));
                            notificationModel.setTitle(responseobj.getString("title"));
                            notificationModel.setType(responseobj.getString("type"));
                            notificationModelArrayList.add(notificationModel);

                        }

                        setupRecycler();

                    } else {

                        Toast.makeText(Notification.this, "invalid", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideProgressDialog();

                //TODO: handle success
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Notification.this, error.toString(), Toast.LENGTH_SHORT).show();

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

        notificationAdapter = new NotificationAdapter(this, notificationModelArrayList);
        rvNotifications.setAdapter(notificationAdapter);
        rvNotifications.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

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