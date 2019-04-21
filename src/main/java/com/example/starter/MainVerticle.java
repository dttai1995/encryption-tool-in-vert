package com.example.starter;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.util.Base64;


public class MainVerticle  {
  /**
   * Generate key which contains a pair of privae and public key using 1024 bytes
   * @return key pair
   * @throws NoSuchAlgorithmException
   */
  public static void generateKey() throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException {
    Security.addProvider(new BouncyCastleProvider());
    KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
    keyGen.initialize(2048);
    KeyPair key = keyGen.generateKeyPair();
    Cipher cipher  = Cipher.getInstance("RSA");
    cipher.init(Cipher.ENCRYPT_MODE, key.getPublic());
    byte[] bytes = cipher.doFinal("Hello world".getBytes());
    String x = Base64.getEncoder().encodeToString(bytes);
    System.out.println(x);
    cipher.init(Cipher.DECRYPT_MODE, key.getPrivate());
    System.out.println(new String(cipher.doFinal(Base64.getDecoder().decode(x.getBytes()))));
  }

  public static void main(String[] args) {
    try {
      generateKey();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (InvalidKeyException e) {
      e.printStackTrace();
    } catch (NoSuchPaddingException e) {
      e.printStackTrace();
    } catch (BadPaddingException e) {
      e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
      e.printStackTrace();
    }
  }

}
