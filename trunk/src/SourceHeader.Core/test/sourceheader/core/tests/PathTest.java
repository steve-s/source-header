/*
 * file PathTest.java
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

import java.io.*;
import org.junit.Test;
import sourceheader.core.Path;
import static org.junit.Assert.*;

/**
 *
 * @author steve
 */
public class PathTest {

    @Test
    public void test_getExtension() {
        Path path = new Path("C:/hello\\World/file.ext");
        assertEquals("ext", path.getExtension());
    }

    @Test
    public void test_getExtension_with_empty_extension() {
        Path path = new Path("C:/hello\\World/hey");
        assertEquals("", path.getExtension());
    }
}
