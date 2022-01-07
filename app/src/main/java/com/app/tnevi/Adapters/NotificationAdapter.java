package com.app.tnevi.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.app.tnevi.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.app.tnevi.model.NotificationModel;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<NotificationModel> notificationModelArrayList;
    Context ctx;

    public NotificationAdapter(Context ctx, ArrayList<NotificationModel> notificationModelArrayList) {
        inflater = LayoutInflater.from(ctx);
        this.notificationModelArrayList = notificationModelArrayList;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public NotificationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.rv_notifications, parent, false);
        NotificationAdapter.MyViewHolder holder = new NotificationAdapter.MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.MyViewHolder holder, int position) {

        holder.tvDescription.setText(notificationModelArrayList.get(position).getMsg());
        holder.tvDate.setText(notificationModelArrayList.get(position).getNotify_date());
        holder.tvHeading.setText(notificationModelArrayList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return notificationModelArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvDescription, tvDate, tvHeading;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvHeading = itemView.findViewById(R.id.tvHeading);


        }
    }
}
