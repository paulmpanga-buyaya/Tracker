package sales.pipesandconduit.tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AddExpenseActivity extends BaseActivity implements CategoryAdapter.CategoryAdapterListener  {
    private static final String TAG = AddExpenseActivity.class.getSimpleName();
    MaterialButton button_cancel, button_submit;
    TextInputEditText et_date, et_amount, et_description, et_category;
    EditText et_category_id;

    private RecyclerView categoriesRecycler;
    private List<Category> categoryList;
    private CategoryAdapter mAdapter;
    private SearchView searchView;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dbRefCategories = database.getReference("categories");
    private DatabaseReference dbRefExpenses = database.getReference("expenses");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

       // button_cancel.setOnClickListener(v -> goBack());
        button_submit = findViewById(R.id.button_submit);

        et_date = findViewById(R.id.et_date);
        et_amount = findViewById(R.id.et_amount);
        et_description = findViewById(R.id.et_description);
        et_category = findViewById(R.id.et_category);
        et_category_id = findViewById(R.id.et_category_id);
        setDate(et_date,AddExpenseActivity.this);
        String str_date = et_date.getText().toString();

        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* HashMap<String, String> user = sessionManager.getUserDetails();
                String username = user.get(sessionManager.key_username);
                String name = user.get(sessionManager.key_name);
                String createdBy = "Name: " + name + " , Username: " + username;*/

                String date = et_date.getText().toString();
                String amountStr = et_amount.getText().toString();
                String description = et_description.getText().toString();
                //Toast.makeText(getActivity(), String.valueOf(dateLong(date)),Toast.LENGTH_LONG).show();
                String tid = UUID.randomUUID().toString();
                String category_name = et_category.getText().toString();
                String cid = et_category_id.getText().toString();
                int amount = Integer.parseInt(amountStr);

                String key = dbRefExpenses.push().getKey();
                //Expense(String eid, String category_name, String cid, String description, int amount, long date)
                Expense expense = new Expense(key,category_name,cid,description,amount,dateLong(date));
                dbRefExpenses.child(key).setValue(expense);
                dbRefExpenses.orderByChild("eid").equalTo(key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){

                            startActivity(new Intent(getApplicationContext(), ExpenseActivity.class));
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

            }
        });

        et_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(AddExpenseActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.select_dialog,null);
                builder.setView(dialogView);
                final AlertDialog dialog = builder.create();
                //  mDatabase = FirebaseDatabase.getInstance().getReference();
                dialog.show();
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int displayWidth = displayMetrics.widthPixels;
                int displayHeight = displayMetrics.heightPixels;
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                layoutParams.copyFrom(dialog.getWindow().getAttributes());
                int dialogWindowWidth = (int) (displayWidth);
                //int dialogWindowHeight = (int) (displayHeight * 0.5f);
                int dialogWindowHeight = (int) (displayHeight * 0.7f);
                layoutParams.width = dialogWindowWidth;
                layoutParams.height = dialogWindowHeight;
                dialog.getWindow().setAttributes(layoutParams);

                categoriesRecycler = dialog.findViewById(R.id.recycler);
                searchView = dialog.findViewById(R.id.search);
                searchCategories(searchView);
                categoryList = new ArrayList<>();
                mAdapter = new CategoryAdapter(AddExpenseActivity.this, categoryList, this::onCategorySelected);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(AddExpenseActivity.this);
                categoriesRecycler.setLayoutManager(mLayoutManager);
                categoriesRecycler.setItemAnimator(new DefaultItemAnimator());
                categoriesRecycler.addItemDecoration(new MyDividerItemDecoration(AddExpenseActivity.this, DividerItemDecoration.VERTICAL, 36));
                categoriesRecycler.setAdapter(mAdapter);
                fetchCategories();

                et_category.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        dialog.dismiss();
                    }
                });

            }

            private void onCategorySelected(Category category) {
                //Toast.makeText(getActivity(), "Selected: " + category.getCategoryName(), Toast.LENGTH_LONG).show();
                et_category.setText(category.getName());
                et_category_id.setText(String.valueOf(category.getKey()));
            }

        });

    }


    private void fetchCategories(){
        dbRefCategories.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot unit : snapshot.getChildren()){
                    Category product = unit.getValue(Category.class);
                    Category pdt = new Category(product.getKey(),product.getName());
                    categoryList.add(product);
                    mAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void searchCategories(SearchView sv) {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
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

    @Override
    public void onCategorySelected(Category category) {
        Toast.makeText(AddExpenseActivity.this, "Selected: " + category.getName(), Toast.LENGTH_LONG).show();
        //showDetailsDialog(product);
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