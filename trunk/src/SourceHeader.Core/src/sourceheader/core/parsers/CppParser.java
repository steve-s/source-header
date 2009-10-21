/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sourceheader.core.parsers;

import java.io.*;
import sourceheader.core.*;

/**
 *
 * @author steve
 */
public class CppParser implements HeaderParser {

    public FileHeader parse(Path path) {
        return null;
    }

    public String[] getExtensions() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public FileHeader parse(Path path, FileHeaderFactory factory) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public FileHeader parse(Reader reader, FileHeaderFactory factory) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
