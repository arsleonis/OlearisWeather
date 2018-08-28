package com.zrz.android.olearisweather;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zrz.android.olearisweather.Retrofit.ApiClient;
import com.zrz.android.olearisweather.Retrofit.ApiInterface;
import com.zrz.android.olearisweather.Retrofit.Weather;
import com.zrz.android.olearisweather.adapters.RvWeatherAdapter;
import com.zrz.android.olearisweather.data.Contract;
import com.zrz.android.olearisweather.data.DBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String COORDINATES="coordinates";
    private static double latitude, longitude;

    ArrayList<String> dates;
    ArrayList<String> currentWeather;
    RecyclerView recyclerViewWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        Intent intent=getIntent();
        double[] coordinates=intent.getDoubleArrayExtra(COORDINATES);
        latitude=coordinates[0];
        longitude=coordinates[1];

        TextView textViewCoordinates=(TextView) findViewById(R.id.tv_weather_coordinates);
        String coordinatesOnTop=getResources().getString(R.string.weather_place)+"\n"+latitude+"\n"+longitude;
        textViewCoordinates.setText(coordinatesOnTop);
        Button buttonCheckWeather=(Button) findViewById(R.id.button_check_weather);
        buttonCheckWeather.setOnClickListener(this);

        recyclerViewWeather=(RecyclerView) findViewById(R.id.rv_weather);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerViewWeather.setLayoutManager(linearLayoutManager);

        dates=new ArrayList<>();
        currentWeather=new ArrayList<>();

        createWeatherHistory();
        createRecyclerView();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.button_check_weather:
                sendRequest();
                break;
            default:
                break;
        }
    }

    public void sendRequest(){
        ApiInterface apiInterface= ApiClient.getApiClient().create(ApiInterface.class);
        String lat=String.valueOf(latitude);
        String lon=String.valueOf(longitude);
        Call<Weather> call=apiInterface.getData(lat, lon, getResources().getString(R.string.api_key));
        call.enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(@NonNull Call<Weather> call, @NonNull Response<Weather> response) {
                if (response.isSuccessful()) {
                    Weather weather = response.body();
                    String newWeather = null;
                    if (weather != null) {
                        newWeather=weather.getCity()+"t= "+weather.getTemp()+"min t= "+weather.getTempMin()+"max t= "+weather.getTempMax()
                                +"humidity= "+weather.getHumidity()+"pressure= "+weather.getPressure();
                    }
                    addWeatherEntry(newWeather);
                    listsCleaning();
                    createWeatherHistory();
                    createRecyclerView();
                }
            }
            @Override
            public void onFailure(@NonNull Call<Weather> call, @NonNull Throwable t) {
                Toast.makeText(WeatherActivity.this,R.string.request_failure, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addWeatherEntry(String newWeather){
        DBHelper dbHelper=new DBHelper(this);
        SQLiteDatabase database=dbHelper.getWritableDatabase();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String simpleDate = simpleDateFormat.format(GregorianCalendar.getInstance().getTime());
        ContentValues contentValues = new ContentValues();
        contentValues.put(Contract.WEATHER_TABLE_COLUMN_DATE, simpleDate);
        contentValues.put(Contract.WEATHER_TABLE_COLUMN_LATITUDE, latitude);
        contentValues.put(Contract.WEATHER_TABLE_COLUMN_LONGITUDE, longitude);
        contentValues.put(Contract.WEATHER_TABLE_COLUMN_WEATHER, newWeather);
        database.insert(Contract.WEATHER_TABLE_NAME, null, contentValues);
        dbHelper.close();
    }

    private void listsCleaning(){
        dates.clear();
        currentWeather.clear();
    }

    private void createWeatherHistory(){
        DBHelper dbHelper=new DBHelper(this);
        SQLiteDatabase database=dbHelper.getReadableDatabase();
        String[]columns={Contract.WEATHER_TABLE_COLUMN_DATE, Contract.WEATHER_TABLE_COLUMN_LATITUDE,
                Contract.WEATHER_TABLE_COLUMN_LONGITUDE, Contract.WEATHER_TABLE_COLUMN_WEATHER};
        Cursor cursor=database.query(Contract.WEATHER_TABLE_NAME, columns, null,null,
                null, null, null);
        if(cursor.moveToFirst()) {
            int columnIndexDate = cursor.getColumnIndex(Contract.WEATHER_TABLE_COLUMN_DATE);
            int columnIndexLat = cursor.getColumnIndex(Contract.WEATHER_TABLE_COLUMN_LATITUDE);
            int columnIndexLng = cursor.getColumnIndex(Contract.WEATHER_TABLE_COLUMN_LONGITUDE);
            int columnIndexWeather = cursor.getColumnIndex(Contract.WEATHER_TABLE_COLUMN_WEATHER);
            String a, d;
            double b, c;
            do {a = cursor.getString(columnIndexDate);
                b = cursor.getDouble(columnIndexLat);
                c = cursor.getDouble(columnIndexLng);
                d = cursor.getString(columnIndexWeather);
                if(b==latitude&&c==longitude){
                    dates.add(a);
                    currentWeather.add(d);
                }
            } while (cursor.moveToNext());
        }
        dbHelper.close();
        cursor.close();
    }

    private void createRecyclerView(){
        RvWeatherAdapter rvWeatherAdapter=new RvWeatherAdapter(this, dates, currentWeather);
        recyclerViewWeather.setAdapter(rvWeatherAdapter);
    }
}
