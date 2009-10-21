/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sourceheader.core.util;

import java.io.*;

/**
 * @author steve
 */
public class Filesystem {
    public static void copyText(File source, File destination, int skipLines)
            throws FileNotFoundException, IOException {
        BufferedWriter writer = null;
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(source));
            writer = new BufferedWriter(new FileWriter(destination, true));

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
}
