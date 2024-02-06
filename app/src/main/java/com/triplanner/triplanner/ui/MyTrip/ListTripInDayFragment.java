package com.triplanner.triplanner.ui.MyTrip;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
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
    RatingBar rating;

    private static final String API_KEY = "AIzaSyC-2lRMHs-8VTX-pePcej_Lsb807VOxk8U";
    private static final GeoApiContext geoApiContext = new GeoApiContext.Builder().apiKey(API_KEY).build();
    private List<Polyline> polylines = new ArrayList<>();

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
        // Solve TSP
        Place[] tspSolution = solveTSP(arrayPlaces);
        int cardViewHeight = calculateCardViewHeight(arrayPlaces.length);

        // Copy the elements from tspSolution to arrayPlaces
        for(int i = 0; i < arrayPlaces.length; i++) {
            arrayPlaces[i] = tspSolution[i];
        }

        // Print or use the solution as needed
        for (Place place : tspSolution) {
            Log.d("mylog","the place is:"+place.getPlaceName());
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

        showMap();

        return view;
    }

    private int calculateCardViewHeight(int numberOfTrips) {
        // Define your height values here
        int heightForOneTrip = 200;  // dp
        int heightIncrement = 200;   // dp


        // Calculate the dynamic height based on the number of trips
        return heightForOneTrip + (heightIncrement * (numberOfTrips - 1));

    }

    public static Place[] solveTSP(Place[] arrayPlaces) {
        List<Place> solutionList = new ArrayList<>();
        List<Place> unvisited = new ArrayList<>();

        for (Place place : arrayPlaces) {
            unvisited.add(place);
        }

        // Start from the first place
        Place currentPlace = arrayPlaces[0];
        solutionList.add(currentPlace);
        unvisited.remove(currentPlace);

        while (!unvisited.isEmpty()) {
            Place nearestPlace = findNearestPlace(currentPlace, unvisited);
            solutionList.add(nearestPlace);
            unvisited.remove(nearestPlace);
            currentPlace = nearestPlace;
        }

        // Convert the List to an array
        Place[] solutionArray = new Place[solutionList.size()];
        solutionList.toArray(solutionArray);

        return solutionArray;
    }

    private static Place findNearestPlace(Place from, List<Place> places) {
        double minDistance = Double.MAX_VALUE;
        Place nearestPlace = null;

        for (Place place : places) {
            double distance = calculateDistance(from, place);
            if (distance < minDistance) {
                minDistance = distance;
                nearestPlace = place;
            }
        }

        return nearestPlace;
    }

    private static double calculateDistance(Place place1, Place place2) {
        double dLat = place2.getPlaceLocationLat() - place1.getPlaceLocationLat();
        double dLon = place2.getPlaceLocationLng() - place1.getPlaceLocationLng();
        return Math.sqrt(dLat * dLat + dLon * dLon);
    }

    private void drawRoutes() {

        if (googleMap == null) {
            // Map is not ready yet, postpone the execution
            return;
        }
        // Define and initialize the LatLng array for places.
        LatLng[] placeLatLngs = new LatLng[arrayPlaces.length];

        // Add markers for the places on the map.
        for (int i = 0; i < arrayPlaces.length; i++) {
            placeLatLngs[i] = new LatLng(arrayPlaces[i].getPlaceLocationLat(), arrayPlaces[i].getPlaceLocationLng());
            googleMap.addMarker(new MarkerOptions().position(placeLatLngs[i]).title(arrayPlaces[i].getPlaceName()));
        }

        LatLngBounds bounds;

        // Create a LatLngBounds.Builder to include all points in the routes
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        // Check if there are more than 1 place before calculating/displaying routes
        if (arrayPlaces.length > 1) {
            // Calculate and display routes only between consecutive places
            for (int i = 1; i < arrayPlaces.length; i++) {
                calculateAndDisplayRoute(placeLatLngs[i], placeLatLngs[i-1]);
            }

            // Include all points in the polylines
            for (Polyline polyline : polylines) {
                for (LatLng point : polyline.getPoints()) {
                    builder.include(point);
                }
            }
        } else if (arrayPlaces.length == 1) {
            // If there is only one place, set an appropriate zoom level to display it
            LatLng placeLatLng = new LatLng(arrayPlaces[0].getPlaceLocationLat(), arrayPlaces[0].getPlaceLocationLng());
            builder.include(placeLatLng);
        }

        // Build the bounds
        bounds = builder.build();
        int padding = 50; // Adjust this value as needed
        // Zoom to fit all markers and polylines with padding
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));
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

                Polyline polyline = googleMap.addPolyline(polylineOptions);
                polylines.add(polyline); // Add the polyline to the collection
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        showMap();
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

    public void showMap(){
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                googleMap = map;
                // Enable zoom controls on the map
                googleMap.getUiSettings().setZoomControlsEnabled(true);

                // Disable nested scrolling for the map
                mapView.setNestedScrollingEnabled(false);

                // Set touch gesture for scrolling and zooming the map
                mapView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            v.getParent().requestDisallowInterceptTouchEvent(true);
                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                        }
                        v.onTouchEvent(event);
                        return true;
                    }
                });

                // Initialize your map settings and drawRoutes() here
                drawRoutes();
            }
        });
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

            rating=view.findViewById(R.id.trip_in_day_row_rating);
            rating.setRating(place.getPlaceRating());
            Drawable drawable = rating.getProgressDrawable();
            drawable.setColorFilter(Color.parseColor("#FDC313"), PorterDuff.Mode.SRC_ATOP);
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
