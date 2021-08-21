package sales.pipesandconduit.tracker;

public class Expense {

    public String eid;
    public String category_name;
    public String cid;
    public String description;
    public int amount;
    public long date;

    public Expense() {
    }

    public Expense(String eid, String category_name, String cid, String description, int amount, long date) {
        this.eid = eid;
        this.category_name = category_name;
        this.cid = cid;
        this.description = description;
        this.amount = amount;
        this.date = date;
    }

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
