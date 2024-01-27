package com.triplanner.triplanner.ui.planTrip;
import androidx.navigation.Navigation;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.core.util.Pair;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;
import com.triplanner.triplanner.R;
import com.triplanner.triplanner.horizontalNumberPicker.HorizontalNumberPicker;
import org.json.JSONObject;
import com.google.android.material.datepicker.CalendarConstraints;
import java.util.concurrent.TimeUnit;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import com.google.android.material.datepicker.MaterialDatePicker;


public class PlanTripFragment extends Fragment {
    private Button selectDatesButton;
    private TextView dateRangeTextView;
    private Calendar startDateCalendar;
    private Calendar endDateCalendar;
    private MaterialDatePicker<Pair<Long, Long>> materialDatePicker; // Corrected declaration

    private TextView tripDatesTextView;

    TextInputLayout InputsTripName;
    Place myPlace = null;
    Integer tripDaysNumber;
    ProgressDialog myLoadingDialog;
    String tripName, tripPicutre , tripDateStart,tripDateEnd;
    TextView planTripButton;

    JSONObject jsonDataPage1 = null, jsonDataPage2 = null, jsonDataPage3 = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plan_trip, container, false);

        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), getString(R.string.places_api_key));
        }

        InputsTripName = view.findViewById(R.id.fragment_plan_trip_input_name_trip);

        // Initialize your views
        selectDatesButton = view.findViewById(R.id.selectDatesButton);

        startDateCalendar = Calendar.getInstance();
        endDateCalendar = Calendar.getInstance();

        // Handle the selection of date range
        // Handle the selection of date range
        // Handle the selection of date range
        materialDatePicker = MaterialDatePicker.Builder.dateRangePicker()
                .setSelection(Pair.create(MaterialDatePicker.todayInUtcMilliseconds(), MaterialDatePicker.todayInUtcMilliseconds()))
                .setCalendarConstraints(buildCalendarConstraints())
                .setTheme(R.style.MaterialCalendarThemeBackground)
                .build();


        // Apply the custom theme


        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
            @Override
            public void onPositiveButtonClick(Pair<Long, Long> selection) {
                long currentDay = MaterialDatePicker.todayInUtcMilliseconds();
                if (selection.first < currentDay || selection.second < currentDay) {
                    Toast.makeText(getContext(), "Please select valid dates.", Toast.LENGTH_SHORT).show();
                } else {
                    startDateCalendar.setTimeInMillis(selection.first);
                    endDateCalendar.setTimeInMillis(selection.second);

                    updateDateRangeText();
                }
            }
        });


        // Handle button click to show the MaterialDatePicker
        selectDatesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDatePicker.show(getChildFragmentManager(), materialDatePicker.toString());
            }
        });

        myLoadingDialog = new ProgressDialog(requireContext());

        TextView tripDays = view.findViewById(R.id.et_number);
        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(requireActivity());
        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setTypeFilter(TypeFilter.CITIES);
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.PHOTO_METADATAS, Place.Field.LAT_LNG));
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                myPlace = place;

                List<PhotoMetadata> photoMetadataList = place.getPhotoMetadatas();
                if (photoMetadataList != null && photoMetadataList.size() > 0) {
                    PhotoMetadata photoMetadata = photoMetadataList.get(0);
                    Log.d("Photo URL", "photos" + photoMetadataList);

                    // Get the photo metadata attributions
                    String attributions = photoMetadata.toString();
                    // Extract the photo reference from attributions (if applicable)
                    String photoReference = extractPhotoReference(attributions);
                    // Use getAttributions to obtain the photo reference
                    if (photoReference != null) {
                        tripPicutre = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + Uri.encode(photoReference) + "&key=AIzaSyC-2lRMHs-8VTX-pePcej_Lsb807VOxk8U";
                    } else {
                        // Handle the case where the attributions (photo reference) is null
                        tripPicutre = "https://dalicanvas.co.il/wp-content/uploads/2022/10/%D7%A9%D7%A7%D7%99%D7%A2%D7%94-%D7%A7%D7%9C%D7%90%D7%A1%D7%99%D7%AA-1.jpg";
                    }

                    // Print or use the URL as needed
                    Log.d("Photo URL", tripPicutre);
                } else {
                    tripPicutre = "https://dalicanvas.co.il/wp-content/uploads/2022/10/%D7%A9%D7%A7%D7%99%D7%A2%D7%94-%D7%A7%D7%9C%D7%90%D7%A1%D7%99%D7%AA-1.jpg";
                    // Handle the case where no photos are available for the selected place
                }

            }

            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
                Toast.makeText(requireContext(), status.toString(), Toast.LENGTH_SHORT).show();
                Log.d("mylog", status.toString());
            }
        });
        final HorizontalNumberPicker np_channel_nr = view.findViewById(R.id.np_channel_nr);
        // use value in your code
        final int nr = np_channel_nr.getValue();
        planTripButton = view.findViewById(R.id.fragment_plan_trip_textview_ok);

        planTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tripName = InputsTripName.getEditText().getText().toString();
                tripDaysNumber = Integer.parseInt(tripDays.getText().toString());
                if (checkName(InputsTripName.getEditText().getText().toString())) {
                    if (myPlace != null) {
                        PlanTripFragmentDirections.ActionNavPlanTripToSplashPlanTripFragment action = PlanTripFragmentDirections.actionNavPlanTripToSplashPlanTripFragment(tripDaysNumber, myPlace.getName(), (float) myPlace.getLatLng().latitude, (float) myPlace.getLatLng().longitude, tripName, myPlace.getName(), tripPicutre,tripDateStart,tripDateEnd);
                        Navigation.findNavController(view).navigate(action);
                    } else {
                        Toast.makeText(requireContext(), "please choose destination for the trip", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return view;
    }
    private CalendarConstraints buildCalendarConstraints() {
        long currentDay = MaterialDatePicker.todayInUtcMilliseconds();

        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        constraintsBuilder.setValidator(DateValidatorPointForward.from(currentDay));

        return constraintsBuilder.build();
    }


    private void updateDateRangeText() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy", Locale.US);
        String startDateStr = dateFormat.format(startDateCalendar.getTime());
        tripDateStart = startDateStr;
        String endDateStr = dateFormat.format(endDateCalendar.getTime());
        tripDateEnd = endDateStr;

       // Update the button text as well
       String buttonText =  startDateStr + " - " + endDateStr;
        selectDatesButton.setText(buttonText);
    }

//    private void showDatePickerDialog(final boolean isStartDate) {
//        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                if (isStartDate) {
//                    startDateCalendar.set(Calendar.YEAR, year);
//                    startDateCalendar.set(Calendar.MONTH, monthOfYear);
//                    startDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//                    showDatePickerDialog(false);
//                } else {
//                    endDateCalendar.set(Calendar.YEAR, year);
//                    endDateCalendar.set(Calendar.MONTH, monthOfYear);
//                    endDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//                    updateDateRangeText();
//                }
//            }
//        };
//
//        DatePickerDialog datePickerDialog = new DatePickerDialog(
//                requireContext(),
//                dateSetListener,
//                isStartDate ? startDateCalendar.get(Calendar.YEAR) : endDateCalendar.get(Calendar.YEAR),
//                isStartDate ? startDateCalendar.get(Calendar.MONTH) : endDateCalendar.get(Calendar.MONTH),
//                isStartDate ? startDateCalendar.get(Calendar.DAY_OF_MONTH) : endDateCalendar.get(Calendar.DAY_OF_MONTH)
//        );
//
//        if (isStartDate) {
//            datePickerDialog.getDatePicker().setMaxDate(endDateCalendar.getTimeInMillis());
//        } else {
//            datePickerDialog.getDatePicker().setMinDate(startDateCalendar.getTimeInMillis());
//        }
//
//        datePickerDialog.show();
//    }
    private String extractPhotoReference(String attributions) {
        if (attributions == null || attributions.isEmpty()) {
            return null;
        }

        // Find the index of "photoReference="
        int photoReferenceIndex = attributions.indexOf("photoReference=");

        if (photoReferenceIndex != -1) {
            // Move to the end of "photoReference="
            int startIndex = photoReferenceIndex + "photoReference=".length();

            // Find the index of the next comma starting from "photoReference="
            int commaIndex = attributions.indexOf(',', startIndex);

            if (commaIndex != -1) {
                // Extract the substring between "photoReference=" and the next comma
                String photoReference = attributions.substring(startIndex, commaIndex);

                return photoReference.trim();
            }
        }

        return null;
    }

    private boolean checkName(String tripName) {
        if (tripName.length() > 0)
            return true;
        Toast.makeText(requireContext(), "please enter a name for the trip", Toast.LENGTH_SHORT).show();
        return false;
    }
}
