package edu.calpoly.mjew.cpe436_calpolymapapp;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static edu.calpoly.mjew.cpe436_calpolymapapp.MainMapsActivity.selectedBuilding;

/**
 * Created by mackenzie on 11/29/16.
 */
public class ClassRoom implements Comparable
{
    private String mRoomNumber;
    private String mRoomDescription; //the string description. We may want in the future to change this to multiple strings.
    private transient String PHOTO_FOLDER_NAME = "classrooms/";

    private ArrayList<String> mAllClassPhotos;

    public ClassRoom(){
        mRoomNumber = "";
        mRoomDescription = "";
        mAllClassPhotos = new ArrayList<>();

        String imageName = "buildings/landscape_icon.jpg";
        mAllClassPhotos.add(0, imageName);
    }

    public ClassRoom(String cNumber, String cDescription) {
        mRoomNumber = cNumber;
        mRoomDescription = cDescription;
        mAllClassPhotos = new ArrayList<>();

        String imageName = "buildings/landscape_icon.jpg";
        mAllClassPhotos.add(0, imageName);
    }

    public String getCRoomNumber() { return mRoomNumber; }
    public String getCRoomDescrip() { return mRoomDescription; }
    public ArrayList<String> getCAllRoomPhotos() { return mAllClassPhotos; }
    public String getCRoomPhotoByIndex(int index) { return mAllClassPhotos.get(index) ;}

    public void setCRoomNumber(String cNum) { mRoomNumber = cNum; }        // TODO: room numbers need to be strings for 282B cases
    public void setCRoomDescrip(String cRoomDescrip) { mRoomDescription = cRoomDescrip; }
    public void setCPhotoList(ArrayList<String> cPhotoList) { mAllClassPhotos = cPhotoList; }


    // used to sort list of ClassRooms in Building class
    @Override
    public int compareTo(Object cCompare)
    {
        ClassRoom cCom = (ClassRoom) cCompare;
        return  this.mRoomNumber.compareTo(cCom.getCRoomNumber());
    }

    @Override
    public String toString()
    {
        return("Room Number: " + this.mRoomNumber + ", "
                + "Description: " + this.mRoomDescription);
    }

    public void uploadUserClassPhoto(String currentPhotoPath, ContentResolver contentResolver, final Context context){
        try {
            // save picture to Gallery
            MediaStore.Images.Media.insertImage(
                    contentResolver, currentPhotoPath,"", "taken Pic");
        } catch (FileNotFoundException fe){

        }

        Toast.makeText(context, "Photo saved to Gallery", Toast.LENGTH_SHORT).show();

        // get components of file name to upload to Firebase Storage
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

        String userName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        String buildingNumber = selectedBuilding.getBuildingNumber();

        String pictureName = buildingNumber + "_"
                + userName + "_"
                + this.mRoomNumber
                + dateFormat.format(date) + ".jpeg";

        // set up Firebase variables
        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://campusmap-7973e.appspot.com");
        StorageReference filePath = mStorageRef.child("classrooms").child(pictureName);

        // update Firebase building value
        this.mAllClassPhotos.add(PHOTO_FOLDER_NAME + pictureName);
        Gson gson = new Gson();
        String myJson = gson.toJson(this);

        Map map = new Gson().fromJson(myJson,
                new TypeToken<HashMap<String, Object>>() {
                }.getType());

        String classIndex = Integer.toString(selectedBuilding.
                getAllClassRooms().indexOf(this));

        mDatabaseRef.child("building").child(Integer.toString(selectedBuilding.getBFbId()))
                .child("mAllClassRooms").child(classIndex).setValue(map);

        // upload photo to Firebase Storage
        Uri imageUri = Uri.fromFile(new File(currentPhotoPath));
        filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(context, "Photo saved to Firebase", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void uploadUserClassPhoto(Uri currentPhotoUri, ContentResolver contentResolver, final Context context)
    {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

        String buildingNumber = selectedBuilding.getBuildingNumber();
        String userName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

        String pictureName = buildingNumber + "_"
                + this.mRoomNumber
                + userName + "_"
                + dateFormat.format(date) + ".jpeg";

        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://campusmap-7973e.appspot.com");
        StorageReference filePath = mStorageRef.child("classrooms").child(pictureName);


        // update Firebase building value
        this.mAllClassPhotos.add(PHOTO_FOLDER_NAME + pictureName);
        Gson gson = new Gson();
        String myJson = gson.toJson(this);

        Map map = new Gson().fromJson(myJson,
                new TypeToken<HashMap<String, Object>>() {
                }.getType());

        mDatabaseRef.child("building").child(Integer.toString(selectedBuilding.getBFbId())).setValue(map);

        // upload photo to Firebase Storage
        filePath.putFile(currentPhotoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(context, "Photo saved to Firebase", Toast.LENGTH_SHORT).show();
            }
        });
    }}
