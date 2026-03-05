// LeaderboardService.java
// Small file-backed leaderboard service. Stores entries as: name|score per line.
// File created in working directory as "leaderboard.db".
package com.vr.onlineexam.service;
import java.io.*;
import java.util.*;

public class LeaderboardService {

    private static final String FILE = "leaderboard.db";

    public static class Entry {
        public final String name;
        public final int score;
        public Entry(String name, int score) { this.name = name; this.score = score; }
    }

    /** Load all entries (may return empty list). */
    public static List<Entry> loadAll() {
        List<Entry> out = new ArrayList<>();
        File f = new File(FILE);
        if (!f.exists()) return out;
        try (BufferedReader r = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = r.readLine()) != null) {
                String[] p = line.split("\\|", 2);
                if (p.length == 2) {
                    try { out.add(new Entry(p[0], Integer.parseInt(p[1]))); } catch (NumberFormatException ignored) {}
                }
            }
        } catch (IOException ignored) {}
        out.sort((a,b) -> Integer.compare(b.score, a.score));
        return out;
    }

    /** Append an entry and persist. */
    public static void addEntry(String name, int score) {
        // sanitize
        if (name == null || name.trim().isEmpty()) name = "Anonymous";
        String safe = name.replace("|", " ");
        try (BufferedWriter w = new BufferedWriter(new FileWriter(FILE, true))) {
            w.write(safe + "|" + score);
            w.newLine();
        } catch (IOException ignored) {}
    }
}
