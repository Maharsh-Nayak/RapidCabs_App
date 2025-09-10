package com.example.rapidcabs;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

import java.util.List;

public interface ApiService {
    @GET("UserLoginAuth")
    Call<User> getUsers();

    @FormUrlEncoded
    @POST("UserLoginAuth")
    Call<ApiResponse> createUser(
            @Field("un") String un,
            @Field("em") String em,
            @Field("num") String num);
}

