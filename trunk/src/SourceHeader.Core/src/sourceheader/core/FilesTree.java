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
 * Instance should be created by factory method of FilesTreeFactory.
 * It will create whole graph of instances representing the tree.
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

    /**
     * Updates headers physicaly on filesystem.
     * 
     * @param progress Progress listener, may be null.
     * @throws sourceheader.core.File.BackupCannotBeCareatedException
     * @throws sourceheader.core.File.FileCannotBeUpdateException
     * @throws sourceheader.core.FileHeader.ContentSyntaxErrorException
     */
    @Override
    public void update(ProgressReportConsumer progress)
            throws BackupCannotBeCareatedException, FileCannotBeUpdateException,
            FileHeader.ContentSyntaxErrorException {
            super.update(progress);
            if (progress != null) {
                progress.done();
            }
    }

    /**
     * @return All headers that 'live' within this container.
     */
    public Iterable<FileHeader> getFileHeaders() {
        return this.headerFactory.getFileHeaders();
    }    
}
