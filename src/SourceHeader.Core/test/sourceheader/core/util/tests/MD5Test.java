/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sourceheader.core.util.tests;

import sourceheader.core.util.MD5;
import org.junit.Test;
import static org.junit.Assert.*;
import sourceheader.core.util.MD5.MD5IsNotSupported;

/**
 *
 * @author steve
 */
public class MD5Test {
    @Test
    public void test_getHash_equals() throws MD5IsNotSupported {
        assertEquals(MD5.create("ahoj"), MD5.create("ahoj"));
    }

    @Test
    public void test_getHas_notEquals() throws MD5IsNotSupported {
        assertFalse( MD5.create("hello").equals(MD5.create("ahoj")));
    }
}