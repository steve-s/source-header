/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sourceheader.core.parsers.tests;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import sourceheader.core.parsers.AbstractParser;
import sourceheader.core.parsers.Block;
import sourceheader.core.tests.Utils;

/**
 *
 * @author steve
 */
class AbstractParserUtils {
    public static AbstractParser getParserWithStubCommentBlock(
            final String start,
            final String end) {
        return new AbstractParser(Utils.getParsersConfig()) {
            @Override
            protected Iterable<Block> getCommentBlocks() {
                return Arrays.asList(new Block[]{new Block(start, end)});
            }

            @Override
            public String[] getExtensions() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
    }

    static public StringReader getStringReader(final String str) {
        // StringReader.ready does not work correctly? strange.
        return new StringReader(str) {
            private int i = 0;

            @Override
            public boolean ready() {
                return i<str.length();
                // StringReader.ready does not work correctly? strange.
            }

            @Override
            public int read() throws IOException {
                this.i++;
                return super.read();
            }
        };
    }
}
