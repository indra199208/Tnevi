package com.app.tnevi.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.app.tnevi.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.tnevi.model.CategoryModel;
import com.bumptech.glide.Glide;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ChooseAdapter extends RecyclerView.Adapter<ChooseAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<CategoryModel> categoryModelArrayList;
    Context ctx;

    public ChooseAdapter(Context ctx, ArrayList<CategoryModel> categoryModelArrayList) {
        inflater = LayoutInflater.from(ctx);
        this.categoryModelArrayList = categoryModelArrayList;
        this.ctx = ctx;
    }

    @Override
    public ChooseAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.rv_allcategory, parent, false);
        ChooseAdapter.MyViewHolder holder = new ChooseAdapter.MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ChooseAdapter.MyViewHolder holder, int position) {

        final CategoryModel categoryModel = categoryModelArrayList.get(position);
        if (categoryModel.isSelected()){
            holder.checkAwards.setVisibility(View.VISIBLE);
        }else {
            holder.checkAwards.setVisibility(View.GONE);
        }


        holder.tvCatname.setText(categoryModelArrayList.get(position).getCategoryname());
        holder.btnCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (categoryModel.isSelected())
                    categoryModel.setSelected(false);
                else
                    categoryModel.setSelected(true);
                notifyItemChanged(holder.getAdapterPosition());


//                Intent intent = new Intent(ctx, Addeventdetails.class);
//                intent.putExtra("category", categoryModelArrayList.get(position).getCategoryname());
//                intent.putExtra("id", categoryModelArrayList.get(position).getId());
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                ctx.startActivity(intent);
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
        LinearLayout btnCategory, checkAwards;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvCatname = itemView.findViewById(R.id.tvCatname);
            imgIcon = itemView.findViewById(R.id.imgIcon);
            btnCategory = itemView.findViewById(R.id.btnCategory);
            checkAwards = itemView.findViewById(R.id.checkAwards);
        }
    }
}
