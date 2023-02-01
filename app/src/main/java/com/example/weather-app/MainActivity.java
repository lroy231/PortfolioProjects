package com.example.hw05;
//HW 05
//LEROY PHOMMA AND BINLY KEONAKHONE
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
//Implement multiple interfaces so it gains access to the methods to pass data from main activity to the next fragment
public class MainActivity extends AppCompatActivity implements Cities.CitiesListener, CurrentWeather.CurrentWeatherListener {
    private final OkHttpClient client = new OkHttpClient();
    final String TAG = "demo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.containerView, new Cities())
                .commit();

    }



    void getCurrentWeather(){
        Request request = new Request.Builder()
                .url("https://api.openweathermap.org/data/2.5/weather?q=Charlotte,US&appid=6b31462038a848872bbcbbff851b5443")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Log.d(TAG, "onResponse: " + Thread.currentThread().getId());

                ResponseBody responseBody = response.body();
                String body = responseBody.string();
                Log.d(TAG, "onResponse: " + body);
            }
        });
    }
    String mCity;
    String mCountry;
    @Override
    public void toCurrentWeatherScreen(String city, String country) {
    this.mCity = city;
    this.mCountry = country;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.containerView, CurrentWeather.newInstance(mCity, mCountry))
                .commit();
    }

    @Override
    public void toWeatherForecast(String city, String country) {
        this.mCity = city;
        this.mCountry = country;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.containerView, WeatherForecast.newInstance(mCity, mCountry))
                .commit();

    }
}