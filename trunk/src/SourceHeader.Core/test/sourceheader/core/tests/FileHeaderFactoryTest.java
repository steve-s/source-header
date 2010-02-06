/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sourceheader.core.tests;

import java.io.IOException;
import java.io.Reader;
import org.junit.Test;
import sourceheader.core.*;
import static org.junit.Assert.*;
import sourceheader.core.HeaderParser.SyntaxErrorException;

/**
 *
 * @author steve
 */
public class FileHeaderFactoryTest {

    /**
     * Tests the creation of header by factory.
     */
    @Test
    public void test_create() throws IOException, IOException, SyntaxErrorException {
        final String extension = "asdsda";

        // create the factory with stub parser that parses nothing
        FileHeaderFactory factory = new FileHeaderFactory(
                Utils.getParsersConfig(),
                new HeaderParser[] {
                    new HeaderParser() {
                        public HeaderAndAlternatingParts parse(Path path, FileHeaderFactory factory)
                                throws IOException {
                            HeaderAndAlternatingParts result = new HeaderAndAlternatingParts();
                            result.header = new FileHeader(path.getExtension(), '%') {};
                            return result;
                        }

                        public HeaderAndAlternatingParts parse(Reader reader, FileHeaderFactory factory)
                                throws IOException {
                            throw new UnsupportedOperationException("Not supported yet.");
                        }

                        public String[] getExtensions() {
                            return new String[] {extension};
                        }
                    }
            });

        // gets header from factory, passing stub Path
        HeaderParser.HeaderAndAlternatingParts result =
                factory.create(new Path("somepath") {
                                @Override
                                public String getExtension() {
                                    return extension;
                                }
                            });
            
        assertEquals(extension, result.header.getContent());
    }

    /**
     * Tests that factory does not create two instances for same header.
     */
    @Test
    public void test_caching() throws IOException {
        // create the factory stub
        FileHeaderFactory factory =
                new FileHeaderFactory(
                    Utils.getParsersConfig(),
                    new HeaderParser[] {});

        // gets 2 headers from factory, passing the same content
        FileHeader header1 = factory.create("constant");
        FileHeader header2 = factory.create("constant");

        // the headers returned by factory should be the same instance.
        assertSame(header1, header2);
    }
}