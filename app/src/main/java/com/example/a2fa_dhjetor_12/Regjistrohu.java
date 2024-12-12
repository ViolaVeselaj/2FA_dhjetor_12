package com.example.a2fa_dhjetor_12;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

import java.util.regex.Pattern;

public class Regjistrohu extends AppCompatActivity {
    private EditText nameField, emailField, phoneField, passwordField;
    DB DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_regjistrohu);

        nameField = findViewById(R.id.NameField);
        emailField = findViewById(R.id.EmailField);
        phoneField = findViewById(R.id.PhoneField);
        passwordField = findViewById(R.id.PasswordField);
        Button signUpButton = findViewById(R.id.SignUpHandler);
        TextView loginRedirect = findViewById(R.id.LogInRedirect);

        DB=new DB(this);

        signUpButton.setOnClickListener(v -> {
            if (validateFields()) {

                String name = nameField.getText().toString().trim();
                String email = emailField.getText().toString().trim();
                String password = passwordField.getText().toString().trim();
                String phone = phoneField.getText().toString().trim();
                // Proceed with signup logic
                if (DB.checkAdminEmail(email)) {
                    Toast.makeText(this, "Egziston perdorues me kete email.", Toast.LENGTH_SHORT).show();
                } else if (DB.insertAdminUser(email,password,name,phone)) {
                    Toast.makeText(this, "Perdoruesi u shtua", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Gabim", Toast.LENGTH_SHORT).show();
                }
            }
        });

        loginRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Regjistrohu.this, Qasja.class);
                startActivity(intent);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private boolean validateFields() {
        // Retrieve values
        String name = nameField.getText().toString().trim();
        String email = emailField.getText().toString().trim();
        String phone = phoneField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();
        TextView logInRedirect = findViewById(R.id.LogInRedirect);

        logInRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Regjistrohu.this, Qasja.class);
                startActivity(intent);
            }
        });

        if (TextUtils.isEmpty(name) || !name.matches("[a-zA-Z]+")) {
            nameField.setError("Emri duhet te kete vetem karaktere");
            return false;
        }

        if (TextUtils.isEmpty(email) || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailField.setError("Duhet te jete email valide");
            return false;
        }

        if (TextUtils.isEmpty(phone) || !phone.matches("\\d{9,12}")) {
            phoneField.setError("Numri i telefonit duhet te kete 9 deri 12 numra");
            return false;
        }

        if (TextUtils.isEmpty(password) ||
                !Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=]).{6,20}$").matcher(password).matches()) {
            passwordField.setError("Paswordi duhet te kete 1 shkronje te vogel dhe 1 te  madhe, 1 numer, 1 karakter special, dhe te jete i gjate 6-20 karaktere");
            return false;
        }

        return true;
    }
}