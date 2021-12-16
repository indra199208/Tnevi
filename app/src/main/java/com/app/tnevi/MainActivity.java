package com.app.tnevi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.tnevi.Adapters.CategoryAdapter;
import com.app.tnevi.Adapters.FeaturedAdapter;
import com.app.tnevi.Adapters.HighlighteventAdapter;
import com.app.tnevi.Adapters.TopeventAdapter;
import com.app.tnevi.Allurl.Allurl;
import com.app.tnevi.internet.CheckConnectivity;
import com.app.tnevi.model.CategoryModel;
import com.app.tnevi.model.GeteventModel;
import com.app.tnevi.session.SessionManager;

import com.app.tnevi.Utils.ItemOffsetDecoration;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    LinearLayoutCompat btnHome, btnSearch, btnWallet, btnProf, btnSettings, btnEvent;
    ImageView imgHome, imgNotification;
    LinearLayout btnBalance, btnEpoints, btnProfile;
    TextView btnViewall, navBaraddress, tvBalance, tvEponts, tvUsername, tvAllHighlight, tvAllFeatured, tvAllTopevents, navLocationchange;
    RelativeLayout btnEventdetails, btnAddeventdetails2;
    private static final String SHARED_PREFS = "sharedPrefs";
    SessionManager sessionManager;
    private static final String TAG = "Myapp";
    String username, useremail, token, msg, address, postalcode,countrycode, address2 ;
    Geocoder geocoder;
    List<Address> addresses;
    FloatingActionButton fab;
    RecyclerView rv_topevents, rv_highlight, rv_browsecat, rv_featured;
    String lat = "";
    String lon = "";
    private ArrayList<CategoryModel> categoryModelArrayList;
    private ArrayList<GeteventModel> homeEventsModelArrayList;
    private CategoryAdapter categoryAdapter;
    private TopeventAdapter topeventAdapter;
    private HighlighteventAdapter highlighteventAdapter;
    private FeaturedAdapter featuredAdapter;
    private static int AUTOCOMPLETE_REQUEST_CODE = 1;
    PlacesClient placesClient;
    Button Viewall;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.api_key));
        }
        placesClient = Places.createClient(this);

        btnSearch = findViewById(R.id.btnSearch);
        btnWallet = findViewById(R.id.btnWallet);
        btnProf = findViewById(R.id.btnProf);
        btnSettings = findViewById(R.id.btnSettings);
        btnEvent = findViewById(R.id.btnEvent);
        btnHome = findViewById(R.id.btnHome);
        tvUsername = findViewById(R.id.tvUsername);
        imgHome = findViewById(R.id.imgHome);
        btnViewall = findViewById(R.id.btnViewall);
        btnEventdetails = findViewById(R.id.btnEventdetails);
        btnAddeventdetails2 = findViewById(R.id.btnAddeventdetails2);
        navBaraddress = findViewById(R.id.navBaraddress);
        tvBalance = findViewById(R.id.tvBalance);
        tvEponts = findViewById(R.id.tvEponts);
        btnEpoints = findViewById(R.id.btnEpoints);
        btnBalance = findViewById(R.id.btnBalance);
        btnProfile = findViewById(R.id.btnProfile);
        fab = findViewById(R.id.fab);
        rv_topevents = findViewById(R.id.rv_topevents);
        rv_highlight = findViewById(R.id.rv_highlight);
        rv_browsecat = findViewById(R.id.rv_browsecat);
        rv_featured = findViewById(R.id.rv_featured);
        tvAllHighlight = findViewById(R.id.tvAllHighlight);
        tvAllFeatured = findViewById(R.id.tvAllFeatured);
        tvAllTopevents = findViewById(R.id.tvAllTopevents);
        navLocationchange = findViewById(R.id.navLocationchange);
        Viewall = findViewById(R.id.Viewall);
        imgNotification = findViewById(R.id.imgNotification);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imgHome.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.colorAccent));
        }

        sessionManager = new SessionManager(getApplicationContext());
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        username = sharedPreferences.getString("name", "");
        useremail = sharedPreferences.getString("email", "");
        token = sharedPreferences.getString("token", "");
        Log.d(TAG, "token-->" + token);
        tvUsername.setText(username);

        getAccount();
        onClick();


    }


    public void onClick() {

        navLocationchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openSearchBar();

            }
        });

        imgNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Notification.class);
                startActivity(intent);
            }
        });

        tvAllFeatured.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, Search2.class);
                intent.putExtra("viewall", "featured");
                intent.putExtra("lat", lat);
                intent.putExtra("long", lon);
                startActivity(intent);
            }
        });

        tvAllHighlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, Search2.class);
                intent.putExtra("viewall", "highlight");
                intent.putExtra("lat", lat);
                intent.putExtra("long", lon);
                startActivity(intent);
            }
        });

        Viewall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(MainActivity.this, Search2.class);
                intent.putExtra("viewall", "highlight");
                intent.putExtra("lat", lat);
                intent.putExtra("long", lon);
                startActivity(intent);

            }
        });

        tvAllTopevents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, Search2.class);
                intent.putExtra("viewall", "topevents");
                intent.putExtra("lat", lat);
                intent.putExtra("long", lon);
                startActivity(intent);

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, Allcategory.class);
                startActivity(intent);
            }
        });

        btnBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, Wallet.class);
                startActivity(intent);
            }
        });

        btnEpoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, Wallet.class);
                startActivity(intent);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, Search.class);
                startActivity(intent);

            }
        });

        btnWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, Wallet.class);
                startActivity(intent);
            }
        });


        btnProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, Profile.class);
                startActivity(intent);

            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, Settings.class);
                startActivity(intent);


            }
        });

        btnEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(MainActivity.this, Events.class);
                startActivity(intent);

            }
        });


        btnViewall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, Viewallcategory.class);
                startActivity(intent);
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, Profile.class);
                startActivity(intent);

            }
        });
    }


    private void openSearchBar() {
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .build(this);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                String lati = place.getLatLng().latitude + "";
                String longi = place.getLatLng().longitude + "";
                address2 = place.getName();
                lat = lati;
                lon = longi;
                try {
                    addresses = geocoder.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 1);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (addresses != null && addresses.size() > 0) {
                    address = addresses.get(0).getLocality();
                    postalcode = addresses.get(0).getPostalCode();
                    countrycode = addresses.get(0).getCountryCode();
                    updateLocation();
                    navBaraddress.setText(address2);
                }else{
                    Toast.makeText(MainActivity.this,"Unable to get the location please try again",Toast.LENGTH_LONG).show();
                    hideProgressDialog();
                }

                Topevents();
                featuredEvents();
                highlightEvents();

                Log.i(TAG, "Place: " + lati + ", " + longi);

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
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

                                    JSONObject userdeatisObj = result.getJSONObject("data");
                                    JSONObject profileObj = userdeatisObj.getJSONObject("profile");
                                    JSONObject walletObj = userdeatisObj.getJSONObject("wallet");
                                    String ticketsell = walletObj.getString("ticket_sell");
                                    String commission = walletObj.getString("commission");
                                    String epoints = walletObj.getString("epoints");
                                    String address = profileObj.getString("address");
                                    String country_code = profileObj.getString("country_code");
                                    if (!profileObj.isNull("lat_val")) {
                                        lat = profileObj.getString("lat_val");
                                        lon = profileObj.getString("long_val");
                                        double lati = Double.parseDouble(lat);
                                        double loni = Double.parseDouble(lon);
                                        geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                                        addresses = geocoder. getFromLocation(lati, loni, 1);
                                        String fulladdress = addresses.get(0).getAddressLine(0);
                                        String city = addresses.get(0).getLocality();
                                        String state = addresses.get(0).getAdminArea();
                                        String country = addresses.get(0).getCountryName();
                                        navBaraddress.setText(city + ", " + state + ", " + country);

                                    } else {
                                        lat = "";
                                        lon = "";
                                        navBaraddress.setText("");

                                    }

                                    tvBalance.setText("$" + ticketsell);
                                    tvEponts.setText(epoints);
//                                    fname = userdeatisObj.getString("name");
//                                    email = userdeatisObj.getString("email");
//
//                                    JSONObject profileObj = userdeatisObj.getJSONObject("profile");
//                                    pro_pic = profileObj.getString("pro_pic");
//                                    Log.d(TAG, "profileurl-->" + pro_pic);
//
//
//
//
//                                    tvName.setText(fname);
//                                    tvEmail.setText(email);


//                                    if (!profileObj.isNull("pro_pic")) {
//                                        pro_pic = profileObj.getString("pro_pic");
//                                    } else {
//                                        pro_pic = "";
//                                    }
                                    getAllcategories();
                                    Topevents();
                                    highlightEvents();
                                    featuredEvents();


                                } else {

                                    hideProgressDialog();
                                    Log.d(TAG, "unsuccessfull - " + "Error");
                                    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


//                            hideProgressDialog();


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            hideProgressDialog();
                            Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", token);
                    return params;
                }

            };


            RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
            requestQueue.add(stringRequest);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    9000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        } else {

            Toast.makeText(getApplicationContext(), "OOPS! No Internet Connection", Toast.LENGTH_SHORT).show();

        }


    }


    public void getAllcategories() {

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
                        Toast.makeText(MainActivity.this, "invalid", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                hideProgressDialog();

                //TODO: handle success
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();

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

    public void Topevents() {

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
                params.put("highlight_event", "");
                params.put("featured_event", "");
                params.put("top_events", 1);
                params.put("latitude", lat);
                params.put("longitude", lon);
                params.put("sort_by_date", "");
                params.put("sort_by_name", "");
                params.put("selling_stat", "");
                params.put("my_ticket", "");
                params.put("soldout_stat", "");


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

                        homeEventsModelArrayList = new ArrayList<>();
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
                            geteventModel.setHighlightevent(responseobj.getString("highlight_event"));
                            geteventModel.setTicket_stat(responseobj.getString("top_events"));
                            geteventModel.setFav_status(responseobj.getString("fav_status"));
                            homeEventsModelArrayList.add(geteventModel);

                        }

                        setupRecyclerTopevents();

                    } else {

                        Log.d(TAG, "unsuccessfull - " + "Error");
                        Toast.makeText(MainActivity.this, "invalid", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                hideProgressDialog();

                //TODO: handle success
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();

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


    public void highlightEvents() {

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

                        homeEventsModelArrayList = new ArrayList<>();
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
                            geteventModel.setMin_price(responseobj.getString("min_price"));
                            geteventModel.setEvent_address(responseobj.getString("event_address"));
                            geteventModel.setEvent_commission(responseobj.getString("event_commission"));
                            geteventModel.setStatus(responseobj.getString("status"));
                            geteventModel.setTicket_stat(responseobj.getString("ticket_stat"));
                            geteventModel.setHighlightevent(responseobj.getString("highlight_event"));
                            geteventModel.setTicket_stat(responseobj.getString("top_events"));
                            geteventModel.setFav_status(responseobj.getString("fav_status"));
                            homeEventsModelArrayList.add(geteventModel);

                        }

                        setupRecyclerHighlightevents();

                    } else {

                        Log.d(TAG, "unsuccessfull - " + "Error");
                        Toast.makeText(MainActivity.this, "invalid", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                hideProgressDialog();

                //TODO: handle success
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();

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

    public void featuredEvents() {


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

                        homeEventsModelArrayList = new ArrayList<>();
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
                            geteventModel.setMin_price(responseobj.getString("min_price"));
                            geteventModel.setEvent_commission(responseobj.getString("event_commission"));
                            geteventModel.setEvent_address(responseobj.getString("event_address"));
                            geteventModel.setStatus(responseobj.getString("status"));
                            geteventModel.setTicket_stat(responseobj.getString("ticket_stat"));
                            geteventModel.setHighlightevent(responseobj.getString("highlight_event"));
                            geteventModel.setTicket_stat(responseobj.getString("top_events"));
                            geteventModel.setFav_status(responseobj.getString("fav_status"));
                            homeEventsModelArrayList.add(geteventModel);

                        }

                        Viewall.setVisibility(response_data.length() > 8 ? View.VISIBLE : View.GONE);
                        setupRecyclerFeaturedevents();

                    } else {

                        Log.d(TAG, "unsuccessfull - " + "Error");
                        Toast.makeText(MainActivity.this, "invalid", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideProgressDialog();

                //TODO: handle success
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();

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


    public void updateLocation() {

        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {

            JSONObject params = new JSONObject();

            try {
                params.put("allow_location", "0");
                params.put("zipcode", postalcode);
                params.put("address", address);
                params.put("lat_val", lat);
                params.put("long_val", lon);
                params.put("country_code", countrycode);


            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Allurl.UpdateLocation, params, response -> {

                Log.d("Response-->", String.valueOf(response));
                Toast.makeText(MainActivity.this, "Location Updated", Toast.LENGTH_SHORT).show();
                hideProgressDialog();

                //TODO: handle success
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, "Invalid", Toast.LENGTH_SHORT).show();
                    hideProgressDialog();

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
        rv_browsecat.setAdapter(categoryAdapter);
        rv_browsecat.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

    }

    private void setupRecyclerTopevents() {

        topeventAdapter = new TopeventAdapter(this, homeEventsModelArrayList);
        rv_topevents.setAdapter(topeventAdapter);
        rv_topevents.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

    }

    private void setupRecyclerHighlightevents() {

        highlighteventAdapter = new HighlighteventAdapter(this, homeEventsModelArrayList);
        rv_highlight.setAdapter(highlighteventAdapter);
        rv_highlight.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

    }

    private void setupRecyclerFeaturedevents() {

        featuredAdapter = new FeaturedAdapter(this, homeEventsModelArrayList);
        rv_featured.setAdapter(featuredAdapter);
        rv_featured.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getApplicationContext(), R.dimen.photos_list_spacing2);
        rv_featured.addItemDecoration(itemDecoration);

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

                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();

                    } else {

                        Log.d(TAG, "unsuccessfull - " + "Error");
                        Toast.makeText(MainActivity.this, "Add to Favourite not successfull", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideProgressDialog();

                //TODO: handle success
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();

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


    public void getcurrentlatlong(String eventid, String eventname) {

        Intent intent = new Intent(this, Search.class);
        intent.putExtra("eventId", eventid);
        intent.putExtra("eventname", eventname);
        intent.putExtra("lat", lat);
        intent.putExtra("long", lon);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {


        finishAffinity();

    }

//
//    @Override
//    public void locationOn() {
//        Toast.makeText(this, "Location ON", Toast.LENGTH_SHORT).show();
//
//    }
//
//    @Override
//    public void currentLocation(Location location) {
//
//        Lat = String.valueOf(location.getLatitude());
//        Long = String.valueOf(location.getLongitude());
//        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
//        try {
//            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
//            String fulladdress = addresses.get(0).getAddressLine(0);
//            String city = addresses.get(0).getLocality();
//            String state = addresses.get(0).getAdminArea();
//            String country = addresses.get(0).getCountryName();
//            navBaraddress.setText(city+", "+state+", "+country);
//
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    @Override
//    public void locationCancelled() {
//        Toast.makeText(this, "Location Cancelled", Toast.LENGTH_SHORT).show();
//
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//            case LOCATION_SETTING_REQUEST_CODE:
//                easyWayLocation.onActivityResult(resultCode);
//                break;
//        }
//    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        easyWayLocation.startLocation();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        easyWayLocation.endUpdates();
//
//    }
}