/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sourceheader.gui.util.preferences;

import java.io.*;
import java.util.regex.Matcher;

/**
 *
 * @author steve
 */
public class AlternatingPartsHelper {
    /**
     * Decodes escape sequences to their real equivalent.
     * @param text
     * @return Encoded text.
     */
    public static String decode(String text) {
        return text.replaceAll("\\\\n", "\n");
    }

    /**
     * Encodes newlines to escape sequences.
     * @param text
     * @return Encoded text.
     */
    public static String encode(String text) {
        return text.replaceAll("\n", "\\\\n");
    }

    /**
     * Saves default configuration with comments into specified file.
     * @param filename
     * @throws IOException
     */
    public static void saveDefaultConfigTo(String filename) throws IOException {
        PrintWriter writer = new PrintWriter(filename);

        writer.println("# This is alternating blocks configuration file. ");
        writer.println("# This is configuration for program SourceHeader. ");
        writer.println("# Syntax is following: ");
        writer.println("#     block-name[newline]block-start[newline]block-end[newline][than other blocks, comment, newlines],");
        writer.println("#     newline as part of block-start or block-end may be entered as '\\n',");
        writer.println("#     empty lines and one line comments ignored when not inside one block config,");
        writer.println("#     one line comment starts with # (must be first character on line).");

        writer.println();
        writer.println();

        writer.println("description-tag");
        writer.println("<description>");
        writer.println("</description>");

        writer.println();

        writer.println("package");
        writer.println("@package");
        writer.println("\\n");

        writer.println();

        writer.println("category");
        writer.println("@category");
        writer.println("\\n");

        writer.println();

        writer.println("subcategory");
        writer.println("@subcategory");
        writer.println("\\n");

        writer.flush();
        writer.close();
    }
}