package com.example.user.tourassistant.page_fragment;



import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.tourassistant.GeofenceTransitionsIntentService;
import com.example.user.tourassistant.R;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlartFragment extends Fragment  {
    TextView position,geoLat,geoLon,radius;
    int PLACE_PICKER_REQUEST = 1;
    private SharedPreferences sharedPref;
    View view;
    Button geoSwitch,GeoChange;

    private GoogleApiClient googleApiClient;
    private GeofencingClient mGeofencingClient;
    private ArrayList<Geofence> geofences;
    private PendingIntent mGeofencePendingIntent;
    public AlartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Taget place");

        view=inflater.inflate(R.layout.fragment_alart, container, false);
        position=view.findViewById(R.id.Position);
        geoLat=view.findViewById(R.id.GeoLat);
        geoLon=view.findViewById(R.id.GeoLon);
        radius=view.findViewById(R.id.GeoRadius);
        GeoChange=view.findViewById(R.id.GeoChange);

        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        position.setText(""+sharedPref.getString("position","mirpur"));
        geoLat.setText(""+sharedPref.getFloat("geoLat",25));
        geoLon.setText(""+sharedPref.getFloat("geoLon",90));
        radius.setText(""+sharedPref.getFloat("radius",100));



        mGeofencePendingIntent = null;
        geofences = new ArrayList<>();
        mGeofencingClient = LocationServices.getGeofencingClient(getActivity());

        GeoChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


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
        });


        return  view;
    }



    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, getContext());
                String toastMsg = String.format("Place: %s", place.getName());

                sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("position",place.getName().toString());
                editor.putFloat("geoLat", (float) place.getLatLng().latitude);
                editor.putFloat("geoLon", (float) place.getLatLng().longitude);
                editor.putFloat("radius", 100);
                editor.commit();
                editor.apply();

                position.setText(""+sharedPref.getString("position","mirpur"));
                geoLat.setText(""+sharedPref.getFloat("geoLat",25));
                geoLon.setText(""+sharedPref.getFloat("geoLon",90));
                radius.setText(""+sharedPref.getFloat("radius",100));


                mGeofencePendingIntent = null;
                geofences = new ArrayList<>();
                mGeofencingClient = LocationServices.getGeofencingClient(getActivity());
                Geofence mGeofence = new Geofence.Builder()
                        .setRequestId("BITM")
                        .setCircularRegion(sharedPref.getFloat("geoLat",25),sharedPref.getFloat("geoLon",90),sharedPref.getFloat("radius",100))
                        .setExpirationDuration(12 * 60 * 60 * 1000)
                        .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                                Geofence.GEOFENCE_TRANSITION_EXIT)
                        .build();

                geofences.add(mGeofence);

                if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},0);
                    return;
                }
                mGeofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getActivity(), "geofence area added", Toast.LENGTH_SHORT).show();
                            }
                        });



            }
        }
    }


    private PendingIntent getGeofencePendingIntent() {

        if(mGeofencePendingIntent != null){
            return mGeofencePendingIntent;
        }else{
            Intent intent = new Intent(getActivity(),GeofenceTransitionsIntentService.class);
            mGeofencePendingIntent = PendingIntent.getService(getActivity(),
                    0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            return mGeofencePendingIntent;
        }
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(geofences);
        return builder.build();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }




}
