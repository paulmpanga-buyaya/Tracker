package sales.pipesandconduit.tracker;

public class Inventory {

    public String key;
    public String name;
    public String code;
    public String location;
    public int quantity;
    public int price;

    public Inventory() {
    }

    public Inventory(String key, String name, String code, String location, int quantity, int price) {
        this.key = key;
        this.name = name;
        this.code = code;
        this.location = location;
        this.quantity = quantity;
        this.price = price;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
