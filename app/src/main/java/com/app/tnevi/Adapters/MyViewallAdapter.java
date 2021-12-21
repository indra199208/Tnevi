package com.app.tnevi.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.tnevi.R;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.app.tnevi.Eventdetails;

import com.app.tnevi.Search2;
import com.app.tnevi.model.GeteventModel;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MyViewallAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<GeteventModel> geteventModelArrayList;
    Context ctx;
    private static final String TAG = "myapp";
    private static final int CONTENT_TYPE = 0;
    private static final int AD_TYPE = 1;

    public MyViewallAdapter(Context ctx, ArrayList<GeteventModel> geteventModelArrayList) {
        inflater = LayoutInflater.from(ctx);
        this.geteventModelArrayList = geteventModelArrayList;
        this.ctx = ctx;

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == CONTENT_TYPE) {
            View view = inflater.inflate(R.layout.rv_featuredevent1, viewGroup, false);
            return new MyViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.list_item_ad, viewGroup, false);
            return new AdRecyclerHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder.getItemViewType() == AD_TYPE) {
            AdRecyclerHolder holder = (AdRecyclerHolder) viewHolder;
            MobileAds.initialize(ctx, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {


                }
            });

            AdRequest adRequest = new AdRequest.Builder().build();
            holder.viewall_ad.loadAd(adRequest);
        } else {
            MyViewHolder holder = (MyViewHolder) viewHolder;
            holder.tvEventname.setText(geteventModelArrayList.get(viewHolder.getAdapterPosition()).getEvent_name());
            holder.tvEpointsvalue.setText("$" + geteventModelArrayList.get(viewHolder.getAdapterPosition()).getEvent_commission() + "/Ticket");
            String eventdate = geteventModelArrayList.get(viewHolder.getAdapterPosition()).getEvent_date();
            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String currentDate = df.format(c);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date1 = sdf.parse(currentDate);
                Date date2 = sdf.parse(eventdate);
                long diff = date2.getTime() - date1.getTime();
                Log.d(TAG, "diffdate-->" + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
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
                    .load("http://dev8.ivantechnology.in/tnevi/storage/app/" + geteventModelArrayList.get(viewHolder.getAdapterPosition()).getEvent_image())
                    .placeholder(R.drawable.bg7)
                    .into(holder.imgBanner);

            if (geteventModelArrayList.get(position).getFav_status().equals("0")) {
                holder.btnHeart.setLiked(false);
            } else {
                holder.btnHeart.setLiked(true);
            }


            holder.btnHeart.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                    ((Search2) ctx).addRemovefav(geteventModelArrayList.get(viewHolder.getAdapterPosition()));
                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    ((Search2) ctx).addRemovefav(geteventModelArrayList.get(viewHolder.getAdapterPosition()));
                }
            });

            holder.btnEventdetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(ctx, Eventdetails.class);
                    intent.putExtra("eventId", geteventModelArrayList.get(viewHolder.getAdapterPosition()).getId());
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
    }

    @Override
    public int getItemViewType(int position) {
        if (geteventModelArrayList.get(position) == null)
            return AD_TYPE;
        return CONTENT_TYPE;
    }

    @Override
    public int getItemCount() {
        return geteventModelArrayList.size();
    }


    static class MyViewHolder extends RecyclerView.ViewHolder {

        public CardView btnEventdetails;
        public ImageView imgBanner;
        public LikeButton btnHeart;
        public TextView tvEventname, tvEpointsvalue;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            btnEventdetails = itemView.findViewById(R.id.btnEventdetails);
            tvEpointsvalue = itemView.findViewById(R.id.tvEpointsvalue);
            tvEventname = itemView.findViewById(R.id.tvEventname);
            imgBanner = itemView.findViewById(R.id.imgBanner);
            btnHeart = itemView.findViewById(R.id.btnHeart);


        }
    }

    static class AdRecyclerHolder extends RecyclerView.ViewHolder {
        public AdView viewall_ad;

        public AdRecyclerHolder(@NonNull View itemView) {
            super(itemView);
            viewall_ad = itemView.findViewById(R.id.viewall_ad);
        }


    }

}
