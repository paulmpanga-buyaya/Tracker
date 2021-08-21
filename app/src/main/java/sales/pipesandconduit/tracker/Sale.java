package sales.pipesandconduit.tracker;

public class Sale {

    public String sid;
    public String customerKey;
    public int total;
    public long date;

    public Sale() {
    }

    public Sale(String sid, String customerKey, int total, long date) {
        this.sid = sid;
        this.customerKey = customerKey;
        this.total = total;
        this.date = date;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getCustomerKey() {
        return customerKey;
    }

    public void setCustomerKey(String customerKey) {
        this.customerKey = customerKey;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
