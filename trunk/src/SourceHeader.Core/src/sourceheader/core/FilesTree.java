/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sourceheader.core;

import sourceheader.core.File.BackupCannotBeCareatedException;
import sourceheader.core.File.FileCannotBeUpdateException;

/**
 * Root of files and their headers.
 * 
 * Instance can be created by factory method that
 * creates instances of related classes.
 *
 * @author steve
 */
public class FilesTree extends Folder {

    private Iterable<Folder> folders;
    private FileHeaderFactory headerFactory;
    private Path path;

    protected FilesTree(Path path, Iterable<File> files,
            Iterable<Folder> subfolders,  FileHeaderFactory headerFactory) {
        super(path, files, subfolders);
        this.headerFactory = headerFactory;
        this.path = path;
    }

    /*public Iterable<File> getFiles() {
        List<File> result = new Vector<File>();

        for (Folder folder : this.folders) {

            result.addAll((Collection<File>)folder.getFiles());
        }

        return (Iterable<File>)result;
    }*/

    @Override
    public void update(ProgressReportConsumer progress)
            throws BackupCannotBeCareatedException, FileCannotBeUpdateException,
            FileHeader.ContentSyntaxErrorException {
            super.update(progress);
            if (progress != null) {
                progress.done();
            }
    }

    public Iterable<FileHeader> getFileHeaders() {
        return this.headerFactory.getFileHeaders();
    }    
}
