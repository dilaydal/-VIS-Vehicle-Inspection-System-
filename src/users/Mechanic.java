package users;

public class Mechanic extends User { // Capitalized class name
    private int mechanicID;
    private String fullName;

   
    public Mechanic(String username, String password, int mechanicID, String fullName) {
        setUsername(username);  
        setPassword(password); 
        this.mechanicID = mechanicID; 
        this.fullName = fullName;     
    }

    // Getters and Setters
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getMechanicID() {
        return mechanicID;
    }

    public void setMechanicID(int mechanicID) {
        this.mechanicID = mechanicID;
    }
}