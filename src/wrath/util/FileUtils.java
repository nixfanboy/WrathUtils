/**
 *  Wrath Engine Utility Library  
 *  Copyright (C) 2014  Dark Paradox Games
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Standard library to easily handle common file operations.
 * @author Epictaco
 */
public class FileUtils
{   
    /**
     * Static library, objects are unnecessary.
     */
    private FileUtils(){}
    
    public static final int COMPRESSION_WORST = 1;
    public static final int COMPRESSION_NORMAL = 5;
    public static final int COMPRESSION_BEST = 9;
    
    /**
     * Compresses a directory into a .zip. Currently does not support sub-directories.
     * @param directory The directory to compress.
     * @param archiveName The desired name of the .zip file (including the .zip extension).
     * @param compressionLevel How compressed you would like it. Higher the number, the more CPU power it takes.
     */
    public static void compressDirectory(File directory, String archiveName, int compressionLevel)
    {
        if(!directory.isDirectory() || !directory.exists()) return;
        
        compressFiles(directory.listFiles(), archiveName, compressionLevel);
    }
    
    /**
     * Compresses a single file into a ZIP archive. Makes a dedicated thread for efficiency.
     * @param file The file to be compressed into a ZIP Format. Cannot be a directory.
     * @param archiveName The desired name of the .zip file (including the .zip extension).
     * @param compressionLevel How compressed you would like it. Higher the number, the more CPU power it takes.
     */
    public static void compressFile(File file, String archiveName, int compressionLevel)       
    {
        Thread cThread = new Thread(() -> 
        {
            File out = new File(file.getParent() + "/" + archiveName);
            if(!out.exists())
            {
                try 
                {
                    out.createNewFile();
                }
                catch (IOException ex)
                {
                    Logger.getErrorLogger().write("Could not create new file to archive '" + file.getName() + "'!");
                    return;
                }
            }
        
            ZipOutputStream zos = null;
        
            try 
            {
                zos = new ZipOutputStream(new FileOutputStream(out));
            }
            catch (FileNotFoundException ex)
            {
                Logger.getErrorLogger().write("Could not find pre-made file '" + archiveName + "' Prehaps an unknown I/O Error occured?!");
                return;
            }
        
            if(zos == null) return;
        
            zos.setLevel(compressionLevel);
        
            // Reading the file
            byte[] buffer = new byte[1024];
            ZipEntry ent = new ZipEntry(file.getName());
        
            try(FileInputStream in = new FileInputStream(file))
            {
                zos.putNextEntry(ent);
                int len;
                while((len = in.read(buffer)) > 0)
                {
                    zos.write(buffer, 0, len);
                }
            
                zos.closeEntry();
                zos.close();
            }
            catch (IOException ex)
            {
                Logger.getErrorLogger().write("Could not put next entry while compressing archive '" + archiveName + "'!");
            }
        });
        
        cThread.start();
    }
    
    /**
     * Compresses numerous files into a ZIP archive. Makes a dedicated thread for efficiency.
     * @param files List of files to be compressed. Cannot contain a directory.
     * @param archiveName The desired name of the .zip file (including the .zip extension).
     * @param compressionLevel How compressed you would like it. Higher the number, the more CPU power it takes.
     */
    public static void compressFiles(File[] files, String archiveName, int compressionLevel)
    {
        if(files.length < 1) return;
        
        Thread cThread = new Thread(() -> 
        {
            File out = new File(files[0].getParent() + "/" + archiveName);
            if(!out.exists())
            {
                try
                {
                    out.createNewFile();
                }
                catch(IOException e)
                {
                    Logger.getErrorLogger().write("Could not create new archive '" + archiveName + "' for compression!");
                    return;
                }
            }
        
            ZipOutputStream zos;
            try 
            {
                zos = new ZipOutputStream(new FileOutputStream(out));
            }
            catch (FileNotFoundException ex)
            {
                Logger.getErrorLogger().write("Could not find pre-made file '" + archiveName + "' Prehaps an unknown I/O Error occured?!");
                return;
            }
        
            //Write to file
            zos.setLevel(compressionLevel);
        
            try
            {
                byte[] buffer = new byte[1024];
                int len;
                for(File current : files)
                {
                    ZipEntry ze = new ZipEntry(current.getName());
                    zos.putNextEntry(ze);
                    FileInputStream in = new FileInputStream(current);
                
                    while((len = in.read(buffer)) > 0)
                    {
                        zos.write(buffer, 0, len);
                    }
                
                    zos.closeEntry();
                    in.close();
                }
            
                zos.close();
            }
            catch(IOException e)
            {
                Logger.getErrorLogger().write("Encountered an error while compressing '" + archiveName + "'! Could not add entries to archive!");
            }
        });
        
        cThread.start();
    }

