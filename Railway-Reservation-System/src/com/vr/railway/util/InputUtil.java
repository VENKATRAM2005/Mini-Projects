/**
 * Input utilities for clean input handling
 * Author: Venkatram
 */
package com.venkatram.oibsip.task1;
import java.util.Scanner;

public class InputUtil {

    private static final Scanner scanner = new Scanner(System.in);

    public static int readInt(String msg) {
        while (true) {
            try {
                System.out.print(msg);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (Exception e) {
                System.out.println("Enter a valid number.");
            }
        }
    }

    public static String readNonEmptyString(String msg) {
        while (true) {
            System.out.print(msg);
            String s = scanner.nextLine().trim();
            if (s.isEmpty()) {
                System.out.println("Value cannot be empty.");
            } else {
                return s;
            }
        }
    }
}
