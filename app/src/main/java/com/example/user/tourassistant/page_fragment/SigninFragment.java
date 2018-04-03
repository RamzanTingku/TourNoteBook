package com.example.user.tourassistant.page_fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.user.tourassistant.R;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SigninFragment extends Fragment {
    public static final String ANONYMOUS = "anonymous";
    public static final int RC_SIGN_IN = 1;
    private ProgressBar mProgressBar;
    private String mUsername;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private SharedPreferences sharedPref;


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }



    public SigninFragment() {


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }





    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUsername = ANONYMOUS;
        mFirebaseAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    onSignedInInitialize(user.getDisplayName());
                } else {
                    // User is signed out
                    onSignedOutCleanup();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setProviders(
                                            AuthUI.EMAIL_PROVIDER,
                                            AuthUI.GOOGLE_PROVIDER)
                                    .setTheme(R.style.LoginTheme)
                                    //.setLogo(R.mipmap.grocery)
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };

    }

    @Override
    public void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);

        }



    }

    @Override
    public void onPause() {
        super.onPause();
        if(mAuthStateListener == null){
            goHome();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if(mAuthStateListener == null){
            goHome();
        }
    }

    private void onSignedInInitialize(String username) {
        mUsername = username;
        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("user",mUsername);
        editor.commit();
        editor.apply();

        goToEvent();
          }

    private void onSignedOutCleanup() {
        mUsername = ANONYMOUS;
        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("user",null);
        editor.commit();
        editor.apply();



    }

    public void goHome(){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        HomeFragment homeFragment = new HomeFragment();
        ft.replace(R.id.homeFragmentView,homeFragment);
        ft.commit();

    }

    public void goToEvent(){
        FragmentManager fme = getActivity().getSupportFragmentManager();
        FragmentTransaction fte = fme.beginTransaction();
        EventFragment eventFragment = new EventFragment();
        fte.replace(R.id.homeFragmentView,eventFragment);
        fte.commit();
    }







}
