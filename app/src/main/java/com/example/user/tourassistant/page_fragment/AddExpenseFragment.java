package com.example.user.tourassistant.page_fragment;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.user.tourassistant.R;
import com.example.user.tourassistant.firebase.Expense;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddExpenseFragment extends Fragment {


    private View view;
    private EditText expense_DetailsET,expense_AmountET;
    private ImageButton closeButton;
    private Button expense_SaveBT;
    FirebaseDatabase database;
    DatabaseReference myExpenseRef;
    String eventkey;
    public AddExpenseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Add Expense");
                database = FirebaseDatabase.getInstance();
        eventkey = getArguments().getString("eventkey");
        database = FirebaseDatabase.getInstance();
        view=inflater.inflate(R.layout.fragment_add_expense, container, false);
        expense_DetailsET=view.findViewById(R.id.Expense_DetailsET);
        expense_AmountET=view.findViewById(R.id.Expense_AmountET);
        closeButton=view.findViewById(R.id.backFromAddExpse);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });


        // Inflate the layout for this fragment
        return view;
    }




public void goBack(){

    FragmentManager fm4 = getActivity().getSupportFragmentManager();
    FragmentTransaction ft4 = fm4.beginTransaction();
    ExpenseListFragment expenseFragment = new ExpenseListFragment();
    Bundle sendKey = new Bundle();
    sendKey.putString("eventkey", eventkey);
    expenseFragment.setArguments(sendKey);
    ft4.replace(R.id.homeFragmentView,expenseFragment);
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
        inflater.inflate(R.menu.save_manu, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveEvent:
                FirebaseDatabase.getInstance().getReference().child("TExpense").child(eventkey).addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                                myExpenseRef = FirebaseDatabase.getInstance().getReference().child("TExpense").child(eventkey).push();
                                myExpenseRef.child("EDetails").setValue(expense_DetailsET.getText().toString());
                                myExpenseRef.child("Expense").setValue(Double.parseDouble(expense_AmountET.getText().toString()));
                                myExpenseRef.child("Date").setValue(date);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        }
                );

                goBack();




                //goBack();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }





}
