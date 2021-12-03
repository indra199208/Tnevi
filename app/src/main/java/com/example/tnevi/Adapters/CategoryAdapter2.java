package com.example.tnevi.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tnevi.Addeventdetails;
import com.example.tnevi.Events;
import com.example.tnevi.R;
import com.example.tnevi.model.CategoryModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CategoryAdapter2 extends RecyclerView.Adapter<CategoryAdapter2.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<CategoryModel> categoryModelArrayList;
    Context ctx;

    public CategoryAdapter2(Context ctx, ArrayList<CategoryModel> categoryModelArrayList) {
        inflater = LayoutInflater.from(ctx);
        this.categoryModelArrayList = categoryModelArrayList;
        this.ctx = ctx;
    }

    @Override
    public CategoryAdapter2.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.rv_allcategory, parent, false);
        CategoryAdapter2.MyViewHolder holder = new CategoryAdapter2.MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CategoryAdapter2.MyViewHolder holder, int position) {

        holder.tvCatname.setText(categoryModelArrayList.get(position).getCategoryname());
        holder.btnCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ctx, Addeventdetails.class);
                intent.putExtra("category", categoryModelArrayList.get(position).getCategoryname());
                intent.putExtra("id", categoryModelArrayList.get(position).getId());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                ctx.startActivity(intent);
            }
        });
        Glide.with(ctx)
                .load("http://dev8.ivantechnology.in/tnevi/storage/app/" +
                        categoryModelArrayList.get(position).getPic())
                .placeholder(R.drawable.ic_awards)
                .into(holder.imgIcon);

    }

    @Override
    public int getItemCount() {
        return categoryModelArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvCatname;
        ImageView imgIcon;
        LinearLayout btnCategory;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvCatname = itemView.findViewById(R.id.tvCatname);
            imgIcon = itemView.findViewById(R.id.imgIcon);
            btnCategory = itemView.findViewById(R.id.btnCategory);
        }
    }
}
