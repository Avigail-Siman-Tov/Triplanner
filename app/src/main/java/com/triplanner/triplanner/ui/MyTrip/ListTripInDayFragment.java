package com.triplanner.triplanner.ui.MyTrip;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.triplanner.triplanner.Model.Place;
import com.triplanner.triplanner.R;

public class ListTripInDayFragment extends Fragment {

    TextView name,numDay;
    ImageView imagev;
    Place[] arrayPlaces;

    ListView listViewPlaces;
    String destination;
    MyAdapter adapter;
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
        adapter=new MyAdapter();
        listViewPlaces.setAdapter(adapter);
        listViewPlaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                ListTripInDayFragmentDirections.ActionListTripInDayFragmentToPlaceTravelerDetailsFragment action= ListTripInDayFragmentDirections.actionListTripInDayFragmentToPlaceTravelerDetailsFragment(arrayPlaces[i],destination);
                Navigation.findNavController(view).navigate( action);
            }
        });
        return view;
    }


    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            if (arrayPlaces == null) {
                return 0;
            } else {
                return arrayPlaces.length;
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
                view = inflater.inflate(R.layout.trip_in_day_row, null);
            } else {

            }
            Place place=arrayPlaces[i];

            name = view.findViewById(R.id.trip_in_day_row_name_trip);
            imagev = view.findViewById(R.id.trip_in_day_row_image);
            name.setText(place.getPlaceName());
            imagev.setTag(place.getPlaceImgUrl());

            if (place.getPlaceImgUrl() != null && !place.getPlaceImgUrl().equals("")) {
                if (place.getPlaceImgUrl() == imagev.getTag()) {
                    Picasso.get().load(place.getPlaceImgUrl()).into(imagev);
                }
            } else {

            }

            return view;
        }

    }

}