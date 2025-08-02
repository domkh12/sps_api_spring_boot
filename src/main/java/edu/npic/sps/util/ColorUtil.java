package edu.npic.sps.util;

public class ColorUtil {
    public static String generateUniqueColor(String input) {
        // Method 1: Hash-based color generation
        int hash = input.hashCode();
        int r = (hash & 0xFF0000) >> 16;
        int g = (hash & 0x00FF00) >> 8;
        int b = hash & 0x0000FF;

        // Ensure colors are bright enough (optional)
        r = Math.max(r, 100);
        g = Math.max(g, 100);
        b = Math.max(b, 100);

        return String.format("#%02X%02X%02X", r, g, b);
    }
}
