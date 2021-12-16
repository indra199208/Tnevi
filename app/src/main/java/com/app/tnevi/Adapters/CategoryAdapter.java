package com.app.tnevi.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.tnevi.model.CategoryModel;
import com.bumptech.glide.Glide;
import com.app.tnevi.MainActivity;

import com.app.tnevi.Viewallcategory;
import com.app.tnevi.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<CategoryModel> categoryModelArrayList;
    Context ctx;

    public CategoryAdapter(Context ctx, ArrayList<CategoryModel> categoryModelArrayList) {
        inflater = LayoutInflater.from(ctx);
        this.categoryModelArrayList = categoryModelArrayList;
        this.ctx = ctx;
    }

    @Override
    public CategoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.rv_allcategory, parent, false);
        CategoryAdapter.MyViewHolder holder = new CategoryAdapter.MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CategoryAdapter.MyViewHolder holder, int position) {

        holder.tvCatname.setText(categoryModelArrayList.get(position).getCategoryname());
        holder.btnCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ctx instanceof MainActivity){
                    ((MainActivity) ctx).getcurrentlatlong(categoryModelArrayList.get(position).getId(), categoryModelArrayList.get(position).getCategoryname());

                }else if (ctx instanceof Viewallcategory){
                    ((Viewallcategory) ctx).getcurrentlatlong(categoryModelArrayList.get(position).getId());
                }

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
