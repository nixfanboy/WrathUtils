/**
 * The MIT License (MIT)
 * Wrath Utils Copyright (c) 2016 Trent Spears
 */
package wrath.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.InflaterInputStream;

/**
 * Class to manage the compression and decompression of data.
 * @author Trent Spears
 */
public class Compression 
{
    /**
     * Enum describing the available compression formats.
     */
    public static enum CompressionType { DEFLATE, GZIP; }
    
    /**
     * Static library, objects are unnecessary.
     */
    private Compression(){}
    
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
            System.err.println("] ERROR: Could not compress data in " + type.toString() + " format! I/O Error!");
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
            byte[] buf = new byte[(int)(decompressionStream.available() * 1.2)];
            int len = decompressionStream.read(buf);
            byte[] ret = new byte[len];
            System.arraycopy(buf, 0, ret, 0, len);
            return ret;
        }
        catch(IOException e)
        {
            System.err.println("] ERROR: Could not decompress data in " + type.toString() + " format! I/O Error!");
        }
        
        return data;
    }
    
    /**
     * Checks to see if the data is compressed and returns the format of compression. Returns null if not compressed.
     * This method is not 100% CERTAIN, but typically accurate.
     * @param data The data to check for the presence of compression.
     * @return Returns the format in which the data is compressed. If not compressed, returns null.
     */
    public static Compression.CompressionType isCompressed(byte[] data)
    {
        if(data.length < 2) return null;
        
        if((data[0] == (byte) (GZIPInputStream.GZIP_MAGIC)) && (data[1] == (byte) (GZIPInputStream.GZIP_MAGIC >> 8))) return Compression.CompressionType.GZIP;
        else if(data[0] == (byte) 1f && data[1] == (byte) 9d) return Compression.CompressionType.DEFLATE;
        else return null;
    }
}
