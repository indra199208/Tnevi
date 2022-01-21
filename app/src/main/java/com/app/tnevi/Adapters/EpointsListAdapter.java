package com.app.tnevi.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.app.tnevi.R;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.app.tnevi.Buyepoint;
import com.app.tnevi.model.EpointListModel;


import java.util.ArrayList;

public class EpointsListAdapter extends RecyclerView.Adapter<EpointsListAdapter.MyViewHolder>{

    private LayoutInflater inflater;
    private ArrayList<EpointListModel> epointListModelArrayList;
    Context ctx;
    private int lastSelectedPosition = -1;


    public EpointsListAdapter(Context ctx, ArrayList<EpointListModel> epointListModelArrayList) {
        inflater = LayoutInflater.from(ctx);
        this.epointListModelArrayList = epointListModelArrayList;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public EpointsListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.rv_epoints, parent, false);
        EpointsListAdapter.MyViewHolder holder = new EpointsListAdapter.MyViewHolder(view);

        return holder;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull EpointsListAdapter.MyViewHolder holder, int position) {



        holder.tvEpoints.setText(epointListModelArrayList.get(position).getEpoint()+" ePoints");
        holder.tvEpointsvalue.setText("$"+epointListModelArrayList.get(position).getAmount());
        if (lastSelectedPosition == position) {
//            holder.radioButton.setChecked(true);
            holder.ll_bg.setBackgroundColor(Color.parseColor("#5F01D0"));
            holder.tvEpoints.setTextColor(ContextCompat.getColor(ctx, R.color.quantum_white_100));
            holder.tvEpointsvalue.setTextColor(ContextCompat.getColor(ctx, R.color.quantum_white_100));


        } else {
//            holder.radioButton.setChecked(false);
            holder.ll_bg.setBackgroundColor(Color.parseColor("#83F1F1F1"));
            holder.tvEpoints.setTextColor(ContextCompat.getColor(ctx, R.color.quantum_black_100));
            holder.tvEpointsvalue.setTextColor(ContextCompat.getColor(ctx, R.color.quantum_black_100));

        }

        holder.cv_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastSelectedPosition = holder.getAdapterPosition();
                if (ctx instanceof Buyepoint) {
                    ((Buyepoint) ctx).updatePriceText(epointListModelArrayList.get(lastSelectedPosition).getId(),
                            epointListModelArrayList.get(position).getEpoint(), epointListModelArrayList.get(position).getAmount());
                }

            }
        });
        holder.cv_item.setTag(position);


    }

    @Override
    public int getItemCount() {
        return epointListModelArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvEpoints, tvEpointsvalue;
//        RadioButton radioButton;
        CardView cv_item;
        LinearLayout ll_bg;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEpoints = itemView.findViewById(R.id.tvEpoints);
            tvEpointsvalue = itemView.findViewById(R.id.tvEpointsvalue);
//            radioButton = itemView.findViewById(R.id.radioButton);
            cv_item = itemView.findViewById(R.id.cv_item);
            ll_bg = itemView.findViewById(R.id.ll_bg);

        }
    }
}
