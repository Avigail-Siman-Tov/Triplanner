package com.triplanner.triplanner.ui.planTrip;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.triplanner.triplanner.Model.PlacePlanning;
import com.triplanner.triplanner.R;

import java.util.ArrayList;
import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;

public class PlacesListFragment extends Fragment {
    ListView listViewPlaces;
    MyAdapter adapter;
    TextView name;
    ImageView imagev,imageBest;
    RatingBar rating;
    PlacePlanning[] arrayPlaces;
    Button button;
    TextView planBtn;
    Integer tripDays,placesNum=0;
    TextView amountUserPlace,location;
    ProgressDialog myLoadingDialog;
    String tripName,tripLocation;
    User user;
    String packageName;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_places_list, container, false);
        listViewPlaces =view.findViewById(R.id.fragment_places_list_listview);

        arrayPlaces =PlacesListFragmentArgs.fromBundle(getArguments()).getArrayPlaces();
        tripDays=PlacesListFragmentArgs.fromBundle(getArguments()).getTripDays();
        String destination = PlacesListFragmentArgs.fromBundle(getArguments()).getLocationTrip();
        packageName = this.getActivity().getPackageName();
        adapter=new MyAdapter();
        listViewPlaces.setAdapter(adapter);
        listViewPlaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                PlacesListFragmentDirections.ActionPlacesListFragmentToPlaceDetailsFragment action=PlacesListFragmentDirections.actionPlacesListFragmentToPlaceDetailsFragment(arrayPlaces[i]);
                Navigation.findNavController(view).navigate( action);
            }
        });
        Realm.init(getContext()); // context, usually an Activity or Application
        App app = new App(new AppConfiguration.Builder(getString(R.string.AppId)).build());
        user = app.currentUser();
        tripName= PlacesListFragmentArgs.fromBundle(getArguments()).getTripName();
        tripLocation = PlacesListFragmentArgs.fromBundle(getArguments()).getLocationTrip();
        amountUserPlace=view.findViewById(R.id.fragment_places_list_count_place_user);
        amountUserPlace.setText(String.valueOf(placesNum));
        location = view.findViewById(R.id.fragment_places_list_location);
        location.setText(destination);
        planBtn=view.findViewById(R.id.fragment_places_list_planBtn);
        myLoadingDialog=new ProgressDialog(getContext());
        return view;
    }

    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            if (arrayPlaces== null) {
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
                view = inflater.inflate(R.layout.place_list_row, null);
            } else {

            }
            PlacePlanning place=arrayPlaces[i];

            name = view.findViewById(R.id.place_list_row_name_trip);
            imagev = view.findViewById(R.id.place_list_row_image);
            imageBest = view.findViewById(R.id.place_list_row__image_best_place);


            button=view.findViewById(R.id.place_list_row_button);
            name.setText(place.getPlaceName());
            imagev.setTag(place.getPlaceImgUrl());
            rating=view.findViewById(R.id.place_list_row_rating);
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