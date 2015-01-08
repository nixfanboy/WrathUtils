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
import javax.swing.JOptionPane;

/**
 * A collection of assorted utilities.
 * @author Trent Spears
 */
public class MiscUtils
{
    public static enum PopupMessageType{ERROR, INFO, PLAIN, QUESTION, WARNING;}
    
    /**
     * Static library, objects are unnecessary.
     */
    private MiscUtils(){}
    
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
     * Displays a pop-up message.
     * @param popupTitle The title of the pop-up message.
     * @param message The message displayed on the pop-up.
     * @param type The type of message to display. Changes the icon.
     */
    public static void displayPopupMessage(String popupTitle, String message, PopupMessageType type)
    {
        int opt = JOptionPane.INFORMATION_MESSAGE;
        
        if(type == null) opt = JOptionPane.PLAIN_MESSAGE;    
        else if(type == PopupMessageType.ERROR) opt = JOptionPane.ERROR_MESSAGE;
        else if(type == PopupMessageType.INFO) opt = JOptionPane.INFORMATION_MESSAGE;
        else if(type == PopupMessageType.PLAIN) opt = JOptionPane.PLAIN_MESSAGE;
        else if(type == PopupMessageType.QUESTION) opt = JOptionPane.QUESTION_MESSAGE;
        else if(type == PopupMessageType.WARNING) opt = JOptionPane.WARNING_MESSAGE;
        
        JOptionPane.showMessageDialog(null, message, popupTitle, opt);
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
