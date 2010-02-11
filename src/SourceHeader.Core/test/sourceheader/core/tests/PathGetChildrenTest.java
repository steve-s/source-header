/*
 * file PathGetChildrenTest.java
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

import java.io.File;
import sourceheader.core.Path;
import java.io.IOException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author steve
 */
public class PathGetChildrenTest {

    @Before
    public void setUp() throws IOException {
        // Preparation of real file on system.
        this.getFile1().createNewFile();
        this.getFile2().createNewFile();
    }

    @After
    public void tearDown() {
        this.getFile1().delete();
        this.getFile2().delete();
    }

    private File getFile1() {
        return new File(TestsConfig.getRootForTests(), "file1.txt");
    }

    private File getFile2() {
        return new File(TestsConfig.getRootForTests(), "file1.cpp");
    }

    @Test
    public void test() {
        // Stimulation of Path
        Path path = new Path(TestsConfig.getRootForTests().getAbsolutePath());

        // Assertion
        boolean cpp = false;
        boolean txt = false;
        for (Path child : path.getChildren()) {
            if (child.getName().equals(this.getFile1().getName())) {
                txt = true;
            }
            else if (child.getName().equals(this.getFile2().getName())) {
                cpp = true;
            }
        }
        assertTrue(txt && cpp);
    }
}
