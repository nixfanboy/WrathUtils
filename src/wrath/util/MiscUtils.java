/**
 * The MIT License (MIT)
 * Wrath Utils Copyright (c) 2015 Trent Spears
 */
package wrath.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.InflaterInputStream;

/**
 * A collection of assorted utilities.
 * @author Trent Spears
 */
public class MiscUtils
{   
    public static enum CompressionType { DEFLATE, GZIP; }
    /**
     * Static library, objects are unnecessary.
     */
    private MiscUtils(){}
    
    /**
     * Converts an array of primitive bytes to a {@link java.nio.ByteBuffer}.
     * @param data The byte array to convert.
     * @return Returns the {@link java.nio.ByteBuffer} form of the byte array.
     */
    public static ByteBuffer byteArrayToByteBuffer(byte[] data)
    {
        ByteBuffer ret = ByteBuffer.allocateDirect(data.length).order(ByteOrder.nativeOrder());
        ret.put(data).flip();
        return ret;
    }
    
    /**
     * Compresses the specified raw data.
     * @param data The byte data to compress.
     * @return Returns the compressed version of the original data.
     */
    public static byte[] compressData(byte[] data)
    {
        return compressData(data, CompressionType.GZIP);
    }
    
    /**
     * Compresses the specified raw data.
     * @param data The byte data to compress.
     * @param type The type of compression to use.
     * @return Returns the compressed version of the original data.
     */
    public static byte[] compressData(byte[] data, CompressionType type)
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream(data.length);
        OutputStream compressionStream;
        
        try
        {
            if(type == CompressionType.DEFLATE) compressionStream = new DeflaterOutputStream(out);
            else if(type == CompressionType.GZIP) compressionStream = new GZIPOutputStream(out);
            else return data;
            compressionStream.write(data);
            compressionStream.close();
            return out.toByteArray();
        }
        catch(IOException e)
        {
            System.err.println("Could not compress data in " + type.toString() + " format! I/O Error!");
        }
        
        return data;
    }
    
    /**
     * Decompresses the specified raw data.
     * @param data The byte data to decompress.
     * @return Returns the decompressed version of the original, compressed data.
     */
    public static byte[] decompressData(byte[] data)
    {
        return decompressData(data, CompressionType.GZIP);
    }
    
    /**
     * Decompresses the specified raw data.
     * @param data The byte data to decompress.
     * @param type The type of compression to use.
     * @return Returns the decompressed version of the original, compressed data.
     */
    public static byte[] decompressData(byte[] data, CompressionType type)
    {
        
        ByteArrayInputStream src = new ByteArrayInputStream(data);
        InputStream decompressionStream;
        try
        {
            if(type == CompressionType.DEFLATE) decompressionStream = new InflaterInputStream(src);
            else if(type == CompressionType.GZIP) decompressionStream = new GZIPInputStream(src);
            else return data;
            byte[] buf = new byte[data.length * 2];
            int len = decompressionStream.read(buf);
            byte[] ret = new byte[len];
            System.arraycopy(buf, 0, ret, 0, len);
            return ret;
        }
        catch(IOException e)
        {
            System.err.println("Could not decompress data in " + type.toString() + " format! I/O Error!");
        }
        
        return data;
    }
    
    /**
     * Decompiles an array of bytes back into the {@link java.lang.Object} format.
     * @param data The byte array to convert to an {@link java.lang.Object}.
     * @return Returns the {@link java.lang.Object} form of the Byte Array.
     */
    public static Object deserializeObject(byte[] data)
    {
        ByteArrayInputStream bai = new ByteArrayInputStream(data);
        ObjectInputStream ois;
        Object ret;
        
        try
        {
            ois = new ObjectInputStream(bai);
        }
        catch(IOException e)
        {
            System.err.println("Could not bind ObjectInput stream to ByteArray stream!");
            return null;
        }
        
        try
        {
            ret = ois.readObject();
        }
        catch(IOException | ClassNotFoundException e)
        {
            System.err.println("Could not read Object from ObjectInput stream!");
            return null;
        }
        
        try
        {
            ois.close();
        }
        catch(IOException e){}
        
        return ret;
    }
    
    /**
     * Converts an array of primitive floats to a {@link java.nio.FloatBuffer}.
     * @param data The float array to convert.
     * @return Returns the {@link java.nio.FloatBuffer} form of the float array.
     */
    public static FloatBuffer floatArrayToFloatBuffer(float[] data)
    {
        FloatBuffer ret = ByteBuffer.allocateDirect(data.length << 2).order(ByteOrder.nativeOrder()).asFloatBuffer();
        ret.put(data).flip();
        return ret;
    }
    
    /**
     * Converts an array of primitive ints to a {@link java.nio.IntBuffer}.
     * @param data The int array to convert.
     * @return Returns the {@link java.nio.IntBuffer} form of the int array.
     */
    public static IntBuffer intArrayToIntBuffer(int[] data)
    {
        IntBuffer ret = ByteBuffer.allocateDirect(data.length << 2).order(ByteOrder.nativeOrder()).asIntBuffer();
        ret.put(data).flip();
        return ret;
    }
    
    /**
     * If true, the specified data is GZIP Compressed.
     * @param data The data to check for the presence of GZIP compression.
     * @return Returns true if the specified data is compressed with the GZIP format.
     */
    public static boolean isGZIPCompressed(byte[] data)
    {
        return ((data[0] == (byte) (GZIPInputStream.GZIP_MAGIC)) && (data[1] == (byte) (GZIPInputStream.GZIP_MAGIC >> 8)));
    }
    
    /**
     * Compiles a {@link java.io.Serializable} object into a Byte Array.
     * @param object The {@link java.lang.Object} to convert into a Byte Array.
     * @return Returns the Byte Array form of the {@link java.io.Serializable}.
     */
    public static byte[] serializeObject(Serializable object)
    {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        ObjectOutputStream oos;
        
        try
        {
            oos = new ObjectOutputStream(bao);
        }
        catch(IOException e)
        {
            System.err.println("Could not bind ObjectOutput stream to ByteArray Stream!");
            return new byte[0];
        }
        
        try
        {
            oos.writeObject(object);
            oos.flush();
            oos.close();
        }
        catch(IOException e)
        {
            System.err.println("Could not write object to ObjectOutput stream!");
            return new byte[0];
        }
        
        return bao.toByteArray();
    }
}
