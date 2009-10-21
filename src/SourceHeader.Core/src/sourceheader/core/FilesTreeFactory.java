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
public class FilesTreeFactory {

    private FileHeaderFactory headerFactory;

    public FilesTreeFactory(FileHeaderFactory headerFactory) {
        this.headerFactory = headerFactory;
    }

    public FilesTree create(Path path) {
        FoldersAndFiles data = this.extractFolder(path);
        return new FilesTree(path, data.files, data.folders, this.headerFactory);
    }

    private FoldersAndFiles extractFolder(Path path) {
        FoldersAndFiles result = new FoldersAndFiles();

        for(Path child : path.getChildren()) {
            if (path.isDirectory()) {
                result.folders.add(this.createFolder(child));
            }
            else if (path.isFile()) {
                result.files.add(this.createFile(child));
            }
        }

        return result;
    }

    private Folder createFolder(Path path) {
        FoldersAndFiles data = this.extractFolder(path);
        return new Folder(path, data.files, data.folders);
    }

    private File createFile(Path path) {
        return new File(path, this.headerFactory.create(path));
    }

    private class FoldersAndFiles {
        List<Folder> folders = new Vector<Folder>();
        List<File> files = new Vector<File>();
    }
}
