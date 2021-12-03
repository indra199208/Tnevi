package com.example.tnevi.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tnevi.Editevent2;
import com.example.tnevi.R;
import com.example.tnevi.model.GalleryModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class GalleryEditAdapter extends RecyclerView.Adapter<GalleryEditAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<GalleryModel> galleryModelArrayList;
    Context ctx;

    public GalleryEditAdapter(Context ctx, ArrayList<GalleryModel> galleryModelArrayList) {
        inflater = LayoutInflater.from(ctx);
        this.galleryModelArrayList = galleryModelArrayList;
        this.ctx = ctx;
    }

    @Override
    public GalleryEditAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.rv_editgallery, parent, false);
        GalleryEditAdapter.MyViewHolder holder = new GalleryEditAdapter.MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull GalleryEditAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Glide.with(ctx)
                .load("http://dev8.ivantechnology.in/tnevi/storage/app/" +
                        galleryModelArrayList.get(position).getFile_name())
                .placeholder(R.drawable.bg7)
                .into(holder.imgGallery);

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((Editevent2)ctx).removeGallery(galleryModelArrayList.get(position), holder.getAdapterPosition());

            }
        });

    }

    @Override
    public int getItemCount() {

        return (galleryModelArrayList == null) ? 0 : galleryModelArrayList.size();

    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imgGallery;
        LinearLayout btnDelete;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            imgGallery = itemView.findViewById(R.id.imgGallery);
            btnDelete = itemView.findViewById(R.id.btnDelete);

        }
    }
}
