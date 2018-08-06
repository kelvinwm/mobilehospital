package com.icta.pdtpapp.contactsbackup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LaunchActivity extends AppCompatActivity {

    Button signinbtn, signupbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        signinbtn=findViewById(R.id.signinbtn);
        signupbtn=findViewById(R.id.signupbtn);

        signinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signinclass=new Intent(LaunchActivity.this, MyBookings.class);
                startActivity(signinclass);
                finish();
            }
        });
        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signupclass=new Intent(LaunchActivity.this,Signup.class);
                startActivity(signupclass);
                finish();
            }
        });
    }
}
