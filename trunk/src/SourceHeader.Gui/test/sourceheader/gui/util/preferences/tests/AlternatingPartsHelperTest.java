/*
 * file AlternatingPartsHelperTest.java
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
