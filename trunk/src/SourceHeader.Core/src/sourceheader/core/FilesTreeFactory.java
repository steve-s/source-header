/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sourceheader.core;

import java.io.IOException;
import java.util.*;
import sourceheader.core.*;
import sourceheader.core.Folder;
import sourceheader.core.HeaderParser.SyntaxErrorException;

/**
 *
 * @author steve
 */
public class FilesTreeFactory {

    private FileHeaderFactory headerFactory;

    public FilesTreeFactory(FileHeaderFactory headerFactory) {
        this.headerFactory = headerFactory;
    }

    public FilesTree create(Path path)
            throws IOException, SyntaxErrorException {
        FoldersAndFiles data = this.extractFolder(path);
        return new FilesTree(path, data.files, data.folders, this.headerFactory);
    }

    private FoldersAndFiles extractFolder(Path path)
            throws IOException, SyntaxErrorException {
        FoldersAndFiles result = new FoldersAndFiles();

        for(Path child : path.getChildren()) {
            if (child.isDirectory()) {
                Folder folder = this.createFolder(child);
                result.folders.add(folder);
            }
            else if (child.isFile()) {
                result.files.add(this.createFile(child));
            }
        }

        return result;
    }

    private Folder createFolder(Path path) 
            throws IOException, SyntaxErrorException {
        FoldersAndFiles data = this.extractFolder(path);
        return new Folder(path, data.files, data.folders);
    }

    private File createFile(Path path)
            throws IOException, SyntaxErrorException {
        return new File(path, this.headerFactory.create(path));
    }

    private class FoldersAndFiles {
        List<Folder> folders = new Vector<Folder>();
        List<File> files = new Vector<File>();
    }
}
