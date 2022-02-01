package com.app.tnevi;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.tnevi.Allurl.Allurl;
import com.app.tnevi.Utils.GetRealPathFromUri;
import com.app.tnevi.internet.CheckConnectivity;
import com.app.tnevi.retrofit.ApiClient;
import com.app.tnevi.retrofit.ApiInterface;
import com.app.tnevi.session.SessionManager;
import com.bumptech.glide.Glide;
import com.developers.imagezipper.ImageZipper;
import com.example.easywaylocation.EasyWayLocation;
import com.example.easywaylocation.Listener;

import com.facebook.login.LoginManager;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

import static com.example.easywaylocation.EasyWayLocation.LOCATION_SETTING_REQUEST_CODE;

public class SettingsActivity extends AppCompatActivity implements Listener {

    LinearLayoutCompat btnHome, btnSearch, btnWallet, btnProf, btnSettings, btnEvent, btnUpdatepassword, btnLogout;
    ImageView iconSetting, imgPrf, btneditName;
    TextView tvName, tvEmail, btnSupport, tvFaq, btnAboutus, btnPaymentOptions, btnBankInfo, tvFeedback;
    private static final String SHARED_PREFS = "sharedPrefs";

    SessionManager sessionManager;
    private static final int REQUEST_WRITE_STORAGE_REQUEST_CODE = 112;
    private int PICK_IMAGE_REQUEST = 1;
    private int CAMERA_IMAGE_REQUEST = 0;
    String username, useremail, token, msg;
    private static final String TAG = "Myapp";
    String fname;
    String lname;
    String pro_pic;
    String push_notification;
    String allow_location;
    String zipcode;
    String address;
    String lat_val;
    String long_val;
    String country_code;
    String email;
    List<Address> addresses;
    Switch switchButton, switchNotification;
    //Location mylocation;
    FusedLocationProviderClient mFusedLocationClient;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    double Lat = 0.0, Long = 0.0;
    String postalcode = "";
    String countrycode = "";
    Geocoder geocoder;
    private GoogleApiClient mGoogleApi;
    EasyWayLocation easyWayLocation;
    int PERMISSION_ID = 44;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        btnSearch = findViewById(R.id.btnSearch);
        btnWallet = findViewById(R.id.btnWallet);
        btnProf = findViewById(R.id.btnProf);
        btnSettings = findViewById(R.id.btnSettings);
        btnEvent = findViewById(R.id.btnEvent);
        btnHome = findViewById(R.id.btnHome);
        iconSetting = findViewById(R.id.iconSetting);
        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        imgPrf = findViewById(R.id.imgPrf);
        btneditName = findViewById(R.id.btneditName);
        switchButton = findViewById(R.id.switchButton);
        btnUpdatepassword = findViewById(R.id.btnUpdatepassword);
        switchNotification = findViewById(R.id.switchNotification);
        btnSupport = findViewById(R.id.btnSupport);
        btnLogout = findViewById(R.id.btnLogout);
        tvFaq = findViewById(R.id.tvFaq);
        btnAboutus = findViewById(R.id.btnAboutus);
        btnPaymentOptions = findViewById(R.id.btnPaymentOptions);
        btnBankInfo = findViewById(R.id.btnBankInfo);
        tvFeedback = findViewById(R.id.tvFeedback);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // method to get the location
        getLastLocation();

