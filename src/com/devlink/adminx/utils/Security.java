package com.devlink.adminx.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

public class Security {

    private SecretKeySpec createSecretKeySpec() throws Exception {
        byte[] key = Environment.CIPHER_KEY.getBytes(StandardCharsets.UTF_8);
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        key = Arrays.copyOf(md.digest(key), 32);
        return new SecretKeySpec(key, "AES");
    }

    public String encrypt(String data) throws Exception {
        SecretKeySpec sks = createSecretKeySpec();
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, sks);

        byte[] string = data.getBytes(StandardCharsets.UTF_8);
        byte[] encryptedStr = cipher.doFinal(string);

        return Base64.getEncoder().encodeToString(encryptedStr);
    }

    public String decrypt(String encryptedData) throws Exception {
        SecretKeySpec sks = createSecretKeySpec();
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, sks);

        byte[] decoded = Base64.getDecoder().decode(encryptedData);
        byte[] decryptedStr = cipher.doFinal(decoded);

        return new String(decryptedStr);
    }

    public static int generateUniqueCode() {
        SecureRandom secureRandom = new SecureRandom();
        final int MIN = 100000;
        return secureRandom.nextInt((999999 - MIN) + 1) + MIN;
    }
}
