package com.example.tnevi.Adapters;

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
import com.example.tnevi.Commissionsearned;
import com.example.tnevi.Eventgeneraldetails;
import com.example.tnevi.QRcode;
import com.example.tnevi.R;
import com.example.tnevi.Ticketsalesreport;
import com.example.tnevi.model.TicketModel;
import com.example.tnevi.model.TicketReportModel;

import java.util.ArrayList;

public class TicketReportAdapter extends RecyclerView.Adapter<TicketReportAdapter.MyViewHolder>{

    private LayoutInflater inflater;
    private ArrayList<TicketReportModel> ticketReportModelArrayList;
    Context ctx;

    public TicketReportAdapter(Context ctx, ArrayList<TicketReportModel> ticketReportModelArrayList) {
        inflater = LayoutInflater.from(ctx);
        this.ticketReportModelArrayList = ticketReportModelArrayList;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public TicketReportAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.rv_ticketsalesreport, parent, false);
        TicketReportAdapter.MyViewHolder holder = new TicketReportAdapter.MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TicketReportAdapter.MyViewHolder holder, int position) {

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


                ((Ticketsalesreport) ctx).cashout(ticketReportModelArrayList.get(position));

            }
        });

        holder.btnDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ctx, Eventgeneraldetails.class);
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
