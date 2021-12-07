package com.ivan.tnevi.Adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tnevi.R;
import com.ivan.tnevi.model.Epointsmodel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EpointsAdapter extends RecyclerView.Adapter<EpointsAdapter.MyViewHolder>{

    private LayoutInflater inflater;
    private ArrayList<Epointsmodel> epointsmodelArrayList;
    Context ctx;

    public EpointsAdapter(Context ctx, ArrayList<Epointsmodel> epointsmodelArrayList) {
        inflater = LayoutInflater.from(ctx);
        this.epointsmodelArrayList = epointsmodelArrayList;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public EpointsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.rv_epointshistory, parent, false);
        EpointsAdapter.MyViewHolder holder = new EpointsAdapter.MyViewHolder(view);

        return holder;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull EpointsAdapter.MyViewHolder holder, int position) {

//        SimpleDateFormat sdf = new SimpleDateFormat(epointsmodelArrayList.get(position).getCreated_at(), Locale.US);
//        String formattedDate = sdf.format(new Date());

        SimpleDateFormat sdf = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSX");
        }
        try {

            String pattern = "MM/dd/yyyy";
            DateFormat df = new SimpleDateFormat(pattern);
            Date d = sdf.parse(epointsmodelArrayList.get(position).getCreated_at());
            String todayAsString = df.format(d);
            holder.tvDate.setText(todayAsString);

        } catch (ParseException e) {
            e.printStackTrace();
        }


        holder.tvEpoints.setText(epointsmodelArrayList.get(position).getEpoints()+" ePoints");
        holder.tvDescription.setText(epointsmodelArrayList.get(position).getReward_description());

    }

    @Override
    public int getItemCount() {
        return epointsmodelArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvEpoints, tvDescription, tvDate;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEpoints = itemView.findViewById(R.id.tvEpoints);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvDate = itemView.findViewById(R.id.tvDate);
        }
    }
}
