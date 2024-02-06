
package com.triplanner.triplanner.ui.MyTrip;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.triplanner.triplanner.Model.Place;
import com.triplanner.triplanner.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ListDayInTripFragment extends Fragment {
    Place[] arrayPlaces;
    Integer tripDays;
    ListView listViewPlaces;
    MyAdapter adapter;
    int[] arrDays;

    TextView numDay, nameTrip, locationTrip,dateStart;
    String location, dateTrip,tripPicture;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_day_in_trip, container, false);
        arrayPlaces = ListDayInTripFragmentArgs.fromBundle(getArguments()).getArrPlaces();
        tripDays = ListDayInTripFragmentArgs.fromBundle(getArguments()).getTripDays();
        String name = ListDayInTripFragmentArgs.fromBundle(getArguments()).getTripName();
        location = ListDayInTripFragmentArgs.fromBundle(getArguments()).getTripLocation();
        dateTrip = ListDayInTripFragmentArgs.fromBundle(getArguments()).getTripDateStart();
        tripPicture = ListDayInTripFragmentArgs.fromBundle(getArguments()).getTripPicture();

        arrDays = new int[tripDays];
        nameTrip = view.findViewById(R.id.fragment_list_day_in_trip_name);

        String temp = name;
        nameTrip.setText(temp);
        locationTrip = view.findViewById(R.id.fragment_list_day_in_trip_location);
        dateStart = view.findViewById(R.id.fragment_list_day_in_trip_date);

        // Set the image resource if tripPicture is a resource ID
        // Find the ImageView by ID
        // Find the wave mask ImageView by ID
// Find the ImageView by ID
        ImageView imageView = view.findViewById(R.id.imageView);

// Load the original image using Picasso or other methods
        Picasso.get().load(tripPicture).fit().centerCrop().into(imageView);

// Find the wave mask ImageView by ID
        ImageView waveMaskImageView = view.findViewById(R.id.waveMask);

// Set the image resource if tripPicture is a resource ID
        waveMaskImageView.setImageResource(R.drawable.circle_shape);
        locationTrip.setText(location);
        String formattedDate = convertDateFormat(dateTrip);
        dateStart.setText(formattedDate +" • "+ tripDays +" days");
        listViewPlaces = view.findViewById(R.id.fragment_list_day_in_trip_list_view);
        ArrayList<ArrayList<Place>> array_place_days_planing = new ArrayList<>();
        for (int k = 0; k < tripDays; ++k) {
            array_place_days_planing.add(new ArrayList<>());
        }
        for (int j = 0; j < arrayPlaces.length; ++j) {
            array_place_days_planing.get(arrayPlaces[j].getDay_in_trip() - 1).add(arrayPlaces[j]);
        }
        adapter = new MyAdapter();
        listViewPlaces.setAdapter(adapter);
        listViewPlaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                Place[] newArrayPlaces = new Place[array_place_days_planing.get(i).size()];
                array_place_days_planing.get(i).toArray(newArrayPlaces);
                ListDayInTripFragmentDirections.ActionListDayInTripFragmentToListTripInDayFragment action =
                        ListDayInTripFragmentDirections.actionListDayInTripFragmentToListTripInDayFragment(newArrayPlaces, location);
                Navigation.findNavController(view).navigate(action);
            }
        });
        return view;
    }

    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            if (arrDays == null) {
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
            }
            numDay = view.findViewById(R.id.day_list_row_textview);
            TextView dateTextView = view.findViewById(R.id.day_list_row_date);

            numDay.setText("Day " + (i + 1));
            if (i < arrDays.length) {
                dateTextView.setText(formatDate(i));
            } else {
                dateTextView.setText("N/A");
            }
            return view;
        }
    }
    public static String convertDateFormat(String inputDate) {
        try {
            // Input date format
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

            // Parse the input date
            Date date = inputFormat.parse(inputDate);

            // Output date format with four-digit year
            SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());

            // Format the date in the desired output format
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null; // Handle parsing exception
        }
    }

    private String formatDate(int day) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat outputFormat = new SimpleDateFormat("EEE \n dd");

        try {
            Date startDate = inputFormat.parse(dateTrip);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            calendar.add(Calendar.DAY_OF_MONTH, day);

            return outputFormat.format(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("mylog", "Error formatting date: " + e.getMessage());
        }
        return "N/A";
    }

}
