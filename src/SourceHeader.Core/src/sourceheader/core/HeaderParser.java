/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sourceheader.core;

import java.io.*;

/**
 *
 * @author steve
 */
public interface HeaderParser {
    FileHeader parse(Path path, FileHeaderFactory factory) 
            throws IOException, SyntaxErrorException;
    FileHeader parse(Reader reader, FileHeaderFactory factory) 
            throws IOException, SyntaxErrorException;
    String[] getExtensions();

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
