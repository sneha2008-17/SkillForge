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

        // My Skills button
        Button btnMySkills = findViewById(R.id.btnMySkills);

        btnMySkills.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    MySkillsActivity.class
            );

            intent.putExtra("USER_EMAIL", userEmail);
            startActivity(intent);
        });


        // Career Explorer button
        Button btnExploreCareers = findViewById(R.id.btnExploreCareers);

        btnExploreCareers.setOnClickListener(view -> {

            Intent careerIntent = new Intent(
                    MainActivity.this,
                    ExploreCareerActivity.class
            );

            careerIntent.putExtra("USER_EMAIL", userEmail);
            startActivity(careerIntent);
        });


// Skill Gap Analysis button
        Button btnSkillGap = findViewById(R.id.btnSkillGap);

        btnSkillGap.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    SkillGapActivity.class
            );

            startActivity(intent);
        });


// Learning Roadmap button
        Button btnRoadmap = findViewById(R.id.btnRoadmap);

        btnRoadmap.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    RoadmapActivity.class
            );

            startActivity(intent);
        });


// Learning Journal button
        Button btnJournal = findViewById(R.id.btnJournal);

        btnJournal.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    LearningJournalActivity.class
            );

            intent.putExtra("USER_EMAIL", userEmail);
            startActivity(intent);
        });


// Progress Analytics button
        Button btnProgress = findViewById(R.id.btnProgress);

        btnProgress.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    ProgressActivity.class
            );

            intent.putExtra("USER_EMAIL", userEmail);
            startActivity(intent);
        });


    }
}
