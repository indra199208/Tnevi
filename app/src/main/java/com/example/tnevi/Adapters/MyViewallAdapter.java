package com.example.tnevi.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tnevi.Eventdetails;
import com.example.tnevi.R;
import com.example.tnevi.Search;
import com.example.tnevi.Search2;
import com.example.tnevi.Ticketdetails;
import com.example.tnevi.model.GeteventModel;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MyViewallAdapter extends RecyclerView.Adapter<MyViewallAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<GeteventModel> geteventModelArrayList;
    Context ctx;
    private static final String TAG = "myapp";


    public MyViewallAdapter(Context ctx, ArrayList<GeteventModel> geteventModelArrayList) {
        inflater = LayoutInflater.from(ctx);
        this.geteventModelArrayList = geteventModelArrayList;
        this.ctx = ctx;

    }


    @NonNull
    @Override
    public MyViewallAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.rv_featuredevent, parent, false);
        MyViewallAdapter.MyViewHolder holder = new MyViewallAdapter.MyViewHolder(view);

        return holder;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull MyViewallAdapter.MyViewHolder holder, int position) {

//        holder.tvAddress.setText(geteventModelArrayList.get(position).getEvent_address());
//        holder.tvEventDate.setText(geteventModelArrayList.get(position).getEvent_date());
        holder.tvEventname.setText(geteventModelArrayList.get(position).getEvent_name());
        holder.tvEpointsvalue.setText("$" + geteventModelArrayList.get(position).getEvent_commission() + "/Ticket");
        String eventdate = geteventModelArrayList.get(position).getEvent_date();
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = df.format(c);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date1 = sdf.parse(currentDate);
            Date date2 = sdf.parse(eventdate);
            long diff = date2.getTime() - date1.getTime();
            Log.d(TAG, "diffdate-->"+ TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) );
            String dateoutput = String.valueOf(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
            char d = dateoutput.charAt(0);
            String s = String.valueOf(d);
//            if (s.equals("-")){
//                holder.tvReamining.setText("Already Completed");
//                holder.tvReamining.setTextColor(Color.RED);
//
//            }else {
//                holder.tvReamining.setText(Math.toIntExact(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS))+" Days");
//            }


        } catch (ParseException e) {
            e.printStackTrace();
        }






        Glide.with(ctx)
                .load("http://dev8.ivantechnology.in/tnevi/storage/app/" + geteventModelArrayList.get(position).getEvent_image())
                .placeholder(R.drawable.bg7)
                .into(holder.imgBanner);

        if(geteventModelArrayList.get(position).getFav_status().equals("0")){
            holder.btnHeart.setLiked(false);
        }else{
            holder.btnHeart.setLiked(true);
        }


        holder.btnHeart.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                ((Search2) ctx).addRemovefav(geteventModelArrayList.get(position));
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                ((Search2) ctx).addRemovefav(geteventModelArrayList.get(position));
            }
        });

        holder.btnEventdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ctx, Eventdetails.class);
                intent.putExtra("eventId", geteventModelArrayList.get(position).getId());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                ctx.startActivity(intent);

            }
        });

//        holder.btnViewticket.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent intent = new Intent(ctx, Ticketdetails.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                ctx.startActivity(intent);
//
//            }
//        });


    }

    @Override
    public int getItemCount() {
        return geteventModelArrayList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        CardView btnEventdetails;
        ImageView imgBanner;
        LikeButton btnHeart;
        TextView tvEventname, tvEpointsvalue;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            btnEventdetails = itemView.findViewById(R.id.btnEventdetails);
            tvEpointsvalue = itemView.findViewById(R.id.tvEpointsvalue);
            tvEventname = itemView.findViewById(R.id.tvEventname);
            imgBanner = itemView.findViewById(R.id.imgBanner);
            btnHeart = itemView.findViewById(R.id.btnHeart);



        }
    }

}
