package com.example.tnevi.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tnevi.Eventdetails;
import com.example.tnevi.Events;
import com.example.tnevi.QRcode;
import com.example.tnevi.R;
import com.example.tnevi.Ticketdetails;
import com.example.tnevi.model.TicketModel;

import java.util.ArrayList;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<TicketModel> ticketModelArrayList;
    Context ctx;


    public TicketAdapter(Context ctx, ArrayList<TicketModel> ticketModelArrayList) {
        inflater = LayoutInflater.from(ctx);
        this.ticketModelArrayList = ticketModelArrayList;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public TicketAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.rv_ticket, parent, false);
        TicketAdapter.MyViewHolder holder = new TicketAdapter.MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TicketAdapter.MyViewHolder holder, int position) {

        holder.tvEventName.setText(ticketModelArrayList.get(position).getEvent_name());
        holder.tvDate.setText(ticketModelArrayList.get(position).getEvent_date());
        holder.tvAddress.setText(ticketModelArrayList.get(position).getEvent_address());
        holder.tvSeat.setText(ticketModelArrayList.get(position).getSeat_no());
        holder.tvRow.setText(ticketModelArrayList.get(position).getRow_name());
        holder.tvSection.setText(ticketModelArrayList.get(position).getBlock_name());
        holder.ll_ticketdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ctx, QRcode.class);
                intent.putExtra("booking_id", ticketModelArrayList.get(position).getId());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                ctx.startActivity(intent);


            }
        });

        holder.btnViewTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((Ticketdetails) ctx).ViewTickets(ticketModelArrayList.get(position));


            }
        });

        holder.btnDownloadticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((Ticketdetails) ctx).downloadTicket(ticketModelArrayList.get(position));


            }
        });

    }

    @Override
    public int getItemCount() {
        return ticketModelArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvEventName, tvDate, tvAddress, tvSeat, tvSection, tvRow;
        LinearLayout ll_ticketdetails, btnDownloadticket, btnViewTicket;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEventName = itemView.findViewById(R.id.tvEventName);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            ll_ticketdetails = itemView.findViewById(R.id.ll_ticketdetails);
            tvSeat = itemView.findViewById(R.id.tvSeat);
            tvSection = itemView.findViewById(R.id.tvSection);
            tvRow = itemView.findViewById(R.id.tvRow);
            btnDownloadticket = itemView.findViewById(R.id.btnDownloadticket);
            btnViewTicket = itemView.findViewById(R.id.btnViewTicket);



        }
    }
}
