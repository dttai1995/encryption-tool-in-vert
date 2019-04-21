package com.taidinh.main.utils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;


public class RSAUtils {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static PublicKey getPublicKey(byte[] base64PublicKey) {
        PublicKey publicKey = null;
        String content = new String(base64PublicKey);
        content = content.replace("-----BEGIN PUBLIC KEY-----\n", "");
        content = content.replace("-----END PUBLIC KEY-----", "");
        content = content.replace("\n", "");
        content = content.replace("\r", "");
        try {
            System.out.println(content);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(content.getBytes()));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            publicKey = keyFactory.generatePublic(keySpec);
            return publicKey;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return publicKey;
    }

    public static PrivateKey getPrivateKey(byte[] base64PublicKey) {
        PrivateKey privateKey = null;
        String content = new String(base64PublicKey);
        content = content.replace("-----BEGIN RSA PRIVATE KEY-----\n", "");
        content = content.replace("-----END RSA PRIVATE KEY-----", "");
        content = content.replace("\n", "");
        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(content.getBytes()));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            privateKey = keyFactory.generatePrivate(keySpec);
            return privateKey;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return privateKey;
    }


    public static String encrypt(String data, byte[] publicKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(publicKey));
        return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes()));
    }

    public static String decrypt(String data, byte[] privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(privateKey));
        return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes()));
    }

}
