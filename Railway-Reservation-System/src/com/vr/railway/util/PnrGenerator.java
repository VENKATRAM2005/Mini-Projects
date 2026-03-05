/**
 * Generates unique readable PNR codes
 * Author: Venkatram
 */
package com.venkatram.oibsip.task1;
import java.util.Random;

public class PnrGenerator {

    private static final Random rand = new Random();

    public static String generate() {
        int n = rand.nextInt(999999);
        return "RES" + String.format("%06d", n);
    }
}
