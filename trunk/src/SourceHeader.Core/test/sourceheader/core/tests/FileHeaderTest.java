/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sourceheader.core.tests;

import org.junit.Test;
import sourceheader.core.*;
import java.util.*;
import static org.junit.Assert.*;

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
    public void test_getNewlinesCount() {
        FileHeader header1 = 
                new FileHeader("lin1\n line2 \n adsadasd \n", '%') {};

        assertEquals(3, header1.getNewlinesCount());
    }

    @Test
    public void test_getRawContent_replaces_back_alternating_parts() {

        Map<String, List<String>> altData = new HashMap<String, List<String>>();
        List<String> value = new Vector<String>();
        value.add("real-value");
        altData.put("description", value);
        
        final String headerStr = "/* constant \n" +
                                " * <description>%description%0%</description>*/";
        FileHeader header = new FileHeader(headerStr, '%', altData) {};

        assertEquals(
                "/* constant \n * <description>real-value</description>*/",
                header.getRawContent());
    }

    @Test
    public void tets_getRawContent_replaces_two_block_with_same_id_correctly() {
        Map<String, List<String>> altData = new HashMap<String, List<String>>();
        List<String> value = new Vector<String>();
        value.add("real-value");
        value.add("real-value2");
        altData.put("description", value);

        final String headerStr = "/* constant \n" +
                " * <description>%description%1%</description> \n" +
                " * <description>%description%0%</description> */";
        FileHeader header = new FileHeader(
                headerStr, '%', altData) {};

        assertEquals(
                "/* constant \n" +
                " * <description>real-value2</description> \n" +
                " * <description>real-value</description> */",
                header.getRawContent());
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
}