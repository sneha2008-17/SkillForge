package com.example.skillforge;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    Button btnLogin;
    TextView tvRegister;
    DatabaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dbHelper = new DatabaseHelper(this);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(v -> {

            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty()) {
                etEmail.setError("Please enter your email");
            }
            else if (password.isEmpty()) {
                etPassword.setError("Please enter your password");
            }
            else {
                boolean validUser = dbHelper.checkUser(email, password);

                if (validUser) {
                    Toast.makeText(LoginActivity.this,
                            "Login successful!",
                            Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                    intent.putExtra("USER_EMAIL", email);

                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(LoginActivity.this,
                            "Invalid email or password",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
