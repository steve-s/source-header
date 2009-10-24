/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sourceheader.core.tests;

import java.io.FileNotFoundException;
import sourceheader.core.*;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import sourceheader.core.File.BackupCannotBeCareatedException;
import sourceheader.core.File.FileCannotBeUpdateException;

/**
 *
 * @author steve
 */
public class FileTest {

    @Before
    public void setUp() throws IOException {
        Path file = this.getFilePath();
        file.createNewFile();

        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write("/* This is one line header */ \n");
        writer.write("This is one line of content");
        writer.close();

        Path tmp = new Path(TestsConfig.getRootForTests(), "tmp");
        tmp.mkdir();
        File.setTmpPath(tmp);
    }

    @After
    public void tearDown() {
        this.getFilePath().delete();
    }

    private Path getFilePath() {
        return new Path(TestsConfig.getRootForTests(), "./ahoj");
    }

    @Test
    public void test_update() 
            throws BackupCannotBeCareatedException,
            FileCannotBeUpdateException,
            FileNotFoundException,
            IOException {
        
        FileHeader newHeader =
                new FileHeader("/* two lines \n header */", '%') {};
        FileHeader oldHeader =
                new FileHeader("/* This is one line header */", '%') {};
        File file = new File(this.getFilePath(), oldHeader) {};

        file.setHeader(newHeader);
        file.update();

        BufferedReader reader =
                new BufferedReader(new FileReader(this.getFilePath()));
        int count = 0;
        while (reader.readLine() != null) {
            count++;
        }

        assertEquals(3, count);
    }
}