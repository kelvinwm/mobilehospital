package com.icta.pdtpapp.contactsbackup;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.icta.pdtpapp.contactsbackup.Adapters.BookedclinicAdapter;
import com.icta.pdtpapp.contactsbackup.Setters.Chosenclinic;

import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;

public class MyBookings extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    List<Chosenclinic> clinicList;
    private DatabaseReference dbaseref;
    ProgressBar clincloader;
    RecyclerView recyclerClinicView;
    BookedclinicAdapter clinicadpter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.booknewclinicfab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cliniclist=new Intent(MyBookings.this,Clinicslist.class);
                startActivity(cliniclist);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // initialization

        clinicList = new ArrayList<>();
        clinicadpter = new BookedclinicAdapter(MyBookings.this, clinicList);
        recyclerClinicView =findViewById(R.id.recyclerClinicView);
        recyclerClinicView.setHasFixedSize(true);
        recyclerClinicView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(MyBookings.this, 1);
        recyclerClinicView.setLayoutManager(mLayoutManager);
        recyclerClinicView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(5), true));
        recyclerClinicView.setItemAnimator(new DefaultItemAnimator());

        dbaseref= FirebaseDatabase.getInstance().getReference().child("Bookclinics");
        clincloader = findViewById(R.id.clinicloader);
        recyclerClinicView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerClinicView, new CustomItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                //to booking details, where they can edit bookings
                final Chosenclinic clickedclinic=clinicList.get(position);
                Intent bookingdetails= new Intent(MyBookings.this,BookingDetails.class);
                bookingdetails.putExtra("clincname", clickedclinic.getClinicname());
                bookingdetails.putExtra("clinicdate", clickedclinic.getClinicdate());
                bookingdetails.putExtra("clinictime", clickedclinic.getClinictime());
                bookingdetails.putExtra("clinickey", clickedclinic.getKeytouse());
                startActivity(bookingdetails);
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));

        fetchData();
    }

    private void fetchData() {
//
//        dbaseref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if(dataSnapshot.exists())
//                {
//                    clinicList.clear();
//
//                    for(DataSnapshot postSnapShot:dataSnapshot.getChildren())
//                    {
//                        Chosenclinic getClinic=postSnapShot.getValue(Chosenclinic.class);
//                        clinicList.add(
//                                new Chosenclinic(
//                                        getClinic.getClinicdate().toString(),
//                                        getClinic.getClinictime().toString(),
//                                        getClinic.getClinicname().toString(),
//                                        getClinic.getKeytouse().toString()));

        clinicList.add(
                new Chosenclinic(

                        "Cancer clinic",
                        "Monday 12/7/2108",
                        "8:30am",
                        "cancerclinic"));
        clinicList.add(
                new Chosenclinic(
                        "A&T clinic",
                        "Thursday 24/6/2108",
                        "10:00am",
                        "aandt"));
        clinicList.add(
                new Chosenclinic(
                        "Cancer clinic",
                        "Tuesday 1/7/2108",
                        "3:30pm",
                        "cancerclinic"));

//                        clinicadpter.notifyDataSetChanged();
//                    }
//                }
//
                clincloader.setVisibility(View.INVISIBLE);
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//                Toast.makeText(MyBookings.this,"error updating",Toast.LENGTH_LONG).show();
//            }
//        });

        recyclerClinicView.setAdapter(clinicadpter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_bookings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent cliniclist=new Intent(MyBookings.this,Clinicslist.class);
            startActivity(cliniclist);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action4\
            Intent sampledata=new Intent(MyBookings.this,SampleData.class);
            startActivity(sampledata);
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (0) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }
    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
