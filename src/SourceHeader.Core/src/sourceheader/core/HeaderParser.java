/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sourceheader.core;

import java.io.*;
import java.util.List;
import java.util.Map;

/**
 *
 * @author steve
 */
public interface HeaderParser {
    HeaderAndAlternatingParts parse(Path path, FileHeaderFactory factory)
            throws IOException, SyntaxErrorException;

    HeaderAndAlternatingParts parse(Reader reader, FileHeaderFactory factory)
            throws IOException, SyntaxErrorException;

    String[] getExtensions();

    /**
     * Simple messenger encapsulates 2 values - return value of createHeader.
     */
    public static class HeaderAndAlternatingParts {
        public FileHeader header;
        public Map<String, List<String>> alternatingParts;
    }

    public class SyntaxErrorException extends Exception {
        private Path path = null;

        public Path getFile() {
            return path;
        }

        public SyntaxErrorException(Path file, Throwable ex) {            
            super("There is syntax error in input file " + 
                    file.getAbsolutePath() + ". Detail: " +
                    ex.getMessage());
            this.path = file;
        }

        public SyntaxErrorException(String message) {
            super(message);
        }

        public SyntaxErrorException(Path file) {
            super("There is syntax error in input file " +
                    file.getAbsolutePath() + ".");
            this.path = file;
        }
    }
}
