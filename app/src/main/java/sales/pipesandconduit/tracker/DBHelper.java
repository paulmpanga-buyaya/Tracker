package sales.pipesandconduit.tracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "merchant.db";
    public static final int DATABASE_VERSION = 1;

    public static final String sale_table_name = "sale_table";
    public static final String sale_item_id = "id";
    public static final String sale_item_name = "item";
    public static final String sale_item_sku = "sku";
    public static final String sale_item_price = "price";
    public static final String sale_item_quantity = "quantity";
    public static final String sale_amount_total = "total";

    public static final String product_table_name = "products";
    public static final String product_name = "name";
    public static final String product_code = "code";
    public static final String product_price = "price";

    //Customer(String key, String businessName, String name, String phoneOrEmail, String tin)
    public static final String customer_table_name = "customers";
    public static final String customer_key = "customerKey";
    public static final String customer_business_name = "business_name";
    public static final String customer_name = "name";
    public static final String customer_phone_or_email = "phone_or_email";
    public static final String customer_tin = "tin";


    public static final String create_sale_table_query = "create table sale_table (id INTEGER PRIMARY KEY, item text, sku text, price int,quantity int, total int)";
    private static String drop_items_sale_table = "DROP TABLE IF EXISTS sale_table";

    public static final String create_product_table = "create table products (name text, code text PRIMARY KEY, price int)";
    private static String drop_product_table = "DROP TABLE IF EXISTS products";

    public static final String create_customer_table = "create table customers (customerKey text PRIMARY KEY, business_name, name text, phone_or_email text, tin text)";
    private static String drop_customer_table = "DROP TABLE IF EXISTS customers";

    public Context context;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(create_sale_table_query);
        db.execSQL(create_product_table);
        db.execSQL(create_customer_table);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(drop_items_sale_table);
        db.execSQL(drop_product_table);
        db.execSQL(drop_customer_table);
        onCreate(db);
    }

    //adds products from firebase to excel
    //Product(String name, String code, int price)
    public boolean addProduct(String name, String code, int price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(product_name, name);
        contentValues.put(product_code, code);
        contentValues.put(product_price, price);
        db.insert(product_table_name, null, contentValues);
        return true;
    }

    public boolean addCustomer(Customer customer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(customer_key, customer.getKey());
        contentValues.put(customer_business_name, customer.getBusinessName());
        contentValues.put(customer_name, customer.getName());
        contentValues.put(customer_phone_or_email, customer.getPhoneOrEmail());
        contentValues.put(customer_tin, customer.getTin());
        db.insert(customer_table_name, null, contentValues);
        return true;
    }

    public String getCustomerKey() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor data =  db.rawQuery("SELECT " + DBHelper.customer_key + " FROM " + DBHelper.customer_table_name, null);//product_category_table_name
        String key = "";
        if(data.getCount() == 1) {
            data.moveToFirst();
            key = data.getString(data.getColumnIndexOrThrow(DBHelper.customer_key));
        }
        return key;
    }

    //adding item to sale table
    public boolean insertItem(int id, String item_name, String sku, int price, int quantity, int total) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(sale_item_id, id);
        contentValues.put(sale_item_name, item_name);
        contentValues.put(sale_item_sku, sku);
        contentValues.put(sale_item_price, price);
        contentValues.put(sale_item_quantity, quantity);
        contentValues.put(sale_amount_total, total);
        db.insert(sale_table_name, null, contentValues);
        return true;
    }

    public boolean exists(String searchItem) {
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = { sale_item_sku };
        String selection = sale_item_sku + " =?";
        String[] selectionArgs = { searchItem };
        String limit = "1";
        Cursor cursor = db.query(sale_table_name, columns, selection, selectionArgs, null, null, null, limit);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public int getIdWithSku(String sku) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor data =  db.rawQuery("SELECT " + DBHelper.sale_item_id + " FROM " + DBHelper.sale_table_name +
                " WHERE " + DBHelper.sale_item_sku + "=?", new String[]{sku});//product_category_table_name
        int id = 0;
        if(data.getCount() == 1) {
            data.moveToFirst();
            id = data.getInt(data.getColumnIndexOrThrow(DBHelper.sale_item_id));
        }
        return id;
    }

    public int getQuantityWithSku(String sku) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor data =  db.rawQuery("SELECT " + DBHelper.sale_item_quantity + " FROM " + DBHelper.sale_table_name +
                " WHERE " + DBHelper.sale_item_sku + "=?", new String[]{sku});//product_category_table_name
        int id = 0;
        if(data.getCount() == 1) {
            data.moveToFirst();
            id = data.getInt(data.getColumnIndexOrThrow(DBHelper.sale_item_quantity));
        }
        return id;
    }

    public int getItemTotal(String sku) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor data =  db.rawQuery("SELECT " + DBHelper.sale_amount_total + " FROM " + DBHelper.sale_table_name +
                " WHERE " + DBHelper.sale_item_sku + "=?", new String[]{sku});//product_category_table_name
        int id = 0;
        if(data.getCount() == 1) {
            data.moveToFirst();
            id = data.getInt(data.getColumnIndexOrThrow(DBHelper.sale_amount_total));
        }
        return id;
    }

    public int getItemQuantity(String sku) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor data =  db.rawQuery("SELECT " + DBHelper.sale_item_quantity + " FROM " + DBHelper.sale_table_name +
                " WHERE " + DBHelper.sale_item_sku + "=?", new String[]{sku});//product_category_table_name
        int id = 0;
        if(data.getCount() == 1) {
            data.moveToFirst();
            id = data.getInt(data.getColumnIndexOrThrow(DBHelper.sale_item_quantity));
        }
        return id;
    }

    public int getItemPrice(String sku) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor data =  db.rawQuery("SELECT " + DBHelper.sale_item_price + " FROM " + DBHelper.sale_table_name +
                " WHERE " + DBHelper.sale_item_sku + "=?", new String[]{sku});//product_category_table_name
        int id = 0;
        if(data.getCount() == 1) {
            data.moveToFirst();
            id = data.getInt(data.getColumnIndexOrThrow(DBHelper.sale_item_price));
        }
        return id;
    }

    public boolean updateItem(int id, String item_name, String sku, int price, int quantity, int total) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues updateItem = new ContentValues();
            updateItem.put(sale_item_name, item_name);
            updateItem.put(sale_item_sku, sku);
            updateItem.put(sale_item_price, price);
            updateItem.put(sale_item_quantity, quantity);
            updateItem.put(sale_amount_total, total);
            db.update(sale_table_name, updateItem, sale_item_id + " = '" + id + "'", null);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateItemWithSku(int id, String item_name, String sku, int price, int quantity, int total) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues updateItemWithSku = new ContentValues();
            updateItemWithSku.put(sale_item_name, item_name);
            updateItemWithSku.put(sale_item_sku, sku);
            updateItemWithSku.put(sale_item_price, price);
            updateItemWithSku.put(sale_item_quantity, quantity);
            updateItemWithSku.put(sale_amount_total, total);
            db.update(sale_table_name, updateItemWithSku, sale_item_sku + " = '" + sku + "'", null);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void deleteItem(String ID) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        //deleting row
        sqLiteDatabase.delete(sale_table_name, "sku=" + ID, null);
        sqLiteDatabase.close();
    }

    public void delete() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        //deleting rows
        sqLiteDatabase.delete(sale_table_name, null, null);
        sqLiteDatabase.delete(customer_table_name, null, null);
        sqLiteDatabase.close();
    }

    public int getMaxId() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT MAX(id) FROM " + sale_table_name, null);
        res.moveToFirst();
        return res.getInt(0);
    }

    public int numberOfRows(String table_name){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, table_name);
        return numRows;
    }

    public int transTotal(){
        SQLiteDatabase db = this.getWritableDatabase();
        int total0 = 0;
        int total1 = 0;
        Cursor cursor = db.rawQuery("SELECT SUM(" + DBHelper.sale_amount_total + ") as total FROM " + DBHelper.sale_table_name, null);
        if (cursor.moveToFirst()) {
            int total = cursor.getInt(cursor.getColumnIndex("total"));
            total =  total0;
        }

        return total0;
    }

    public int getTotal(){
        int sum = 0435;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT SUM(" + DBHelper.sale_amount_total + ") as total FROM " + DBHelper.sale_table_name, null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            sum = res.getInt(res.getColumnIndex("total"));
            res.moveToNext();
        }
        return sum;
    }

    public ArrayList<HashMap<String, Object>> getAllItems() {
        ArrayList<HashMap<String, Object>> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + sale_table_name, null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put(sale_item_id, res.getInt(0));
            hashMap.put(sale_item_name, res.getString(1));
            hashMap.put(sale_item_sku, res.getString(2));
            hashMap.put(sale_item_price, res.getInt(3));
            hashMap.put(sale_item_quantity, res.getInt(4));
            hashMap.put(sale_amount_total, res.getInt(5));
            array_list.add(hashMap);
            res.moveToNext();
        }
        return array_list;
    }

}
