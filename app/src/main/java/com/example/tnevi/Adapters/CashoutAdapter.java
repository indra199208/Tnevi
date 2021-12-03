package com.example.tnevi.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tnevi.R;
import com.example.tnevi.model.CashoutModel;
import com.example.tnevi.model.TicketReportModel;

import java.util.ArrayList;

public class CashoutAdapter extends RecyclerView.Adapter<CashoutAdapter.MyViewHolder>{

    private LayoutInflater inflater;
    private ArrayList<CashoutModel> cashoutModelArrayList;
    Context ctx;

    public CashoutAdapter(Context ctx, ArrayList<CashoutModel> cashoutModelArrayList) {
        inflater = LayoutInflater.from(ctx);
        this.cashoutModelArrayList = cashoutModelArrayList;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public CashoutAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.rv_cashout, parent, false);
        CashoutAdapter.MyViewHolder holder = new CashoutAdapter.MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CashoutAdapter.MyViewHolder holder, int position) {

        holder.tvAddress.setText(cashoutModelArrayList.get(position).getEvent_address());
        holder.tvEvent.setText(cashoutModelArrayList.get(position).getEvent_name());
        holder.tvDate.setText(cashoutModelArrayList.get(position).getEvent_date());
        holder.tvCashout.setText("$"+cashoutModelArrayList.get(position).getTran_amount());
        holder.tvDate2.setText(cashoutModelArrayList.get(position).getEvent_date());

    }

    @Override
    public int getItemCount() {
        return cashoutModelArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvEvent, tvDate, tvAddress, tvCashout, tvDate2;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEvent = itemView.findViewById(R.id.tvEvent);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvCashout = itemView.findViewById(R.id.tvCashout);
            tvDate2 = itemView.findViewById(R.id.tvDate2);
        }
    }
}
