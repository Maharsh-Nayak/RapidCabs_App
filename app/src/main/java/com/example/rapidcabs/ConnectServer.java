package com.example.rapidcabs;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import java.util.List;

public interface ApiService {
    @GET("/UserAuth")  // endpoint of servlet
    Call<List<User>> getUsers();
}
public class ConnectServer {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("localhost:")  // your servlet base URL
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    ApiService api = retrofit.create(ApiService.class);

api.getUsers().enqueue(new Callback<List<User>>() {
        @Override
        public void onResponse(Call<List<User>> call, Response<List<User>> response) {
            if (response.isSuccessful()) {
                List<User> users = response.body();
                for (User u : users) {
                    Log.d("API", u.getId() + " - " + u.getName());
                }
            }
        }

        @Override
        public void onFailure(Call<List<User>> call, Throwable t) {
            Log.e("API_ERROR", t.getMessage());
        }
    });

}
