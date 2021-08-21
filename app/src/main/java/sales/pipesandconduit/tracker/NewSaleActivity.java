package sales.pipesandconduit.tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NewSaleActivity extends AppCompatActivity implements InventoryAdapter.InventoryAdapterListener {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dbRefSales = database.getReference("sales");
    private DatabaseReference dbRefInventory = database.getReference("inventory");

    private RecyclerView recyclerView;
    private List<Inventory> inventoryList;
    private InventoryAdapter mAdapter;
    private SearchView searchView;

    private DBHelper dbHelper;

    TextView amount;
    LinearLayout cartSummary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_sale);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        dbHelper = new DBHelper(NewSaleActivity.this);

        amount = findViewById(R.id.amount);
        amount.setText(String.valueOf(String.format("%,d",dbHelper.getTotal())));
        String sid = getIntent().getExtras().getString("sid");
        recyclerView = findViewById(R.id.saleProductsRecyclerView);
        searchView = findViewById(R.id.searchSaleProducts);
        // searchProducts(searchView);
        inventoryList = new ArrayList<>();
        mAdapter = new InventoryAdapter(NewSaleActivity.this, inventoryList, this::onInventorySelected);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(NewSaleActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(NewSaleActivity.this, DividerItemDecoration.VERTICAL, 36));
        recyclerView.setAdapter(mAdapter);
        fetchInventory();

       // findViewById(R.id.cartSummary).setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), CartActivity.class)));
        findViewById(R.id.cartSummary).setOnClickListener(view -> goToCart());

    }

    private void fetchInventory(){
        dbRefInventory.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot unit : snapshot.getChildren()){
                    Inventory inventory = unit.getValue(Inventory.class);
                    inventoryList.add(inventory);
                    mAdapter.notifyDataSetChanged();
                    //Toast.makeText(getActivity(), product.getName() + " " + product.getCode(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void goToCart(){
        String sid = getIntent().getExtras().getString("sid");
        Bundle bundle = new Bundle();
        bundle.putString("sid",sid);
        Intent intent = new Intent(NewSaleActivity.this, CartActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onInventorySelected(Inventory inventory) {
       // Toast.makeText(NewSaleActivity.this, "Selected: " + inventory.getName(), Toast.LENGTH_LONG).show();
        String sid = getIntent().getExtras().getString("sid");
        Bundle bundle = new Bundle();
        bundle.putString("sid",sid);
        bundle.putString("code",inventory.getCode());
        bundle.putString("name",inventory.getName());
        bundle.putInt("price",inventory.getPrice());
        bundle.putInt("stock",inventory.getQuantity());
        Intent intent = new Intent(NewSaleActivity.this, EditItemActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        //showDetailsDialog(product);
        //changePriceDialog(product);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
        return;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}