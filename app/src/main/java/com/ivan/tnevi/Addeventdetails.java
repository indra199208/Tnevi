package com.ivan.tnevi;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tnevi.R;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Addeventdetails extends AppCompatActivity {

    LinearLayout btnAddeventdescription;
    EditText etEventname, etVenuename, etVenueaddress, etPhone, sDate, startDate, endDate, eDate, etfullAddress;
    String eventname, venuename, venueaddress, phone, startdate, eventStartdate, eventEnddate, enddate, category, eventLat, eventLong, catid;
    TextView tvCategory, btnViewall;
    final Calendar myCalendar = Calendar.getInstance();
    ImageView btn_back;
    PlacesClient placesClient;
    private static int AUTOCOMPLETE_REQUEST_CODE = 1;
    private static final String TAG = "Myapp";
    Geocoder geocoder;
    List<Address> addresses;
    String fulladdress = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addeventdetails);
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.api_key));
        }
        placesClient = Places.createClient(this);
        geocoder = new Geocoder(Addeventdetails.this, Locale.getDefault());

        Intent intent = getIntent();
        category = intent.getStringExtra("category");
        catid = intent.getStringExtra("id");
        btnAddeventdescription = findViewById(R.id.btnAddeventdescription);
        etEventname = findViewById(R.id.etEventname);
        etVenuename = findViewById(R.id.etVenuename);
        etVenueaddress = findViewById(R.id.etVenueaddress);
        etPhone = findViewById(R.id.etPhone);
        sDate = findViewById(R.id.sDate);
        startDate = findViewById(R.id.startDate);
        endDate = findViewById(R.id.endDate);
        eDate = findViewById(R.id.eDate);
        btn_back = findViewById(R.id.btn_back);
        tvCategory = findViewById(R.id.tvCategory);
        btnViewall = findViewById(R.id.btnViewall);
        etfullAddress = findViewById(R.id.etfullAddress);

        tvCategory.setText(category);

        etVenueaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                openSearchBar();


            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                onBackPressed();
            }
        });


        btnViewall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Addeventdetails.this, Allcategory.class);
                startActivity(intent);

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
                mTimePicker = new TimePickerDialog(Addeventdetails.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        sDate.setText(selectedHour + ":" + selectedMinute);
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
                mTimePicker = new TimePickerDialog(Addeventdetails.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        eDate.setText(selectedHour + ":" + selectedMinute);
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
                // TODO Auto-generated method stub
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, 0);


                DatePickerDialog datePickerDialog = new DatePickerDialog(Addeventdetails.this, eventdate, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
                datePickerDialog.show();

            }
        });


        endDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, 0);


                DatePickerDialog datePickerDialog = new DatePickerDialog(Addeventdetails.this, eventdate2, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
                datePickerDialog.show();

            }
        });


        btnAddeventdescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                gotoEventdescription();


            }
        });
    }


    private void eventdateupdateLabel() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        startDate.setText(sdf.format(myCalendar.getTime()));

    }

    private void eventdateupdateLabel2() {
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
                    Toast.makeText(Addeventdetails.this,"Unable to get the location please try again",Toast.LENGTH_LONG).show();
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


    public void gotoEventdescription() {

        eventname = etEventname.getText().toString();
        venuename = etVenuename.getText().toString();
        venueaddress = etVenueaddress.getText().toString();
        phone = etPhone.getText().toString();
        startdate = sDate.getText().toString();
        eventStartdate = startDate.getText().toString();
        eventEnddate = endDate.getText().toString();
        enddate = eDate.getText().toString();

        if (eventname.length() == 0) {

            Toast.makeText(this, "Please enter Event Name", Toast.LENGTH_SHORT).show();

        } else if (venuename.length() == 0) {

            Toast.makeText(this, "Please enter Venue Name", Toast.LENGTH_SHORT).show();

        } else if (venueaddress.length() == 0) {

            Toast.makeText(this, "Please enter Venue address Name", Toast.LENGTH_SHORT).show();


        } else if (phone.length() == 0 || phone.length() < 10) {

            Toast.makeText(this, "Please enter Valid Phone number", Toast.LENGTH_SHORT).show();


        } else if (startdate.length() == 0) {

            Toast.makeText(this, "Please Select Start Time", Toast.LENGTH_SHORT).show();

        } else if (eventStartdate.length() == 0) {

            Toast.makeText(this, "Please Select Event Start Date", Toast.LENGTH_SHORT).show();


        } else if (eventEnddate.length() == 0) {

            Toast.makeText(this, "Please Select Event End Date", Toast.LENGTH_SHORT).show();

        } else if (enddate.length() == 0) {

            Toast.makeText(this, "Please Select End Time", Toast.LENGTH_SHORT).show();

        } else {

            Intent intent = new Intent(Addeventdetails.this, Eventdescription.class);
            intent.putExtra("eventname", eventname);
            intent.putExtra("venuename", venuename);
            intent.putExtra("venueaddress", venueaddress);
            intent.putExtra("eventLat", eventLat);
            intent.putExtra("eventLong", eventLong);
            intent.putExtra("phone", phone);
            intent.putExtra("startdate", startdate);
            intent.putExtra("eventdate", eventStartdate);
            intent.putExtra("eventdate2", eventEnddate);
            intent.putExtra("enddate", enddate);
            intent.putExtra("id", catid);
            intent.putExtra("address2", etfullAddress.getText().toString());
            startActivity(intent);
        }


    }


}