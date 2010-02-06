/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sourceheader.core.parsers.tests;

import java.io.*;
import java.io.Reader;
import sourceheader.core.tests.Utils;
import org.junit.Test;
import sourceheader.core.*;
import sourceheader.core.HeaderParser.SyntaxErrorException;
import sourceheader.core.parsers.*;
import static org.junit.Assert.*;

/**
 *
 * @author steve
 */
public class AbstractParserTest {
    
    @Test
    public void test_parser_when_header_is_finished_by_couple_new_lines()
            throws IOException, SyntaxErrorException {
        AbstractParser parser = AbstractParserUtils.getParserWithStubCommentBlock("/*", "*/");
        
        // create the factory stub
        FileHeaderFactory factory =
                new FileHeaderFactory(Utils.getParsersConfig(), new HeaderParser[] {});
        
        Reader reader = 
                new StringReader("\n\n\t\t  " + 
                "\n/*This is really\n\tcute\n\n header****/\n\n\n" +
                "/*3 new lines should exclude this from header*/\n\n #include;");

        HeaderParser.HeaderAndAlternatingParts result =
                parser.parse(reader, factory);

        assertEquals("\n\n\t\t  \n/*This is really\n\tcute\n\n header****/",
                result.header.getContent());
    }

    @Test
    public void test_parse_when_header_is_finished_by_non_comment_text() throws IOException, SyntaxErrorException {
        AbstractParser parser = AbstractParserUtils.getParserWithStubCommentBlock("/*", "*/");

        // create the factory stub
        FileHeaderFactory factory =
                new FileHeaderFactory(Utils.getParsersConfig(),
                new HeaderParser[] {});

        Reader reader =
                new StringReader(
                "\n/*This is another\n *\n *header\n */#include \"xx.hpp\";\n");

        HeaderParser.HeaderAndAlternatingParts result =
                parser.parse(reader, factory);

        assertEquals("\n/*This is another\n *\n *header\n */",
                result.header.getContent());
    }

    @Test
    public void test_parse_oneline_cstyle_comment_header() throws IOException, SyntaxErrorException {
        AbstractParser parser = AbstractParserUtils.getParserWithStubCommentBlock("//", "\n");

        // create the factory stub
        FileHeaderFactory factory =
                new FileHeaderFactory(Utils.getParsersConfig(),
                    new HeaderParser[] {});

        Reader reader =
                new StringReader(
                "// This is \n // header constructed \n " +
                "// with one line comments \n int global_speed = 8; \n");

        HeaderParser.HeaderAndAlternatingParts result =
                parser.parse(reader, factory);

        assertEquals("// This is \n // header constructed \n " +
                "// with one line comments \n",
                result.header.getContent());
    }

    @Test
    public void test_parse_when_there_is_only_header_in_the_file()
            throws IOException, SyntaxErrorException {
        AbstractParser parser = AbstractParserUtils.getParserWithStubCommentBlock("/*", "*/");

        // create the factory stub
        FileHeaderFactory factory =
                new FileHeaderFactory(Utils.getParsersConfig(),
                    new HeaderParser[] {});

        final String headerStr = "/* This is \n * header constructed \n " +
                               "* with star comments \n */";
        Reader reader = new StringReader(headerStr);

        HeaderParser.HeaderAndAlternatingParts result =
                parser.parse(reader, factory);

        assertEquals(headerStr, result.header.getContent());
    }

    @Test
    public void test_parse_throw_when_header_has_syntax_error()
            throws IOException {

        AbstractParser parser = AbstractParserUtils.getParserWithStubCommentBlock("/*", "*/");

        // create the factory stub
        FileHeaderFactory factory =
                new FileHeaderFactory(Utils.getParsersConfig(),
                    new HeaderParser[] {});

        final String headerStr = "/* This is \n * header with \n " +
                                  "* syntax error \n no end of comment here ";
        Reader reader = AbstractParserUtils.getStringReader(headerStr);

        try {
            HeaderParser.HeaderAndAlternatingParts result =
                    parser.parse(reader, factory);
            fail("SyntaxErrorException was not thrown.");
        }
        catch (SyntaxErrorException exception) {
        }
    }

    @Test
    public void test_parse_when_one_line_comment_is_followed_by_two_newlines()
            throws IOException, SyntaxErrorException {
        AbstractParser parser = AbstractParserUtils.getParserWithStubCommentBlock("//", "\n");

        // create the factory stub
        FileHeaderFactory factory =
                new FileHeaderFactory(Utils.getParsersConfig(),
                    new HeaderParser[] {});

        Reader reader = AbstractParserUtils.getStringReader("// One line \n\n");

        HeaderParser.HeaderAndAlternatingParts result =
                parser.parse(reader, factory);

        assertEquals("// One line \n", result.header.getContent());
    }
}