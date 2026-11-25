
package com.example.skillforge;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegistrationActivity extends AppCompatActivity {

    EditText etName, etRegEmail, etRegPassword, etConfirmPassword;
    Button btnRegister;
    TextView tvGoToLogin;
    DatabaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        dbHelper = new DatabaseHelper(this);

        etName = findViewById(R.id.etName);
        etRegEmail = findViewById(R.id.etRegEmail);
        etRegPassword = findViewById(R.id.etRegPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        btnRegister = findViewById(R.id.btnRegister);
        tvGoToLogin = findViewById(R.id.tvGoToLogin);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = etName.getText().toString().trim();
                String email = etRegEmail.getText().toString().trim();
                String password = etRegPassword.getText().toString().trim();
                String confirmPassword = etConfirmPassword.getText().toString().trim();

                if (name.isEmpty()) {
                    etName.setError("Enter your name");
                }
                else if (email.isEmpty()) {
                    etRegEmail.setError("Enter your email");
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    etRegEmail.setError("Enter a valid email");
                }
                else if (password.isEmpty()) {
                    etRegPassword.setError("Enter a password");
                }
                else if (password.length() < 8) {
                    etRegPassword.setError("Password must have at least 8 characters");
                }
                else if (confirmPassword.isEmpty()) {
                    etConfirmPassword.setError("Confirm your password");
                }
                else if (!password.equals(confirmPassword)) {
                    etConfirmPassword.setError("Passwords do not match");
                }
                else {
                    boolean inserted = dbHelper.insertUser(name, email, password);

                    if (inserted) {
                        Toast.makeText(RegistrationActivity.this,
                                "Registration successful!",
                                Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        etRegEmail.setError("Email already registered or database error");
                    }
                }
            }
        });

        tvGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
