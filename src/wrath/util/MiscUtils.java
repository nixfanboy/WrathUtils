/**
 * The MIT License (MIT)
 * Wrath Utils Copyright (c) 2016 Trent Spears
 */
package wrath.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * A collection of assorted utilities.
 * @author Trent Spears
 */
public class MiscUtils
{   
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
