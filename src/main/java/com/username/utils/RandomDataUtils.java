package com.username.utils;

import java.util.concurrent.ThreadLocalRandom;

/** Unique test data, so tests do not clash when they run again or in parallel. */
public final class RandomDataUtils {

    private static final String[] FIRST = {"Alex", "Sam", "Jordan", "Taylor", "Morgan", "Riley"};
    private static final String[] LAST = {"Smith", "Brown", "Lee", "Patel", "Garcia", "Khan"};

    private RandomDataUtils() {
    }

    public static String firstName() {
        return FIRST[ThreadLocalRandom.current().nextInt(FIRST.length)];
    }

    public static String lastName() {
        return LAST[ThreadLocalRandom.current().nextInt(LAST.length)];
    }

    /** A 6-digit postal code such as 560034. */
    public static String postalCode() {
        return String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
    }

    public static String email() {
        return "user" + System.currentTimeMillis() + "@example.com";
    }
}
