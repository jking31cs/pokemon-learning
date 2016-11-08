package com.jking31cs;

import java.util.UUID;

/**
 * Created by jking31 on 11/8/16.
 */
public class IdGenerator {
    public static String randomId() {
        return UUID.randomUUID().toString();
    }
}
