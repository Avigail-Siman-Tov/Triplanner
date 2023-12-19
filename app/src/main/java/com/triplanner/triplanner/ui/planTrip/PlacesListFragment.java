package com.triplanner.triplanner.ui.planTrip;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
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
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.squareup.picasso.Picasso;
import com.triplanner.triplanner.Model.Model;
import com.triplanner.triplanner.Model.Place;
import com.triplanner.triplanner.Model.PlacePlanning;
import com.triplanner.triplanner.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;

import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.Request;

import android.os.Build;

public class PlacesListFragment extends Fragment {
    ListView listViewPlaces;
    MyAdapter adapter;
    TextView name;
    ImageView imagev,imageBest;;
    RatingBar rating;
    PlacePlanning[] arrayPlaces;
    ArrayList<PlacePlanning> chosenPlaces;
    Button button;
    TextView planBtn;
    Integer tripDays,placesNum=0;
    TextView amountUserPlace,location;
    ProgressDialog myLoadingDialog;
    String tripName,tripLocation ,tripPicture ,tripDestination;
    int[] colorArray;
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
        tripDestination = PlacesListFragmentArgs.fromBundle(getArguments()).getDestinationTrip();

        Log.d("mylog","999" +tripDestination);

        chosenNumber(arrayPlaces);
        amountUserPlace=view.findViewById(R.id.fragment_places_list_count_place_user);
        amountUserPlace.setText(String.valueOf(placesNum));
        location = view.findViewById(R.id.fragment_places_list_location);
        location.setText(destination);
        planBtn=view.findViewById(R.id.fragment_places_list_planBtn);
        myLoadingDialog=new ProgressDialog(getContext());
        planBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                planBtn.setBackgroundResource(R.drawable.button_hover);