        easyWayLocation = new EasyWayLocation(this, true, false, this);
        easyWayLocation.startLocation();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            iconSetting.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.colorAccent));
        }

        requestAppPermissions();
        sessionManager = new SessionManager(getApplicationContext());
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        username = sharedPreferences.getString("name", "");
        useremail = sharedPreferences.getString("email", "");
        token = sharedPreferences.getString("token", "");

        mGoogleApi = new GoogleApiClient.Builder(getApplicationContext()) //Use app context to prevent leaks using activity
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();

        getAccount();
        onclick();


    }


    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApi.connect();
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApi.isConnected()) {
            mGoogleApi.disconnect();
        }
        easyWayLocation.endUpdates();
    }


    private void signOut() {
        if (mGoogleApi.isConnected()) {
            Auth.GoogleSignInApi.signOut(mGoogleApi);
            mGoogleApi.disconnect();
            mGoogleApi.connect();
        }
    }


    public void onclick() {


        tvFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(android.content.Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.app.tnevi"));
                startActivity(i);
            }
        });


        btnPaymentOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SettingsActivity.this, Checkoutpaymentoption.class);
                startActivity(intent);
            }
        });

        btnBankInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SettingsActivity.this, Checkoutbankinfo.class);
                startActivity(intent);
            }
        });


        tvFaq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SettingsActivity.this, FAQ.class);
                startActivity(intent);

            }
        });

        btnAboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(SettingsActivity.this, Aboutus.class);
                startActivity(intent);
            }
        });

        btnSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SettingsActivity.this, Contactsupport.class);
                startActivity(intent);
            }
        });

        imgPrf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                showFileChooser();

            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SettingsActivity.this, Search.class);
                startActivity(intent);
            }
        });

        btnEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SettingsActivity.this, Events.class);
                startActivity(intent);

            }
        });

        btnProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SettingsActivity.this, Profile.class);
                startActivity(intent);


            }
        });

        btnWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(SettingsActivity.this, Wallet.class);
                startActivity(intent);

            }
        });

        btneditName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateusername();

            }
        });

        btnUpdatepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(SettingsActivity.this, Updatepassword.class);
                startActivity(intent);

            }
        });

        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if (isChecked) {
                    easyWayLocation.startLocation();
                    if(easyWayLocation.hasLocationEnabled()) {
                        locationOn();
                    }
                } else {
                    Locationoff();
                }



            }
        });

        switchNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    Toast.makeText(getApplicationContext(), "Notification ON", Toast.LENGTH_SHORT).show();
                    notificationOn();


                } else {
                    Toast.makeText(getApplicationContext(), "Notification OFF", Toast.LENGTH_SHORT).show();
                    notificationOff();
                }

            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                builder.setMessage("Do you really want to logout?");
                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        sessionManager.logoutUser();
                        SharedPreferences settings = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
                        settings.edit().clear().apply();
                        LoginManager.getInstance().logOut();
                        signOut();
                        startActivity(new Intent(SettingsActivity.this, Login.class));
                        finish();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
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


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2404 && resultCode == Activity.RESULT_OK) {
            Uri fileUri = data.getData();
            try {

                uploadToServer(GetRealPathFromUri.getPathFromUri(SettingsActivity.this, fileUri));

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.RESULT_ERROR, Toast.LENGTH_SHORT).show();

        } else if (requestCode == LOCATION_SETTING_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                //easyWayLocation.onActivityResult(resultCode);
                easyWayLocation.startLocation();
                locationOn();
            }

        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }

    }


    private void uploadToServer(String fileUri) throws IOException {
        showProgressDialog();
        Retrofit retrofit = ApiClient.getRetrofitInstance();
        ApiInterface uploadAPIs = retrofit.create(ApiInterface.class);
        //Create a file object using file path
        File file = new File(fileUri);
        File imageZipperFile = new ImageZipper(SettingsActivity.this)
                .setQuality(50)
                .setMaxWidth(300)
                .setMaxHeight(300)
                .compressToFile(file);

        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/jpg"), imageZipperFile);
        MultipartBody.Part part = MultipartBody.Part.createFormData("pro_pic", imageZipperFile.getName(), fileReqBody);


        Call<ResponseBody> mcall = uploadAPIs.uploadImage(token, part);
        mcall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    if (response.body() != null) {
                        String result = response.body().string();
                        Log.v("response2-->", result);
                        JSONObject mjonsresponse = new JSONObject(result);
                        msg = mjonsresponse.getString("message");
                        String stat = mjonsresponse.getString("stat");
                        if (stat.equals("succ")) {
                            Toast.makeText(SettingsActivity.this, msg, Toast.LENGTH_SHORT).show();
                            JSONObject userdeatisObj = mjonsresponse.getJSONObject("data");
                            JSONObject profileObj2 = userdeatisObj.getJSONObject("profile");
                            String picurl = profileObj2.getString("pro_pic");
                            Log.d(TAG, "picurl-->" + picurl);


                            Log.v("picurl-->", picurl);
                            Glide.with(SettingsActivity.this)
                                    .load("http://dev8.ivantechnology.in/tnevi/storage/app/" + picurl)
                                    .circleCrop()
                                    .placeholder(R.drawable.dp2)
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

//                                    Toast.makeText(Settings.this, msg, Toast.LENGTH_SHORT).show();
                                    JSONObject userdeatisObj = result.getJSONObject("data");
                                    fname = userdeatisObj.getString("name");
                                    email = userdeatisObj.getString("email");

                                    JSONObject profileObj = userdeatisObj.getJSONObject("profile");
                                    pro_pic = profileObj.getString("pro_pic");
                                    String push_notification = profileObj.getString("push_notification");
                                    String allow_location = profileObj.getString("allow_location");
                                    Log.d(TAG, "profileurl-->" + pro_pic);


                                    Glide.with(SettingsActivity.this)
                                            .load("http://dev8.ivantechnology.in/tnevi/storage/app/" + pro_pic)
                                            .circleCrop()
                                            .placeholder(R.drawable.dp2)
                                            .into(imgPrf);


                                    tvName.setText(fname);
                                    tvEmail.setText(email);
                                    switchNotification.setChecked(push_notification.equals("1"));
                                    switchButton.setChecked(allow_location.equals("1"));


                                } else {

                                    hideProgressDialog();
                                    Log.d(TAG, "unsuccessfull - " + "Error");
                                    Toast.makeText(SettingsActivity.this, msg, Toast.LENGTH_SHORT).show();
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            hideProgressDialog();


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            hideProgressDialog();
                            Toast.makeText(SettingsActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", token);
                    return params;
                }

            };

            Volley.newRequestQueue(this).add(stringRequest);

        } else {

            Toast.makeText(getApplicationContext(), "OOPS! No Internet Connection", Toast.LENGTH_SHORT).show();

        }


    }


    public void updateusername() {
        final EditText name;
        Button submit;
        final Dialog dialog = new Dialog(SettingsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        dialog.setContentView(R.layout.dialog);
        params.copyFrom(dialog.getWindow().getAttributes());
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(params);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        name = (EditText) dialog.findViewById(R.id.name);
        submit = (Button) dialog.findViewById(R.id.submit);


        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (name.getText().toString().isEmpty()) {
                    name.setError("Please Enter Name");
                } else if (name.getText().toString().length() < 4) {
                    name.setError("Please enter at least 4 characters");

                } else {
                    //updating note

                    if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {


                        showProgressDialog();

                        JSONObject params = new JSONObject();

                        try {
                            params.put("uname", name.getText().toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Allurl.UpdateAccount, params, response -> {

                            Log.d("Response4-->", String.valueOf(response));

                            hideProgressDialog();
                            dialog.cancel();
                            getAccount();


                            //TODO: handle success
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                hideProgressDialog();
                                Toast.makeText(SettingsActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }) {

                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("Authorization", token);
                                return params;
                            }

                        };

                        Volley.newRequestQueue(SettingsActivity.this).add(jsonRequest);


                    } else {

                        Toast.makeText(getApplicationContext(), "Ooops! Internet Connection Error", Toast.LENGTH_SHORT).show();

                    }


                }
            }
        });
    }


