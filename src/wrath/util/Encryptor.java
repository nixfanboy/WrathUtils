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
import javax.security.auth.DestroyFailedException;

/**
 * Library to Encrypt and Decrypt byte data.
 * Uses AES Encryption Algorithm.
 * Only static methods are thread-safe.
 * Make sure to install JCE Unlimited Strength Jurisdiction Policy Files. https://en.wikipedia.org/wiki/Java_Cryptography_Extension
 * @author Trent Spears
 */
public class Encryptor
{
    public static final String STATIC_ALGORITHM = "AES";
    
    private final String alg;
    private Cipher cip;
    private final SecretKeySpec key;
    
    /**
     * Constructor.
     * @param key The key generated by {@link #generateKey(java.lang.String, java.lang.String) }.
     */
    public Encryptor(SecretKeySpec key)
    {
        this(key, "AES");
    }
    
    /**
     * Constructor.
     * @param key The key generated by {@link #generateKey(java.lang.String, java.lang.String) }.
     * @param algorithm The algorithm to use. Default is AES.
     */
    public Encryptor(SecretKeySpec key, String algorithm)
    {
        this.alg = algorithm;
        this.key = key;
        
        try
        {
            cip = Cipher.getInstance(alg);
        }
        catch(NoSuchAlgorithmException | NoSuchPaddingException ex)
        {
            System.err.println("] ERROR: ] ERROR: " + alg + " algorithm not supported!");
        }
    }
    
    /**
     * Closes the encryption object by destroying sensitive key data.
     * @see javax.crypto.spec.SecretKeySpec#destroy() 
     */
    public void close()
    {
        try 
        {
            key.destroy();
        }
        catch(DestroyFailedException ex)
        {
            System.err.println("] ERROR: ] ERROR: Key destroy failed! If on secure system, killing process is recommended.");
        }
    }
    
    /**
     * Decrypts specified data.
     * @param encryptedData The encrypted data to decrypt.
     * @return Returns the decrypted data according to specifications. If decryption failed, returns original encrypted data.
     */
    public byte[] decryptData(byte[] encryptedData)
    {
        if(key.isDestroyed())
        {
            System.err.println("] ERROR: ] ERROR: Tried to decrypt data with destroyed key.");
            return encryptedData;
        }
        
        try
        {
            cip.init(Cipher.DECRYPT_MODE, key);
            return cip.doFinal(encryptedData);
        } 
        catch (InvalidKeyException ex) 
        {
            System.err.println("] ERROR: ] ERROR: Invalid encryption key! Is JCE Unlimited Strength installed?");
        } 
        catch (IllegalBlockSizeException | BadPaddingException ex) 
        {
            System.err.println("] ERROR: ] ERROR: Invalid Padding or Block Size!");
        }
        return encryptedData;
    }
    
    /**
     * Decrypts specified data using specified key.
     * @param encryptedData The encrypted data to decrypt.
     * @param key The security key to decrypt the data.
     * @return Returns the decrypted data according to specifications. If decryption failed, returns original encrypted data.
     * @see #generateKey(java.lang.String, java.lang.String) 
     */
    public static byte[] decryptData(byte[] encryptedData, SecretKeySpec key)
    {
        if(key.isDestroyed())
        {
            System.err.println("] ERROR: ] ERROR: Tried to decrypt data with destroyed key.");
            return encryptedData;
        }
        
        Cipher cip = null;
        try
        {
            cip = Cipher.getInstance(STATIC_ALGORITHM);
        }
        catch(NoSuchAlgorithmException | NoSuchPaddingException ex) 
        {
            System.err.println("] ERROR: ] ERROR: " + STATIC_ALGORITHM + " algorithm not supported!");
        }
        
        if(cip == null)
        {
            System.err.println("] ERROR: ] ERROR: CIPHER could not be formed!");
            return encryptedData;
        }
        
        try
        {
            cip.init(Cipher.DECRYPT_MODE, key);
            return cip.doFinal(encryptedData);
        } 
        catch (InvalidKeyException ex) 
        {
            System.err.println("] ERROR: ] ERROR: Invalid encryption key! Is JCE Unlimited Strength installed?");
        } 
        catch (IllegalBlockSizeException | BadPaddingException ex) 
        {
            System.err.println("] ERROR: ] ERROR: Invalid Padding or Block Size!");
        }
        return encryptedData;
    }
    
