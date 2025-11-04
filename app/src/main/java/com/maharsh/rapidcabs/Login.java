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

import org.json.JSONException;
import org.json.JSONObject;

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
                    api.getUsers(number).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            int status = response.code();
                            Log.d("status", String.valueOf(status));
                            if(status==200){
                                String resp = response.body();
                                Log.d("Response", response.body());
                                try {
                                    JSONObject ans = new JSONObject(resp);
                                    int OTP = ans.getInt("number");
                                    int uid = ans.getInt("uid");
                                    String name = ans.getString("name");
                                    String phoneNumber = ans.getString("phone");
                                    String email = ans.getString("email");

                                    Intent it = new Intent(Login.this, LoginOtp.class);
                                    it.putExtra("Otp", OTP);
                                    it.putExtra("uid", uid);
                                    it.putExtra("name", name);
                                    it.putExtra("phone", phoneNumber);
                                    it.putExtra("email", email);
                                    startActivity(it);
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }else{
                                Toast.makeText(Login.this, "User doesn't exist", Toast.LENGTH_LONG).show();
                                Intent it = new Intent(Login.this, SignUp.class);
                                startActivity(it);
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.e(TAG, "Error: " + t.getMessage());
                        }
                    });
                }
            }
        });
    }
}