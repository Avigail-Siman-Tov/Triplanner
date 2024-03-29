package com.triplanner.triplanner.ui.profile;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
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
import com.squareup.picasso.Picasso;
import com.triplanner.triplanner.CircularImageView;
import com.triplanner.triplanner.Model.FavoriteCategories;
import com.triplanner.triplanner.Model.Model;
import com.triplanner.triplanner.Model.Traveler;
import com.triplanner.triplanner.R;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TravelerProfileFragment extends Fragment {
    TextView name, mail;
    Button editBtn;
    ListView listCategory;
    MyAdapter adapter;
    String [] arrCategory;
    TextView category,logout;
    Button cameraGalleryBtn;

    String travelerPicture;

    CircularImageView imageProfile;

    User user;

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
        user = app.currentUser();
        logout=view.findViewById(R.id.traveler_profile_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(TravelerProfileFragmentDirections.actionNavProfileToLogoutActivity());
            }
        });

        imageProfile= view.findViewById(R.id.displayImageView);
        Model.instance.getTravelerByEmailInDB(user.getProfile().getEmail(), getContext(), new Model.GetTravelerByEmailListener() {
            @Override
            public void onComplete(Traveler traveler, List<String> favoriteCategories) {
                name.setText(traveler.getTravelerName());
                mail.setText(traveler.getTravelerMail());
                arrCategory= new String[favoriteCategories.size()];
                favoriteCategories.toArray(arrCategory);
                adapter=new MyAdapter();
                listCategory.setAdapter(adapter);
                String picturePath = traveler.getTravelerPicture(); // Assuming this is a String representing the URI or file path
                Picasso.get().load(picturePath).into(imageProfile);
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
            category.setText("  "+a);
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
                // Tell the media scanner to scan the newly created image file
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(imageFile);
                requireActivity().sendBroadcast(mediaScanIntent);
                travelerPicture = contentUri.toString();
                editTraveler();
            }
        }

        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                Uri contentUri = data.getData();

                // Create a temporary file to store the selected image
                File imageFile = createGalleryImageFile();

                try {
                    // Copy the selected image to the temporary file
                    copyImageToTempFile(contentUri, imageFile);

                    // Display the image and proceed to handle it
                    selectedImage.setImageURI(Uri.fromFile(imageFile));
                    travelerPicture = Uri.fromFile(imageFile).toString();
                    editTraveler();
                } catch (IOException e) {
                    Log.e("tag", "Failed to copy the image to the temporary file");
                    e.printStackTrace();
                }
            }
        }
    }

    // Function to create a temporary file for gallery images
    private File createGalleryImageFile() {
        try {
            // Create an image file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            return File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            Log.e("tag", "Failed to create the temporary file");
            e.printStackTrace();
            return null;
        }
    }

    // Function to copy the selected image to a temporary file
    private void copyImageToTempFile(Uri sourceUri, File destFile) throws IOException {
        try (InputStream inputStream = requireActivity().getContentResolver().openInputStream(sourceUri);
             OutputStream outputStream = new FileOutputStream(destFile)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
        }
    }

    // Function to get the real path from a content URI
    private String getRealPathFromURI(Uri contentUri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        try (Cursor cursor = requireActivity().getContentResolver().query(contentUri, projection, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                if (columnIndex != -1) {
                    return cursor.getString(columnIndex);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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

    // Function to create a temporary file for gallery images
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



    private void editTraveler() {
        Model.instance.getTravelerByEmailInDB(user.getProfile().getEmail(), getContext(), new Model.GetTravelerByEmailListener() {
            @Override
            public void onComplete(Traveler traveler, List<String> favoriteCategories) {
                name.setText(traveler.getTravelerName());
                mail.setText(traveler.getTravelerMail());
                arrCategory= new String[favoriteCategories.size()];
                favoriteCategories.toArray(arrCategory);
                adapter=new MyAdapter();
                listCategory.setAdapter(adapter);
                Traveler traveler1=new Traveler(user.getProfile().getEmail(),traveler.getTravelerName(),traveler.getTravelerBirthYear(),traveler.getTravelerGender(),travelerPicture);
                List<FavoriteCategories> listFavoriteCategories = new ArrayList<FavoriteCategories>();
                for(int i=0; i< favoriteCategories.size();++i){
                    listFavoriteCategories.add(new FavoriteCategories(favoriteCategories.get(i),traveler.getTravelerMail()));
                }
                Model.instance.editTraveler(traveler1,listFavoriteCategories,getContext(),new Model.EditTravelerListener() {
                    @Override
                    public void onComplete(String isSuccess) {
                        if (isSuccess.equals("true")) {
                            Toast.makeText(getContext(), "Edit user successful", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(getContext(), "Edit user successful", Toast.LENGTH_SHORT).show();
                        }
                    }

                });
            }
        });

    }

}