    /**
     * Encrypts specified data.
     * @param originalData The data to encrypt.
     * @return Returns the encrypted data according to specifications. If encryption failed, returns original data.
     */
    public byte[] encryptData(byte[] originalData)
    {
        if(key.isDestroyed())
        {
            System.err.println("] ERROR: ] ERROR: Tried to encrypt data with destroyed key.");
            return originalData;
        }
        
        try
        {
            cip.init(Cipher.ENCRYPT_MODE, key);
            return cip.doFinal(originalData);
        } 
        catch (InvalidKeyException ex) 
        {
            System.err.println("] ERROR: ] ERROR: Invalid encryption key! Is JCE Unlimited Strength installed?");
        }
        catch (IllegalBlockSizeException | BadPaddingException ex) 
        {
            System.err.println("] ERROR: ] ERROR: Invalid Padding or Block Size!");
        }
        return originalData;
    }
    
    /**
     * Encrypts specified data using specified key.
     * @param originalData The data to encrypt.
     * @param key The security key to encrypt the data.
     * @return Returns the encrypted data according to specifications. If encryption failed, returns original data.
     * @see #generateKey(java.lang.String, java.lang.String) 
     */
    public static byte[] encryptData(byte[] originalData, SecretKeySpec key)
    {
        if(key.isDestroyed())
        {
            System.err.println("] ERROR: ] ERROR: Tried to encrypt data with destroyed key.");
            return originalData;
        }
        
        Cipher cip = null;
        try
        {
            cip = Cipher.getInstance(STATIC_ALGORITHM);
        }
        catch (NoSuchAlgorithmException | NoSuchPaddingException ex) 
        {
            System.err.println("] ERROR: ] ERROR: " + STATIC_ALGORITHM + " algorithm not supported!");
        }
        
        if(cip == null)
        {
            System.err.println("] ERROR: ] ERROR: CIPHER could not be formed!");
            return originalData;
        }
        
        try
        {
            cip.init(Cipher.ENCRYPT_MODE, key);
            return cip.doFinal(originalData);
        } 
        catch (InvalidKeyException ex) 
        {
            System.err.println("] ERROR: ] ERROR: Invalid encryption key! Is JCE Unlimited Strength installed?");
        }
        catch (IllegalBlockSizeException | BadPaddingException ex) 
        {
            System.err.println("] ERROR: ] ERROR: Invalid Padding or Block Size!");
        }
        return originalData;
    }
    
    /**
     * Generates a key with no salt using AES.
     * This is NOT recommended.
     * @param key The key, or passphrase, to generate the security key with.
     * @return Returns the {@link java.security.Key} object for cyptography.
     */
    public static SecretKeySpec generateKey(String key)
    {
        return generateKeyAlg(key, "AES");
    }
    
    /**
     * Generates a key with no salt.
     * This is NOT recommended.
     * @param key The key, or passphrase, to generate the security key with.
     * @param algorithm The algorithm to use.
     * @return Returns the {@link java.security.Key} object for cyptography.
     */
    public static SecretKeySpec generateKeyAlg(String key, String algorithm)
    {
        try 
        {
            return generateKey(key.getBytes("UTF-8"), algorithm);
        } 
        catch (UnsupportedEncodingException ex) 
        {
            System.err.println("] ERROR: ERROR: System does not support UTF-8?!");
        }
        
        return generateKey(key.getBytes(), algorithm);
    }
    
