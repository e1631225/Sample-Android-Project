package com.example.infolinetestapplication.utils;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

/**
 * Created by suleyman on 31.3.2016.
 */



// kullanıcı şifresini encode etmek için kullanılır
public class Encoder {

    static final String ENCRIPTION_ALGORITHM = "PBEWithMD5AndDES";
    private static final char[] PASSWORD = "asdfghjklzxcvbnm".toCharArray();
    private static final byte[] SALT = {
            (byte) 0xde, (byte) 0x33, (byte) 0x10, (byte) 0x12,
            (byte) 0xde, (byte) 0x33, (byte) 0x10, (byte) 0x12,
    };

    public static String encrypt(String userPassword) throws GeneralSecurityException, UnsupportedEncodingException {
        try {
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ENCRIPTION_ALGORITHM);
            SecretKey key = keyFactory.generateSecret(new PBEKeySpec(PASSWORD));
            Cipher pbeCipher = Cipher.getInstance(ENCRIPTION_ALGORITHM);
            pbeCipher.init(Cipher.ENCRYPT_MODE, key, new PBEParameterSpec(SALT, 20));
            return base64Encode(pbeCipher.doFinal(userPassword.getBytes("UTF-8")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String base64Encode(byte[] bytes) {
        return  Base64.encodeToString(bytes, Base64.DEFAULT);
    }
}
