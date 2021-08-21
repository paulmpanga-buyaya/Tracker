package sales.pipesandconduit.tracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.MyViewHolder> implements Filterable {

    private Context context;
    private List<Customer> customerList;
    private List<Customer> customerListFiltered;
    private CustomerAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, phoneOrEmail, tin, org_name;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            phoneOrEmail = view.findViewById(R.id.phoneOrEmail);
            tin = view.findViewById(R.id.tin);
            org_name = view.findViewById(R.id.org_name);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onCustomerSelected(customerListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }


    public CustomerAdapter(Context context, List<Customer> customerList, CustomerAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.customerList = customerList;
        this.customerListFiltered = customerList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customer_row_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Customer customer = customerListFiltered.get(position);
        holder.name.setText(customer.getName());
        holder.phoneOrEmail.setText(customer.getPhoneOrEmail());
        holder.tin.setText(customer.getTin());
        holder.org_name.setText(customer.getBusinessName());
    }

    @Override
    public int getItemCount() {
        return customerListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    customerListFiltered = customerList;
                } else {
                    List<Customer> filteredList = new ArrayList<>();
                    for (Customer row : customerList) {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getBusinessName().toLowerCase().contains(charString.toLowerCase()) ||
                        row.getPhoneOrEmail().toLowerCase().contains(charString.toLowerCase()) ||
                        row.getTin().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    customerListFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = customerListFiltered;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                customerListFiltered = (ArrayList<Customer>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface CustomerAdapterListener {
        void onCustomerSelected(Customer customer);
    }

}
