package com.triplanner.triplanner.ui.profile;
import android.content.Intent;
import android.net.Uri;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import java.io.IOException;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.triplanner.triplanner.Model.Model;
import com.triplanner.triplanner.Model.Traveler;
import com.triplanner.triplanner.R;
import java.util.List;
import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;


public class TravelerProfileFragment extends Fragment {
    TextView name, mail,categories;
    Button editBtn;
    ListView listCategory;
    MyAdapter adapter;
    String [] arrCategory;
    TextView category,logout;
    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView profileImage; // Add this line

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
    @RequiresApi(api = Build.VERSION_CODES.N)
   public View onCreateView(@NonNull LayoutInflater inflater,
                               ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_traveler_profile, container, false);
       //categories = view.findViewById(R.id.traveler_profile_categories);
        editBtn = view.findViewById(R.id.traveler_profile_edit_btn);
        mail = view.findViewById(R.id.traveler_profile_email);
        name = view.findViewById(R.id.traveler_profile_name);
        listCategory=view.findViewById(R.id.traveler_profile_list_category);
        profileImage = view.findViewById(R.id.profileImage); // Add this line

        Realm.init(getContext()); // context, usually an Activity or Application
        App app = new App(new AppConfiguration.Builder(getString(R.string.AppId)).build());
        User user = app.currentUser();
        logout=view.findViewById(R.id.traveler_profile_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(TravelerProfileFragmentDirections.actionNavProfileToLogoutActivity());
            }
        });
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkConnected()) {
                    // ... (your existing code)

                    // Add the following lines to launch the image selection
                    Intent imagePickerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(imagePickerIntent, PICK_IMAGE_REQUEST);
                } else {
                    Toast.makeText(getContext(), "Error! Connect to Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
       Model.instance.getTravelerByEmailInDB(user.getProfile().getEmail(), getContext(), new Model.GetTravelerByEmailListener() {
            @Override
            public void onComplete(Traveler traveler, List<String> favoriteCategories) {
                name.setText("Hello "+traveler.getTravelerName());
                mail.setText(traveler.getTravelerMail());
                arrCategory= new String[favoriteCategories.size()];
                favoriteCategories.toArray(arrCategory);
                adapter=new MyAdapter();
                listCategory.setAdapter(adapter);
                editBtn= view.findViewById(R.id.traveler_profile_edit_btn);
                editBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isNetworkConnected()) {
                            // Add the following lines to launch the image selection
                            Intent imagePickerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(imagePickerIntent, PICK_IMAGE_REQUEST);
                        } else {
                            Toast.makeText(getContext(), "Error! Connect to Internet", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                // User picked an image from the gallery
                Uri selectedImage = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImage);
                    profileImage.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            if (arrCategory== null) {
                return 0;
            } else {
                return arrCategory.length;
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
                view = inflater.inflate(R.layout.row_favorite_category, null);
            } else {

            }
            category = view.findViewById(R.id.row_favorite_category_text_view);
            String c = arrCategory[i];
            String a=c.replace('_',' ');
            category.setText(a);
            return view;
        }

    }

}