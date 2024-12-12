package com.example.a2fa_dhjetor_12;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import org.mindrot.jbcrypt.BCrypt;

import android.database.sqlite.SQLiteOpenHelper;

public class DB extends SQLiteOpenHelper {

public static final String DBNAME="2fa.db";

    public DB(@Nullable Context context) {
        super(context, "2fa.db", null, 3);
    }

    public void onCreate(SQLiteDatabase db){
    db.execSQL("CREATE TABLE adminUser (email TEXT PRIMARY KEY, password TEXT, name TEXT, phone TEXT, otp TEXT, otpTimestamp INTEGER)");
    }

    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
     db.execSQL("DROP TABLE IF EXISTS adminUser");
     onCreate(db);
    }

    public Boolean validateUser(String email, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM adminUser WHERE email=?", new String []{email});
        if(cursor.moveToFirst()){
            String storedHashedPassword = cursor.getString(0);
            cursor.close();

            return BCrypt.checkpw(password, storedHashedPassword);
        }
        cursor.close();
        return false;
    }

    public Boolean insertAdminUser(String email, String password, String name, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        contentValues.put("email", email);
        contentValues.put("password", hashedPassword);
        contentValues.put("name", name);
        contentValues.put("phone", phone);

        long result = db.insert("adminUser", null, contentValues);
        return result != -1;
    }

    public Boolean checkAdminEmail(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM adminUser WHERE email=?", new String[]{email});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }
    public void updateOTP(String email, String otp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        long timestamp = System.currentTimeMillis();

        contentValues.put("otp", otp);
        contentValues.put("otpTimestamp", timestamp); // Correct column name

        // Use db.update instead of execSQL
        db.update("adminUser", contentValues, "email=?", new String[]{email});
    }

    public Cursor getOTPData(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT otp, otpTimestamp FROM adminUser WHERE email=?", new String[]{email});
    }

}
