package com.ivan.tnevi.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tnevi.R;
import com.ivan.tnevi.model.FaqModel;

import java.util.ArrayList;

public class FaqAdapter extends RecyclerView.Adapter<FaqAdapter.MyViewHolder>{

    private LayoutInflater inflater;
    private ArrayList<FaqModel>faqModelArrayList;
    Context ctx;

    public FaqAdapter(Context ctx, ArrayList<FaqModel> faqModelArrayList) {
        inflater = LayoutInflater.from(ctx);
        this.faqModelArrayList = faqModelArrayList;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public FaqAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.rv_faq, parent, false);
        FaqAdapter.MyViewHolder holder = new FaqAdapter.MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FaqAdapter.MyViewHolder holder, int position) {

        holder.tvQuestion.setText("Question : "+faqModelArrayList.get(position).getQuestion());
        holder.tvAnswer.setText("Answer : "+faqModelArrayList.get(position).getAnswer());

    }

    @Override
    public int getItemCount() {
        return faqModelArrayList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvQuestion, tvAnswer;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuestion = itemView.findViewById(R.id.tvQuestion);
            tvAnswer = itemView.findViewById(R.id.tvAnswer);


        }
    }

    }
