/*
 * file HeaderParser.java
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

package sourceheader.core;

import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * Interface for parser that extracts {@link FileHeader header} from source file.
 *
 * @author steve
 */
public interface HeaderParser {

    /**
     * Parses source file with given path.
     * @param path File's path.
     * @param factory Factory for header creation.
     * @return Instance of class that ecapsulates header instance and map of
     * content of alternating parts.
     * @throws IOException
     * @throws sourceheader.core.HeaderParser.SyntaxErrorException
     */
    HeaderAndAlternatingParts parse(Path path, FileHeaderFactory factory)
            throws IOException, SyntaxErrorException;

    /**
     * Parses source from reader.
     * @param reader
     * @param factory Factory for header creation.
     * @return Instance of class that ecapsulates header instance and map of
     * content of alternating parts.
     * @throws IOException
     * @throws sourceheader.core.HeaderParser.SyntaxErrorException
     */
    HeaderAndAlternatingParts parse(Reader reader, FileHeaderFactory factory)
            throws IOException, SyntaxErrorException;

    /**
     * @return List of file extensions supported by parser.
     */
    String[] getExtensions();

    /**
     * Simple messenger encapsulates 2 values - return value of createHeader.
     */
    public static class HeaderAndAlternatingParts {
        public FileHeader header;
        public Map<String, List<String>> alternatingParts;
    }

    /**
     * Parser may find systax error in source code.
     */
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
