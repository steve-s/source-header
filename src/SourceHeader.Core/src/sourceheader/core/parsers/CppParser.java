/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sourceheader.core.parsers;

import java.util.*;

/**
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
