package com.example.skillforge;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Button;
import java.util.ArrayList;
import android.app.AlertDialog;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView;

public class SkillAdapter extends ArrayAdapter<Skill> {

    Activity context;
    ArrayList<Skill> skills;
    DatabaseHelper dbHelper;
    String userEmail;

    public SkillAdapter(Activity context, ArrayList<Skill> skills,
                        DatabaseHelper dbHelper, String userEmail) {

        super(context, 0, skills);

        this.context = context;
        this.skills = skills;
        this.dbHelper = dbHelper;
        this.userEmail = userEmail;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_skill, parent, false);
        }

        TextView tvSkillName = convertView.findViewById(R.id.tvSkillName);
        TextView tvSkillLevel = convertView.findViewById(R.id.tvSkillLevel);

        Button btnEditSkill = convertView.findViewById(R.id.btnEditSkill);
        Button btnDeleteSkill = convertView.findViewById(R.id.btnDeleteSkill);

        Skill skill = skills.get(position);

        tvSkillName.setText(skill.getSkillName());
        tvSkillLevel.setText(skill.getSkillLevel());
        btnEditSkill.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setTitle("Edit Skill");

            android.widget.LinearLayout layout = new android.widget.LinearLayout(context);
            layout.setOrientation(android.widget.LinearLayout.VERTICAL);
            layout.setPadding(40, 20, 40, 10);

            EditText etEditName = new EditText(context);
            etEditName.setText(skill.getSkillName());
            etEditName.setHint("Skill name");

            Spinner spinnerEditLevel = new Spinner(context);

            String[] levels = {"Beginner", "Intermediate", "Advanced"};

            ArrayAdapter<String> levelAdapter = new ArrayAdapter<>(
                    context,
                    android.R.layout.simple_spinner_item,
                    levels
            );

            levelAdapter.setDropDownViewResource(
                    android.R.layout.simple_spinner_dropdown_item
            );

            spinnerEditLevel.setAdapter(levelAdapter);

            int selectedPosition = levelAdapter.getPosition(skill.getSkillLevel());
            spinnerEditLevel.setSelection(selectedPosition);

            layout.addView(etEditName);
            layout.addView(spinnerEditLevel);

            builder.setView(layout);

            builder.setPositiveButton("Save", (dialog, which) -> {

                String newName = etEditName.getText().toString().trim();
                String newLevel = spinnerEditLevel.getSelectedItem().toString();

                if (newName.isEmpty()) {
                    android.widget.Toast.makeText(
                            context, "Skill name cannot be empty",
                            android.widget.Toast.LENGTH_SHORT
                    ).show();
                    return;
                }

                boolean updated = dbHelper.updateSkill(
                        skill.getId(), userEmail, newName, newLevel
                );

                if (updated) {
                    skill.setSkillName(newName);
                    skill.setSkillLevel(newLevel);
                    notifyDataSetChanged();

                    android.widget.Toast.makeText(
                            context, "Skill updated!",
                            android.widget.Toast.LENGTH_SHORT
                    ).show();
                }
            });

            builder.setNegativeButton("Cancel", null);

            builder.show();
        });

        btnDeleteSkill.setOnClickListener(v -> {

            new AlertDialog.Builder(context)
                    .setTitle("Delete Skill")
                    .setMessage("Are you sure you want to delete "
                            + skill.getSkillName() + "?")

                    .setPositiveButton("Delete", (dialog, which) -> {

                        boolean deleted = dbHelper.deleteSkill(
                                skill.getId(), userEmail
                        );

                        if (deleted) {

                            skills.remove(skill);
                            notifyDataSetChanged();

                            android.widget.Toast.makeText(
                                    context,
                                    "Skill deleted!",
                                    android.widget.Toast.LENGTH_SHORT
                            ).show();

                        } else {

                            android.widget.Toast.makeText(
                                    context,
                                    "Unable to delete skill",
                                    android.widget.Toast.LENGTH_SHORT
                            ).show();
                        }
                    })

                    .setNegativeButton("Cancel", null)
                    .show();
        });

        return convertView;
    }
}