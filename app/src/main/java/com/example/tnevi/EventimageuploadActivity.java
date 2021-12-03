package com.example.tnevi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.developers.imagezipper.ImageZipper;
import com.example.tnevi.Utils.GetRealPathFromUri;
import com.example.tnevi.retrofit.ApiClient;
import com.example.tnevi.retrofit.ApiInterface;
import com.example.tnevi.session.SessionManager;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class


EventimageuploadActivity extends AppCompatActivity {

    LinearLayout btnEventsubmit;
    String eventname, venuename, venueaddress, phone, startdate, eventdate, enddate, description, token,
            ticketamount, quantity, commission, currencyid, imagepath, eventLat, eventLong, feature, highlight, eventid;
    ImageView btn_back;
    private static final String TAG = "myapp";
    private static final String SHARED_PREFS = "sharedPrefs";
    SessionManager sessionManager;
    String useremail, username;
    String image_path = "";
    FusedLocationProviderClient fusedLocationProviderClient;
    private final int REQUEST_CODE_READ_STORAGE = 2;
    ImageView imgPrf, imgPrf2, imgPrf3, imgPrf4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventimageupload);
        Intent intent = getIntent();
        eventid = intent.getStringExtra("eventid");
        btnEventsubmit = findViewById(R.id.btnEventsubmit);
        btn_back = findViewById(R.id.btn_back);
        imgPrf = findViewById(R.id.imgPrf);
        imgPrf2 = findViewById(R.id.imgPrf2);
        imgPrf3 = findViewById(R.id.imgPrf3);
        imgPrf4 = findViewById(R.id.imgPrf4);


        sessionManager = new SessionManager(getApplicationContext());
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        username = sharedPreferences.getString("name", "");
        useremail = sharedPreferences.getString("email", "");
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        onClick();


    }

    public void onClick() {

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                onBackPressed();
            }
        });


        imgPrf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImagePicker.Companion.with(EventimageuploadActivity.this)
                        .crop()
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .start();

                image_path = "1";
            }
        });


        imgPrf2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImagePicker.Companion.with(EventimageuploadActivity.this)
                        .crop()
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .start();

                image_path = "2";

            }
        });


        imgPrf3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImagePicker.Companion.with(EventimageuploadActivity.this)
                        .crop()
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .start();

                image_path = "3";


            }
        });


        imgPrf4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ImagePicker.Companion.with(EventimageuploadActivity.this)
                        .crop()
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .start();

                image_path = "4";

            }
        });


        btnEventsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(EventimageuploadActivity.this, Adcongrats.class);
                startActivity(intent);

            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "imagepath-->" + image_path);

        if (requestCode == 2404 && resultCode == Activity.RESULT_OK) {
            Uri fileUri = data.getData();
            try {

                switch (image_path) {
                    case "1":
                        uploadToServer(GetRealPathFromUri.getPathFromUri(EventimageuploadActivity.this, fileUri));
                        break;
                    case "2":
                        uploadToServer2(GetRealPathFromUri.getPathFromUri(EventimageuploadActivity.this, fileUri));
                        break;
                    case "3":
                        uploadToServer3(GetRealPathFromUri.getPathFromUri(EventimageuploadActivity.this, fileUri));
                        break;
                    case "4":
                        uploadToServer4(GetRealPathFromUri.getPathFromUri(EventimageuploadActivity.this, fileUri));
                        break;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.RESULT_ERROR, Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }


    }


    private void uploadToServer(String fileUri) throws IOException {
        showProgressDialog();
        Retrofit retrofit = ApiClient.getRetrofitInstance();
        ApiInterface uploadAPIs = retrofit.create(ApiInterface.class);
        File file = new File(fileUri);
        File imageZipperFile = new ImageZipper(EventimageuploadActivity.this)
                .setQuality(50)
                .setMaxWidth(300)
                .setMaxHeight(300)
                .compressToFile(file);

        RequestBody event_id = RequestBody.create(MediaType.parse("text/plain"), eventid);
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/jpg"), imageZipperFile);
        MultipartBody.Part part = MultipartBody.Part.createFormData("gallery_image", imageZipperFile.getName(), fileReqBody);


        Call<ResponseBody> mcall = uploadAPIs.uploadGallery(token, event_id, part);
        mcall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    if (response.body() != null) {
                        String result = response.body().string();
                        Log.v("response2-->", result);
                        JSONObject mjonsresponse = new JSONObject(result);
                        String msg = mjonsresponse.getString("message");
                        String stat = mjonsresponse.getString("stat");
                        if (stat.equals("succ")) {
                            Toast.makeText(EventimageuploadActivity.this, msg, Toast.LENGTH_SHORT).show();
                            JSONObject userdeatisObj = mjonsresponse.getJSONObject("data");
                            String picurl = userdeatisObj.getString("file_name");
                            Log.d(TAG, "picurl-->" + picurl);


                            Log.v("picurl-->", picurl);
                            Glide.with(EventimageuploadActivity.this)
                                    .load("http://dev8.ivantechnology.in/tnevi/storage/app/" + picurl)
                                    .placeholder(R.drawable.loading)
                                    .into(imgPrf);

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                hideProgressDialog();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.v("onFailure", t.getMessage());
            }
        });
    }

    private void uploadToServer2(String fileUri) throws IOException {
        showProgressDialog();
        Retrofit retrofit = ApiClient.getRetrofitInstance();
        ApiInterface uploadAPIs = retrofit.create(ApiInterface.class);
        File file = new File(fileUri);
        File imageZipperFile = new ImageZipper(EventimageuploadActivity.this)
                .setQuality(50)
                .setMaxWidth(300)
                .setMaxHeight(300)
                .compressToFile(file);

        RequestBody event_id = RequestBody.create(MediaType.parse("text/plain"), eventid);
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/jpg"), imageZipperFile);
        MultipartBody.Part part = MultipartBody.Part.createFormData("gallery_image", imageZipperFile.getName(), fileReqBody);


        Call<ResponseBody> mcall = uploadAPIs.uploadGallery(token, event_id, part);
        mcall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    if (response.body() != null) {
                        String result = response.body().string();
                        Log.v("response2-->", result);
                        JSONObject mjonsresponse = new JSONObject(result);
                        String msg = mjonsresponse.getString("message");
                        String stat = mjonsresponse.getString("stat");
                        if (stat.equals("succ")) {
                            Toast.makeText(EventimageuploadActivity.this, msg, Toast.LENGTH_SHORT).show();
                            JSONObject userdeatisObj = mjonsresponse.getJSONObject("data");
                            String picurl = userdeatisObj.getString("file_name");
                            Log.d(TAG, "picurl-->" + picurl);


                            Log.v("picurl-->", picurl);
                            Glide.with(EventimageuploadActivity.this)
                                    .load("http://dev8.ivantechnology.in/tnevi/storage/app/" + picurl)
                                    .placeholder(R.drawable.loading)
                                    .into(imgPrf2);

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                hideProgressDialog();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.v("onFailure", t.getMessage());
            }
        });
    }

    private void uploadToServer3(String fileUri) throws IOException {
        showProgressDialog();
        Retrofit retrofit = ApiClient.getRetrofitInstance();
        ApiInterface uploadAPIs = retrofit.create(ApiInterface.class);
        File file = new File(fileUri);
        File imageZipperFile = new ImageZipper(EventimageuploadActivity.this)
                .setQuality(50)
                .setMaxWidth(300)
                .setMaxHeight(300)
                .compressToFile(file);

        RequestBody event_id = RequestBody.create(MediaType.parse("text/plain"), eventid);
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/jpg"), imageZipperFile);
        MultipartBody.Part part = MultipartBody.Part.createFormData("gallery_image", imageZipperFile.getName(), fileReqBody);


        Call<ResponseBody> mcall = uploadAPIs.uploadGallery(token, event_id, part);
        mcall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    if (response.body() != null) {
                        String result = response.body().string();
                        Log.v("response2-->", result);
                        JSONObject mjonsresponse = new JSONObject(result);
                        String msg = mjonsresponse.getString("message");
                        String stat = mjonsresponse.getString("stat");
                        if (stat.equals("succ")) {
                            Toast.makeText(EventimageuploadActivity.this, msg, Toast.LENGTH_SHORT).show();
                            JSONObject userdeatisObj = mjonsresponse.getJSONObject("data");
                            String picurl = userdeatisObj.getString("file_name");
                            Log.d(TAG, "picurl-->" + picurl);


                            Log.v("picurl-->", picurl);
                            Glide.with(EventimageuploadActivity.this)
                                    .load("http://dev8.ivantechnology.in/tnevi/storage/app/" + picurl)
                                    .placeholder(R.drawable.loading)
                                    .into(imgPrf3);

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                hideProgressDialog();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.v("onFailure", t.getMessage());
            }
        });
    }

    private void uploadToServer4(String fileUri) throws IOException {
        showProgressDialog();
        Retrofit retrofit = ApiClient.getRetrofitInstance();
        ApiInterface uploadAPIs = retrofit.create(ApiInterface.class);
        File file = new File(fileUri);
        File imageZipperFile = new ImageZipper(EventimageuploadActivity.this)
                .setQuality(50)
                .setMaxWidth(300)
                .setMaxHeight(300)
                .compressToFile(file);

        RequestBody event_id = RequestBody.create(MediaType.parse("text/plain"), eventid);
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/jpg"), imageZipperFile);
        MultipartBody.Part part = MultipartBody.Part.createFormData("gallery_image", imageZipperFile.getName(), fileReqBody);


        Call<ResponseBody> mcall = uploadAPIs.uploadGallery(token, event_id, part);
        mcall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    if (response.body() != null) {
                        String result = response.body().string();
                        Log.v("response2-->", result);
                        JSONObject mjonsresponse = new JSONObject(result);
                        String msg = mjonsresponse.getString("message");
                        String stat = mjonsresponse.getString("stat");
                        if (stat.equals("succ")) {
                            Toast.makeText(EventimageuploadActivity.this, msg, Toast.LENGTH_SHORT).show();
                            JSONObject userdeatisObj = mjonsresponse.getJSONObject("data");
                            String picurl = userdeatisObj.getString("file_name");
                            Log.d(TAG, "picurl-->" + picurl);


                            Log.v("picurl-->", picurl);
                            Glide.with(EventimageuploadActivity.this)
                                    .load("http://dev8.ivantechnology.in/tnevi/storage/app/" + picurl)
                                    .placeholder(R.drawable.loading)
                                    .into(imgPrf4);

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                hideProgressDialog();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.v("onFailure", t.getMessage());
            }
        });
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