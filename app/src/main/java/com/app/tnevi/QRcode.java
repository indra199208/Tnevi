package com.app.tnevi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bestsoft32.tt_fancy_gif_dialog_lib.TTFancyGifDialog;
import com.bestsoft32.tt_fancy_gif_dialog_lib.TTFancyGifDialogListener;
import com.app.tnevi.Adapters.QrAdapter;
import com.app.tnevi.Allurl.Allurl;

import com.app.tnevi.internet.CheckConnectivity;
import com.app.tnevi.model.QrModel;
import com.facebook.login.LoginManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QRcode extends AppCompatActivity {

    TextView tvSection, tvRow, tvSeat, tvButtonStatus;
    ImageView btn_back, img_show_qr;
    String id;
    RecyclerView rv_qr;
    QrAdapter qrAdapter;
    private ArrayList<QrModel> qrModelArrayList;
    LinearLayout btnReedem;
    String token;
    private static final String SHARED_PREFS = "sharedPrefs";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        Intent intent = getIntent();
        id = intent.getStringExtra("booking_id");
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        btn_back = findViewById(R.id.btn_back);
        tvRow = findViewById(R.id.tvRow);
        tvSeat = findViewById(R.id.tvSeat);
        btnReedem = findViewById(R.id.btnReedem);
        tvButtonStatus = findViewById(R.id.tvButtonStatus);
        rv_qr = findViewById(R.id.rv_qr);

//        img_show_qr = findViewById(R.id.img_show_qr);
//        tvSection.setText(section);
//        tvRow.setText(row);
//        tvSeat.setText(seat);
//        if (status.equals("0")){
//            tvButtonStatus.setText("CHECK IN NOW");
//        }else {
//            tvButtonStatus.setText("ALREADY REDDEMED");
//        }
//        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
//        Display display = manager.getDefaultDisplay();
//        Point point = new Point();
//        display.getSize(point);
//        int width = point.x;
//        int height = point.y;
//        int dimen = Math.min(width, height);
//        dimen = dimen * 3 / 4;
//        qrgEncoder = new QRGEncoder(qr, null, QRGContents.Type.TEXT, dimen);
//        try {
//            bitmap = qrgEncoder.encodeAsBitmap();
//            img_show_qr.setImageBitmap(bitmap);
//        } catch (WriterException e) {
//            Log.e("Tag", e.toString());
//        }


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

//        btnReedem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (status.equals("0")){
//                    checkin();
//                }else {
//                    Toast.makeText(QRcode.this, "ALREADY REDDEMED", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        QrList();

    }

    public void QrList(){

        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {


            showProgressDialog();
            JSONObject params = new JSONObject();

            try {
                params.put("booking_id", id);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Allurl.QRlist, params, response -> {

                Log.i("Response-->", String.valueOf(response));

                try {
                    JSONObject result = new JSONObject(String.valueOf(response));
                    String msg = result.getString("message");
                    String stat = result.getString("stat");
                    if (stat.equals("succ")) {

                        qrModelArrayList = new ArrayList<>();
                        JSONArray response_data = result.getJSONArray("data");
                        for (int i = 0; i < response_data.length(); i++) {

                            QrModel qrModel = new QrModel();
                            JSONObject responseobj = response_data.getJSONObject(i);
                            qrModel.setSeat_id(responseobj.getString("seat_id"));
                            qrModel.setRow_name(responseobj.getString("row_name"));
                            qrModel.setBlock_name(responseobj.getString("block_name"));
                            qrModel.setQr_id(responseobj.getString("qr_id"));
                            qrModel.setStatus(responseobj.getString("status"));
                            qrModel.setSeat_no(responseobj.getString("seat_no"));
                            qrModelArrayList.add(qrModel);

                        }

                        qrAdapter = new QrAdapter(this, qrModelArrayList);
                        rv_qr.setAdapter(qrAdapter);
                        rv_qr.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));


                    } else {

                        Toast.makeText(QRcode.this, "invalid", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideProgressDialog();

                //TODO: handle success
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(QRcode.this, error.toString(), Toast.LENGTH_SHORT).show();

                }
            }){
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

    public void checkin(QrModel qrModel){

        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {

            showProgressDialog();

            JSONObject params = new JSONObject();

            try {
                params.put("qr_id", qrModel.getQr_id());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Allurl.TicketCheckin, params, response -> {

                Log.i("Response-->", String.valueOf(response));

                try {
                    JSONObject result = new JSONObject(String.valueOf(response));
                    String msg = result.getString("message");
                    String stat = result.getString("stat");
                    if (stat.equals("succ")) {

                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                        new TTFancyGifDialog.Builder(QRcode.this)
                                .setTitle("Checkin Successfully Done!")
                                .setPositiveBtnText("Ok")
                                .setPositiveBtnBackground("#22b573")
                                .setGifResource(R.drawable.check)      //pass your gif, png or jpg
                                .isCancellable(true)
                                .OnPositiveClicked(new TTFancyGifDialogListener() {
                                    @Override
                                    public void OnClick() {


                                    }
                                })
                                .build();
                        qrAdapter.notifyDataSetChanged();
                        QrList();

                    } else {

                        hideProgressDialog();
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                hideProgressDialog();

                //TODO: handle success
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    hideProgressDialog();
                    Toast.makeText(QRcode.this, error.toString(), Toast.LENGTH_SHORT).show();

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

    public void showcodeid (QrModel qrModel){

        AlertDialog.Builder builder = new AlertDialog.Builder(QRcode.this);
        builder.setMessage("tnevi_"+qrModel.getQr_id());
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });


        AlertDialog alert = builder.create();
        alert.show();

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