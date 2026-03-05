/**
 * Railway Reservation System - Task 1
 * Author: Venkatram
 */
package com.venkatram.oibsip.task1;
import java.util.Scanner;

public class ReservationApp {

    private static final Scanner scanner = new Scanner(System.in);
    private static final ReservationService service = new ReservationService();

    public static void main(String[] args) {

        System.out.println("\n=== Railway Reservation System - Venkatram ===\n");

        while (true) {
            printMenu();
            int choice = InputUtil.readInt("Enter choice: ");

            switch (choice) {
                case 1 -> createReservation();
                case 2 -> viewReservation();
                case 3 -> cancelReservation();
                case 4 -> {
                    System.out.println("Thank you for using the Reservation System.\n");
                    System.exit(0);
                }
                default -> System.out.println("Invalid option, please try again.\n");
            }
        }
    }

    private static void printMenu() {
        System.out.println("1. Book Ticket");
        System.out.println("2. View Ticket");
        System.out.println("3. Cancel Ticket");
        System.out.println("4. Exit\n");
    }

    private static void createReservation() {
        System.out.println("\n--- New Reservation ---");

        String name = InputUtil.readNonEmptyString("Passenger Name: ");
        int age = InputUtil.readInt("Age: ");
        String from = InputUtil.readNonEmptyString("From Station: ");
        String to = InputUtil.readNonEmptyString("To Station: ");
        String train = InputUtil.readNonEmptyString("Train Name: ");

        Passenger p = new Passenger(name, age, from, to, train);

        String pnr = service.createReservation(p);

        System.out.println("\nReservation Successful!");
        System.out.println("Generated PNR: " + pnr + "\n");
    }

    private static void viewReservation() {
        System.out.println("\n--- View Reservation ---");
        String pnr = InputUtil.readNonEmptyString("Enter PNR: ").toUpperCase();

        Passenger p = service.getReservation(pnr);

        if (p == null) {
            System.out.println("No reservation found for PNR: " + pnr + "\n");
            return;
        }

        System.out.println("\nReservation Details:");
        System.out.println(p);
        System.out.println();
    }

    private static void cancelReservation() {
        System.out.println("\n--- Cancel Reservation ---");
        String pnr = InputUtil.readNonEmptyString("Enter PNR: ").toUpperCase();

        if (service.cancelReservation(pnr)) {
            System.out.println("Reservation cancelled successfully.\n");
        } else {
            System.out.println("PNR not found.\n");
        }
    }
}
