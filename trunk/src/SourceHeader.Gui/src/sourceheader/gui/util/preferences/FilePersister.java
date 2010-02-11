/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sourceheader.gui.util.preferences;

import java.io.*;
import java.util.*;
import sourceheader.core.parsers.Block;

/**
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

    public void saveAlternatingParts(Map<String, Block> parts) throws Exception {
        PrintWriter writer = new PrintWriter(this.file);

        for (String key : parts.keySet()) {
            writer.println(key);
            Block block = parts.get(key);
            writer.println(
                    AlternatingPartsHelper.encode(block.getStartSequence()));
            writer.println(
                    AlternatingPartsHelper.encode(block.getEndSequence()));
            writer.println();
        }

        writer.flush();
        writer.close();
    }

    public static class SyntaxException extends Exception {
        public SyntaxException() {
            super("There is syntax error in config file.");
        }
    }
}
