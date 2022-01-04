package com.app.tnevi;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.app.tnevi.Adapters.MyViewallAdapter;
import com.app.tnevi.Allurl.Allurl;
import com.app.tnevi.internet.CheckConnectivity;
import com.app.tnevi.model.CategoryModelSpinner;
import com.app.tnevi.model.GeteventModel;
import com.app.tnevi.session.SessionManager;

import com.app.tnevi.Utils.ItemOffsetDecoration;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class Search2 extends AppCompatActivity {

    LinearLayoutCompat btnHome, btnSearch, btnWallet, btnProf, btnSettings, btnEvent;
    ImageView iconSearch, btn_back;
    EditText etEventname, etEventdate;
    Spinner spComission, spCat;
    TextView tvTitle;
    LinearLayout btnSearchevent, ll_searchDetails;
    RecyclerView rvSearch;
    private static final String TAG = "myapp";
    private static final String SHARED_PREFS = "sharedPrefs";
    SessionManager sessionManager;
    String useremail, username, token, catid, viewall, lat, lon;
    ArrayList<GeteventModel> geteventModelArrayList;
    private ArrayList<CategoryModelSpinner> categoryModelArrayList = new ArrayList<>();
    ArrayList<String> category = new ArrayList<>();
    private MyViewallAdapter myViewallAdapter;
    final Calendar myCalendar = Calendar.getInstance();
//    private AdView viewall_ad;
    LinearLayout ll_nativeAd;
    NativeAd nativeAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search2);
        Intent intent = getIntent();
        viewall = intent.getStringExtra("viewall");
        lat = intent.getStringExtra("lat");
        lon = intent.getStringExtra("long");
        btnSearch = findViewById(R.id.btnSearch);
        btnWallet = findViewById(R.id.btnWallet);
        btnProf = findViewById(R.id.btnProf);
        btnSettings = findViewById(R.id.btnSettings);
        btnEvent = findViewById(R.id.btnEvent);
        btnHome = findViewById(R.id.btnHome);
        btnSearchevent = findViewById(R.id.btnSearchevent);
        etEventname = findViewById(R.id.etEventname);
        etEventdate = findViewById(R.id.etEventdate);
        spComission = findViewById(R.id.spComission);
        iconSearch = findViewById(R.id.iconSearch);
        btn_back = findViewById(R.id.btn_back);
        tvTitle = findViewById(R.id.tvTitle);
        ll_nativeAd = findViewById(R.id.ll_nativeAd);
        ll_searchDetails = findViewById(R.id.ll_searchDetails);
        rvSearch = findViewById(R.id.rvSearch);
        spCat = findViewById(R.id.spCat);
        tvTitle = findViewById(R.id.tvTitle);
//
//        MobileAds.initialize(this, new OnInitializationCompleteListener() {
//            @Override
//            public void onInitializationComplete(InitializationStatus initializationStatus) {
//
//
//            }
//        });
//
//        AdRequest adRequest = new AdRequest.Builder().build();
//        viewall_ad.loadAd(adRequest);


        sessionManager = new SessionManager(getApplicationContext());
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        username = sharedPreferences.getString("name", "");
        useremail = sharedPreferences.getString("email", "");


        onClick();
        refreshAd();


        if (viewall == null) {
            ll_searchDetails.setVisibility(View.GONE);
            rvSearch.setVisibility(View.GONE);
        } else if (viewall.equals("featured")) {
            viewallFeatured();
            tvTitle.setText("Featured Events");
            ll_searchDetails.setVisibility(View.GONE);
            rvSearch.setVisibility(View.VISIBLE);

        } else if (viewall.equals("highlight")) {
            viewallHighlights();
            tvTitle.setText("Hightlight Events");
            ll_searchDetails.setVisibility(View.GONE);
            rvSearch.setVisibility(View.VISIBLE);
        } else if (viewall.equals("topevents")) {
            viewallTop();
            tvTitle.setText("Top Events");
            ll_searchDetails.setVisibility(View.GONE);
            rvSearch.setVisibility(View.VISIBLE);
        } else {
            ll_searchDetails.setVisibility(View.GONE);
            rvSearch.setVisibility(View.GONE);
        }


    }

    public void onClick() {


//        viewall_ad.setAdListener(new AdListener() {
//            @Override
//            public void onAdClosed() {
//                super.onAdClosed();
//                Log.d("ad-test", "Banner ad closed");
//            }
//
//            @Override
//            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
//                super.onAdFailedToLoad(loadAdError);
//                Log.d("ad-test", "Banner Failed to load");
//
//            }
//
//            @Override
//            public void onAdOpened() {
//                super.onAdOpened();
//                Log.d("ad-test", "Banner ad Opened");
//
//            }
//
//            @Override
//            public void onAdLoaded() {
//                super.onAdLoaded();
//                Log.d("ad-test", "Banner ad loaded successfully");
//
//            }
//
//            @Override
//            public void onAdClicked() {
//                super.onAdClicked();
//                Log.d("ad-test", "Banner ad Clicked");
//
//            }
//        });


        spCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position > 0) {
                    catid = categoryModelArrayList.get(position).getId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        DatePickerDialog.OnDateSetListener eventdate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                eventdateupdateLabel();
            }

        };

        etEventdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(Search2.this, eventdate, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Search2.this, MainActivity.class);
                startActivity(intent);

            }
        });


        btnWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Search2.this, Wallet.class);
                startActivity(intent);
            }
        });


        btnProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Search2.this, Profile.class);
                startActivity(intent);

            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Search2.this, Settings.class);
                startActivity(intent);


            }
        });

        btnEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(Search2.this, Events.class);
                startActivity(intent);

            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Search2.this, Search.class);
                startActivity(intent);
            }
        });


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });


    }


