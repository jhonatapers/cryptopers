package com.jhonatapers;

import java.math.BigInteger;

public final class Euclides {

    public static BigInteger mdc(BigInteger a, BigInteger b) {
        if (a.compareTo(BigInteger.ZERO) == 0)
            return b;

        return mdc(b.mod(a), a);
    }

}
