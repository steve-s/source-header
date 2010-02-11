/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sourceheader.core.util;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.Stack;

/**
 * @author steve
 */
public class Filesystem {

    public static void copyText(
            File source, 
            File destination, 
            int skipLines)
            throws FileNotFoundException, IOException {    
        copyText(source, destination, skipLines, true);
    }

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

    public static void copyText(File source, File destination) 
            throws FileNotFoundException, IOException {
        copyText(source, destination, 0);
    }

    public static void deleteText(File file) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
        writer.write("", 0, 0);
        writer.close();
    }

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