/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sourceheader.core;

import java.util.*;
import sourceheader.core.util.MD5;

/**
 * Represents file header instance.
 * 
 * File headers should be created via class @link FileHeaderFactory.
 * 
 * File header consists of content (String), where all alternating parts
 * (e.g. content of tags &gt;description&lt;, filename, ...) are replaced with special sequences.
 * Two file headers are equal when contents are equal. 
 *
 * When one wants to get content of header he must provide real values
 * of alternating parts.
 *
 * Example of content of header with alternating part filename: 
 * <code>
 *      /////
 *      /// File $filename$. Part of GNU class path.
 *      /////
 * </code>
 * 
 * @author steve
 */
public class FileHeader {

    private String content;
    private char specialCharacter;

    /**
     * FileHeader instances should be created via FileHeaderFactory.
     * Content should be parsed using implementaction of HeaderParser.
     *
     * @param content Content of header, should be already parsed and therefore
     * alternating parts should be already replaced with special sequences.
     * @param specialCharacter Used to distinguish special sequences like alternating parts.
     * @param alernatingBlocks Real values of alternating parts.
     * One alternating part may occure more times.
     */
    protected FileHeader(
            String content,
            char specialCharacter) {
        this.specialCharacter = specialCharacter;
        this.content = content;
    }

    /**
     * Copy constructor.
     * @param header
     */
    public FileHeader(FileHeader header) {
        this.content = header.content;
    }

    /**
     * @return Content where alternating parts are represented by special sequences.
     */
    public String getContent() {
        return this.content;
    }

    /**
     * @return Content of header where all special sequences for
     * alternating parts are replaced with their real content.
     */
    public String getRawContent(Map<String, List<String>> alernatingParts) {
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

                result.append(alernatingParts.get(name).get(number));
            }
        }

        return result.toString();
    }

    /**
     * Sets the content of this file header. 
     * @param content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Helper function for classes that writes header in some file.
     * @return Count of \n characters in header
     */
    public int getNewlinesCount(Map<String, List<String>> alternatingParts) {
        String rawContent = this.getRawContent(alternatingParts);
        int fromIndex = 0;
        int count = -1;
        do {
            count++;
            fromIndex = rawContent.indexOf("\n", fromIndex) + 1;
        } while(fromIndex != 0);

        return count;
    }

    /**
     * Returns unique identifier of given header.
     *
     * Used for instances caching: when creating FileHeader via FileHeaderFactory
     * two different instances that are equal don't exist.
     *
     * @return Unique identifier of given header
     */
    public String getHash() {
        try {
            return MD5.create(this.content);
        }
        catch(MD5.MD5IsNotSupported exception) {
            return this.content;
        }
    }

    /**
     * Equality is based on content String.
     */
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

    /**
     * Hash code is has code of content String.
     * @return
     */
    @Override
    public int hashCode() {
        return this.content.hashCode();
    }
}
