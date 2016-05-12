/**
 * The MIT License (MIT)
 * Wrath Utils Copyright (c) 2016 Trent Spears
 */
package wrath.util;

/**
 * Interface to filter Logger messages.
 * @author Trent Spears
 */
public interface LogFilter
{
    /**
     * Takes in original String and returns modified String to be printed.
     * If method returns null, nothing will be printed to the console.
     * @param message The original message to be decided whether to print or not.
     * @return If a String is returned, it is what will be printed to the console. If returns null, nothing is printed.
     */
    public String filterConsole(String message);
    
    /**
     * Takes in original String and returns modified String to be printed.
     * If method returns null, nothing will be printed to the log file.
     * @param message The original message to be decided whether to print or not.
     * @return If a String is returned, it is what will be printed to the log file. If returns null, nothing is printed.
     */
    public String filterLog(String message);
}