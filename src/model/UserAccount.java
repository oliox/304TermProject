package model;

public class UserAccount {
    private final String username;
    private final String password_hash;

    public UserAccount(String username, String password_hash) {
        this.username = username;
        this.password_hash = password_hash;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword_hash() {
        return password_hash;
    }
}
