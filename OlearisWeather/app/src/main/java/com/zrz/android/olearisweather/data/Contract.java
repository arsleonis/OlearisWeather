package com.zrz.android.olearisweather.data;

import android.provider.BaseColumns;

public final class Contract implements BaseColumns{

    public static final String PLACES_TABLE_NAME ="Places";
    public static final String PLACES_TABLE_COLUMN_ID =BaseColumns._ID;
    public static final String PLACES_TABLE_COLUMN_LATITUDE ="latitude";
    public static final String PLACES_TABLE_COLUMN_LONGITUDE ="longitude";

    public static final String WEATHER_TABLE_NAME ="Weather";
    public static final String WEATHER_TABLE_COLUMN_ID =BaseColumns._ID;
    public static final String WEATHER_TABLE_COLUMN_DATE ="date";
    public static final String WEATHER_TABLE_COLUMN_LATITUDE ="latitude";
    public static final String WEATHER_TABLE_COLUMN_LONGITUDE ="longitude";
    public static final String WEATHER_TABLE_COLUMN_WEATHER ="weather";
}
