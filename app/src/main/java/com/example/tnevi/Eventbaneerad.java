package com.example.tnevi;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.developers.imagezipper.ImageZipper;
import com.example.tnevi.Utils.DatabaseHelper;
import com.example.tnevi.Utils.GetRealPathFromUri;
import com.example.tnevi.retrofit.ApiClient;
import com.example.tnevi.retrofit.ApiInterface;
import com.example.tnevi.session.SessionManager;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Eventbaneerad extends AppCompatActivity {

    LinearLayout btnEventimageupload;
    String eventname, venuename, venueaddress, phone, startdate, eventdate, enddate, description, seatObject, eventdate2,
            token, ticketamount, quantity, commission, currencyid, eventLat, eventLong, venueimagepath,
            feature, highlight, youtubelink, catid, categoryid, featurevent, highlightevent, address2, freestat;
    private static final String TAG = "myapp";
    private static final String SHARED_PREFS = "sharedPrefs";
    SessionManager sessionManager;
    LinearLayout imgPrf;
    ImageView imgPrf2;
    private static final int REQUEST_WRITE_STORAGE_REQUEST_CODE = 112;
    private int PICK_IMAGE_REQUEST = 1;
    String imagepath, fileExt;
    ImageView btn_back;
    EditText etYoutubelink;
    String useremail, username, bodyInStringFormat, bodyInStringFormat2;
    DatabaseHelper database_helper;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventbaneerad);
        Intent intent = getIntent();
        eventname = intent.getStringExtra("eventname");
        venuename = intent.getStringExtra("venuename");
        venueaddress = intent.getStringExtra("venueaddress");
        phone = intent.getStringExtra("phone");
        startdate = intent.getStringExtra("startdate");
        eventdate = intent.getStringExtra("eventdate");
        enddate = intent.getStringExtra("enddate");
        description = intent.getStringExtra("description");
        ticketamount = intent.getStringExtra("ticketamount");
        commission = intent.getStringExtra("commission");
        currencyid = intent.getStringExtra("currency");
        eventLat = intent.getStringExtra("eventLat");
        eventLong = intent.getStringExtra("eventLong");
//        feature = intent.getStringExtra("feature");
//        highlight = intent.getStringExtra("highlight");
        catid = intent.getStringExtra("id");
        seatObject = intent.getStringExtra("seatObject");
        eventdate2 = intent.getStringExtra("eventdate2");
        address2 = intent.getStringExtra("address2");
        venueimagepath = intent.getStringExtra("venueimagepath");
        freestat = intent.getStringExtra("freestat");
        database_helper = new DatabaseHelper(this);

        Gson gson = new Gson();
        bodyInStringFormat = gson.toJson(seatObject);
        bodyInStringFormat2 = bodyInStringFormat.replaceAll("\\\\n", "");

        Log.d(TAG, "seatObject-->"+bodyInStringFormat2);

        imgPrf = findViewById(R.id.imgPrf);
        imgPrf2 = findViewById(R.id.imgPrf2);
        btn_back = findViewById(R.id.btn_back);
        etYoutubelink = findViewById(R.id.etYoutubelink);
        btnEventimageupload = findViewById(R.id.btnEventimageupload);

