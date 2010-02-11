/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
