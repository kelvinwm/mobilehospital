package com.icta.pdtpapp.contactsbackup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Signup extends AppCompatActivity {

    Button tosignin,registerbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        registerbtn=findViewById(R.id.registerbtn);
        tosignin=findViewById(R.id.tosignin);

        tosignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tosignin=new Intent(Signup.this,Signin.class);
                startActivity(tosignin);
                finish();
            }
        });
    }
}
