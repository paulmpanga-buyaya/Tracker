package sales.pipesandconduit.tracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.MyViewHolder> implements Filterable {

    private Context context;
    private List<Inventory> inventoryList;
    private List<Inventory> inventoryListFiltered;
    private InventoryAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, price, code;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            code = view.findViewById(R.id.code);
            price = view.findViewById(R.id.price);

            if (context instanceof NewSaleActivity) {

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Toast.makeText(context, "This is new sale",Toast.LENGTH_LONG).show();
                        listener.onInventorySelected(inventoryListFiltered.get(getAdapterPosition()));
                    }
                });
            } else {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // send selected contact in callback
                        //listener.onProductSelected(productListFiltered.get(getAdapterPosition()));
                        PopupMenu popup = new PopupMenu(context, view);
                        //Inflating the Popup using xml file
                        popup.getMenuInflater().inflate(R.menu.product_popup_menu, popup.getMenu());
                        //registering popup with OnMenuItemClickListener
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem item) {
                                //Toast.makeText(context,"You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                                if ((item.getTitle()).equals("Add stock")){
                                    Bundle bundle = new Bundle();
                                    // bundle.putString("name",productListFiltered.get(getAdapterPosition()).getName());
                                    // bundle.putString("price",String.valueOf(productListFiltered.get(getAdapterPosition()).getPrice()));
                                    // bundle.putString("code",productListFiltered.get(getAdapterPosition()).getCode());

                               /* Intent intent = new Intent(context, StockInActivity.class);
                                intent.putExtras(bundle);
                                context.startActivity(intent);*/
                                }
                                return true;
                            }
                        });

                        popup.show();//showing popup menu*/
                    }
                });
            }
        }
    }


    public InventoryAdapter(Context context, List<Inventory> inventoryList, InventoryAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.inventoryList = inventoryList;
        this.inventoryListFiltered = inventoryList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_row_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Inventory inventory = inventoryListFiltered.get(position);
        holder.name.setText(inventory.getName());
        holder.code.setText(inventory.getCode());
        holder.price.setText("Quantity : " + " " +String.valueOf(inventory.getQuantity()));
       /* Glide.with(context)
                .load(contact.getImage())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.thumbnail);*/
    }

    @Override
    public int getItemCount() {
        return inventoryListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    inventoryListFiltered = inventoryList;
                } else {
                    List<Inventory> filteredList = new ArrayList<>();
                    for (Inventory row : inventoryList) {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getCode().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    inventoryListFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = inventoryListFiltered;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                inventoryListFiltered = (ArrayList<Inventory>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface InventoryAdapterListener {
        void onInventorySelected(Inventory inventory);
    }

}
