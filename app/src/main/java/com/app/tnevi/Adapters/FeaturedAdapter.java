package com.app.tnevi.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.app.tnevi.R;


import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.app.tnevi.Eventdetails;
import com.app.tnevi.MainActivity;

import com.app.tnevi.model.GeteventModel;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.like.LikeButton;
import com.like.OnLikeListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

public class FeaturedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<GeteventModel> homeEventsModelArrayList;
    Context ctx;
    private final int limit = 11;
    private static final String TAG = "myapp";
    private static final int CONTENT_TYPE = 0;
    private static final int AD_TYPE = 1;


    public FeaturedAdapter(Context ctx, ArrayList<GeteventModel> homeEventsModelArrayList) {
        inflater = LayoutInflater.from(ctx);
        this.homeEventsModelArrayList = homeEventsModelArrayList;
        this.ctx = ctx;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == CONTENT_TYPE) {
            View view = inflater.inflate(R.layout.rv_featuredevent, viewGroup, false);
            return new MyViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.list_item_ad1, viewGroup, false);
            return new AdRecyclerHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        if (viewHolder.getItemViewType() == AD_TYPE){


        }else {
            FeaturedAdapter.MyViewHolder holder = (MyViewHolder) viewHolder;
            holder.tvEventname.setText(homeEventsModelArrayList.get(i).getEvent_name());
            holder.tvEpointsvalue.setText("$" + homeEventsModelArrayList.get(i).getEvent_commission() + "/Ticket");
            Glide.with(ctx)
                    .load("http://dev8.ivantechnology.in/tnevi/storage/app/" +
                            homeEventsModelArrayList.get(i).getEvent_image())
                    .placeholder(R.drawable.bg7)
                    .into(holder.imgBanner);
            holder.btnHeart.setLiked(!homeEventsModelArrayList.get(i).getFav_status().equals("0"));
            holder.btnHeart.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {

                    ((MainActivity) ctx).addRemovefav(homeEventsModelArrayList.get(i));

                }

                @Override
                public void unLiked(LikeButton likeButton) {

                    ((MainActivity) ctx).addRemovefav(homeEventsModelArrayList.get(i));

                }
            });


            holder.btnEventdetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(ctx, Eventdetails.class);
                    intent.putExtra("eventId", homeEventsModelArrayList.get(i).getId());
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    ctx.startActivity(intent);

                }
            });
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
//
//            return homeEventsModelArrayList.size();
//
//    }

    @Override
    public int getItemCount() {
        if (homeEventsModelArrayList.size() > limit) {
            return limit;
        } else {
        return homeEventsModelArrayList.size();
    }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        CardView btnEventdetails;
        ImageView imgBanner;
        LikeButton btnHeart;
        TextView tvEventname, tvEpointsvalue;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            btnEventdetails = itemView.findViewById(R.id.btnEventdetails);
            imgBanner = itemView.findViewById(R.id.imgBanner);
            btnHeart = itemView.findViewById(R.id.btnHeart);
            tvEventname = itemView.findViewById(R.id.tvEventname);
            tvEpointsvalue = itemView.findViewById(R.id.tvEpointsvalue);


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
