package sales.pipesandconduit.tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CartActivity extends AppCompatActivity implements CustomItemAdapter.OnListItemClick,
        CustomerAdapter.CustomerAdapterListener{

    private Menu menu;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dbRefSales = database.getReference("sales");
    private DatabaseReference dbRefInventory = database.getReference("inventory");

    private ArrayList<HashMap<String, Object>> customItemArrayList;
    private ListView currentSaleList;
    private CustomItemAdapter customItemAdapter;

    private DatabaseReference dbRef = database.getReference("customers");
    private RecyclerView recyclerView;
    private List<Customer> customerList;
    private CustomerAdapter mAdapter;
    private SearchView searchView;
    private List<Inventory> inventoryList;
    private DBHelper dbHelper;
    private int maxRowId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        dbHelper = new DBHelper(CartActivity.this);
        String sid = getIntent().getExtras().getString("sid");

        currentSaleList = (ListView) findViewById(R.id.current_sale_list);
        maxRowId = dbHelper.getMaxId();
        if (maxRowId == 0) {
            maxRowId = 1000;}
        //customItemArrayList = dbHelper.getAllItems();
        loadCart();
        fillAdapter();
        findViewById(R.id.addItems).setOnClickListener(view -> addItems());
    }

    @Override
    public void onItemClick(int position) {
        editItemDialog(customItemArrayList.get(position), position);
    }

    public void addCustomerDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View editItemDialogView = inflater.inflate(R.layout.select_customer_dialog_layout,null);
        builder.setView(editItemDialogView);
        final AlertDialog editItemDialog = builder.create();
        editItemDialog.show();

        recyclerView = editItemDialogView.findViewById(R.id.customersRecycler);
        searchView = editItemDialogView.findViewById(R.id.searchCustomers);
        searchCustomers(searchView);
        customerList = new ArrayList<>();
        mAdapter = new CustomerAdapter(getApplicationContext(), customerList, this::onCustomerSelected);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL, 36));
        recyclerView.setAdapter(mAdapter);
        fetchCustomers();

       /* String sid = getIntent().getExtras().getString("sid");
        Bundle bundle = new Bundle();
        bundle.putString("sid",sid);
        bundle.putString("activity","cart");
        Intent intent = new Intent(CartActivity.this, CustomersActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);*/

    }

    public void searchCustomers(SearchView sv) {
        SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
        sv.setMaxWidth(Integer.MAX_VALUE);
        // listening to search query text change
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                mAdapter.getFilter().filter(query);
                return false;
            }
        });
    }

    private void fetchCustomers(){
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot unit : snapshot.getChildren()){
                    Customer customer = unit.getValue(Customer.class);
                    // Product pdt = new Product(product.getName(), product.getCode(),product.price);
                    customerList.add(customer);
                    mAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public void onCustomerSelected(Customer customer) {
        Toast.makeText(CartActivity.this, "Selected: " + customer.getKey(), Toast.LENGTH_LONG).show();
        dbHelper.addCustomer(customer);
        menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_baseline_person_added_24));
        //showDetailsDialog(product);
        //changePriceDialog(product);
    }

    public void loadCart(){
        customItemArrayList = dbHelper.getAllItems();
    }
    @SuppressLint("NewApi")
    public void editItemDialog(final HashMap<String, Object> hashMap, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View editItemDialogView = inflater.inflate(R.layout.cart_item_layout,null);
        builder.setView(editItemDialogView);
        final AlertDialog editItemDialog = builder.create();
        editItemDialog.show();

        TextView itemName = editItemDialogView.findViewById(R.id.itemName);
        String name = hashMap.get("item").toString();
        String sku = hashMap.get("sku").toString();
        itemName.setText(name);

        TextView stock = editItemDialogView.findViewById(R.id.stock);
        //stock.setText(String.valueOf(inventory.getQuantity()));

        EditText itemQuantity = editItemDialogView.findViewById(R.id.itemQuantity);
        itemQuantity.setText(hashMap.get("quantity").toString());

        EditText itemPrice = editItemDialogView.findViewById(R.id.itemPrice);
        itemPrice.setText(hashMap.get("price").toString());

        EditText itemTotal = editItemDialogView.findViewById(R.id.totalAmount);
        itemTotal.setText(hashMap.get("total").toString());


        MaterialButton deleteItem = editItemDialogView.findViewById(R.id.deleteItem);
        deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.deleteItem(hashMap.get("sku").toString());
                String sid = getIntent().getExtras().getString("sid");
                Bundle bundle = new Bundle();
                bundle.putString("sid",sid);
                Intent intent = new Intent(CartActivity.this, CartActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        MaterialButton updatedItem = editItemDialogView.findViewById(R.id.updatedItem);
        updatedItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sid = getIntent().getExtras().getString("sid");
                //Toast.makeText(getApplicationContext(),sid,Toast.LENGTH_LONG).show();
                int entered_quantity = Integer.parseInt(itemQuantity.getText().toString());
                int price = Integer.parseInt(hashMap.get("price").toString());
                int new_total = entered_quantity * price;
                dbHelper.updateItem(maxRowId,name,sku,price,entered_quantity,new_total);
                Bundle bundle = new Bundle();
                bundle.putString("sid",sid);
                Intent intent = new Intent(CartActivity.this, CartActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

       /* TextView title = editItemDialogView.findViewById(R.id.dialogTitle);
        title.setText("Editing item..");

        final EditText fieldName = editItemDialogView.findViewById(R.id.fieldName);
        fieldName.setText(hashMap.get("item").toString());

        final EditText fieldQuantity = editItemDialogView.findViewById(R.id.fieldQuantity);
        fieldQuantity.setText(hashMap.get("quantity").toString());

        final EditText fieldPrice = editItemDialogView.findViewById(R.id.fieldPrice);
        fieldPrice.setText(hashMap.get("price").toString());

        final EditText fieldSku = editItemDialogView.findViewById(R.id.fieldSku);
        fieldSku.setText(hashMap.get("sku").toString());

        Button delete = editItemDialogView.findViewById(R.id.buttonDelete);
        delete.setVisibility(View.VISIBLE);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String sku = fieldSku.getText().toString();
                dbHelper.deleteItem(dbHelper.getIdWithSku(sku));
                customItemArrayList.remove(position);
                editItemDialog.dismiss();
                //startActivity(new Intent(getApplicationContext(), SalesActivity.class));
            }
        });

        editItemDialogView.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editItemDialog.dismiss();
            }
        });

        editItemDialogView.findViewById(R.id.buttonAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = Integer.parseInt(fieldQuantity.getText().toString());
                int price = Integer.parseInt(fieldPrice.getText().toString());
                int total = price * quantity;
                int id = Integer.parseInt(customItemArrayList.get(position).get("id").toString());
                String name = hashMap.get("item").toString();
                String sku = hashMap.get("sku").toString();
                customItemArrayList.get(position).put("item", fieldName.getText().toString());
                customItemArrayList.get(position).put("sku", fieldSku.getText().toString());
                customItemArrayList.get(position).put("quantity", String.valueOf(quantity));
                customItemArrayList.get(position).put("price", String.valueOf(price));
                customItemArrayList.get(position).put("total", String.valueOf(total));
                customItemAdapter.notifyDataSetChanged();
                sumListValue();
                if (quantity == 0){
                    dbHelper.deleteItem(maxRowId);
                    customItemArrayList.remove(position);
                    editItemDialog.dismiss();
                }else if (quantity != 0){
                    dbHelper.updateItem(id,name,sku,price,quantity,total);
                    editItemDialog.dismiss();
                }
            }
        });*/

    }

    private void fillAdapter() {
        customItemAdapter = new CustomItemAdapter(getApplicationContext(), customItemArrayList, CartActivity.this);
        currentSaleList.setAdapter(customItemAdapter);
    }

    public void addItems(){
        String sid = getIntent().getExtras().getString("sid");
        Bundle bundle = new Bundle();
        bundle.putString("sid",sid);
        Intent intent = new Intent(CartActivity.this, NewSaleActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cart_menu, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_customer_to_cart){
            //Toast.makeText(getApplicationContext(), "Add Customer",Toast.LENGTH_LONG).show();
            if ((dbHelper.numberOfRows(DBHelper.customer_table_name) == 0)){
                addCustomerDialog();
            } else {
                Toast.makeText(getApplicationContext(), dbHelper.getCustomerKey(), Toast.LENGTH_LONG).show();
            }
        }
        if (id == R.id.clear) {
            dbHelper.delete();
            int rows = dbHelper.numberOfRows(DBHelper.sale_table_name);
            if (rows == 0){
                startActivity(new Intent(getApplicationContext(), NewSaleActivity.class));
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}