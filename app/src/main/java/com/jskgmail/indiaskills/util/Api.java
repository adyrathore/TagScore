package com.jskgmail.indiaskills.util;

import com.jskgmail.indiaskills.BuildConfig;
import com.jskgmail.indiaskills.pojo.UploadZipResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface Api {
    //the base URL for our API
    //make sure you are not using localhost
    //find the ip usinc ipconfig command
    public static String BASE_URL = BuildConfig.BASE_URL;
    public static String BASE_URL_ZIP = BuildConfig.BASE_URL;

    //this is our multipart request
    //we have two parameters on is name and other one is description
    @Multipart
    @POST("users/media_upload")
    Call<UploadZipResponse> uploadImage(@Header("Authorization") String token,
                                        @Part MultipartBody.Part image,
                                        @Part("userId") RequestBody userId,
                                        @Part("api_key") RequestBody api_key,
                                        @Part("testID") RequestBody testID,
                                        @Part("schedule_id") RequestBody schedule_id);

    @Multipart
    @POST("users/user_upload_video_photo")
    Call<UploadZipResponse> uploadImageVideo(@Header("Authorization") String token,
                                             @Part MultipartBody.Part image,
                                             @Part("userId") RequestBody userId,
                                             @Part("api_key") RequestBody api_key,
                                             @Part("testID") RequestBody testID,
                                             @Part("uniqueID") RequestBody uniqueID,
                                             @Part("picType") RequestBody picType,
                                             @Part("candidate_id") RequestBody candidate_id,
                                             @Part("version") RequestBody version,
                                             @Part("target_dir") RequestBody target_dir,
                                             @Part("filename") RequestBody filename);

    @Streaming
    @GET
    Call<ResponseBody> downloadFileByUrl(@Url String urlString);


    @Multipart
    @POST("users/user_upload_video_photo")
    Call<UploadZipResponse> uploadImageVideo(@Header("Authorization") String token,
                                             @Part MultipartBody.Part image,
                                             @Part("userId") RequestBody userId,
                                             @Part("api_key") RequestBody api_key,
                                             @Part("testID") RequestBody testID,
                                             @Part("uniqueID") RequestBody uniqueID,
                                             @Part("picType") RequestBody picType,
                                             @Part("candidate_id") RequestBody candidate_id,
                                             @Part("version") RequestBody version,
                                             @Part("target_dir") RequestBody target_dir);
}
