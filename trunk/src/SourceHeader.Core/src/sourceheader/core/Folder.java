/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sourceheader.core;

import sourceheader.core.File.BackupCannotBeCareatedException;
import sourceheader.core.File.FileCannotBeUpdateException;

/**
 * @author steve
 */
public class Folder {

    private Iterable<File> files;
    private Iterable<Folder> folders;
    private Path path;

    protected Folder(Path path, Iterable<File> files,
            Iterable<Folder> subfolders) {
        this.folders = subfolders;
        this.files = files;
        this.path = path;
    }

    public Path getPath() {
        return this.path;
    }

    public String getName() {
        return this.path.getName();
    }

    public Iterable<Folder> getFolders() {
        return this.folders;
    }

    /**
     * Updates content of all files and files in subfolders.
     * @throws sourceheader.core.File.BackupCannotBeCareatedException
     * @throws sourceheader.core.File.FileCannotBeUpdateException
     */
    public void update()
            throws BackupCannotBeCareatedException,
            FileCannotBeUpdateException {
        for(File file : this.files) {
            file.update();
        }

        for(Folder folder : this.folders) {
            folder.update();
        }
    }

    /**
     * @return the list of files.
     */
    public Iterable<File> getFiles() {
        return files;
    }
}
