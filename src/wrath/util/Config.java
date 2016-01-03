/**
 * The MIT License (MIT)
 * Wrath Utils Copyright (c) 2015 Trent Spears
 */
package wrath.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Basic, extendable configuration system. Creates a simple 'key: value' config file and stores it in a map.
 * Set Properties with the setProperty(String, String) method and use any get() method to retrieve them.
 * All error handling and parsing is done here as well.
 * @author Trent Spears
 */
public class Config 
{
    private final File configFile;
    private final String configName;
    private final HashMap<String, String> map = new HashMap<>();
    
    /**
     * Constructor.
     * @param configFile The {@link java.io.File} that this config will be stored in.
     */
    public Config(File configFile)
    {
        this.configName = configFile.getName();
        this.configFile = configFile;
        
        if(configFile.exists()) load();
    }
    
    /**
     * Returns a property corresponding to the key as a boolean.
     * @param key The key of the configuration value to query
     * @return The value of the key. Returns false by default.
     */
    public boolean getBoolean(String key)
    {
        if(!map.containsKey(key)) return false;
        
        return Boolean.parseBoolean(map.get(key));
    }
    
    /**
     * Returns a property corresponding to the key as a boolean.
     * @param key The key of the configuration value to query
     * @param defaultValue The default value if the key is not present.
     * @return The value of the key.
     */
    public boolean getBoolean(String key, boolean defaultValue)
    {
        if(!map.containsKey(key))
        {
            this.setProperty(key, defaultValue);
            return defaultValue;
        }
        
        return Boolean.parseBoolean(map.get(key));
    }
    
    /**
     * Returns a property corresponding to the key as a double. Default is 0.0.
     * @param key The key of the configuration value to query
     * @return The value of the key. Returns 0.0 by default.
     */
    public double getDouble(String key)
    {
        if(!map.containsKey(key)) return 0.0;
        try
        {
            return Double.parseDouble(map.get(key));
        }
        catch(NumberFormatException e)
        {
            return 0.0;
        }
    }
    
    /**
     * Returns a property corresponding to the key as a double.
     * @param key The key of the configuration value to query
     * @param defaultValue The default value if the key is not present.
     * @return The value of the key.
     */
    public double getDouble(String key, double defaultValue)
    {
        if(!map.containsKey(key))
        {
            this.setProperty(key, defaultValue);
            return defaultValue;
        }
        
        try
        {
            return Double.parseDouble(map.get(key));
        }
        catch(NumberFormatException e)
        {
            return defaultValue;
        }
    }
    
    /**
     * Returns a property corresponding to the key as an Array of Doubles. Default is an empty array.
     * @param key The key of the configuration value to query.
     * @return Returns an array of doubles corresponding to the key. Returns empty array by default.
     */
    public double[] getDoubleArray(String key)
    {
        if(!map.containsKey(key)) return new double[]{};
        
        String[] buf = map.get(key).split(",");
        final ArrayList<Double> buf2 = new ArrayList<>();
        for(String f : buf)
            try
            {
                buf2.add(Double.parseDouble(f));
            }
            catch(NumberFormatException e)
            {
                System.err.println("Could not read double '" + f + "' in getDoubleArray()!");
            }
        
        double[] buf3 = new double[buf2.size()];
        for(int x = 0; x < buf3.length; x++) buf3[x] = buf2.get(x);
        
        return buf3;
    }
    
    /**
     * Returns a property corresponding to the key as an Array of Doubles.
     * @param key The key of the configuration value to query.
     * @param defaultValue The default value if the key is not present.
     * @return Returns an array of integers corresponding to the key.
     */
    public double[] getDoubleArray(String key, double[] defaultValue)
    {
        if(!map.containsKey(key))
        {
            String str = "";
            for(double num : defaultValue)
                str = str + num + ",";
            this.setProperty(key, str);
            return defaultValue;
        }
        
        String[] buf = map.get(key).split(",");
        final ArrayList<Double> buf2 = new ArrayList<>();
        for(String f : buf)
            try
            {
                buf2.add(Double.parseDouble(f));
            }
            catch(NumberFormatException e)
            {
                System.err.println("Could not read double '" + f + "' in getDoubleArray()!");
            }
        
        double[] buf3 = new double[buf2.size()];
        for(int x = 0; x < buf3.length; x++)  buf3[x] = buf2.get(x);
        
        return buf3;
    }
    
    /**
     * Returns a property corresponding to the key as a floating-point number. Default is 0.0f.
     * @param key The key of the configuration value to query
     * @return The value of the key. Returns 0.0f by default.
     */
    public float getFloat(String key)
    {
        if(!map.containsKey(key)) return 0;
        try
        {
            return Float.parseFloat(map.get(key));
        }
        catch(NumberFormatException e)
        {
            return 0.0f;
        }
    }
    
