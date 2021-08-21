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

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.MyViewHolder> implements Filterable {

    private Context context;
    private List<Product> productList;
    private List<Product> productListFiltered;
    private ProductsAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, price, code;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            code = view.findViewById(R.id.code);
            price = view.findViewById(R.id.price);

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
                               bundle.putString("name",productListFiltered.get(getAdapterPosition()).getName());
                               bundle.putString("price",String.valueOf(productListFiltered.get(getAdapterPosition()).getPrice()));
                               bundle.putString("code",productListFiltered.get(getAdapterPosition()).getCode());

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


    public ProductsAdapter(Context context, List<Product> productList, ProductsAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.productList = productList;
        this.productListFiltered = productList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_row_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Product product = productListFiltered.get(position);
        holder.name.setText(product.getName());
        holder.code.setText(product.getCode());
        holder.price.setText("Price : " + " " +String.valueOf(product.getPrice()));
       /* Glide.with(context)
                .load(contact.getImage())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.thumbnail);*/
    }

    @Override
    public int getItemCount() {
        return productListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    productListFiltered = productList;
                } else {
                    List<Product> filteredList = new ArrayList<>();
                    for (Product row : productList) {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getCode().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    productListFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = productListFiltered;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                productListFiltered = (ArrayList<Product>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ProductsAdapterListener {
        void onProductSelected(Product product);
    }

}
