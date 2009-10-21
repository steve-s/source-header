/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sourceheader.core.parsers.tests;

import java.io.*;
import java.io.Reader;
import java.util.Arrays;
import org.junit.Test;
import sourceheader.core.*;
import sourceheader.core.parsers.*;
import static org.junit.Assert.*;

/**
 *
 * @author steve
 */
public class AbstractParserTest {
    
    @Test
    public void test_parser_when_header_is_finished_by_couple_new_lines()
            throws IOException {
        AbstractParser parser = new AbstractParser() {
            @Override
            protected Iterable<Block> getCommentBlocks() {
                return Arrays.asList(new Block[]{new Block("/*", "*/")});
            }

            @Override
            public String[] getExtensions() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        
        // create the factory stub
        FileHeaderFactory factory =
                new FileHeaderFactory(new HeaderParser[] {});
        
        Reader reader = 
                new StringReader("\n\n\t\t  " + 
                "\n/*This is really\n\tcute\n\n header*/\n\n\n" +
                "/*3 new lines should exclude this from header*/\n\n #include;");

        FileHeader header = parser.parse(reader, factory);

        assertEquals("\n\n\t\t  \n/*This is really\n\tcute\n\n header*/",
                header.getContent());
    }

    @Test
    public void test_parse_when_header_is_finished_by_non_comment_text() throws IOException {
        AbstractParser parser = new AbstractParser() {
            @Override
            protected Iterable<Block> getCommentBlocks() {
                return Arrays.asList(new Block[]{new Block("/*", "*/")});
            }

            @Override
            public String[] getExtensions() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };

        // create the factory stub
        FileHeaderFactory factory =
                new FileHeaderFactory(new HeaderParser[] {});

        Reader reader =
                new StringReader(
                "\n/*This is another\n *\n *header\n */#include \"xx.hpp\";\n");

        FileHeader header = parser.parse(reader, factory);

        assertEquals("\n/*This is another\n *\n *header\n */",
                header.getContent());
    }
}