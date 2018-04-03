package com.example.user.tourassistant.page_fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.user.tourassistant.R;
import com.example.user.tourassistant.TopPlace;
import com.example.user.tourassistant.TopPlaceAdapter;
import com.example.user.tourassistant.google_place.Example;
import com.example.user.tourassistant.google_place.RetrofitMaps;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    BottomNavigationView bottomNavigationView;
    private Button homepageCityBt;
    private ImageView homeImage;
    double latitude=23.777176;
    double longitude=90.399452;
    String placename;
    int PLACE_PICKER_REQUEST = 1;
    private ArrayList<TopPlace> topPlaces;
    private RecyclerView topPlaceRecyclerView;
    private TopPlaceAdapter topPlaceAdapter;
    private SharedPreferences sharedPref;


    public HomeFragment() {
        // Required empty public constructor

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Tour Assistant");

        final View v = inflater.inflate(R.layout.fragment_home, container, false);
        homepageCityBt=v.findViewById(R.id.HomepageCityBt);
        topPlaceRecyclerView = v.findViewById(R.id.top_places_recyclerview);

        homeImage= v.findViewById(R.id.homeImage);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        latitude=sharedPref.getFloat("latitude",23);
        longitude=sharedPref.getFloat("longitude",90);
        placename=sharedPref.getString("placeName","Dhaka");

        topPlaces = getTopPlaceInfo(placename, latitude,longitude);
        topPlaceAdapter = new TopPlaceAdapter(getActivity(),topPlaces);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        topPlaceRecyclerView.setLayoutManager(mLayoutManager);
        topPlaceRecyclerView.setItemAnimator(new DefaultItemAnimator());
        topPlaceRecyclerView.setAdapter(topPlaceAdapter);
        homepageCityBt.setText(placename);
        homepageCityBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAutocompleteActivity();

            }
        });

    }

    public ArrayList<TopPlace> getTopPlaceInfo(String placename, double latitude, double longitude){
        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("placeName",placename);
        editor.putFloat("latitude", (float) latitude);
        editor.putFloat("longitude", (float) longitude);
        editor.commit();
        editor.apply();

        final ArrayList<TopPlace>topPlaces = new ArrayList<>();
        this.latitude=sharedPref.getFloat("latitude",23);
        this.longitude=sharedPref.getFloat("longitude",90);
        placename=sharedPref.getString("placeName","Dhaka");
        homepageCityBt.setText(placename);



        String url = "https://maps.googleapis.com/maps/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitMaps service = retrofit.create(RetrofitMaps.class);
        Call<Example> call=service.getNearbyPlaces("restaurant",latitude+","+longitude,10000);
        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {

                for (int i = 0; i < response.body().getResults().size(); i++) {
                    if(i==6)
                        break;

                    if(response.body().getResults().get(i).getPhotos().size()>0){
                        String placeName;
                        if(response.body().getResults().get(i).getName()==null){
                            placeName="unknown";
                        }
                        else{
                            placeName = response.body().getResults().get(i).getName();
                        }
                        String photoUrl = String.format("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + response.body().getResults().get(i).getPhotos().get(0).getPhotoReference() + "&key=AIzaSyDNeai9oJaTgOWuvohReDXGkSkDvKcBWsc");
                        topPlaces.add(new TopPlace(placeName,photoUrl));
                        topPlaceAdapter.notifyDataSetChanged();
                    }
                }
                if(response.body().getResults().size()>0) {
                    String photoUrl2 = String.format("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + response.body().getResults().get(0).getPhotos().get(0).getPhotoReference() + "&key=AIzaSyDNeai9oJaTgOWuvohReDXGkSkDvKcBWsc");
                    Glide.with(getContext()).load(photoUrl2).asBitmap()
                            .error(R.drawable.coxbazer).centerCrop().into(homeImage);


                }
            }
            @Override
            public void onFailure(Call<Example> call, Throwable t) {

            }
        });

        return topPlaces;
    }


    private void openAutocompleteActivity() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            Intent intent =builder.build(getActivity());
            startActivityForResult(intent, PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {

                Place place = PlacePicker.getPlace(data, getActivity());
                String toastMsg = String.format("Place: %s", place.getName());

                sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("placeName",place.getName().toString());
                editor.putFloat("latitude", (float) place.getLatLng().latitude);
                editor.putFloat("longitude", (float) place.getLatLng().longitude);
                editor.commit();
                editor.apply();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                HomeFragment homeFragment = new HomeFragment();
                ft.replace(R.id.homeFragmentView,homeFragment);
         //       ft.addToBackStack(null);
                ft.commit();

            }
        }



    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                AuthUI.getInstance().signOut(getActivity());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }




}