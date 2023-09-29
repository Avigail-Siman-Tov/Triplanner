package com.triplanner.triplanner.ui.MyTrip;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.squareup.picasso.Picasso;
import com.triplanner.triplanner.Model.Place;
import com.triplanner.triplanner.R;

import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.TravelMode;

import java.util.ArrayList;
import java.util.List;

public class ListTripInDayFragment extends Fragment {

    TextView name,numDay;
    ImageView imagev;
    Place[] arrayPlaces;

    ListView listViewPlaces;
    String destination;
    MyAdapter adapter;

    private MapView mapView;
    private GoogleMap googleMap;

    private static final String API_KEY = "AIzaSyANQj_r2MAGPHMi7JZLlxyT80AysMRV3IA";
    private static final GeoApiContext geoApiContext = new GeoApiContext.Builder().apiKey(API_KEY).build();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_trip_in_day, container, false);
        listViewPlaces =view.findViewById(R.id.fragment_list_trip_in_day_list_view);
        arrayPlaces =  ListTripInDayFragmentArgs.fromBundle(getArguments()).getArrPlaces();
        for(int i=0 ; i<arrayPlaces.length;i++){
            Log.d("mylog","place:"+arrayPlaces[i].getPlaceFormattedAddress());
        }
        destination= ListTripInDayFragmentArgs.fromBundle(getArguments()).getTripDestination();
        numDay = view.findViewById(R.id.fragment_list_trip_in_day_num_day);
        numDay.setText("Day "+String.valueOf(arrayPlaces[0].getDay_in_trip()));
        adapter=new MyAdapter();
        listViewPlaces.setAdapter(adapter);
        listViewPlaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                ListTripInDayFragmentDirections.ActionListTripInDayFragmentToPlaceTravelerDetailsFragment action= ListTripInDayFragmentDirections.actionListTripInDayFragmentToPlaceTravelerDetailsFragment(arrayPlaces[i],destination);
                Navigation.findNavController(view).navigate(action);
            }
        });

        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);


        mapView.getMapAsync(gMap -> {
            googleMap = gMap;
            // Set your preferred map settings here if needed.
            drawRoutes();
        });
        return view;
    }

    private void drawRoutes() {
        // Define your three places (LatLng objects).
        LatLng placeA = new LatLng(arrayPlaces[0].getPlaceLocationLat(), arrayPlaces[0].getPlaceLocationLng()); // Replace with your coordinates
        LatLng placeB = new LatLng(arrayPlaces[1].getPlaceLocationLat(), arrayPlaces[1].getPlaceLocationLng()); // Replace with your coordinates
        LatLng placeC = new LatLng(arrayPlaces[2].getPlaceLocationLat(), arrayPlaces[2].getPlaceLocationLng());   // Replace with your coordinates

        // Add markers for the places on the map.
        googleMap.addMarker(new MarkerOptions().position(placeA).title(arrayPlaces[0].getPlaceName()));
        googleMap.addMarker(new MarkerOptions().position(placeB).title(arrayPlaces[1].getPlaceName()));
        googleMap.addMarker(new MarkerOptions().position(placeC).title(arrayPlaces[2].getPlaceName()));

        // Calculate and display routes between the places.
        calculateAndDisplayRoute(placeA, placeB);
        calculateAndDisplayRoute(placeB, placeC);
//        calculateAndDisplayRoute(placeA, placeC);

        // Zoom to fit all markers
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(placeA);
        builder.include(placeB);
        builder.include(placeC);
        LatLngBounds bounds = builder.build();
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 15));
    }

    private void calculateAndDisplayRoute(LatLng origin, LatLng destination) {
        DirectionsApiRequest request = DirectionsApi.newRequest(geoApiContext)
                .mode(TravelMode.DRIVING)
                .origin(new com.google.maps.model.LatLng(origin.latitude, origin.longitude))
                .destination(new com.google.maps.model.LatLng(destination.latitude, destination.longitude));

        try {
            DirectionsResult result = request.await();
            if (result.routes.length > 0) {
                DirectionsRoute route = result.routes[0];
                List<com.google.maps.model.LatLng> path = route.overviewPolyline.decodePath();

                // Convert the path to a list of LatLng from the Google Maps Android API
                List<LatLng> androidLatLngPath = new ArrayList<>();
                for (com.google.maps.model.LatLng latLng : path) {
                    androidLatLngPath.add(new LatLng(latLng.lat, latLng.lng));
                }

                PolylineOptions polylineOptions = new PolylineOptions()
                        .addAll(androidLatLngPath) // Use the converted list
                        .width(10)
                        .color(0xFF2196F3);

                googleMap.addPolyline(polylineOptions);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
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
