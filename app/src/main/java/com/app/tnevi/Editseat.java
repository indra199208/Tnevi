package com.app.tnevi;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.tnevi.Adapters.BlockAdapter;

import com.app.tnevi.Utils.DatabaseHelper;
import com.app.tnevi.model.BlockModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Editseat extends AppCompatActivity {

    String eventname, venuename, venueaddress, phone, startdate, eventdate, enddate, description,
            currencyId, amount, comission, eventimage, eventLat, eventLong, youtubelink, response2,
            eventId, catid, eventdate2, venueaddress2, token;
    DatabaseHelper database_helper;
    RecyclerView rv_editseats;
    ArrayList<BlockModel> arrayList = new ArrayList<>();
    BlockAdapter adapter;
    FloatingActionButton add;
    ImageView btn_back;
    LinearLayout btnContinueEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editseat);
        database_helper = new DatabaseHelper(this);
        Intent intent = getIntent();
        eventname = intent.getStringExtra("eventname");
        venuename = intent.getStringExtra("venuename");
        venueaddress = intent.getStringExtra("venueaddress");
        phone = intent.getStringExtra("phone");
        startdate = intent.getStringExtra("startdate");
        eventdate = intent.getStringExtra("eventdate");
        enddate = intent.getStringExtra("enddate");
        description = intent.getStringExtra("description");
        currencyId = intent.getStringExtra("currencyId");
        amount = intent.getStringExtra("amount");
        comission = intent.getStringExtra("comission");
        eventimage = intent.getStringExtra("eventimage");
        eventId = intent.getStringExtra("eventid");
        eventLat = intent.getStringExtra("eventLat");
        eventLong = intent.getStringExtra("eventLong");
        youtubelink = intent.getStringExtra("youtube");
        response2 = intent.getStringExtra("gallery_data");
        catid = intent.getStringExtra("catid");
        eventdate2 = intent.getStringExtra("eventdate2");
        venueaddress2 = intent.getStringExtra("venueaddress2");

        rv_editseats = findViewById(R.id.rv_editseats);
        add = findViewById(R.id.add);
        btn_back = findViewById(R.id.btn_back);
        btnContinueEdit = findViewById(R.id.btnContinueEdit);



        addSeatInDataBase();



        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });


        btnContinueEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Editseat.this, Editevent2.class);
                intent.putExtra("eventname", eventname);
                intent.putExtra("venuename", venuename);
                intent.putExtra("venueaddress", venueaddress);
                intent.putExtra("venueaddress2", venueaddress2);
                intent.putExtra("phone", phone);
                intent.putExtra("startdate", startdate);
                intent.putExtra("eventdate", eventdate);
                intent.putExtra("enddate", enddate);
                intent.putExtra("description", description);
                intent.putExtra("currencyId", "1");
                intent.putExtra("amount", amount);
                intent.putExtra("comission", comission);
                intent.putExtra("eventimage", eventimage);
                intent.putExtra("eventid", eventId);
                intent.putExtra("eventLat", eventLat);
                intent.putExtra("eventLong", eventLong);
                intent.putExtra("youtube", youtubelink);
                intent.putExtra("gallery_data", response2);
                intent.putExtra("catid", catid);
                intent.putExtra("eventdate2", eventdate2);
                intent.putExtra("seatObject", createJson());
                startActivity(intent);

            }
        });
    }







    private void addSeatInDataBase() {
        if (response2 != null) {
            try {
                JSONObject mJsonObject = new JSONObject(response2);
                JSONObject venueobj = mJsonObject.getJSONObject("data").getJSONObject("venue");
                JSONArray blocksarr = venueobj.getJSONArray("blocks");
                for (int i = 0; i < blocksarr.length(); i++) {

                    JSONArray blockrowsarr = blocksarr.getJSONObject(i).getJSONArray("blockrows");
                    String block_name = blocksarr.getJSONObject(i).getString("block_name");
                    String no_of_rows = blocksarr.getJSONObject(i).getString("no_of_rows");
                    String row_end = blockrowsarr.length() > 0 ? blockrowsarr.getJSONObject(0).getString("row_end") : "";
                    JSONArray seats = blockrowsarr.getJSONObject(0).getJSONArray("seat");
                    String seat_price = seats.length() > 0 ? seats.getJSONObject(0).getString("seat_price") : "";
                    database_helper.addNotes(block_name, no_of_rows, row_end, seat_price);
                    // blockrows
                }

                displayNotes();

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


        Log.v("inseted data", database_helper.getNotes().size() + "");
    }







    //display notes list
    public void displayNotes() {
        arrayList.addAll(database_helper.getNotes());
        rv_editseats.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rv_editseats.setItemAnimator(new DefaultItemAnimator());
        adapter = new BlockAdapter(getApplicationContext(), this, arrayList);
        rv_editseats.setAdapter(adapter);
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