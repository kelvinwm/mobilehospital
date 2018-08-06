package com.icta.pdtpapp.contactsbackup;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ProductViewHolder> implements Filterable {


    //this context we will use to inflate the layout
    private Context mCtx;
    private ContactsAdapterListener listener;
    private List<Setter> productList;
    private List<Setter> contactList;

    //getting the context and product list with constructor
    public Adapter(Context mCtx, List<Setter> productList){
        this.mCtx = mCtx;
        this.productList = productList;
        this.contactList = productList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.item, null);

        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        //getting the product of the specified position
        Setter product = productList.get(position);

        //binding the data with the viewholder views
        holder.phonenumber.setText(product.getPhone());
        holder.nameofperson.setText(product.getName());
//        holder.textViewRating.setTag(position);
//        holder.textViewRating.setRating((float) product.getRating());
//        holder.textViewRating.equals(String.valueOf(product.getRating()));
//        holder.textViewPrice.setText(product.getPhone());

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView phonenumber, nameofperson;
        CheckBox checkBox;


        public ProductViewHolder(View itemView) {
            super(itemView);
            nameofperson = itemView.findViewById(R.id.nameofperson);
            phonenumber = itemView.findViewById(R.id.number);
            checkBox = itemView.findViewById(R.id.chkme);
        }

    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    productList = contactList;
                } else {
                    List<Setter> filteredList = new ArrayList<>();
                    for (Setter row : productList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())
                                ||row.getPhone().toLowerCase().contains(charString.toLowerCase())||
                                row.getPhone().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    productList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = productList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                productList = (ArrayList<Setter>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ContactsAdapterListener {
        void onContactSelected(Setter contact);
    }
}
