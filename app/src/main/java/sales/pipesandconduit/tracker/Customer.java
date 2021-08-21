package sales.pipesandconduit.tracker;

public class Customer {

    private String key;
    private String businessName;
    private String name;
    private String phoneOrEmail;
    private String tin;

    public Customer() {
    }

    public Customer(String key, String businessName, String name, String phoneOrEmail, String tin) {
        this.key = key;
        this.businessName = businessName;
        this.name = name;
        this.phoneOrEmail = phoneOrEmail;
        this.tin = tin;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneOrEmail() {
        return phoneOrEmail;
    }

    public void setPhoneOrEmail(String phoneOrEmail) {
        this.phoneOrEmail = phoneOrEmail;
    }

    public String getTin() {
        return tin;
    }

    public void setTin(String tin) {
        this.tin = tin;
    }
}
