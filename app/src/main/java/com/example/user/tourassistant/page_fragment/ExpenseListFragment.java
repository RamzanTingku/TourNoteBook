package com.example.user.tourassistant.page_fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.tourassistant.R;
import com.example.user.tourassistant.firebase.Events;
import com.example.user.tourassistant.firebase.Expense;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExpenseListFragment extends Fragment {


    private View view;

    private RecyclerView eventListView;
    private TextView cost,budget;

    private ProgressBar budgetPercent;
    private FirebaseRecyclerAdapter<Expense,EventViewHolder1> firebaseRecyclerAdapter;
    private DatabaseReference eventDatabase,userDatabase;
    FirebaseDatabase database;
    DatabaseReference myEventRef;
    private FirebaseAuth mAuth;

    private ImageButton backToEvent;

    String eventkey;
    String keyid;
    public ExpenseListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Expense List");
        // Inflate the layout for this fragment
        eventkey = getArguments().getString("eventkey");

        database = FirebaseDatabase.getInstance();


        view=inflater.inflate(R.layout.fragment_expense_list, container, false);

        eventDatabase = FirebaseDatabase.getInstance().getReference().child("TExpense").child(eventkey);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setReverseLayout(true);
        llm.setStackFromEnd(true);

        eventListView = (RecyclerView)view.findViewById(R.id.recyclerview_expenseList);
        eventListView.setHasFixedSize(true);
        eventListView.setLayoutManager(llm);

        cost=view.findViewById(R.id.cost);
        budget=view.findViewById(R.id.budget);
        backToEvent=view.findViewById(R.id.backToEvent);







        // Inflate the layout for this fragment
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        backToEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fme = getActivity().getSupportFragmentManager();
                FragmentTransaction fte = fme.beginTransaction();
                EventFragment eventFragment = new EventFragment();
                fte.replace(R.id.homeFragmentView,eventFragment);
                fte.addToBackStack(null);
                fte.commit();

            }
        });

    }


    @Override
    public void onStart() {
        super.onStart();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Expense, EventViewHolder1>(
                Expense.class, R.layout.expense_row, EventViewHolder1.class, eventDatabase
        ) {
            @Override
            protected void populateViewHolder(EventViewHolder1 viewHolder, Expense model, int position) {

                viewHolder.setEventName(model.getEDetails());
                viewHolder.setFromDate(""+model.getExpense());
                viewHolder.setToDate(model.getDate());

            }
        };
        eventListView.setAdapter(firebaseRecyclerAdapter);


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("TExpense").child(eventkey);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                double Costsum= 0.0;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //Getting the data from snapshot
                    Expense expense = postSnapshot.getValue(Expense.class);
                    Costsum=Costsum+expense.getExpense();
                    //Adding it to a stringString expenses = "Amount: "+dogExpenditure.getAmount()+"\nReason for Use: "+dogExpenditure.getItem()+"\n\n";


                }
                cost.setText(""+Costsum);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mAuth = FirebaseAuth.getInstance();
        final FirebaseDatabase databaseBuget = FirebaseDatabase.getInstance();
        DatabaseReference refb = databaseBuget.getReference().child("Events").child(mAuth.getCurrentUser().getUid()).child(eventkey);
        refb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               Events events=dataSnapshot.getValue(Events.class);

                budget.setText(""+events.getBudget());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





    }

    public static class EventViewHolder1 extends RecyclerView.ViewHolder{

        View mView;
        public EventViewHolder1(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setEventName(String value){
            TextView eventName=mView.findViewById(R.id.ExpenseDetailsTV);
            eventName.setText(value);

        }

        public void setFromDate(String value){
            TextView fromDate=mView.findViewById(R.id.ExpenseTV);
            fromDate.setText(value);
        }

        public void setToDate(String value){
            TextView toDate=mView.findViewById(R.id.ExpenseDateTv);
            toDate.setText(value);
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
        inflater.inflate(R.menu.event_manu, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addEvent:
                FragmentManager fm5 = getActivity().getSupportFragmentManager();
                FragmentTransaction ft5 = fm5.beginTransaction();
                AddExpenseFragment addExpenseFragment = new AddExpenseFragment();
                Bundle sendKey = new Bundle();
                sendKey.putString("eventkey", eventkey);
                addExpenseFragment.setArguments(sendKey);
                //ft5.addToBackStack("ExpenseListFragment");
                ft5.replace(R.id.homeFragmentView,addExpenseFragment);
                ft5.commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }






}
