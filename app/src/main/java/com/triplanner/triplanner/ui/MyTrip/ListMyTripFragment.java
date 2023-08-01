package com.triplanner.triplanner.ui.MyTrip;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.triplanner.triplanner.Model.Model;
import com.triplanner.triplanner.Model.Place;
import com.triplanner.triplanner.Model.Trip;
import com.triplanner.triplanner.R;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;

public class ListMyTripFragment extends Fragment {

    Trip[] arrayTrip;
    TextView name,destination,numDays,date;
    ListView listViewTrip;
    MyAdapter adapter;
    User user;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_list_my_trip, container, false);
        Realm.init(getContext()); // context, usually an Activity or Application
        App app = new App(new AppConfiguration.Builder(getString(R.string.AppId)).build());
        user = app.currentUser();

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
            Trip trip =arrayTrip[i];

            name = view.findViewById(R.id.my_plan_row_name);
            name.setText(trip.getTripName());
            destination= view.findViewById(R.id.my_plan_row_destination);
            destination.setText(trip.getTripDestination());
            numDays = view.findViewById(R.id.my_plan_row_num_days);
            numDays.setText(String.valueOf(trip.getTripDaysNumber()));
            date=view.findViewById(R.id.my_plan_row_date);
            date.setText(trip.getDate());

            return view;
        }

    }
}

