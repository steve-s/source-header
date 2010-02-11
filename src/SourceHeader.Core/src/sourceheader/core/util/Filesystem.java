/*
 * file Filesystem.java
 *
 * This file is part of SourceHeader project.
 *
 * SourceHeader is software for easier maintaining source files' headers.
 * project web: http://code.google.com/p/source-header/
 * author: Steve Sindelar
 * licence: New BSD Licence
 * 
 * (c) Steve Sindelar
 */

package sourceheader.core.util;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.Stack;

/**
 * Static class with helper methods that manipulates filesystem.
 *
 * @author steve
 */
public class Filesystem {

    /**
     * Copies text from one file to another.
     * @param source
     * @param destination
     * @param skipLines How many lines from start should be skipped. (For skipping header)
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void copyText(
            File source, 
            File destination, 
            int skipLines)
            throws FileNotFoundException, IOException {    
        copyText(source, destination, skipLines, true);
    }

    /**
     * Copies text from one file to another.
     * @param source
     * @param destination
     * @param skipLines How many lines from start should be skipped. (For skipping header)
     * @param append Append (True) text or rewrite (False).
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void copyText(
            File source,
            File destination,
            int skipLines,
            boolean append)
            throws FileNotFoundException, IOException {
        BufferedWriter writer = null;
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(source));
            writer = new BufferedWriter(new FileWriter(destination, append));

            for(; skipLines>0; skipLines--) {
                reader.readLine();
            }

            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line + "\n");
            }
        }
        finally {
            if (reader != null) {
                reader.close();
            }
            if (writer != null) {
                writer.close();
            }
        }
    }

    /**
     * Copies text from one file to another.
     * @param source
     * @param destination
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void copyText(File source, File destination) 
            throws FileNotFoundException, IOException {
        copyText(source, destination, 0);
    }

    /**
     * Deletes text from given file.
     * @param file
     * @throws IOException
     */
    public static void deleteText(File file) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
        writer.write("", 0, 0);
        writer.close();
    }

    /**
     * Counts files in given folder and it's subfolders.
     * @param file Folder
     * @param maxCount Maximal count of files, if the counter reaches this number
     * method returns maxCount and does not count more.
     * @param fileFilter
     * @return Count of files if lower than maxCount, otherwise maxCount.
     */
    public static long getFilesCount(File file, long maxCount, FileFilter fileFilter)
    {
        if (file.isFile()) {
            return 1;
        }

        long result = 0;
        Stack<File> dirs = new Stack<File>();
        dirs.add(file);
        while (!dirs.empty()) {
            File current = dirs.pop();
            File[] children = current.listFiles();

            for(int i=children.length-1; i>=0; i--) {
                File child = children[i];
                if (!fileFilter.accept(child)) {
                    continue;
                }
                else if (child.isFile()) {
                    result++;
                    if (result >= maxCount) {
                        return result;
                    }
                }
                else if (child.isDirectory()) {
                    dirs.push(child);
                }
            }
        }

        return result;
    }
}
