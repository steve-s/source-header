/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sourceheader.core;

import java.io.FileFilter;
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
    private FileFilter fileFilter;

    public FilesTreeFactory(FileHeaderFactory headerFactory) {
        this.headerFactory = headerFactory;
        this.fileFilter = new HiddenFilesFilter();
    }

    public FilesTreeFactory(FileHeaderFactory headerFactory, FileFilter filter) {
        this.headerFactory = headerFactory;
        this.fileFilter = filter;
    }

    public FilesTree create(Path path, ProgressReportConsumer progress)
            throws IOException, SyntaxErrorException {
        FoldersAndFiles data = this.extractFolder(path, progress);
        FilesTree result =
                new FilesTree(path, data.files, data.folders, this.headerFactory);
        if (progress != null) {
            progress.done();
        }
        return result;
    }

    private FoldersAndFiles extractFolder(Path path, ProgressReportConsumer progress)
            throws IOException, SyntaxErrorException {
        FoldersAndFiles result = new FoldersAndFiles();

        for(Path child : path.getChildren()) {
            if (!this.fileFilter.accept(child)) {
                continue;
            }

            if (progress != null) {
                progress.progress();
            }
            
            if (child.isDirectory()) {
                Folder folder = this.createFolder(child, progress);
                result.folders.add(folder);
            }
            else if (child.isFile() && 
                    this.headerFactory.hasParserFor(child)) {
                result.files.add(this.createFile(child));
            }
        }

        return result;
    }

    private Folder createFolder(Path path, ProgressReportConsumer progress)
            throws IOException, SyntaxErrorException {
        FoldersAndFiles data = this.extractFolder(path, progress);
        return new Folder(path, data.files, data.folders);
    }

    private File createFile(Path path)
            throws IOException, SyntaxErrorException {
        HeaderParser.HeaderAndAlternatingParts headerData =
                this.headerFactory.create(path);
        return new File(path, headerData.header, headerData.alternatingParts);
    }

    private class FoldersAndFiles {
        List<Folder> folders = new Vector<Folder>();
        List<File> files = new Vector<File>();
    }

    public class HiddenFilesFilter implements FileFilter {
        public boolean accept(java.io.File pathname) {
            return !pathname.isHidden();
        }
    }
}
