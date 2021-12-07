package com.ivan.tnevi;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ivan.tnevi.Allurl.Allurl;
import com.example.tnevi.R;
import com.ivan.tnevi.Utils.DatabaseHelper;
import com.ivan.tnevi.internet.CheckConnectivity;
import com.ivan.tnevi.model.CategoryModelSpinner;
import com.ivan.tnevi.model.CurrencyModel;
import com.ivan.tnevi.session.SessionManager;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Editevent extends AppCompatActivity {

    private static final String TAG = "myapp";
    private static final String SHARED_PREFS = "sharedPrefs";
    SessionManager sessionManager;
    String useremail, username, token, eventId, youtubelink, actualLat, actualLong;
    String msg, eventname, postedby, eventimage, freestat, social, comission, eventdate, stime, etime, minprice, maxprice, phonenumber,
            address, longval, latval, description, venuename, venueaddress, venueaddress2,
            currencyid, eventLat, eventLong, catName, catoriginalid, Enddate;
    int id, userid, favstatus;
    EditText etEventname, startDate, endDate, sDate, eDate, etVenuename,
            etVenueaddress, etPhone, etDescription, etTicketAmount, etCommission, etfullAddress;
    Spinner spCurrency, spCategory;
    String currencyId = "";
    String catid = "";
    String categoryid = "";
    ArrayList<String> currency = new ArrayList<>();
    ArrayList<CurrencyModel> setcurrency = new ArrayList<>();
    int MY_SOCKET_TIMEOUT_MS = 90000;
    LinearLayout btnContinueEdit;
    final Calendar myCalendar = Calendar.getInstance();
    PlacesClient placesClient;
    private static int AUTOCOMPLETE_REQUEST_CODE = 1;
    String response2;
    ImageView btn_back;
    DatabaseHelper database_helper;
    private ArrayList<CategoryModelSpinner> categoryModelArrayList = new ArrayList<>();
    ArrayList<String> category = new ArrayList<>();
    JSONArray categoryarry;
    Geocoder geocoder;
    List<Address> addresses;
    String fulladdress = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editevent);
        database_helper = new DatabaseHelper(this);
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.api_key));
        }
        placesClient = Places.createClient(this);
        geocoder = new Geocoder(Editevent.this, Locale.getDefault());

        Intent intent = getIntent();
        eventId = intent.getStringExtra("eventId");
        etEventname = findViewById(R.id.etEventname);
        startDate = findViewById(R.id.startDate);
        endDate = findViewById(R.id.endDate);
        sDate = findViewById(R.id.sDate);
        eDate = findViewById(R.id.eDate);
        etVenuename = findViewById(R.id.etVenuename);
        etVenueaddress = findViewById(R.id.etVenueaddress);
        etPhone = findViewById(R.id.etPhone);
        etDescription = findViewById(R.id.etDescription);
