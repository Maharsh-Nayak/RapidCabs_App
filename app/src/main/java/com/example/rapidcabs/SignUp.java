package com.example.rapidcabs;

import static android.content.ContentValues.TAG;

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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp extends AppCompatActivity {

    EditText inputEmail, inputNumber, inputFname, inputLname;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        inputEmail = findViewById(R.id.email);
        inputFname = findViewById(R.id.Fname);
        inputLname = findViewById(R.id.Lname);
        inputNumber = findViewById(R.id.phnum);

        submit = findViewById(R.id.SignUpSubmit);

        ApiService api = RetroFitClient.getApiService();

        api.getUsers().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    List<User> users = response.body();
                    for (User u : users) {
                        Log.d(TAG, "User: " + u.getName() + " (" + u.getEmail() + ")");
                    }
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.e(TAG, "Error: " + t.getMessage());
            }
        });



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=null, fname=null, lname=null;
                int num=0;
                email = String.valueOf(inputEmail.getText());
                fname = String.valueOf(inputFname.getText());
                lname = String.valueOf(inputLname.getText());
                num = Integer.parseInt(String.valueOf(inputNumber.getText()));

                if(email==null){
                    Toast.makeText(SignUp.this, "Please enter an email", Toast.LENGTH_SHORT).show();
                }
                if(fname==null || lname==null){
                    Toast.makeText(SignUp.this, "Please enter you name", Toast.LENGTH_SHORT).show();
                }
                if(num==0){
                    Toast.makeText(SignUp.this, "Please enter you number", Toast.LENGTH_SHORT).show();
                }

                User newUser = new User(fname, email, num);

                api.createUser(newUser).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()) {
                            User created = response.body();
                            Log.d(TAG, "Created User ID: " + created.getNum());
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.e(TAG, "Error: " + t.getMessage());
                    }
                });
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}