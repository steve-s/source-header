/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sourceheader.core.tests;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sourceheader.core.*;
import sourceheader.core.HeaderParser.SyntaxErrorException;
import sourceheader.core.parsers.*;
import static org.junit.Assert.*;

/**
 *
 * @author steve
 */
public class FilesTreeFactoryTest {

    final String mainHeader = "// main.cpp is \n//" +
            " project file \n// hello world \n";

    final String mainHeaderParsed = "// %filename% is \n//" +
            " project file \n// hello world \n";

    final String classHeader =
            "/*\n" +
            " * To change this template, choose Tools | Templates\n" +
            " * and open the template in the editor.\n" +
            " */";

    @Before
    public void setUp() throws IOException {
        this.getTestDirPath().mkdir();
        this.getFolderPath().mkdir();

        Path main = new Path(this.getTestDirPath(), "main.cpp");
        main.createNewFile();
        this.writeToFile(main, mainHeader + 
                "\n #include \"folder/class.cpp\" \n int main(){return 0;}");

        Path cpp = this.getCppFilePath();
        cpp.createNewFile();
        this.writeToFile(cpp, classHeader);

        Path hpp = this.getHppFilePath();
        hpp.createNewFile();
        this.writeToFile(hpp, classHeader);

        Path tmp = new Path(new Path(".","test-data"), "exampleTmp");
        tmp.mkdir();
        File.setTmpPath(tmp);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void tets_change_the_tree() throws IOException, SyntaxErrorException {
        FilesTreeFactory factory =
                new FilesTreeFactory(this.getHeaderFactory());

        FilesTree tree = factory.create(this.getTestDirPath());

        File clazz = tree.getFolders()
                .iterator().next()
                .getFiles().iterator().next();

        clazz.getHeader().setContent("// New header man \n");

        tree.update();

        FileHeaderFactory headersFactory = this.getHeaderFactory();
        FileHeader header1 = headersFactory.create(this.getCppFilePath()).header;
        FileHeader header2 = headersFactory.create(this.getHppFilePath()).header;

        assertEquals("// New header man \n", header1.getContent());
        assertEquals(header1.getContent(), header2.getContent());
    }


    @Test
    public void test_parse_the_tree() throws IOException, SyntaxErrorException {
        FilesTreeFactory factory =
                new FilesTreeFactory(this.getHeaderFactory());

        FilesTree tree = factory.create(this.getTestDirPath());

        assertEquals(1, this.getCount(tree.getFiles()));
        File file = tree.getFiles().iterator().next();
        assertEquals("main.cpp", file.getName());
        assertEquals("cpp", file.getExtension());
        assertEquals(mainHeaderParsed, file.getHeader().getContent());

        Folder folder = tree.getFolders().iterator().next();
        assertEquals("folder", folder.getName());
        assertEquals(2, this.getCount(folder.getFiles()));

        Iterable<File> files = folder.getFiles();
        File cpp = null, hpp = null;
        for(File f : files) {
            if (f.getExtension().equals("cpp")) {
                cpp = f;
            }
            else if (f.getExtension().equals("h")) {
                hpp = f;
            }
        }

        assertNotNull(cpp);
        assertEquals("class.cpp", cpp.getName());
        assertEquals(classHeader, cpp.getHeader().getContent());

        assertNotNull(hpp);
        assertEquals("class.h", hpp.getName());
        assertEquals(classHeader, hpp.getHeader().getContent());

        assertSame(hpp.getHeader(), cpp.getHeader());
    }

    private Path getTestDirPath() {
        return new Path(new Path(".", "test-data"), "complex-test");
    }

    private Path getFolderPath() {
        return new Path(this.getTestDirPath(), "folder");
    }

    private Path getCppFilePath() {
        return new Path(this.getFolderPath(), "class.cpp");
    }

    private Path getHppFilePath() {
        return new Path(this.getFolderPath(), "class.h");
    }

    private void writeToFile(Path path, String content)
            throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(path));
        writer.write(content);
        writer.close();
    }

    private int getCount(Iterable<?> iterable) {
        int count = 0;
        for(Object o:iterable) { count++; }
        return count;
    }

    private FileHeaderFactory getHeaderFactory() {
        return new FileHeaderFactory(
                    Utils.getParsersConfig(),
                    new HeaderParser[] {
                        new CppParser(Utils.getParsersConfig()),
                        new ScriptsParser(Utils.getParsersConfig())
                  });
    }
}
