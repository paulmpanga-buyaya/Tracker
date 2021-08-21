package sales.pipesandconduit.tracker;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoriesActivity extends AppCompatActivity implements CategoryAdapter.CategoryAdapterListener {
    private static final String TAG = CategoriesActivity.class.getSimpleName();

    private RecyclerView recyclerView;
    private List<Category> categoryList = new ArrayList<>();
    private CategoryAdapter mAdapter;
    private SearchView searchView;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dbRefCategories = database.getReference("categories");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView = findViewById(R.id.categoriesRecyclerView);
       // searchView = root.findViewById(R.id.searchProducts);
       // searchProducts(searchView);
        //categoryList = new ArrayList<>();
        mAdapter = new CategoryAdapter(CategoriesActivity.this, categoryList, this::onCategorySelected);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(CategoriesActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(CategoriesActivity.this, DividerItemDecoration.VERTICAL, 36));
        recyclerView.setAdapter(mAdapter);
        fetchCategories();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                addCategory();
            }
        });
    }

    private void fetchCategories(){
        categoryList.clear();
        dbRefCategories.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot unit : snapshot.getChildren()){
                    Category product = unit.getValue(Category.class);
                    categoryList.add(product);
                    mAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    public void addCategory(){
        AlertDialog.Builder builder = new AlertDialog.Builder(CategoriesActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_expense_category,null);
        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();
        dialog.show();

        Button save = dialogView.findViewById(R.id.button_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText et_name = dialogView.findViewById(R.id.et_category_name);
                String key = dbRefCategories.push().getKey();
                String name = et_name.getText().toString();
                Category category = new Category(key,name);
                dbRefCategories.child(key).setValue(category);
                //mAdapter.notifyDataSetChanged();

                dbRefCategories.orderByChild("key").equalTo(key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            fetchCategories();
                            dialog.dismiss();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

            }
        });

    }

    @Override
    public void onCategorySelected(Category category) {
        //Toast.makeText(getActivity(), "Selected: " + product.getProductName(), Toast.LENGTH_LONG).show();
        //showDetailsDialog(product);
        //changePriceDialog(product);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),ExpenseActivity.class));
        finish();
        return;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}