//        if (feature.equals("Yes")){
//            featurevent = "1";
//        }else {
//            featurevent = "2";
//        }
//
//        if (highlight.equals("Yes")){
//            highlightevent = "1";
//        }else {
//            highlightevent = "2";
//        }




        sessionManager = new SessionManager(getApplicationContext());
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        username = sharedPreferences.getString("name", "");
        useremail = sharedPreferences.getString("email", "");

        requestAppPermissions();
        onClick();


    }


    public void onClick(){

        btnEventimageupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                gotoEventimageupload();
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

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                onBackPressed();
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
            imagepath = getRealPathFromURI(filePath);

            Log.d(TAG, "path-->"+imagepath);

            imgPrf.setVisibility(View.GONE);
            imgPrf2.setVisibility(View.VISIBLE);
            imgPrf2.setImageURI(filePath);

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.RESULT_ERROR, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }

    }


    public void gotoEventimageupload(){

//        youtubelink = "[\\\"" + etYoutubelink.getText().toString() + "\\\"]";
        youtubelink =  etYoutubelink.getText().toString();

        categoryid= "[\\\"" + catid + "\\\"]";

        if (imagepath==null){

            imagepath="";
        }

        if (imagepath.length()==0){

            Toast.makeText(getApplicationContext(),"Please Choose an image", Toast.LENGTH_LONG).show();

        }
//
//
//        else if (youtubelink.length() == 0){
//
//            Toast.makeText(getApplicationContext(), "Enter Youtube link", Toast.LENGTH_LONG).show();
//
//
//        }
        else {

            try {
                createEvent();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }


    }



    public void createEvent() throws IOException {
        showProgressDialog();
        ApiInterface uploadAPIs = ApiClient.getRetrofitInstance().create(ApiInterface.class);

//        String blocks = "[{\\\"rows\\\":[{\\\"name\\\":\\\"A\\\",\\\"start\\\":\\\"1\\\",\\\"end\\\":\\\"10\\\"},{\\\"name\\\":\\\"B\\\",\\\"start\\\":\\\"1\\\",\\\"end\\\":\\\"10\\\"}],\\\"name\\\":\\\"p1\\\",\\\"price\\\":\\\"12\\\"},{\\\"rows\\\":[{\\\"name\\\":\\\"A\\\",\\\"start\\\":\\\"1\\\",\\\"end\\\":\\\"10\\\"}],\\\"name\\\":\\\"p1\\\",\\\"price\\\":\\\"12\\\"}]";
        String blocks1 = bodyInStringFormat2.replaceFirst("\"", "");
        String blocks2 = blocks1.replaceAll("]\"","]");
        File eventfile = new File(imagepath);
        File imageZipperFile = new ImageZipper(Eventbaneerad.this)
                .setQuality(50)
                .setMaxWidth(300)
                .setMaxHeight(300)
                .compressToFile(eventfile);

        File venuefile = new File(venueimagepath);
        File imageZipperFile2 = new ImageZipper(Eventbaneerad.this)
                .setQuality(50)
                .setMaxWidth(500)
                .setMaxHeight(500)
                .compressToFile(venuefile);

        RequestBody eventName = RequestBody.create(MediaType.parse("text/plain"), eventname);
        RequestBody eventComission = RequestBody.create(MediaType.parse("text/plain"), commission);
        RequestBody eventDescription = RequestBody.create(MediaType.parse("text/plain"), description);
        RequestBody Phone = RequestBody.create(MediaType.parse("text/plain"), phone);
        RequestBody eventDate = RequestBody.create(MediaType.parse("text/plain"), eventdate);
        RequestBody eventDate2 = RequestBody.create(MediaType.parse("text/plain"), eventdate2);
        RequestBody eventStime = RequestBody.create(MediaType.parse("text/plain"), startdate);
        RequestBody eventEtime = RequestBody.create(MediaType.parse("text/plain"), enddate);
        RequestBody currency = RequestBody.create(MediaType.parse("text/plain"), currencyid);
        RequestBody freeState = RequestBody.create(MediaType.parse("text/plain"), freestat);
        RequestBody planId = RequestBody.create(MediaType.parse("text/plain"), "1");
        RequestBody adStart = RequestBody.create(MediaType.parse("text/plain"), "08-04-2021");
        RequestBody adEnd = RequestBody.create(MediaType.parse("text/plain"), "10-04-2021");
        RequestBody TranId = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody invId = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody Email = RequestBody.create(MediaType.parse("text/plain"), useremail);
        RequestBody Amount = RequestBody.create(MediaType.parse("text/plain"), ticketamount);
        RequestBody postedBy = RequestBody.create(MediaType.parse("text/plain"), username);

        RequestBody imagebody = RequestBody.create(MediaType.parse("image/jpg"), imageZipperFile);
        MultipartBody.Part imagemultipart = MultipartBody.Part.createFormData("event_image", "file.jpg", imagebody);

        RequestBody venueimagebody = RequestBody.create(MediaType.parse("image/jpg"), imageZipperFile2);
        MultipartBody.Part venueimagemultipart = MultipartBody.Part.createFormData("stadium_image", "file.jpg", venueimagebody);

        RequestBody Categories = RequestBody.create(MediaType.parse("text/plain"), categoryid);
        RequestBody socialLinks = RequestBody.create(MediaType.parse("text/plain"), "[\\\"fb.com\\\"]");
        RequestBody videoLinks = RequestBody.create(MediaType.parse("text/plain"), youtubelink);
        RequestBody venueName = RequestBody.create(MediaType.parse("text/plain"), venuename);
        RequestBody venueAddress = RequestBody.create(MediaType.parse("text/plain"), venueaddress);
        RequestBody venueAddress2 = RequestBody.create(MediaType.parse("text/plain"), address2);
        RequestBody EventLat = RequestBody.create(MediaType.parse("text/plain"), eventLat);
        RequestBody EventLong = RequestBody.create(MediaType.parse("text/plain"), eventLong);
        RequestBody Blocks = RequestBody.create(MediaType.parse("text/plain"), blocks2);
//        RequestBody featured_event = RequestBody.create(MediaType.parse("text/plain"), featurevent);
//        RequestBody highlight_event = RequestBody.create(MediaType.parse("text/plain"), highlightevent);


        Call<ResponseBody> call = uploadAPIs.createEvent(token, eventName, eventComission, eventDescription, Phone, eventDate, eventDate2, eventStime,
                eventEtime, currency, freeState, planId, adStart, adEnd, TranId, invId, Email, Amount, postedBy, imagemultipart, venueimagemultipart, Categories, socialLinks,
                videoLinks, venueName, venueAddress, venueAddress2, EventLat, EventLong, Blocks);


        call.enqueue(new Callback<ResponseBody>() {


            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    if (response.body() != null) {
                        String responsetsr = response.body().string();
                        Log.v("responsetsr", responsetsr);
                        JSONObject result = new JSONObject(responsetsr);
                        String msg = result.getString("message");
                        String stat = result.getString("stat");
                        if (stat.equals("succ")) {

                            JSONObject data = result.getJSONObject("data");
                            String eventid = data.getString("id");
                            database_helper.clearTable();
                            Toast.makeText(Eventbaneerad.this, msg, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Eventbaneerad.this, EventimageuploadActivity.class);
                            intent.putExtra("eventid", eventid);
                            startActivity(intent);

                        } else {

                            Toast.makeText(Eventbaneerad.this, "Event not created successfully!", Toast.LENGTH_SHORT).show();

                        }


                    } else {
                        if (response.errorBody() != null) {
                            String errorResponse = response.errorBody().string();
                            Log.v("errorResponse", errorResponse);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                hideProgressDialog();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Log.v("faul", t.getMessage());
                hideProgressDialog();

            }

        });


    }





        // RUN TIME PERMISSIONS FOR READ AND WRITE

    private void requestAppPermissions() {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }

        if (hasReadPermissions() && hasWritePermissions()) {
            return;
        }

        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, REQUEST_WRITE_STORAGE_REQUEST_CODE); // your request code
    }

    private boolean hasReadPermissions() {
        return (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    private boolean hasWritePermissions() {
        return (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
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