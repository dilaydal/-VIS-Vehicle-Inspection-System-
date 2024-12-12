package users;

public class Admin extends User{
    private int adminID;

    public Admin(String username, String password){
        setUsername(username);
        setPassword(password);
    }

    public int getAdminID() {
        return adminID;
    }

    public void setAdminID(int adminID) {
        this.adminID = adminID;
    }
}
