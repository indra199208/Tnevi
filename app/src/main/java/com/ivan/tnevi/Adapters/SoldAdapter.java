package com.ivan.tnevi.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tnevi.R;
import com.ivan.tnevi.model.GeteventModel;

import java.util.ArrayList;

public class SoldAdapter extends RecyclerView.Adapter<SoldAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<GeteventModel> geteventModelArrayList;
    Context ctx;

    public SoldAdapter(Context ctx, ArrayList<GeteventModel> geteventModelArrayList) {
        inflater = LayoutInflater.from(ctx);
        this.geteventModelArrayList = geteventModelArrayList;
        this.ctx = ctx;

    }

    @NonNull
    @Override
    public SoldAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.rv_soldticket, parent, false);
        SoldAdapter.MyViewHolder holder = new SoldAdapter.MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SoldAdapter.MyViewHolder holder, int position) {

        holder.tvEventAddress.setText(geteventModelArrayList.get(position).getEvent_address());
        holder.tvEventDate.setText(geteventModelArrayList.get(position).getEvent_date());
        holder.tvEventname.setText(geteventModelArrayList.get(position).getEvent_name());
        holder.tvMaxprice.setText("$" +geteventModelArrayList.get(position).getEvent_commission()+ "/ticket");
        holder.tvCommission.setText("$"+geteventModelArrayList.get(position).getEvent_commission());
        Glide.with(ctx)
                .load("http://dev8.ivantechnology.in/tnevi/storage/app/" + geteventModelArrayList.get(position).getEvent_image())
                .placeholder(R.drawable.bg7)
                .into(holder.imgBanner);
        String eventstatus = geteventModelArrayList.get(position).getStatus();
        switch (eventstatus) {
            case "0":
                holder.ll_pending.setVisibility(View.VISIBLE);
                holder.ll_live.setVisibility(View.GONE);
                holder.ll_past.setVisibility(View.GONE);
                holder.ll_active.setVisibility(View.GONE);
                holder.ll_upcoming.setVisibility(View.GONE);
                holder.ll_review.setVisibility(View.GONE);
                holder.ll_cancel.setVisibility(View.GONE);
                break;
            case "1":
                holder.ll_pending.setVisibility(View.GONE);
                holder.ll_live.setVisibility(View.GONE);
                holder.ll_past.setVisibility(View.GONE);
                holder.ll_active.setVisibility(View.VISIBLE);
                holder.ll_upcoming.setVisibility(View.GONE);
                holder.ll_review.setVisibility(View.GONE);
                holder.ll_cancel.setVisibility(View.GONE);
                break;
            case "2":
                holder.ll_pending.setVisibility(View.GONE);
                holder.ll_live.setVisibility(View.GONE);
                holder.ll_past.setVisibility(View.GONE);
                holder.ll_active.setVisibility(View.GONE);
                holder.ll_upcoming.setVisibility(View.VISIBLE);
                holder.ll_review.setVisibility(View.GONE);
                holder.ll_cancel.setVisibility(View.GONE);
                break;
            case "3":
                holder.ll_pending.setVisibility(View.GONE);
                holder.ll_live.setVisibility(View.GONE);
                holder.ll_past.setVisibility(View.GONE);
                holder.ll_active.setVisibility(View.GONE);
                holder.ll_upcoming.setVisibility(View.GONE);
                holder.ll_review.setVisibility(View.VISIBLE);
                holder.ll_cancel.setVisibility(View.GONE);
                break;
            case "4":
                holder.ll_pending.setVisibility(View.GONE);
                holder.ll_live.setVisibility(View.VISIBLE);
                holder.ll_past.setVisibility(View.GONE);
                holder.ll_active.setVisibility(View.GONE);
                holder.ll_upcoming.setVisibility(View.GONE);
                holder.ll_review.setVisibility(View.GONE);
                holder.ll_cancel.setVisibility(View.GONE);
                break;
            case "5":
                holder.ll_pending.setVisibility(View.GONE);
                holder.ll_live.setVisibility(View.GONE);
                holder.ll_past.setVisibility(View.VISIBLE);
                holder.ll_active.setVisibility(View.GONE);
                holder.ll_upcoming.setVisibility(View.GONE);
                holder.ll_review.setVisibility(View.GONE);
                holder.ll_cancel.setVisibility(View.GONE);
                break;
            case "6":
                holder.ll_pending.setVisibility(View.GONE);
                holder.ll_live.setVisibility(View.GONE);
                holder.ll_past.setVisibility(View.GONE);
                holder.ll_active.setVisibility(View.GONE);
                holder.ll_upcoming.setVisibility(View.GONE);
                holder.ll_review.setVisibility(View.GONE);
                holder.ll_cancel.setVisibility(View.VISIBLE);
                break;
        }



    }

    @Override
    public int getItemCount() {
        return geteventModelArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imgBanner;
        TextView tvEventname, tvMaxprice, tvEventDate, tvEventAddress, tvCommission;
        LinearLayout btnViewreport;
        LinearLayout ll_live, ll_past, ll_pending, ll_active, ll_upcoming, ll_review, ll_cancel;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imgBanner = itemView.findViewById(R.id.imgBanner);
            tvEventname = itemView.findViewById(R.id.tvEventname);
            tvMaxprice = itemView.findViewById(R.id.tvMaxprice);
            tvEventDate = itemView.findViewById(R.id.tvEventDate);
            tvEventAddress = itemView.findViewById(R.id.tvEventAddress);
            tvCommission = itemView.findViewById(R.id.tvCommission);
            btnViewreport = itemView.findViewById(R.id.btnViewreport);
            ll_live = itemView.findViewById(R.id.ll_live);
            ll_past = itemView.findViewById(R.id.ll_past);
            ll_pending = itemView.findViewById(R.id.ll_pending);
            ll_active = itemView.findViewById(R.id.ll_active);
            ll_upcoming = itemView.findViewById(R.id.ll_upcoming);
            ll_review = itemView.findViewById(R.id.ll_review);
            ll_cancel = itemView.findViewById(R.id.ll_cancel);

        }
    }
}
