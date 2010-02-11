/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sourceheader.core;

import java.io.FileFilter;
import java.io.IOException;
import java.util.*;
import sourceheader.core.HeaderParser.SyntaxErrorException;

/**
 * Creates instances of FilesTree.
 * 
 * User provide path on filesystem and FilesTreeFactory 
 * will create whole graph of instances representing the tree.
 *
 * @author steve
 */
public class FilesTreeFactory {

    private FileHeaderFactory headerFactory;
    private FileFilter fileFilter;

    /**
     * Creates tree factory with given headers factory.
     * @param headerFactory
     */
    public FilesTreeFactory(FileHeaderFactory headerFactory) {
        this.headerFactory = headerFactory;
        this.fileFilter = new HiddenFilesFilter();
    }

    /**
     * Creates tree factory with given headers factory and files filter
     * for excluding files and folders like '.svn'.
     * @param headerFactory
     * @param filter
     */
    public FilesTreeFactory(FileHeaderFactory headerFactory, FileFilter filter) {
        this.headerFactory = headerFactory;
        this.fileFilter = filter;
    }

    /**
     * Creates FilesTree from given path on filesystem.
     * @param path
     * @param progress Progress listener.
     * @return New instance of FilesTree
     * @throws IOException
     * @throws sourceheader.core.HeaderParser.SyntaxErrorException
     */
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

    /**
     * Extracts files and folders from given folder and calls appropriate methods.
     * @param path Folder's path.
     * @param progress Progress listener.
     * @return List of folders and files - childern of given folder.
     * @throws IOException
     * @throws sourceheader.core.HeaderParser.SyntaxErrorException
     */
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

    /**
     * Creates Folder class instance from given path.
     * @param path Path on filesystem.
     * @param progress Progress listener.
     * @return Folder instance.
     * @throws IOException
     * @throws sourceheader.core.HeaderParser.SyntaxErrorException
     */
    private Folder createFolder(Path path, ProgressReportConsumer progress)
            throws IOException, SyntaxErrorException {
        FoldersAndFiles data = this.extractFolder(path, progress);
        return new Folder(path, data.files, data.folders);
    }

    /**
     * Creates File class instance from given path.
     * @param path File's path on filesystem.
     * @return File instance.
     * @throws IOException
     * @throws sourceheader.core.HeaderParser.SyntaxErrorException
     */
    private File createFile(Path path)
            throws IOException, SyntaxErrorException {
        HeaderParser.HeaderAndAlternatingParts headerData =
                this.headerFactory.create(path);
        return new File(path, headerData.header, headerData.alternatingParts);
    }

    /**
     * Encapsulates return value for method extractFolder.
     */
    private class FoldersAndFiles {
        List<Folder> folders = new Vector<Folder>();
        List<File> files = new Vector<File>();
    }

    /**
     * Useful file filter.
     */
    public class HiddenFilesFilter implements FileFilter {
        public boolean accept(java.io.File pathname) {
            return !pathname.isHidden();
        }
    }
}
