/*
 * file PhpParser.java
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
 * Php has specific syntax of headers because they must start with
 * '&gt;?php' or '&gt?'.
 *
 * @author steve
 */
public class PhpParser extends AbstractParser {
    
    public PhpParser(ParsersConfig config) {
        super(config);
    }

    @Override
    protected Iterable<Block> getCommentBlocks() {
        return Arrays.asList(
            new Block[] {
                new Block("/*", "*/"),
                new Block("//", "\n"),
                new Block("#", "\n"),
                new Block("<?", ""),
                new Block("php", "")
            }
        );
    }

    @Override
    public String[] getExtensions() {
        return new String[] {
            "php", "php4", "php5", "php6", "php3"};
    }
}
