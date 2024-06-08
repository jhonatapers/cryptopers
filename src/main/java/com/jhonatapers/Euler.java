package com.jhonatapers;

import java.math.BigInteger;

public final class Euler {

    public static BigInteger calcular(BigInteger fatorPrimoP, BigInteger fatorPrimoQ) {
        return fatorPrimoP.subtract(BigInteger.ONE).multiply(fatorPrimoQ.subtract(BigInteger.ONE));
    }

}
