/*
 * file PhpParserTest.java
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
import java.io.StringReader;
import org.junit.Test;
import sourceheader.core.*;
import sourceheader.core.HeaderParser.SyntaxErrorException;
import sourceheader.core.parsers.*;
import static org.junit.Assert.*;

/**
 *
 * @author steve
 */
public class PhpParserTest {

    @Test
    public void test_php_and_no_new_line_afterwards() throws IOException, SyntaxErrorException {
        final String content = 
                "<?php /* Ahoj svete, supr header \n hahah */";
        this.test(content);
    }

    @Test
    public void test_php_and_new_line_after() throws IOException, SyntaxErrorException {
        final String content =
                "<?php \n /* Ahoj svete, supr header \n hahah */";
        this.test(content);
    }

    @Test
    public void test_php_and_new_line_after1() throws IOException, SyntaxErrorException {
        final String content =
                "<?php\n" +
                "/** \n" +
                " * Smart Library - Smart Framework \n " +
                " * http://smart-framework.highet.eu \n */";
        this.test(content);
    }

    private void test(final String content) 
            throws IOException, SyntaxErrorException {
        
        ParsersConfig config = new ParsersConfig('$');
        PhpParser parser = new PhpParser(config);
        FileHeaderFactory factory =
                new FileHeaderFactory(config) {
                    @Override
                    public FileHeader create(String c) {
                        assertEquals(content, c);
                        return null;
                    }
                };

        StringReader reader = new StringReader(content);
        parser.parse(reader, factory);
    }

}
