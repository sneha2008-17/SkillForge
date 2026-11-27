
package com.example.skillforge;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.graphics.Color;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class MySkillsActivity extends AppCompatActivity {

    EditText etSkillName;
    Spinner spinnerLevel;
    Button btnAddSkill;
    ListView listSkills;

    String userEmail;
    DatabaseHelper dbHelper;

    ArrayList<Skill> skills;
    SkillAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_skills);

        userEmail = getIntent().getStringExtra("USER_EMAIL");

        dbHelper = new DatabaseHelper(this);

        etSkillName = findViewById(R.id.etSkillName);
        spinnerLevel = findViewById(R.id.spinnerLevel);
        btnAddSkill = findViewById(R.id.btnAddSkill);
        listSkills = findViewById(R.id.listSkills);

        // Skill proficiency levels
        String[] levels = {"Beginner", "Intermediate", "Advanced"};

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                levels
        ) {
            @Override
            public View getView(int position, View convertView,
                                ViewGroup parent) {

                View view = super.getView(position, convertView, parent);

                TextView text = view.findViewById(android.R.id.text1);
                text.setTextColor(Color.parseColor("#222222"));

                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {

                View view = super.getDropDownView(position, convertView, parent);

                TextView text = view.findViewById(android.R.id.text1);

                text.setTextColor(Color.parseColor("#222222"));
                text.setBackgroundColor(Color.WHITE);
                view.setBackgroundColor(Color.WHITE);

                return view;
            }
        };

        spinnerAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerLevel.setAdapter(spinnerAdapter);

        // Load skills from SQLite
        skills = dbHelper.getSkills(userEmail);

        // Connect custom adapter
        adapter = new SkillAdapter(
                this,
                skills,
                dbHelper,
                userEmail
        );

        listSkills.setAdapter(adapter);

        // Add skill button
        btnAddSkill.setOnClickListener(v -> {

            String skillName = etSkillName.getText().toString().trim();
            String level = spinnerLevel.getSelectedItem().toString();

            if (skillName.isEmpty()) {

                etSkillName.setError("Please enter a skill");

            } else {

                boolean inserted = dbHelper.insertSkill(
                        userEmail,
                        skillName,
                        level
                );

                if (inserted) {

                    // Reload skills from database
                    skills.clear();
                    skills.addAll(dbHelper.getSkills(userEmail));

                    adapter.notifyDataSetChanged();

                    etSkillName.setText("");

                    Toast.makeText(
                            MySkillsActivity.this,
                            "Skill saved in database!",
                            Toast.LENGTH_SHORT
                    ).show();

                } else {

                    Toast.makeText(
                            MySkillsActivity.this,
                            "Unable to save skill",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        });
    }
}
