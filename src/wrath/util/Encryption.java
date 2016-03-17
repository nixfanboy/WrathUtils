/**
 * The MIT License (MIT)
 * Wrath Utils Copyright (c) 2016 Trent Spears
 */
package wrath.util;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

/**
 * Library to Encrypt and Decrypt byte data.
 * @author Trent Spears
 */
public class Encryption
{
    /**
     * Static library, Constructor not necessary.
     * Uses PBEWithSHA1AndDESede algorithm with 8-bit hash salt as it is the best algorithm widely available in JDK8.
     */
    private Encryption(){}
    
    private static final byte[] SALT = new byte[]{(byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32, (byte) 0x56, (byte) 0x35, (byte) 0xE3, (byte) 0x03};
    
    /**
     * Encrypts specified data using specified algorithm and key.
     * @param originalData The data to encrypt.
     * @param key The security key to encrypt the data, similar to a passphrase.
     * @return Returns the encrypted data according to specifications. If encryption failed, returned original data.
     */
    public static byte[] encryptData(byte[] originalData, String key)
    {
        return encryptData(originalData, key.toCharArray());
    }
    
    /**
     * Encrypts specified data using specified algorithm and key.
     * @param originalData The data to encrypt.
     * @param key The security key to encrypt the data, similar to a passphrase.
     * @return Returns the encrypted data according to specifications. If encryption failed, returned original data.
     */
    public static byte[] encryptData(byte[] originalData, char[] key)
    {
        byte[] ret = originalData;
        try 
        {
            KeySpec spec = new PBEKeySpec(key, SALT, 19);
            SecretKey secretKey = SecretKeyFactory.getInstance("PBEWithSHA1AndDESede").generateSecret(spec);
            AlgorithmParameterSpec pspec = new PBEParameterSpec(SALT, 19);
            
            Cipher c = Cipher.getInstance(secretKey.getAlgorithm());
            c.init(Cipher.ENCRYPT_MODE, secretKey, pspec);
            ret = c.doFinal(originalData);
        }
        catch(NoSuchAlgorithmException ex) 
        {
            System.err.println("ERROR: System does not support DES encryption algorithm!");
        } 
        catch(NoSuchPaddingException ex) 
        {
            System.err.println("ERROR: System does not support default encryption padding.");
        } 
        catch(InvalidKeyException ex) 
        {
            System.err.println("ERROR: Specified key is invalid!");
        } 
        catch(IllegalBlockSizeException ex) 
        {
            System.err.println("ERROR: Data to be encrypted is not correct block size!");
        } 
        catch(BadPaddingException ex) 
        {
            System.err.println("ERROR: System does not support default encryption padding OR I/O Error!");
        }
        catch(InvalidKeySpecException ex)
        {
            System.err.println("ERROR: System does not support salted encryption!");
        } 
        catch(InvalidAlgorithmParameterException ex) 
        {
            System.err.println("ERROR: Invalid algorithm parameter specified!");
        }
        return ret;
    }
    
    /**
     * Decrypts specified data using specified algorithm and key.
     * @param encryptedData The encrypted data to decrypt.
     * @param key The security key to decrypt the data, similar to a passphrase.
     * @return Returns the decrypted data according to specifications. If decryption failed, returned original encrypted data.
     */
    public static byte[] decryptData(byte[] encryptedData, String key)
    {
        return decryptData(encryptedData, key.toCharArray());
    }
    
    /**
     * Decrypts specified data using specified algorithm and key.
     * @param encryptedData The encrypted data to decrypt.
     * @param key The security key to decrypt the data, similar to a passphrase.
     * @return Returns the decrypted data according to specifications. If decryption failed, returned original encrypted data.
     */
    public static byte[] decryptData(byte[] encryptedData, char[] key)
    {
        byte[] ret = encryptedData;
        try 
        {
            KeySpec spec = new PBEKeySpec(key, SALT, 19);
            SecretKey secretKey = SecretKeyFactory.getInstance("PBEWithSHA1AndDESede").generateSecret(spec);
            AlgorithmParameterSpec pspec = new PBEParameterSpec(SALT, 19);
            
            Cipher c = Cipher.getInstance(secretKey.getAlgorithm());
            c.init(Cipher.DECRYPT_MODE, secretKey, pspec);
            ret = c.doFinal(encryptedData);
        }
        catch(NoSuchAlgorithmException ex) 
        {
            System.err.println("ERROR: System does not support DES encryption algorithm!");
        } 
        catch(NoSuchPaddingException ex) 
        {
            System.err.println("ERROR: System does not support default encryption padding.");
        } 
        catch(InvalidKeyException ex) 
        {
            System.err.println("ERROR: Specified key is invalid!");
        } 
        catch(IllegalBlockSizeException ex) 
        {
            System.err.println("ERROR: Data to be encrypted is not correct block size!");
        } 
        catch(BadPaddingException ex) 
        {
            System.err.println("ERROR: System does not support default encryption padding OR I/O Error!");
        }
        catch(InvalidKeySpecException ex)
        {
            System.err.println("ERROR: System does not support salted encryption!");
        } 
        catch(InvalidAlgorithmParameterException ex) 
        {
            System.err.println("ERROR: Invalid algorithm parameter specified!");
        }
        return ret;
    }
}
