package com.example.a2fa_dhjetor_12;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import javax.mail.MessagingException;

public class Qasja extends AppCompatActivity {
    private EmailSender sender= new EmailSender();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_qasja);

        EditText userEmail = findViewById(R.id.EmailField);
        EditText userPass = findViewById(R.id.PasswordField);
        Button loginBtn = findViewById(R.id.LogInHandler);
        TextView signUpRedirect = findViewById(R.id.SignUpRedirect);
        DB db = new DB(this);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = userEmail.getText().toString().trim();
                String pass= userPass.getText().toString().trim();
                if(email.isEmpty()||pass.isEmpty()){
                    Toast.makeText(Qasja.this,"ploteso fushat e zbrazeta",Toast.LENGTH_SHORT).show();
                } else if (db.validateUser(email, pass)){
                    String otp=OTPclass.generate(6);
                    db.updateOTP(email, otp);
                    sendMail(email,otp);
                    Intent intent= new Intent(Qasja.this,KontrolloOTP.class);
                    intent.putExtra("email",email);
                    intent.putExtra("otp",otp);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(Qasja.this,"nuk jane dhene te dhenat e sakta",Toast.LENGTH_SHORT).show();
                }
            }
        });

        signUpRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Qasja.this, Regjistrohu.class);
                startActivity(intent);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void sendMail(String email, String otp) {
        new Thread(() -> {
            try {
                sender.sendOTPEmail(email, otp);
                runOnUiThread(() -> Toast.makeText(Qasja.this, "OTP u dergua ne " + email, Toast.LENGTH_SHORT).show());
            } catch (MessagingException e) {
                runOnUiThread(() -> Toast.makeText(Qasja.this, "OTP nuk u dergua", Toast.LENGTH_SHORT).show());
                e.printStackTrace();
            }
        }).start();
    }
}