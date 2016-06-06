/**
 * The MIT License (MIT)
 * Wrath Utils Copyright (c) 2016 Trent Spears
 */
package wrath.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Flexible and expandable logging system. Includes Time stamps, console verbose and file logging.
 * @author Trent Spears
 */
public class Logger extends PrintStream implements LogFilter
{
    public static enum TimestampFormat
    {
        STANDARD,
        US;
    }
    
    public static final PrintStream SYS_ERR = System.err;
    public static final PrintStream SYS_OUT = System.out;
    private static final Calendar NOW = Calendar.getInstance();
    
    private boolean closed = false;
    private final boolean console;
    private final LogFilter filter;
    private final DateFormat format;
    private PrintWriter fout;
    private final boolean time;
    
    /**
     * Constructor.
     */
    public Logger()
    {
        this(null, null, true, TimestampFormat.STANDARD, true);
    }
    
    /**
     * Constructor.
     * @param timeStampFormat The {@link wrath.util.Logger.TimestampFormat} to write the timestamp as.
     */
    public Logger(TimestampFormat timeStampFormat)
    {
        this(null, null, true, timeStampFormat, true);
    }
    
    /**
     * Constructor.
     * @param filter The {@link wrath.util.LogFilter} to filter console and log messages.
     */
    public Logger(LogFilter filter)
    {
        this(null, filter, true, TimestampFormat.STANDARD, true);
    }
    
    /**
     * Constructor.
     * @param filter The {@link wrath.util.LogFilter} to filter console and log messages.
     * @param timeStampFormat The {@link wrath.util.Logger.TimestampFormat} to write the timestamp as.
     */
    public Logger(LogFilter filter, TimestampFormat timeStampFormat)
    {
        this(null, filter, true, timeStampFormat, true);
    }
    
    /**
     * Constructor.
     * @param timeStamp If true, a time stamp and the name of the logger will be displayed before the output is printed.
     */
    public Logger(boolean timeStamp)
    {
        this(null, null, timeStamp, TimestampFormat.STANDARD, true);
    }
    
    /**
     * Constructor.
     * @param filter The {@link wrath.util.LogFilter} to filter console and log messages.
     * @param timeStamp If true, a time stamp and the name of the logger will be displayed before the output is printed.
     */
    public Logger(LogFilter filter, boolean timeStamp)
    {
        this(null, filter, timeStamp, TimestampFormat.STANDARD, true);
    }
    
    /**
     * Constructor.
     * @param logFile The {@link java.io.File} to save the log to. Will not write to file if null.
     */
    public Logger(File logFile)
    {
        this(logFile, null, true, TimestampFormat.STANDARD, true);
    }
    
    /**
     * Constructor.
     * @param logFile The {@link java.io.File} to save the log to. Will not write to file if null.
     * @param timeStampFormat The {@link wrath.util.Logger.TimestampFormat} to write the timestamp as.
     */
    public Logger(File logFile, TimestampFormat timeStampFormat)
    {
        this(logFile, null, true, timeStampFormat, true);
    }
    
    /**
     * Constructor.
     * @param logFile The {@link java.io.File} to save the log to. Will not write to file if null.
     * @param timeStamp If true, a time stamp and the name of the logger will be displayed before the output is printed.
     */
    public Logger(File logFile, boolean timeStamp)
    {
        this(logFile, null, timeStamp, TimestampFormat.STANDARD, true);
    }
    
    /**
     * Constructor.
     * @param logFile The {@link java.io.File} to save the log to. Will not write to file if null.
     * @param filter The {@link wrath.util.LogFilter} to filter console and log messages.
     */
    public Logger(File logFile, LogFilter filter)
    {
        this(logFile, filter, true, TimestampFormat.STANDARD, true);
    }
    
    /**
     * Constructor.
     * @param logFile The {@link java.io.File} to save the log to. Will not write to file if null.
     * @param filter The {@link wrath.util.LogFilter} to filter console and log messages.
     * @param timeStampFormat The {@link wrath.util.Logger.TimestampFormat} to write the timestamp as.
     */
    public Logger(File logFile, LogFilter filter, TimestampFormat timeStampFormat)
    {
        this(logFile, filter, true, timeStampFormat, true);
    }
    
    /**
     * Constructor.
     * @param logFile The {@link java.io.File} to save the log to. Will not write to file if null.
     * @param filter The {@link wrath.util.LogFilter} to filter console and log messages.
     * @param timeStamp If true, a time stamp and the name of the logger will be displayed before the output is printed.
     */
    public Logger(File logFile, LogFilter filter, boolean timeStamp)
    {
        this(logFile, filter, timeStamp, TimestampFormat.STANDARD, true);
    }
    
