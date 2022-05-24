package com.iwaa.server.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD2Encryptor implements Encryptor {

    private static final short RADIX = 16;
    private static final short HASH_TEXT_LENGTH = 32;

    public String encrypt(String string) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD2");
            byte[] messageDigest = md.digest(string.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            StringBuilder hashText = new StringBuilder(no.toString(RADIX));
            while (hashText.length() < HASH_TEXT_LENGTH) {
                hashText.insert(0, "0");
            }
            return hashText.toString();
        } catch (NoSuchAlgorithmException e) {
            return "illegal algorithm";
        }
    }
}
