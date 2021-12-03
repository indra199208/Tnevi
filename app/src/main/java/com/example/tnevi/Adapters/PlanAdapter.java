package com.example.tnevi.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tnevi.Events;
import com.example.tnevi.R;
import com.example.tnevi.model.PlanModel;

import java.util.ArrayList;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<PlanModel> planModelArrayList;
    Context ctx;
    private int lastSelectedPosition = -1;
    private CompoundButton lastCheckedRB = null;


    public PlanAdapter(Context ctx, ArrayList<PlanModel> planModelArrayList) {

        inflater = LayoutInflater.from(ctx);
        this.planModelArrayList = planModelArrayList;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public PlanAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.rv_getplans, parent, false);
        PlanAdapter.MyViewHolder holder = new PlanAdapter.MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PlanAdapter.MyViewHolder holder, int position) {

        holder.tvPlantitle.setText(planModelArrayList.get(position).getPlan_name());
        holder.tvPlanprice.setText("$" + planModelArrayList.get(position).getPlan_price());
        holder.tvPlandetails.setText(planModelArrayList.get(position).getDescription());
        holder.radioButton.setOnCheckedChangeListener(ls);
        holder.radioButton.setTag(position);


    }

    @Override
    public int getItemCount() {
        return planModelArrayList.size();
    }


    private CompoundButton.OnCheckedChangeListener ls = (new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            int tag = (int) buttonView.getTag();
            if (lastCheckedRB == null) {
                lastCheckedRB = buttonView;
            } else if (tag != (int) lastCheckedRB.getTag()) {
                lastCheckedRB.setChecked(false);
                lastCheckedRB = buttonView;
            }

        }
    });


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvPlantitle, tvPlandetails, tvPlanprice;
        RadioButton radioButton;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPlantitle = itemView.findViewById(R.id.tvPlantitle);
            tvPlandetails = itemView.findViewById(R.id.tvPlandetails);
            tvPlanprice = itemView.findViewById(R.id.tvPlanprice);
            radioButton = itemView.findViewById(R.id.radioButton);


        }
    }
}
