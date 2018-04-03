package com.example.user.tourassistant.page_fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.user.tourassistant.R;
import com.example.user.tourassistant.firebase.Moment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * A simple {@link Fragment} subclass.
 */
public class SingleMomentFragment extends Fragment {

    private DatabaseReference mDatabase,SDatabase,userDatabase;
     private FirebaseStorage mFirebaseStorage;
    private String eventkey,EditKey;
    private boolean dbflag=true;
    public SingleMomentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Modify moment");

        EditKey=getArguments().getString("EditKey");
        eventkey=getArguments().getString("eventkey");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("moments").child(eventkey).child(EditKey);



        return inflater.inflate(R.layout.fragment_single_moment, container, false);
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);




    }

    public void setTitle(String title){
        TextView viewTitle = getActivity().findViewById(R.id.et_add_titleS);
        viewTitle.setText(title);
    }

    public void setDesc(String desc){
        TextView viewDesc = getActivity().findViewById(R.id.et_add_descriptionS);
        viewDesc.setText(desc);
    }

    public void setImage(Context imgContext, String image){

        final ProgressBar progressBar = new ProgressBar(imgContext);
        ImageView viewImage = getActivity().findViewById(R.id.add_imageS);

        //Picasso.with(imgContext).load(image).into(viewImage);
        //Glide.with(imgContext).load(image).into(viewImage);
        int radius = 30; // corner radius, higher value = more rounded
        int margin = 10; // crop margin, set to 0 for corners with no crop
           /* Glide.with(imgContext)
                    .load(image)
                    .bitmapTransform(new RoundedCornersTransformation(imgContext, radius, margin))
                    .into(viewImage);*/

        progressBar.setVisibility(View.VISIBLE);

        Glide.with(imgContext)
                .load(image)
                .bitmapTransform(new RoundedCornersTransformation(imgContext, radius, margin))
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) // resizes the image to 100x200 pixels but does not respect aspect ratio
                .fitCenter() // scale to fit entire image within ImageView
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false; // important to return false so the error placeholder can be placed
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(viewImage);

    }

    public void deletePost(){


        mDatabase.removeValue();


        FragmentManager fm4 = getActivity().getSupportFragmentManager();
        FragmentTransaction ft4 = fm4.beginTransaction();
        MomentFragment momentFragment = new MomentFragment();

        Bundle sendKey = new Bundle();
        sendKey.putString("eventkey", eventkey);
        momentFragment.setArguments(sendKey);

        ft4.replace(R.id.homeFragmentView,momentFragment);
        ft4.commit();



    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
       }



    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.medit_manu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteEvent:
                deletePost();


                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



}
