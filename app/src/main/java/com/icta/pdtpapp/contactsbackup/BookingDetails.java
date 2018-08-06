package com.icta.pdtpapp.contactsbackup;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class BookingDetails extends AppCompatActivity {

    String clinname,clindate,clintime,clinickey;
    TextView clinicname,clinicdate,clinictime,editclinic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Intent intent = getIntent();
        clinname = intent.getStringExtra("clincname");
        clindate = intent.getStringExtra("clinicdate");
        clintime = intent.getStringExtra("clinictime");
        clinickey = intent.getStringExtra("clinickey");

        clinicname=findViewById(R.id.clinicname);
        clinicdate=findViewById(R.id.clinicdate);
        clinictime=findViewById(R.id.clinictime);
        editclinic=findViewById(R.id.editclinic);

        clinicname.setText(clinname);
        clinicdate.setText(clindate);
        clinictime.setText(clintime);

        //when edit buttoon on the menu is clicked open
        editclinic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent bookingdetails= new Intent(BookingDetails.this,Clinicavailabity.class);
                bookingdetails.putExtra("editthisclinic", clinname);
                bookingdetails.putExtra("editthisclinickey", clinickey);
                startActivity(bookingdetails);
            }
        });

    }
}
