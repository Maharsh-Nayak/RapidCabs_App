package com.example.rapidcabs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginOtp extends AppCompatActivity {

    Button btn;
    EditText otp;

    private  Integer o;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_otp);

        btn = findViewById(R.id.btn_otp);
        otp = findViewById(R.id.Otp_ip);

        Intent it = getIntent();
        o = it.getIntExtra("Otp", -1);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int OTP = Integer.parseInt(String.valueOf(otp.getText()));
                if(OTP==0){
                    Toast.makeText(LoginOtp.this, "Please enter OTP", Toast.LENGTH_SHORT).show();
                }else if(OTP == o){
                    Toast.makeText(LoginOtp.this, "Login Successful", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(LoginOtp.this, Home.class);
                    startActivity(intent);
                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}