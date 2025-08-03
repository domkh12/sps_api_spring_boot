package edu.npic.sps.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

    public static String getDeviceTypeName(String userAgent) {
        if (userAgent == null || userAgent.isEmpty()) {
            return "Unknown";
        }
        String[] tokens = userAgent.split(" ");
        for (String token : tokens) {
            if (token.contains("Mobile")) {
                return "Mobile";
            } else if (token.contains("Tablet")) {
                return "Tablet";
            } else if (token.contains("TV")) {
                return "TV";
            } else if (token.contains("Console")) {
                return "Console";
            } else if (token.contains("Watch")) {
                return "Watch";
            } else if (token.contains("Desktop")) {
                return "Desktop";
            } else if (token.contains("Bot")) {
                return "Bot";
            } else if (token.contains("Spider")) {
                return "Spider";
            } else if (token.contains("Android")) {
                return "Mobile";
            } else if (token.contains("Linux")) {
                return "Desktop";
            } else if (token.contains("iPad")) {
                return "Tablet";
            }
        }
        return "Desktop";
    }

    public static String getOsName(String userAgent) {
        if (userAgent == null || userAgent.isEmpty()) {
            return "Unknown";
        }
        // Windows detection
        if (userAgent.contains("Windows NT 10.0")) {
            return "Windows 10";
        } else if (userAgent.contains("Windows NT 6.3")) {
            return "Windows 8.1";
        } else if (userAgent.contains("Windows NT 6.2")) {
            return "Windows 8";
        } else if (userAgent.contains("Windows NT 6.1")) {
            return "Windows 7";
        } else if (userAgent.contains("Windows NT")) {
            return "Windows";
        }

        // macOS detection
        if (userAgent.contains("Mac OS X")) {
            // Extract version like "10_15_7" and convert to "10.15.7"
            Pattern macPattern = Pattern.compile("Mac OS X (\\d+)[_\\.]?(\\d+)?[_\\.]?(\\d+)?");
            Matcher matcher = macPattern.matcher(userAgent);
            if (matcher.find()) {
                String major = matcher.group(1);
                String minor = matcher.group(2);
                String patch = matcher.group(3);

                StringBuilder version = new StringBuilder("Mac OS ").append(major);
                if (minor != null) {
                    version.append(".").append(minor);
                    if (patch != null) {
                        version.append(".").append(patch);
                    }
                }
                return version.toString();
            }
            return "macOS";
        }

        // iOS detection
        if (userAgent.contains("iPhone OS") || userAgent.contains("CPU OS")) {
            Pattern iosPattern = Pattern.compile("OS (\\d+)[_\\.]?(\\d+)?[_\\.]?(\\d+)?");
            Matcher matcher = iosPattern.matcher(userAgent);
            if (matcher.find()) {
                String major = matcher.group(1);
                String minor = matcher.group(2);
                String patch = matcher.group(3);

                StringBuilder version = new StringBuilder("iOS ").append(major);
                if (minor != null) {
                    version.append(".").append(minor);
                    if (patch != null) {
                        version.append(".").append(patch);
                    }
                }
                return version.toString();
            }
            return "iOS";
        }

        // Android detection
        if (userAgent.contains("Android")) {
            Pattern androidPattern = Pattern.compile("Android (\\d+)[\\.]?(\\d+)?[\\.]?(\\d+)?");
            Matcher matcher = androidPattern.matcher(userAgent);
            if (matcher.find()) {
                String major = matcher.group(1);
                String minor = matcher.group(2);
                String patch = matcher.group(3);

                StringBuilder version = new StringBuilder("Android ").append(major);
                if (minor != null) {
                    version.append(".").append(minor);
                    if (patch != null) {
                        version.append(".").append(patch);
                    }
                }
                return version.toString();
            }
            return "Android";
        }

        // Linux detection
        if (userAgent.contains("Linux")) {
            return "Linux";
        }

        return "Unknown";
    }

    public static String getBrowserName(String userAgent) {
        if (userAgent == null || userAgent.isEmpty()) {
            return "Unknown";
        }
        if (userAgent.contains("Edg/")) {
            return "Edge";
        }
        else if (userAgent.contains("OPR/") || userAgent.contains("Opera/")) {
            return "Opera";
        }
        else if (userAgent.contains("Chrome/")) {
            return "Chrome";
        }
        else if (userAgent.contains("Firefox/")) {
            return "Firefox";
        }
        else if (userAgent.contains("Safari/") && !userAgent.contains("Chrome/")) {
            return "Safari";
        }
        else if (userAgent.contains("MSIE") || userAgent.contains("Trident/")) {
            return "Internet Explorer";
        }
        else {
            return "Unknown";
        }
    }

}
