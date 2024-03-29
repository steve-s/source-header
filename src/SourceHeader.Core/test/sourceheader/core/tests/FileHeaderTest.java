/*
 * file FileHeaderTest.java
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

package sourceheader.core.tests;

import org.junit.Test;
import sourceheader.core.*;
import java.util.*;
import static org.junit.Assert.*;
import sourceheader.core.FileHeader.ContentSyntaxErrorException;

/**
 *
 * @author steve
 */
public class FileHeaderTest {
    @Test
    public void test_hashCode_overriden() {
        FileHeader header1 = 
                new FileHeader("hello", '%') {};
        FileHeader header2 = 
                new FileHeader("hello", '%') {};

        assertEquals(header1.hashCode(), header2.hashCode());
    }

    @Test
    public void test_equals_overriden() {
        FileHeader header1 = 
                new FileHeader("hello", '%') {};
        FileHeader header2 = 
                new FileHeader("hello", '%') {};

        assertTrue(header1.equals(header2));
    }

    @Test
    public void test_getNewlinesCount() throws ContentSyntaxErrorException {
        FileHeader header1 = 
                new FileHeader("lin1\n line2 \n adsadasd \n", '%') {};

        assertEquals(3, header1.getNewlinesCount(new Hashtable<String, List<String>>()));
    }

    @Test
    public void test_getRawContent_replaces_back_alternating_parts() throws ContentSyntaxErrorException {

        Map<String, List<String>> altData = new HashMap<String, List<String>>();
        List<String> value = new Vector<String>();
        value.add("real-value");
        altData.put("description", value);
        
        final String headerStr = "/* constant \n" +
                                " * <description>%description%0%</description>*/";
        FileHeader header = new FileHeader(headerStr, '%') {};

        assertEquals(
                "/* constant \n * <description>real-value</description>*/",
                header.getRawContent(altData));
    }

    @Test
    public void tets_getRawContent_replaces_two_block_with_same_id_correctly() throws ContentSyntaxErrorException {
        Map<String, List<String>> altData = new HashMap<String, List<String>>();
        List<String> value = new Vector<String>();
        value.add("real-value");
        value.add("real-value2");
        altData.put("description", value);

        final String headerStr = "/* constant \n" +
                " * <description>%description%1%</description> \n" +
                " * <description>%description%0%</description> */";
        FileHeader header = new FileHeader(headerStr, '%') {};

        assertEquals(
                "/* constant \n" +
                " * <description>real-value2</description> \n" +
                " * <description>real-value</description> */",
                header.getRawContent(altData));
    }
    
    @Test
    public void test_getContent_does_not_replace_id_of_alternating_part() {
        
        final String headerStr = "/* constant \n" +
                            " * <description>%description%0%</description>*/";
        FileHeader header = new FileHeader(headerStr,'%') {};
        
        assertEquals(
                "/* constant \n * <description>%description%0%</description>*/",
                header.getContent());
    }
    
    @Test
    public void test_getRawContent_replaces_classname() throws ContentSyntaxErrorException {
        final String headerStr = "/* constant \n" +
                            " * %classname% */";
        FileHeader header = new FileHeader(headerStr,'%') {};

        assertEquals(
                "/* constant \n * MyClass */",
                header.getRawContent(new HashMap<String, List<String>>(), "MyClass.cs"));
    }

    @Test
    public void test_getRawContent_replaces_filename() throws ContentSyntaxErrorException {
        final String headerStr = "/* constant \n" +
                            " * %filename% */";
        FileHeader header = new FileHeader(headerStr,'%') {};

        assertEquals(
                "/* constant \n * MyClass.js */",
                header.getRawContent(new HashMap<String, List<String>>(), "MyClass.js"));
    }
}
