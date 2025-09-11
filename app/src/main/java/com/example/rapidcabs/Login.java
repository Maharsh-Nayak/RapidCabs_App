package com.example.rapidcabs;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    EditText num;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        btn = findViewById(R.id.Next_btn_Login);
        num = findViewById(R.id.phoneNumberOtp);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = String.valueOf(num.getText());
                if(number==""){
                    Toast.makeText(Login.this, "Please enter phone number", Toast.LENGTH_SHORT).show();
                }else{
                    ApiService api = RetroFitClient.getApiService();
                    api.getUsers(number).enqueue(new Callback<Integer>() {
                        @Override
                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                            Integer resp = response.body();
                            boolean exists = resp!=-1 ? true : false;
                            if(exists){
                                Intent it = new Intent(Login.this, LoginOtp.class);
                                it.putExtra("Otp", resp);
                                startActivity(it);
                            }else{
                                Toast.makeText(Login.this, "User doesn't exist", Toast.LENGTH_LONG).show();
                                Intent it = new Intent(Login.this, SignUp.class);
                                startActivity(it);
                            }
                        }

                        @Override
                        public void onFailure(Call<Integer> call, Throwable t) {
                            Log.e(TAG, "Error: " + t.getMessage());
                        }
                    });
                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.phoneNumber), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}