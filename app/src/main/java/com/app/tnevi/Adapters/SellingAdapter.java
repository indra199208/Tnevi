package com.app.tnevi.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.app.tnevi.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.app.tnevi.Eventdetails;

import com.app.tnevi.Sellingticket;
import com.app.tnevi.model.GeteventModel;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.ArrayList;

public class SellingAdapter extends RecyclerView.Adapter<SellingAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<GeteventModel> geteventModelArrayList;
    Context ctx;

    public SellingAdapter(Context ctx, ArrayList<GeteventModel> geteventModelArrayList) {
        inflater = LayoutInflater.from(ctx);
        this.geteventModelArrayList = geteventModelArrayList;
        this.ctx = ctx;

    }

    @NonNull
    @Override
    public SellingAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.rv_selling, parent, false);
        SellingAdapter.MyViewHolder holder = new SellingAdapter.MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SellingAdapter.MyViewHolder holder, int position) {

        holder.tvEventAddress.setText(geteventModelArrayList.get(position).getEvent_address());
        holder.tvEventDate.setText(geteventModelArrayList.get(position).getEvent_date());
        holder.tvEventname.setText(geteventModelArrayList.get(position).getEvent_name());
        holder.tvMaxprice.setText("$" + geteventModelArrayList.get(position).getEvent_commission()+ "/ticket");
        holder.tvCommission.setText("$" + geteventModelArrayList.get(position).getEvent_commission());
        Glide.with(ctx)
                .load("http://dev8.ivantechnology.in/tnevi/storage/app/" + geteventModelArrayList.get(position).getEvent_image())
                .placeholder(R.drawable.bg7)
                .into(holder.imgBanner);

        switch (geteventModelArrayList.get(position).getStatus()) {
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

        if (geteventModelArrayList.get(position).getFav_status().equals("0")) {
            holder.btnHeart.setLiked(false);
        } else {
            holder.btnHeart.setLiked(true);
        }


        holder.btnHeart.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                ((Sellingticket) ctx).addRemovefav(geteventModelArrayList.get(position));
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                ((Sellingticket) ctx).addRemovefav(geteventModelArrayList.get(position));
            }
        });


        holder.btn_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ctx, Eventdetails.class);
                intent.putExtra("eventId", geteventModelArrayList.get(position).getId());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                ctx.startActivity(intent);

            }
        });


        holder.btnEarnCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "https://bit.ly/3hr6kz8";
                String shareSub = "Your subject here";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSub);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                ctx.startActivity(Intent.createChooser(sharingIntent, "Share using"));
            }
        });


    }

    @Override
    public int getItemCount() {
        return geteventModelArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imgBanner;
        LinearLayout ll_live, ll_review, btnEarnCash, btn_details;
        TextView tvEventname, tvMaxprice, tvEventDate, tvEventAddress, tvCommission, tvStatus, tvLive;
        LikeButton btnHeart;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imgBanner = itemView.findViewById(R.id.imgBanner);
            ll_live = itemView.findViewById(R.id.ll_live);
            ll_review = itemView.findViewById(R.id.ll_review);
            ll_review = itemView.findViewById(R.id.ll_review);
            btnEarnCash = itemView.findViewById(R.id.btnEarnCash);
            tvEventname = itemView.findViewById(R.id.tvEventname);
            tvMaxprice = itemView.findViewById(R.id.tvMaxprice);
            tvEventDate = itemView.findViewById(R.id.tvEventDate);
            tvEventAddress = itemView.findViewById(R.id.tvEventAddress);
            tvCommission = itemView.findViewById(R.id.tvCommission);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvLive = itemView.findViewById(R.id.tvLive);
            btnHeart = itemView.findViewById(R.id.btnHeart);
            btn_details = itemView.findViewById(R.id.btn_details);


        }
    }
}
