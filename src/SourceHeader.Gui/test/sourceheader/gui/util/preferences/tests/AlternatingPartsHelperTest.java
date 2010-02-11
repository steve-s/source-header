/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sourceheader.gui.util.preferences.tests;

import org.junit.Test;
import sourceheader.gui.util.preferences.AlternatingPartsHelper;
import static org.junit.Assert.*;

/**
 *
 * @author steve
 */
public class AlternatingPartsHelperTest {

    @Test
    public void test_decode() {
        assertEquals("\n", AlternatingPartsHelper.decode("\\n"));
    }

    @Test
    public void test_encode() {
        assertEquals("\\n", AlternatingPartsHelper.encode("\n"));
    }
}