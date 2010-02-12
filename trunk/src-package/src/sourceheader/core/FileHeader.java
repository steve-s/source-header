/*
 * file FileHeader.java
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

package sourceheader.core;

import java.util.*;
import sourceheader.core.util.MD5;

/**
 * Represents file header instance.
 *
 * <p>
 * File headers should be created via class 
 * {@link FileHeaderFactory FileHeaderFactory}.
 * </p>
 *
 * <p>
 * File header consists of content (String), where all alternating parts
 * (e.g. content of javaDoc tag (a)category, filename, ...) are replaced with special sequences.
 * Two file headers are equal when contents are equal.
 * </p>
 *
 * <p>
 * When one wants to get content of header he must provide real values
 * of alternating parts.
 * </p>
 *
 * <p>
 * Example of content of header with alternating part filename:
 * <pre><code>
 *      /////
 *      /// File $filename$. Part of GNU class path.
 *      /////
 * </code></pre>
 * </p>
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
     * @param alernatingParts Content of alternating parts.
     * @param filename Content for special alternating part %filename%.
     * @return Content of header where all special sequences for
     * alternating parts are replaced with their real content.
     */
    public String getRawContent(Map<String, List<String>> alernatingParts,
            String filename) throws ContentSyntaxErrorException {
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
                if (name.equals("filename")) {
                    result.append(filename);                    
                }
                else {
                    assert tokenizer.hasMoreTokens() : "There must be number after id";
                    int number = Integer.parseInt(tokenizer.nextToken());

                    List<String> values = alernatingParts.get(name);
                    if (values == null) {
                        throw new ContentSyntaxErrorException();
                    }

                    if (number >= values.size() || number < 0) {
                        throw new ContentSyntaxErrorException();
                    }

                    result.append(values.get(number));
                }
            }
        }

        return result.toString();
    }

    /**
     * Calls getRawContent with empty filename.
     * @param alernatingParts Content of alternating parts.
     * @return Content of header where all special sequences for
     * alternating parts are replaced with their real content.
     * @throws sourceheader.core.FileHeader.ContentSyntaxErrorException
     */
    public String getRawContent(Map<String, List<String>> alernatingParts)
            throws ContentSyntaxErrorException {
        return this.getRawContent(alernatingParts, "");
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
    public int getNewlinesCount(Map<String, List<String>> alternatingParts)
        throws ContentSyntaxErrorException {
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

    public static class ContentSyntaxErrorException extends Exception {
    }
}