    /**
     * Constructor.
     * @param logFile The {@link java.io.File} to save the log to. Will not write to file if null.
     * @param filter The {@link wrath.util.LogFilter} to filter console and log messages.
     * @param timeStamp If true, a time stamp and the name of the logger will be displayed before the output is printed.
     * @param timeStampFormat The {@link wrath.util.Logger.TimestampFormat} to write the timestamp as.
     * @param writeToConsole If true, outputs to the console.
     */
    public Logger(File logFile, LogFilter filter, boolean timeStamp, TimestampFormat timeStampFormat, boolean writeToConsole)
    {
        super(SYS_OUT, true);
        
        this.console = writeToConsole;
        this.time = timeStamp;
        
        if(filter != null) this.filter = filter;
        else this.filter = this;
        
        if(timeStampFormat == TimestampFormat.STANDARD) format = new SimpleDateFormat("[dd/MM/yyyy][HH:mm:ss]");
        else format = new SimpleDateFormat("[MM/dd/yyyy][HH:mm:ss]");
        
        if(logFile != null)
        {
            if(logFile.getParentFile() != null && !logFile.getParentFile().exists()) logFile.getParentFile().mkdirs();
            
            if(!logFile.exists())
                try
                {
                    logFile.createNewFile();
                }
                catch(IOException e)
                {
                    System.err.println("] ERROR: Could not create new file for logger '" + logFile.getName() + "'! I/O Error!");
                }
            
            try
            {
                fout = new PrintWriter(new FileWriter(logFile, true), true);
            }
            catch(IOException ex)
            {
                System.err.println("] ERROR: Could not log to file for logger '" + logFile.getName() + "'! I/O Error!");
            }
        }
    }
    
    @Override
    public void close()
    {
        if(closed) return;
        closed = true;
        if(fout != null) fout.close();
    }
    
    @Override
    public String filterConsole(String message)
    {
        return message;
    }
    
    @Override
    public String filterLog(String message)
    {
        return message;
    }
    
    /**
     * If true, the logger is saved and closed, and cannot be written to anymore.
     * @return Returns true if the logger is closed.
     */
    public boolean isClosed()
    {
        return closed;
    }
    
    @Override
    public void print(boolean b)
    {
        print(b + "");
    }
    
    @Override
    public void print(char x)
    {
        print(x + "");
    }
    
    @Override
    public void print(char[] chars)
    {
        print(new String(chars));
    }
    
    @Override
    public void print(double d)
    {
        print(d + "");
    }
    
    @Override
    public void print(float f)
    {
        print(f + "");
    }
    
    @Override
    public void print(int i)
    {
        print(i + "");
    }
    
    @Override
    public void print(long l)
    {
        print(l + "");
    }
    
    @Override
    public void print(Object o)
    {
        print(o.toString());
    }
    
    @Override
    public void println(boolean b)
    {
        println(b + "");
    }
    
    @Override
    public void println(char x)
    {
        println(x + "");
    }
    
    @Override
    public void println(char[] chars)
    {
        println(new String(chars));
    }
    
    @Override
    public void println(double d)
    {
        println(d + "");
    }
    
    @Override
    public void println(float f)
    {
        println(f + "");
    }
    
    @Override
    public void println(int i)
    {
        println(i + "");
    }
    
    @Override
    public void println(long l)
    {
        println(l + "");
    }
    
    @Override
    public void println(Object o)
    {
        println(o.toString());
    }
    
    @Override
    public void print(String string)
    {
        if(console)
        {
            String prnt = filter.filterConsole(string);
            if(prnt != null)
            {
                if(time) super.print(format.format(NOW.getTime()) + " " + prnt);
                else super.print(prnt);
            }
        }
        
        if(fout != null && !closed)
        {
            String fin = filter.filterLog(string);
            if(fin != null)
            {
                fout.println(format.format(NOW.getTime()) + " " + string);
                fout.flush();
            }
        }
    }
    
    @Override
    public PrintStream printf(String string, Object...args)
    {
        if(console)
        {
            String prnt = filter.filterConsole(string);
            if(prnt != null)
            {
                if(time) super.printf(format.format(NOW.getTime()) + " " + prnt, args);
                else super.printf(prnt, args);
            }
        }
        
        if(fout != null && !closed)
        {
            String fin = filter.filterLog(string);
            if(fin != null)
            {
                fout.printf(format.format(NOW.getTime()) + " " + string, args);
                fout.flush();
            }
        }
        return this;
    }
    
    @Override
    public void println(String string)
    {
        print(string + '\n');
    }
    
    /**
     * Changes the System's standard output, to replace System.out.
     * @param logger The {@link wrath.util.Logger} to make the system's standard output. If null, resets the System.out logger back to default.
     */
    public static void registerOutputLogger(Logger logger)
    {
        if(logger == null) System.setOut(SYS_OUT);
        System.setOut(logger);
    }
    
    /**
     * Changes the System's standard error output, to replace System.err.
     * @param logger The {@link wrath.util.Logger} to make the system's standard error output. If null, resets the System.err logger back to default.
     */
    public static void registerErrorLogger(Logger logger)
    {
        if(logger == null) System.setErr(SYS_ERR);
        System.setErr(logger);
    }
}