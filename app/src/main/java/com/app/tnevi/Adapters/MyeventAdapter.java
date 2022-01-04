package com.app.tnevi.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.app.tnevi.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.app.tnevi.Editevent;
import com.app.tnevi.Eventdetails;
import com.app.tnevi.Events;
import com.app.tnevi.Featuread;

import com.app.tnevi.model.MyeventModel;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.ArrayList;

public class MyeventAdapter extends RecyclerView.Adapter<MyeventAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<MyeventModel> myeventModelArrayList;
    Context ctx;

    public MyeventAdapter(Context ctx, ArrayList<MyeventModel> myeventModelArrayList) {
        inflater = LayoutInflater.from(ctx);
        this.myeventModelArrayList = myeventModelArrayList;
        this.ctx = ctx;

    }



    @NonNull
    @Override
    public MyeventAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.rv_myevent, parent, false);
        MyeventAdapter.MyViewHolder holder = new MyeventAdapter.MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyeventAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.tvAddress.setText(myeventModelArrayList.get(position).getEvent_address());
        holder.tvDateTime.setText(myeventModelArrayList.get(position).getEvent_date());
        holder.tvEventname.setText(myeventModelArrayList.get(position).getEvent_name());
        holder.tvTicketprice.setText("$" + myeventModelArrayList.get(position).getEvent_commission() + "/ticket");
        Glide.with(ctx)
                .load("http://dev8.ivantechnology.in/tnevi/storage/app/" + myeventModelArrayList.get(position).getEvent_image())
                .placeholder(R.drawable.bg7)
                .into(holder.imgBanner);
        switch (myeventModelArrayList.get(position).getStatus()) {
            case "0":

                holder.ll_live.setVisibility(View.GONE);
                holder.ll_review.setVisibility(View.VISIBLE);
                holder.tvStatus.setText("Pending");
                break;
            case "1":

                holder.ll_live.setVisibility(View.VISIBLE);
                holder.ll_review.setVisibility(View.GONE);
                holder.tvLive.setText("Active");

                break;
            case "2":

                holder.ll_live.setVisibility(View.GONE);
                holder.ll_review.setVisibility(View.VISIBLE);
                holder.tvStatus.setText("Upcoming");
                break;
            case "3":

                holder.ll_live.setVisibility(View.GONE);
                holder.ll_review.setVisibility(View.VISIBLE);
                holder.tvStatus.setText("In Review");

                break;
            case "4":

                holder.ll_live.setVisibility(View.VISIBLE);
                holder.ll_review.setVisibility(View.GONE);
                holder.tvLive.setText("Live");
                break;
            case "5":


                holder.ll_live.setVisibility(View.GONE);
                holder.ll_review.setVisibility(View.VISIBLE);
                holder.tvStatus.setText("Completed");

                break;
            case "6":

                holder.ll_live.setVisibility(View.GONE);
                holder.ll_review.setVisibility(View.VISIBLE);
                holder.tvStatus.setText("Canceled");

                break;
            default:

                holder.ll_live.setVisibility(View.GONE);
                holder.ll_review.setVisibility(View.VISIBLE);
                holder.tvStatus.setText("Nil");

                break;
        }


        holder.btnEditevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ctx, Editevent.class);
                intent.putExtra("eventId", myeventModelArrayList.get(position).getId());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                ctx.startActivity(intent);


            }
        });


        if(myeventModelArrayList.get(position).getFav_status().equals("0")){
            holder.btnHeart.setLiked(false);
        }else{
            holder.btnHeart.setLiked(true);
        }


        holder.btnHeart.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {

                ((Events) ctx).addRemovefav(myeventModelArrayList.get(position));

            }

            @Override
            public void unLiked(LikeButton likeButton) {

                ((Events) ctx).addRemovefav(myeventModelArrayList.get(position));

            }
        });


        holder.btnMarksold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((Events) ctx).MarkSold(myeventModelArrayList.get(position));

            }
        });


        holder.btn_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ctx, Eventdetails.class);
                intent.putExtra("eventId", myeventModelArrayList.get(position).getId());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                ctx.startActivity(intent);

            }
        });


        holder.btnFeatureNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ctx, Featuread.class);
                intent.putExtra("eventId", myeventModelArrayList.get(position).getId());
                ctx.startActivity(intent);

            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((Events) ctx).deleteEvent(myeventModelArrayList.get(position),holder.getAdapterPosition());

            }
        });

    }

    @Override
    public int getItemCount() {
        return myeventModelArrayList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imgBanner;
        LinearLayout ll_live, ll_review, btnEditevent, btn_details;
        TextView tvDateTime, tvAddress, tvEventname, tvTicketprice, tvStatus, tvLive;
        Button btnMarksold, btnFeatureNow, btnDelete;
        LikeButton btnHeart;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBanner = itemView.findViewById(R.id.imgBanner);
            ll_live = itemView.findViewById(R.id.ll_live);
            ll_review = itemView.findViewById(R.id.ll_review);
            btnMarksold = itemView.findViewById(R.id.btnMarksold);
            btnFeatureNow = itemView.findViewById(R.id.btnFeatureNow);
            btnEditevent = itemView.findViewById(R.id.btnEditevent);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvEventname = itemView.findViewById(R.id.tvEventname);
            tvTicketprice = itemView.findViewById(R.id.tvTicketprice);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvLive = itemView.findViewById(R.id.tvLive);
            btnHeart = itemView.findViewById(R.id.btnHeart);
            btn_details = itemView.findViewById(R.id.btn_details);



        }
    }
}
