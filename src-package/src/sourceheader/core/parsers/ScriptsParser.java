/*
 * file ScriptsParser.java
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

package sourceheader.core.parsers;

import java.util.Arrays;

/**
 * Parser for languages that support C-style comments and hash (#) comments.
 *
 * @author steve
 */
public class ScriptsParser extends AbstractParser {

    public ScriptsParser(ParsersConfig config) {
        super(config);
    }

    @Override
    protected Iterable<Block> getCommentBlocks() {
        return Arrays.asList(
            new Block[] {
                new Block("/*", "*/"),
                new Block("//", "\n"),
                new Block("#", "\n")
            }
        );
    }

    @Override
    public String[] getExtensions() {
        return new String[] {
            "sh", "bash",
            "js"};
    }
}
