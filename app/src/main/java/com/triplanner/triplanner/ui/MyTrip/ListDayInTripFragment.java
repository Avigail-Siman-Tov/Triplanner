package com.triplanner.triplanner.ui.MyTrip;

import static io.realm.Realm.getApplicationContext;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.triplanner.triplanner.R;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.triplanner.triplanner.Model.Place;

import java.util.ArrayList;
import java.util.NoSuchElementException;


public class ListDayInTripFragment extends Fragment {
    Place[] arrayPlaces;
    Integer tripDays;
    ListView listViewPlaces;
    ImageView imageTrip;

    MyAdapter adapter;
    int [] arrDays;
    TextView numDay,nameTrip,locationTrip;
    String location, tripPicture;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_day_in_trip, container, false);
        arrayPlaces = ListDayInTripFragmentArgs.fromBundle(getArguments()).getArrPlaces();
        tripDays = ListDayInTripFragmentArgs.fromBundle(getArguments()).getTripDays();
        String name = ListDayInTripFragmentArgs.fromBundle(getArguments()).getTripName();
        location = ListDayInTripFragmentArgs.fromBundle(getArguments()).getTripLocation();
        tripPicture = ListDayInTripFragmentArgs.fromBundle(getArguments()).getTripPicture();
        arrDays = new int[tripDays];
        nameTrip = view.findViewById(R.id.fragment_list_day_in_trip_name);
        String temp = "Schedule of trip \"";
        temp+=name;
        temp+= "\"";
        nameTrip.setText(temp);
        locationTrip = view.findViewById(R.id.fragment_list_day_in_trip_location);
        locationTrip.setText(location);
        listViewPlaces =view.findViewById(R.id.fragment_list_day_in_trip_list_view);
        // Find the ImageView by ID

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


// Load the image using Picasso
//

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

    // Function to clip the bitmap with the wave shape



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

