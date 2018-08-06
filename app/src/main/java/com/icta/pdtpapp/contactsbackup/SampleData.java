package com.icta.pdtpapp.contactsbackup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.icta.pdtpapp.contactsbackup.Setters.ClinicAvailability;
public class SampleData extends AppCompatActivity {

    Button sendbtn;
    EditText edit1;
    private DatabaseReference rootRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_data);

        edit1=findViewById(R.id.edit1);
        sendbtn=findViewById(R.id.sendbtn);
        rootRef= FirebaseDatabase.getInstance().getReference().child("availableclinics");
        /// send infor to database
        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newPost=rootRef.child("Person").push().getKey();
                ClinicAvailability person = new ClinicAvailability(edit1.getText().toString(),newPost.toString());
                //Adding values
                rootRef.child("Person").child(newPost).setValue(person);
            }
        });
    }
}
