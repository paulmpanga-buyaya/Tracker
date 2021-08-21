package sales.pipesandconduit.tracker;

public class SaleItem {

    public String key;
    public String sid;
    public String name;
    public String code;
    public int quantity;
    public int price;
    public int total;

    public SaleItem() {
    }

    public SaleItem(String key, String sid, String name, String code, int quantity, int price, int total) {
        this.key = key;
        this.sid = sid;
        this.name = name;
        this.code = code;
        this.quantity = quantity;
        this.price = price;
        this.total = total;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
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

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
