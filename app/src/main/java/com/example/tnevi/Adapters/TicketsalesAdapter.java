package com.example.tnevi.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tnevi.Comissionsales;
import com.example.tnevi.R;
import com.example.tnevi.model.TicketReportModel;

import java.util.ArrayList;

public class TicketsalesAdapter extends RecyclerView.Adapter<TicketsalesAdapter.MyViewHolder>{

    private LayoutInflater inflater;
    private ArrayList<TicketReportModel> ticketReportModelArrayList;
    Context ctx;
    private int selectedPosition = -1;// no selection by default



    public TicketsalesAdapter(Context ctx, ArrayList<TicketReportModel> ticketReportModelArrayList) {
        inflater = LayoutInflater.from(ctx);
        this.ticketReportModelArrayList = ticketReportModelArrayList;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public TicketsalesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.rv_ticketsales, parent, false);
        TicketsalesAdapter.MyViewHolder holder = new TicketsalesAdapter.MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TicketsalesAdapter.MyViewHolder holder, int position) {

        holder.checkBox.setChecked(selectedPosition == position);
        holder.tvEventname.setText(ticketReportModelArrayList.get(position).getEvent_name());
        holder.tvAddress.setText(ticketReportModelArrayList.get(position).getEvent_address());
        holder.tvEventdate.setText(ticketReportModelArrayList.get(position).getEvent_date());
        holder.tvTicketsales.setText("$"+ticketReportModelArrayList.get(position).getTotal_sale());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                selectedPosition = holder.getAdapterPosition();

            }
        });
    }

    @Override
    public int getItemCount() {
        return ticketReportModelArrayList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvEventname, tvEventdate, tvAddress, tvTicketsales;
        CheckBox checkBox;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEventname = itemView.findViewById(R.id.tvEventname);
            tvEventdate = itemView.findViewById(R.id.tvEventdate);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvTicketsales = itemView.findViewById(R.id.tvTicketsales);
            checkBox = itemView.findViewById(R.id.checkBox);

        }
    }
}