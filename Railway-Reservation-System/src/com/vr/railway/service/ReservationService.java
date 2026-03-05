package com.venkatram.oibsip.task1;

import java.util.Map;

/**
 * ReservationService - business logic layer (works with ReservationRepository).
 * Author: Venkatram
 */
public class ReservationService {

    private final ReservationRepository repo = new ReservationRepository();

    /**
     * Create a reservation and persist using repository.save(...)
     * Ensures PNR uniqueness by checking existing records.
     */
    public String createReservation(Passenger passenger) {
        Map<String, Passenger> existing = repo.loadAll();
        String pnr;
        do {
            pnr = PnrGenerator.generate();
        } while (existing.containsKey(pnr));

        // persist this single reservation
        repo.save(pnr, passenger);
        return pnr;
    }

    /**
     * Retrieve reservation by PNR (case-insensitive).
     */
    public Passenger getReservation(String pnr) {
        if (pnr == null) return null;
        return repo.loadAll().get(pnr.toUpperCase());
    }

    /**
     * Cancel reservation: remove from repository and persist.
     */
    public boolean cancelReservation(String pnr) {
        if (pnr == null) return false;
        pnr = pnr.toUpperCase();
        Map<String, Passenger> existing = repo.loadAll();
        if (!existing.containsKey(pnr)) return false;
        repo.delete(pnr);
        return true;
    }

    /**
     * Convenience: list all reservations (used by UI).
     */
    public Map<String, Passenger> listAll() {
        return repo.loadAll();
    }

    /**
     * Convenience: get count of reservations.
     */
    public int countReservations() {
        return repo.loadAll().size();
    }
}
