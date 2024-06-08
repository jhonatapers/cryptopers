package com.jhonatapers;

import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public final class AES {

    public static String decifrar(byte[] textoCifrado, byte[] chave, int tamanhoIV) {

        try {

            byte[] iv = Arrays.copyOfRange(textoCifrado, 0, tamanhoIV);
            byte[] _textoCifrado = Arrays.copyOfRange(textoCifrado, tamanhoIV, textoCifrado.length);

            SecretKeySpec secretKeySpec = new SecretKeySpec(chave, "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

            byte[] decryptedBytes;

            decryptedBytes = cipher.doFinal(_textoCifrado);

            return new String(decryptedBytes, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return "erro";
        }
    }

    private static byte[] gerarIV(int tamanhoIV) {
        byte[] iv = new byte[tamanhoIV];
        new SecureRandom().nextBytes(iv);
        return iv;
    }

    public static String cifrar(byte[] textoClaro, byte[] chave, int tamanhoIV) {

        try {

            SecretKeySpec secretKeySpec = new SecretKeySpec(chave, "AES");
            byte[] iv = gerarIV(tamanhoIV);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] cipherText = cipher.doFinal(textoClaro);

            byte[] cipherTextWithIV = new byte[iv.length + cipherText.length];
            System.arraycopy(iv, 0, cipherTextWithIV, 0, iv.length);
            System.arraycopy(cipherText, 0, cipherTextWithIV, iv.length, cipherText.length);

            return new String(cipherTextWithIV, "UTF-8");

        } catch (Exception e) {
            e.printStackTrace();
            return "erro";
        }

    }

}
