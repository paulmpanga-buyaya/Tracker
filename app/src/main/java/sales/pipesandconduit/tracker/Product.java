package sales.pipesandconduit.tracker;

import com.google.firebase.database.DataSnapshot;

public class Product {

    public String name;
    public String code;
    public int price;

    public Product() {
    }
    public Product(String name, String code, int price) {
        this.name = name;
        this.code = code;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
