package com.neo.tx.enums;

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public enum RiskMerchant {
    GAMBLING, CRYPTO, ADULT;

    private static final Set<String> VALUES =
            Arrays.stream(values())
                    .map(Enum::name)
                    .collect(Collectors.toUnmodifiableSet());

    public static boolean contains(String value) {
        return value != null && VALUES.contains(value.toUpperCase(Locale.ROOT));
    }
}