package com.example.skillforge;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LearningJournalActivity extends AppCompatActivity {

    EditText etLearning, etLearningTime, etChallenges, etTakeaways;
    Button btnSaveJournal;
    TextView tvJournalEntries;

    DatabaseHelper dbHelper;
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_learning_journal);

        userEmail = getIntent().getStringExtra("USER_EMAIL");

        dbHelper = new DatabaseHelper(this);

        etLearning = findViewById(R.id.etLearning);
        etLearningTime = findViewById(R.id.etLearningTime);
        etChallenges = findViewById(R.id.etChallenges);
        etTakeaways = findViewById(R.id.etTakeaways);

        btnSaveJournal = findViewById(R.id.btnSaveJournal);
        tvJournalEntries = findViewById(R.id.tvJournalEntries);

        loadJournalEntries();

        btnSaveJournal.setOnClickListener(v -> {

            String learning = etLearning.getText().toString().trim();
            String timeText = etLearningTime.getText().toString().trim();
            String challenges = etChallenges.getText().toString().trim();
            String takeaways = etTakeaways.getText().toString().trim();

            if (learning.isEmpty()) {
                etLearning.setError("Please describe what you learned");
                return;
            }

            if (timeText.isEmpty()) {
                etLearningTime.setError("Enter learning time");
                return;
            }

            int learningTime = Integer.parseInt(timeText);

            if (learningTime <= 0) {
                etLearningTime.setError("Enter a valid time");
                return;
            }

            if (userEmail == null || userEmail.isEmpty()) {
                Toast.makeText(this, "User session not found",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            String date = new SimpleDateFormat(
                    "dd-MM-yyyy",
                    Locale.getDefault()
            ).format(new Date());

            boolean saved = dbHelper.insertJournal(
                    userEmail,
                    learning,
                    learningTime,
                    challenges,
                    takeaways,
                    date
            );

            if (saved) {

                Toast.makeText(this, "Journal entry saved!",
                        Toast.LENGTH_SHORT).show();

                etLearning.setText("");
                etLearningTime.setText("");
                etChallenges.setText("");
                etTakeaways.setText("");

                loadJournalEntries();

            } else {

                Toast.makeText(this, "Unable to save entry",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadJournalEntries() {

        ArrayList<String> entries =
                dbHelper.getJournalEntries(userEmail);

        if (entries.isEmpty()) {

            tvJournalEntries.setText(
                    "No journal entries yet. Start recording your learning!"
            );

        } else {

            StringBuilder builder = new StringBuilder();

            for (String entry : entries) {
                builder.append(entry);
                builder.append("\n\n--------------------\n\n");
            }

            tvJournalEntries.setText(builder.toString());
        }
    }
}