//    private void fetchLocation() {
//        if (ActivityCompat.checkSelfPermission(
//                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
//            return;
//        }
//
//        Task<Location> task = fusedLocationProviderClient.getLastLocation();
//        task.addOnSuccessListener(new OnSuccessListener<Location>() {
//            @Override
//            public void onSuccess(Location location) {
//                if (location != null) {
//                    currentLocation = location;
//                    Lat = currentLocation.getLatitude();
//                    Long = currentLocation.getLongitude();
//                    geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
//                    try {
//                        addresses = geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 1);
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    address = addresses.get(0).getLocality();
//                    postalcode = addresses.get(0).getPostalCode();
//                    countrycode = addresses.get(0).getCountryCode();
//
//                    updateLocation();
//
//                } else {
//
//                    Toast.makeText(getApplicationContext(), "Turn on GPS!", Toast.LENGTH_SHORT).show();
//                    Intent settingintent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                    startActivity(settingintent);
//                    finish();
//
//                }
//            }
//        });
//
//    }

    public void locationOn() {

        //Lat = mylocation.getLatitude();
        //Long = mylocation.getLongitude();
        showProgressDialog();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                    addresses = geocoder.getFromLocation(Lat, Long, 1);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (addresses != null && addresses.size() > 0) {
                    address = addresses.get(0).getLocality();
                    postalcode = addresses.get(0).getPostalCode();
                    countrycode = addresses.get(0).getCountryCode();

                    updateLocation();
                }else{
                    Toast.makeText(SettingsActivity.this,"Unable to get the location please try again",Toast.LENGTH_LONG).show();
                    switchButton.setChecked(false);
                    hideProgressDialog();
                }
            }
        },3000);



    }

    @Override
    public void currentLocation(Location location) {
        Log.v("location",location.getLatitude()+"=="+location.getLongitude());
        Lat = location.getLatitude();
        Long = location.getLongitude();
//        StringBuilder data = new StringBuilder();
//        data.append(location.getLatitude());
//        data.append(location.getLongitude());

    }

    @Override
    public void locationCancelled() {
        Toast.makeText(this, "Location Cancelled", Toast.LENGTH_SHORT).show();

    }


  /*  @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationOn();
                }
                break;
        }
    }*/


