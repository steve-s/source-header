/*
 * file Utils.java
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

package sourceheader.core.tests;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import sourceheader.core.parsers.Block;
import sourceheader.core.parsers.ParsersConfig;

/**
 *
 * @author steve
 */
public class Utils {
    public static ParsersConfig getParsersConfig() {
        Map<String, Block> parts = new HashMap<String, Block>();
        parts.put("desc", new Block("<description>", "</description>"));
        parts.put("cat", new Block("@category", "\n"));
        return new ParsersConfig('%', parts);
    }

    public static void writeToFile(String filename, String content) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        writer.write(content);
        writer.close();
    }
}
