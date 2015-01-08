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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Flexible and expandable logging system. Includes Time stamps, console verbose and file logging.
 * @author Trent Spears
 */
public class Logger 
{
    private static final Logger errLogger = new Logger("error", true, true);
    private static final DateFormat format = new SimpleDateFormat("[MM/dd/yyyy][HH:mm:ss]");
    private static final Calendar now = Calendar.getInstance();
    
    /**
     * Gets the standard output stream/logging object to report errors to console and file.
     * @return Returns the Standard error logger
     */
    public static Logger getErrorLogger()
    {
        return errLogger;
    }
    
    private boolean closed = false;
    private final File loggerFile;
    private final String loggerName;
    private PrintWriter out;
    private boolean verboseToConsole = true;
    private boolean writeToFile = true;
    
    /**
     * The name of the logger. The file will be (root)/etc/logs/(name).log
     * @param loggerName The name of the Logger
     */
    public Logger(String loggerName)
    {
        this(loggerName, true, true);
    }
    
    /**
     * The name of the logger. The file will be (root)/etc/logs/(name).log
     * @param loggerName The name of the Logger
     * @param verboseToConsole specifies whether this logger writes to console.
     * @param writeToFile specifies whether this logger writes out to a file.
     */
    public Logger(String loggerName, boolean verboseToConsole, boolean writeToFile)
    {   
        this.loggerName = loggerName;
        this.verboseToConsole = verboseToConsole;
        loggerFile = new File("etc/logs/" + loggerName + ".log");
        
        if(writeToFile)
        {
            File dir = new File("etc/logs");
            if(!dir.exists()) dir.mkdirs();
        
            if(!loggerFile.exists())
            {
                try
                {
                    loggerFile.createNewFile();
                }
                catch(IOException e)
                {
                    if(loggerName.equals("ERROR")) System.out.println("Could not create standard ERROR logger!");
                    else errLogger.log("Could not create file for logger '" + loggerName + "'!");
                }
            }
        }
    }
    
    /**
     * Closes the Logger and its output streams.
     */
    public void close()
    {
        if(closed) return;
        closed = true;
        out.println();
        if(out != null) out.close();
    }
    
    /**
     * To write to the console, depending on Logger options, it will write to console and/or file.
     * Timestamping and prefixing will be done automatically.
     * @param message The message to write.
     */
    public void log(String message)
    {
        if(closed) return;
        if(!verboseToConsole && !writeToFile) return;
        
        String prnt = format.format(now.getTime()) + " " + message;
        if(verboseToConsole) System.out.println("[" + loggerName.toUpperCase() + "] " + prnt);
        
        if(writeToFile)
        {
            if(out == null)
            {
                try
                {
                    out = new PrintWriter(new FileWriter(loggerFile, true), true);
                }
                catch(IOException e)
                {
                    writeToFile = false;
                    log("Cannot write to log file! Could not bind stream writer!");
                }
            }
            
            out.println(prnt);
        }
    }
}
