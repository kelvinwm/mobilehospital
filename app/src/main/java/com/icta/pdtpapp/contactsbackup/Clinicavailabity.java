package com.icta.pdtpapp.contactsbackup;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.icta.pdtpapp.contactsbackup.Adapters.ClinicavailabilityAdapter;
import com.icta.pdtpapp.contactsbackup.Adapters.ListclinicsAdapter;
import com.icta.pdtpapp.contactsbackup.Setters.Bookedtime;
import com.icta.pdtpapp.contactsbackup.Setters.ClinicAvailability;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class Clinicavailabity extends AppCompatActivity {

    String clinname,editthisclinickey;
    public static  String  dateString;
    TextView clinicname,todaytxt;
    ImageView prev,next;

    List<ClinicAvailability> availableclinics;
    private DatabaseReference dbaseref;
    ProgressBar loaderavailability;
    RecyclerView recyclerClinicavailability;
    ClinicavailabilityAdapter clinicavailabilityAdapter;

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Name = "nameKey";
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinicavailabity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //initialize dates staff here
        final Calendar c = Calendar.getInstance();
        final Calendar ctoday = Calendar.getInstance();
        final Calendar dayofweek = Calendar.getInstance();
        final SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        final SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        final String todayDate = df.format(ctoday.getTime());
        final String daytoday = sdf.format(c.getTime());

        // got sharred preferences
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        editor.clear();

        // initialize textviews and imageview for dates update
        todaytxt=findViewById(R.id.today);
        prev=findViewById(R.id.prev);
        next=findViewById(R.id.next);
        todaytxt.setText("Today");
        editor.putString(Name, daytoday+" "+todayDate);
        editor.commit();
        // bar to include date navigator
        Intent intent = getIntent();
        clinicname=findViewById(R.id.clinicname);
        availableclinics = new ArrayList<>();
        clinicavailabilityAdapter = new ClinicavailabilityAdapter(Clinicavailabity.this, availableclinics);
        recyclerClinicavailability =findViewById(R.id.recyclerClinicavailability);
        if(!(intent.getStringExtra("clinicname")==null)){

            clinname = intent.getStringExtra("clinicname");
            clinicname.setText(clinname);
        }else {
            clinname = intent.getStringExtra("editthisclinic");
            editthisclinickey = intent.getStringExtra("editthisclinickey");
            clinicname.setText(clinname);
        }


        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(Clinicavailabity.this, 1);
        recyclerClinicavailability.setLayoutManager(mLayoutManager);
        recyclerClinicavailability.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(5), true));
        recyclerClinicavailability.setItemAnimator(new DefaultItemAnimator());

        dbaseref= FirebaseDatabase.getInstance().getReference().child("availableclinics");
        loaderavailability = findViewById(R.id.loaderavailability);
        recyclerClinicavailability.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerClinicavailability, new CustomItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                //to book clicked clinic
                final ClinicAvailability clickedclinic=availableclinics.get(position);

//                if(clickedclinic.getAvailability().equals("Booked")){
//                    return;
//                }
                AlertDialog.Builder builder;
                Bookedtime bookedtime;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(Clinicavailabity.this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(Clinicavailabity.this);
                }
                builder.setTitle("Book Clinic")
                        .setMessage("Do want you to book "+clinname+ todaytxt.getText().toString() +" at "+clickedclinic.getAvailtime()+" ?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue
                                String keyval=dbaseref.push().getKey();
                                if(todaytxt.getText().toString().equals("Today")){
                                    Bookedtime bookedtime = new Bookedtime(clickedclinic.getKeyvalue(),daytoday+" "+todayDate);
                                    dbaseref.child("Bookedtimes").child(keyval).setValue(bookedtime);
                                }else{
                                    Bookedtime bookedtime = new Bookedtime(clickedclinic.getKeyvalue(),todaytxt.getText().toString());
                                    dbaseref.child("Bookedtimes").child(keyval).setValue(bookedtime);
                                }

                                finish();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));


        /// GET PREV AND NEXT DATES

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c.add(Calendar.DATE, -1);
                dayofweek.set(Calendar.WEEK_OF_MONTH, -1);
                String prevdate = df.format(c.getTime());
                String day = sdf.format(c.getTime());
                Date todaydate= null;
                Date predates= null;
                try {
                    todaydate = df.parse(todayDate);
                    predates = df.parse(prevdate);

                    if(todaydate.equals(predates)|| predates.before(todaydate)) {

                        Log.v("PREVIOUS DATE : ", prevdate);
                        todaytxt.setText("Today");
                        editor.clear();
                        editor.putString(Name, daytoday+" "+todayDate);;
                        editor.commit();
                        fetchData();
                        prev.setEnabled(false);
                        prev.setVisibility(View.GONE);
                    }else {
//                        STRING DATE TO TIMESTAMP
                        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                        Date date = (Date)formatter.parse(prevdate);
//                        date.getTime()- will generate the time stamp
                        todaytxt.setText(day+" "+prevdate);
                        editor.clear();
                        editor.putString(Name, todaytxt.getText().toString());;
                        editor.commit();
                        fetchData();

                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dayofweek.set(Calendar.WEEK_OF_MONTH, 1);
                c.add(Calendar.DATE, 1);
                String formattedDate = df.format(c.getTime());
                String day = sdf.format(c.getTime());
                Log.v("NEXT DATE : ", formattedDate);
                todaytxt.setText(day+" "+formattedDate);
                prev.setEnabled(true);
                prev.setVisibility(View.VISIBLE);
                editor.clear();
                editor.putString(Name, todaytxt.getText().toString());;
                editor.commit();
                fetchData();
            }
        });

        fetchData();
//        getDate();

    }


    private void fetchData() {

        dbaseref.child("Person").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

        // search for the other means of retrieval of data frm database
                if(dataSnapshot.exists())
                {
        availableclinics.clear();

        for(DataSnapshot postSnapShot:dataSnapshot.getChildren())
        {
            ClinicAvailability getClinic=postSnapShot.getValue(ClinicAvailability.class);
                availableclinics.add(
                    new ClinicAvailability(
                            getClinic.getAvailtime(),
                            getClinic.getKeyvalue()));
                clinicavailabilityAdapter.notifyDataSetChanged();

           // Toast.makeText(Clinicavailabity.this,getClinic.getAvailtime(),Toast.LENGTH_LONG).show();
                    }
                }
                loaderavailability.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(getApplicationContext(),"error updating",Toast.LENGTH_LONG).show();
            }
        });

        recyclerClinicavailability.setAdapter(clinicavailabilityAdapter);
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
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

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
