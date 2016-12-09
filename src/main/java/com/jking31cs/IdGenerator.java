package com.jking31cs;

import java.util.UUID;

/**
 * Used to ensure a unique uuid.
 */
public class IdGenerator {
    public static String randomId() {
        return UUID.randomUUID().toString();
    }
}
