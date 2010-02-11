/*
 * file AbstractParserWithAlternatingBlocksTest.java
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

package sourceheader.core.parsers.tests;

import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Map.Entry;
import org.junit.Test;
import static org.junit.Assert.*;
import sourceheader.core.HeaderParser.SyntaxErrorException;
import sourceheader.core.parsers.*;
import sourceheader.core.*;
import sourceheader.core.tests.Utils;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author steve
 */
public class AbstractParserWithAlternatingBlocksTest {

    @Test
    public void test_parse_with_filename_in_content() throws IOException, SyntaxErrorException {
        Map<String, Block> parts = new HashMap<String, Block>();
        ParsersConfig config = new ParsersConfig('%', parts);

        AbstractParser parser =
            new AbstractParser(config) {
                @Override
                protected Iterable<Block> getCommentBlocks() {
                    return (Iterable<Block>)
                            Arrays.asList(new Block[]{ new Block("/*","*/")});
                }

                @Override
                public String[] getExtensions() {
                    throw new UnsupportedOperationException("Not supported yet.");
                }
        };

        FileHeaderFactory factory =
                new FileHeaderFactory(Utils.getParsersConfig(),
                    new HeaderParser[] {});

        Reader reader =
                new StringReader("/* \n Hello filename.java world!\n" +
                                "filename.java */ \n");

        HeaderParser.HeaderAndAlternatingParts result =
                parser.parse(reader, factory, "filename.java");

        assertEquals("/* \n Hello %filename% world!\n" +
                "%filename% */",
                result.header.getContent());
    }

    @Test
    public void test_parse_with_alternating_block_in_comment()
            throws IOException, SyntaxErrorException {

        Map<String, Block> parts = new HashMap<String, Block>();
        parts.put("desc", new Block("<desc>", "</desc>"));
        ParsersConfig config = new ParsersConfig('%', parts);

        AbstractParser parser =
            new AbstractParser(config) {
                @Override
                protected Iterable<Block> getCommentBlocks() {
                    return (Iterable<Block>)
                            Arrays.asList(new Block[]{ new Block("/*","*/")});
                }

                @Override
                public String[] getExtensions() {
                    throw new UnsupportedOperationException("Not supported yet.");
                }
        };

        FileHeaderFactory factory =
                new FileHeaderFactory(Utils.getParsersConfig(), 
                    new HeaderParser[] {});

        Reader reader =
                new StringReader("/* \n <desc>alternating-tet</desc> */ \n");

        HeaderParser.HeaderAndAlternatingParts result =
                parser.parse(reader, factory);

        assertEquals("/* \n <desc>%desc%0%</desc> */",
                result.header.getContent());

        assertEquals(1, result.alternatingParts.size());

        Entry<String, List<String>> part = 
                result.alternatingParts.entrySet().iterator().next();
        assertEquals("desc",part.getKey());
        assertEquals(1, part.getValue().size());
        assertEquals("alternating-tet", part.getValue().iterator().next());
    }
}
