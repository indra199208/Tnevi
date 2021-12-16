package com.app.tnevi.retrofit;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {
    @Multipart
    @POST("v1/update-account")
    Call<ResponseBody> uploadImage(@Header("Authorization") String Authorization,
                                   @Part MultipartBody.Part file);

    @Multipart
    @POST("v1/event-galley-image-upload")
    Call<ResponseBody> uploadGallery(@Header("Authorization") String Authorization,
                                     @Part("event_id") RequestBody event_id,
                                     @Part MultipartBody.Part file);


    @Multipart
    @POST("v1/create-event")
    Call<ResponseBody> createEvent(@Header("Authorization") String Authorization,
                                   @Part("event_name") RequestBody event_name,
                                   @Part("event_commission") RequestBody event_commission,
                                   @Part("event_description") RequestBody event_description,
                                   @Part("helpline_no") RequestBody helpline_no,
                                   @Part("event_date") RequestBody event_date,
                                   @Part("event_edate") RequestBody event_edate,
                                   @Part("event_stime") RequestBody event_stime,
                                   @Part("event_etime") RequestBody event_etime,
                                   @Part("currency_id") RequestBody currency_id,
                                   @Part("free_stat") RequestBody free_stat,
                                   @Part("plan_id") RequestBody plan_id,
                                   @Part("ad_start") RequestBody ad_start,
                                   @Part("ad_end") RequestBody ad_end,
                                   @Part("tran_id") RequestBody tran_id,
                                   @Part("inv_id") RequestBody inv_id,
                                   @Part("payer_email") RequestBody payer_email,
                                   @Part("payment_amount") RequestBody payment_amount,
                                   @Part("posted_by") RequestBody posted_by,
                                   @Part MultipartBody.Part eventfile,
                                   @Part MultipartBody.Part venuefile,
                                   @Part("categories") RequestBody categories,
                                   @Part("social_links") RequestBody social_links,
                                   @Part("youtube_links") RequestBody video_links,
                                   @Part("venu_name") RequestBody venu_name,
                                   @Part("event_address") RequestBody event_address,
                                   @Part("event_address2") RequestBody event_address2,
                                   @Part("event_lat_val") RequestBody event_lat_val,
                                   @Part("event_long_val") RequestBody event_long_val,
                                   @Part("blocks") RequestBody blocks);


    @Multipart
    @POST("v1/update-event")
    Call<ResponseBody> updateEvent(@Header("Authorization") String Authorization,
                                   @Part("event_id") RequestBody event_id,
                                   @Part("event_name") RequestBody event_name,
                                   @Part("event_commission") RequestBody event_commission,
                                   @Part("event_description") RequestBody event_description,
                                   @Part("helpline_no") RequestBody helpline_no,
                                   @Part("event_date") RequestBody event_date,
                                   @Part("event_edate") RequestBody event_edate,
                                   @Part("event_stime") RequestBody event_stime,
                                   @Part("event_etime") RequestBody event_etime,
                                   @Part("currency_id") RequestBody currency_id,
                                   @Part("payment_amount") RequestBody payment_amount,
                                   @Part("posted_by") RequestBody posted_by,
                                   @Part MultipartBody.Part eventfile,
                                   @Part("categories") RequestBody categories,
                                   @Part("social_links") RequestBody social_links,
                                   @Part("youtube_links") RequestBody video_links,
//                                   @Part MultipartBody.Part galleryfile,
                                   @Part("venu_name") RequestBody venu_name,
                                   @Part("event_address") RequestBody event_address,
                                   @Part("event_address2") RequestBody event_address2,
                                   @Part("event_lat_val") RequestBody event_lat_val,
                                   @Part("event_long_val") RequestBody event_long_val,
                                   @Part("blocks") RequestBody blocks);


    @Multipart
    @POST("v1/update-event")
    Call<ResponseBody> updateEvent2(@Header("Authorization") String Authorization,
                                    @Part("event_id") RequestBody event_id,
                                    @Part("event_name") RequestBody event_name,
                                    @Part("event_commission") RequestBody event_commission,
                                    @Part("event_description") RequestBody event_description,
                                    @Part("helpline_no") RequestBody helpline_no,
                                    @Part("event_date") RequestBody event_date,
                                    @Part("event_edate") RequestBody event_edate,
                                    @Part("event_stime") RequestBody event_stime,
                                    @Part("event_etime") RequestBody event_etime,
                                    @Part("currency_id") RequestBody currency_id,
                                    @Part("payment_amount") RequestBody payment_amount,
                                    @Part("posted_by") RequestBody posted_by,
//                                   @Part MultipartBody.Part eventfile,
                                    @Part("categories") RequestBody categories,
                                    @Part("social_links") RequestBody social_links,
                                    @Part("youtube_links") RequestBody video_links,
//                                   @Part MultipartBody.Part galleryfile,
                                    @Part("venu_name") RequestBody venu_name,
                                    @Part("event_address") RequestBody event_address,
                                    @Part("event_address2") RequestBody event_address2,
                                    @Part("event_lat_val") RequestBody event_lat_val,
                                    @Part("event_long_val") RequestBody event_long_val,
                                    @Part("blocks") RequestBody blocks);

    @HTTP(method = "DELETE", path = "v1/delete-event", hasBody = true)
    Call<ResponseBody> deleteObject(@Header("Authorization") String Authorization, @Body Map<String, String> object);

}

