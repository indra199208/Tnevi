package com.app.tnevi.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.app.tnevi.Comissionsales;
import com.app.tnevi.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.app.tnevi.model.TicketReportModel;

import java.util.ArrayList;

public class CommissionsalesAdapter extends RecyclerView.Adapter<CommissionsalesAdapter.MyViewHolder>{

    private LayoutInflater inflater;
    private ArrayList<TicketReportModel> ticketReportModelArrayList;
    Context ctx;
    private int selectedPosition = -1;// no selection by default



    public CommissionsalesAdapter(Context ctx, ArrayList<TicketReportModel> ticketReportModelArrayList) {
        inflater = LayoutInflater.from(ctx);
        this.ticketReportModelArrayList = ticketReportModelArrayList;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public CommissionsalesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.rv_ticketsales, parent, false);
        CommissionsalesAdapter.MyViewHolder holder = new CommissionsalesAdapter.MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommissionsalesAdapter.MyViewHolder holder, int position) {

        holder.radioButton.setChecked(selectedPosition == position);
        holder.tvEventname.setText(ticketReportModelArrayList.get(position).getEvent_name());
        holder.tvAddress.setText(ticketReportModelArrayList.get(position).getEvent_address());
        holder.tvEventdate.setText(ticketReportModelArrayList.get(position).getEvent_date());
        holder.tvTicketsales.setText("$"+ticketReportModelArrayList.get(position).getTotal_sale());
        if (selectedPosition == position) {
            holder.radioButton.setChecked(true);
        } else {
            holder.radioButton.setChecked(false);
        }


        holder.radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                selectedPosition = holder.getAdapterPosition();
                if (ctx instanceof Comissionsales) {
                    ((Comissionsales) ctx).cashoutselectedevent(ticketReportModelArrayList.get(selectedPosition).getId());
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return ticketReportModelArrayList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvEventname, tvEventdate, tvAddress, tvTicketsales;
        RadioButton radioButton;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEventname = itemView.findViewById(R.id.tvEventname);
            tvEventdate = itemView.findViewById(R.id.tvEventdate);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvTicketsales = itemView.findViewById(R.id.tvTicketsales);
            radioButton = itemView.findViewById(R.id.radioButton);

        }
    }
}
