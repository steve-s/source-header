/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sourceheader.core.tests;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import sourceheader.core.parsers.ParsersConfig;

/**
 *
 * @author steve
 */
public class Utils {
    public static ParsersConfig getParsersConfig() {
        return new ParsersConfig('%');
    }

    public static void writeToFile(String filename, String content) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        writer.write(content);
        writer.close();
    }
}
