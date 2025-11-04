package com.maharsh.rapidcabs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroFitClient {
    private static final String BASE_URL = "http://43.205.199.165:8080/Backend-1.0-SNAPSHOT/";
//    private static final String BASE_URL = "http://10.0.0.1/Backend-1.0-SNAPSHOT/";
    private static Retrofit retrofit = null;

    private static Gson gson = new GsonBuilder()
            .setDateFormat("MMM dd, yyyy, hh:mm:ss a")  // matches Feb 24, 2025, 12:00:00 AM
            .create();


    public static ApiService getApiService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit.create(ApiService.class);
    }
}

