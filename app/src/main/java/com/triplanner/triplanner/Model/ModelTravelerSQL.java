package com.triplanner.triplanner.Model;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ModelTravelerSQL {
    public List<Traveler> getTravelerByEmail(String travelerMail,Context context) {
        return AppLocalDB.getDatabase(context).travelerDao().getTravelerByEmail(travelerMail);
    }
    public List<Traveler> getAllTraveler(Context context){
        return AppLocalDB.getDatabase(context).travelerDao().getAllTraveler();
    }
    public void getAllFavoriteCategoriesOfTraveler(String travelerMail,Context context,final Model.GetTravelerFavoriteCategories listener){

        class MyAsynchTask extends AsyncTask {
            List<String> listF;
            @Override
            protected Object doInBackground(Object[] objects) {
                listF = AppLocalDB.getDatabase(context).favoriteCategoriesDao().getAllFavoriteCategoriesOfTraveler(travelerMail);

                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (listener != null) {
                    listener.onComplete(listF);
                }
            }
        }
        MyAsynchTask task = new MyAsynchTask();
        task.execute();

    }
    public  void addTraveler(Traveler traveler,List<FavoriteCategories> favoriteCategories ,Context context,final Model.AddTravelerListener listener) {
        class MyAsynchTask extends AsyncTask {
            @Override
            protected Object doInBackground(Object[] objects) {
                AppLocalDB.getDatabase(context).travelerDao().insertAll(traveler);
                for (int i=0;i<favoriteCategories.size();++i){
                    AppLocalDB.getDatabase(context).favoriteCategoriesDao().insertAll(favoriteCategories.get(i));
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (listener != null) {
                    listener.onComplete("true");
                }
            }
        }
        MyAsynchTask task = new MyAsynchTask();
        task.execute();

    }

    public void addTrip(Trip trip,Context context,Model.AddTripListener listener ){
        class MyAsynchTask extends AsyncTask {
            @Override
            protected Object doInBackground(Object[] objects) {
                AppLocalDB.getDatabase(context).tripDao().insertAll(trip);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (listener != null) {
                    listener.onComplete("true");
                }
            }
        }
        MyAsynchTask task = new MyAsynchTask();
        task.execute();
    }

    public  void editTraveler(Traveler traveler,List<FavoriteCategories> favoriteCategories ,Context context,final Model.AddTravelerListener listener) {
        class MyAsynchTask extends AsyncTask {
            @Override
            protected Object doInBackground(Object[] objects) {
                Traveler traveler1= AppLocalDB.getDatabase(context).travelerDao().getTravelerByEmail(traveler.getTravelerMail()).get(0);
                List<FavoriteCategories> listFavoriteCategories = AppLocalDB.getDatabase(context).favoriteCategoriesDao().getAllFavoriteCategories(traveler.getTravelerMail());
                AppLocalDB.getDatabase(context).travelerDao().deleteTraveler(traveler1);
                for(int i=0;i<listFavoriteCategories.size();++i){
                    AppLocalDB.getDatabase(context).favoriteCategoriesDao().deleteFavoriteCategories(listFavoriteCategories.get(i));
                }

                AppLocalDB.getDatabase(context).travelerDao().insertAll(traveler);
                for (int i=0;i<favoriteCategories.size();++i){
                    AppLocalDB.getDatabase(context).favoriteCategoriesDao().insertAll(favoriteCategories.get(i));
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (listener != null) {
                    listener.onComplete("true");
                }
            }
        }
        MyAsynchTask task = new MyAsynchTask();
        task.execute();

    }
    public void getTravelerByMail(final String travelerMail, Context context, final Model.GetTravelerByEmailListener listener) {
        class MyAsynchTask extends AsyncTask<String,String,String> {
            Traveler traveler;
            List<String>favoriteCategories;
            //will execute on the new thread
            @Override
            protected String doInBackground(String... strings) {
                favoriteCategories= AppLocalDB.getDatabase(context).favoriteCategoriesDao().getAllFavoriteCategoriesOfTraveler(travelerMail);
                List<Traveler>temp = AppLocalDB.getDatabase(context).travelerDao().getTravelerByEmail(travelerMail);
                if(temp.size()>0)
                    traveler=temp.get(0);

                return null;
            }

            //will execute on the main thread
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                listener.onComplete(traveler,favoriteCategories);
            }
        }
        MyAsynchTask task = new MyAsynchTask();
        task.execute();
    }
    public static void deleteTraveler(String travelerMail, Context context, final Model.DeleteTravelerListener listener) {

        class MyAsynchTask extends AsyncTask {
            @Override
            protected Object doInBackground(Object[] objects) {
                context.deleteDatabase("dbFileName.db");
                return null;
            }
            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (listener != null) {
                    listener.onComplete(true);
                }
            }
        }
        MyAsynchTask task = new MyAsynchTask();
        task.execute();
    }



    public void getOpenHoursOfPlace(String placeId,Context context,Model.GetOpenHoursOfPlaceListener listener){
        class MyAsynchTask extends AsyncTask {
            List<String> openHours;
            @Override
            protected Object doInBackground(Object[] objects) {
                openHours = AppLocalDB.getDatabase(context).openHoursDao().getOpenHoursOfPlace(placeId);

                return null;
            }
            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (listener != null) {
                    listener.onComplete(openHours);
                }
            }
        }
        MyAsynchTask task = new MyAsynchTask();
        task.execute();
    }

    public void addPlace(Place place,List<OpenHours>listOpenHours,Context context,Model.AddTripListener listener) {
        class MyAsynchTask extends AsyncTask {
            @Override
            protected Object doInBackground(Object[] objects) {
                AppLocalDB.getDatabase(context).placeDao().insertAll(place);
                if (listOpenHours != null) {
                    for (int i = 0; i < listOpenHours.size(); ++i) {
                        AppLocalDB.getDatabase(context).openHoursDao().insertAll(listOpenHours.get(i));
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (listener != null) {
                    listener.onComplete("true");
                }
            }
        }
        MyAsynchTask task = new MyAsynchTask();
        task.execute();
    }
    public void editPlace(Place place,Context context,Model.EditPlaceListener listener){
        class MyAsynchTask extends AsyncTask {
            @Override
            protected Object doInBackground(Object[] objects) {
                AppLocalDB.getDatabase(context).placeDao().insertAll(place);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (listener != null) {
                    listener.onComplete(true);
                }
            }
        }
        MyAsynchTask task = new MyAsynchTask();
        task.execute();
    }
        public  void getAllTrip(String travelerMail,Context context,Model.GetAllTripListener listener){
        class MyAsynchTask extends AsyncTask {
            Trip [] arrTrip ;
            @Override
            protected Object doInBackground(Object[] objects) {
                List<Trip> trips = AppLocalDB.getDatabase(context).tripDao().getTripByEmail(travelerMail);

                if(trips!=null){
                    Collections.sort(trips, new SortByDate());
                    arrTrip=new Trip[trips.size()];
                    trips.toArray(arrTrip);

                }
                return null;
            }
            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (listener != null) {
                    listener.onComplete(arrTrip);
                }
            }
        }
        MyAsynchTask task = new MyAsynchTask();
        task.execute();
    }
    static class SortByDate implements Comparator<Trip> {

        @Override
        public int compare(Trip o1, Trip o2) {
            return o1.getDate().compareTo(o2.getDate())*-1;
        }
    }

    public void getAllPlacesOfTrip(String tripId,Context context,Model.GetAllPlacesOfTrip listener){

        class MyAsynchTask extends AsyncTask {
            Place [] arrPlaces;
            @Override
            protected Object doInBackground(Object[] objects) {
                List<Place> places = AppLocalDB.getDatabase(context).placeDao().getPlaceOfTrip(tripId);

                if(places!=null){
                    arrPlaces=new Place[places.size()];
                    places.toArray(arrPlaces);

                }
                return null;
            }
            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (listener != null) {
                    listener.onComplete(arrPlaces);
                }
            }
        }
        MyAsynchTask task = new MyAsynchTask();
        task.execute();


    }


}