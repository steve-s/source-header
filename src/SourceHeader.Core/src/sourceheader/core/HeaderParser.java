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
        public SyntaxErrorException(String filename) {
            super("There is syntax error in input file " + filename + ".");
        }

        public SyntaxErrorException() {
            super("There is syntax error in input file.");
        }
    }
}
