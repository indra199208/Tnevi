package com.example.tnevi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.developers.imagezipper.ImageZipper;
import com.example.tnevi.Adapters.GalleryEditAdapter;
import com.example.tnevi.Allurl.Allurl;
import com.example.tnevi.Utils.DatabaseHelper;
import com.example.tnevi.Utils.GetRealPathFromUri;
import com.example.tnevi.Utils.ItemOffsetDecoration;
import com.example.tnevi.internet.CheckConnectivity;
import com.example.tnevi.model.GalleryModel;
import com.example.tnevi.retrofit.ApiClient;
import com.example.tnevi.retrofit.ApiInterface;
import com.example.tnevi.session.SessionManager;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Editevent2 extends AppCompatActivity {

    String eventname, venuename, venueaddress, phone, startdate, eventdate, enddate, description,
            currencyId, amount, comission, eventimage, eventLat,
            eventLong, youtubelink, response2, seatObject, catid, categoryid, eventdate2, venueaddress2;
    ImageView imgPrf, btn_back, img1, img2, img3, img4;
    private static final String TAG = "myapp";
    private static final String SHARED_PREFS = "sharedPrefs";
    SessionManager sessionManager;
    String token;
    String imagefile, bodyInStringFormat, bodyInStringFormat2;
    LinearLayout btnUpdate;
    String username, useremail, eventId;
    EditText etYoutubelink;
    private ArrayList<GalleryModel> galleryModelArrayList;
    private GalleryEditAdapter galleryEditAdapter;
    RecyclerView rv_galleryedit;
    LinearLayout ll_addGallery;
    String image_path = "";
    DatabaseHelper database_helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editevent2);
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
        seatObject = intent.getStringExtra("seatObject");
        catid = intent.getStringExtra("catid");
        eventdate2 = intent.getStringExtra("eventdate2");
        venueaddress2 = intent.getStringExtra("venueaddress2");
        rv_galleryedit = findViewById(R.id.rv_galleryedit);
        btn_back = findViewById(R.id.btn_back);
        ll_addGallery = findViewById(R.id.ll_addGallery);
        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);
        img4 = findViewById(R.id.img4);
        imgPrf = findViewById(R.id.imgPrf);
        btnUpdate = findViewById(R.id.btnUpdate);
        etYoutubelink = findViewById(R.id.etYoutubelink);
        etYoutubelink.setText(youtubelink);

        Gson gson = new Gson();
        bodyInStringFormat = gson.toJson(seatObject);
        bodyInStringFormat2 = bodyInStringFormat.replaceAll("\\\\n", "");

        Log.d(TAG, "seatObject-->"+bodyInStringFormat2);

        sessionManager = new SessionManager(getApplicationContext());
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        username = sharedPreferences.getString("name", "");
        useremail = sharedPreferences.getString("email", "");

        Glide.with(Editevent2.this)
                .load("http://dev8.ivantechnology.in/tnevi/storage/app/" + eventimage)
                .placeholder(R.drawable.ic_add)
                .into(imgPrf);


        galleryimage();

        imgPrf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                image_path = "0";
                showFileChooser();

            }
        });


        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImagePicker.Companion.with(Editevent2.this)
                        .crop()
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .start();

                image_path = "1";
            }
        });


        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImagePicker.Companion.with(Editevent2.this)
                        .crop()
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .start();

                image_path = "2";

            }
        });


        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImagePicker.Companion.with(Editevent2.this)
                        .crop()
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .start();

                image_path = "3";


            }
        });


        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ImagePicker.Companion.with(Editevent2.this)
                        .crop()
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .start();

                image_path = "4";

            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                youtubelink = etYoutubelink.getText().toString();
