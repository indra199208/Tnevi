package com.example.tnevi.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tnevi.Eventdetails;
import com.example.tnevi.Events;
import com.example.tnevi.R;
import com.example.tnevi.model.GalleryModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<GalleryModel> galleryModelArrayList;
    Context ctx;

    public GalleryAdapter(Context ctx, ArrayList<GalleryModel> galleryModelArrayList) {
        inflater = LayoutInflater.from(ctx);
        this.galleryModelArrayList = galleryModelArrayList;
        this.ctx = ctx;
    }

    @Override
    public GalleryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.rv_gallery, parent, false);
        GalleryAdapter.MyViewHolder holder = new GalleryAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull GalleryAdapter.MyViewHolder holder, int position) {

        Glide.with(ctx)
                .load("http://dev8.ivantechnology.in/tnevi/storage/app/" +
                        galleryModelArrayList.get(position).getFile_name())
                .placeholder(R.drawable.bg7)
                .into(holder.Imggallery);



        holder.Imggallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((Eventdetails) ctx).popupImage(galleryModelArrayList.get(position), view, holder.getAdapterPosition());


            }
        });

    }

    @Override
    public int getItemCount() {

        return (galleryModelArrayList == null) ? 0 : galleryModelArrayList.size();

    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView Imggallery;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            Imggallery = itemView.findViewById(R.id.Imggallery);


        }
    }
}
