/*
 * file FilesTree.java
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
import sourceheader.core.File.BackupCannotBeCareatedException;
import sourceheader.core.File.FileCannotBeUpdateException;

/**
 * Root of {@link File files} and their {@link FileHeader headers}.
 * 
 * Instance should be created by factory method of {@link FilesTreeFactory FilesTreeFactory}.
 * It will create whole graph of instances representing the tree.
 *
 * @author steve
 */
public class FilesTree extends Folder {
    private FileHeaderFactory headerFactory;

    protected FilesTree(Path path, List<File> files,
            List<Folder> subfolders,  FileHeaderFactory headerFactory) {
        super(path, files, subfolders);
        this.headerFactory = headerFactory;
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
    public Collection<FileHeader> getFileHeaders() {
        return this.headerFactory.getFileHeaders();
    }    
}
