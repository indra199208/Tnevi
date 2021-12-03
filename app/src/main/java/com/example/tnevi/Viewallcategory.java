package com.example.tnevi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.tnevi.Adapters.CategoryAdapter;
import com.example.tnevi.Adapters.FavAdapter;
import com.example.tnevi.Allurl.Allurl;
import com.example.tnevi.Utils.ItemOffsetDecoration;
import com.example.tnevi.internet.CheckConnectivity;
import com.example.tnevi.model.CategoryModel;
import com.example.tnevi.model.GeteventModel;
import com.example.tnevi.session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Viewallcategory extends AppCompatActivity {

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
    RecyclerView rv_allcategory;
    private ArrayList<CategoryModel> categoryModelArrayList;
    private CategoryAdapter categoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewallcategory);
        btnAddevent = findViewById(R.id.btnAddevent);
        btn_back =findViewById(R.id.btn_back);
        rv_allcategory = findViewById(R.id.rv_allcategory);

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


        btnAddevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                    Intent intent = new Intent(Viewallcategory.this, Allcategory.class);
                    intent.putExtra("category", category);
                    startActivity(intent);



            }
        });


    }


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
                        Toast.makeText(Viewallcategory.this, "invalid", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideProgressDialog();

                //TODO: handle success
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Viewallcategory.this, error.toString(), Toast.LENGTH_SHORT).show();

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

        categoryAdapter = new CategoryAdapter(this, categoryModelArrayList);
        rv_allcategory.setAdapter(categoryAdapter);
        rv_allcategory.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getApplicationContext(), R.dimen.photos_list_spacing);
        rv_allcategory.addItemDecoration(itemDecoration);


    }


    public void getcurrentlatlong(String eventid){

        Intent intent = new Intent(this, Search.class);
        intent.putExtra("eventId", eventid);
        intent.putExtra("lat", "22.777121");
        intent.putExtra("long", "88.637451");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(Viewallcategory.this, MainActivity.class);
        startActivity(intent);

    }


}