//    public void spcat() {
//
//        showProgressDialog();
//        JSONObject params = new JSONObject();
//
//        try {
//            params.put("page_no", "1");
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Allurl.AllCategory, params, response -> {
//            Log.i("Response-->", String.valueOf(response));
//
//            category.clear();
//            categoryModelArrayList.clear();
//            category.add("Select Category");
//
//            try {
//                JSONObject result = new JSONObject(String.valueOf(response));
//                String msg = result.getString("message");
//                Log.d(TAG, "msg-->" + msg);
//                String stat = result.getString("stat");
//                if (stat.equals("succ")) {
//
//                    categoryModelArrayList = new ArrayList<>();
//                    JSONArray jsonArray = result.getJSONArray("data");
//                    for (int i = 0; i < jsonArray.length(); i++) {
//
//                        JSONObject catobj = jsonArray.getJSONObject(i);
//                        String catid = catobj.getString("id");
//                        String catname = catobj.getString("category_name");
//                        category.add(catname);
//                        CategoryModelSpinner categoryModelSpinner = new CategoryModelSpinner(catname, catid);
//                        categoryModelArrayList.add(categoryModelSpinner);
//                    }
//
//                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
//                            (Search2.this, android.R.layout.simple_spinner_dropdown_item, category);
//                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                    spCat.setAdapter(spinnerArrayAdapter);
//
//                } else {
//
//                    Log.d(TAG, "unsuccessfull - " + "Error");
//                    Toast.makeText(Search2.this, "invalid", Toast.LENGTH_SHORT).show();
//                }
//
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            hideProgressDialog();
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                hideProgressDialog();
//                Toast.makeText(Search2.this, error.toString(), Toast.LENGTH_SHORT).show();
//            }
//        }) {
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("Authorization", token);
//                return params;
//            }
//
//        };
//        Volley.newRequestQueue(this).add(jsonRequest);
//
//
//    }


    private void eventdateupdateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        etEventdate.setText(sdf.format(myCalendar.getTime()));
    }

    public void viewallFeatured() {
        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {


            showProgressDialog();

            JSONObject params = new JSONObject();

            try {
                params.put("commission", "");
                params.put("search_address", "");
                params.put("page_no", 1);
                params.put("categories", "");
                params.put("search_date", "");
                params.put("keyword", "");
                params.put("featured_event", "");
                params.put("fev_list", "");
                params.put("highlight_event ", "");
                params.put("featured_event ", 1);
                params.put("top_events", "");
                params.put("latitude", lat);
                params.put("longitude", lon);


            } catch (JSONException e) {
                e.printStackTrace();
            }


            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Allurl.GetEvent, params, response -> {

                Log.i("Response-->", String.valueOf(response));

                try {
                    JSONObject result = new JSONObject(String.valueOf(response));
                    String msg = result.getString("message");
                    Log.d(TAG, "msg-->" + msg);
                    String stat = result.getString("stat");
                    if (stat.equals("succ")) {

                        geteventModelArrayList = new ArrayList<>();
                        JSONArray response_data = result.getJSONArray("data");
                        for (int i = 0; i < response_data.length(); i++) {

                            GeteventModel geteventModel = new GeteventModel();
                            JSONObject responseobj = response_data.getJSONObject(i);
                            geteventModel.setId(responseobj.getString("id"));
                            geteventModel.setEvent_name(responseobj.getString("event_name"));
                            if (!responseobj.isNull("event_image")) {
                                geteventModel.setEvent_image(responseobj.getString("event_image"));
                            } else {
                                geteventModel.setEvent_image("");
                            }
                            geteventModel.setFree_stat(responseobj.getString("free_stat"));
                            geteventModel.setCurrency_id(responseobj.getString("currency_id"));
                            geteventModel.setEvent_date(responseobj.getString("event_date"));
                            geteventModel.setMax_price(responseobj.getString("max_price"));
                            geteventModel.setEvent_commission(responseobj.getString("event_commission"));
                            geteventModel.setMin_price(responseobj.getString("min_price"));
                            geteventModel.setEvent_address(responseobj.getString("event_address"));
                            geteventModel.setStatus(responseobj.getString("status"));
                            geteventModel.setTicket_stat(responseobj.getString("ticket_stat"));
                            geteventModel.setFav_status(responseobj.getString("fav_status"));
                            if (i > 0 && i % 8 == 0) {
                                geteventModelArrayList.add(null);
                            }
                            geteventModelArrayList.add(geteventModel);

                        }

                        if (geteventModelArrayList.size()<=8){
                            ll_nativeAd.setVisibility(View.VISIBLE);
                        }else{
                            ll_nativeAd.setVisibility(View.GONE);
                        }

                        setupRecycler();

                    } else {

                        Log.d(TAG, "unsuccessfull - " + "Error");
                        Toast.makeText(Search2.this, "invalid", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideProgressDialog();

                //TODO: handle success
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Search2.this, error.toString(), Toast.LENGTH_SHORT).show();

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

    public void viewallHighlights() {

        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {


            showProgressDialog();

            JSONObject params = new JSONObject();

            try {
                params.put("commission", "");
                params.put("search_address", "");
                params.put("page_no", 1);
                params.put("categories", "");
                params.put("search_date", "");
                params.put("keyword", "");
                params.put("featured_event", "");
                params.put("fev_list", "");
                params.put("highlight_event ", 1);
                params.put("featured_event ", "");
                params.put("top_events", "");
                params.put("latitude", lat);
                params.put("longitude", lon);

            } catch (JSONException e) {
                e.printStackTrace();
            }


            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Allurl.GetEvent, params, response -> {

                Log.i("Response-->", String.valueOf(response));

                try {
                    JSONObject result = new JSONObject(String.valueOf(response));
                    String msg = result.getString("message");
                    Log.d(TAG, "msg-->" + msg);
                    String stat = result.getString("stat");
                    if (stat.equals("succ")) {

                        geteventModelArrayList = new ArrayList<>();
                        JSONArray response_data = result.getJSONArray("data");
                        for (int i = 0; i < response_data.length(); i++) {

                            GeteventModel geteventModel = new GeteventModel();
                            JSONObject responseobj = response_data.getJSONObject(i);
                            geteventModel.setId(responseobj.getString("id"));
                            geteventModel.setEvent_name(responseobj.getString("event_name"));
                            if (!responseobj.isNull("event_image")) {
                                geteventModel.setEvent_image(responseobj.getString("event_image"));
                            } else {
                                geteventModel.setEvent_image("");
                            }
                            geteventModel.setFree_stat(responseobj.getString("free_stat"));
                            geteventModel.setCurrency_id(responseobj.getString("currency_id"));
                            geteventModel.setEvent_date(responseobj.getString("event_date"));
                            geteventModel.setMax_price(responseobj.getString("max_price"));
                            geteventModel.setEvent_commission(responseobj.getString("event_commission"));
                            geteventModel.setEvent_address(responseobj.getString("event_address"));
                            geteventModel.setStatus(responseobj.getString("status"));
                            geteventModel.setTicket_stat(responseobj.getString("ticket_stat"));
                            geteventModel.setFav_status(responseobj.getString("fav_status"));
                            if (i > 0 && i % 8 == 0) {
                                geteventModelArrayList.add(null);
                            }
                            geteventModelArrayList.add(geteventModel);
                        }

                        if (geteventModelArrayList.size()<=8){
                            ll_nativeAd.setVisibility(View.VISIBLE);
                        }else{
                            ll_nativeAd.setVisibility(View.GONE);
                        }

                        setupRecycler();

                    } else {

                        Log.d(TAG, "unsuccessfull - " + "Error");
                        Toast.makeText(Search2.this, "invalid", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideProgressDialog();

                //TODO: handle success
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Search2.this, error.toString(), Toast.LENGTH_SHORT).show();

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

    public void viewallTop() {

        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {


            showProgressDialog();

            JSONObject params = new JSONObject();

            try {
                params.put("commission", "");
                params.put("search_address", "");
                params.put("page_no", 1);
                params.put("categories", "");
                params.put("search_date", "");
                params.put("keyword", "");
                params.put("featured_event", "");
                params.put("fev_list", "");
                params.put("highlight_event ", "");
                params.put("featured_event ", "");
                params.put("top_events", 1);
                params.put("latitude", lat);
                params.put("longitude", lon);

            } catch (JSONException e) {
                e.printStackTrace();
            }


            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Allurl.GetEvent, params, response -> {

                Log.i("Response-->", String.valueOf(response));

                try {
                    JSONObject result = new JSONObject(String.valueOf(response));
                    String msg = result.getString("message");
                    Log.d(TAG, "msg-->" + msg);
                    String stat = result.getString("stat");
                    if (stat.equals("succ")) {

                        geteventModelArrayList = new ArrayList<>();
                        JSONArray response_data = result.getJSONArray("data");
                        for (int i = 0; i < response_data.length(); i++) {

                            GeteventModel geteventModel = new GeteventModel();
                            JSONObject responseobj = response_data.getJSONObject(i);
                            geteventModel.setId(responseobj.getString("id"));
                            geteventModel.setEvent_name(responseobj.getString("event_name"));
                            if (!responseobj.isNull("event_image")) {
                                geteventModel.setEvent_image(responseobj.getString("event_image"));
                            } else {
                                geteventModel.setEvent_image("");
                            }
                            geteventModel.setFree_stat(responseobj.getString("free_stat"));
                            geteventModel.setCurrency_id(responseobj.getString("currency_id"));
                            geteventModel.setEvent_date(responseobj.getString("event_date"));
                            geteventModel.setEvent_commission(responseobj.getString("event_commission"));
                            geteventModel.setMax_price(responseobj.getString("max_price"));
                            geteventModel.setMin_price(responseobj.getString("min_price"));
                            geteventModel.setEvent_address(responseobj.getString("event_address"));
                            geteventModel.setStatus(responseobj.getString("status"));
                            geteventModel.setTicket_stat(responseobj.getString("ticket_stat"));
                            geteventModel.setFav_status(responseobj.getString("fav_status"));
                            if (i > 0 && i % 8 == 0) {
                                geteventModelArrayList.add(null);
                            }
                            geteventModelArrayList.add(geteventModel);
                        }

                        if (geteventModelArrayList.size()<=8){
                            ll_nativeAd.setVisibility(View.VISIBLE);
                        }else{
                            ll_nativeAd.setVisibility(View.GONE);
                        }

                        setupRecycler();

                    } else {

                        Log.d(TAG, "unsuccessfull - " + "Error");
                        Toast.makeText(Search2.this, "invalid", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideProgressDialog();

                //TODO: handle success
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Search2.this, error.toString(), Toast.LENGTH_SHORT).show();

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

        myViewallAdapter = new MyViewallAdapter(this, geteventModelArrayList);
        rvSearch.setAdapter(myViewallAdapter);
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
              /*  int spancount = position > 0 && (position % 4) == 0 ? 1 : 2;
                Log.v("spancount", spancount + "");
                return (spancount);*/
                if (geteventModelArrayList.get(position) == null) {
                    return 2;
                } else {
                    return 1;
                }
            }
        });
        rvSearch.setLayoutManager(manager);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getApplicationContext(), R.dimen.photos_list_spacing);
        rvSearch.addItemDecoration(itemDecoration);

    }


    public void addRemovefav(GeteventModel geteventModel) {


        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {

            showProgressDialog();

            JSONObject params = new JSONObject();

            try {
                params.put("event_id", geteventModel.getId());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Allurl.AddRemoveFav, params, response -> {

                Log.i("Response-->", String.valueOf(response));
                try {
                    JSONObject result = new JSONObject(String.valueOf(response));
                    String msg = result.getString("message");
                    Log.d(TAG, "msg-->" + msg);
                    String stat = result.getString("stat");
                    if (stat.equals("succ")) {

                        Toast.makeText(Search2.this, msg, Toast.LENGTH_SHORT).show();

                    } else {

                        Log.d(TAG, "unsuccessfull - " + "Error");
                        Toast.makeText(Search2.this, "Add to Favourite not successfull", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideProgressDialog();

                //TODO: handle success
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Search2.this, error.toString(), Toast.LENGTH_SHORT).show();

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


    private void populateUnifiedNativeAdView(NativeAd nativeAd, NativeAdView adView) {
        adView.setMediaView((MediaView) adView.findViewById(R.id.ad_media));
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));


        ((TextView) Objects.requireNonNull(adView.getHeadlineView())).setText(nativeAd.getHeadline());
        Objects.requireNonNull(adView.getMediaView()).setMediaContent(Objects.requireNonNull(nativeAd.getMediaContent()));


        if (nativeAd.getBody() == null) {
            Objects.requireNonNull(adView.getBodyView()).setVisibility(View.INVISIBLE);

        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }
        if (nativeAd.getCallToAction() == null) {
            Objects.requireNonNull(adView.getCallToActionView()).setVisibility(View.INVISIBLE);
        } else {
            Objects.requireNonNull(adView.getCallToActionView()).setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }
        if (nativeAd.getIcon() == null) {
            Objects.requireNonNull(adView.getIconView()).setVisibility(View.GONE);
        } else {
            ((ImageView) Objects.requireNonNull(adView.getIconView())).setImageDrawable(nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            Objects.requireNonNull(adView.getPriceView()).setVisibility(View.INVISIBLE);

        } else {
            Objects.requireNonNull(adView.getPriceView()).setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }
        if (nativeAd.getStore() == null) {
            Objects.requireNonNull(adView.getStoreView()).setVisibility(View.INVISIBLE);
        } else {
            Objects.requireNonNull(adView.getStoreView()).setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }
        if (nativeAd.getStarRating() == null) {
            Objects.requireNonNull(adView.getStarRatingView()).setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) Objects.requireNonNull(adView.getStarRatingView())).setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            Objects.requireNonNull(adView.getAdvertiserView()).setVisibility(View.INVISIBLE);
        } else
            ((TextView) Objects.requireNonNull(adView.getAdvertiserView())).setText(nativeAd.getAdvertiser());
        adView.getAdvertiserView().setVisibility(View.VISIBLE);


        adView.setNativeAd(nativeAd);


    }


    private void refreshAd() {
        AdLoader.Builder builder = new AdLoader.Builder(this, getString(R.string.ADMOB_ADS_UNIT_ID));
        builder.forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
            @Override
            public void onNativeAdLoaded(NativeAd unifiedNativeAd) {

                if (nativeAd != null) {
                    nativeAd.destroy();
                }
                nativeAd = unifiedNativeAd;
                FrameLayout frameLayout = findViewById(R.id.fl_adplaceholder);
                NativeAdView adView = (NativeAdView) getLayoutInflater().inflate(R.layout.ad_helper, null);

                populateUnifiedNativeAdView(unifiedNativeAd, adView);
                frameLayout.removeAllViews();
                frameLayout.addView(adView);
            }
        }).build();
        NativeAdOptions adOptions = new NativeAdOptions.Builder().build();
        builder.withNativeAdOptions(adOptions);
        AdLoader adLoader = builder.withAdListener(new AdListener() {
            public void onAdFailedToLoad(int i) {

            }
        }).build();
        adLoader.loadAd(new AdRequest.Builder().build());

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