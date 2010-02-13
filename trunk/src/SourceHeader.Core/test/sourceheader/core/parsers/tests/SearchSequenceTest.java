/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sourceheader.core.parsers.tests;

import org.junit.Test;
import sourceheader.core.parsers.*;
import static org.junit.Assert.*;

/**
 *
 * @author steve
 */
public class SearchSequenceTest {

    @Test
    public void test_that_searchSequence_finds() {
        SearchSequence s = new SearchSequence(null, "abac");

        s.next('a');
        assertFalse(s.found());
        s.next('b');
        assertFalse(s.found());
        s.next('a');
        assertFalse(s.found());
        s.next('b');
        assertFalse(s.found());
        s.next('a');
        assertFalse(s.found());
        s.next('c');
        assertTrue(s.found());
    }

    @Test
    public void test_comment_start() {
        SearchSequence s = new SearchSequence(null, "*/");

        s.next('*');
        assertFalse(s.found());
        s.next('*');
        assertFalse(s.found());
        s.next('/');
        assertTrue(s.found());
    }

}
