package com.app.tnevi.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import com.app.tnevi.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.tnevi.Featuread;

import com.app.tnevi.model.PlanModel;

import java.util.ArrayList;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<PlanModel> planModelArrayList;
    Context ctx;
    private int lastSelectedPosition = -1;


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
        holder.tvPlandetails.setText(planModelArrayList.get(position).getTagline());
        holder.tvDays.setText("For "+planModelArrayList.get(position).getPlan_length()+" Days Validation");
        if (lastSelectedPosition == position) {
            holder.radioButton.setChecked(true);
        } else {
            holder.radioButton.setChecked(false);
        }
        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastSelectedPosition = holder.getAdapterPosition();
                if (ctx instanceof Featuread) {
                    ((Featuread) ctx).updatePriceText(planModelArrayList.get(lastSelectedPosition).getPlan_price(),
                            planModelArrayList.get(lastSelectedPosition).getId(), planModelArrayList.get(lastSelectedPosition).getType());
                }
            }
        });
        holder.radioButton.setTag(position);


    }

    @Override
    public int getItemCount() {
        return planModelArrayList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvPlantitle, tvPlandetails, tvPlanprice, tvDays;
        RadioButton radioButton;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPlantitle = itemView.findViewById(R.id.tvPlantitle);
            tvPlandetails = itemView.findViewById(R.id.tvPlandetails);
            tvPlanprice = itemView.findViewById(R.id.tvPlanprice);
            radioButton = itemView.findViewById(R.id.radioButton);
            tvDays = itemView.findViewById(R.id.tvDays);


        }
    }
}
