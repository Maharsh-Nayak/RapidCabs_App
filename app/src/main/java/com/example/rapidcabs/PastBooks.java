package com.example.rapidcabs;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PastBooks extends Fragment {

    private ListView ls;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_past_books, container, false);

        ls = view.findViewById(R.id.ls);

        int id;
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        id = sharedPreferences.getInt("uid", -1);

        Log.d("Geting rides", String.valueOf(id));

        ApiService api = RetroFitClient.getApiService();
        api.getRides(id).enqueue(new Callback<ArrayList<PastRide>>() {
            @Override
            public void onResponse(Call<ArrayList<PastRide>> call, Response<ArrayList<PastRide>> response) {
                Log.d("Respose", String.valueOf(response.body()));
                ArrayList<PastRide> ans = response.body();

                for(int i=0 ; i<ans.size() ; i++)
                {
                    Log.d("Resp date", String.valueOf(ans.get(i).getD()));
                    Log.d("Resp dist", String.valueOf(ans.get(i).getDist()));
                }

                PastRidesAdapter adapter = new PastRidesAdapter(getActivity(), ans);
                ls.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ArrayList<PastRide>> call, Throwable t) {
                Log.e(TAG, "Error: " + t.getMessage());
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}