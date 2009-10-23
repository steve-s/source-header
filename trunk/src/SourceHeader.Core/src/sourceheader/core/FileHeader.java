/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sourceheader.core;

import sourceheader.core.util.MD5;

/**
 *
 * @author steve
 */
public class FileHeader {

    private String content;
    private int hashCodeCache = -1;

    protected FileHeader(String content) {
        this.content = content;
    }

    public FileHeader(FileHeader header) {
        this.content = header.content;
    }

    public String getContent() {
        return this.content;
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
