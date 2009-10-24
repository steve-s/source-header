/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sourceheader.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import sourceheader.core.util.MD5;

/**
 *
 * @author steve
 */
public class FileHeader {

    private String content;
    private Map<String, List<String>> alernatingBlocks;
    private int hashCodeCache = -1;
    private char specialCharacter;

    protected FileHeader(String content, char specialCharacter) {
        this.specialCharacter = specialCharacter;
        this.content = content;
        this.alernatingBlocks = new HashMap<String, List<String>>();
    }

    protected FileHeader(
            String content,
            char specialCharacter,
            Map<String, List<String>> alernatingBlocks) {
        this.specialCharacter = specialCharacter;
        this.content = content;
        this.alernatingBlocks = alernatingBlocks;
    }

    public FileHeader(FileHeader header) {
        this.content = header.content;
    }

    public String getContent() {
        return this.content;
    }

    public String getRawContent() {
        StringBuilder result = new StringBuilder();

        StringTokenizer tokenizer =
                new StringTokenizer(
                    this.content,
                    Character.toString(this.specialCharacter),
                    false);

        while (tokenizer.hasMoreTokens()) {
            result.append(tokenizer.nextToken());

            if (tokenizer.hasMoreTokens()) {
                String name = tokenizer.nextToken();
                assert tokenizer.hasMoreTokens() : "There must be number after id";
                int number = Integer.parseInt(tokenizer.nextToken());

                result.append(this.alernatingBlocks.get(name).get(number));
            }
        }

        return result.toString();
    }

    public Map<String, List<String>> getAlernatingParts() {
        return alernatingBlocks;
    }

    public void setContent(String content) {
        this.content = content;
        this.hashCodeCache = -1;
    }

    public int getNewlinesCount() {
        int fromIndex = 0;
        int count = -1;
        do {
            count++;
            fromIndex = this.content.indexOf("\n", fromIndex) + 1;
        } while(fromIndex != 0);

        return count;
    }

    public String getHash() {
        try {
            return MD5.create(this.content);
        }
        catch(MD5.MD5IsNotSupported exception) {
            return this.content;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof FileHeader) {
            return ((FileHeader)obj).getHash().equals(this.getHash());
        }

        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        if (this.hashCodeCache >= 0) {
            return this.hashCodeCache;
        }

        this.hashCodeCache = this.content.length();
        return this.hashCodeCache;
    }
}
