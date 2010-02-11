/*
 * file Folder.java
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

import sourceheader.core.File.BackupCannotBeCareatedException;
import sourceheader.core.File.FileCannotBeUpdateException;

/**
 * Represents folder, it's subfolders and files.
 *
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
     * Updates headers of child files to filesystem.
     * @param progress Progress listener.
     * @throws sourceheader.core.File.BackupCannotBeCareatedException
     * @throws sourceheader.core.File.FileCannotBeUpdateException
     */
    public void update(ProgressReportConsumer progress)
            throws BackupCannotBeCareatedException,
            FileCannotBeUpdateException,
            FileHeader.ContentSyntaxErrorException{
        for(File file : this.files) {
            if (progress != null) {
                progress.progress();
            }
            
            file.update();
        }

        for(Folder folder : this.folders) {
            folder.update(progress);
        }
    }

    /**
     * Updates headers of child files to filesystem.
     * @throws sourceheader.core.File.BackupCannotBeCareatedException
     * @throws sourceheader.core.File.FileCannotBeUpdateException
     * @throws sourceheader.core.FileHeader.ContentSyntaxErrorException
     */
    public void update()
            throws BackupCannotBeCareatedException, FileCannotBeUpdateException,
            FileHeader.ContentSyntaxErrorException {
        this.update(null);
    }

    /**
     * @return the list of files.
     */
    public Iterable<File> getFiles() {
        return files;
    }

    @Override
    public String toString() {
        return this.getPath().getName();
    }
}
