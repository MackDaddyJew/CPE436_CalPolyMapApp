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
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class PhotoAdd extends AppCompatActivity {


    private static final int REQUEST_CAMERA = 0;
    private static final int REQUEST_STORAGE = 1;
    private String mCurrentPhotoPath;
    String buildingName;
    ImageView newPic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_add);

        newPic = (ImageView) findViewById(R.id.newPhoto);
        mayRequestCamera();
        mayRequestGallery();



        Button takePic = (Button) findViewById(R.id.takePicButton);
        takePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

        Button selectPic = (Button) findViewById(R.id.selectPicButton);
        selectPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
            Snackbar.make(newPic, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
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
            Snackbar.make(newPic, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
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
                Bundle extras = data.getExtras();

                try {
                    MediaStore.Images.Media.insertImage(
                            getContentResolver(), mCurrentPhotoPath,
                            "", "taken Pic");
                } catch (FileNotFoundException fe){

                }

                Bitmap imgBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
                newPic.setImageBitmap(imgBitmap);
            }
        }
    }
}
