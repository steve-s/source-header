/*
 * file AlternatingPartsHelper.java
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

package sourceheader.gui.util.preferences;

import java.io.*;
import java.util.*;
import sourceheader.core.parsers.Block;

/**
 * Helper methods for alternating parts configuration.
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

        try {
            new FilePersister("").saveAlternatingParts(writer, getDefaultAlternatingParts());
        } catch(Exception ex) {
            throw new IOException(ex);
        }

        writer.flush();
        writer.close();
    }

    /**
     * @return The map of default alternating parts.
     */
    public static Map<String, Block> getDefaultAlternatingParts() {
        Map<String, Block> result = new HashMap<String, Block>();

        result.put("description-tag", new Block("<description>", "</description>"));
        result.put("package", new Block("@package", "\n"));
        result.put("category", new Block("@category", "\n"));
        result.put("subcategory", new Block("@subcategory", "\n"));
        result.put("group", new Block("@group", "\n"));
        result.put("subgroup", new Block("@subgroup", "\n"));

        return result;
    }
}
