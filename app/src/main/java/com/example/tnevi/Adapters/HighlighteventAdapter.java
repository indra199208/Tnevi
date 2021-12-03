package com.example.tnevi.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tnevi.Eventdetails;
import com.example.tnevi.MainActivity;
import com.example.tnevi.R;
import com.example.tnevi.model.GeteventModel;
import com.like.LikeButton;
import com.like.OnLikeListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HighlighteventAdapter extends RecyclerView.Adapter<HighlighteventAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<GeteventModel> homeEventsModelArrayList;
    Context ctx;

    public HighlighteventAdapter(Context ctx, ArrayList<GeteventModel> homeEventsModelArrayList) {
        inflater = LayoutInflater.from(ctx);
        this.homeEventsModelArrayList = homeEventsModelArrayList;
        this.ctx = ctx;
    }

    @Override
    public HighlighteventAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.rv_highlights, parent, false);
        HighlighteventAdapter.MyViewHolder holder = new HighlighteventAdapter.MyViewHolder(view);

        return holder;

    }

    @Override
    public void onBindViewHolder(HighlighteventAdapter.MyViewHolder holder, int position) {

        holder.tvEventname.setText(homeEventsModelArrayList.get(position).getEvent_name());
        holder.tvEpointsvalue.setText("$"+homeEventsModelArrayList.get(position).getEvent_commission()+"/Ticket");
        Glide.with(ctx)
                .load("http://dev8.ivantechnology.in/tnevi/storage/app/" +
                        homeEventsModelArrayList.get(position).getEvent_image())
                .placeholder(R.drawable.bg7)
                .into(holder.imgBanner);

        holder.btnHeart.setLiked(!homeEventsModelArrayList.get(position).getFav_status().equals("0"));
        holder.btnHeart.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                ((MainActivity) ctx).addRemovefav(homeEventsModelArrayList.get(position));
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                ((MainActivity) ctx).addRemovefav(homeEventsModelArrayList.get(position));
            }
        });


        holder.btnEventdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ctx, Eventdetails.class);
                intent.putExtra("eventId", homeEventsModelArrayList.get(position).getId());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                ctx.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return homeEventsModelArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        CardView btnEventdetails;
        ImageView imgBanner;
        LikeButton btnHeart;
        TextView tvEventname, tvEpointsvalue;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            btnEventdetails = itemView.findViewById(R.id.btnEventdetails);
            imgBanner = itemView.findViewById(R.id.imgBanner);
            btnHeart = itemView.findViewById(R.id.btnHeart);
            tvEventname = itemView.findViewById(R.id.tvEventname);
            tvEpointsvalue = itemView.findViewById(R.id.tvEpointsvalue);


        }
    }
}
