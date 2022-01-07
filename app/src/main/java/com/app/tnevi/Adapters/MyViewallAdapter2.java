package com.app.tnevi.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.tnevi.Eventdetails;
import com.app.tnevi.R;
import com.app.tnevi.Search2;
import com.app.tnevi.model.GeteventModel;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class MyViewallAdapter2 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<GeteventModel> homeEventsModelArrayList;
    public Context ctx;
    private static final String TAG = "myapp";
    private static final int CONTENT_TYPE = 0;
    private static final int AD_TYPE = 1;
    private final int limit = 11;

    public MyViewallAdapter2(Context ctx, ArrayList<GeteventModel> homeEventsModelArrayList) {
        inflater = LayoutInflater.from(ctx);
        this.homeEventsModelArrayList = homeEventsModelArrayList;
        this.ctx = ctx;

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == CONTENT_TYPE) {
            View view = inflater.inflate(R.layout.rv_featuredevent1, viewGroup, false);
            return new MyViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.list_item_ad, viewGroup, false);
            return new AdRecyclerHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder.getItemViewType() == AD_TYPE) {

        } else {
            MyViewHolder holder = (MyViewHolder) viewHolder;
            holder.tvEventname.setText(homeEventsModelArrayList.get(viewHolder.getAdapterPosition()).getEvent_name());
            holder.tvEpointsvalue.setText("$" + homeEventsModelArrayList.get(viewHolder.getAdapterPosition()).getEvent_commission() + "/Ticket");
            String eventdate = homeEventsModelArrayList.get(viewHolder.getAdapterPosition()).getEvent_date();
            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String currentDate = df.format(c);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date1 = sdf.parse(currentDate);
                Date date2 = sdf.parse(eventdate);
                long diff = date2.getTime() - date1.getTime();
                Log.d(TAG, "diffdate-->" + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
                String dateoutput = String.valueOf(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
                char d = dateoutput.charAt(0);
                String s = String.valueOf(d);
//            if (s.equals("-")){
//                holder.tvReamining.setText("Already Completed");
//                holder.tvReamining.setTextColor(Color.RED);
//
//            }else {
//                holder.tvReamining.setText(Math.toIntExact(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS))+" Days");
//            }


            } catch (ParseException e) {
                e.printStackTrace();
            }


            Glide.with(ctx)
                    .load("http://dev8.ivantechnology.in/tnevi/storage/app/" + homeEventsModelArrayList.get(viewHolder.getAdapterPosition()).getEvent_image())
                    .placeholder(R.drawable.bg7)
                    .into(holder.imgBanner);

            if (homeEventsModelArrayList.get(position).getFav_status().equals("0")) {
                holder.btnHeart.setLiked(false);
            } else {
                holder.btnHeart.setLiked(true);
            }


            holder.btnHeart.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                    ((Search2) ctx).addRemovefav(homeEventsModelArrayList.get(viewHolder.getAdapterPosition()));
                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    ((Search2) ctx).addRemovefav(homeEventsModelArrayList.get(viewHolder.getAdapterPosition()));
                }
            });

            holder.btnEventdetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(ctx, Eventdetails.class);
                    intent.putExtra("eventId", homeEventsModelArrayList.get(viewHolder.getAdapterPosition()).getId());
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    ctx.startActivity(intent);

                }
            });

//        holder.btnViewticket.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent intent = new Intent(ctx, Ticketdetails.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                ctx.startActivity(intent);
//
//            }
//        });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (homeEventsModelArrayList.get(position) == null)
            return AD_TYPE;
        return CONTENT_TYPE;
    }

