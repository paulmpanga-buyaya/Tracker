package sales.pipesandconduit.tracker.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import sales.pipesandconduit.tracker.Inventory;
import sales.pipesandconduit.tracker.InventoryAdapter;
import sales.pipesandconduit.tracker.MyDividerItemDecoration;
import sales.pipesandconduit.tracker.R;

public class DashboardFragment extends Fragment implements InventoryAdapter.InventoryAdapterListener {

    private static final String TAG = DashboardFragment.class.getSimpleName();

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dbRef = database.getReference("inventory");
    private RecyclerView recyclerView;
    private List<Inventory> inventoryList;
    private InventoryAdapter mAdapter;
    private SearchView searchView;

    EditText location;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);


        recyclerView = root.findViewById(R.id.productsRecyclerView);
        searchView = root.findViewById(R.id.searchProducts);
       // searchProducts(searchView);
        inventoryList = new ArrayList<>();
        mAdapter = new InventoryAdapter(getActivity(), inventoryList, this::onInventorySelected);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL, 36));
        recyclerView.setAdapter(mAdapter);
        root.findViewById(R.id.enter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inventoryList.clear();
                EditText location = root.findViewById(R.id.location);
                String loco = location.getText().toString();
                if (loco.isEmpty()){
                    fetchInventory();
                } else {
                    fetchInventory(loco);
                }
            }
        });
        EditText location = root.findViewById(R.id.location);
        String loco = location.getText().toString();
        if (loco.isEmpty()){
            fetchInventory();
        }
        //fetchInventory();
        return root;
    }

    private void fetchInventory(String name){
        dbRef.orderByChild("location").equalTo(name).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot unit : snapshot.getChildren()){
                    Inventory inventory = unit.getValue(Inventory.class);
                    //productList.add(product);
                    //Log.d("TAG", productList);
                    //Inventory(String key, String name, String code, String location, int quantity)
                    mAdapter.notifyDataSetChanged();
                    //Toast.makeText(getActivity(), product.getName() + " " + product.getCode(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void fetchInventory(){
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot unit : snapshot.getChildren()){
                    Inventory inventory = unit.getValue(Inventory.class);
                    //productList.add(product);
                    //Log.d("TAG", productList);
                    //Inventory(String key, String name, String code, String location, int quantity)
                    mAdapter.notifyDataSetChanged();
                    //Toast.makeText(getActivity(), product.getName() + " " + product.getCode(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public void onInventorySelected(Inventory inventory) {
        //Toast.makeText(getActivity(), "Selected: " + product.getProductName(), Toast.LENGTH_LONG).show();
        //showDetailsDialog(product);
        //changePriceDialog(product);
    }


}