package com.example.a2fa_dhjetor_12;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;



public class KontrolloOTP extends AppCompatActivity {
    private EmailSender sender = new EmailSender();
    private String currentOtp;
    private DB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_kontrollo_otp);

        currentOtp = getIntent().getStringExtra("otp");
        final String email = getIntent().getStringExtra("email");

        Toast.makeText(KontrolloOTP.this, currentOtp, Toast.LENGTH_SHORT).show();

        EditText codeTxt = findViewById(R.id.OtpTxt);
        Button validateBtn= findViewById(R.id.Verifybtn);
        Button resendBtn = findViewById(R.id.resendbtn);
        TextView logRedirect = findViewById(R.id.LogRedirect);


        validateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp=codeTxt.getText().toString();
                if (validateOTP(email, otp)) {
                    Toast.makeText(KontrolloOTP.this, "Valid OTP", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(KontrolloOTP.this, FaqjaKryesore.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(KontrolloOTP.this, "Invalid or expired OTP", Toast.LENGTH_SHORT).show();
                }
            }
        });
        resendBtn.setOnClickListener(v -> {
            currentOtp = OTPclass.generate(6);
            updateOTPInDatabase(email, currentOtp);
            sendMail(email, currentOtp);
        });
        logRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(KontrolloOTP.this, FaqjaKryesore.class);
                startActivity(intent);
            }
        });
    }
    private void sendMail(String email, String otp) {
        new Thread(() -> {
            try {
                sender.sendOTPEmail(email, otp);
                runOnUiThread(() -> Toast.makeText(KontrolloOTP.this, "OTP u ridergua " + email, Toast.LENGTH_SHORT).show());
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(KontrolloOTP.this, "nuk u dergua OTP", Toast.LENGTH_SHORT).show());
                e.printStackTrace();
            }
        }).start();
    }
    private boolean validateOTP(String email, String otp) {
        DB db = new DB(this);
        Cursor cursor = db.getOTPData(email);

        if (cursor.moveToFirst()) {
            String storedOtp = cursor.getString(0);
            long timestamp = cursor.getLong(1);
            cursor.close();

            long currentTime = System.currentTimeMillis();
            if (storedOtp.equals(otp) && currentTime - timestamp <= 300000) {
                return true;
            }
        }
        return false;
    }
    private void updateOTPInDatabase(String email, String otp) {
        DB db = new DB(this);
        db.updateOTP(email, otp);
    }
}
