package sales.pipesandconduit.tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class EditItemActivity extends BaseActivity {

    TextView nameText, stockText;
    EditText priceText, quantityText, totalText, dateText;
    MaterialButton button_submit;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dbRefSales = database.getReference("sales");
    private DatabaseReference dbRefSaleItems = database.getReference("saleItems");

    private ArrayList<HashMap<String, Object>> customItemArrayList;
    private ListView currentSaleList;
    private CustomItemAdapter customItemAdapter;

    private DBHelper dbHelper;
    private int maxRowId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        dbHelper = new DBHelper(EditItemActivity.this);

        maxRowId = dbHelper.getMaxId();
        if (maxRowId == 0) {
            maxRowId = 1000;}
        customItemArrayList = dbHelper.getAllItems();

        String sid = getIntent().getExtras().getString("sid");
        String name = getIntent().getExtras().getString("name");
        String code = getIntent().getExtras().getString("code");
        int stock = getIntent().getExtras().getInt("stock");
        int price = getIntent().getExtras().getInt("price");

        dateText = findViewById(R.id.date);
        setDate(dateText,EditItemActivity.this);
        nameText = findViewById(R.id.itemName);
        nameText.setText(name);
        stockText = findViewById(R.id.stock);
        stockText.setText("Available: " + String.valueOf(stock));
        priceText = findViewById(R.id.itemPrice);
        priceText.setText(String.valueOf(price));
        totalText = findViewById(R.id.totalAmount);
        quantityText = findViewById(R.id.itemQuantity);
        totalText.setText(String.valueOf(price));
        quantityText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String qs = quantityText.getText().toString();
                if (qs.equals("")){
                    int quantity = 0;
                    int price = getIntent().getExtras().getInt("price");
                    int total = quantity * price;
                    totalText.setText(String.valueOf(total));
                } else {
                    int quantity = Integer.parseInt(quantityText.getText().toString());
                    int price = getIntent().getExtras().getInt("price");
                    int total = quantity * price;
                    totalText.setText(String.valueOf(total));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        button_submit = findViewById(R.id.button_submit);
        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dbHelper.exists(code)){

                    String sid = getIntent().getExtras().getString("sid");
                    //Toast.makeText(getApplicationContext(),sid,Toast.LENGTH_LONG).show();
                    int entered_quantity = Integer.parseInt(quantityText.getText().toString());
                    int previous_quantity = dbHelper.getItemQuantity(code);
                    int new_quantity = entered_quantity + previous_quantity;
                    int price = Integer.parseInt(priceText.getText().toString());
                    int new_total = new_quantity * price;
                    dbHelper.updateItem(maxRowId,name,code,price,new_quantity,new_total);
                    Bundle bundle = new Bundle();
                    bundle.putString("sid",sid);
                    Intent intent = new Intent(EditItemActivity.this, NewSaleActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);

                } else {

                    int price = Integer.parseInt(priceText.getText().toString());
                    int quantity = Integer.parseInt(quantityText.getText().toString());
                    int total = price * quantity;
                    HashMap<String, Object> hashMap = new HashMap<String, Object>();
                    hashMap.put("id", maxRowId);
                    hashMap.put("item", name);
                    hashMap.put("sku", code);
                    hashMap.put("price", price);
                    hashMap.put("quantity", quantity);
                    hashMap.put("total", total);
                    maxRowId++;
                    customItemArrayList.add(hashMap);
                    dbHelper.insertItem(maxRowId,name,code,price, quantity, total);
                    Bundle bundle = new Bundle();
                    bundle.putString("sid",sid);
                    Intent intent = new Intent(EditItemActivity.this, NewSaleActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);

                }

              /*  String sid = getIntent().getExtras().getString("sid");
                String name = getIntent().getExtras().getString("name");
                String code = getIntent().getExtras().getString("code");
                int stock = getIntent().getExtras().getInt("stock");
                int price = getIntent().getExtras().getInt("price");
*/

             /*   if (dbHelper.exists(code)){

                    //int price = Integer.parseInt(priceText.getText().toString());
                    int quantity = Integer.parseInt(quantityText.getText().toString());
                    int previous_quantity = dbHelper.getItemQuantity(code);
                    int new_quantity = quantity + previous_quantity;
                    int total = price * quantity;
                    int previous_total = dbHelper.getItemTotal(code);
                    int new_total = total + previous_total;
                    dbHelper.updateItem(maxRowId,name,code,price,new_quantity,new_total);
                    Bundle bundle = new Bundle();
                    bundle.putString("sid",sid);
                    Intent intent = new Intent(EditItemActivity.this, NewSaleActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);

                } else {

                    int price = Integer.parseInt(priceText.getText().toString());
                    int quantity = Integer.parseInt(quantityText.getText().toString());
                    int total = price * quantity;
                    HashMap<String, Object> hashMap = new HashMap<String, Object>();
                    hashMap.put("id", maxRowId);
                    hashMap.put("item", name);
                    hashMap.put("code", code);
                    hashMap.put("price", price);
                    hashMap.put("quantity", quantity);
                    hashMap.put("total", total);
                    maxRowId++;
                    customItemArrayList.add(hashMap);
                    //insertItem(int id, String item_name, String sku, int price, int quantity, int total)
                    dbHelper.insertItem(maxRowId, name,code,price, quantity, total);
                    Bundle bundle = new Bundle();
                    bundle.putString("sid",sid);
                    Intent intent = new Intent(EditItemActivity.this, NewSaleActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);

                }*/
            }
        });
    }

    public void addSale(String sid, String name, String customerKey, String code, int quantity, int price, int total,long date){

        //Sale(String sid, int total, long date)
        Sale sale = new Sale(sid,customerKey,total,date);
        dbRefSales.child(sid).setValue(sale);
        dbRefSales.orderByChild("sid").equalTo(sid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String key = dbRefSaleItems.push().getKey();
                    //SaleItem(String key, String sid, String name, String code, int quantity, int price, int total)
                    SaleItem saleItem = new SaleItem(key,sid,name,code,quantity,price,total);
                    dbRefSaleItems.child(key).setValue(saleItem);
                    dbRefSaleItems.orderByChild("key").equalTo(key).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){

                                Bundle bundle = new Bundle();
                                bundle.putString("sid",sid);
                                Intent intent = new Intent(EditItemActivity.this, NewSaleActivity.class);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                //startActivity(new Intent(getApplicationContext(), NewSaleActivity.class));
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });

                    //startActivity(new Intent(getApplicationContext(), ExpenseActivity.class));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),NewSaleActivity.class));
        finish();
        return;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}