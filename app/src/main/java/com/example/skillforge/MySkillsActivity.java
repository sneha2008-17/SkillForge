
package com.example.skillforge;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;



public class MySkillsActivity extends AppCompatActivity {

    EditText etSkillName;
    Spinner spinnerLevel;
    Button btnAddSkill;
    ListView listSkills;
    String userEmail;
    DatabaseHelper dbHelper;

    ArrayList<String> skills;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_skills);
        userEmail = getIntent().getStringExtra("USER_EMAIL");
        Toast.makeText(this, "User: " + userEmail, Toast.LENGTH_LONG).show();

        dbHelper = new DatabaseHelper(this);

        etSkillName = findViewById(R.id.etSkillName);
        spinnerLevel = findViewById(R.id.spinnerLevel);
        btnAddSkill = findViewById(R.id.btnAddSkill);
        listSkills = findViewById(R.id.listSkills);

        String[] levels = {"Beginner", "Intermediate", "Advanced"};

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                levels
        ) {
            @Override
            public android.view.View getView(int position,
                                             android.view.View convertView,
                                             android.view.ViewGroup parent) {

                android.view.View view = super.getView(position, convertView, parent);

                android.widget.TextView text = view.findViewById(android.R.id.text1);
                text.setTextColor(android.graphics.Color.parseColor("#222222"));

                return view;
            }

            @Override
            public android.view.View getDropDownView(int position,
                                                     android.view.View convertView,
                                                     android.view.ViewGroup parent) {

                android.view.View view = super.getDropDownView(position, convertView, parent);

                android.widget.TextView text = view.findViewById(android.R.id.text1);

                text.setTextColor(android.graphics.Color.parseColor("#222222"));
                text.setBackgroundColor(android.graphics.Color.WHITE);
                view.setBackgroundColor(android.graphics.Color.WHITE);

                return view;
            }
        };

        spinnerAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerLevel.setAdapter(spinnerAdapter);

        skills = dbHelper.getSkills(userEmail);

        adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                skills
        ) {
            @Override
            public android.view.View getView(int position,
                                             android.view.View convertView,
                                             android.view.ViewGroup parent) {

                android.view.View view = super.getView(position, convertView, parent);

                android.widget.TextView text = view.findViewById(android.R.id.text1);
                text.setTextColor(android.graphics.Color.parseColor("#222222"));

                return view;
            }
        };

        listSkills.setAdapter(adapter);

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

                    skills.add(skillName + " - " + level);
                    adapter.notifyDataSetChanged();

                    etSkillName.setText("");

                    Toast.makeText(MySkillsActivity.this,
                            "Skill saved in database!",
                            Toast.LENGTH_SHORT).show();

                } else {

                    Toast.makeText(MySkillsActivity.this,
                            "Unable to save skill",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

