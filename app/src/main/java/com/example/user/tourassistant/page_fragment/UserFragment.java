package com.example.user.tourassistant.page_fragment;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.user.tourassistant.R;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private TextView userNameTV, userMailTV;
    private ImageView userImage;

    public UserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        getActivity().setTitle("My profile");

        mAuth = FirebaseAuth.getInstance();

        return inflater.inflate(R.layout.fragment_user, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mAuth.getCurrentUser() != null) {
            String name = mAuth.getCurrentUser().getDisplayName();
            String email = mAuth.getCurrentUser().getEmail();
            Uri image = mAuth.getCurrentUser().getPhotoUrl();
            Toast.makeText(getActivity(), name, Toast.LENGTH_SHORT).show();

            userNameTV = getView().findViewById(R.id.user_name);
            userMailTV = getView().findViewById(R.id.user_mail);
            userImage = getView().findViewById(R.id.user_image);

            userNameTV.setText(name);
            userMailTV.setText(email);
            Glide.with(getContext()).load(image).into(userImage);


        }
    }
}