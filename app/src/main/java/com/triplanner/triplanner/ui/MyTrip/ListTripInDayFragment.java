package com.triplanner.triplanner.ui.MyTrip;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.triplanner.triplanner.Model.Place;
import com.triplanner.triplanner.R;

public class ListTripInDayFragment extends Fragment {

    TextView name,numDay;
    ImageView imagev;

    Place[] arrayPlaces;
    ListView listViewPlaces;
    String destination;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_trip_in_day, container, false);
        listViewPlaces =view.findViewById(R.id.fragment_list_trip_in_day_list_view);
        arrayPlaces =  ListTripInDayFragmentArgs.fromBundle(getArguments()).getArrPlaces();
        destination= ListTripInDayFragmentArgs.fromBundle(getArguments()).getTripDestination();
        numDay = view.findViewById(R.id.fragment_list_trip_in_day_num_day);
        numDay.setText("Day "+String.valueOf(arrayPlaces[0].getDay_in_trip()));
        return view;
    }

}