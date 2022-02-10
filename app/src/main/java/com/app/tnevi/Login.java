package com.app.tnevi;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.tnevi.Allurl.Allurl;
import com.app.tnevi.internet.CheckConnectivity;
import com.app.tnevi.session.SessionManager;
import com.example.easywaylocation.EasyWayLocation;
import com.example.easywaylocation.Listener;


import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import static com.example.easywaylocation.EasyWayLocation.LOCATION_SETTING_REQUEST_CODE;

public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, Listener {

    TextView btnSignup, btnForgotpass, btnSigninlater;
    Button btnSignin;
    EditText etUsername, etPassword;
    String username, password, msg, fbloginstatus;
    String status, name, email, email_verification_code, phone_no, role, address, postalcode, register_email, token, countrycode, set_pref;
    int id, otp_time;
    private static final String SHARED_PREFS = "sharedPrefs";
    SessionManager sessionManager;
    LoginButton login_button;
    CallbackManager callbackManager;
    LinearLayout btnFacebook, btnGmail;
    private static final String TAG = "Myapp";
    String fb_first_name, fb_last_name, fb_email, fb_image_url, fb_id, gsigninpassword, g_username, g_email, g_id;
    SignInButton sign_in_button;
    private static final int RC_SIGN_IN = 7;
    private GoogleApiClient mGoogleApi;
    Geocoder geocoder;
    List<Address> addresses;
    double Lat = 0.0, Long = 0.0;
    EasyWayLocation easyWayLocation;
    String fcmtoken = "";
    FusedLocationProviderClient mFusedLocationClient;
    int PERMISSION_ID = 44;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent intent = getIntent();
        register_email = intent.getStringExtra("email");
        btnSignup = findViewById(R.id.btnSignup);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnSignin = findViewById(R.id.btnSignin);
        btnSigninlater = findViewById(R.id.btnSigninlater);
        btnForgotpass = findViewById(R.id.btnForgotpass);
        btnGmail = findViewById(R.id.btnGmail);
        btnFacebook = findViewById(R.id.btnFacebook);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        login_button = findViewById(R.id.login_button);
        sign_in_button = findViewById(R.id.sign_in_button);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        // method to get the location
        getLastLocation();

        if (register_email==null){
            etUsername.setText("");
        }else {
            etUsername.setText(register_email);
        }


        easyWayLocation = new EasyWayLocation(this, false, false, this);

        login_button.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends"));
        sessionManager = new SessionManager(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApi = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        fcmtoken = task.getResult();
                        Log.d(TAG, "FCM-->"+fcmtoken);
                    }
                });


        onClick();

    }

    public void onClick() {

        btnGmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent signIngoogle = Auth.GoogleSignInApi.getSignInIntent(mGoogleApi);
                startActivityForResult(signIngoogle, RC_SIGN_IN);

            }
        });

        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                login_button.performClick();
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Login.this, Registration.class));
                finish();

            }
        });


        btnForgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Login.this, Forgetpassword.class));


            }
        });

        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                checkblank();


            }
        });

        btnSigninlater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });


        loginfacebook();


    }

    public void checkblank() {

        username = etUsername.getText().toString();
        password = etPassword.getText().toString();

        if (username.length() == 0) {

            Toast.makeText(this, "Please enter a valid User Name", Toast.LENGTH_SHORT).show();

        } else if (password.length() == 0) {


            Toast.makeText(this, "Please enter a Password", Toast.LENGTH_SHORT).show();


        } else {

            login();
        }


    }


    public void login() {

        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {

            showProgressDialog();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, Allurl.loginUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.i("Response-->", String.valueOf(response));

                            try {
                                JSONObject result = new JSONObject(String.valueOf(response));
                                msg = result.getString("message");
                                Log.d(TAG, "msg-->" + msg);
                                String stat = result.getString("stat");
                                if (stat.equals("succ")) {

                                    Toast.makeText(Login.this, msg, Toast.LENGTH_SHORT).show();
                                    JSONObject userdeatisObj = result.getJSONObject("data");
                                    id = userdeatisObj.getInt("id");
                                    status = userdeatisObj.getString("status");
                                    role = userdeatisObj.getString("role");
                                    name = userdeatisObj.getString("name");
                                    email = userdeatisObj.getString("email");
                                    email_verification_code = userdeatisObj.getString("email_verification_code");
                                    phone_no = userdeatisObj.getString("phone_no");
                                    otp_time = userdeatisObj.getInt("otp_time");
                                    token = result.getString("token");
                                    set_pref = userdeatisObj.getString("set_preference");
                                    Log.d(TAG, "Token-->" + token);

                                    //SharedPref
                                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("role", role);
                                    editor.putInt("id", id);
                                    editor.putString("status", status);
                                    editor.putString("name", name);
                                    editor.putString("email", email);
                                    editor.putString("email_verification_code", email_verification_code);
                                    editor.putString("phone_no", phone_no);
                                    editor.putInt("otp_time", otp_time);
                                    editor.putString("token", token);
                                    editor.apply();
                                    locationOn();

                                    if (set_pref.equals("0")){
                                        Intent intent = new Intent(Login.this, Chooseyoulike.class);
                                        intent.putExtra("username", username);
                                        intent.putExtra("password", password);
                                        startActivity(intent);
                                        finish();
                                    }else {
                                        sessionManager.createLoginSession(username, password);
                                        Intent intent = new Intent(Login.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }




                                } else if (stat.equals("veri_err")) {
                                    //VerifyEmail
                                    VerifyEmail();

                                } else {

                                    hideProgressDialog();
                                    Log.d(TAG, "unsuccessfull - " + "Error");
                                    Toast.makeText(Login.this, msg, Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(Login.this, error.toString(), Toast.LENGTH_SHORT).show();

                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("emailid", username);
                    params.put("password", password);
                    params.put("fcm_token", fcmtoken);

                    return params;
                }

            };

            RequestQueue requestQueue = Volley.newRequestQueue(Login.this);
            requestQueue.add(stringRequest);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    9000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        } else {

            Toast.makeText(getApplicationContext(), "OOPS! No Internet Connection", Toast.LENGTH_SHORT).show();

        }

    }

    public void loginfacebook() {

        login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                fbloginstatus = loginResult.getAccessToken().getToken();
                Log.d(TAG, "tokenlogin-->" + fbloginstatus);

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());

                                try {
                                    fb_first_name = object.optString("name");
                                    fb_id = object.optString("id");
                                    fb_email = object.optString("email");
                                    Log.d("details-->",fb_email+", "+fb_id+", "+fb_first_name);
                                    facebooklogin();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();


            }

            @Override
            public void onCancel() {
                Toast.makeText(Login.this, "Cancel", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(Login.this, "error", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void facebooklogin() {


        showProgressDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Allurl.SocailLogin,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.i("Response-->", String.valueOf(response));

                        try {
                            JSONObject result = new JSONObject(String.valueOf(response));
                            msg = result.getString("message");
                            Log.d(TAG, "msg-->" + msg);
                            String stat = result.getString("stat");
                            if (stat.equals("succ")) {

                                Toast.makeText(Login.this, msg, Toast.LENGTH_SHORT).show();
                                JSONObject userdeatisObj = result.getJSONObject("data");


                                id = userdeatisObj.getInt("id");
//                                status = userdeatisObj.getString("status");
                                role = userdeatisObj.getString("role");
                                name = userdeatisObj.getString("name");
                                email = userdeatisObj.getString("email");
                                email_verification_code = userdeatisObj.getString("email_verification_code");
//                                phone_no = userdeatisObj.getString("phone_no");
                                otp_time = userdeatisObj.getInt("otp_time");
                                token = result.getString("token");
                                Log.d(TAG, "Token-->" + token);

                                //SharedPref
                                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("role", role);
                                editor.putInt("id", id);
                                editor.putString("status", status);
                                editor.putString("name", name);
                                editor.putString("email", email);
                                editor.putString("email_verification_code", email_verification_code);
                                editor.putString("phone_no", phone_no);
                                editor.putInt("otp_time", otp_time);
                                editor.putString("token", token);
                                editor.apply();

                                locationOn();

                                sessionManager.createLoginSession(username, password);
                                Intent intent = new Intent(Login.this, MainActivity.class);
                                startActivity(intent);
                                finish();


                            } else if (stat.equals("veri_err")) {
                                //VerifyEmail
                                VerifyEmail();

                            } else {

                                hideProgressDialog();
                                Log.d(TAG, "unsuccessfull - " + "Error");
                                Toast.makeText(Login.this, msg, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(Login.this, error.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("Error-->", error.toString());

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("emailid", fb_email);
                params.put("password", "123456");
                params.put("social_platform", "fb");
                params.put("fb_id", fb_id);
                params.put("google_id", "");
                params.put("emailid", fb_email);
                params.put("uname", fb_first_name);
                params.put("fcm_token", fcmtoken);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(Login.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                9000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }


    public void VerifyEmail() {

        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {

            showProgressDialog();

            JSONObject params = new JSONObject();

            try {
                params.put("emailid", username);


            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Allurl.ResendOTP, params, response -> {

                Log.i("Response-->", String.valueOf(response));

                try {
                    JSONObject result = new JSONObject(String.valueOf(response));
                    String msg = result.getString("message");
                    Log.d(TAG, "msg-->" + msg);
                    String stat = result.getString("stat");
                    if (stat.equals("succ")) {

                        Toast.makeText(Login.this, msg, Toast.LENGTH_SHORT).show();
                        hideProgressDialog();
                        Intent intent = new Intent(Login.this, Emailverification.class);
                        intent.putExtra("email", username);
                        startActivity(intent);
                        Log.d(TAG, "unsuccessfull - " + "Error");


                    } else {
                        Toast.makeText(Login.this, msg, Toast.LENGTH_SHORT).show();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideProgressDialog();

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(Login.this, error.toString(), Toast.LENGTH_SHORT).show();

                    error.printStackTrace();
                    //TODO: handle failure
                }
            });

            Volley.newRequestQueue(this).add(jsonRequest);

        } else {

            Toast.makeText(getApplicationContext(), "Ooops! Internet Connection Error", Toast.LENGTH_SHORT).show();


        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("GoogleLogincode :", String.valueOf(requestCode));

        if (requestCode == RC_SIGN_IN) {

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handelSigninresult(result);

        }

        if (requestCode == LOCATION_SETTING_REQUEST_CODE) {
            easyWayLocation.onActivityResult(resultCode);
        }

    }


    private void handelSigninresult(GoogleSignInResult result) {

        Log.e("GoogleLogin :", String.valueOf(result.isSuccess()));
        final GoogleSignInAccount acct = result.getSignInAccount();
        g_username = acct.getDisplayName();
        g_email = acct.getEmail();
        g_id = acct.getId();
        Log.d(TAG, "Gmail-->" + g_id);

        if (result.isSuccess()) {


            showProgressDialog();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, Allurl.SocailLogin,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.i("Response-->", String.valueOf(response));

                            try {
                                JSONObject result = new JSONObject(String.valueOf(response));
                                msg = result.getString("message");
                                Log.d(TAG, "msg-->" + msg);
                                String stat = result.getString("stat");
                                if (stat.equals("succ")) {

                                    Toast.makeText(Login.this, msg, Toast.LENGTH_SHORT).show();
                                    JSONObject userdeatisObj = result.getJSONObject("data");
                                    id = userdeatisObj.getInt("id");
//                                status = userdeatisObj.getString("status");
                                    role = userdeatisObj.getString("role");
                                    name = userdeatisObj.getString("name");
                                    email = userdeatisObj.getString("email");
                                    email_verification_code = userdeatisObj.getString("email_verification_code");
//                                phone_no = userdeatisObj.getString("phone_no");
                                    otp_time = userdeatisObj.getInt("otp_time");
                                    token = result.getString("token");
                                    Log.d(TAG, "Token-->" + token);

                                    //SharedPref
                                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("role", role);
                                    editor.putInt("id", id);
//                                    editor.putString("status", status);
                                    editor.putString("name", name);
                                    editor.putString("email", email);
                                    editor.putString("email_verification_code", email_verification_code);
//                                    editor.putString("phone_no", phone_no);
                                    editor.putInt("otp_time", otp_time);
                                    editor.putString("token", token);
                                    editor.apply();

                                    locationOn();

                                    sessionManager.createLoginSession(username, "password");
                                    Intent intent = new Intent(Login.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();


                                } else if (stat.equals("veri_err")) {
                                    //VerifyEmail
                                    VerifyEmail();

                                } else {

                                    hideProgressDialog();
                                    Log.d(TAG, "unsuccessfull - " + "Error");
                                    Toast.makeText(Login.this, msg, Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(Login.this, error.toString(), Toast.LENGTH_SHORT).show();
                            Log.d("Error-->", error.toString());

                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("emailid", g_email);
                    params.put("password", "");
                    params.put("social_platform", "google");
                    params.put("fb_id", "");
                    params.put("google_id", g_id);
                    params.put("emailid", g_email);
                    params.put("uname", g_username);
                    params.put("fcm_token", fcmtoken);
                    return params;
                }

            };

            Volley.newRequestQueue(Login.this).add(stringRequest);


        } else {

            Toast.makeText(getApplicationContext(), "Google Sign in not Success", Toast.LENGTH_SHORT).show();

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


    @Override
    public void onBackPressed() {

        finishAffinity();
    }


    @Override
    public void onConnectionFailed(@NonNull @NotNull ConnectionResult connectionResult) {


    }


    @Override
    public void locationOn() {
        showProgressDialog();

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
                    Toast.makeText(Login.this,"Unable to get the location please try again",Toast.LENGTH_LONG).show();
                    hideProgressDialog();
                }

    }

    @Override
    public void currentLocation(Location location) {

        Log.v("location",location.getLatitude()+"=="+location.getLongitude());
        Lat = location.getLatitude();
        Long = location.getLongitude();

    }

    @Override
    public void locationCancelled() {
        Toast.makeText(this, "Location Cancelled", Toast.LENGTH_SHORT).show();

    }


    @Override
    protected void onResume() {
        super.onResume();
        easyWayLocation.startLocation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        easyWayLocation.endUpdates();

    }


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

                Log.d("Response2-->", String.valueOf(response));
                Toast.makeText(Login.this, "Location Updated", Toast.LENGTH_SHORT).show();

                hideProgressDialog();

                //TODO: handle success
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Login.this, error.toString(), Toast.LENGTH_SHORT).show();
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