                if(placesNum==0)
                    Toast.makeText(getContext(),"no places selected ", Toast.LENGTH_SHORT).show();
                else if (placesNum<tripDays)
                    Toast.makeText(getContext(),"Too few places selected ",Toast.LENGTH_SHORT).show();
                else if (placesNum > tripDays*3)
                    Toast.makeText(getContext(),"Too many places selected ",Toast.LENGTH_SHORT).show();
                else
                    CreateListForPlanning();
            }
        });
        colorArray= getContext().getResources().getIntArray(R.array.array_name);
        return view;
    }

    private class GoogleImageTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String query = params[0];
            StringBuilder result = new StringBuilder();
            HttpURLConnection connection = null;

            try {
                Log.d("mylog","I am");
                String apiKey = getString(R.string.places_api_key);
                String cx = "f4af544f1e97c4189";
//                String searchUrl = "https://www.googleapis.com/customsearch/v1?q=" + query +
//                        "&cx=" + cx + "&key=" + apiKey + "&searchType=image";
                String searchUrl = Uri.parse("https://www.googleapis.com/customsearch/v1")
                        .buildUpon()
                        .appendQueryParameter("q", tripDestination + " city")
                        .appendQueryParameter("cx", cx)
                        .appendQueryParameter("key", apiKey)
                        .appendQueryParameter("searchType", "image")
                        .build()
                        .toString();

                URL url = new URL(searchUrl);
                connection = (HttpURLConnection) url.openConnection();

                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                if (reader != null)
                    reader.close();
                if (inputStream != null)
                    inputStream.close();
            } catch (IOException e) {
                Log.d("mylog","oppp");
                e.printStackTrace();
                return null;
            }
            finally {
                Log.d("mylog","11"+connection);
                // Close the connection in the finally block to ensure it is always done
                if (connection != null) {
                        connection.disconnect();

                }
            }

            return result.toString();
        }
    }





    private void CreateListForPlanning() {
        planBtn.setEnabled(false);
        myLoadingDialog.setTitle("Planing Trip");
        myLoadingDialog.setMessage("Please Wait, planing your trip");
        myLoadingDialog.setCanceledOnTouchOutside(false);
        myLoadingDialog.show();
        chosenPlaces=new ArrayList<PlacePlanning>();
        for(int i=0;i<arrayPlaces.length;i++)
        {
            if(arrayPlaces[i].getStatus()==true) {
                PlacePlanning place = getPlaceDetailsById(arrayPlaces[i].getPlaceID());
                chosenPlaces.add(place);
            }
        }

        String query = tripDestination; // Replace with the actual destination
        new GoogleImageTask().execute(query);

        Model.instance.planTrip(chosenPlaces, tripDays, new Model.PlanTripListener() {
            @Override
            public void onComplete(ArrayList<PlacePlanning> chosenPlaces1) {

                Model.instance.addTrip(tripName, tripLocation, user.getProfile().getEmail(), tripDays,tripPicture,getContext(), new Model.AddTripListener() {
                    @Override
                    public void onComplete(String  tripId) {
                     addPlaces(chosenPlaces1,0,tripId);
                    }
                });
            }
        });


    }
    public void addPlaces(ArrayList<PlacePlanning> chosenPlaces,int index,String tripId){
        if(index==chosenPlaces.size()) {
            Model.instance.getAllPlacesOfTrip(tripId, getContext(), new Model.GetAllPlacesOfTrip() {
                @Override
                public void onComplete(Place[] places) {
                    myLoadingDialog.dismiss();
                    PlacesListFragmentDirections.ActionPlacesListFragmentToListDayInTripFragment action=PlacesListFragmentDirections.actionPlacesListFragmentToListDayInTripFragment(tripName,tripLocation,tripDays,tripPicture,places );
                    Navigation.findNavController(getView()).navigate( action);
                }
            });

        }
        else {
            Model.instance.addPlace(chosenPlaces.get(index), tripLocation, user.getProfile().getEmail(), tripId, getContext(),new Model.AddPlaceListener() {
                @Override
                public void onComplete(boolean isSuccess) {
                    addPlaces(chosenPlaces, index + 1, tripId);
                }

            });
        }
    }
    private void chosenNumber(PlacePlanning[] arrayPlaces) {
        placesNum=0;
        for(int i=0; i<arrayPlaces.length;++i)
            if(arrayPlaces[i].getStatus()==true)
                placesNum++;
    }
    int count;
    int j;


    JSONObject jsonData=null;
    String jsonStringPlace;
    private  PlacePlanning getPlaceDetailsById(String placeId) {
        PlacePlanning p=new PlacePlanning();
        Thread placeThread=new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    String url="https://maps.googleapis.com/maps/api/place/details/json?place_id=";
                    url+=placeId+"&key="+getString(R.string.places_api_key);
                    OkHttpClient client = new OkHttpClient().newBuilder()
                            .build();
                    Request request = new Request.Builder()
                            .url(url)
                            .method("GET", null)
                            .build();
                    Response response = client.newCall(request).execute();
                    jsonStringPlace= response.body().string();
                    jsonData = new JSONObject(jsonStringPlace);
                }catch (IOException | JSONException f){
                }
            }
        });
        placeThread.start();
        try {
            placeThread.join();
            if(jsonData!=null)
            {
                JSONObject result=(JSONObject)jsonData.get("result");
                p=PlacesList.JsonToPlace(result);
            }
        } catch (InterruptedException | JSONException e) {
            e.printStackTrace();
        }
        return p;
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
            imageBest.setTag(place.isRecommended());
            if(place.isRecommended()!=null){
                if(place.isRecommended().equals("1") && imageBest.getTag().equals("1")){
                    imageBest.setVisibility(View.VISIBLE);
                    String uri = "@drawable/icons_best_seller";  // where myresource (without the extension) is the file

                    int imageResource = getResources().getIdentifier(uri, null,packageName);
                    Drawable res = getResources().getDrawable(imageResource);
                    imageBest.setImageDrawable(res);
                }

                else{
                    imageBest.setVisibility(View.GONE);
                }
            }
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!place.getStatus()){
                        place.setStatus(true);
                        placesNum++;
                        arrayPlaces[i].setStatus(true);
                        amountUserPlace.setText(String.valueOf(placesNum));

                    }
                    else{
                        place.setStatus(false);
                        placesNum--;
                        arrayPlaces[i].setStatus(false);
                        amountUserPlace.setText(String.valueOf(placesNum));
                    }
                    notifyDataSetChanged();
                }
            });

            button.setTag(place.getStatus());
            if(!place.getStatus() && String.valueOf(button.getTag()).equals("false")){
                button.setText("Add");
                button.setBackgroundColor(colorArray[1]);
            }
            else{
                button.setText("Remove");
                button.setBackgroundColor(colorArray[0]);
            }
            return view;
        }

    }

}