//    @Override
//    protected void onResume() {
//        super.onResume();
//        easyWayLocation.startLocation();
//    }




    public void updateLocation() {

        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {





            JSONObject params = new JSONObject();

            try {
                params.put("allow_location", "1");
                params.put("zipcode", postalcode);
                params.put("address", address);
                params.put("lat_val", Lat);
                params.put("long_val", Long);
                params.put("country_code", countrycode);


            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Allurl.UpdateLocation, params, response -> {

                Log.d("Response-->", String.valueOf(response));
                switchButton.setChecked(true);
                Toast.makeText(SettingsActivity.this, "Location Updated", Toast.LENGTH_SHORT).show();

                hideProgressDialog();

                //TODO: handle success
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(SettingsActivity.this, "Invalid", Toast.LENGTH_SHORT).show();
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


    public void Locationoff() {

        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {


            showProgressDialog();


            JSONObject params = new JSONObject();

            try {
                params.put("allow_location", "0");
                params.put("zipcode", postalcode);
                params.put("address", address);
                params.put("lat_val", Lat);
                params.put("long_val", Long);
                params.put("country_code", countrycode);


            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Allurl.UpdateLocation, params, response -> {

                Log.d("Response-->", String.valueOf(response));
                Toast.makeText(SettingsActivity.this, "Location Off", Toast.LENGTH_SHORT).show();

                hideProgressDialog();
                switchButton.setChecked(false);

                //TODO: handle success
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(SettingsActivity.this, "Invalid", Toast.LENGTH_SHORT).show();

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


    public void notificationOn() {


        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {


            showProgressDialog();


            JSONObject params = new JSONObject();

            try {
                params.put("push_notification", "1");


            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Allurl.UpdateLocation, params, response -> {

                Log.d("Response-->", String.valueOf(response));


                hideProgressDialog();

                //TODO: handle success
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(SettingsActivity.this, "Invalid", Toast.LENGTH_SHORT).show();

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


    public void notificationOff() {


        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {


            showProgressDialog();


            JSONObject params = new JSONObject();

            try {
                params.put("push_notification", "0");


            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Allurl.UpdateLocation, params, response -> {

                Log.d("Response-->", String.valueOf(response));


                hideProgressDialog();

                //TODO: handle success
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(SettingsActivity.this, "Invalid", Toast.LENGTH_SHORT).show();

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

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        // check if permissions are given
        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {

                // getting last
                // location from
                // FusedLocationClient
                // object
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                           /* latitudeTextView.setText(location.getLatitude() + "");
                            longitTextView.setText(location.getLongitude() + "");*/
                            Lat = location.getLatitude();
                            Long = location.getLongitude();
                        }
                    }
                });
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
           /* latitudeTextView.setText("Latitude: " + mLastLocation.getLatitude() + "");
            longitTextView.setText("Longitude: " + mLastLocation.getLongitude() + "");*/
            Lat = mLastLocation.getLatitude();
            Long = mLastLocation.getLongitude();
        }
    };

    // method to check for permissions
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    // method to request for permissions
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    // method to check
    // if location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    // If everything is alright then
    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }


}