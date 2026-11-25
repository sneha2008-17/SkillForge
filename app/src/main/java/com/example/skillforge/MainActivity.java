package com.example.skillforge;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String userEmail = getIntent().getStringExtra("USER_EMAIL");

        Button btnMySkills = findViewById(R.id.btnMySkills);

        btnMySkills.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MySkillsActivity.class);

            intent.putExtra("USER_EMAIL", userEmail);

            startActivity(intent);
        });
    }
}
