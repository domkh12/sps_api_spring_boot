package edu.npic.sps.util;

import java.util.Set;
import java.util.regex.Pattern;

public class RegexUtil {

    public static final Pattern IPV4_PATTERN = Pattern.compile(
            "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$"
    );

    public static final Pattern IPV6_PATTERN = Pattern.compile(
            "^([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$|" + // Full IPv6
                    "^::1$|" + // Loopback
                    "^::$|" + // All zeros
                    "^([0-9a-fA-F]{1,4}:){1,7}:$|" + // Compressed
                    "^:([0-9a-fA-F]{1,4}:){1,6}[0-9a-fA-F]{1,4}$|" + // Compressed with ending
                    "^([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}$" // Mixed compression
    );
    // Set of private/internal IP ranges to filter out or handle specially

    public static final Set<String> PRIVATE_IP_PREFIXES = Set.of(
            "10.", "172.16.", "172.17.", "172.18.", "172.19.", "172.20.",
            "172.21.", "172.22.", "172.23.", "172.24.", "172.25.", "172.26.",
            "172.27.", "172.28.", "172.29.", "172.30.", "172.31.", "192.168.",
            "127.", "169.254.", "::1", "fc00:", "fd00:", "fe80:"
    );

}
