/*
 * file FileHeaderFactory.java
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

import java.io.IOException;
import java.util.*;
import sourceheader.core.HeaderParser.SyntaxErrorException;
import sourceheader.core.parsers.*;

/**
 * {@link FileHeader FileHeader} instances should be created via this factory.
 * 
 * This factory caches instances of same header and chooses
 * correct {@link HeaderParser HeaderParser} implementation according to file extension.
 *
 * @author steve
 */
public class FileHeaderFactory {
    
    private List<HeaderParser> parsers;
    private Map<String, FileHeader> headers = new HashMap<String, FileHeader>();
    private ParsersConfig config;

    /**
     * Initializes factory with one sample parser.
     * @param config Parsing configuration.
     */
    public FileHeaderFactory(ParsersConfig config) {
        this.config = config;
        this.parsers = new ArrayList<HeaderParser>();

        this.parsers.add(new CppParser(config));
    }

    /**
     * Initializes factory.
     * @param config Parsing configuration.
     * @param parsers List of parsers for different file extensions.
     */
    public FileHeaderFactory(ParsersConfig config, HeaderParser[] parsers) {
        this.config = config;
        this.parsers = new ArrayList<HeaderParser>(Arrays.asList(parsers));
    }

    /**
     * @return List of all headers that have been created trought this factory.
     */
    public Collection<FileHeader> getFileHeaders() {
        return this.headers.values();
    }

    /**
     * Creates header only from content where alternating parts
     * should be already replaced with special sequences.
     *
     * @param content Content of file header.
     * @return {@link FileHeader FileHeader} instance that corresponds to given content.
     */
    public FileHeader create(String content) {
        FileHeader result = new FileHeader(content, 
                this.config.getSpecialCharacter());
        FileHeader cached = this.headers.get(result.getHash());

        if (cached != null) {
            return cached;
        }

        this.headers.put(result.getHash(), result);
        return result;
    }

    /**
     * @param file Path on filesystem.
     * @return True if this factory has registered parser for such file.
     * This is based only on file extension.
     */
    public boolean hasParserFor(Path file) {
        return this.findParserFor(file) != null;
    }

    /**
     * Extracts header from given file and creates
     * {@link FileHeader FileHeader} class instance for it
     * and also creates map of real values for alternating parts.
     *
     * Ensures that for equal headers the same instances will be returned.
     *
     * @param path Path on filesystem to file.
     * @return Messenger class with {@link FileHeader FileHeader} instance
     * and map of real values of alternating parts.
     */
    public HeaderParser.HeaderAndAlternatingParts create(Path path)
            throws IOException, SyntaxErrorException {
        HeaderParser.HeaderAndAlternatingParts result =
                new HeaderParser.HeaderAndAlternatingParts();
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

    /**
     * Internal helper method.
     * @param path
     * @return {@link HeaderParser Parser} for file on given path.
     */
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
