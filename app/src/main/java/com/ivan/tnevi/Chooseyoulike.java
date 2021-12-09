package com.ivan.tnevi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.tnevi.R;
import com.ivan.tnevi.Adapters.CategoryAdapter2;
import com.ivan.tnevi.Adapters.ChooseAdapter;
import com.ivan.tnevi.Allurl.Allurl;
import com.ivan.tnevi.Utils.ItemOffsetDecoration;
import com.ivan.tnevi.internet.CheckConnectivity;
import com.ivan.tnevi.model.CategoryModel;
import com.ivan.tnevi.session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Chooseyoulike extends AppCompatActivity {

    ImageView btn_back;
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String SHARED_PREFS2 = "chooselike";
    SessionManager sessionManager;
    private static final String TAG = "Myapp";
    String username, password, token, msg;
    RecyclerView rv_choosecategory;
    private ArrayList<CategoryModel> categoryModelArrayList;
    private ChooseAdapter chooseAdapter;
    Button btnSubmit;
    JSONArray data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooseyoulike);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");
        rv_choosecategory = findViewById(R.id.rv_choosecategory);
        btn_back =findViewById(R.id.btn_back);
        btnSubmit = findViewById(R.id.btnSubmit);

        sessionManager = new SessionManager(getApplicationContext());
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = 0;
                if(categoryModelArrayList!=null){
                    data = new JSONArray();
                    for(CategoryModel mCategoryModel :categoryModelArrayList){
                        if(mCategoryModel.isSelected()){
                            count++;
                            data.put(mCategoryModel.getId());
                        }
                    }
                    if(count>2){
                        eventmaylike();
                    }else{
                        Toast.makeText(Chooseyoulike.this,"Please select atleast 3",Toast.LENGTH_SHORT).show();
                    }
                    Log.v("data","categories:"+data.toString());
                }

            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });

        getAllcategories();

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
                        Toast.makeText(Chooseyoulike.this, "invalid", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideProgressDialog();

                //TODO: handle success
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Chooseyoulike.this, error.toString(), Toast.LENGTH_SHORT).show();

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



    public void eventmaylike(){

        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {
            showProgressDialog();
            JSONObject params = new JSONObject();

            try {
                params.put("categories", data.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Allurl.EventsMayLike, params, response -> {

                Log.i("Response-->", String.valueOf(response));

                try {
                    JSONObject result = new JSONObject(String.valueOf(response));
                    String stat = result.getString("stat");
                    if (stat.equals("succ")) {
                        JSONObject response_data = result.getJSONObject("data");
                        String chosen_categories = response_data.getString("chosen_categories");
                        SharedPreferences sharedPreferences2 = getApplicationContext().getSharedPreferences(SHARED_PREFS2, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences2.edit();
                        editor.putString("chosen_categories", chosen_categories);
                        editor.apply();
                        sessionManager.createLoginSession(username, password);
                        Intent intent = new Intent(Chooseyoulike.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                    } else {

                        hideProgressDialog();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                hideProgressDialog();

                //TODO: handle success
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    hideProgressDialog();
                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();

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

        chooseAdapter = new ChooseAdapter(this, categoryModelArrayList);
        rv_choosecategory.setAdapter(chooseAdapter);
        rv_choosecategory.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getApplicationContext(), R.dimen.photos_list_spacing);
        rv_choosecategory.addItemDecoration(itemDecoration);

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