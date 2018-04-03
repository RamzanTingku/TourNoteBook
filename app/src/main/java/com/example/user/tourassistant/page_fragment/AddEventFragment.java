package com.example.user.tourassistant.page_fragment;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.user.tourassistant.R;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddEventFragment extends Fragment {
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    private EditText destinationEt,fromDateEt,toDateET,budgetEt;
    private ImageButton closeButton;
    private DatePicker fromDateEtd,toDateETd;
    private Button eventSavebt;
    private View view;
    FirebaseDatabase database;
    DatabaseReference myEventRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    private int year, month, day, hour, minute, year2, month2, day2, hour2, minute2;
    private Calendar calendar, calendar2;
    private boolean is24Hours, is24Hours2;
    private SimpleDateFormat sdfDate, sdfDate2, sdfTime;
    private final long threeDays = 3 * 24 * 60 * 60 * 1000;


    public AddEventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Add Event");
        mAuth = FirebaseAuth.getInstance();



        database = FirebaseDatabase.getInstance();
        view=inflater.inflate(R.layout.fragment_add_event, container, false);
        destinationEt=view.findViewById(R.id.destinationEt);
        fromDateEt=view.findViewById(R.id.fromDateEt);
        toDateET=view.findViewById(R.id.toDateET);
        budgetEt=view.findViewById(R.id.budgetEt);
        destinationEt.setInputType(InputType.TYPE_NULL);
        destinationEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAutocompleteActivity();
            }
        });
        destinationEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                }
            }
        });

        closeButton=view.findViewById(R.id.backFromAddEvent);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm3 = getActivity().getSupportFragmentManager();
                FragmentTransaction ft3 = fm3.beginTransaction();
                EventFragment eventFragment = new EventFragment();
                ft3.replace(R.id.homeFragmentView,eventFragment);
                //ft3.addToBackStack(null);
                ft3.commit();
            }
        });



        calendar = Calendar.getInstance(Locale.getDefault());
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR);
        minute = calendar.get(Calendar.MINUTE);
        is24Hours = false;

        calendar2 = Calendar.getInstance(Locale.getDefault());
        year2 = calendar2.get(Calendar.YEAR);
        month2 = calendar2.get(Calendar.MONTH);
        day2 = calendar2.get(Calendar.DAY_OF_MONTH);
        hour2 = calendar2.get(Calendar.HOUR);
        minute2 = calendar2.get(Calendar.MINUTE);
        is24Hours2 = false;
        fromDateEt.setInputType(InputType.TYPE_NULL);
        fromDateEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), datelistener, year, month, day);
                datePickerDialog.getDatePicker().setMinDate(Calendar.getInstance(Locale.getDefault()).getTimeInMillis());
//                datePickerDialog.getDatePicker().setMaxDate(Calendar.getInstance(Locale.getDefault()).getTimeInMillis() + threeDays);
                datePickerDialog.show();
            }
        });
        toDateET.setInputType(InputType.TYPE_NULL);
        toDateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog2 = new DatePickerDialog(getContext(), datelistener2, year2, month2, day2);
                datePickerDialog2.getDatePicker().setMinDate(Calendar.getInstance(Locale.getDefault()).getTimeInMillis() + threeDays);
//                datePickerDialog2.getDatePicker().setMaxDate(Calendar.getInstance(Locale.getDefault()).getTimeInMillis() + threeDays + threeDays);
                datePickerDialog2.show();
            }
        });



        // Inflate the layout for this fragment
        return view;
    }


    private DatePickerDialog.OnDateSetListener datelistener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            //btnDatePicker.setText(day + "/" + (month+1) + "/" + year);

            /*calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, (month));
            calendar.set(Calendar.DAY_OF_MONTH, day);*/
            calendar.set(year, month, day);

            sdfDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            fromDateEt.setText(sdfDate.format(calendar.getTime()));

        }
    };

    private DatePickerDialog.OnDateSetListener datelistener2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            //btnDatePicker.setText(day + "/" + (month+1) + "/" + year);

            /*calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, (month));
            calendar.set(Calendar.DAY_OF_MONTH, day);*/
            calendar2.set(year,month,day);

            sdfDate2 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            toDateET.setText(sdfDate2.format(calendar2.getTime()));
        }
    };

    private void openAutocompleteActivity() {
        try {

            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .build(getActivity());
            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
        } catch (GooglePlayServicesRepairableException e) {

            GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), e.getConnectionStatusCode(),
                    0 /* requestCode */).show();
        } catch (GooglePlayServicesNotAvailableException e) {

            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);


        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check that the result was from the autocomplete widget.
        if (requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                destinationEt.setText(place.getName());

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);

            } else if (resultCode == RESULT_CANCELED) {

            }
        }
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
                myEventRef = database.getReference("Events").child(mAuth.getCurrentUser().getUid()).push();
                myEventRef.child("Destination").setValue(destinationEt.getText().toString());
                myEventRef.child("FromDate").setValue(fromDateEt.getText().toString());
                myEventRef.child("ToDate").setValue(toDateET.getText().toString());
                myEventRef.child("budget").setValue(Double.parseDouble(budgetEt.getText().toString()));

                FragmentManager fm3 = getActivity().getSupportFragmentManager();
                FragmentTransaction ft3 = fm3.beginTransaction();
                EventFragment eventFragment = new EventFragment();
                ft3.replace(R.id.homeFragmentView,eventFragment);
                //ft3.addToBackStack(null);
                ft3.commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
