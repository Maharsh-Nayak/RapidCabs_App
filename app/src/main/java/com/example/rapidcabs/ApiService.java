package com.example.rapidcabs;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

import java.util.List;

public interface ApiService {
    @GET("UserAuth")
    Call<List<User>> getUsers();

    @POST("UserAuth")
    Call<User> createUser(@Body User user);
}

