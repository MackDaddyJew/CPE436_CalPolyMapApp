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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static edu.calpoly.mjew.cpe436_calpolymapapp.MainMapsActivity.selectedBuilding;

public class ClassRoomAdd extends AppCompatActivity {

    private static final int REQUEST_CAMERA = 0;
    private static final int REQUEST_STORAGE = 1;
    private static final int REQUEST_GALLERY = 2;

    private String mCurrentPhotoPath;
    private Uri mCurrentPhotoUri;
    private int tookPic = -1;

    ImageView newPic;
    private String classroomNum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_room_add);

        final EditText classNumView = (EditText) findViewById(R.id.editClassroomNumber);
        final EditText classDescripView = (EditText) findViewById(R.id.editClassroomDescrip);


        newPic = (ImageView) findViewById(R.id.classroomPic);

        //Intent in = new Intent();
        //classroomNum = in.getStringExtra("classroomNum");


        Button confirmChange = (Button) findViewById(R.id.confirmAddButton);
        confirmChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cNum = classNumView.getText().toString();
                String cDescrip = classDescripView.getText().toString();

                if(cNum.trim().matches(""))
                {
                    Toast.makeText(getApplicationContext(),
                            "Please enter a Classroom number.", Toast.LENGTH_SHORT).show();
                }
                else
                {

                    selectedBuilding.addNewClassRoom(cNum, cDescrip);
                    ClassRoom cr = selectedBuilding.getClassRoom(cNum);

                    if(tookPic == 1) {
                        cr.uploadUserClassPhoto(mCurrentPhotoPath, getContentResolver(), getApplicationContext());
                        finish();
                    }
                    // got a picture from gallery
                    else if (tookPic == 0 ){
                        cr.uploadUserClassPhoto(mCurrentPhotoUri, getContentResolver(), getApplicationContext());
                        finish();
                    }
                    // no picture taken or uploaded
                    else {
                        Snackbar.make(newPic, "No photo taken or uploaded", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }
            }
        });


        Button cancelChange = (Button) findViewById(R.id.cancelAddButton);
        cancelChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button takePic = (Button) findViewById(R.id.takeClassroomPicButton);
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

        Button selectPic = (Button) findViewById(R.id.selectClassroomPicButton);
        selectPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra("number", 000);
                startActivityForResult(intent, REQUEST_GALLERY);
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
