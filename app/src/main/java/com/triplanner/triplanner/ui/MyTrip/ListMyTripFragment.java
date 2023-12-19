package com.triplanner.triplanner.ui.MyTrip;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.squareup.picasso.Picasso;
import com.triplanner.triplanner.Model.Model;
import com.triplanner.triplanner.Model.Place;
import com.triplanner.triplanner.Model.Trip;
import com.triplanner.triplanner.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;

import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;


public class ListMyTripFragment extends Fragment {

    Trip[] arrayTrip;
    TextView name,destination,numDays,date;
    ListView listViewTrip;
    MyAdapter adapter;
    User user;
    ImageView image;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the Places API client
    }
    private LinearLayout cardView; // Replace with your actual CardView ID
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragmen
        View view=inflater.inflate(R.layout.fragment_list_my_trip, container, false);
        Realm.init(getContext()); // context, usually an Activity or Application
        App app = new App(new AppConfiguration.Builder(getString(R.string.AppId)).build());
        user = app.currentUser();


        Model.instance.getAllTrip(user.getProfile().getEmail(), getContext(), new Model.GetAllTripListener() {
            @Override
            public void onComplete(Trip[] trips) {
                arrayTrip=trips;
                listViewTrip = view.findViewById(R.id.fragment_list_my_trip_list);
                adapter=new MyAdapter();
                listViewTrip.setAdapter(adapter);
                listViewTrip.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                        Model.instance.getAllPlacesOfTrip(arrayTrip[i].getId_trip(), getContext(), new Model.GetAllPlacesOfTrip() {
                            @Override
                            public void onComplete(Place[] places) {
                                ListMyTripFragmentDirections.ActionNavMyTripToListDayInTripFragment action=ListMyTripFragmentDirections.actionNavMyTripToListDayInTripFragment(arrayTrip[i].getTripName(),arrayTrip[i].getTripDestination(),arrayTrip[i].getTripDaysNumber(),arrayTrip[i].getTripPicture(),places);
                                Navigation.findNavController(view).navigate( action);
                            }
                        });

                    }
                });
            }
        });
        return view;
    }

    class MyAdapter extends BaseAdapter {

        public MyAdapter(){

        }
        @Override
        public int getCount() {
            if (arrayTrip== null) {
                return 0;
            } else {
                return arrayTrip.length;
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
                view = inflater.inflate(R.layout.my_plan_row, null);
            } else {

            }
            Trip trip = arrayTrip[i];


            name = view.findViewById(R.id.my_plan_row_name);
            name.setText(trip.getTripName());
            destination= view.findViewById(R.id.my_plan_row_destination);
            destination.setText(trip.getTripDestination());
            numDays = view.findViewById(R.id.my_plan_row_num_days);
            numDays.setText(String.valueOf(trip.getTripDaysNumber()));
//            date=view.findViewById(R.id.my_plan_row_date);
//            date.setText(trip.getDate());
            image = view.findViewById(R.id.imageView4);
            if (trip.getTripPicture() != null) {
                Picasso.get().load(trip.getTripPicture()).into(image);
            }

            return view;
        }

    }
}

