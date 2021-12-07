package com.ivan.tnevi.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tnevi.R;
import com.ivan.tnevi.model.CommissionReportDetailsModel;

import java.util.ArrayList;

public class CommissionDetailsAdapter extends RecyclerView.Adapter<CommissionDetailsAdapter.MyViewHolder>{

    private LayoutInflater inflater;
    private ArrayList<CommissionReportDetailsModel>commissionReportDetailsModelArrayList;
    Context ctx;

    public CommissionDetailsAdapter(Context ctx, ArrayList<CommissionReportDetailsModel> commissionReportDetailsModelArrayList) {
        inflater = LayoutInflater.from(ctx);
        this.commissionReportDetailsModelArrayList = commissionReportDetailsModelArrayList;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public CommissionDetailsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.rv_commissiondetails, parent, false);
        CommissionDetailsAdapter.MyViewHolder holder = new CommissionDetailsAdapter.MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommissionDetailsAdapter.MyViewHolder holder, int position) {

        holder.tvDate.setText(commissionReportDetailsModelArrayList.get(position).getDate());
        holder.tvSale.setText("$"+commissionReportDetailsModelArrayList.get(position).getTotal_sale_datewise());

    }

    @Override
    public int getItemCount() {
        return commissionReportDetailsModelArrayList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvDate, tvSale;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvSale = itemView.findViewById(R.id.tvSale);


        }
    }

    }
