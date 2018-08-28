package com.zrz.android.olearisweather.Retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("weather")
    Call<Weather> getData(@Query("lat") String lat, @Query("lon") String lon, @Query("appid") String appid);
}