    /**
     * Decompresses .ZIP files. This method returns a field, therefore it cannot have a thread.
     * @param file .zip file to decompress.
     * @return List of files decompressed as a result.
     */
    public static File[] decompressFile(File file)
    {
        ArrayList<File> fileList = new ArrayList<>();
        File outDir = new File(file.getParent());
        
        ZipInputStream in = null;
        try
        {
            in = new ZipInputStream(new FileInputStream(file));
        }
        catch(FileNotFoundException e)
        {
            Logger.getErrorLogger().write("Could not find file '" + file.getName() + "' to decompress!");
        }
        if(in == null) return null;
        
        ZipEntry ze;
        try
        {
           ze = in.getNextEntry();
           while(ze != null)
           {
               File output = new File(outDir + "/" + ze.getName());
               new File(output.getParent()).mkdirs();
               
               FileOutputStream fos = new FileOutputStream(output);
               
               byte[] buffer = new byte[1024];
               int len;
               while((len = in.read(buffer)) > 0)
               {
                   fos.write(buffer, 0, len);
               }
               
               fos.close();
               
               fileList.add(output);
               ze = in.getNextEntry();
           }
           
           in.closeEntry();
           in.close();
        }
        catch(IOException e)
        {
            Logger.getErrorLogger().write("Could not decompress file '" + file.getName() + "'! Zip Entry error!");
        }
        
        File[] rl = new File[fileList.size()];
        
        for(int x = 0; x < fileList.size(); x++)
        {
            rl[x] = fileList.get(x);
        }
        
        return rl;
    }
    
    /**
     * Convenience Method for a certain developer.
     * @param filename The path of the file
     * @return returns the requested file data in the form of a String.
     * @throws Exception In the case the file could not be read.
     */
    public static String readFileAsString(String filename) throws Exception
    {
        StringBuilder source = new StringBuilder();
        FileInputStream in = new FileInputStream(filename);
        Exception exception = null;
        BufferedReader reader;
        
        try
        {
            reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            Exception innerExc = null;
            try
            {
                String line;
                while ((line = reader.readLine()) != null)
                {
                    source.append(line).append('\n');
                }
            } 
            catch (Exception exc)
            {
                exception = exc;
            } 
            finally
            {
                try
                {
                    reader.close();
                } 
                catch (Exception exc)
                {
                    if (innerExc == null)
                    {
                        innerExc = exc;
                    } 
                    else
                    {
                        exc.printStackTrace();
                    }
                }
            }
            if (innerExc != null)
            {
                throw innerExc;
            }
        } 
        catch (Exception exc)
        {
            exception = exc;
        } 
        finally
        {
            try
            {
                in.close();
            } 
            catch (Exception exc)
            {
                if (exception == null)
                {
                    exception = exc;
                } 
                else
                {
                    exc.printStackTrace();
                }
            }
            if (exception != null)
            {
                throw exception;
            }
        }
        return source.toString();
    }

    /**
     * Convenience Method to write a single string to a file.
     * Do not use repeatedly or to write multiple strings to a file. 
     * Instead use writeStringsToFile(File file, String[] outputs).
     * @param file The file to write to.
     * @param output The string you wish to write to the file.
     */
    public static void writeStringToFile(File file, String output)
    {
        PrintWriter out = null;
        
        try
        {
            out = new PrintWriter(new FileWriter(file, true));
        }
        catch(IOException e)
        {
            Logger.getErrorLogger().write("Could not write string to file: failed to open stream.");
        }
        
        if(out != null)
        {
            out.println(output);
            out.close();
        }
        else
        {
            Logger.getErrorLogger().write("Could not write string to file: stream is null!");
        }
    }
    
    /**
     * Convenience Method to write numerous strings to a file.
     * Strings will be written in order of the array.
     * Each string is a new line.
     * @param file The file to write to.
     * @param outputs The strings to write to the file.
     */
    public static void writeStringsToFile(File file, String[] outputs)
    {
        PrintWriter out = null;
        
        try
        {
            out = new PrintWriter(new FileWriter(file, true));
        }
        catch(IOException e)
        {
            Logger.getErrorLogger().write("Could not write strings to file: failed to open stream.");
        }
        
        if(out != null)
        {
            for(String c : outputs)
            {
                out.println(c);
            }
            out.close();
        }
        else
        {
            Logger.getErrorLogger().write("Could not write strings to file: stream is null!");
        }
    }
}
