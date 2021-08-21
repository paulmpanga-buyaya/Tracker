package sales.pipesandconduit.tracker;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
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

public class ProductsFragment extends Fragment implements ProductsAdapter.ProductsAdapterListener {
    private static final String TAG = ProductsFragment.class.getSimpleName();

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dbRef = database.getReference("products");
    private RecyclerView recyclerView;
    private List<Product> productList;
    private ProductsAdapter mAdapter;
    private SearchView searchView;

    Toolbar toolbar;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_products, container, false);

        getActivity().setTitle("Products");

        recyclerView = root.findViewById(R.id.productsRecyclerView);
        searchView = root.findViewById(R.id.searchProducts);
        searchProducts(searchView);
        productList = new ArrayList<>();
        mAdapter = new ProductsAdapter(getActivity(), productList, this::onProductSelected);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL, 36));
        recyclerView.setAdapter(mAdapter);
        fetchProducts();

        return root;
    }

    public void searchProducts(SearchView sv) {
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
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

    private void fetchProducts(){
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot unit : snapshot.getChildren()){
                    Product product = unit.getValue(Product.class);
                    //productList.add(product);
                    //Log.d("TAG", productList);
                    Product pdt = new Product(product.getName(), product.getCode(),product.price);
                    productList.add(product);
                    mAdapter.notifyDataSetChanged();
                    //Toast.makeText(getActivity(), product.getName() + " " + product.getCode(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void cashIn(){
       /* AddProductzFragment fragment = new AddProductzFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container,fragment)
                .commitNow();*/
    }

    @Override
    public void onProductSelected(Product product) {
        //Toast.makeText(getActivity(), "Selected: " + product.getProductName(), Toast.LENGTH_LONG).show();
        //showDetailsDialog(product);
        //changePriceDialog(product);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(ProductsFragment.this)
                        .navigate(R.id.action_ProductsFragment_to_AddProductFragment);
            }
        });
    }
}