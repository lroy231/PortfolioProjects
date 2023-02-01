package com.example.hw05;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WeatherForecast#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeatherForecast extends Fragment {
    TextView cAndC;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    ArrayList<ForeCast> weatherForecastList = new ArrayList<>();
    ForecastRecyclerAdapter adapter;

    private final OkHttpClient client = new OkHttpClient();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WeatherForecast() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WeatherForecast.
     */
    // TODO: Rename and change types and number of parameters
    public static WeatherForecast newInstance(String param1, String param2) {
        WeatherForecast fragment = new WeatherForecast();
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
        View view = inflater.inflate(R.layout.fragment_weather_forecast, container, false);
        cAndC = view.findViewById(R.id.cAndC);
        cAndC.setText(mParam1+","+mParam2);
        getActivity().setTitle("Weather Forecast");
        getForeCast(mParam1, mParam2);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());




        return view;
    }

    void getForeCast(String city, String country){
        String aCity = city;
        String aCountry = country;
        Request request = new Request.Builder()
                .url("https://api.openweathermap.org/data/2.5/forecast?q="+aCity+","+aCountry+"&units=imperial&appid=6b31462038a848872bbcbbff851b5443")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                if(response.isSuccessful()){
                    try {
                        ArrayList<ForeCast> foreCastsArrayList = new ArrayList<>();
                        ArrayList<Weather> weatherArrayList = new ArrayList<>();
                        JSONObject json =  new JSONObject(response.body().string());
                        JSONArray list = json.getJSONArray("list");
                        for(int i=0; i<list.length(); i++) {
                            JSONObject jsonObject = list.getJSONObject(i);
                            JSONObject jsonMain = jsonObject.getJSONObject("main");
                            ForeCast foreCast = new ForeCast();
                            foreCast.setTemp(jsonMain.getString("temp"));
                            foreCast.setTempMin(jsonMain.getString("temp_min"));
                            foreCast.setTempMax(jsonMain.getString("temp_max"));
                            foreCast.setHumid(jsonMain.getString("humidity"));
                            foreCast.setDate(jsonObject.getString("dt_txt"));
                            foreCastsArrayList.add(foreCast);

                            JSONArray weatherArray = jsonObject.getJSONArray("weather");
                            for (int j = 0; j<weatherArray.length(); j++){
                                JSONObject jsonObject1 = weatherArray.getJSONObject(j);
                                Weather weather = new Weather();
                                weather.setId(jsonObject1.getString("id"));
                                weather.setMain(jsonObject1.getString("main"));
                                weather.setDescription(jsonObject1.getString("description"));
                                weather.setIcon(jsonObject1.getString("icon"));
                                foreCast.setWeather(weather);
                                weatherArrayList.add(weather);

                            }
                        }
                        Log.d("TAG", "onResponse: " + foreCastsArrayList.get(0).getTemp());
                        Log.d("TAG", "onResponseTOO: " + weatherArrayList.get(0).getDescription());
                        Log.d("TAG", "onResponseTOD: " + foreCastsArrayList.get(0).getDate());

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                recyclerView.setLayoutManager(layoutManager);
                                adapter = new ForecastRecyclerAdapter(foreCastsArrayList, getActivity());
                                recyclerView.setAdapter(adapter);
                                Log.d("AG", "onCreateView: " + foreCastsArrayList.size());
                            }
                        });

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