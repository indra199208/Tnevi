package com.app.tnevi;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amar.library.ui.StickyScrollView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.app.tnevi.Adapters.ChoosemayAdapter;
import com.app.tnevi.Adapters.FeaturedAdapter;
import com.app.tnevi.Adapters.GalleryAdapter;
import com.app.tnevi.internet.CheckConnectivity;
import com.app.tnevi.model.GeteventModel;
import com.app.tnevi.session.SessionManager;
import com.bumptech.glide.Glide;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.app.tnevi.Allurl.Allurl;
import com.app.tnevi.Utils.ItemOffsetDecoration;
import com.app.tnevi.model.GalleryModel;
import com.kv.popupimageview.PopupImageView;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.pierfrancescosoffritti.youtubeplayer.player.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerInitListener;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Eventdetails extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "myapp";
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String SHARED_PREFS2 = "chooselike";
    SessionManager sessionManager;
    String useremail, username, token, eventId, response2;
    ImageView btn_back, imgBanner, btnSend, imgPrfpic;
    String msg, eventname, postedby, eventimage, freestat, social, comission,
            eventdate, stime, etime, minprice, maxprice, phonenumber,
            address, longval, latval, description, venuename, venueaddress, currencyid,
            youtubelink, videoid, id, userid, favstatus, venueImage, event_edate, choosecategoryid;

    LikeButton btnHeart;
    YouTubePlayerView youtube_player_view;
    TextView tvEventName, tvPostedby, tvEventdate, tvTicketprice, tvEventdate2, tvAddress, tvEventdetails, tvTiminig;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    LinearLayout btnEarncash, btnBuyticket, btnSpam;
    RecyclerView rv_gallery, rv_youmaylike;
    private ArrayList<GalleryModel> galleryModelArrayList;
    private GalleryAdapter galleryAdapter;
    StickyScrollView scrollview;
    CardView ll_details;
    EditText etEmail;
    JSONArray gallery_data;
    ArrayList<String> list = new ArrayList<>();
    private ArrayList<GeteventModel> homeEventsModelArrayList;
    private FeaturedAdapter featuredAdapter;
    private ChoosemayAdapter choosemayAdapter;
    NativeAd nativeAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventdetails);
        Intent intent = getIntent();
        eventId = intent.getStringExtra("eventId");
        btn_back = findViewById(R.id.btn_back);
        btnHeart = findViewById(R.id.btnHeart);
        youtube_player_view = findViewById(R.id.youtube_player_view);
        imgBanner = findViewById(R.id.imgBanner);
        tvEventName = findViewById(R.id.tvEventName);
        tvPostedby = findViewById(R.id.tvPostedby);
        tvEventdate = findViewById(R.id.tvEventdate);
        tvTicketprice = findViewById(R.id.tvTicketprice);
        tvEventdate2 = findViewById(R.id.tvEventdate2);
        tvAddress = findViewById(R.id.tvAddress);
        tvEventdetails = findViewById(R.id.tvEventdetails);
        btnEarncash = findViewById(R.id.btnEarncash);
        btnBuyticket = findViewById(R.id.btnBuyticket);
        rv_gallery = findViewById(R.id.rv_gallery);
        getLifecycle().addObserver(youtube_player_view);
        scrollview = findViewById(R.id.scrollview);
        ll_details = findViewById(R.id.ll_details);
        btnSend = findViewById(R.id.btnSend);
        etEmail = findViewById(R.id.etEmail);
        btnSpam = findViewById(R.id.btnSpam);
        rv_youmaylike = findViewById(R.id.rv_youmaylike);
        tvTiminig = findViewById(R.id.tvTiminig);
        imgPrfpic = findViewById(R.id.imgPrfpic);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);



        sessionManager = new SessionManager(getApplicationContext());
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        username = sharedPreferences.getString("name", "");
        useremail = sharedPreferences.getString("email", "");
        SharedPreferences sharedPreferences2 = getApplicationContext().getSharedPreferences(SHARED_PREFS2, MODE_PRIVATE);
        choosecategoryid = sharedPreferences2.getString("chosen_categories", "");

        getEventDetails();
        refreshAd();


        scrollview.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                /* calculating the maximum distance we need to scroll for parallax */
                int maxDistance = imgBanner.getHeight();
                /* getting the amount of scroll on Y-axis */
                int movement = scrollview.getScrollY();
                /* checking if we've reached the top. if yes then stop translation otherwise continue */
                if (movement >= 0 && movement <= maxDistance) {
                    /* moving the parallax image by half the distance of the total scroll */
                    imgBanner.setTranslationY(-movement / 2);
                }
            }
        });

        onClick();


    }

    public void onClick() {

        tvPostedby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Eventdetails.this, Profile.class);
                startActivity(intent);
            }
        });

        btnSpam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Eventdetails.this);
                builder.setMessage("Do you really want to Spam report this event?");
                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Spam();

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


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (etEmail.getText().toString().length() == 0) {

                    Toast.makeText(Eventdetails.this, "Enter Valid Email Address", Toast.LENGTH_SHORT).show();

                } else {

                    sendemail();
                }


            }
        });


        btnHeart.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {

                addRemovefav();
            }

            @Override
            public void unLiked(LikeButton likeButton) {

                addRemovefav();
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });

        btnEarncash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                referralshare();


            }
        });

        btnBuyticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(Eventdetails.this, Gallerybooking.class);
                intent.putExtra("eventname", eventname);
                intent.putExtra("date", eventdate);
                intent.putExtra("address", address);
                intent.putExtra("response2", response2);
                intent.putExtra("maxprice", maxprice);
                intent.putExtra("event_id", eventId);
                intent.putExtra("name", postedby);
                intent.putExtra("latvalue", latval);
                intent.putExtra("lonvalue", longval);
                intent.putExtra("currencyid", currencyid);
                intent.putExtra("venueimage", venueImage);
                intent.putExtra("fees", "12");
                startActivity(intent);

            }
        });
    }


    public void Spam() {


        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {

            showProgressDialog();

            JSONObject params = new JSONObject();
            try {
                params.put("event_id", eventId);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Allurl.Spam, params, response -> {

                Log.i("Response-->", String.valueOf(response));

                try {
                    JSONObject result = new JSONObject(String.valueOf(response));
                    msg = result.getString("message");
                    Log.d(TAG, "msg-->" + msg);
                    String stat = result.getString("stat");
                    if (stat.equals("succ")) {
                        Toast.makeText(Eventdetails.this, msg, Toast.LENGTH_SHORT).show();
                    } else {
                        hideProgressDialog();
                        Log.d(TAG, "unsuccessfull - " + "Error");
                        Toast.makeText(Eventdetails.this, msg, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                hideProgressDialog();

                //TODO: handle success
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Eventdetails.this, error.toString(), Toast.LENGTH_SHORT).show();

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

                response2 = String.valueOf(response);
                try {
                    JSONObject result = new JSONObject(response2);
                    msg = result.getString("message");
                    Log.d(TAG, "msg-->" + msg);
                    String stat = result.getString("stat");
                    if (stat.equals("succ")) {

                        JSONObject userdeatisObj = result.getJSONObject("data");
                        galleryModelArrayList = new ArrayList<>();
                        JSONObject venueObj = userdeatisObj.getJSONObject("venue");
                        gallery_data = userdeatisObj.getJSONArray("gallery");
                        for (int i = 0; i < gallery_data.length(); i++) {
                            JSONObject galleryobj = gallery_data.getJSONObject(i);
                            GalleryModel galleryModel = new GalleryModel();
                            galleryModel.setFile_name(galleryobj.getString("file_name"));
                            list.add(galleryobj.getString("file_name"));
                            galleryModelArrayList.add(galleryModel);
                        }

                        setupRecyclerview();
                        id = userdeatisObj.getString("id");
                        userid = userdeatisObj.getString("user_id");
                        eventname = userdeatisObj.getString("event_name");
                        postedby = userdeatisObj.getString("posted_by");
                        if (!userdeatisObj.isNull("event_image")) {
                            eventimage = userdeatisObj.getString("event_image");
                        } else {
                            eventimage = "";
                        }
                        if (!userdeatisObj.isNull("youtube_links")) {
                            youtubelink = userdeatisObj.getString("youtube_links");
                        } else {
                            youtubelink = "";
                        }
                        freestat = userdeatisObj.getString("free_stat");
                        social = userdeatisObj.getString("social_links");
                        comission = userdeatisObj.getString("event_commission");
                        currencyid = userdeatisObj.getString("currency_id");
                        eventdate = userdeatisObj.getString("event_date");
                        event_edate = userdeatisObj.getString("event_edate");
                        stime = userdeatisObj.getString("event_stime");
                        etime = userdeatisObj.getString("event_etime");
                        minprice = userdeatisObj.getString("min_price");
                        maxprice = userdeatisObj.getString("max_price");
                        address = userdeatisObj.getString("event_address");
                        longval = userdeatisObj.getString("event_long_val");
                        latval = userdeatisObj.getString("event_lat_val");
                        description = userdeatisObj.getString("event_description");
                        favstatus = userdeatisObj.getString("fav_status");
                        phonenumber = userdeatisObj.getString("helpline_no");
                        venuename = venueObj.getString("venue_name");
                        venueaddress = venueObj.getString("venue_location");
                        if (!venueObj.isNull("stadium_image")) {
                            venueImage = venueObj.getString("stadium_image");
                        } else {
                            venueImage = "";
                        }

                        Glide.with(this)
                                .load("http://dev8.ivantechnology.in/tnevi/storage/app/" + eventimage)
                                .placeholder(R.drawable.bg7)
                                .into(imgBanner);

                        tvEventName.setText(eventname);
                        tvPostedby.setText(postedby);
                        tvEventdate.setText("$" + minprice + "-" + "$" + maxprice);
                        tvTicketprice.setText("$" + comission + "/Price");
                        tvEventdate2.setText(eventdate+" and "+event_edate);
                        tvTiminig.setText(stime+" and "+etime);
                        tvAddress.setText(address);
                        tvEventdetails.setText(description);
                        btnHeart.setLiked(favstatus.equals("1"));

                        youtube_player_view.initialize(new YouTubePlayerInitListener() {
                            @Override
                            public void onInitSuccess(final YouTubePlayer initializedYouTubePlayer) {
                                initializedYouTubePlayer.addListener(new AbstractYouTubePlayerListener() {
                                    @Override
                                    public void onReady() {
                                        String videoId = getYouTubeId(youtubelink);
                                        initializedYouTubePlayer.loadVideo(videoId, 0);
                                        initializedYouTubePlayer.pause();
                                    }
                                });
                            }
                        }, true);

                        fetchLocation();
                        choosemaylike();


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


    public void choosemaylike() {


        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {


            showProgressDialog();

            JSONObject params = new JSONObject();

            try {
                params.put("commission", "");
                params.put("search_address", "");
                params.put("page_no", 1);
                params.put("categories", choosecategoryid);
                params.put("search_date", "");
                params.put("keyword", "");
                params.put("featured_event", "");
                params.put("fev_list", "");
                params.put("highlight_event ", "");
                params.put("featured_event ", "");
                params.put("top_events", "");
                params.put("latitude", 0.0);
                params.put("longitude", 0.0);


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

                        homeEventsModelArrayList = new ArrayList<>();
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
                            geteventModel.setHighlightevent(responseobj.getString("highlight_event"));
                            geteventModel.setTicket_stat(responseobj.getString("top_events"));
                            geteventModel.setFav_status(responseobj.getString("fav_status"));
                            homeEventsModelArrayList.add(geteventModel);

                        }

                        choosemayAdapter = new ChoosemayAdapter(this, homeEventsModelArrayList);
                        rv_youmaylike.setAdapter(choosemayAdapter);
                        rv_youmaylike.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getApplicationContext(), R.dimen.photos_list_spacing3);
                        rv_youmaylike.addItemDecoration(itemDecoration);

                    } else {

                        Log.d(TAG, "unsuccessfull - " + "Error");
                        Toast.makeText(Eventdetails.this, "invalid", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideProgressDialog();

                //TODO: handle success
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Eventdetails.this, error.toString(), Toast.LENGTH_SHORT).show();

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


    private String getYouTubeId(String youTubeUrl) {
        String pattern = "(?<=youtu.be/|watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(youTubeUrl);
        if (matcher.find()) {
            return matcher.group();
        } else {
            return "";
        }
    }

    public void setupRecyclerview() {

        galleryAdapter = new GalleryAdapter(this, galleryModelArrayList);
        rv_gallery.setAdapter(galleryAdapter);
        rv_gallery.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getApplicationContext(), R.dimen.photos_list_spacing5);
        rv_gallery.addItemDecoration(itemDecoration);
    }


    public void popupImage(GalleryModel galleryModel, View view, int pos) {

        new PopupImageView(this, view, list, pos, "http://dev8.ivantechnology.in/tnevi/storage/app/");

    }


    public void addRemovefav() {


        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {

            showProgressDialog();

            JSONObject params = new JSONObject();

            try {
                params.put("event_id", eventId);

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

                        Toast.makeText(Eventdetails.this, msg, Toast.LENGTH_SHORT).show();

                    } else {

                        Log.d(TAG, "unsuccessfull - " + "Error");
                        Toast.makeText(Eventdetails.this, "Add to Favourite not successfull", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideProgressDialog();

                //TODO: handle success
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Eventdetails.this, error.toString(), Toast.LENGTH_SHORT).show();

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


    private void fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }

        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
//                    Toast.makeText(getApplicationContext(), currentLocation.getLatitude() + "" + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.current_location);
                    assert supportMapFragment != null;
                    supportMapFragment.getMapAsync(Eventdetails.this);
                }
            }
        });

    }


    public void sendemail() {
        final EditText details;
        Button submit;
        final Dialog dialog = new Dialog(Eventdetails.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        dialog.setContentView(R.layout.dailouge_sendmail);
        params.copyFrom(dialog.getWindow().getAttributes());
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(params);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        details = dialog.findViewById(R.id.details);
        submit = dialog.findViewById(R.id.submit);


        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (details.getText().toString().isEmpty()) {
                    details.setError("Please Enter Details");
                } else {
                    //updating note

                    if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {


                        showProgressDialog();

                        JSONObject params = new JSONObject();

                        try {
                            params.put("event_id", eventId);
                            params.put("contact_email", etEmail.getText().toString());
                            params.put("message", details.getText().toString());


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Allurl.ContactOrganizer, params, response -> {

                            Log.d("Response4-->", String.valueOf(response));

                            Toast.makeText(Eventdetails.this, "Message has been sent", Toast.LENGTH_SHORT).show();
                            hideProgressDialog();
                            dialog.cancel();


                            //TODO: handle success
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                hideProgressDialog();
                                Toast.makeText(Eventdetails.this, error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }) {

                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("Authorization", token);
                                return params;
                            }

                        };

                        Volley.newRequestQueue(Eventdetails.this).add(jsonRequest);

                    } else {
                        Toast.makeText(getApplicationContext(), "Ooops! Internet Connection Error", Toast.LENGTH_SHORT).show();
                    }


                }
            }
        });
    }

    public void referralshare() {

        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {


            showProgressDialog();

            JSONObject params = new JSONObject();

            try {
                params.put("event_id", eventId);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Allurl.RefferalCode, params, response -> {

                Log.d("Response4-->", String.valueOf(response));
                hideProgressDialog();
                try {
                    JSONObject result = new JSONObject(String.valueOf(response));
                    msg = result.getString("message");
                    Log.d(TAG, "msg-->" + msg);
                    String stat = result.getString("stat");
                    if (stat.equals("succ")) {
                        String data = result.getString("data");
                        Toast.makeText(Eventdetails.this, msg, Toast.LENGTH_SHORT).show();
                        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        String shareBody = "Hi! I use the Tnevi app to buy all my event tickets.I receive cash and point using this app. Use my link to download and try.Refferal Code: " + data;
//                        String shareSub = "Your subject here";
//                        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSub);
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                        startActivity(Intent.createChooser(sharingIntent, "Share using"));


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                //TODO: handle success
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    hideProgressDialog();
                    Toast.makeText(Eventdetails.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", token);
                    return params;
                }

            };

            Volley.newRequestQueue(Eventdetails.this).add(jsonRequest);

        } else {
            Toast.makeText(getApplicationContext(), "Ooops! Internet Connection Error", Toast.LENGTH_SHORT).show();
        }

    }


    private void populateUnifiedNativeAdView(NativeAd nativeAd, NativeAdView adView) {
        adView.setMediaView((MediaView) adView.findViewById(R.id.ad_media));
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));


        ((TextView) Objects.requireNonNull(adView.getHeadlineView())).setText(nativeAd.getHeadline());
        Objects.requireNonNull(adView.getMediaView()).setMediaContent(Objects.requireNonNull(nativeAd.getMediaContent()));


        if (nativeAd.getBody() == null) {
            Objects.requireNonNull(adView.getBodyView()).setVisibility(View.INVISIBLE);

        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }
        if (nativeAd.getCallToAction() == null) {
            Objects.requireNonNull(adView.getCallToActionView()).setVisibility(View.INVISIBLE);
        } else {
            Objects.requireNonNull(adView.getCallToActionView()).setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }
        if (nativeAd.getIcon() == null) {
            Objects.requireNonNull(adView.getIconView()).setVisibility(View.GONE);
        } else {
            ((ImageView) Objects.requireNonNull(adView.getIconView())).setImageDrawable(nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            Objects.requireNonNull(adView.getPriceView()).setVisibility(View.INVISIBLE);

        } else {
            Objects.requireNonNull(adView.getPriceView()).setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }
        if (nativeAd.getStore() == null) {
            Objects.requireNonNull(adView.getStoreView()).setVisibility(View.INVISIBLE);
        } else {
            Objects.requireNonNull(adView.getStoreView()).setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }
        if (nativeAd.getStarRating() == null) {
            Objects.requireNonNull(adView.getStarRatingView()).setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) Objects.requireNonNull(adView.getStarRatingView())).setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            Objects.requireNonNull(adView.getAdvertiserView()).setVisibility(View.INVISIBLE);
        } else
            ((TextView) Objects.requireNonNull(adView.getAdvertiserView())).setText(nativeAd.getAdvertiser());
        adView.getAdvertiserView().setVisibility(View.VISIBLE);


        adView.setNativeAd(nativeAd);


    }


    private void refreshAd() {
        AdLoader.Builder builder = new AdLoader.Builder(this, getString(R.string.ADMOB_ADS_UNIT_ID));
        builder.forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
            @Override
            public void onNativeAdLoaded(NativeAd unifiedNativeAd) {

                if (nativeAd != null) {
                    nativeAd.destroy();
                }
                nativeAd = unifiedNativeAd;
                FrameLayout frameLayout = findViewById(R.id.fl_adplaceholder);
                NativeAdView adView = (NativeAdView) getLayoutInflater().inflate(R.layout.ad_helper, null);

                populateUnifiedNativeAdView(unifiedNativeAd, adView);
                frameLayout.removeAllViews();
                frameLayout.addView(adView);
            }
        }).build();
        NativeAdOptions adOptions = new NativeAdOptions.Builder().build();
        builder.withNativeAdOptions(adOptions);
        AdLoader adLoader = builder.withAdListener(new AdListener() {
            public void onAdFailedToLoad(int i) {

            }
        }).build();
        adLoader.loadAd(new AdRequest.Builder().build());

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
    public void onMapReady(GoogleMap googleMap) {

        LatLng latLng = new LatLng(Double.parseDouble(latval), Double.parseDouble(longval));
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Event Location");
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
        googleMap.addMarker(markerOptions);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLocation();
                }
                break;
        }
    }


}