    /**
     * Generates a secure, salted hash for a security key.
     * @param key The key, or passphrase, to generate the security key with.
     * @param salt The salt to generate this key's hash with.
     * @return Returns the {@link java.security.Key} object for cyptography.
     */
    public static SecretKeySpec generateKey(String key, String salt)
    {
        return generateKey(key, salt, "AES");
    }
    
    /**
     * Generates a secure, salted hash for a security key.
     * @param key The key, or passphrase, to generate the security key with.
     * @param salt The salt to generate this key's hash with.
     * @param algorithm The algorithm to use.
     * @return Returns the {@link java.security.Key} object for cyptography.
     */
    public static SecretKeySpec generateKey(String key, String salt, String algorithm)
    {
        try 
        {
            return generateKey((salt + key).getBytes("UTF-8"), algorithm);
        } 
        catch (UnsupportedEncodingException ex) 
        {
            System.err.println("] ERROR: ERROR: System does not support UTF-8?!");
        }
        
        return generateKey((salt + key).getBytes(), algorithm);
    }
    
    /**
     * Generates a secure, salted hash for a security key.
     * @param key The raw key to generate the security key with.
     * @param salt The salt to generate this key's hash with.
     * @return Returns the {@link java.security.Key} object for cyptography.
     */
    public static SecretKeySpec generateKey(byte[] key, byte[] salt)
    {
        return generateKey(key, salt, "AES");
    }
    
    /**
     * Generates a secure, salted hash for a security key.
     * @param key The raw key to generate the security key with.
     * @param salt The salt to generate this key's hash with.
     * @param algorithm The algorithm to use.
     * @return Returns the {@link java.security.Key} object for cyptography.
     */
    public static SecretKeySpec generateKey(byte[] key, byte[] salt, String algorithm)
    {
        byte[] nkey = new byte[key.length + salt.length];
        System.arraycopy(salt, 0, nkey, 0, salt.length);
        System.arraycopy(key, 0, nkey, salt.length, key.length);
        return generateKey(nkey, algorithm);
    }
    
    /**
     * Generates a key with no salt.
     * This is NOT recommended!
     * @param key The raw key to generate the security key with.
     * @return Returns the {@link javax.crypto.spec.SecretKeySpec} object for cyptography.
     */
    public static SecretKeySpec generateKey(byte[] key)
    {
        return generateKey(key, "AES");
    }
    
    /**
     * Generates a key with no salt.
     * This is NOT recommended!
     * @param key The raw key to generate the security key with.
     * @param algorithm The algorithm to use.
     * @return Returns the {@link javax.crypto.spec.SecretKeySpec} object for cyptography.
     */
    public static SecretKeySpec generateKey(byte[] key, String algorithm)
    {
        MessageDigest mdg;
        try 
        {
            mdg = MessageDigest.getInstance("SHA-256");
            key = mdg.digest(key);
        }
        catch (NoSuchAlgorithmException ex) 
        {
            System.err.println("] ERROR: ] ERROR: Could not use 256-bit hashing as SHA-256 is not supported!");
            try 
            {
                mdg = MessageDigest.getInstance("SHA-1");
                key = mdg.digest(key);
                key = Arrays.copyOf(key, 16);
            }
            catch (NoSuchAlgorithmException ex1) 
            {
                System.err.println("] ERROR: ] ERROR: Could not use 128-bit hashing as SHA-1 is not supported?! Giving up on SHA!");
                try
                {
                    mdg = MessageDigest.getInstance("MD5");
                    key = mdg.digest(key);
                    key = Arrays.copyOf(key, 16);
                }
                catch (NoSuchAlgorithmException ex2)
                {
                    System.err.println("] ERROR: ] ERROR: Could not use 128-bit hashing as MD5 is not supported?! Giving up on HASHING!");
                }
            }
        }
        
        return new SecretKeySpec(key, algorithm);
    }
}