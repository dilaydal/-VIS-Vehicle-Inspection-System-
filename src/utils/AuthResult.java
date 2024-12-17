package utils;

import users.User;

public class AuthResult {
    private String type; 
    private User user;  

    public AuthResult(String type, User user) {
        this.type = type;
        this.user = user;
    }

    public String getType() {
        return type;
    }

    public User getUser() {
        return user;
    }
}