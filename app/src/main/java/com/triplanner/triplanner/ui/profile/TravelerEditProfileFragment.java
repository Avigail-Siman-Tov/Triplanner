package com.triplanner.triplanner.ui.profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.triplanner.triplanner.Model.FavoriteCategories;
import com.triplanner.triplanner.Model.Model;
import com.triplanner.triplanner.Model.Traveler;
import com.triplanner.triplanner.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;


public class TravelerEditProfileFragment extends Fragment {
    Traveler traveler;

    TextInputLayout InputsName;
    Spinner genderSpinner, birthYearSpinner;
    TextView textViewCategory,errorCategory;
    boolean[] selectedCategory;
    List<String> travelerFavoriteCategories;
    Button saveBtn,cancelBtn;
    ArrayList<Integer> categoriesList = new ArrayList<>();
    String travelerName,travelerGender;
    int travelerBirthYear;

    final String[] categoriesArray={
            "amusement park","aquarium","art gallery","bar","casino",
            "museum","night club","park","shopping mall","spa",
            "tourist attraction","zoo", "bowling alley","cafe",
            "church","city hall","library","mosque", "synagogue"
    };
    final String[] categoriesArraySaveInMongoDb={
            "amusement_park","aquarium","art_gallery","bar","casino",
            "museum","night_club","park","shopping_mall","spa",
            "tourist_attraction","zoo", "bowling_alley","cafe",
            "church","city_hall","library","mosque", "synagogue"
    };
    final String[] genderArray={ "Male","Female","Other"};
    User user;
    String[] listCategories;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_traveler_edit_profile, container, false);

        Realm.init(getContext()); // context, usually an Activity or Application
        App app = new App(new AppConfiguration.Builder(getString(R.string.AppId)).build());
        user = app.currentUser();

        Traveler traveler=TravelerEditProfileFragmentArgs.fromBundle(getArguments()).getTraveler();
        listCategories = TravelerEditProfileFragmentArgs.fromBundle(getArguments()).getListCategories();

        InputsName= view.findViewById(R.id.fragment_edit_profile_userName);
        InputsName.getEditText().setText(traveler.getTravelerName());
        genderSpinner= view.findViewById(R.id.fragment_edit_profile_spinner_gender);
        ArrayAdapter<CharSequence> adapterGender = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item,genderArray);
        adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapterGender);
        genderSpinner.setSelection(Arrays.asList(genderArray).indexOf(traveler.getTravelerGender()));

        birthYearSpinner = view.findViewById(R.id.fragment_edit_profile_spinner_birthyear);
        ArrayAdapter<CharSequence> adapterBirthYear = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, getYears());
        adapterBirthYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        birthYearSpinner.setAdapter(adapterBirthYear);
        textViewCategory = view.findViewById(R.id.fragment_edit_profile_category_textView);
        errorCategory = view.findViewById(R.id.fragment_edit_profile_textView_error_category);
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        birthYearSpinner.setSelection(year-traveler.getTravelerBirthYear());
        birthYearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                travelerBirthYear=Integer.valueOf( parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                travelerGender = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        selectedCategory = new boolean[categoriesArray.length];
        for(int i= 0; i<selectedCategory.length;++i){
            selectedCategory[i]=false;
        }
        textViewCategory = view.findViewById(R.id.fragment_edit_profile_category_textView);
        textViewCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BuildCategoryList();
            }});

        travelerFavoriteCategories=new ArrayList<>();
        errorCategory.setText("");
        // Initialize string builder
        StringBuilder stringBuilder = new StringBuilder();
        // use for loop
        for (int j = 0; j < listCategories.length; j++) {
            categoriesList.add(Arrays.asList(categoriesArraySaveInMongoDb).indexOf(listCategories[j]));
            selectedCategory[Arrays.asList(categoriesArraySaveInMongoDb).indexOf(listCategories[j])]= true;
            travelerFavoriteCategories.add(listCategories[j]);
            // concat array value
            stringBuilder.append(listCategories[j].replace("_"," "));
            // check condition
            if (j != listCategories.length - 1) {
                // When j value  not equal
                // to lang list size - 1
                // add comma
                stringBuilder.append(", ");
            }
        }
        Collections.sort(categoriesList);
        // set text on textView
        textViewCategory.setText(stringBuilder.toString());
        saveBtn = view.findViewById(R.id.fragment_edit_profile_edit_btn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertUser();
            }
        });
        cancelBtn = view.findViewById(R.id.fragment_edit_profile_cancel_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getView()).navigate(TravelerEditProfileFragmentDirections.actionTravelerEditProfileFragmentToNavProfile());
            }
        });
        return view;
    }
    public String[] getYears() {
        String[] years = new String[120];
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int s;
        for (int index = 0; index < years.length; ) {
            s = year - index;
            years[index++] = String.valueOf(s);
        }
        return years;
    }

    private void BuildCategoryList(){
        // Initialize alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        // set title
        builder.setTitle("Select Categories:");

        // set dialog non cancelable
        builder.setCancelable(false);

        int selected = 0; // or whatever you want


        builder.setMultiChoiceItems(categoriesArray, selectedCategory, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                // check condition
                if (b) {
                    // when checkbox selected
                    // Add position  in lang list
                    categoriesList.add(i);
                    // Sort array list
                    Collections.sort(categoriesList);
                } else {
                    // when checkbox unselected
                    // Remove position from langList
                    categoriesList.remove( categoriesList.indexOf(i));
                }
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                travelerFavoriteCategories=new ArrayList<>();
                errorCategory.setText("");
                // Initialize string builder
                StringBuilder stringBuilder = new StringBuilder();
                // use for loop
                for (int j = 0; j < categoriesList.size(); j++) {

                    travelerFavoriteCategories.add(categoriesArraySaveInMongoDb[categoriesList.get(j)]);
                    // concat array value
                    stringBuilder.append(categoriesArray[categoriesList.get(j)]);
                    // check condition
                    if (j != categoriesList.size() - 1) {
                        // When j value  not equal
                        // to lang list size - 1
                        // add comma
                        stringBuilder.append(", ");
                    }
                }
                // set text on textView
                textViewCategory.setText(stringBuilder.toString());
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // dismiss dialog
                dialogInterface.dismiss();
            }
        });
        builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // use for loop
                for (int j = 0; j < selectedCategory.length; j++) {
                    // remove all selection
                    selectedCategory[j] = false;
                    // clear language list

                }
                categoriesList.clear();
                travelerFavoriteCategories.clear();
                // clear text view value
                textViewCategory.setText("");
            }
        });
        // show dialog
        builder.show();
    }
    private  void insertUser(){
        String username= InputsName.getEditText().getText().toString();
        if(username.isEmpty() || username.length()<3)  {
            showError(InputsName,"UserName is not valid");
        }
        else if (textViewCategory.getText()==""){
            errorCategory.setText("Select at least one category ");
        }
        else{
            editTraveler();
        }
    }
    private void editTraveler() {
        String partitionValue = user.getProfile().getEmail();
        travelerName=InputsName.getEditText().getText().toString();
        Traveler traveler=new Traveler(partitionValue, travelerName,travelerBirthYear,travelerGender);
        List<FavoriteCategories> listFavoriteCategories = new ArrayList<FavoriteCategories>();
        for(int i=0; i< travelerFavoriteCategories.size();++i){
            listFavoriteCategories.add(new FavoriteCategories(travelerFavoriteCategories.get(i),traveler.getTravelerMail()));
        }
        Model.instance.editTraveler(traveler,listFavoriteCategories,getContext(),new Model.EditTravelerListener() {
            @Override
            public void onComplete(String isSuccess) {
                if (isSuccess.equals("true")) {
                    Toast.makeText(getContext(), "Edit user successful", Toast.LENGTH_LONG).show();


                } else {
                    Toast.makeText(getContext(), "Edit user successful", Toast.LENGTH_SHORT).show();
                }
                Navigation.findNavController(getView()).navigate(TravelerEditProfileFragmentDirections.actionTravelerEditProfileFragmentToNavProfile());
            }

        });

    }

    private void showError(TextInputLayout field, String text) {
        field.setError(text);
        field.requestFocus();
    }
}