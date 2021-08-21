package sales.pipesandconduit.tracker;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    LinearLayout addExpense, newSale, goTransactions;
    ImageView goToMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);

       /* getSupportFragmentManager().beginTransaction()
                .replace(R.id.container,new ProductsFragment())
                .commitNow();*/

        goTransactions = findViewById(R.id.goTransactions);

        addExpense = findViewById(R.id.addExpense);
        addExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addExpense();
            }
        });
        newSale = findViewById(R.id.newSale);
        newSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newSale();
            }
        });

    }

    public void newSale(){
        String sid = UUID.randomUUID().toString();
        Bundle bundle = new Bundle();
        bundle.putString("sid",sid);
        Intent intent = new Intent(MainActivity.this, NewSaleActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        //startActivity(new Intent(getApplicationContext(), NewSaleActivity.class));
    }

    public void addExpense(){
        startActivity(new Intent(getApplicationContext(), AddExpenseActivity.class));
    }

    public void mainMenu(View view){
        PopupMenu popup = new PopupMenu(MainActivity.this,view);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.pop_main_menu, popup.getMenu());
        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                //Toast.makeText(context,"You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                if ((item.getTitle()).equals("Products")){
                   startActivity(new Intent(getApplicationContext(), ProductsActivity.class));
                } else if ((item.getTitle()).equals("Expenses")){
                    startActivity(new Intent(getApplicationContext(), ExpenseActivity.class));
                } else if ((item.getTitle()).equals("Stock")){
                    startActivity(new Intent(getApplicationContext(), StockActivity.class));
                } else if ((item.getTitle()).equals("Change price")){
                } else if ((item.getTitle()).equals("Add stock")){
                }else if ((item.getTitle()).equals("Customers")){
                    startActivity(new Intent(getApplicationContext(), CustomersActivity.class));
                }

                return true;
            }
        });

        popup.show();//showing popup menu*/
    }

    public void rangeSelection(View view){
        PopupMenu popup = new PopupMenu(MainActivity.this,view);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.pop_date_range_selection, popup.getMenu());
        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                //Toast.makeText(context,"You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();


                return true;
            }
        });

        popup.show();//showing popup menu*/

    }
}