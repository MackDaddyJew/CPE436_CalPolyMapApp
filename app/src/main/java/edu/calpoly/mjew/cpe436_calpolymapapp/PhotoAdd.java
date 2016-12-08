package edu.calpoly.mjew.cpe436_calpolymapapp;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static edu.calpoly.mjew.cpe436_calpolymapapp.MainMapsActivity.selectedBuilding;

public class PhotoAdd extends AppCompatActivity {

    private static final int REQUEST_CAMERA = 0;
    private static final int REQUEST_STORAGE = 1;
    private static final int REQUEST_GALLERY = 2;

    private String mCurrentPhotoPath;
    private Uri mCurrentPhotoUri;
    private int tookPic = -1;
    String buildingName;
    ImageView newPic;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseReference;

    public void initDatabase() {
        mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://campusmap-7973e.appspot.com");
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_add);

        initDatabase();

        newPic = (ImageView) findViewById(R.id.newPhoto);
        mayRequestCamera();
        mayRequestGallery();

        // grab building name
        //  TODO: need a way to check the extras being sent through an intent
        //          because classroom and route can also call photo add class
        Intent in = getIntent();
        buildingName = in.getStringExtra("BuildingName");



        Button selectPic = (Button) findViewById(R.id.selectPicButton);
        selectPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra("number", 000);
                startActivityForResult(intent, REQUEST_GALLERY);
            }
        });


        Button takePic = (Button) findViewById(R.id.takePicButton);
        takePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("hello", "test works");

                Intent takePicIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                File imgFile = null;
                try{
                    imgFile = getFile();
                } catch (IOException ex){
                    Log.v("error", "IO exception on getFile()");
                }

                if(imgFile != null){
                    Log.v("imgFile", "not null: " + imgFile.toString());
                    Uri imgUri = FileProvider.getUriForFile(getApplicationContext(),
                            "edu.calpoly.mjew.cpe436_calpolymapapp.fileprovider",
                            imgFile);

                    takePicIntent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
                    startActivityForResult(takePicIntent, REQUEST_CAMERA);
                }
            }
        });

        Button confirmButton = (Button) findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // took a picture
                if(tookPic == 1) {
                    selectedBuilding.uploadUserPhoto(mCurrentPhotoPath, getContentResolver(), getApplicationContext());
                    finish();
                }
                // got a picture from gallery
                else if (tookPic == 0 ){
                    selectedBuilding.uploadUserPhoto(mCurrentPhotoUri, getContentResolver(), getApplicationContext());
                    finish();
                }
                // no picture taken or uploaded
                else {
                    Snackbar.make(newPic, "No photo taken or uploaded", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        Button cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private boolean mayRequestCamera() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(CAMERA)) {
            Snackbar.make(newPic, R.string.camera_permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{CAMERA}, REQUEST_CAMERA);
                        }
                    });
        } else {
            requestPermissions(new String[]{CAMERA}, REQUEST_CAMERA);
        }
        return false;
    }

    private boolean mayRequestGallery(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) {
            Snackbar.make(newPic, R.string.external_permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE);
                        }
                    });
        } else {
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE);
        }
        return false;
    }

    // change to file return type
    private File getFile() throws IOException {

        //File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        //Log.v("storageDir", storageDir.toString());

        File storDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        Log.v("storDir", storDir.toString());


        File image_file = new File(storDir,"test_image.jpg");
        mCurrentPhotoPath = image_file.getAbsolutePath();
        return image_file;
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                tookPic = 1;    // took pic? - yes

                Bitmap imgBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
                newPic.setImageBitmap(imgBitmap);
            }
            if (requestCode == REQUEST_GALLERY) {
                final Uri uri = data.getData();
                mCurrentPhotoUri = uri;
                tookPic = 0;    // took pic? - no

                try {
                    Bitmap imgBitmap = MediaStore.Images.Media
                            .getBitmap(this.getContentResolver(), mCurrentPhotoUri);
                    newPic.setImageBitmap(imgBitmap);
                } catch (IOException e){
                    Snackbar.make(newPic, "Error Reading Photo", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        }
    }
}
