/*
 * file CppParser.java
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

import java.util.*;

/**
 * Parser for programming languages with C-style syntax.
 * For those languages that support even hash (#) comments
 * there is ScriptParser.
 *
 * @author steve
 */
public class CppParser extends AbstractParser {

    public CppParser(ParsersConfig config) {
        super(config);
    }

    @Override
    protected Iterable<Block> getCommentBlocks() {
        return Arrays.asList(
            new Block[] { 
                new Block("/*", "*/"),
                new Block("//", "\n")
            }
        );
    }

    @Override
    public String[] getExtensions() {
        return new String[] {
            "cpp", "hpp",
            "h", "c",
            "java", "cs" };
    }
}
