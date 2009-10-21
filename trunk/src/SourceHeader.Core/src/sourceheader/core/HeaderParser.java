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
    FileHeader parse(Path path, FileHeaderFactory factory) throws IOException;
    FileHeader parse(Reader reader, FileHeaderFactory factory) throws IOException;
    String[] getExtensions();
}