//        etTicketAmount = findViewById(R.id.etTicketAmount);
        etfullAddress = findViewById(R.id.etfullAddress);
        etCommission = findViewById(R.id.etCommission);
        spCurrency = findViewById(R.id.spCurrency);
        btnContinueEdit = findViewById(R.id.btnContinueEdit);
        btn_back = findViewById(R.id.btn_back);
        spCategory = findViewById(R.id.spCategory);

        sessionManager = new SessionManager(getApplicationContext());
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        username = sharedPreferences.getString("name", "");
        useremail = sharedPreferences.getString("email", "");

        getEventDetails();
        spCurrency();

        onclick();


    }

    private void onclick() {

        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position > 0) {
                    catid = categoryModelArrayList.get(position-1).getId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });


        etVenueaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                openSearchBar();



            }
        });

        sDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Editevent.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        sDate.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });


        eDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Editevent.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        eDate.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

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

        startDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, 0);


                DatePickerDialog datePickerDialog = new DatePickerDialog(Editevent.this, eventdate, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
                datePickerDialog.show();
            }
        });


        endDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, 0);


                DatePickerDialog datePickerDialog = new DatePickerDialog(Editevent.this, eventdate2, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
                datePickerDialog.show();
            }
        });


        spCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i > 0) {
                    currencyId = setcurrency.get(i - 1).getCurrencyId();
//                    customerName = setcustomer.get(i-1).getCustomerName();
                    Log.d(TAG, "value --->" + currencyId);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        btnContinueEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (eventLat==null){
                    actualLat = latval;
                }else {
                    actualLat = eventLat;
                }

                if (eventLong==null){
                    actualLong = longval;
                }else {
                    actualLong = eventLong;
                }

                if (catid.matches("")){
                    catoriginalid = categoryid;
                }else {
                    catoriginalid = catid;
                }

                database_helper.clearTable();
                Intent intent = new Intent(Editevent.this, Editseat.class);
                intent.putExtra("eventname", etEventname.getText().toString());
                intent.putExtra("venuename", etVenuename.getText().toString());
                intent.putExtra("venueaddress", etVenueaddress.getText().toString());
                intent.putExtra("venueaddress2", etfullAddress.getText().toString());
                intent.putExtra("phone", etPhone.getText().toString());
                intent.putExtra("startdate", sDate.getText().toString());
                intent.putExtra("eventdate", startDate.getText().toString());
                intent.putExtra("eventdate2", endDate.getText().toString());
                intent.putExtra("enddate", eDate.getText().toString());
                intent.putExtra("description", etDescription.getText().toString());
                intent.putExtra("currencyId", "1");
                intent.putExtra("amount", "123");
                intent.putExtra("comission", etCommission.getText().toString());
                intent.putExtra("eventimage", eventimage);
                intent.putExtra("eventid", eventId);
                intent.putExtra("eventLat", actualLat);
                intent.putExtra("eventLong", actualLong);
                intent.putExtra("youtube", youtubelink);
                intent.putExtra("gallery_data", response2);
                intent.putExtra("catid", catoriginalid);
                startActivity(intent);



            }
        });


    }





    private void eventdateupdateLabel(){
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        startDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void eventdateupdateLabel2(){
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        endDate.setText(sdf.format(myCalendar.getTime()));
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
                Double lat = place.getLatLng().latitude;
                Double lon = place.getLatLng().longitude;
                String address = place.getName();
                try {
                    addresses = geocoder.getFromLocation(lat, lon, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (addresses != null && addresses.size() > 0) {
                    fulladdress = addresses.get(0).getAddressLine(0);
                }else{
                    Toast.makeText(Editevent.this,"Unable to get the location please try again",Toast.LENGTH_LONG).show();
                }

                eventLat = lati;
                eventLong = longi;
                etVenueaddress.setText(address);
                etfullAddress.setText(fulladdress);

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

    public void getEventDetails() {

        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {


            showProgressDialog();

            JSONObject params = new JSONObject();

            try {
                params.put("event_id", eventId);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Allurl.GetEventDetails, params, response -> {

                Log.i("Response-->", String.valueOf(response));

                try {
                    JSONObject result = new JSONObject(String.valueOf(response));
                    response2 = String.valueOf(response);
                    msg = result.getString("message");
                    Log.d(TAG, "msg-->" + msg);
                    String stat = result.getString("stat");
                    if (stat.equals("succ")) {

//                        Toast.makeText(Editevent.this, msg, Toast.LENGTH_SHORT).show();

                        JSONObject userdeatisObj = result.getJSONObject("data");
                        JSONObject venueObj = userdeatisObj.getJSONObject("venue");
                        JSONArray gallery_data = userdeatisObj.getJSONArray("gallery");
                        categoryarry = userdeatisObj.getJSONArray("category");
                        if (categoryarry.length()>0) {
                            for (int i = 0; i < categoryarry.length(); i++) {
                                JSONObject catobj = categoryarry.getJSONObject(0);
                                categoryid = catobj.getString("id");
                                catName = catobj.getString("category_name");
                            }
                            spcat();
                        }else {

                            spcat2();
                        }

                        id = userdeatisObj.getInt("id");
                        userid = userdeatisObj.getInt("user_id");
                        eventname = userdeatisObj.getString("event_name");
                        postedby = userdeatisObj.getString("posted_by");
                        if (!userdeatisObj.isNull("youtube_links")) {
                            youtubelink = userdeatisObj.getString("youtube_links");
                        } else {
                            youtubelink = "";
                        }
                        if (!userdeatisObj.isNull("event_image")) {
                            eventimage = userdeatisObj.getString("event_image");
                        } else {
                            eventimage = "";
                        }
                        freestat = userdeatisObj.getString("free_stat");
                        social = userdeatisObj.getString("social_links");
                        comission = userdeatisObj.getString("event_commission");
                        currencyid = userdeatisObj.getString("currency_id");
                        eventdate = userdeatisObj.getString("event_date");
                        stime = userdeatisObj.getString("event_stime");
                        etime = userdeatisObj.getString("event_etime");
                        minprice = userdeatisObj.getString("min_price");
                        maxprice = userdeatisObj.getString("max_price");
                        address = userdeatisObj.getString("event_address");
                        longval = userdeatisObj.getString("event_long_val");
                        latval = userdeatisObj.getString("event_lat_val");
                        description = userdeatisObj.getString("event_description");
                        favstatus = userdeatisObj.getInt("fav_status");
                        phonenumber = userdeatisObj.getString("helpline_no");
                        venuename = venueObj.getString("venue_name");
                        venueaddress = venueObj.getString("venue_location");
                        Enddate = userdeatisObj.getString("event_edate");
                        if (!userdeatisObj.isNull("event_address2")) {
                             venueaddress2 =  userdeatisObj.getString("event_address2");
                        } else {
                            venueaddress2 = "";
                        }



                        etEventname.setText(eventname);
                        startDate.setText(eventdate);
                        sDate.setText(stime);
                        eDate.setText(etime);
                        etVenuename.setText(venuename);
                        etVenueaddress.setText(venueaddress);
                        etPhone.setText(phonenumber);
                        etDescription.setText(description);
//                        etTicketAmount.setText(maxprice);
                        etCommission.setText(comission);
                        endDate.setText(Enddate);
                        etfullAddress.setText(venueaddress2);



                    } else {

                        hideProgressDialog();
                        Log.d(TAG, "unsuccessfull - " + "Error");
//                        Toast.makeText(Editevent.this, msg, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                hideProgressDialog();

                //TODO: handle success
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                    Toast.makeText(Editevent.this, error.toString(), Toast.LENGTH_SHORT).show();

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

    private void spCurrency() {

        showProgressDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Allurl.GetCurrency,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("CurrencyResponse-->", response);
                        currency.clear();
                        setcurrency.clear();
                        currency.add("Select Currrency");

                        try {

                            JSONObject object = new JSONObject(response);
                            msg = object.getString("message");
                            String stat = object.getString("stat");
                            if (stat.equals("succ")) {
                                JSONArray jsonArray = object.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject currencyObj = jsonArray.getJSONObject(i);
                                    currencyid = currencyObj.getString("id");
                                    String currency_name = currencyObj.getString("currency_name");
                                    currency.add(currency_name);
                                    CurrencyModel currencyModel = new CurrencyModel(currency_name, currencyid);
                                    setcurrency.add(currencyModel);
                                }

                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                        (Editevent.this, android.R.layout.simple_spinner_dropdown_item, currency);
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spCurrency.setAdapter(spinnerArrayAdapter);
                                if (currencyid.equals("1")) {
                                    spCurrency.setSelection(1);
                                } else {
                                    spCurrency.setSelection(0);
                                }


                            } else {

                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(Editevent.this, e.toString(), Toast.LENGTH_LONG).show();

                        }

                        hideProgressDialog();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                hideProgressDialog();
                Toast.makeText(Editevent.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", token);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(Editevent.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }

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
            category.add(catName);


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
                            (Editevent.this, android.R.layout.simple_spinner_dropdown_item, category);
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spCategory.setAdapter(spinnerArrayAdapter);

                } else {

                    Log.d(TAG, "unsuccessfull - " + "Error");
                    Toast.makeText(Editevent.this, "invalid", Toast.LENGTH_SHORT).show();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            hideProgressDialog();
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                hideProgressDialog();
                Toast.makeText(Editevent.this, error.toString(), Toast.LENGTH_SHORT).show();
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

    public void spcat2() {

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
            category.add("Please Select");


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
                            (Editevent.this, android.R.layout.simple_spinner_dropdown_item, category);
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spCategory.setAdapter(spinnerArrayAdapter);

                } else {

                    Log.d(TAG, "unsuccessfull - " + "Error");
                    Toast.makeText(Editevent.this, "invalid", Toast.LENGTH_SHORT).show();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            hideProgressDialog();
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                hideProgressDialog();
                Toast.makeText(Editevent.this, error.toString(), Toast.LENGTH_SHORT).show();
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