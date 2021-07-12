package com.gian.gethome.Interface;

import com.gian.gethome.Clases.MainPojo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("place/queryautocomplete/json")
    Call<MainPojo> getPlace(@Query("input")String text,
                            @Query("key")String key);
}
