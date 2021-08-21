package sales.pipesandconduit.tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ExpenseActivity extends AppCompatActivity implements ExpenseAdapter.ExpenseAdapterListener  {
    private static final String TAG = ExpenseActivity.class.getSimpleName();

    private RecyclerView recyclerView;
    private List<Expense> expenseList;
    private ExpenseAdapter mAdapter;
    private SearchView searchView;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dbRefCategories = database.getReference("categories");
    private DatabaseReference dbRefExpenses = database.getReference("expenses");


    LinearLayout goToCategories,addExpense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView = findViewById(R.id.expensesRecyclerView);
        searchView = findViewById(R.id.searchExpenses);
        searchExpenses(searchView);
        expenseList = new ArrayList<>();
        mAdapter = new ExpenseAdapter(ExpenseActivity.this, expenseList, this::onExpenseSelected);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ExpenseActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(ExpenseActivity.this, DividerItemDecoration.VERTICAL, 36));
        recyclerView.setAdapter(mAdapter);
        fetchExpenses();

        goToCategories = findViewById(R.id.goToCategories);
        goToCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CategoriesActivity.class));
            }
        });

        addExpense = findViewById(R.id.addExpense);
        addExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddExpenseActivity.class));
            }
        });
    }

    @Override
    public void onExpenseSelected(Expense expense) {
        //Toast.makeText(getActivity(), "Selected: " + product.getProductName(), Toast.LENGTH_LONG).show();
        //showDetailsDialog(product);
        //changePriceDialog(product);
    }

    public void searchExpenses(SearchView sv) {
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

    private void fetchExpenses(){
        dbRefExpenses.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot unit : snapshot.getChildren()){
                    Expense expense = unit.getValue(Expense.class);
                    expenseList.add(expense);
                    mAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
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