//                categoryid= "[\\\"" + catid + "\\\"]";
                categoryid =  "[\""+catid+"\"]";


                if (imagefile == null) {

                    try {
                        updateEvent2();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {

                    try {
                        updateEvent();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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

        if (requestCode == 2404 && resultCode == Activity.RESULT_OK) {
            Uri fileUri = data.getData();
            try {

                switch (image_path) {
                    case "0":
                        Uri filePath = data.getData();
                        imagefile = getRealPathFromURI(filePath);
                        imgPrf.setImageURI(filePath);
                        break;
                    case "1":
                        uploadToServer(GetRealPathFromUri.getPathFromUri(Editevent2.this, fileUri));
                        break;
                    case "2":
                        uploadToServer2(GetRealPathFromUri.getPathFromUri(Editevent2.this, fileUri));
                        break;
                    case "3":
                        uploadToServer3(GetRealPathFromUri.getPathFromUri(Editevent2.this, fileUri));
                        break;
                    case "4":
                        uploadToServer4(GetRealPathFromUri.getPathFromUri(Editevent2.this, fileUri));
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

    public void galleryimage() {

        try {
            JSONObject result = new JSONObject(String.valueOf(response2));
            String msg = result.getString("message");
            Log.d(TAG, "msg-->" + msg);
            String stat = result.getString("stat");
            if (stat.equals("succ")) {

                JSONObject userdeatisObj = result.getJSONObject("data");
                galleryModelArrayList = new ArrayList<>();
                JSONArray gallery_data = userdeatisObj.getJSONArray("gallery");
                for (int i = 0; i < gallery_data.length(); i++) {
                    JSONObject galleryobj = gallery_data.getJSONObject(i);
                    GalleryModel galleryModel = new GalleryModel();
                    galleryModel.setFile_name(galleryobj.getString("file_name"));
                    galleryModel.setEvent_id(galleryobj.getString("event_id"));
                    galleryModel.setId(galleryobj.getString("id"));
                    galleryModelArrayList.add(galleryModel);

                }

                setupRecyclerview();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void removeGallery(GalleryModel galleryModel, int pos) {

        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {

            showProgressDialog();
            JSONObject params = new JSONObject();

            try {
                params.put("event_id", galleryModel.getEvent_id());
                params.put("file_id", galleryModel.getId());

            } catch (JSONException e) {
                e.printStackTrace();
            }


            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Allurl.RemoveGallery, params, response -> {

                Log.i("Response-->", String.valueOf(response));

                galleryModelArrayList.remove(pos);
                galleryEditAdapter.notifyDataSetChanged();
                hideProgressDialog();

                //TODO: handle success
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Editevent2.this, error.toString(), Toast.LENGTH_SHORT).show();

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

            Toast.makeText(Editevent2.this, "Ooops! Internet Connection Error", Toast.LENGTH_SHORT).show();

        }

    }

    public void setupRecyclerview() {

        galleryEditAdapter = new GalleryEditAdapter(this, galleryModelArrayList);
        rv_galleryedit.setAdapter(galleryEditAdapter);
        rv_galleryedit.setLayoutManager(new GridLayoutManager(Editevent2.this, 2));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getApplicationContext(), R.dimen.photos_list_spacing6);
        rv_galleryedit.addItemDecoration(itemDecoration);

    }


    private void uploadToServer(String fileUri) throws IOException {
        showProgressDialog();
        Retrofit retrofit = ApiClient.getRetrofitInstance();
        ApiInterface uploadAPIs = retrofit.create(ApiInterface.class);
        File file = new File(fileUri);
        File imageZipperFile = new ImageZipper(Editevent2.this)
                .setQuality(50)
                .setMaxWidth(300)
                .setMaxHeight(300)
                .compressToFile(file);

        RequestBody event_id = RequestBody.create(MediaType.parse("text/plain"), eventId);
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
                            Toast.makeText(Editevent2.this, msg, Toast.LENGTH_SHORT).show();
                            JSONObject userdeatisObj = mjonsresponse.getJSONObject("data");
                            String picurl = userdeatisObj.getString("file_name");
                            Log.d(TAG, "picurl-->" + picurl);


                            Log.v("picurl-->", picurl);
                            Glide.with(Editevent2.this)
                                    .load("http://dev8.ivantechnology.in/tnevi/storage/app/" + picurl)
                                    .placeholder(R.drawable.loading)
                                    .into(img1);

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
        File imageZipperFile = new ImageZipper(Editevent2.this)
                .setQuality(50)
                .setMaxWidth(300)
                .setMaxHeight(300)
                .compressToFile(file);

        RequestBody event_id = RequestBody.create(MediaType.parse("text/plain"), eventId);
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
                            Toast.makeText(Editevent2.this, msg, Toast.LENGTH_SHORT).show();
                            JSONObject userdeatisObj = mjonsresponse.getJSONObject("data");
                            String picurl = userdeatisObj.getString("file_name");
                            Log.d(TAG, "picurl-->" + picurl);


                            Log.v("picurl-->", picurl);
                            Glide.with(Editevent2.this)
                                    .load("http://dev8.ivantechnology.in/tnevi/storage/app/" + picurl)
                                    .placeholder(R.drawable.loading)
                                    .into(img2);

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
        File imageZipperFile = new ImageZipper(Editevent2.this)
                .setQuality(50)
                .setMaxWidth(300)
                .setMaxHeight(300)
                .compressToFile(file);

        RequestBody event_id = RequestBody.create(MediaType.parse("text/plain"), eventId);
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
                            Toast.makeText(Editevent2.this, msg, Toast.LENGTH_SHORT).show();
                            JSONObject userdeatisObj = mjonsresponse.getJSONObject("data");
                            String picurl = userdeatisObj.getString("file_name");
                            Log.d(TAG, "picurl-->" + picurl);


                            Log.v("picurl-->", picurl);
                            Glide.with(Editevent2.this)
                                    .load("http://dev8.ivantechnology.in/tnevi/storage/app/" + picurl)
                                    .placeholder(R.drawable.loading)
                                    .into(img3);

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
        File imageZipperFile = new ImageZipper(Editevent2.this)
                .setQuality(50)
                .setMaxWidth(300)
                .setMaxHeight(300)
                .compressToFile(file);

        RequestBody event_id = RequestBody.create(MediaType.parse("text/plain"), eventId);
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
                            Toast.makeText(Editevent2.this, msg, Toast.LENGTH_SHORT).show();
                            JSONObject userdeatisObj = mjonsresponse.getJSONObject("data");
                            String picurl = userdeatisObj.getString("file_name");
                            Log.d(TAG, "picurl-->" + picurl);
                            Glide.with(Editevent2.this)
                                    .load("http://dev8.ivantechnology.in/tnevi/storage/app/" + picurl)
                                    .placeholder(R.drawable.loading)
                                    .into(img4);

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


    public void updateEvent() throws IOException {
        showProgressDialog();
        ApiInterface uploadAPIs = ApiClient.getRetrofitInstance().create(ApiInterface.class);
//        String blocks = "[{\\\"rows\\\":[{\\\"name\\\":\\\"A\\\",\\\"start\\\":\\\"1\\\",\\\"end\\\":\\\"10\\\"},{\\\"name\\\":\\\"B\\\",\\\"start\\\":\\\"1\\\",\\\"end\\\":\\\"10\\\"}],\\\"name\\\":\\\"p1\\\",\\\"price\\\":\\\"12\\\"},{\\\"rows\\\":[{\\\"name\\\":\\\"A\\\",\\\"start\\\":\\\"1\\\",\\\"end\\\":\\\"10\\\"}],\\\"name\\\":\\\"p1\\\",\\\"price\\\":\\\"12\\\"}]";
        String blocks1 = bodyInStringFormat2.replaceFirst("\"", "");
        String blocks2 = blocks1.replaceAll("]\"","]");
        File eventfile = new File(imagefile);
        File galleryfile = new File(imagefile);
        File imageZipperFile = new ImageZipper(Editevent2.this)
                .setQuality(50)
                .setMaxWidth(300)
                .setMaxHeight(300)
                .compressToFile(eventfile);

        RequestBody eventName = RequestBody.create(MediaType.parse("text/plain"), eventname);
        RequestBody eventid = RequestBody.create(MediaType.parse("text/plain"), eventId);
        RequestBody eventComission = RequestBody.create(MediaType.parse("text/plain"), comission);
        RequestBody eventDescription = RequestBody.create(MediaType.parse("text/plain"), description);
        RequestBody Phone = RequestBody.create(MediaType.parse("text/plain"), phone);
        RequestBody eventDate = RequestBody.create(MediaType.parse("text/plain"), eventdate);
        RequestBody eventDate2 = RequestBody.create(MediaType.parse("text/plain"), eventdate2);
        RequestBody eventStime = RequestBody.create(MediaType.parse("text/plain"), startdate);
        RequestBody eventEtime = RequestBody.create(MediaType.parse("text/plain"), enddate);
        RequestBody currency = RequestBody.create(MediaType.parse("text/plain"), currencyId);
        RequestBody Amount = RequestBody.create(MediaType.parse("text/plain"), amount);
        RequestBody postedBy = RequestBody.create(MediaType.parse("text/plain"), username);
        RequestBody imagebody = RequestBody.create(MediaType.parse("image/jpg"), imageZipperFile);
        MultipartBody.Part imagemultipart = MultipartBody.Part.createFormData("event_image", "file.jpg", imagebody);
        RequestBody Categories = RequestBody.create(MediaType.parse("text/plain"), categoryid);
        RequestBody socialLinks = RequestBody.create(MediaType.parse("text/plain"), "[\\\"fb.com\\\"]");
        RequestBody videoLinks = RequestBody.create(MediaType.parse("text/plain"), youtubelink);
        RequestBody venueName = RequestBody.create(MediaType.parse("text/plain"), venuename);
        RequestBody venueAddress = RequestBody.create(MediaType.parse("text/plain"), venueaddress);
        RequestBody venueAddress2 = RequestBody.create(MediaType.parse("text/plain"), venueaddress2);
        RequestBody EventLat = RequestBody.create(MediaType.parse("text/plain"), eventLat);
        RequestBody EventLong = RequestBody.create(MediaType.parse("text/plain"), eventLong);
        RequestBody Blocks = RequestBody.create(MediaType.parse("text/plain"), blocks2);

        Call<ResponseBody> call = uploadAPIs.updateEvent(token, eventid, eventName, eventComission, eventDescription, Phone, eventDate, eventDate2, eventStime,
                eventEtime, currency, Amount, postedBy, imagemultipart, Categories, socialLinks,
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

                            Toast.makeText(Editevent2.this, msg, Toast.LENGTH_SHORT).show();
                            database_helper.clearTable();
                            Intent intent = new Intent(Editevent2.this, Events.class);
                            startActivity(intent);

                        } else {

                            Toast.makeText(Editevent2.this, "Event not created successfully!", Toast.LENGTH_SHORT).show();

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


    public void updateEvent2() throws IOException {
        showProgressDialog();
        ApiInterface uploadAPIs = ApiClient.getRetrofitInstance().create(ApiInterface.class);
//        String blocks = "[{\\\"rows\\\":[{\\\"name\\\":\\\"A\\\",\\\"start\\\":\\\"1\\\",\\\"end\\\":\\\"10\\\"},{\\\"name\\\":\\\"B\\\",\\\"start\\\":\\\"1\\\",\\\"end\\\":\\\"10\\\"}],\\\"name\\\":\\\"p1\\\",\\\"price\\\":\\\"12\\\"},{\\\"rows\\\":[{\\\"name\\\":\\\"A\\\",\\\"start\\\":\\\"1\\\",\\\"end\\\":\\\"10\\\"}],\\\"name\\\":\\\"p1\\\",\\\"price\\\":\\\"12\\\"}]";
        String blocks1 = bodyInStringFormat2.replaceFirst("\"", "");
        String blocks2 = blocks1.replaceAll("]\"","]");
        RequestBody eventName = RequestBody.create(MediaType.parse("text/plain"), eventname);
        RequestBody eventid = RequestBody.create(MediaType.parse("text/plain"), eventId);
        RequestBody eventComission = RequestBody.create(MediaType.parse("text/plain"), comission);
        RequestBody eventDescription = RequestBody.create(MediaType.parse("text/plain"), description);
        RequestBody Phone = RequestBody.create(MediaType.parse("text/plain"), phone);
        RequestBody eventDate = RequestBody.create(MediaType.parse("text/plain"), eventdate);
        RequestBody eventDate2 = RequestBody.create(MediaType.parse("text/plain"), eventdate2);
        RequestBody eventStime = RequestBody.create(MediaType.parse("text/plain"), startdate);
        RequestBody eventEtime = RequestBody.create(MediaType.parse("text/plain"), enddate);
        RequestBody currency = RequestBody.create(MediaType.parse("text/plain"), currencyId);
        RequestBody Amount = RequestBody.create(MediaType.parse("text/plain"), amount);
        RequestBody postedBy = RequestBody.create(MediaType.parse("text/plain"), username);
//        RequestBody imagebody = RequestBody.create(MediaType.parse("image/jpg"), imageZipperFile);
//        MultipartBody.Part imagemultipart = MultipartBody.Part.createFormData("event_image", "file.jpg", imagebody);
        //RequestBody eventpart = RequestBody.create(MediaType.parse("image/jpeg"), eventfile);
        RequestBody Categories = RequestBody.create(MediaType.parse("text/plain"), categoryid);
        RequestBody socialLinks = RequestBody.create(MediaType.parse("text/plain"), "[\\\"fb.com\\\"]");
        RequestBody videoLinks = RequestBody.create(MediaType.parse("text/plain"), youtubelink);
//        RequestBody galleryimagebody = RequestBody.create(MediaType.parse("*/*"), galleryZipperFile);
//        MultipartBody.Part gallerymultipart = MultipartBody.Part.createFormData("event_gallery", galleryZipperFile.getName(), galleryimagebody);
        RequestBody venueName = RequestBody.create(MediaType.parse("text/plain"), venuename);
        RequestBody venueAddress = RequestBody.create(MediaType.parse("text/plain"), venueaddress);
        RequestBody venueAddress2 = RequestBody.create(MediaType.parse("text/plain"), venueaddress2);
        RequestBody EventLat = RequestBody.create(MediaType.parse("text/plain"), eventLat);
        RequestBody EventLong = RequestBody.create(MediaType.parse("text/plain"), eventLong);
        RequestBody Blocks = RequestBody.create(MediaType.parse("text/plain"), blocks2);

        Call<ResponseBody> call = uploadAPIs.updateEvent2(token, eventid, eventName, eventComission, eventDescription, Phone, eventDate, eventDate2, eventStime,
                eventEtime, currency, Amount, postedBy, Categories, socialLinks,
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

                            Toast.makeText(Editevent2.this, msg, Toast.LENGTH_SHORT).show();
                            database_helper.clearTable();
                            Intent intent = new Intent(Editevent2.this, Events.class);
                            startActivity(intent);

                        } else {

                            Toast.makeText(Editevent2.this, "Event not created successfully!", Toast.LENGTH_SHORT).show();

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