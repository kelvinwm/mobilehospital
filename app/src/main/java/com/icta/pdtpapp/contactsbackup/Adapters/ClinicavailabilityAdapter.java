package com.icta.pdtpapp.contactsbackup.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.icta.pdtpapp.contactsbackup.Adapter;
import com.icta.pdtpapp.contactsbackup.Clinicavailabity;
import com.icta.pdtpapp.contactsbackup.R;
import com.icta.pdtpapp.contactsbackup.Setters.Bookedtime;
import com.icta.pdtpapp.contactsbackup.Setters.ClinicAvailability;

import java.util.ArrayList;
import java.util.List;

import static com.icta.pdtpapp.contactsbackup.Clinicavailabity.MyPREFERENCES;
import static com.icta.pdtpapp.contactsbackup.Clinicavailabity.Name;
import static com.icta.pdtpapp.contactsbackup.Clinicavailabity.dateString;

public class ClinicavailabilityAdapter extends RecyclerView.Adapter<ClinicavailabilityAdapter.ClinicViewHolder> implements Filterable {


    //this context we will use to inflate the layout
    private Context mCtx;
    private Adapter.ContactsAdapterListener listener;
    private List<ClinicAvailability> clinicList;
    private List<ClinicAvailability> searchList;


    //getting the context and clinic list with constructor
    public ClinicavailabilityAdapter(Context mCtx, List<ClinicAvailability> clinicList){
        this.mCtx = mCtx;
        this.clinicList = clinicList;
        this.searchList = clinicList;
    }

    @Override
    public ClinicavailabilityAdapter.ClinicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.availabiltyitem, null);

        return new ClinicavailabilityAdapter.ClinicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ClinicavailabilityAdapter.ClinicViewHolder holder, int position) {
        //getting the product of the specified position
        ClinicAvailability clinic = clinicList.get(position);
        //binding the data with the viewholder views
        holder.availtime.setText(clinic.getAvailtime());
        getDate(clinic.getKeyvalue(),holder);
    }
    @Override
    public int getItemCount() {
        return clinicList.size();
    }

    class ClinicViewHolder extends RecyclerView.ViewHolder {

        TextView availtime, availability;
        ImageButton viewthisclinic;
        public ClinicViewHolder(View itemView) {
            super(itemView);
            availtime = itemView.findViewById(R.id.availtime);
            availability = itemView.findViewById(R.id.availabilty);;
        }
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    clinicList = searchList;
                } else {
                    List<ClinicAvailability> filteredList = new ArrayList<>();
                    for (ClinicAvailability row : clinicList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getAvailability().toLowerCase().contains(charString.toLowerCase())
                                ||row.getAvailtime().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    clinicList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = clinicList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                clinicList = (ArrayList<ClinicAvailability>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ContactsAdapterListener {
        void onContactSelected(ClinicAvailability contact);
    }
    private  void getDate(final String datestring, final ClinicavailabilityAdapter.ClinicViewHolder holder){
        DatabaseReference dbaseref= FirebaseDatabase.getInstance().getReference().child("availableclinics");
        SharedPreferences pref = mCtx.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        final String appdate=pref.getString(Name, "");

        final ClinicAvailability clindate=new ClinicAvailability();
        dbaseref.child("Bookedtimes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists())
                {
                    for(DataSnapshot postSnapShot:dataSnapshot.getChildren())
                    {
                        Bookedtime getClinic=postSnapShot.getValue(Bookedtime.class);

                        if(getClinic.getKeyfrmtime().equals(datestring)&&appdate.equals(getClinic.getTimedate())){
                            holder.availability.setText("Booked");
                        }

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
