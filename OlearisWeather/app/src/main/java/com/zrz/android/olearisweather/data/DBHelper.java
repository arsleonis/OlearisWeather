package com.zrz.android.olearisweather.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME="OlearisWeather";
    private static final int DB_VERSION=1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table "+Contract.PLACES_TABLE_NAME +"("+Contract.PLACES_TABLE_COLUMN_ID +" integer primary key autoincrement,"
                +Contract.PLACES_TABLE_COLUMN_LATITUDE +" real,"+Contract.PLACES_TABLE_COLUMN_LONGITUDE +" real"+")");

        db.execSQL("create table "+Contract.WEATHER_TABLE_NAME +"("+Contract.WEATHER_TABLE_COLUMN_ID +" integer primary key autoincrement,"
                +Contract.WEATHER_TABLE_COLUMN_DATE +" text,"+Contract.WEATHER_TABLE_COLUMN_LATITUDE +" real,"+Contract.WEATHER_TABLE_COLUMN_LONGITUDE +" real,"+Contract.WEATHER_TABLE_COLUMN_WEATHER +" text"+")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
