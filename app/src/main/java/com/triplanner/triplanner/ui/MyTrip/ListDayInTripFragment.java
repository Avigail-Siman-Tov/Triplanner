package com.triplanner.triplanner.ui.MyTrip;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.triplanner.triplanner.R;

import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.triplanner.triplanner.Model.Place;


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

