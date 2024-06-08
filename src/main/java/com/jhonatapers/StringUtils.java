package com.jhonatapers;

public final class StringUtils {

    public static String inverter(String string) {

        if (string == null)
            return string;

        String invertida = "";

        for (int i = string.length() - 1; i >= 0; i--)
            invertida = invertida + string.charAt(i);

        return invertida;
    }

}
