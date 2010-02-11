/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sourceheader.core.parsers;

import java.util.Arrays;

/**
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
