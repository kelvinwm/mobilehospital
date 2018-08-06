package com.icta.pdtpapp.contactsbackup.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import com.icta.pdtpapp.contactsbackup.Adapter;
import com.icta.pdtpapp.contactsbackup.R;
import com.icta.pdtpapp.contactsbackup.Setters.Chosenclinic;

import java.util.ArrayList;
import java.util.List;

public class BookedclinicAdapter  extends RecyclerView.Adapter<BookedclinicAdapter.ClinicViewHolder> implements Filterable {


    //this context we will use to inflate the layout
    private Context mCtx;
    private Adapter.ContactsAdapterListener listener;
    private List<Chosenclinic> clinicList;
    private List<Chosenclinic> searchList;

    //getting the context and clinic list with constructor
    public BookedclinicAdapter(Context mCtx, List<Chosenclinic> clinicList){
        this.mCtx = mCtx;
        this.clinicList = clinicList;
        this.searchList = clinicList;
    }

    @Override
    public BookedclinicAdapter.ClinicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.bookinglistiem, null);

        return new BookedclinicAdapter.ClinicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BookedclinicAdapter.ClinicViewHolder holder, int position) {
        //getting the product of the specified position
        Chosenclinic clinic = clinicList.get(position);

        //binding the data with the viewholder views
        holder.myclinic.setText(clinic.getClinicname());
        holder.mydate.setText(clinic.getClinicdate());
        holder.mytime.setText(clinic.getClinictime());
//        holder.viewthisclinic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // view this clinic in booking details class.....
//            }
//        });
//        holder.textViewRating.setTag(position);
//        holder.textViewRating.setRating((float) product.getRating());
//        holder.textViewRating.equals(String.valueOf(product.getRating()));
//        holder.textViewPrice.setText(product.getPhone());

    }

    @Override
    public int getItemCount() {
        return clinicList.size();
    }


    class ClinicViewHolder extends RecyclerView.ViewHolder {

        TextView myclinic, mydate,mytime;
        ImageButton viewthisclinic;


        public ClinicViewHolder(View itemView) {
            super(itemView);
            myclinic = itemView.findViewById(R.id.myclinic);
            mydate = itemView.findViewById(R.id.mydate);
            mytime = itemView.findViewById(R.id.mytime);
            //viewthisclinic = itemView.findViewById(R.id.viewthisclinic);
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
                    List<Chosenclinic> filteredList = new ArrayList<>();
                    for (Chosenclinic row : clinicList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getClinicname().toLowerCase().contains(charString.toLowerCase())
                                ||row.getClinicdate().toLowerCase().contains(charString.toLowerCase())||
                                row.getClinictime().contains(charSequence)) {
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
                clinicList = (ArrayList<Chosenclinic>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ContactsAdapterListener {
        void onContactSelected(Chosenclinic contact);
    }
}
