
package com.example.skillforge;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ProgressActivity extends AppCompatActivity {

    TextView tvTotalSkills;
    TextView tvTotalLearningTime;
    TextView tvTotalJournalEntries;

    DatabaseHelper dbHelper;
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_progress);

        userEmail = getIntent().getStringExtra("USER_EMAIL");

        dbHelper = new DatabaseHelper(this);

        tvTotalSkills = findViewById(R.id.tvTotalSkills);
        tvTotalLearningTime = findViewById(R.id.tvTotalLearningTime);
        tvTotalJournalEntries = findViewById(R.id.tvTotalJournalEntries);

        if (userEmail != null && !userEmail.isEmpty()) {

            int skills = dbHelper.getTotalSkills(userEmail);

            int learningTime =
                    dbHelper.getTotalLearningTime(userEmail);

            int journalEntries =
                    dbHelper.getTotalJournalEntries(userEmail);

            tvTotalSkills.setText(String.valueOf(skills));

            tvTotalLearningTime.setText(
                    learningTime + " minutes"
            );

            tvTotalJournalEntries.setText(
                    String.valueOf(journalEntries)
            );
        }
    }
}
