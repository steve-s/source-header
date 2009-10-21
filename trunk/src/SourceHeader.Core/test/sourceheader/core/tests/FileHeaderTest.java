/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sourceheader.core.tests;

import org.junit.Test;
import sourceheader.core.*;
import static org.junit.Assert.*;

/**
 *
 * @author steve
 */
public class FileHeaderTest {
    @Test
    public void test_hashCode_overriden() {
        FileHeader header1 = new FileHeader("hello") {};
        FileHeader header2 = new FileHeader("hello") {};

        assertEquals(header1.hashCode(), header2.hashCode());
    }

    @Test
    public void test_equals_overriden() {
        FileHeader header1 = new FileHeader("hello") {};
        FileHeader header2 = new FileHeader("hello") {};

        assertTrue(header1.equals(header2));
    }

    @Test
    public void test_getNewlinesCount() {
        FileHeader header1 = new FileHeader("lin1\n line2 \n adsadasd \n") {};

        assertEquals(3, header1.getNewlinesCount());
    }
}