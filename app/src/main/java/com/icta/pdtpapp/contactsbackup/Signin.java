package com.icta.pdtpapp.contactsbackup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Signin extends AppCompatActivity {

    Button loginbtn,tosignup;
    EditText username, passwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        loginbtn=findViewById(R.id.loginbtn);
        tosignup=findViewById(R.id.tosignup);
        username=findViewById(R.id.username);
        passwd=findViewById(R.id.passwd);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent tosignup= new Intent(Signin.this,MyBookings.class);
                startActivity(tosignup);
                finish();
            }
        });
        tosignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent tosignup= new Intent(Signin.this,Signup.class);
                startActivity(tosignup);
            }
        });
    }
}
