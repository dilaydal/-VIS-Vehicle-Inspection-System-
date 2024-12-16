package users;

public class Customer extends User{
    private int customerID;
    private String fullName;
    
   public Customer(String username, String password, String fullName) {
        this.fullName = fullName;
        setUsername(username);
        setPassword(password);
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    
}
