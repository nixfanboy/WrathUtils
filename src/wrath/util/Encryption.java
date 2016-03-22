/**
 * The MIT License (MIT)
 * Wrath Utils Copyright (c) 2016 Trent Spears
 */
package wrath.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * Library to Encrypt and Decrypt byte data.
 * @author Trent Spears
 */
public class Encryption
{
    public static final String ALGORITHM = "AES";
    private static Cipher CIPHER = null;
    
    /**
     * Static library, Constructor not necessary.
     * Uses AES algorithm as it is the best algorithm widely available in JDK8.
     */
    private Encryption(){}
    
    /**
     * Decrypts specified data using specified algorithm and key.
     * @param encryptedData The encrypted data to decrypt.
     * @param key The security key to decrypt the data, similar to a passphrase.
     * @return Returns the decrypted data according to specifications. If decryption failed, returned original encrypted data.
     */
    public static byte[] decryptData(byte[] encryptedData, SecretKeySpec key)
    {
        if(CIPHER == null)
        {
            try 
            {
                CIPHER = Cipher.getInstance(ALGORITHM);
            }
            catch (NoSuchAlgorithmException | NoSuchPaddingException ex) 
            {
                System.err.println("] ERROR: " + ALGORITHM + " algorithm not supported!");
            }
        }
        
        if(CIPHER == null)
        {
            System.err.println("] ERROR: CIPHER could not be formed!");
            return encryptedData;
        }
        
        byte[] ret = encryptedData;
        try
        {
            CIPHER.init(Cipher.DECRYPT_MODE, key);
            ret = CIPHER.doFinal(encryptedData);
        } 
        catch (InvalidKeyException ex) 
        {
            System.err.println("] ERROR: Invalid encryption key!");
        } 
        catch (IllegalBlockSizeException | BadPaddingException ex) 
        {
            System.err.println("] ERROR: Invalid Padding or Block Size!");
        }
        return ret;
    }
    
    /**
     * Encrypts specified data using specified algorithm and key.
     * @param originalData The data to encrypt.
     * @param key The security key to encrypt the data, similar to a passphrase.
     * @return Returns the encrypted data according to specifications. If encryption failed, returned original data.
     */
    public static byte[] encryptData(byte[] originalData, SecretKeySpec key)
    {
        if(CIPHER == null)
        {
            try 
            {
                CIPHER = Cipher.getInstance(ALGORITHM);
            }
            catch (NoSuchAlgorithmException | NoSuchPaddingException ex) 
            {
                System.err.println("] ERROR: " + ALGORITHM + " algorithm not supported!");
            }
        }
        
        if(CIPHER == null)
        {
            System.err.println("] ERROR: CIPHER could not be formed!");
            return originalData;
        }
        
        byte[] ret = originalData;
        try
        {
            CIPHER.init(Cipher.ENCRYPT_MODE, key);
            ret = CIPHER.doFinal(originalData);
        } 
        catch (InvalidKeyException ex) 
        {
            System.err.println("] ERROR: Invalid encryption key!");
        }
        catch (IllegalBlockSizeException | BadPaddingException ex) 
        {
            System.err.println("] ERROR: Invalid Padding or Block Size!");
        }
        return ret;
    }
    
    /**
     * Generates a key with a salt found in the passphrase itself.
     * Not as secure as using a unique salt but more secure than no salt.
     * @param key The key, or passphrase, to generate the security key with.
     * @return Returns the {@link java.security.Key} object for cyptography.
     */
    public static SecretKeySpec generateKey(String key)
    {
        return generateKey(key, key.substring((key.length() / 2), key.length()));
    }
    
    /**
     * Generates a secure, salted hash for a security key.
     * @param key The key, or passphrase, to generate the security key with.
     * @param salt The salt to generate this key's hash with.
     * @return Returns the {@link java.security.Key} object for cyptography.
     */
    public static SecretKeySpec generateKey(String key, String salt)
    {
        try 
        {
            return generateKey((salt + key).getBytes("UTF-8"));
        } 
        catch (UnsupportedEncodingException ex) 
        {
            System.err.println("ERROR: System does not support UTF-8?!");
        }
        
        return generateKey((salt + key).getBytes());
    }
    
    /**
     * Generates a secure, salted hash for a security key.
     * @param key The raw key to generate the security key with.
     * @param salt The salt to generate this key's hash with.
     * @return Returns the {@link java.security.Key} object for cyptography.
     */
    public static SecretKeySpec generateKey(byte[] key, byte[] salt)
    {
        byte[] nkey = new byte[key.length + salt.length];
        System.arraycopy(salt, 0, nkey, 0, salt.length);
        System.arraycopy(key, 0, nkey, salt.length, key.length);
        return generateKey(nkey);
    }
    
    /**
     * Generates a key with no salt.
     * This is NOT recommended!
     * @param key The raw key to generate the security key with.
     * @return Returns the {@link javax.crypto.spec.SecretKeySpec} object for cyptography.
     */
    public static SecretKeySpec generateKey(byte[] key)
    {
        MessageDigest mdg;
        try 
        {
            mdg = MessageDigest.getInstance("SHA-256");
            key = mdg.digest(key);
        }
        catch (NoSuchAlgorithmException ex) 
        {
            System.err.println("] ERROR: Could not use 256-bit encryption as SHA-256 is not supported!");
            try 
            {
                mdg = MessageDigest.getInstance("SHA-1");
                key = mdg.digest(key);
                key = Arrays.copyOf(key, 16);
            }
            catch (NoSuchAlgorithmException ex1) 
            {
                System.err.println("] ERROR: Could not use 16-bit encryption as SHA-1 is not supported?! Giving up on SHA!");
            }
        }
        
        return new SecretKeySpec(key, ALGORITHM);
    }
}
