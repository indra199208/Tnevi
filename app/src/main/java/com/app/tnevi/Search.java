package com.app.tnevi;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.app.tnevi.Adapters.MySearchtAdapter;
import com.app.tnevi.Adapters.MyViewallAdapter;
import com.app.tnevi.Allurl.Allurl;
import com.app.tnevi.internet.CheckConnectivity;
import com.app.tnevi.model.CategoryModelSpinner;
import com.app.tnevi.model.GeteventModel;
import com.app.tnevi.session.SessionManager;

import com.app.tnevi.Utils.ItemOffsetDecoration;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Search extends AppCompatActivity {

    LinearLayoutCompat btnHome, btnSearch, btnWallet, btnProf, btnSettings, btnEvent;
    RelativeLayout rl_bg;
    ImageView iconSearch, btn_back, btn_backsearch;
    EditText etEventname, etEventdate, etLocation, etEventenddate;
    Spinner spComission, spCat;
    TextView tvTitle;
    LinearLayout btnSearchevent, ll_searchDetails, ll_nocat;
    RecyclerView rvSearch;
    private static final String TAG = "myapp";
    private static final String SHARED_PREFS = "sharedPrefs";
    SessionManager sessionManager;
    String useremail, username, token, catid, viewall, lat, lon;
    ArrayList<GeteventModel> geteventModelArrayList;
    private ArrayList<CategoryModelSpinner> categoryModelArrayList = new ArrayList<>();
    ArrayList<String> category = new ArrayList<>();
    private MySearchtAdapter mySearchtAdapter;
    final Calendar myCalendar = Calendar.getInstance();
    String commissionselected, cat, date, eventname, commission, eventName, enddate;
    PlacesClient placesClient;
    private static int AUTOCOMPLETE_REQUEST_CODE = 1;
    String Lat = "";
    String Lon = "";
    private MyViewallAdapter myViewallAdapter;
    private AdView viewall_ad2;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.api_key));
        }
        placesClient = Places.createClient(this);
        Intent intent = getIntent();
        catid = intent.getStringExtra("eventId");
        eventName = intent.getStringExtra("eventname");
        if (eventName == null) {
            eventName = "";
        } else {
            eventName = intent.getStringExtra("eventname");
        }
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
        etEventenddate = findViewById(R.id.etEventenddate);
        spComission = findViewById(R.id.spComission);
        iconSearch = findViewById(R.id.iconSearch);
        btn_back = findViewById(R.id.btn_back);
        tvTitle = findViewById(R.id.tvTitle);
        ll_searchDetails = findViewById(R.id.ll_searchDetails);
        rvSearch = findViewById(R.id.rvSearch);
        spCat = findViewById(R.id.spCat);
        tvTitle = findViewById(R.id.tvTitle);
        viewall_ad2 = findViewById(R.id.viewall_ad2);
//        etLocation = findViewById(R.id.etLocation);
        ll_nocat = findViewById(R.id.ll_nocat);
        rl_bg = findViewById(R.id.rl_bg);
        btn_backsearch = findViewById(R.id.btn_backsearch);


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {


            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        viewall_ad2.loadAd(adRequest);


        sessionManager = new SessionManager(getApplicationContext());
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        username = sharedPreferences.getString("name", "");
        useremail = sharedPreferences.getString("email", "");


        spcat();
        onClick();
        spcomission();

        if (catid != null) {
            iconSearch.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.darkgray));
            getEvents();
            rl_bg.setBackgroundResource(R.drawable.bg14);
            btn_back.setColorFilter(getApplication().getResources().getColor(R.color.quantum_white_100));
            tvTitle.setTextColor(ContextCompat.getColor(Search.this, R.color.quantum_white_100));
            tvTitle.setText(eventName);
        } else {

            ll_searchDetails.setVisibility(View.VISIBLE);
            rvSearch.setVisibility(View.GONE);
            rl_bg.setBackgroundResource(R.drawable.bg3);
            btn_back.setColorFilter(getApplication().getResources().getColor(R.color.quantum_black_100));
            tvTitle.setTextColor(ContextCompat.getColor(Search.this, R.color.quantum_black_100));
            iconSearch.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.colorAccent));

        }


    }

    public void onClick() {


//        etLocation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                openSearchBar();
//
//            }
//        });

        viewall_ad2.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                Log.d("ad-test", "Banner ad closed");
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                Log.d("ad-test", "Banner Failed to load");

            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                Log.d("ad-test", "Banner ad Opened");

            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.d("ad-test", "Banner ad loaded successfully");

            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
                Log.d("ad-test", "Banner ad Clicked");

            }
        });

        spCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position > 0) {
                    catid = categoryModelArrayList.get(position - 1).getId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spComission.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position > 0) {

                    switch (spComission.getSelectedItem().toString()) {
                        case "Highest to Lowest":
                            commissionselected = "DESC ";
                            break;
                        case "Lowest to Highest":
                            commissionselected = "ASC";
                            break;
                        default:
                            commissionselected = "";
                            break;
                    }
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


        DatePickerDialog.OnDateSetListener eventdate2 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                eventdateupdateLabel2();
            }

        };


        etEventdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(Search.this, eventdate, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        etEventenddate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(Search.this, eventdate2, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Search.this, MainActivity.class);
                startActivity(intent);

            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Search.this, Search.class);
                startActivity(intent);
            }
        });


        btnWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Search.this, Wallet.class);
                startActivity(intent);
            }
        });


        btnProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Search.this, Profile.class);
                startActivity(intent);

            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Search.this, Settings.class);
                startActivity(intent);


            }
        });

        btnEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(Search.this, Events.class);
                startActivity(intent);

            }
        });


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });


        btnSearchevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


