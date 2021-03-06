package edu.calpoly.mjew.cpe436_calpolymapapp;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mackenzie on 11/29/16.
 */
public class Building
{
    private transient String PHOTO_FOLDER_NAME = "buildings/";
    private transient int mBuildingFBId;          // set Firebase ID
    private String mBuildingNumber;
    private String mBuildingName;
    private String mBuildingDescription;
    private LatLng mBuildingCenter;           // TODO change from transient as they are implemented

    private transient ArrayList<ClassRoom> mAllClassRooms;
    private ArrayList<Route> mAllRoutes;
    // if we add an official photo, don't want to have to search for it everytime


    public Building()
    {
        mBuildingNumber = "";
        mBuildingName = "";
        mBuildingDescription = "";
        mBuildingCenter = null; //bCenter;

        mAllClassRooms = new ArrayList<>();
        mAllRoutes = new ArrayList<>();
    }

    public Building(String bNumber, String bName, String bDescription) //, LatLng bCenter) TODO: add this param later
    {
        mBuildingNumber = bNumber;
        mBuildingName = bName;
        mBuildingDescription = bDescription;
        mBuildingCenter = null; //bCenter;

        mAllClassRooms = new ArrayList<>();
        mAllRoutes = new ArrayList<>();
    }

    @Override
    public String toString() {
        return("Building Number: " + this.mBuildingNumber + ", "
                + "Building Name: " + this.mBuildingName + ", "
                + "Description: " + this.mBuildingDescription + ", ");

        // TODO add rest of fields to toString
    }

    // initialize database with buildings
    //      only ever need to call this once
    // TODO: maybe move into a BuildingHandler class?
    static public void InitializeAllBuildings(String[] allBuildingNames, ArrayList<Double> allCoords)
    {
        Building newBuild;
        String bName, bNumber, bDescription;
        LatLng bCenter;// = new LatLng(35.301028, -120.658602);

        int i = 0;  // start at 1 because 0 is a filler in spinner object

        // FIREBASE AND JSON
        Gson gson = new Gson();
        DatabaseReference mDatabaseRef;
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> map;


        for(String s : allBuildingNames)
        {
            // skip first entry
            if(i != 0) {
                bNumber = s.split(" - ")[0];
                bName = s.split(" - ")[1];
                bDescription = "TO FILL IN";        // TODO - access another resource page and pass that in as another string array

                int j = i*2;

                newBuild = new Building(bNumber, bName, bDescription);

                bCenter = new LatLng(allCoords.get(j), allCoords.get(j+1));

                newBuild.setBCenter(bCenter);

                //ClassRoom basis = new ClassRoom("201", "Short Detail");
                //newBuild.mAllClassRooms.add(0, basis);

                Route basis = new Route();
                basis.setCreateName("");
                basis.setSnapshotPath("");

                newBuild.mAllRoutes.add(0, basis);

                String myJson = gson.toJson(newBuild);

                map = new Gson().fromJson(myJson,
                        new TypeToken<HashMap<String, Object>>() {
                        }.getType());

                mDatabaseRef.child("building").child(Integer.toString(i)).setValue(map);
            }
            i++;
        }
    }

    // basic information setters
    public void setBFbId(int bID) { mBuildingFBId = bID; }
    public void setBNum(String bNum)
    {
        mBuildingNumber = bNum;
    }
    public void setBName(String bName)
    {
        mBuildingName = bName;
    }
    public void setBDescription(String bDescription)
    {
        mBuildingDescription = bDescription;
    }
    public void setBCenter (LatLng bCenter) { mBuildingCenter = bCenter; }

    public void setBClassroomList(ArrayList<ClassRoom> bClassroomList) { mAllClassRooms = bClassroomList; }
    public void setBRouteList(ArrayList<Route> bRouteList) { mAllRoutes = bRouteList; }

    // basic information return functions
    public int getBFbId() { return mBuildingFBId; }
    public String getBuildingNumber() { return mBuildingNumber; }
    public String getBuildingName() { return mBuildingName; }
    public String getBuildingDescription(){ return mBuildingDescription; }
    public LatLng getBuildingCenter(){ return mBuildingCenter; }

    public ArrayList<Route> getAllBuildingRoutes() { return  mAllRoutes; }



 // CLASSROOM RELATED FUNCTIONS

    //Searches all classrooms that have been added to the ArrayList for a matching room number to the given "roomNumber" parameter
    //will return null if the room number is not found;
    public ClassRoom getClassRoom(String roomNumber)
    {
        int size = mAllClassRooms.size();
        for(int i = 0; i < size; i++)
            if(mAllClassRooms.get(i).getCRoomNumber().equals(roomNumber))
                return mAllClassRooms.get(i);
        return null;
    }

    // for use with Grid function
    public ArrayList<ClassRoom> getAllClassRooms()
    {
        return mAllClassRooms;
    }

    public ArrayList<String> getAllClassRoomsNames(){
        ArrayList<String> roomNames = new ArrayList<>();

        for(ClassRoom cr : mAllClassRooms){
            roomNames.add(cr.getCRoomNumber());
        }

        return roomNames;
    }


    // need to update firebase listing too
    // TODO: need to add photo as well
    public void addNewClassRoom(String cRoomNumber, String cDescription)
    {
        ClassRoom createdClassRoom = new ClassRoom(cRoomNumber, cDescription);
        this.mAllClassRooms.add(createdClassRoom);

         // sort here or right before view?

        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        Gson gson = new Gson();
        String myJson = gson.toJson(this);

        Map map = new Gson().fromJson(myJson,
                new TypeToken<HashMap<String, Object>>() {
                }.getType());

        mDatabaseRef.child("building").child(Integer.toString(this.mBuildingFBId)).setValue(map);
    }

 // ROUTE RELATED FUNCTIONS
    public void addNewRoute(Route bRoute)
    {
        mAllRoutes.add(bRoute);

        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        Gson gson = new Gson();
        String myJson = gson.toJson(this);

        Map map = new Gson().fromJson(myJson,
                new TypeToken<HashMap<String, Object>>() {
                }.getType());

        mDatabaseRef.child("building").child(Integer.toString(this.mBuildingFBId)).setValue(map);
    }


 // PHOTO RELATED FUNCTIONS
    // upload photo passed in as a String path
    /*public void uploadMapSnapshot(String currentPhotoPath, ContentResolver contentResolver, final Context context){
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
                + dateFormat.format(date) + ".jpeg";

        // set up Firebase variables
        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://campusmap-7973e.appspot.com");
        StorageReference filePath = mStorageRef.child("buildings").child(pictureName);

        // update Firebase building value
        Gson gson = new Gson();
        String myJson = gson.toJson(this);

        Map map = new Gson().fromJson(myJson,
                new TypeToken<HashMap<String, Object>>() {
                }.getType());

        mDatabaseRef.child("building").child(Integer.toString(this.mBuildingFBId)).setValue(map);

        // upload photo to Firebase Storage
        Uri imageUri = Uri.fromFile(new File(currentPhotoPath));
        filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(context, "Photo saved to Firebase", Toast.LENGTH_SHORT).show();
            }
        });
    }*/
}
