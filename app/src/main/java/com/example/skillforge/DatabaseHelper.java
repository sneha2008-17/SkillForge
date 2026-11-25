package com.example.skillforge;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SkillForge.db";
    private static final int DATABASE_VERSION = 2;

    public static final String TABLE_USERS = "users";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_EMAIL + " TEXT UNIQUE, " +
                COLUMN_PASSWORD + " TEXT)";

        db.execSQL(createUsersTable);

        String createSkillsTable = "CREATE TABLE skills (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_email TEXT, " +
                "skill_name TEXT, " +
                "skill_level TEXT)";

        db.execSQL(createSkillsTable);
    }


    public boolean insertUser(String name, String email, String password) {

        SQLiteDatabase db = this.getWritableDatabase();

        android.content.ContentValues values = new android.content.ContentValues();

        values.put(COLUMN_NAME, name);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);

        long result = db.insert(TABLE_USERS, null, values);

        db.close();

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }


    public boolean checkUser(String email, String password) {

        SQLiteDatabase db = this.getReadableDatabase();

        android.database.Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_USERS +
                        " WHERE " + COLUMN_EMAIL + "=? AND " +
                        COLUMN_PASSWORD + "=?",
                new String[]{email, password}
        );

        boolean exists = cursor.getCount() > 0;

        cursor.close();
        db.close();

        return exists;
    }

    public boolean insertSkill(String email, String skillName, String skillLevel) {

        SQLiteDatabase db = this.getWritableDatabase();

        android.content.ContentValues values = new android.content.ContentValues();

        values.put("user_email", email);
        values.put("skill_name", skillName);
        values.put("skill_level", skillLevel);

        long result = db.insert("skills", null, values);

        db.close();

        return result != -1;
    }

    public ArrayList<String> getSkills(String email) {

        ArrayList<String> skillList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        android.database.Cursor cursor = db.rawQuery(
                "SELECT skill_name, skill_level FROM skills WHERE user_email=?",
                new String[]{email}
        );

        while (cursor.moveToNext()) {

            String skillName = cursor.getString(0);
            String skillLevel = cursor.getString(1);

            skillList.add(skillName + " - " + skillLevel);
        }

        cursor.close();
        db.close();

        return skillList;
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion < 2) {

            String createSkillsTable = "CREATE TABLE skills (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "user_email TEXT, " +
                    "skill_name TEXT, " +
                    "skill_level TEXT)";

            db.execSQL(createSkillsTable);
        }
    }
}
