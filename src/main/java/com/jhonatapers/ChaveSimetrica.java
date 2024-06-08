package com.jhonatapers;

import java.math.BigInteger;
import java.security.SecureRandom;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ChaveSimetrica {

    private final byte[] bytes;

    @JsonProperty("bytes")
    public byte[] getBytes() {
        return bytes;
    }

    @JsonCreator
    public ChaveSimetrica(@JsonProperty("bytes") byte[] bytes) {
        this.bytes = bytes;
    }

    public ChaveSimetrica(String hex) {
        this.bytes = HexUtils.hexToByteArray(hex);
    }

    public static ChaveSimetrica gerar(int numBits) {
        byte[] bytes = new byte[numBits / 8];
        new SecureRandom().nextBytes(bytes);
        return new ChaveSimetrica(bytes);
    }

    public BigInteger toBigInteger() {
        return new BigInteger(1, bytes);
    }

    public byte[] toBytes() {
        return bytes;
    }

    public String toHex() {
        return HexUtils.toHexString(bytes);
    }

    public int tamanhoEmBits() {
        return bytes.length * 8;
    }

    public int tamanhoEmBytes() {
        return bytes.length;
    }

    @Override
    public String toString() {
        return "-----  CHAVE-SIMÉTRICA  -----\nbits=[%s]\nhex=[%s]\n---------------------------"
                .formatted(
                        tamanhoEmBits(),
                        toHex());
    }

}
