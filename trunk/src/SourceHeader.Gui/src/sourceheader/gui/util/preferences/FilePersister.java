/*
 * file FilePersister.java
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

package sourceheader.gui.util.preferences;

import java.io.*;
import java.util.*;
import sourceheader.core.parsers.Block;

/**
 * Basic implementation of PreferencesPresister that uses file as storage.
 *
 * @author steve
 */
public class FilePersister implements PreferencesPersister {
    private File file;

    public FilePersister(File file) {
        this.file = file;
    }

    public FilePersister(String path) {
        this.file = new File(path);
    }

    /**
     * @inheritDoc
     */
    public Map<String, Block> getAlternatingParts() throws Exception {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(this.file)));
        Map<String, Block> result = new HashMap<String, Block>();

        String line;
        while((line = reader.readLine()) != null) {
            if (line.isEmpty() || line.startsWith("#")) {
                continue;
            }

            String name = line;
            if ((line = reader.readLine()) == null) {
                throw new SyntaxException();
            }

            String start = line;
            if ((line = reader.readLine()) == null) {
                throw new SyntaxException();
            }

            result.put(name,
                    new Block(
                        AlternatingPartsHelper.decode(start),
                        AlternatingPartsHelper.decode(line)));
        }

        return result;
    }

    /**
     * @inheritDoc
     */
    public void saveAlternatingParts(Map<String, Block> parts) throws Exception {
        PrintWriter writer = new PrintWriter(this.file);
        this.saveAlternatingParts(writer, parts);
        writer.flush();
        writer.close();
    }

    /**
     * This method is used by AlternatingPartsHelper that needs to
     * insert also comments into config file.
     * @param writer Output writer.
     * @param parts
     * @throws Exception
     */
    protected void saveAlternatingParts(PrintWriter writer, Map<String, Block> parts) throws Exception {
        for (String key : parts.keySet()) {
            writer.println(key);
            Block block = parts.get(key);
            writer.println(
                    AlternatingPartsHelper.encode(block.getStartSequence()));
            writer.println(
                    AlternatingPartsHelper.encode(block.getEndSequence()));
            writer.println();
        }
    }

    /**
     * Specific exception of this storage.
     */
    public static class SyntaxException extends Exception {
        public SyntaxException() {
            super("There is syntax error in config file.");
        }
    }
}
