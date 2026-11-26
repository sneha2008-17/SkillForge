package com.example.skillforge;

import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class SkillGapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_skill_gap);

        Button btnCreateRoadmap = findViewById(R.id.btnCreateRoadmap);

        btnCreateRoadmap.setOnClickListener(v -> {

            Intent intent = new Intent(
                    SkillGapActivity.this,
                    RoadmapActivity.class
            );

            startActivity(intent);
        });
    }
}
