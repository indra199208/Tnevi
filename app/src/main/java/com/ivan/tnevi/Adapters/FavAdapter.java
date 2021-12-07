package com.ivan.tnevi.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ivan.tnevi.Eventdetails;
import com.ivan.tnevi.Favticket;
import com.ivan.tnevi.Gallerybooking;
import com.example.tnevi.R;
import com.ivan.tnevi.model.GeteventModel;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.ArrayList;

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<GeteventModel> geteventModelArrayList;
    Context ctx;

    public FavAdapter(Context ctx, ArrayList<GeteventModel> geteventModelArrayList) {
        inflater = LayoutInflater.from(ctx);
        this.geteventModelArrayList = geteventModelArrayList;
        this.ctx = ctx;

    }

    @NonNull
    @Override
    public FavAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.rv_favlist, parent, false);
        FavAdapter.MyViewHolder holder = new FavAdapter.MyViewHolder(view);

        return holder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull FavAdapter.MyViewHolder holder, int position) {

        holder.tvAddress.setText(geteventModelArrayList.get(position).getEvent_address());
        holder.tvDateTime.setText(geteventModelArrayList.get(position).getEvent_date());
        holder.tvEventname.setText(geteventModelArrayList.get(position).getEvent_name());
        holder.tvTicketprice.setText("$" + geteventModelArrayList.get(position).getEvent_commission() + "/ticket");
        holder.tvCommission.setText("$"+geteventModelArrayList.get(position).getEvent_commission());
        Glide.with(ctx)
                .load("http://dev8.ivantechnology.in/tnevi/storage/app/" + geteventModelArrayList.get(position).getEvent_image())
                .placeholder(R.drawable.bg7)
                .into(holder.imgBanner);

        holder.btnHeart.setLiked(true);

        holder.btnHeart.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {



            }

            @Override
            public void unLiked(LikeButton likeButton) {

                ((Favticket) ctx).addRemovefav(geteventModelArrayList.get(position));


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


        holder.btnBuytickt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ctx, Gallerybooking.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                ctx.startActivity(intent);
            }
        });


        holder.btnEarncash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "https://bit.ly/3hr6kz8";
                String shareSub = "Your subject here";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSub);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                ctx.startActivity (Intent.createChooser(sharingIntent, "Share using"));
            }
        });



    }

    @Override
    public int getItemCount() {
        return geteventModelArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imgBanner;
        LikeButton btnHeart;
        TextView tvEventname, tvTicketprice, tvAddress, tvDateTime, tvCommission;
        LinearLayout btnBuytickt, btnEarncash, btn_details;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBanner = itemView.findViewById(R.id.imgBanner);
            btnHeart = itemView.findViewById(R.id.btnHeart);
            tvEventname = itemView.findViewById(R.id.tvEventname);
            tvTicketprice = itemView.findViewById(R.id.tvTicketprice);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            tvCommission = itemView.findViewById(R.id.tvCommission);
            btnBuytickt = itemView.findViewById(R.id.btnBuytickt);
            btnEarncash = itemView.findViewById(R.id.btnEarncash);
            btn_details = itemView.findViewById(R.id.btn_details);

        }
    }

}
