package com.example.user.tourassistant.page_fragment;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
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
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.tourassistant.firebase.Expense;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.example.user.tourassistant.R;
import com.example.user.tourassistant.firebase.Events;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventFragment extends Fragment {


    private View view;

    private RecyclerView eventListView;
    private FirebaseRecyclerAdapter<Events,EventViewHolder> firebaseRecyclerAdapter;
    private DatabaseReference eventDatabase,expenseDatabase;
    FirebaseDatabase database;
    DatabaseReference myEventRef;
    private FirebaseAuth mAuth;


    private FirebaseUser mCurrentUser;
    //String keyid;

    public EventFragment(){

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Events");
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();


        view=inflater.inflate(R.layout.fragment_event, container, false);

        eventDatabase = FirebaseDatabase.getInstance().getReference().child("Events").child(mAuth.getCurrentUser().getUid());
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setReverseLayout(true);
        llm.setStackFromEnd(true);

        eventListView = (RecyclerView)view.findViewById(R.id.recyclerview_event);
        eventListView.setHasFixedSize(true);
        eventListView.setLayoutManager(llm);



        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    public int getTotallExpense(String expenseId, int duget){


        return 70;
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Events, EventViewHolder>(
                Events.class, R.layout.event_row, EventViewHolder.class, eventDatabase
        ) {
            @Override
            protected void populateViewHolder(EventViewHolder viewHolder, Events model, int position) {
               final String keyid=getRef(position).getKey();

                viewHolder.setEventName(model.getDestination());
                viewHolder.setFromDate(model.getFromDate());
                viewHolder.setToDate(model.getToDate());
                viewHolder.setBudget(""+model.getBudget());
                viewHolder.expenseShowBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FragmentManager fm4 = getActivity().getSupportFragmentManager();
                        FragmentTransaction ft4 = fm4.beginTransaction();
                        ExpenseListFragment expenseFragment = new ExpenseListFragment();
                        Bundle sendKey = new Bundle();
                        sendKey.putString("eventkey", keyid);
                        expenseFragment.setArguments(sendKey);
                        ft4.addToBackStack("EventFragment");
                        ft4.replace(R.id.homeFragmentView,expenseFragment);
                        ft4.commit();
                    }
                });
                viewHolder.momentShowBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FragmentManager fm4 = getActivity().getSupportFragmentManager();
                        FragmentTransaction ft4 = fm4.beginTransaction();
                        MomentFragment momentFragment = new MomentFragment();
                        Bundle sendKey = new Bundle();
                        sendKey.putString("eventkey", keyid);
                        momentFragment.setArguments(sendKey);
                        ft4.addToBackStack("EventFragment");
                        ft4.replace(R.id.homeFragmentView,momentFragment);
                        ft4.commit();
                    }
                });

                viewHolder.mView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {

                        FragmentManager fm4 = getActivity().getSupportFragmentManager();
                        FragmentTransaction ft4 = fm4.beginTransaction();
                        SingleEventFragment  singleEventFragment  = new SingleEventFragment();
                        Bundle sendKey = new Bundle();
                        sendKey.putString("eventkey", keyid);
                        singleEventFragment.setArguments(sendKey);
                        ft4.addToBackStack("EventFragment");
                        ft4.replace(R.id.homeFragmentView,singleEventFragment);
                        ft4.commit();
                        return false;
                    }
                });
            }
        };
        eventListView.setAdapter(firebaseRecyclerAdapter);



    }

    public static class EventViewHolder extends RecyclerView.ViewHolder{

        View mView;
        TextView expenseShowBt;
        TextView momentShowBt;
        public EventViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            expenseShowBt=mView.findViewById(R.id.ExpenseShowBt);
            momentShowBt=mView.findViewById(R.id.MomentShowBt);
        }

        public void setEventName(String value){
            TextView eventName=mView.findViewById(R.id.eventNameRow);
            eventName.setText(value);

        }

        public void setFromDate(String value){
            TextView fromDate=mView.findViewById(R.id.fromDateRow);
            fromDate.setText("Start date : "+value);
        }

        public void setToDate(String value){
            TextView toDate=mView.findViewById(R.id.toDateRow);
            toDate.setText("End date : "+value);
        }

        public void setBudget(String value){
            TextView budget=mView.findViewById(R.id.budgetRow);
            budget.setText("Aprox buget : "+value);
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
        menu.clear();
        inflater.inflate(R.menu.event_manu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addEvent:
                FragmentManager fm3 = getActivity().getSupportFragmentManager();
                FragmentTransaction ft3 = fm3.beginTransaction();
                AddEventFragment addEventFragment = new AddEventFragment();
                ft3.addToBackStack("EventFragment");
                ft3.replace(R.id.homeFragmentView,addEventFragment);
                ft3.commit();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }





}
