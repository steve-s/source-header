/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sourceheader.core.util.tests;

import java.io.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sourceheader.core.tests.TestsConfig;
import sourceheader.core.util.Filesystem;
import static org.junit.Assert.*;

/**
 *
 * @author steve
 */
public class FilesystemTest {

    @Before
    public void setUp() throws IOException {
        this.getFile1().delete();
        this.getFile2().delete();
        for(java.io.File f : TestsConfig.getRootForTests().listFiles()) {
            f.delete();
        }
        
        this.getFile1().createNewFile();
        BufferedWriter writer =
                new BufferedWriter(new FileWriter(this.getFile1()));
        writer.write("hello \n wolrd \n ahoj \n svete");
        writer.close();

        this.getFile2().createNewFile();
    }

    @After
    public void tearDown() {
        this.getFile1().delete();
        this.getFile2().delete();
    }

    public File getFile1() {
        return new File(TestsConfig.getRootForTests(), "asdasdf1.x");
    }

    public File getFile2() {
        return new File(TestsConfig.getRootForTests(), "fasddwec2.x");
    }

    @Test
    public void test_copyText() 
            throws FileNotFoundException, IOException {
        Filesystem.copyText(this.getFile1(), this.getFile2());

        BufferedReader reader =
                new BufferedReader(new FileReader(this.getFile2()));
        int count = 0;
        while (reader.readLine() != null) {
            count++;
        }

        assertEquals(4, count);
    }

    @Test
    public void test_getFilesCount() {
        assertEquals(2,
                Filesystem.getFilesCount(
                    TestsConfig.getRootForTests(),
                    100,
                    new FileFilter() {
                        public boolean accept(File pathname) {
                            return !pathname.isHidden() && 
                                   !pathname.getName().startsWith(".") &&
                                   !pathname.isDirectory();
                        }
                    }));
    }
}