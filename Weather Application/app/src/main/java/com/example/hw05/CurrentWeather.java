package com.example.hw05;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CurrentWeather#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CurrentWeather extends Fragment {
    private final OkHttpClient client = new OkHttpClient();

    TextView tempData;
    TextView tempMax;
    TextView tempMin;
    TextView description;
    TextView humidity;
    TextView windSpeed;
    TextView windDegree;
    TextView cloudiness;
    TextView cityAndCountry;
    ImageView iconView;
    Button showForeCast;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CurrentWeather() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CurrentWeather.
     */
    // TODO: Rename and change types and number of parameters
    public static CurrentWeather newInstance(String param1, String param2) {
        CurrentWeather fragment = new CurrentWeather();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_current_weather, container, false);
        getActivity().setTitle("Current Weather");
        getCurrentWeather(mParam1, mParam2);

        tempData = view.findViewById(R.id.tempVal);
        tempMax = view.findViewById(R.id.tempValM);
        tempMin = view.findViewById(R.id.tempValMin);
        description = view.findViewById(R.id.descripValue);
        humidity = view.findViewById(R.id.humidVal);
        windSpeed = view.findViewById(R.id.windSpeedVal);
        windDegree = view.findViewById(R.id.windDegreeVal);
        cloudiness = view.findViewById(R.id.cloudVal);
        cityAndCountry = view.findViewById(R.id.cityAndCountry);
        cityAndCountry.setText(mParam1 +","+ mParam2);
        iconView = view.findViewById(R.id.imageView);

        showForeCast = view.findViewById(R.id.checkForecastButton);

        showForeCast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.toWeatherForecast(mParam1, mParam2);
            }
        });

        return view;

    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (CurrentWeatherListener) context;
    }
    CurrentWeatherListener mListener;
    interface CurrentWeatherListener{
        void toWeatherForecast(String city, String country);
    }

    void getCurrentWeather(String city, String country){
        String aCity = city;
        String aCountry = country;
        Log.d("TAG", "getCurrentWeather: " + aCity + aCountry);
        Request request = new Request.Builder()
                .url("https://api.openweathermap.org/data/2.5/weather?q="+aCity+","+aCountry+"&units=imperial&appid=6b31462038a848872bbcbbff851b5443")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Log.d("TAG", "onResponse: " + Thread.currentThread().getId());

                if(response.isSuccessful()){
                    try {
                        ArrayList<Weather> weatherArrayList = new ArrayList<>();
                        JSONObject json =  new JSONObject(response.body().string());
                        JSONObject weatherDataJson = json.getJSONObject("main");
                        JSONObject weatherDataJson2 = json.getJSONObject("wind");
                        JSONObject weatherDataJson3 = json.getJSONObject("clouds");
                        WeatherData weatherData = new WeatherData();
                        weatherData.setTemp(weatherDataJson.getString("temp"));
                        weatherData.setTempMax(weatherDataJson.getString("temp_max"));
                        weatherData.setTempMin(weatherDataJson.getString("temp_min"));
                        weatherData.setHumidity(weatherDataJson.getString("humidity"));
                        weatherData.setWindDegree(weatherDataJson2.getString("deg"));
                        weatherData.setWindSpeed(weatherDataJson2.getString("speed"));
                        weatherData.setCloudiness(weatherDataJson3.getString("all"));

                        JSONArray weatherJson = json.getJSONArray("weather");
                        for(int i=0; i<weatherJson.length(); i ++) {
                            JSONObject weatherJsonObject = weatherJson.getJSONObject(i);
                            Weather weather = new Weather();
                            weather.setId(weatherJsonObject.getString("id"));
                            weather.setMain(weatherJsonObject.getString("main"));
                            weather.setDescription(weatherJsonObject.getString("description"));
                            weather.setIcon(weatherJsonObject.getString("icon"));
                            weatherData.setWeather(new Weather());
                            weatherArrayList.add(weather);

                        }

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                    tempData.setText(weatherData.getTemp() + " F");
                                    tempMax.setText(weatherData.getTempMax() + " F");
                                    tempMin.setText(weatherData.getTempMin() + " F");
                                    description.setText(weatherArrayList.get(0).getDescription());
                                    String icon = weatherArrayList.get(0).getIcon();
                                    Picasso.with(getActivity()).load("https://openweathermap.org/img/wn/"+icon+".png").into(iconView);
                                    humidity.setText(weatherData.getHumidity() + "%");
                                    windSpeed.setText(weatherData.getWindSpeed() + " miles/hr");
                                    windDegree.setText(weatherData.getWindDegree() + " degrees");
                                    cloudiness.setText(weatherData.getCloudiness()+"%");


                            }
                        });

                        Log.d("TAG", "onResponse: " + weatherData);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    ResponseBody responseBody = response.body();
                    String body = responseBody.string();
                    Log.d("TAG", "onResponse: " + body);
                }
            }
        });
    }
}