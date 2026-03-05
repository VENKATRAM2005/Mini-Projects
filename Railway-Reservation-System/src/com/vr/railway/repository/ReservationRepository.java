package com.venkatram.oibsip.task1;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * ReservationRepository
 * Handles saving/loading reservations using a simple text format.
 */
public class ReservationRepository {

    // Main storage file (GUI wants a readable .txt file)
    private final File storage = new File("reservations.txt");

    // In-memory cache
    private Map<String, Passenger> data = new HashMap<>();

    public ReservationRepository() {
        load();
    }

    // Returns filename for GUI display
    public String storageFilename() {
        return storage.getName();
    }

    // -------- Load from file --------
    public Map<String, Passenger> loadAll() {
        return new HashMap<>(data);
    }

    // -------- Save a reservation --------
    public void save(String pnr, Passenger p) {
        data.put(pnr, p);
        persist();
    }

    // -------- Delete reservation --------
    public void delete(String pnr) {
        data.remove(pnr);
        persist();
    }

    // -------- Internal: load from text file --------
    private void load() {
        if (!storage.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(storage))) {
            String line;
            data.clear();

            while ((line = br.readLine()) != null) {
                // Format:
                // PNR|name|age|from|to|train

                String[] parts = line.split("\\|");
                if (parts.length != 6) continue;

                String pnr = parts[0];
                String name = parts[1];
                int age = Integer.parseInt(parts[2]);
                String from = parts[3];
                String to = parts[4];
                String train = parts[5];

                Passenger p = new Passenger(name, age, from, to, train);
                data.put(pnr, p);
            }
        } catch (Exception e) {
            System.err.println("Failed loading reservations: " + e.getMessage());
        }
    }

    // -------- Internal: save to text file --------
    private void persist() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(storage))) {
            // Write all reservations in text format
            for (Map.Entry<String, Passenger> e : data.entrySet()) {
                Passenger p = e.getValue();
                pw.println(
                        e.getKey() + "|" +
                        p.getName() + "|" +
                        p.getAge() + "|" +
                        p.getFrom() + "|" +
                        p.getTo() + "|" +
                        p.getTrain()
                );
            }
        } catch (Exception e) {
            System.err.println("Failed saving reservations: " + e.getMessage());
        }
    }
}
