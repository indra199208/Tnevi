package com.app.tnevi.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.tnevi.Epointcheckout;
import com.app.tnevi.MainActivity;
import com.app.tnevi.Plancheckout;
import com.app.tnevi.R;
import com.app.tnevi.Viewallcategory;
import com.app.tnevi.activity_checkout;
import com.app.tnevi.model.FaqModel;
import com.app.tnevi.model.SaveCardsModel;

import java.util.ArrayList;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.MyViewHolder>{

    private LayoutInflater inflater;
    private ArrayList<SaveCardsModel>saveCardsModelArrayList;
    Context ctx;

    public CardAdapter(Context ctx, ArrayList<SaveCardsModel> saveCardsModelArrayList) {
        inflater = LayoutInflater.from(ctx);
        this.saveCardsModelArrayList = saveCardsModelArrayList;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public CardAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.rv_card, parent, false);
        CardAdapter.MyViewHolder holder = new CardAdapter.MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CardAdapter.MyViewHolder holder, int position) {

        holder.tvCardbrand.setText(saveCardsModelArrayList.get(position).getBrand());
        holder.tvExpdate.setText(saveCardsModelArrayList.get(position).getExp_month()+" / "+saveCardsModelArrayList.get(position).getExp_year());
        holder.tvlast4.setText(saveCardsModelArrayList.get(position).getLast4());

        holder.rl_selectedcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ctx instanceof activity_checkout){
                    ((activity_checkout) ctx).savecardpayment(saveCardsModelArrayList.get(position));
                }else if (ctx instanceof Epointcheckout){
                    ((Epointcheckout) ctx).savecardpayment(saveCardsModelArrayList.get(position));
                }else if (ctx instanceof Plancheckout){
                    ((Plancheckout) ctx).savecardpayment(saveCardsModelArrayList.get(position));
                }


            }
        });

//        if (ctx instanceof MainActivity){
//            ((MainActivity) ctx).getcurrentlatlong(categoryModelArrayList.get(position).getId(), categoryModelArrayList.get(position).getCategoryname());
//
//        }else if (ctx instanceof Viewallcategory){
//            ((Viewallcategory) ctx).getcurrentlatlong(categoryModelArrayList.get(position).getId());
//        }

    }

    @Override
    public int getItemCount() {
        return saveCardsModelArrayList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvCardbrand, tvlast4, tvExpdate;
        RelativeLayout rl_selectedcard;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCardbrand = itemView.findViewById(R.id.tvCardbrand);
            tvlast4 = itemView.findViewById(R.id.tvlast4);
            tvExpdate = itemView.findViewById(R.id.tvExpdate);
            rl_selectedcard = itemView.findViewById(R.id.rl_selectedcard);


        }
    }

    }
