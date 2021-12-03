package com.example.tnevi;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tnevi.Adapters.BlockAdapter;
import com.example.tnevi.Utils.DatabaseHelper;
import com.example.tnevi.model.BlockModel;
import com.example.tnevi.session.SessionManager;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Arrangeseats extends AppCompatActivity {

    String eventname, venuename, venueaddress, phone, startdate, eventdate, enddate, description, token,
            ticketamount, quantity, commission, currencyid, eventLat, eventLong, feature,
            highlight, catid, eventdate2, address2, venueimagepath;
    private static final String TAG = "myapp";
    private static final String SHARED_PREFS = "sharedPrefs";
    SessionManager sessionManager;
    ImageView btn_back;
    LinearLayout eventBannerad;
    FloatingActionButton add;
    RecyclerView rv_seats;
    DatabaseHelper database_helper;
    ArrayList<BlockModel> arrayList = new ArrayList<>();
    BlockAdapter adapter;
    LinearLayout imgPrf;
    ImageView imgPrf2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrangeseats);
        Intent intent = getIntent();
        eventname = intent.getStringExtra("eventname");
        venuename = intent.getStringExtra("venuename");
        venueaddress = intent.getStringExtra("venueaddress");
        phone = intent.getStringExtra("phone");
        startdate = intent.getStringExtra("startdate");
        eventdate = intent.getStringExtra("eventdate");
        enddate = intent.getStringExtra("enddate");
        description = intent.getStringExtra("description");
//        ticketamount = intent.getStringExtra("ticketamount");
//        commission = intent.getStringExtra("commission");
        currencyid = intent.getStringExtra("currency");
        eventLat = intent.getStringExtra("eventLat");
        eventLong = intent.getStringExtra("eventLong");
//        feature = intent.getStringExtra("feature");
//        highlight = intent.getStringExtra("highlight");
        catid = intent.getStringExtra("id");
        eventdate2 = intent.getStringExtra("eventdate2");
        address2 = intent.getStringExtra("address2");
        btn_back = findViewById(R.id.btn_back);
        eventBannerad = findViewById(R.id.eventBannerad);
        rv_seats = findViewById(R.id.rv_seats);
        add = findViewById(R.id.add);
        imgPrf = findViewById(R.id.imgPrf);
        imgPrf2 = findViewById(R.id.imgPrf2);
        database_helper = new DatabaseHelper(this);
        displayNotes();


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });

        imgPrf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                showFileChooser();

            }
        });


        imgPrf2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showFileChooser();
            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        sessionManager = new SessionManager(getApplicationContext());
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");


        eventBannerad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (arrayList.size()==0){
                    Toast.makeText(getApplicationContext(), "Please Add seats to continue!", Toast.LENGTH_SHORT).show();
                }else {

                    Intent intent = new Intent(Arrangeseats.this, Eventcost.class);
                    intent.putExtra("eventname", eventname);
                    intent.putExtra("venuename", venuename);
                    intent.putExtra("venueaddress", venueaddress);
                    intent.putExtra("phone", phone);
                    intent.putExtra("startdate", startdate);
                    intent.putExtra("eventdate", eventdate);
                    intent.putExtra("enddate", enddate);
                    intent.putExtra("description", description);
                    intent.putExtra("currency", currencyid);
//                    intent.putExtra("ticketamount", ticketamount);
//                    intent.putExtra("commission", commission);
                    intent.putExtra("eventLat", eventLat);
                    intent.putExtra("eventLong", eventLong);
//                    intent.putExtra("feature", feature);
//                    intent.putExtra("highlight", highlight);
                    intent.putExtra("id", catid);
                    intent.putExtra("eventdate2", eventdate2);
                    intent.putExtra("address2", address2);
                    intent.putExtra("venueimagepath", venueimagepath);
                    intent.putExtra("seatObject", createJson());
                    startActivity(intent);

                }



            }
        });


    }

    private void showFileChooser() {
        ImagePicker.Companion.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start();
    }


    private String getRealPathFromURI(Uri contentURI) {
        String filePath;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            filePath = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            filePath = cursor.getString(idx);
            cursor.close();
        }
        return filePath;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            Uri filePath = data.getData();
            venueimagepath = getRealPathFromURI(filePath);

            Log.d(TAG, "path-->"+venueimagepath);

            imgPrf.setVisibility(View.GONE);
            imgPrf2.setVisibility(View.VISIBLE);
            imgPrf2.setImageURI(filePath);

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.RESULT_ERROR, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }

    }


    //display notes list
    public void displayNotes() {
        arrayList.addAll(database_helper.getNotes());
        rv_seats.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rv_seats.setItemAnimator(new DefaultItemAnimator());
        adapter = new BlockAdapter(getApplicationContext(), this, arrayList);
        rv_seats.setAdapter(adapter);
    }


    public void showDialog() {
        final EditText etBlockname, etNumberofrows, etNoseats, etSeatprice;
        Button submit;
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        dialog.setContentView(R.layout.dailouge);
        params.copyFrom(dialog.getWindow().getAttributes());
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(params);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        etBlockname = (EditText) dialog.findViewById(R.id.etBlockname);
        etNumberofrows = (EditText) dialog.findViewById(R.id.etNumberofrows);
        etNoseats = (EditText) dialog.findViewById(R.id.etNoseats);
        etSeatprice = (EditText) dialog.findViewById(R.id.etSeatprice);

        submit = (Button) dialog.findViewById(R.id.submit);


        submit.setOnClickListener(new View.OnClickListener() {
            ;

            @Override
            public void onClick(View v) {
                if (etBlockname.getText().toString().isEmpty()) {
                    etBlockname.setError("Please Enter Block name");
                } else if (etNumberofrows.getText().toString().isEmpty()) {
                    etNumberofrows.setError("Please Enter Rows");
                } else if (etNoseats.getText().toString().isEmpty()) {
                    etNoseats.setError("Please Enter Seats");
                } else if (etSeatprice.getText().toString().isEmpty()) {
                    etSeatprice.setError("Please enter seats");
                } else {
                    database_helper.addNotes(etBlockname.getText().toString(), etNumberofrows.getText().toString(), etNoseats.getText().toString(), etSeatprice.getText().toString());
                    arrayList.clear();
                    arrayList.addAll(database_helper.getNotes());
                    adapter.notifyDataSetChanged();
                    dialog.cancel();
                }
            }
        });
    }

    public String createJson() {
        String ticketData = "[]";
        if (arrayList.size() < 1) {
            return ticketData;
        }
        try {
            JSONArray mJsonArray = new JSONArray();

            for (int i = 0; i < arrayList.size(); i++) {
                JSONObject mJsonObject = new JSONObject();
                JSONArray rowrsdata = new JSONArray();
                BlockModel model = arrayList.get(i);
                int noofrow = Integer.parseInt(model.getNumberofrows());
                for (int j = 0; j < noofrow; j++) {
                    JSONObject rowobj = new JSONObject();
                    rowobj.put("name", ("A" + (j + 1)));
                    rowobj.put("start", "1");
                    rowobj.put("end", model.getNumberofseats());
                    rowrsdata.put(rowobj);
                }
                mJsonObject.put("rows", rowrsdata);
                mJsonObject.put("name", model.getBlockname());
                mJsonObject.put("price", model.getSeatprice());
                mJsonArray.put(mJsonObject);

            }

            Log.v("Binding jsondata", mJsonArray.toString());
            ticketData = mJsonArray.toString(0);
            return ticketData;
        } catch (JSONException e) {
            e.printStackTrace();
            return ticketData;
        }

    }

}