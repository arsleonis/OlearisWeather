package com.zrz.android.olearisweather.Retrofit;

import com.google.gson.annotations.SerializedName;

public class Weather {

    public class Temperature {
        Double temp;
        Double humidity;
        Double pressure;
        Double temp_min;
        Double temp_max;
    }
    @SerializedName("name")
    private String city;

    @SerializedName("main")
    private Temperature temperature;

    public String getCity() { return city+"\n"; }

    public String getTemp() { return String.valueOf(temperature.temp)+"\n"; }

    public String getTempMin() { return String.valueOf(temperature.temp_min)+"\n"; }

    public String getTempMax() { return String.valueOf(temperature.temp_max)+"\n"; }

    public String getHumidity() { return String.valueOf(temperature.humidity)+"\n"; }

    public String getPressure() { return String.valueOf(temperature.pressure); }
}
