package com.triplanner.triplanner.ui.profile;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.view.View;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.triplanner.triplanner.Model.Model;
import com.triplanner.triplanner.Model.Traveler;
import com.triplanner.triplanner.R;
import java.util.List;
import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.triplanner.triplanner.R;

import org.bson.types.Binary;
import org.w3c.dom.Document;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class TravelerProfileFragment extends Fragment {
    TextView name, mail,categories;
    Button editBtn;
    ListView listCategory;
    MyAdapter adapter;
    String [] arrCategory;
    TextView category,logout;

    Button cameraGalleryBtn;



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

       Model.instance.getTravelerByEmailInDB(user.getProfile().getEmail(), getContext(), new Model.GetTravelerByEmailListener() {
            @Override
            public void onComplete(Traveler traveler, List<String> favoriteCategories) {
                name.setText(traveler.getTravelerName());
                mail.setText(traveler.getTravelerMail());
                arrCategory= new String[favoriteCategories.size()];
                favoriteCategories.toArray(arrCategory);
                adapter=new MyAdapter();
                listCategory.setAdapter(adapter);
                editBtn= view.findViewById(R.id.traveler_profile_edit_btn);
                editBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(isNetworkConnected()) {
                            String[] arrayCategories = new String[favoriteCategories.size()];
                            favoriteCategories.toArray(arrayCategories);
                            TravelerProfileFragmentDirections.ActionNavProfileToTravelerEditProfileFragment action = TravelerProfileFragmentDirections.actionNavProfileToTravelerEditProfileFragment(traveler, arrayCategories);
                            Navigation.findNavController(view).navigate(action);
                        }
                        else{
                            Toast.makeText(getContext(), "Error! Connect to Internet", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        //for galary and camera
        selectedImage = view.findViewById(R.id.displayImageView);

        cameraGalleryBtn = view.findViewById(R.id.cameraGalleryBtn);


        cameraGalleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show a dialog or menu to allow the user to choose between camera and gallery
                showCameraOrGalleryDialog();
            }
        });

        return view;
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


    //camera and galary for profile
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int GALLERY_REQUEST_CODE = 105;
    ImageView selectedImage;
    String currentPhotoPath;

    private void askCameraPermissions() {
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        } else {
            dispatchTakePictureIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERM_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(requireActivity(), "Camera Permission is Required to Use camera.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                // Handle the result when an image is captured with the camera
                File imageFile = new File(currentPhotoPath);
                selectedImage.setImageURI(Uri.fromFile(imageFile));
                Log.d("tag", "Absolute Url of Image is " + Uri.fromFile(imageFile));

                // Tell the media scanner to scan the newly created image file
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(imageFile);
                requireActivity().sendBroadcast(mediaScanIntent);
            }
        }

        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                // Handle the result when an image is selected from the gallery
                Uri contentUri = data.getData();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "." + getFileExt(contentUri);
                Log.d("tag", "onActivityResult: Gallery Image Uri:  " + imageFileName);
                selectedImage.setImageURI(contentUri);
            }
        }
    }
    private String getFileExt(Uri contentUri) {
        ContentResolver c = requireActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(contentUri));
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Handle the exception
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(requireContext(),  "com.triplanner.triplanner.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }


    private void showCameraOrGalleryDialog() {
        // Create a custom dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_camera_gallery, null);

        builder.setView(dialogView);

        final AlertDialog dialog = builder.create();

        // Set onClickListeners for camera and gallery options
        Button cameraOption = dialogView.findViewById(R.id.cameraOption);
        Button galleryOption = dialogView.findViewById(R.id.galleryOption);
        Button cancelButton = dialogView.findViewById(R.id.cancelOption);

        cameraOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                askCameraPermissions();
            }
        });

        galleryOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, GALLERY_REQUEST_CODE);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the dialog or perform any desired action on cancel
                dialog.dismiss(); // If the "Cancel" button is in a dialog
            }
        });

        dialog.show();
    }

}