//                if (etLocation.getText().toString().length() == 0) {
//                    Toast.makeText(Search.this, "Please Select Address", Toast.LENGTH_SHORT).show();
//
//                } else if (spComission.getSelectedItem().toString().matches("")) {
//
//                    Toast.makeText(getApplicationContext(), "Select Comission", Toast.LENGTH_LONG).show();
//
//                } else {

                searchList();
//                }


            }
        });

        btn_backsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ll_searchDetails.setVisibility(View.VISIBLE);
                rvSearch.setVisibility(View.GONE);
                btn_backsearch.setVisibility(View.GONE);
                btn_back.setVisibility(View.VISIBLE);
            }
        });
    }


//    private void openSearchBar() {
//        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);
//        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
//                .build(this);
//        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
//            if (resultCode == RESULT_OK) {
//                Place place = Autocomplete.getPlaceFromIntent(data);
//                String lati = place.getLatLng().latitude + "";
//                String longi = place.getLatLng().longitude + "";
//                String address = place.getName();
//                Lat = lati;
//                Lon = longi;
//                etLocation.setText(address);
//
//                Log.i(TAG, "Place: " + lati + ", " + longi);
//
//            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
//                // TODO: Handle the error.
//                Status status = Autocomplete.getStatusFromIntent(data);
//                Log.i(TAG, status.getStatusMessage());
//            } else if (resultCode == RESULT_CANCELED) {
//                // The user canceled the operation.
//            }
//            return;
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }


    public void spcat() {

        showProgressDialog();
        JSONObject params = new JSONObject();

        try {
            params.put("page_no", "1");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Allurl.AllCategory, params, response -> {
            Log.i("Response-->", String.valueOf(response));

            category.clear();
            categoryModelArrayList.clear();
            category.add("Select Category");

            try {
                JSONObject result = new JSONObject(String.valueOf(response));
                String msg = result.getString("message");
                Log.d(TAG, "msg-->" + msg);
                String stat = result.getString("stat");
                if (stat.equals("succ")) {

                    categoryModelArrayList = new ArrayList<>();
                    JSONArray jsonArray = result.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject catobj = jsonArray.getJSONObject(i);
                        String catid = catobj.getString("id");
                        String catname = catobj.getString("category_name");
                        category.add(catname);
                        CategoryModelSpinner categoryModelSpinner = new CategoryModelSpinner(catname, catid);
                        categoryModelArrayList.add(categoryModelSpinner);
                    }

                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                            (Search.this, android.R.layout.simple_spinner_dropdown_item, category);
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spCat.setAdapter(spinnerArrayAdapter);

                } else {

                    Log.d(TAG, "unsuccessfull - " + "Error");
                    Toast.makeText(Search.this, "invalid", Toast.LENGTH_SHORT).show();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            hideProgressDialog();
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                hideProgressDialog();
                Toast.makeText(Search.this, error.toString(), Toast.LENGTH_SHORT).show();
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


    }


    private void eventdateupdateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        etEventdate.setText(sdf.format(myCalendar.getTime()));
    }

    private void eventdateupdateLabel2() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        etEventenddate.setText(sdf.format(myCalendar.getTime()));
    }

    private void spcomission() {

        List<String> comission = new ArrayList<String>();

        comission.add("Please Select");
        comission.add("Highest to Lowest");
        comission.add("Lowest to Highest");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, comission);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spComission.setAdapter(arrayAdapter);


    }

    public void searchList() {

        ll_searchDetails.setVisibility(View.GONE);
        rvSearch.setVisibility(View.VISIBLE);
        btn_backsearch.setVisibility(View.VISIBLE);
        btn_back.setVisibility(View.GONE);

        if (etEventname.getText().toString().length() == 0) {
            eventname = "";
        } else {
            eventname = etEventname.getText().toString();
        }
        if (etEventdate.getText().toString().length() == 0) {
            date = "";
        } else {
            date = etEventdate.getText().toString();
        }

        if (etEventenddate.getText().toString().length() == 0) {
            enddate = "";
        } else {
            enddate = "";
        }

        if (catid == null) {
            cat = "";
        } else {
            cat = "[\"" + catid + "\"]";
        }

        if (spComission.getSelectedItem().toString().equals("Please Select")) {
            commission = "";
        } else {
            commission = commissionselected;
        }


        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {

            showProgressDialog();
            JSONObject params = new JSONObject();

            try {
                params.put("page_no", "1");
                params.put("commission", commission);
                params.put("search_address ", "");
                params.put("categories", cat);
                params.put("start_date", date);
                params.put("end_date", enddate);
                params.put("keyword", eventname);
                params.put("featured_event", "");
                params.put("fev_list", "");
                params.put("latitude", 0.0);
                params.put("longitude", 0.0);
                params.put("soldout_stat", "");
                params.put("highlight_event", "");
                params.put("top_events", "");
                params.put("my_ticket", "");
                params.put("sort_by_date", "");
                params.put("sort_by_name", "");

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
                            geteventModel.setMin_price(responseobj.getString("min_price"));
                            geteventModel.setEvent_address(responseobj.getString("event_address"));
                            geteventModel.setStatus(responseobj.getString("status"));
                            geteventModel.setTicket_stat(responseobj.getString("ticket_stat"));
                            geteventModel.setFav_status(responseobj.getString("fav_status"));
                            geteventModel.setEvent_commission(responseobj.getString("event_commission"));
                            geteventModelArrayList.add(geteventModel);

                        }

                        setupSearch();

                    } else {

                        Log.d(TAG, "unsuccessfull - " + "Error");
                        Toast.makeText(Search.this, "invalid", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideProgressDialog();

                //TODO: handle success
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Search.this, error.toString(), Toast.LENGTH_SHORT).show();

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


    public void getEvents() {

        ll_searchDetails.setVisibility(View.GONE);
        rvSearch.setVisibility(View.VISIBLE);
        viewall_ad2.setVisibility(View.VISIBLE);
//        String categoryid = "[\\\"" + catid + "\\\"]";
        String categoryid = "[\"" + catid + "\"]";

        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {

            showProgressDialog();
            JSONObject params = new JSONObject();

            try {
                params.put("page_no", "1");
                params.put("commission", "");
                params.put("search_address ", "");
                params.put("categories", categoryid);
                params.put("search_date", "");
                params.put("keyword", "");
                params.put("featured_event", "");
                params.put("fev_list", "");
                params.put("latitude", lat);
                params.put("longitude", lon);
                params.put("soldout_stat", "");
                params.put("highlight_event", "");
                params.put("top_events", "");
                params.put("my_ticket", "");
                params.put("sort_by_date", "");
                params.put("sort_by_name", "");


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
                            geteventModel.setMin_price(responseobj.getString("min_price"));
                            geteventModel.setEvent_commission(responseobj.getString("event_commission"));
                            geteventModel.setEvent_address(responseobj.getString("event_address"));
                            geteventModel.setStatus(responseobj.getString("status"));
                            geteventModel.setTicket_stat(responseobj.getString("ticket_stat"));
                            geteventModel.setFav_status(responseobj.getString("fav_status"));
                            if (i > 0 && i % 4 == 0) {
                                geteventModelArrayList.add(null);
                            }
                            geteventModelArrayList.add(geteventModel);

                        }
                        if (response_data.length() == 0) {
                            ll_nocat.setVisibility(View.VISIBLE);
                            ll_searchDetails.setVisibility(View.GONE);
                            rvSearch.setVisibility(View.GONE);
                        } else {
                            ll_searchDetails.setVisibility(View.GONE);
                            rvSearch.setVisibility(View.VISIBLE);
                            ll_nocat.setVisibility(View.GONE);
                        }

                        if (geteventModelArrayList.size() <= 4) {
                            viewall_ad2.setVisibility(View.VISIBLE);
                        } else {
                            viewall_ad2.setVisibility(View.GONE);
                        }


                        if (catid != null) {
                            setupRecycler();
                        } else {
                            setupSearch();
                        }


                    } else {

                        Log.d(TAG, "unsuccessfull - " + "Error");
                        Toast.makeText(Search.this, "invalid", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideProgressDialog();

                //TODO: handle success
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Search.this, error.toString(), Toast.LENGTH_SHORT).show();

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


    private void setupSearch() {
        mySearchtAdapter = new MySearchtAdapter(this, geteventModelArrayList);
        rvSearch.setAdapter(mySearchtAdapter);
        rvSearch.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
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

                        Toast.makeText(Search.this, msg, Toast.LENGTH_SHORT).show();

                    } else {

                        Log.d(TAG, "unsuccessfull - " + "Error");
                        Toast.makeText(Search.this, "Add to Favourite not successfull", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideProgressDialog();

                //TODO: handle success
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Search.this, error.toString(), Toast.LENGTH_SHORT).show();

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