    /**
     * Returns a property corresponding to the key as a floating-point number.
     * @param key The key of the configuration value to query
     * @param defaultValue The default value if the key is not present.
     * @return The value of the key.
     */
    public float getFloat(String key, float defaultValue)
    {
        if(!map.containsKey(key))
        {
            this.setProperty(key, defaultValue);
            return defaultValue;
        }
        
        try
        {
            return Float.parseFloat(map.get(key));
        }
        catch(NumberFormatException e)
        {
            return defaultValue;
        }
    }
    
    /**
     * Returns a property corresponding to the key as an Array of floating-point numbers. Default is an empty array.
     * @param key The key of the configuration value to query.
     * @return Returns an array of floats corresponding to the key. Returns empty array by default.
     */
    public float[] getFloatArray(String key)
    {
        if(!map.containsKey(key)) return new float[]{};
        
        String[] buf = map.get(key).split(",");
        final ArrayList<Float> buf2 = new ArrayList<>();
        for(String f : buf)
            try
            {
                buf2.add(Float.parseFloat(f));
            }
            catch(NumberFormatException e)
            {
                System.err.println("Could not read float '" + f + "' in getFloatArray()!");
            }
        float[] buf3 = new float[buf2.size()];
        for(int x = 0; x < buf3.length; x++) buf3[x] = buf2.get(x);
        
        return buf3;
    }
    
    /**
     * Returns a property corresponding to the key as an Array of floating-point numbers.
     * @param key The key of the configuration value to query.
     * @param defaultValue The default value if the key is not present.
     * @return Returns an array of floating-point numbers corresponding to the key.
     */
    public float[] getFloatArray(String key, float[] defaultValue)
    {
        if(!map.containsKey(key))
        {
            String str = "";
            for(float num : defaultValue)
                str = str + num + ",";
            this.setProperty(key, str);
            return defaultValue;
        }
        
        String[] buf = map.get(key).split(",");
        final ArrayList<Float> buf2 = new ArrayList<>();
        for(String f : buf)
            try
            {
                buf2.add(Float.parseFloat(f));
            }
            catch(NumberFormatException e)
            {
                System.err.println("Could not read float '" + f + "' in getFloatArray()!");
            }
        
        float[] buf3 = new float[buf2.size()];
        for(int x = 0; x < buf3.length; x++) buf3[x] = buf2.get(x);
        
        return buf3;
    }
    
    /**
     * Returns a property corresponding to the key as a integer. Default is 0.
     * @param key The key of the configuration value to query
     * @return The value of the key. Returns 0 by default.
     */
    public int getInt(String key)
    {
        if(!map.containsKey(key)) return 0;
        try
        {
            return Integer.parseInt(map.get(key));
        }
        catch(NumberFormatException e)
        {
            return 0;
        }
    }
    
    /**
     * Returns a property corresponding to the key as a integer.
     * @param key The key of the configuration value to query
     * @param defaultValue The default value if the key is not present.
     * @return The value of the key.
     */
    public int getInt(String key, int defaultValue)
    {
        if(!map.containsKey(key))
        {
            this.setProperty(key, defaultValue);
            return defaultValue;
        }
        
        try
        {
            return Integer.parseInt(map.get(key));
        }
        catch(NumberFormatException e)
        {
            return defaultValue;
        }
    }
    
    /**
     * Returns a property corresponding to the key as an Array of Integers. Default is an empty array.
     * @param key The key of the configuration value to query.
     * @return Returns an array of integers. Returns empty array by default.
     */
    public int[] getIntArray(String key)
    {
        if(!map.containsKey(key)) return new int[]{};
        
        String[] buf = map.get(key).split(",");
        final ArrayList<Integer> buf2 = new ArrayList<>();
        for(String f : buf)
            try
            {
                buf2.add(Integer.parseInt(f));
            }
            catch(NumberFormatException e)
            {
                System.err.println("Could not read int '" + f + "' in getIntArray()!");
            }
        
        int[] buf3 = new int[buf2.size()];
        for(int x = 0; x < buf3.length; x++) buf3[x] = buf2.get(x);
        
        return buf3;
    }
    
    /**
     * Returns a property corresponding to the key as an Array of Integers.
     * @param key The key of the configuration value to query.
     * @param defaultValue The default value if the key is not present.
     * @return Returns an array of integers.
     */
    public int[] getIntArray(String key, int[] defaultValue)
    {
        if(!map.containsKey(key))
        {
            String str = "";
            for(int num : defaultValue)
                str = str + num + ",";
            this.setProperty(key, str);
            return defaultValue;
        }
        
        String[] buf = map.get(key).split(",");
        final ArrayList<Integer> buf2 = new ArrayList<>();
        for(String f : buf)
            try
            {
                buf2.add(Integer.parseInt(f));
            }
            catch(NumberFormatException e)
            {
                System.err.println("Could not read int '" + f + "' in getIntArray()!");
                return defaultValue;
            }
        
        int[] buf3 = new int[buf2.size()];
        for(int x = 0; x < buf3.length; x++) buf3[x] = buf2.get(x);
        
        return buf3;
    }
    
