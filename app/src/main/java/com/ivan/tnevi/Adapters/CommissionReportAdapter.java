package com.ivan.tnevi.Adapters;

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
import com.ivan.tnevi.Comissiondetails;
import com.ivan.tnevi.Commissionsearned;
import com.example.tnevi.R;
import com.ivan.tnevi.model.TicketReportModel;

import java.util.ArrayList;

public class CommissionReportAdapter extends RecyclerView.Adapter<CommissionReportAdapter.MyViewHolder>{

    private LayoutInflater inflater;
    private ArrayList<TicketReportModel> ticketReportModelArrayList;
    Context ctx;

    public CommissionReportAdapter(Context ctx, ArrayList<TicketReportModel> ticketReportModelArrayList) {
        inflater = LayoutInflater.from(ctx);
        this.ticketReportModelArrayList = ticketReportModelArrayList;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public CommissionReportAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.rv_ticketsalesreport, parent, false);
        CommissionReportAdapter.MyViewHolder holder = new CommissionReportAdapter.MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommissionReportAdapter.MyViewHolder holder, int position) {

        holder.tvEventname.setText(ticketReportModelArrayList.get(position).getEvent_name());
        holder.tvDate.setText(ticketReportModelArrayList.get(position).getEvent_date());
        holder.tvSalesEarned.setText("$"+ticketReportModelArrayList.get(position).getTotal_sale());
        holder.tvTotalsale.setText(ticketReportModelArrayList.get(position).getTotal_ticket());
        Glide.with(ctx)
                .load("http://dev8.ivantechnology.in/tnevi/storage/app/" + ticketReportModelArrayList.get(position).getEvent_image())
                .placeholder(R.drawable.bg7)
                .into(holder.imgBanner);
        holder.btnCashout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((Commissionsearned) ctx).cashout(ticketReportModelArrayList.get(position));


            }
        });

        holder.btnDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ctx, Comissiondetails.class);
                intent.putExtra("id", ticketReportModelArrayList.get(position).getId());
                ctx.startActivity(intent);

            }
        });

        String eventstatus = ticketReportModelArrayList.get(position).getStatus();
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
        return ticketReportModelArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvEventname, tvDate, tvSalesEarned, tvTotalsale;
        LinearLayout btnDetails, btnCashout;
        ImageView imgBanner;
        LinearLayout ll_live, ll_past, ll_pending, ll_active, ll_upcoming, ll_review, ll_cancel;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEventname = itemView.findViewById(R.id.tvEventname);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvSalesEarned = itemView.findViewById(R.id.tvSalesEarned);
            tvTotalsale = itemView.findViewById(R.id.tvTotalsale);
            btnDetails = itemView.findViewById(R.id.btnDetails);
            btnCashout = itemView.findViewById(R.id.btnCashout);
            imgBanner = itemView.findViewById(R.id.imgBanner);
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
