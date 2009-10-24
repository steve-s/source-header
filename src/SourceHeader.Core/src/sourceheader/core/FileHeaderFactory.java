/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sourceheader.core;

import java.io.IOException;
import java.util.*;
import sourceheader.core.HeaderParser.SyntaxErrorException;
import sourceheader.core.parsers.*;

/**
 *
 * @author steve
 */
public class FileHeaderFactory {
    
    private List<HeaderParser> parsers;
    private Map<String, FileHeader> headers = new HashMap<String, FileHeader>();
    private ParsersConfig config;

    public FileHeaderFactory(ParsersConfig config) {
        this.config = config;
        this.parsers = new Vector<HeaderParser>();

        this.parsers.add(new CppParser(config));
    }

    public FileHeaderFactory(ParsersConfig config, HeaderParser[] parsers) {
        this.config = config;
        this.parsers = new Vector<HeaderParser>(Arrays.asList(parsers));
    }

    public Iterable<FileHeader> getFileHeaders() {
        return (Iterable<FileHeader>)this.headers.values();
    }

    public FileHeader create(String content) {
        FileHeader result = new FileHeader(content, this.config.getSpecialCharacter());
        FileHeader cached = this.headers.get(result.getHash());

        if (cached != null) {
            return cached;
        }

        this.headers.put(result.getHash(), result);
        return result;
    }

    /**
     * Extracts header from given file and creates
     * @link sourceheader.core#FileHeader FileHeader class instance for it.
     *
     * Ensures that for same headers the same instances will be returned.
     *
     * @param path Path on filesystem to file.
     * @return instance of @link sourceheader.core#FileHeader FileHeader class.
     */
    public FileHeader create(Path path)
            throws IOException, SyntaxErrorException {
        // find correct parser
        HeaderParser parser = this.findParserFor(path);

        if (parser == null) {
            return null;
        }

        return parser.parse(path, this);
        // parser must internally use method create(String content)
        // of this factory, so the pooling of instances of FileHeader
        // is done there.
    }

    private HeaderParser findParserFor(Path path) {
        for (HeaderParser parser : this.parsers) {
            for (String extension : parser.getExtensions()) {
                if (extension.equals(path.getExtension())) {
                    return parser;
                }
            }
        }

        return null;
    }
}
