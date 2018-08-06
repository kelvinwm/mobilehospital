package com.icta.pdtpapp.contactsbackup;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.icta.pdtpapp.contactsbackup.Adapters.BookedclinicAdapter;
import com.icta.pdtpapp.contactsbackup.Adapters.ListclinicsAdapter;
import com.icta.pdtpapp.contactsbackup.Setters.Chosenclinic;
import com.icta.pdtpapp.contactsbackup.Setters.ClinicList;

import java.util.ArrayList;
import java.util.List;

public class Clinicslist extends AppCompatActivity {

    List<ClinicList> listofclinics;
    private DatabaseReference dbaseref;
    ProgressBar listloader;
    RecyclerView recyclerClinicList;
    ListclinicsAdapter listAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinicslist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listofclinics = new ArrayList<>();
        listAdapter = new ListclinicsAdapter(Clinicslist.this, listofclinics);
        recyclerClinicList =findViewById(R.id.recyclerClinicList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(Clinicslist.this, 1);
        recyclerClinicList.setLayoutManager(mLayoutManager);
        recyclerClinicList.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(5), true));
        recyclerClinicList.setItemAnimator(new DefaultItemAnimator());

        dbaseref= FirebaseDatabase.getInstance().getReference().child("Listofclinics");
        listloader = findViewById(R.id.listloader);
        recyclerClinicList.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerClinicList, new CustomItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                //to book clicked clinic
                final ClinicList clickedclinic=listofclinics.get(position);
                Intent clinicavailability= new Intent(Clinicslist.this,Clinicavailabity.class);
                clinicavailability.putExtra("clinicname", clickedclinic.getClinicname());
                startActivity(clinicavailability);
                finish();
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));

        fetchData();

    }

    private void fetchData() {

//        dbaseref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {

                // search for the other means of retrieval of data frm database
//                if(dataSnapshot.exists())
//                {
                    listofclinics.clear();
//
//                    for(DataSnapshot postSnapShot:dataSnapshot.getChildren())
//                    {
                        //ClinicList getClinic=postSnapShot.getValue(ClinicList.class);
                        listofclinics.add(
                                new ClinicList(
                                        "A&T",
                                        "Emegency clinics"));
                        listofclinics.add(
                                new ClinicList(
                                        "Cancer",
                                        "Cancer therapy"));
                        listofclinics.add(
                                new ClinicList(
                                        "Diabetes",
                                        "Diabeties clincs"));
//
//                        listAdapter.notifyDataSetChanged();
//                    }
//                }

                listloader.setVisibility(View.INVISIBLE);
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//                Toast.makeText(Clinicslist.this,"error updating",Toast.LENGTH_LONG).show();
//            }
//        });

        recyclerClinicList.setAdapter(listAdapter);
    }
    /**
     * Converting dp to pixel
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        public int spanCount;
        public int spacing;
        public boolean includeEdge;

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
