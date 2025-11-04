package com.maharsh.rapidcabs;

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

public class SignUp extends AppCompatActivity {

    EditText inputEmail, inputNumber, inputFname;
    Button submit, login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        inputEmail = findViewById(R.id.email);
        inputFname = findViewById(R.id.Fname);
        inputNumber = findViewById(R.id.phnum);

        submit = findViewById(R.id.SignUpSubmit);

        ApiService api = RetroFitClient.getApiService();
        login = findViewById(R.id.loginbtn);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(SignUp.this, Login.class);
                startActivity(it);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=null, fname=null, lname=null;
                String num;
                email = String.valueOf(inputEmail.getText());
                fname = String.valueOf(inputFname.getText());
                num = (String.valueOf(inputNumber.getText()));

                if(email==null){
                    Toast.makeText(SignUp.this, "Please enter an email", Toast.LENGTH_SHORT).show();
                }
                if(fname==null){
                    Toast.makeText(SignUp.this, "Please enter you name", Toast.LENGTH_SHORT).show();
                }
                if(num==null){
                    Toast.makeText(SignUp.this, "Please enter you number", Toast.LENGTH_SHORT).show();
                }

                Toast.makeText(SignUp.this, "Sending", Toast.LENGTH_SHORT).show();

                ApiResponse resp = new ApiResponse();
                api.createUser(fname, email, num).enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if (response.isSuccessful()) {
                            ApiResponse created = response.body();
                            Log.d(TAG, "Created User ID: " + created.getResp());
                            Intent it = new Intent(SignUp.this, Home.class);
                            startActivity(it);
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        Log.e(TAG, "Error: " + t.getMessage());
                    }
                });

            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.phoneNumber), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}