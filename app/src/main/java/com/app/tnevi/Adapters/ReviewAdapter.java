package com.app.tnevi.Adapters;

import static com.willy.ratingbar.BaseRatingBar.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.app.tnevi.R;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.app.tnevi.model.ReviewModel;
import com.bumptech.glide.Glide;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder>{

    private LayoutInflater inflater;
    private ArrayList<ReviewModel> reviewModelArrayList;
    Context ctx;

    public ReviewAdapter(Context ctx, ArrayList<ReviewModel> reviewModelArrayList) {
        inflater = LayoutInflater.from(ctx);
        this.reviewModelArrayList = reviewModelArrayList;
        this.ctx = ctx;
    }


    @NonNull
    @Override
    public ReviewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.rv_review, parent, false);
        ReviewAdapter.MyViewHolder holder = new ReviewAdapter.MyViewHolder(view);

        return holder;
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.MyViewHolder holder, int position) {

        holder.tvUserName.setText(reviewModelArrayList.get(position).getUser_name());
        holder.tvReview.setText(reviewModelArrayList.get(position).getDescription());
        String rating = reviewModelArrayList.get(position).getRating();
        switch (rating) {
            case "1":
                holder.ratings.setRating(1);
                break;
            case "1.5":
                holder.ratings.setRating(1.5F);
                break;
            case "2":
                holder.ratings.setRating(2);
                break;
            case "2.5":
                holder.ratings.setRating(2.5F);
                break;
            case "3":
                holder.ratings.setRating(3);
                break;
            case "3.5":
                holder.ratings.setRating(3.5F);
                break;
            case "4":
                holder.ratings.setRating(4);
                break;
            case "4.5":
                holder.ratings.setRating(4.5F);
                break;
            case "5":
                holder.ratings.setRating(5);
                break;
        }
        holder.tvRating.setText(reviewModelArrayList.get(position).getRating());
        String reviewdate = reviewModelArrayList.get(position).getDate();
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String currentDate = df.format(c);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date date1 = sdf.parse(currentDate);
            Date date2 = sdf.parse(reviewdate);
            long diff = date2.getTime() - date1.getTime();
            Log.d(TAG, "diffdate-->"+ TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) );
            String dateoutput = String.valueOf(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
            char d = dateoutput.charAt(0);
            String s = String.valueOf(d);
            if (s.equals("-")){
                String datediff = String.valueOf(Math.toIntExact(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)));
                String datefinal = datediff.replaceAll("-","");
                holder.tvDate.setText(datefinal+" Days Ago");

            }else {
                holder.tvDate.setText(Math.toIntExact(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS))+" Days");
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }


//        holder.tvDate.setText(reviewModelArrayList.get(position).getDate());
        Glide.with(ctx)
                .load("http://dev8.ivantechnology.in/tnevi/storage/app/" + reviewModelArrayList.get(position).getPro_pic())
                .placeholder(R.drawable.dp)
                .circleCrop()
                .into(holder.imgPrf);

    }

    @Override
    public int getItemCount() {
        return reviewModelArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imgPrf;
        TextView tvUserName, tvRating, tvDate, tvReview;
        RatingBar ratings;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPrf = itemView.findViewById(R.id.imgPrf);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvReview = itemView.findViewById(R.id.tvReview);
            ratings = itemView.findViewById(R.id.ratings);

        }
    }
}
