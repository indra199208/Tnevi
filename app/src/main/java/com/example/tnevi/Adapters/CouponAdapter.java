package com.example.tnevi.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tnevi.Pricedetails;
import com.example.tnevi.R;
import com.example.tnevi.model.CouponModel;

import java.util.ArrayList;

public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<CouponModel> couponModelArrayList;
    Context ctx;

    public CouponAdapter(Context ctx, ArrayList<CouponModel> couponModelArrayList) {
        inflater = LayoutInflater.from(ctx);
        this.couponModelArrayList = couponModelArrayList;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public CouponAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.rv_coupon, parent, false);
        CouponAdapter.MyViewHolder holder = new CouponAdapter.MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CouponAdapter.MyViewHolder holder, int position) {

        holder.tvCash.setText("$" + couponModelArrayList.get(position).getCoupon_amount());
        holder.btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                ((Pricedetails) ctx).applyCoupon(couponModelArrayList.get(position),holder.getAdapterPosition());
                holder.btnApply.setVisibility(View.GONE);
                holder.tvApplied.setVisibility(View.VISIBLE);

            }
        });

    }

    @Override
    public int getItemCount() {
        return couponModelArrayList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvCash, tvApplied;
        LinearLayout btnApply;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCash = itemView.findViewById(R.id.tvCash);
            btnApply = itemView.findViewById(R.id.btnApply);
            tvApplied = itemView.findViewById(R.id.tvApplied);


        }
    }

}
