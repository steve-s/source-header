/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sourceheader.core;


import java.util.*;

/**
 *
 * @author steve
 */
public class Path extends java.io.File {

    public Path(String fullPath) {
        super(fullPath);
    }

    public Path(String parent, String child) {
        super(parent, child);
    }

    public Path(java.io.File parent, String child) {
        super(parent, child);
    }

    /**
     * @return The extension of the file, if path doesn't lead to file with
     * extension empty string is returned.
     */
    public String getExtension() {
        // If the dot before extension is not present, than no extension is there.
        int index = this.getName().lastIndexOf(".");
        if (index == -1) {
            return "";
        }

        return this.getName().substring(index+1);
    }

    /**
     * Custom method for getting children instead of list.
     * This one returnes instances of @link sourceheader.core.Path Path class.
     * @return list of subdirectories and files.
     */
    public Iterable<Path> getChildren() {
        List<Path> result = new Vector<Path>();
        for (String path : this.list()) {
            result.add(new Path(path));
        }
        
        return (Iterable<Path>)result;
    }
}
