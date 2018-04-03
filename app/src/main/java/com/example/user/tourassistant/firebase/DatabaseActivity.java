package com.example.user.tourassistant.firebase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.user.tourassistant.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DatabaseActivity extends AppCompatActivity {
    // Write a message to the database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("message");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);



    }



    public void addData(View view) {


        myRef.push().setValue("Hello, World!");

    }

    public void getData(View view) {


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String value = dataSnapshot.getValue(String.class);
                Toast.makeText(getApplication(),value,Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCancelled(DatabaseError error) {


            }
        });
    }
}
