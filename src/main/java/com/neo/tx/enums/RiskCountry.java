package com.neo.tx.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum RiskCountry {

    BURKINA_FASO("BFA"),
    LAOS("LAO"),
    MALI("MLI"),
    NIGER("NER"),
    SIERRA_LEONE("SLE"),
    SOUTH_SUDAN("SSD"),
    SYRIA("SYR"),
    ANGOLA("AGO"),
    ANTIGUA_AND_BARBUDA("ATG"),
    BENIN("BEN"),
    COTE_D_IVOIRE("CIV"),
    DOMINICA("DMA"),
    GABON("GAB"),
    THE_GAMBIA("GMB"),
    MALAWI("MWI"),
    MAURITANIA("MRT"),
    NIGERIA("NGA"),
    SENEGAL("SEN"),
    TANZANIA("TZA"),
    TONGA("TON"),
    TURKMENISTAN("TKM"),
    ZAMBIA("ZMB"),
    ZIMBABWE("ZWE");

    private final String alpha3;

    RiskCountry(String alpha3) {
        this.alpha3 = alpha3;
    }

    public String getAlpha3() {
        return alpha3;
    }

    private static final Map<String, RiskCountry> ALPHA3_MAP =
            Arrays.stream(values())
                    .collect(Collectors.toUnmodifiableMap(
                            RiskCountry::getAlpha3,
                            Function.identity()
                    ));

    public static boolean contains(String code) {
        return ALPHA3_MAP.containsKey(code);
    }
}