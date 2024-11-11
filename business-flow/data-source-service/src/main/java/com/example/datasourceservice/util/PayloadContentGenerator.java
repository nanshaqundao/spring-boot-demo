package com.example.datasourceservice.util;

import java.nio.charset.StandardCharsets;
import java.util.Random;

public class PayloadContentGenerator {

    private static final int CONTENT_SIZE = 10_000; // 10KB approximate size

    public static String generateRandomContent() {
        Random random = new Random();
        byte[] array = new byte[CONTENT_SIZE];
        random.nextBytes(array);
        return new String(array, StandardCharsets.UTF_8);
    }
}
