package com.example.tnevi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
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
import com.example.tnevi.Allurl.Allurl;
import com.example.tnevi.internet.CheckConnectivity;
import com.google.firebase.encoders.EncodingException;
import com.google.zxing.WriterException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class QRcode extends AppCompatActivity {

    TextView tvSection, tvRow, tvSeat, tvButtonStatus;
    ImageView btn_back, img_show_qr;
    String section,row,seat, qr, status;
    Bitmap bitmap;
    QRGEncoder qrgEncoder;
    LinearLayout btnReedem;
    String token;
    private static final String SHARED_PREFS = "sharedPrefs";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        Intent intent = getIntent();
        section = intent.getStringExtra("section");
        row = intent.getStringExtra("row");
        seat = intent.getStringExtra("seat");
        qr = intent.getStringExtra("qr");
        status = intent.getStringExtra("status");
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        btn_back = findViewById(R.id.btn_back);
        tvSection = findViewById(R.id.tvSection);
        tvRow = findViewById(R.id.tvRow);
        tvSeat = findViewById(R.id.tvSeat);
        btnReedem = findViewById(R.id.btnReedem);
        tvButtonStatus = findViewById(R.id.tvButtonStatus);

        img_show_qr = findViewById(R.id.img_show_qr);
        tvSection.setText(section);
        tvRow.setText(row);
        tvSeat.setText(seat);
        if (status.equals("0")){
            tvButtonStatus.setText("CHECK IN NOW");
        }else {
            tvButtonStatus.setText("ALREADY REDDEMED");
        }
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int dimen = Math.min(width, height);
        dimen = dimen * 3 / 4;
        qrgEncoder = new QRGEncoder(qr, null, QRGContents.Type.TEXT, dimen);
        try {
            bitmap = qrgEncoder.encodeAsBitmap();
            img_show_qr.setImageBitmap(bitmap);
        } catch (WriterException e) {
            Log.e("Tag", e.toString());
        }


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnReedem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (status.equals("0")){
                    checkin();
                }else {
                    Toast.makeText(QRcode.this, "ALREADY REDDEMED", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void checkin(){

        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {

            showProgressDialog();

            JSONObject params = new JSONObject();

            try {
                params.put("qr_id", qr);

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
                        Intent intent = new Intent(QRcode.this, Ticketdetails.class);
                        startActivity(intent);

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