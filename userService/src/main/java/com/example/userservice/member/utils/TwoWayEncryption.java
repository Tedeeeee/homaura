package com.example.userservice.member.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

@Component
public class TwoWayEncryption {

    private static final String ALGORITHM = "AES";
    private final Key key;

    public TwoWayEncryption(@Value("${secret.key}") String secretKey) {
        this.key = new SecretKeySpec(secretKey.getBytes(), ALGORITHM);
    }

    public String encrypt(String plaintext) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("암호화 중 오류 발생", e);
        }
    }

    public String decrypt(String ciphertext) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(ciphertext));
            return new String(decryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("복호화 중 오류 발생", e);
        }
    }
}