package com.example.user.tourassistant.page_fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.tourassistant.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

/**
 * A simple {@link Fragment} subclass.
 */
public class SingleEventFragment extends Fragment {

    private DatabaseReference mDatabase,eventDatabase,expenceDatabase,chngeEventDatabase;
    private FirebaseStorage imgeStorge;
    private FirebaseAuth mAuth;
    private String eventkey;
    boolean flag=false;

    private View view;

    private EditText eventNameRowS,fromDateRowS,toDateRowS,budgetRowS;
    private String eventName,fromDate,toDate,budget;
    public SingleEventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle("Modify event");

        view=inflater.inflate(R.layout.fragment_single_event, container, false);
        eventkey=getArguments().getString("eventkey");
        mAuth = FirebaseAuth.getInstance();

        eventNameRowS=(EditText)view.findViewById(R.id.EventNameRowS);

        eventName=eventNameRowS.getText().toString();
        // Toast.makeText(getActivity(),eventName,Toast.LENGTH_LONG).show();




        // Inflate the layout for this fragment
        return view;
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
                deleteEvent();

                return true;
            case R.id.saveEvent:
                udateEvent();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void deleteEvent(){
        Toast.makeText(getActivity(), eventkey, Toast.LENGTH_SHORT).show();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("moments").child(eventkey);
        mDatabase.removeValue();

        eventDatabase = FirebaseDatabase.getInstance().getReference().child("Events").child(mAuth.getCurrentUser().getUid()).child(eventkey);
        eventDatabase.removeValue();
        expenceDatabase = FirebaseDatabase.getInstance().getReference().child("TExpense").child(eventkey);
        expenceDatabase.removeValue();

        goToEvent();



    }

    public void udateEvent(){

        chngeEventDatabase=FirebaseDatabase.getInstance().getReference().child("Events").child(mAuth.getCurrentUser().getUid()).child(eventkey);
        chngeEventDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                eventNameRowS=(EditText)view.findViewById(R.id.EventNameRowS);
                fromDateRowS=(EditText)view.findViewById(R.id.FromDateRowS);
                toDateRowS=(EditText)view.findViewById(R.id.ToDateRowS);
                budgetRowS=(EditText)view.findViewById(R.id.BudgetRowS);

                eventName=eventNameRowS.getText().toString();
                fromDate=fromDateRowS.getText().toString();
                toDate=toDateRowS.getText().toString();
                budget=budgetRowS.getText().toString();

                if (eventName!=null && fromDate!=null && toDate!=null && budget!=null ){
                    dataSnapshot.child("Destination").getRef().setValue(eventName);
                    dataSnapshot.child("FromDate").getRef().setValue(fromDate);
                    dataSnapshot.child("ToDate").getRef().setValue(toDate);
                    dataSnapshot.child("budget").getRef().setValue(budget);

                }
                else {

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        goToEvent();


    }

    public void goToEvent(){
        FragmentManager fm3 = getActivity().getSupportFragmentManager();
        FragmentTransaction ft3 = fm3.beginTransaction();
        EventFragment eventFragment = new EventFragment();
        ft3.replace(R.id.homeFragmentView,eventFragment);
        ft3.commit();
    }



}
