package com.example.user.tourassistant.google_place;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.*;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.user.tourassistant.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AllmapActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    String type;
    double latitude;
    double longitude;
    private int PROXIMITY_RADIUS = 5000;
    Marker mCurrLocationMarker;



    public AllmapActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allmap);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapall);
        mapFragment.getMapAsync(this);

        Bundle bundle=getIntent().getExtras();
        type=bundle.getString("type");
        latitude=bundle.getDouble("latitude");
        longitude=bundle.getDouble("longitude");

        build_retrofit_and_get_response(type,latitude,longitude);
    }


    private void build_retrofit_and_get_response(String type, double latitude,double longitude) {


        String url = "https://maps.googleapis.com/maps/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitMaps service = retrofit.create(RetrofitMaps.class);


        Call<Example> call=service.getNearbyPlaces(type,latitude+","+longitude,PROXIMITY_RADIUS);
        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                try {
                    mMap.clear();
                    // This loop will go through all the results and add marker on each location.
                    Toast.makeText(getApplication(),""+response.body().getResults().size(),Toast.LENGTH_LONG).show();
                    for (int i = 0; i < response.body().getResults().size(); i++) {
                        Double lat = response.body().getResults().get(i).getGeometry().getLocation().getLat();
                        Double lng = response.body().getResults().get(i).getGeometry().getLocation().getLng();
                        String placeName = response.body().getResults().get(i).getName();
                        String vicinity = response.body().getResults().get(i).getVicinity();
                        MarkerOptions markerOptions = new MarkerOptions();
                        LatLng latLng = new LatLng(lat, lng);
                        // Position of Marker on Map
                        markerOptions.position(latLng);
                        // Adding Title to the Marker
                        markerOptions.title(placeName + " : " + vicinity);
                        // Adding Marker to the Camera.
                        Marker m = mMap.addMarker(markerOptions);
                        // Adding colour to the marker
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                        // move map camera
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
                        // mMap.animateCamera(CameraUpdateFactory.);
                    }
                } catch (Exception e) {
                    Log.d("onResponse", "There is an error");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {

            }
        });

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                mMap.setMyLocationEnabled(true);
            }
        } else {

            mMap.setMyLocationEnabled(true);
        }

    }


}
