package com.example.rapidcabs;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

import java.util.List;

public interface ApiService {

    @GET("UserLoginAuth")
    Call<Integer> getUsers(@Query("num") String n);

    @FormUrlEncoded
    @POST("UserLoginAuth")
    Call<ApiResponse> createUser(
            @Field("un") String un,
            @Field("em") String em,
            @Field("num") String num);
}

