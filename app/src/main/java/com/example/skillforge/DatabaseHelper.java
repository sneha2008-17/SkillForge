
package com.example.skillforge;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SkillForge.db";
    private static final int DATABASE_VERSION = 3;

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

        String createJournalTable = "CREATE TABLE journal (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_email TEXT, " +
                "learning TEXT, " +
                "learning_time INTEGER, " +
                "challenges TEXT, " +
                "takeaways TEXT, " +
                "entry_date TEXT)";

        db.execSQL(createJournalTable);
    }

    public boolean insertUser(String name, String email, String password) {

        SQLiteDatabase db = this.getWritableDatabase();

        android.content.ContentValues values =
                new android.content.ContentValues();

        values.put(COLUMN_NAME, name);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);

        long result = db.insert(TABLE_USERS, null, values);

        db.close();

        return result != -1;
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

        android.content.ContentValues values =
                new android.content.ContentValues();

        values.put("user_email", email);
        values.put("skill_name", skillName);
        values.put("skill_level", skillLevel);

        long result = db.insert("skills", null, values);

        db.close();

        return result != -1;
    }

    public ArrayList<Skill> getSkills(String email) {

        ArrayList<Skill> skillList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        android.database.Cursor cursor = db.rawQuery(
                "SELECT id, skill_name, skill_level FROM skills WHERE user_email=?",
                new String[]{email}
        );

        while (cursor.moveToNext()) {

            int id = cursor.getInt(0);
            String skillName = cursor.getString(1);
            String skillLevel = cursor.getString(2);

            Skill skill = new Skill(id, skillName, skillLevel);
            skillList.add(skill);
        }

        cursor.close();
        db.close();

        return skillList;
    }

    // Update an existing skill
    public boolean updateSkill(int id, String email,
                               String skillName, String skillLevel) {

        SQLiteDatabase db = this.getWritableDatabase();

        android.content.ContentValues values =
                new android.content.ContentValues();

        values.put("skill_name", skillName);
        values.put("skill_level", skillLevel);

        int result = db.update(
                "skills",
                values,
                "id=? AND user_email=?",
                new String[]{String.valueOf(id), email}
        );

        db.close();

        return result > 0;
    }

    // Delete a skill
    public boolean deleteSkill(int id, String email) {

        SQLiteDatabase db = this.getWritableDatabase();

        int result = db.delete(
                "skills",
                "id=? AND user_email=?",
                new String[]{String.valueOf(id), email}
        );

        db.close();

        return result > 0;
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

        if (oldVersion < 3) {

            String createJournalTable = "CREATE TABLE journal (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "user_email TEXT, " +
                    "learning TEXT, " +
                    "learning_time INTEGER, " +
                    "challenges TEXT, " +
                    "takeaways TEXT, " +
                    "entry_date TEXT)";

            db.execSQL(createJournalTable);
        }
    }

    public boolean insertJournal(String email, String learning,
                                 int learningTime, String challenges,
                                 String takeaways, String date) {

        SQLiteDatabase db = this.getWritableDatabase();

        android.content.ContentValues values =
                new android.content.ContentValues();

        values.put("user_email", email);
        values.put("learning", learning);
        values.put("learning_time", learningTime);
        values.put("challenges", challenges);
        values.put("takeaways", takeaways);
        values.put("entry_date", date);

        long result = db.insert("journal", null, values);

        db.close();

        return result != -1;
    }

    public ArrayList<String> getJournalEntries(String email) {

        ArrayList<String> entries = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        android.database.Cursor cursor = db.rawQuery(
                "SELECT learning, learning_time, challenges, takeaways, entry_date " +
                        "FROM journal WHERE user_email=? ORDER BY id DESC",
                new String[]{email}
        );

        while (cursor.moveToNext()) {

            String learning = cursor.getString(0);
            int time = cursor.getInt(1);
            String challenges = cursor.getString(2);
            String takeaways = cursor.getString(3);
            String date = cursor.getString(4);

            String entry = "Date: " + date +
                    "\nLearned: " + learning +
                    "\nTime: " + time + " minutes" +
                    "\nChallenges: " + challenges +
                    "\nTakeaways: " + takeaways;

            entries.add(entry);
        }

        cursor.close();
        db.close();

        return entries;
    }

    public int getTotalSkills(String email) {

        SQLiteDatabase db = this.getReadableDatabase();

        android.database.Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM skills WHERE user_email=?",
                new String[]{email}
        );

        int count = 0;

        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        cursor.close();
        db.close();

        return count;
    }

    public int getTotalJournalEntries(String email) {

        SQLiteDatabase db = this.getReadableDatabase();

        android.database.Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM journal WHERE user_email=?",
                new String[]{email}
        );

        int count = 0;

        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        cursor.close();
        db.close();

        return count;
    }

    public int getTotalLearningTime(String email) {

        SQLiteDatabase db = this.getReadableDatabase();

        android.database.Cursor cursor = db.rawQuery(
                "SELECT SUM(learning_time) FROM journal WHERE user_email=?",
                new String[]{email}
        );

        int totalTime = 0;

        if (cursor.moveToFirst()) {
            totalTime = cursor.getInt(0);
        }

        cursor.close();
        db.close();

        return totalTime;
    }
}
