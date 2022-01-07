package com.app.tnevi.Adapters;

import static android.content.Context.WINDOW_SERVICE;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tnevi.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.tnevi.QRcode;
import com.app.tnevi.model.QrModel;

import com.google.zxing.WriterException;

import java.util.ArrayList;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class QrAdapter extends RecyclerView.Adapter<QrAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<QrModel> qrModelArrayList;
    Context ctx;
    Bitmap bitmap;
    QRGEncoder qrgEncoder;

    public QrAdapter(Context ctx, ArrayList<QrModel> qrModelArrayList) {
        inflater = LayoutInflater.from(ctx);
        this.qrModelArrayList = qrModelArrayList;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public QrAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.rv_qrcode, parent, false);
        QrAdapter.MyViewHolder holder = new QrAdapter.MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull QrAdapter.MyViewHolder holder, int position) {

        String status = qrModelArrayList.get(position).getStatus();
        holder.tvRow.setText(qrModelArrayList.get(position).getRow_name());
        holder.tvSeat.setText(qrModelArrayList.get(position).getSeat_no());
        holder.tvSection.setText(qrModelArrayList.get(position).getBlock_name());
        if (status.equals("0")) {
            holder.tvButtonStatus.setText("CHECK IN NOW");
        } else {
            holder.tvButtonStatus.setText("ALREADY REDDEMED");
        }
        holder.btnReedem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (status.equals("0")) {
                    ((QRcode) ctx).checkin(qrModelArrayList.get(position));

                } else {
                    Toast.makeText(ctx, "ALREADY REDDEMED", Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.img_show_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (status.equals("0")) {
                    Toast.makeText(ctx, "Need to Checkin for getting the code!", Toast.LENGTH_SHORT).show();

                } else {
                    ((QRcode) ctx).showcodeid(qrModelArrayList.get(position));
                }
            }
        });
        WindowManager manager = (WindowManager) ctx.getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int dimen = Math.min(width, height);
        dimen = dimen * 3 / 4;
        qrgEncoder = new QRGEncoder(qrModelArrayList.get(position).getQr_id(), null, QRGContents.Type.TEXT, dimen);
        try {
            bitmap = qrgEncoder.encodeAsBitmap();
            holder.img_show_qr.setImageBitmap(bitmap);
        } catch (WriterException e) {
            Log.e("Tag", e.toString());
        }

    }

    @Override
    public int getItemCount() {
        return qrModelArrayList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvRow, tvSeat, tvButtonStatus, tvSection;
        ImageView img_show_qr;
        LinearLayout btnReedem;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRow = itemView.findViewById(R.id.tvRow);
            tvSeat = itemView.findViewById(R.id.tvSeat);
            img_show_qr = itemView.findViewById(R.id.img_show_qr);
            btnReedem = itemView.findViewById(R.id.btnReedem);
            tvButtonStatus = itemView.findViewById(R.id.tvButtonStatus);
            tvSection = itemView.findViewById(R.id.tvSection);

        }
    }

}
