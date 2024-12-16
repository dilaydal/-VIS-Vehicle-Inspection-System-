package utils;

public class AuthResult {
    //userımızın type ve ID bilgisini tutmak için yardımcı bir obje
    private String type;
    private int userId;
    private String username;

    public AuthResult(String type, int userId, String username) {
        this.type = type;
        this.userId = userId;
        this.username = username;
    }

    public String getType() {
        return type;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }
}