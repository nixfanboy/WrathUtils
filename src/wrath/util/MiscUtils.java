/**
 *  Wrath Engine Utility Library  
 *  Copyright (C) 2015  Trent Spears
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
            Logger.getErrorLogger().log("Could not bind ObjectInput stream to ByteArray stream!");
            return null;
        }
        
        try
        {
            ret = ois.readObject();
        }
        catch(IOException | ClassNotFoundException e)
        {
            Logger.getErrorLogger().log("Could not read Object from ObjectInput stream!");
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
            Logger.getErrorLogger().log("Could not bind ObjectOutput stream to ByteArray Stream!");
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
            Logger.getErrorLogger().log("Could not write object to ObjectOutput stream!");
            return new byte[0];
        }
        
        return bao.toByteArray();
    }
}
