// UserRegistry.java
// Simple file-backed registry for users. File: users.db
// Format: username|displayName|password
package com.vr.onlineexam.model;
import java.io.*;
import java.util.*;

public class UserRegistry {
    private static final String FILE = "users.db";
    private final Map<String, User> users = new HashMap<>();

    public static class User {
        public final String username;
        public String displayName;
        public String password;
        public User(String u, String d, String p) { username = u; displayName = d; password = p; }
    }

    public UserRegistry() { load(); }

    private void load() {
        File f = new File(FILE);
        if (!f.exists()) return;
        try (BufferedReader r = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = r.readLine()) != null) {
                String[] p = line.split("\\|", 3);
                if (p.length >= 3) {
                    users.put(p[0], new User(p[0], p[1], p[2]));
                }
            }
        } catch (IOException ignored) {}
    }

    public synchronized void save() {
        try (BufferedWriter w = new BufferedWriter(new FileWriter(FILE, false))) {
            for (User u : users.values()) {
                w.write(u.username + "|" + u.displayName + "|" + u.password);
                w.newLine();
            }
        } catch (IOException ignored) {}
    }

    public synchronized boolean register(String username, String displayName, String password) {
        if (username == null || username.isEmpty() || password == null) return false;
        if (users.containsKey(username)) return false;
        users.put(username, new User(username, displayName == null ? username : displayName, password));
        save();
        return true;
    }

    public synchronized User getUser(String username) {
        return users.get(username);
    }

    public synchronized boolean validate(String username, String password) {
        User u = users.get(username);
        return u != null && u.password != null && u.password.equals(password);
    }

    public Map<String, User> allUsers() { return Collections.unmodifiableMap(users); }
}
