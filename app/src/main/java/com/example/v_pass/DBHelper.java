package com.example.v_pass;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "vpass.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_VISITOR = "visitor";
    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_IC = "ic";
    public static final String COL_PHONE = "phone";
    public static final String COL_VEHICLE = "vehicle";
    public static final String COL_PURPOSE = "purpose";
    public static final String COL_QR = "qr_data";
    public static final String COL_TIME = "timestamp";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_VISITOR + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT, " +
                COL_IC + " TEXT, " +
                COL_PHONE + " TEXT, " +
                COL_VEHICLE + " TEXT, " +
                COL_PURPOSE + " TEXT, " +
                COL_QR + " TEXT, " +
                COL_TIME + " TEXT" +
                ");";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VISITOR + ";");
        onCreate(db);
    }

    // 🔹 Insert visitor record
    public boolean insertVisitor(
            String name,
            String ic,
            String phone,
            String vehicle,
            String purpose,
            String qrData,
            String time
    ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_NAME, name);
        values.put(COL_IC, ic);
        values.put(COL_PHONE, phone);
        values.put(COL_VEHICLE, vehicle);
        values.put(COL_PURPOSE, purpose);
        values.put(COL_QR, qrData);
        values.put(COL_TIME, time);

        long result = db.insert(TABLE_VISITOR, null, values);
        return result != -1;
    }
}