//    @Override
//    public int getItemCount() {
//        return geteventModelArrayList.size();
//    }

    @Override
    public int getItemCount() {
        if (homeEventsModelArrayList.size() > limit) {
            return limit;
        } else {
            return homeEventsModelArrayList.size();
        }
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        public CardView btnEventdetails;
        public ImageView imgBanner;
        public LikeButton btnHeart;
        public TextView tvEventname, tvEpointsvalue;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            btnEventdetails = itemView.findViewById(R.id.btnEventdetails);
            tvEpointsvalue = itemView.findViewById(R.id.tvEpointsvalue);
            tvEventname = itemView.findViewById(R.id.tvEventname);
            imgBanner = itemView.findViewById(R.id.imgBanner);
            btnHeart = itemView.findViewById(R.id.btnHeart);


        }
    }

     class AdRecyclerHolder extends RecyclerView.ViewHolder {
        NativeAd nativeAd;

        public AdRecyclerHolder(@NonNull View itemView) {
            super(itemView);
            refreshAd(itemView);
        }

        public void refreshAd(@NonNull View itemView) {
            AdLoader.Builder builder = new AdLoader.Builder(ctx, ctx.getString(R.string.ADMOB_ADS_UNIT_ID));
            builder.forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                @Override
                public void onNativeAdLoaded(NativeAd unifiedNativeAd) {

                    if (nativeAd != null) {
                        nativeAd.destroy();
                    }
                    nativeAd = unifiedNativeAd;
                    FrameLayout frameLayout = itemView.findViewById(R.id.fl_adplaceholder);
                    NativeAdView adView = (NativeAdView) LayoutInflater.from(ctx).inflate(R.layout.ad_helper, null);

                    populateUnifiedNativeAdView(unifiedNativeAd, adView);
                    frameLayout.removeAllViews();
                    frameLayout.addView(adView);
                }
            }).build();
            NativeAdOptions adOptions = new NativeAdOptions.Builder().build();
            builder.withNativeAdOptions(adOptions);
            AdLoader adLoader = builder.withAdListener(new AdListener() {
                public void onAdFailedToLoad(int i) {

                }
            }).build();
            adLoader.loadAd(new AdRequest.Builder().build());

        }

         private void populateUnifiedNativeAdView(NativeAd nativeAd, NativeAdView adView) {
             adView.setMediaView((MediaView) adView.findViewById(R.id.ad_media));
             adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
             adView.setBodyView(adView.findViewById(R.id.ad_body));
             adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
             adView.setIconView(adView.findViewById(R.id.ad_app_icon));
             adView.setPriceView(adView.findViewById(R.id.ad_price));
             adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
             adView.setStoreView(adView.findViewById(R.id.ad_store));
             adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));


             ((TextView) Objects.requireNonNull(adView.getHeadlineView())).setText(nativeAd.getHeadline());
             Objects.requireNonNull(adView.getMediaView()).setMediaContent(Objects.requireNonNull(nativeAd.getMediaContent()));


             if (nativeAd.getBody() == null) {
                 Objects.requireNonNull(adView.getBodyView()).setVisibility(View.INVISIBLE);

             } else {
                 adView.getBodyView().setVisibility(View.VISIBLE);
                 ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
             }
             if (nativeAd.getCallToAction() == null) {
                 Objects.requireNonNull(adView.getCallToActionView()).setVisibility(View.INVISIBLE);
             } else {
                 Objects.requireNonNull(adView.getCallToActionView()).setVisibility(View.VISIBLE);
                 ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
             }
             if (nativeAd.getIcon() == null) {
                 Objects.requireNonNull(adView.getIconView()).setVisibility(View.GONE);
             } else {
                 ((ImageView) Objects.requireNonNull(adView.getIconView())).setImageDrawable(nativeAd.getIcon().getDrawable());
                 adView.getIconView().setVisibility(View.VISIBLE);
             }

             if (nativeAd.getPrice() == null) {
                 Objects.requireNonNull(adView.getPriceView()).setVisibility(View.INVISIBLE);

             } else {
                 Objects.requireNonNull(adView.getPriceView()).setVisibility(View.VISIBLE);
                 ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
             }
             if (nativeAd.getStore() == null) {
                 Objects.requireNonNull(adView.getStoreView()).setVisibility(View.INVISIBLE);
             } else {
                 Objects.requireNonNull(adView.getStoreView()).setVisibility(View.VISIBLE);
                 ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
             }
             if (nativeAd.getStarRating() == null) {
                 Objects.requireNonNull(adView.getStarRatingView()).setVisibility(View.INVISIBLE);
             } else {
                 ((RatingBar) Objects.requireNonNull(adView.getStarRatingView())).setRating(nativeAd.getStarRating().floatValue());
                 adView.getStarRatingView().setVisibility(View.VISIBLE);
             }

             if (nativeAd.getAdvertiser() == null) {
                 Objects.requireNonNull(adView.getAdvertiserView()).setVisibility(View.INVISIBLE);
             } else
                 ((TextView) Objects.requireNonNull(adView.getAdvertiserView())).setText(nativeAd.getAdvertiser());
             adView.getAdvertiserView().setVisibility(View.VISIBLE);


             adView.setNativeAd(nativeAd);


         }


    }



}
