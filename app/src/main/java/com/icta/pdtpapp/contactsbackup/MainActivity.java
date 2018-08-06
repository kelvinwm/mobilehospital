package com.icta.pdtpapp.contactsbackup;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wafflecopter.multicontactpicker.ContactResult;
import com.wafflecopter.multicontactpicker.MultiContactPicker;

import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {

    List<Setter> productList;
    private DatabaseReference dbref;
    ProgressBar loader;
    RecyclerView recyclerView;
    Adapter adapter;
    private SearchView searchView;
    private int CONTACT_PICKER_REQUEST=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton fab =findViewById(R.id.fabsps);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new MultiContactPicker.Builder(MainActivity.this) //Activity/fragment context
                        .theme(R.style.MyCustomPickerTheme) //Optional - default: MultiContactPicker.Azure
                        .hideScrollbar(false) //Optional - default: false
                        .showTrack(true) //Optional - default: true
                        .searchIconColor(Color.WHITE) //Option - default: White
                        .setChoiceMode(MultiContactPicker.CHOICE_MODE_MULTIPLE) //Optional - default: CHOICE_MODE_MULTIPLE
                        .handleColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary)) //Optional - default: Azure Blue
                        .bubbleColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary)) //Optional - default: Azure Blue
                        .bubbleTextColor(Color.WHITE) //Optional - default: White
                        .showPickerForResult(CONTACT_PICKER_REQUEST);

            }
        });

        productList = new ArrayList<>();
        adapter = new Adapter(this, productList);
        recyclerView =findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbref= FirebaseDatabase.getInstance().getReference().child("Backedupcontacts");
        loader = findViewById(R.id.loader);


        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new CustomItemClickListener() {
            @Override
            public void onClick(View view, int position) {

                final Setter specialist=productList.get(position);

            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
        searchView = findViewById(R.id.search);
        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                adapter.getFilter().filter(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed

                adapter.getFilter().filter(query);
                return false;
            }
        });
        fetchData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CONTACT_PICKER_REQUEST){
            if(resultCode == RESULT_OK) {
                List<ContactResult> results = MultiContactPicker.obtainResult(data);
                Log.d("MyTag", results.get(0).getDisplayName());

                Toast.makeText(MainActivity.this,results.get(0).getDisplayName(),Toast.LENGTH_LONG).show();
                final long timestamp= System.currentTimeMillis();
                for (ContactResult contact : results) {
                    // process the contacts...
                    Setter newSpec= new Setter(contact.getDisplayName().toString(),contact.getPhoneNumbers().toString(),timestamp);
                    dbref.setValue(newSpec);
                }
            } else if(resultCode == RESULT_CANCELED){
                System.out.println("User closed the picker without selecting items.");
            }
        }
    }

    private void fetchData() {

        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    productList.clear();

                    for(DataSnapshot postSnapShot:dataSnapshot.getChildren())
                    {
                        Setter getSpecialist=postSnapShot.getValue(Setter.class);
                        productList.add(
                                new Setter(
                                        getSpecialist.getName(),
                                        getSpecialist.getPhone(),
                                        getSpecialist.getTimestamp()));

                        adapter.notifyDataSetChanged();
                    }
                }

                loader.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(MainActivity.this,"error updating",Toast.LENGTH_LONG).show();
            }
        });

        recyclerView.setAdapter(adapter);
    }
}
