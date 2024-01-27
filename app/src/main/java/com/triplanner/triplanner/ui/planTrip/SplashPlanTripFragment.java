package com.triplanner.triplanner.ui.planTrip;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.triplanner.triplanner.Model.Model;
import com.triplanner.triplanner.Model.PlacePlanning;
import com.triplanner.triplanner.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
public class SplashPlanTripFragment extends Fragment {
    JSONObject jsonDataPage1=null,jsonDataPage2=null,jsonDataPage3=null;
    String placeName;

    String tripPicture;
    Integer tripDaysNumber;
    List<PlacePlanning> myAllPlaces;
    int end=0;
    int m;
    float latitude,longitude;
    String  tripName,tripLocation,tripDestination,tripDateStart,tripDateEnd;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shplash_plan_trip, container, false);
        placeName =SplashPlanTripFragmentArgs.fromBundle(getArguments()).getLocationTrip();
        tripDaysNumber=SplashPlanTripFragmentArgs.fromBundle(getArguments()).getTripDays();
        latitude= SplashPlanTripFragmentArgs.fromBundle(getArguments()).getLatitude();
        longitude = SplashPlanTripFragmentArgs.fromBundle(getArguments()).getLongitude();
        tripName = SplashPlanTripFragmentArgs.fromBundle(getArguments()).getNameTrip();
        tripLocation = SplashPlanTripFragmentArgs.fromBundle(getArguments()).getLocationTrip();
        tripDestination = SplashPlanTripFragmentArgs.fromBundle(getArguments()).getDestinationTrip();
        tripPicture = SplashPlanTripFragmentArgs.fromBundle(getArguments()).getPictureTrip();
        tripDateStart = SplashPlanTripFragmentArgs.fromBundle(getArguments()).getDateStartTrip();
        tripDateEnd = SplashPlanTripFragmentArgs.fromBundle(getArguments()).getDateEndTrip();

        Runnable runnable=new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                searchByTextInGooglePlaceApi(view);
            }


        };
        Handler handler=new Handler();
        handler.postDelayed(runnable,3000);
        return view;
    }
    List<String> list_category;
    public void searchByTextInGooglePlaceApi(View view) {
        Realm.init(getContext()); // context, usually an Activity or Application
        App app = new App(new AppConfiguration.Builder(getString(R.string.AppId)).build());
        User user = app.currentUser();
       Model.instance.getAllFavoriteCategoriesOfTraveler(user.getProfile().getEmail(), getContext(), new Model.GetTravelerFavoriteCategories() {
            @Override
            public void onComplete(List<String> favoriteCategories) {
                list_category =favoriteCategories;
                end=list_category.size()-1;
                myAllPlaces =  new ArrayList<PlacePlanning>();
                for (m = 0; m < list_category.size(); ++m) {

                    Thread placeThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                jsonDataPage1=null;jsonDataPage2=null;jsonDataPage3=null;
                                String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+latitude+"%2C"+longitude+"&radius=30000&type=" + list_category.get(m);

                                //Page 1 in location search - 20 result in this page from google api places
                                OkHttpClient clientPage1 = new OkHttpClient().newBuilder()
                                        .build();
                                String urlPage1 = url + "&key=" + getString(R.string.places_api_key);
                                System.out.println(urlPage1);
                                Request requestPage1 = new Request.Builder()
                                        .url(urlPage1)
                                        .method("GET", null)
                                        .build();
                                String next_page_token_page1 = "";
                                Response responsePage1 = clientPage1.newCall(requestPage1).execute();
                                final String dataPage1 = responsePage1.body().string();
                                jsonDataPage1 = new JSONObject(dataPage1);
                                if (jsonDataPage1.has("next_page_token"))
                                    next_page_token_page1 = jsonDataPage1.getString("next_page_token").toString();
                                //Page 2 in location search - 20 result in this page from google api places
                                if (next_page_token_page1 != null && next_page_token_page1 != "") {
                                    Thread.sleep(2000);
                                    String urlPage2 = url + "&pagetoken=" + next_page_token_page1 + "&key=" + getString(R.string.places_api_key);
                                    Request requestPage2 = new Request.Builder()
                                            .url(urlPage2)
                                            .method("GET", null)
                                            .build();
                                    OkHttpClient clientPage2 = new OkHttpClient().newBuilder().build();
                                    Response responsePage2 = clientPage2.newCall(requestPage2).execute();
                                    final String dataPage2 = responsePage2.body().string();
                                    jsonDataPage2 = new JSONObject(dataPage2);
                                    //Page 2 in location search - 20 result in this page from google api places
                                    if (jsonDataPage2.has("next_page_token")) {
                                        String next_page_token_page2 = "";
                                        next_page_token_page2 = jsonDataPage2.getString("next_page_token");
                                        if (next_page_token_page2 != null && next_page_token_page2 != "") {
                                            Thread.sleep(2000);
                                            String urlPage3 = url + "&pagetoken=" + next_page_token_page2 + "&key=" + getString(R.string.places_api_key);
                                            Request requestPage3 = new Request.Builder()
                                                    .url(urlPage3)
                                                    .method("GET", null)
                                                    .build();
                                            OkHttpClient clientPage3 = new OkHttpClient().newBuilder().build();
                                            Response responsePage3 = clientPage3.newCall(requestPage3).execute();
                                            final String dataPage3 = responsePage3.body().string();
                                            jsonDataPage3 = new JSONObject(dataPage3);
                                        }
                                    }
                                }
                            } catch (IOException | JSONException | InterruptedException exception) {
                            }
                        }
                    });
                    placeThread.start();
                    try {
                        placeThread.join();
                        JSONArray places = null;
                        if (jsonDataPage1 != null) {
                            places = (JSONArray) jsonDataPage1.getJSONArray("results");
                            if (jsonDataPage2 != null) {
                                JSONArray placesPage2 = (JSONArray) jsonDataPage2.getJSONArray("results");
                                for (int i = 0; i < placesPage2.length(); ++i) {
                                    places.put((JSONObject) placesPage2.get(i));
                                }
                                if (jsonDataPage3 != null) {
                                    JSONArray placesPage3 = (JSONArray) jsonDataPage3.getJSONArray("results");
                                    for (int j = 0; j < placesPage3.length(); ++j) {
                                        places.put((JSONObject) placesPage3.get(j));
                                    }
                                }

                            }
                            List<PlacePlanning> myPlaces = PlacesList.JsonArrayToListPlace(places);
                            Collections.sort(myPlaces, new Comparator<PlacePlanning>() {
                                @Override
                                public int compare(PlacePlanning p1, PlacePlanning p2) {
                                    return Float.compare(p2.getPlaceRating(), p1.getPlaceRating());
                                }
                            });
                            myAllPlaces.addAll(myPlaces);
                            if(m== end) {
                                Model.instance.getPlaceFromRecommender(user.getProfile().getEmail(), tripLocation, new Model.GetPlaceRecommenderListener() {
                                    @Override
                                    public void onComplete(List<PlacePlanning> result) {
                                        if(result!=null) {
                                            Log.d("mylog","resulttt"+result);
                                            for(int b=0;b<myAllPlaces.size();++b){
                                                for(int c=0;c<result.size();++c){
                                                    if(result.get(c).getPlaceID().equals(myAllPlaces.get(b).getPlaceID())){
                                                        myAllPlaces.remove(b);
                                                        break;
                                                    }
                                                }
                                            }
                                            Collections.sort(result, new SortByDatePlace());
                                            Collections.sort(myAllPlaces, new SortByDatePlace());
                                            myAllPlaces.addAll(0,result);

                                        }
                                        else {
                                            Collections.sort(myAllPlaces, new SortByDatePlace());
                                        }
                                        PlacePlanning[] arrayPlaces = new PlacePlanning[myAllPlaces.size()];
                                        myAllPlaces.toArray(arrayPlaces);
                                        getParentFragmentManager().popBackStack();
                                        SplashPlanTripFragmentDirections.ActionSplashPlanTripFragmentToPlacesListFragment action = SplashPlanTripFragmentDirections.actionSplashPlanTripFragmentToPlacesListFragment(arrayPlaces,tripDaysNumber,tripName,tripLocation,tripDestination,tripPicture,tripDateStart,tripDateEnd);
                                        Navigation.findNavController(view).navigate(action);
                                    }
                                });

                            }
                        }
                        System.out.println("Finish");
                    } catch (InterruptedException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }
    static class SortByDatePlace implements Comparator<PlacePlanning> {
        @Override
        public int compare(PlacePlanning o1, PlacePlanning o2) {

            return String.valueOf(o1.getPlaceRating()).compareTo(String.valueOf(o2.getPlaceRating()))*-1;
        }
    }
}