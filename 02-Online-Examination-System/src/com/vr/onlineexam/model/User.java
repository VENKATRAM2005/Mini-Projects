import java.io.Serializable;
import java.util.UUID;
package com.vr.onlineexam.model;
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String userId = UUID.randomUUID().toString();
    private final String username;
    private final String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }

    @Override
    public String toString() {
        return username;
    }
}

