package com.triplanner.triplanner.ui.MyTrip;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.triplanner.triplanner.R;

import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.triplanner.triplanner.Model.Place;

import java.util.ArrayList;


public class ListDayInTripFragment extends Fragment {
    Place[] arrayPlaces;
    Integer tripDays;
    ListView listViewPlaces;
    MyAdapter adapter;
    int [] arrDays;
    TextView numDay,nameTrip,locationTrip;
    String location;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_day_in_trip, container, false);
        arrayPlaces = ListDayInTripFragmentArgs.fromBundle(getArguments()).getArrPlaces();
        tripDays=  ListDayInTripFragmentArgs.fromBundle(getArguments()).getTripDays();
        String name= ListDayInTripFragmentArgs.fromBundle(getArguments()).getTripName();
        location = ListDayInTripFragmentArgs.fromBundle(getArguments()).getTripLocation();
        arrDays = new int[tripDays];
        nameTrip = view.findViewById(R.id.fragment_list_day_in_trip_name);
        String temp = "Schedule of trip \"";
        temp+=name;
        temp+= "\"";
        nameTrip.setText(temp);
        locationTrip = view.findViewById(R.id.fragment_list_day_in_trip_location);
        locationTrip.setText(location);
        listViewPlaces =view.findViewById(R.id.fragment_list_day_in_trip_list_view);
        ArrayList<ArrayList<Place>> array_place_days_planing = new ArrayList<>();
        for(int k=0; k<tripDays;++k){
            array_place_days_planing.add(new ArrayList<>());
        }
        for (int j=0; j<arrayPlaces.length;++j){
            array_place_days_planing.get(arrayPlaces[j].getDay_in_trip()-1).add(arrayPlaces[j]);
        }
        adapter=new MyAdapter();
        listViewPlaces.setAdapter(adapter);
        listViewPlaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                Place[] newArrayPlaces = new Place[array_place_days_planing.get(i).size()];
                array_place_days_planing.get(i).toArray(newArrayPlaces);
                ListDayInTripFragmentDirections.ActionListDayInTripFragmentToListTripInDayFragment action =  ListDayInTripFragmentDirections.actionListDayInTripFragmentToListTripInDayFragment(newArrayPlaces,location);
                Navigation.findNavController(view).navigate(action);
            }
        });
        return view;
    }


    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            if (arrDays== null) {
                return 0;
            } else {
                return arrDays.length;
            }
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                LayoutInflater inflater = getLayoutInflater();
                view = inflater.inflate(R.layout.day_list_row, null);
            } else {

            }
            numDay = view.findViewById(R.id.day_list_row_textview);
            numDay.setText("Day "+(i+1));
            return view;
        }


    }


}

