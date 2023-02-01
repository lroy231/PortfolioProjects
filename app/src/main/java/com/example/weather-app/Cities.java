package com.example.hw05;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class Cities extends Fragment {
    ListView listView;
    ArrayList<Data.City> citiesList = new ArrayList<>();
    ArrayAdapter<Data.City> adapter;
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private ArrayList<Data.City> obj_token;

    public Cities() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cities, container, false);
        listView = view.findViewById(R.id.listView);
        getActivity().setTitle("Cities");
        citiesList.clear();
        citiesList.addAll(Data.cities);
        adapter = new ArrayAdapter<Data.City>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, citiesList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("TAG", "onItemClick: " + position);
               String selected_City = citiesList.get(position).getCity();
               String selected_Country = citiesList.get(position).getCountry();
                Log.d("TAG", "onItemClick: " + selected_City + selected_Country);

                mListener.toCurrentWeatherScreen(selected_City, selected_Country);

            }
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (CitiesListener) context;
    }
    CitiesListener mListener;
    interface CitiesListener{
        void toCurrentWeatherScreen(String city, String country);
     }
}