package edu.npic.sps.util;

import java.util.UUID;

public class ApiKeyGenerate {
    public static String generateApiKey() {
        return UUID.randomUUID().toString();
    }
}
