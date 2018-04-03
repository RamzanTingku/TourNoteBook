package com.example.user.tourassistant.page_fragment;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.user.tourassistant.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddMomentFragment extends Fragment {

    private static final int REQUEST_CAMERA = 1;
    private static final int GALLERY_REQUEST = 1;
    private ImageButton addImage,closeButton;
    private Uri outputFileUri;

    private EditText addTitleET, addDescriptionET;
    private Button postMomentBTN;
    private Uri imageUri = null;
    private StorageReference mStorageRef;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private DatabaseReference momentDatabase, mDatabaseUser;
    String eventkey;
    public AddMomentFragment() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Add Moment");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_moment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        eventkey = getArguments().getString("eventkey");
//        mAuth = FirebaseAuth.getInstance();
//
//        mCurrentUser = mAuth.getCurrentUser();
//        Toast.makeText(getActivity(),mCurrentUser.getEmail(),Toast.LENGTH_LONG).show();

        mStorageRef = FirebaseStorage.getInstance().getReference();
        momentDatabase = FirebaseDatabase.getInstance().getReference().child("moments").child(eventkey);
        //mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("users").child(mCurrentUser.getUid());

        addImage = (ImageButton) getActivity().findViewById(R.id.add_image);
        addTitleET = (EditText) getActivity().findViewById(R.id.et_add_title);
        addDescriptionET = (EditText) getActivity().findViewById(R.id.et_add_description);

        progressDialog = new ProgressDialog(getActivity());


        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openImageIntent();
                               /*Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                               galleryIntent.setType("image*//*");
                               startActivityForResult(galleryIntent, GALLERY_REQUEST);*/


            }
        });

        closeButton=getActivity().findViewById(R.id.backFromAddmoment);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });









    }


    private void openImageIntent() {

        // Determine Uri of camera image to save.


        final File root = new File(Environment.getExternalStorageDirectory() + File.separator + "TravelPic" + File.separator);
        root.mkdirs();
        final String fname = "img_"+ System.currentTimeMillis() + ".jpg";
        final File sdImageMainDirectory = new File(root, fname);
        outputFileUri = Uri.fromFile(sdImageMainDirectory);




        // Camera.
        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getActivity().getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for(ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(packageName, res.activityInfo.name));
            intent.setPackage(packageName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            cameraIntents.add(intent);
        }

        // Filesystem.
        final Intent galleryIntent = new Intent();
        galleryIntent.setType("image//*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

        // Chooser of filesystem options.
        final Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Source");

        // Add the camera options.
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[cameraIntents.size()]));

        startActivityForResult(chooserIntent, REQUEST_CAMERA);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                final boolean isCamera;
                if (data == null || data.getData() == null) {
                    isCamera = true;
                } else {
                    final String action = data.getAction();
                    if (action == null) {
                        isCamera = false;
                    } else {
                        isCamera = action.equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    }
                }

                if (isCamera) {
                    imageUri = outputFileUri;
                    addImage.setImageURI(imageUri);
                } else {
                    imageUri = data == null ? null : data.getData();
                    addImage.setImageURI(imageUri);
                }
            }
        }


    }

   /* @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);


                if(requestCode==GALLERY_REQUEST && resultCode == RESULT_OK){
                    imageUri = imageReturnedIntent.getData();
                    addImage.setImageURI(imageUri);
                }



    }*/



    private void startPosting() {
        progressDialog.setMessage("Uploading Post..");
        final String title = addTitleET.getText().toString().trim();
        final String description = addDescriptionET.getText().toString().trim();
        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(description) && imageUri != null){
            progressDialog.show();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String currentDateandTime = sdf.format(new Date());

            StorageReference filepath = mStorageRef.child("Moment_Image").child(eventkey).child(currentDateandTime);
            filepath.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            @SuppressWarnings("VisibleForTests") final Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            final DatabaseReference newPost = momentDatabase.push();
                            newPost.child("title").setValue(title);
                            newPost.child("desc").setValue(description);
                            newPost.child("image").setValue(downloadUrl.toString());

                            goBack();



                            progressDialog.dismiss();



                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            Toast.makeText(getActivity(), "Uploading Post Failed", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });
        }

    }


    public void goBack(){
        FragmentManager fm3 = getActivity().getSupportFragmentManager();
        FragmentTransaction ft3 = fm3.beginTransaction();
        MomentFragment momentFragment = new MomentFragment();
        Bundle sendKey = new Bundle();
        sendKey.putString("eventkey", eventkey);
        momentFragment.setArguments(sendKey);
        ft3.replace(R.id.homeFragmentView,momentFragment);
        ft3.commit();


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.save_manu, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveEvent:
                startPosting();

                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {

                    }
                } else {
                    Toast.makeText(getActivity(),"storage permission",Toast.LENGTH_LONG).show();

                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }




}