    /**
     * Gets a list of keys present in the config, in the form of an iterator.
     * @return The list of keys in the config in the form of an iterator.
     */
    public Iterator<String> getKeyList()
    {
        return  map.keySet().iterator();
    }
    
    /**
     * Returns a property corresponding to the key as a String. Default is "null".
     * @param key The key of the configuration value to query
     * @return The value of the key. Returns "null" by default.
     */
    public String getString(String key)
    {
        if(!map.containsKey(key)) return "null";
        
        return map.get(key);
    }
    
    /**
     * Returns a property corresponding to the key as a String.
     * @param key The key of the configuration value to query
     * @param defaultValue The default value if the key is not present.
     * @return The value of the key.
     */
    public String getString(String key, String defaultValue)
    {
        if(!map.containsKey(key))
        {
            this.setProperty(key, defaultValue);
            return defaultValue;
        }
        
        return map.get(key);
    }
    
    /**
     * Returns a property corresponding to the key as an Array of Strings.
     * @param key The key of the configuration value to query
     * @return The value of the key. Returns an empty array by default.
     */
    public String[] getStringArray(String key)
    {
        if(!map.containsKey(key)) return new String[]{};
        
        return map.get(key).split(",");
    }
    
    /**
     * Returns a property corresponding to the key as an Array of Strings.
     * @param key The key of the configuration value to query
     * @param defaultValue The default value if the key is not present.
     * @return The value of the key.
     */
    public String[] getStringArray(String key, String[] defaultValue)
    {
        if(!map.containsKey(key))
        {
            String str = "";
            for(String s : defaultValue)
                str = str + s + ",";
            this.setProperty(key, str);
            return defaultValue;
        }
        
        return map.get(key).split(",");
    }
    
    /**
     * Method to check if an option is present in the config.
     * @param key The key to search for
     * @return Whether or not the requested option is set.
     */
    public boolean hasOption(String key)
    {
        return map.containsKey(key);
    }
    
    private void load()
    {
        try(BufferedReader in = new BufferedReader(new FileReader(configFile)))
        {              
            String current;
            while((current = in.readLine()) != null)
            {
                if(current.startsWith("#")) continue;
                String[] buf = current.split(": ", 2);
                if(buf.length < 1) continue;
                map.put(buf[0], buf[1]);
            }
                
            in.close();
        }
        catch(IOException e)
        {
            System.err.println("Could not read from config file '" + configName + "'!");
        }
    }
    
    /**
     * Clears the config map and reloads the properties from the file
     */
    public void reload()
    {
        map.clear();
        load();
    }
    
    /**
     * Sets a property's value. For other variables (like boolean), just type it in string form. e.g. "false".
     * @param key The key of the configuration value to query
     * @param value The value to place in the configuration. This will be formatted to a {@link java.lang.String} using the {@link java.lang.Object#toString()} method.
     * Note that if the intended value is an array of objects, set the value argument as Config.arrayToConfigValue(value)!
     */
    public void setProperty(String key, Object value)
    {
        map.put(key, value.toString());
    }
    
    /**
     * Saves the config currently stored in memory to file. This will override the previous file.
     */
    public void save()
    {
        if(map.isEmpty()) return;
        
        if(!configFile.exists())
            try
            {
                configFile.createNewFile();
            }
            catch(IOException e)
            {
                System.err.println("Could not create mew file for config '" + configName + "'!");
            }
        
        try(PrintWriter out = new PrintWriter(new FileWriter(configFile, false)))
        {
            map.entrySet().stream().forEach((e) -> 
            {
                out.println(e.getKey() + ": " + e.getValue());
            });
            
            out.close();
        }
        catch(IOException e)
        {
            System.err.println("Could not interpret config file '" + configName + "'!");
        }
    }
    
    
    /**
     * Converts an array of Objects to a valid Config property to be used in {@link wrath.util.Config#setProperty(java.lang.String, java.lang.Object) }
     * @param array The array of objects to convert. Each element will be converted to a string using the {@link java.lang.Object#toString()} method then formatted into a proper configuration array.
     * @return Returns the Object that has been properly formatted to be used as a value in  {@link wrath.util.Config#setProperty(java.lang.String, java.lang.Object) }.
     */
    public static Object arrayToConfigValue(Object[] array)
    {
        String str = "";
        for(Object o : array)
            str = str + o.toString() + ",";
        
        return str;
    }
}
