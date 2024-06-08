package com.jhonatapers;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class HashUtils {

    public static byte[] sha256(byte[] input) {

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(input);
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

    }

}
