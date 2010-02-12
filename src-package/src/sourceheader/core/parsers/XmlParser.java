/*
 * file XmlParser.java
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
 * Parser for xml and similar (html) languages.
 * 
 * @author steve
 */
public class XmlParser extends AbstractParser {
    public XmlParser(ParsersConfig config) {
        super(config);
    }

    @Override
    protected Iterable<Block> getCommentBlocks() {
        return Arrays.asList(
            new Block[] {
                new Block("<!--", "-->"),
            }
        );
    }

    @Override
    public String[] getExtensions() {
        return new String[] {
            "xml", "xhtml", "html", "htm", "xslt", "xsl", "xsd"};
    }
}
