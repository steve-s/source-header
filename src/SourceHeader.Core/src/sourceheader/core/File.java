/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sourceheader.core;

import java.util.*;
import java.io.*;
import sourceheader.core.util.Filesystem;

/**
 * Represents file with it's header.
 *
 * @author steve
 */
public class File {

    private static Path tmpPath = new Path(".");
    private java.io.File tmpFile;
    private java.io.File backupFile;
    private Map<String, List<String>> alternatingParts;

    /**
     * Sets the path where tmp files will be created.
     * @param path
     */
    public static void setTmpPath(Path path) {
        tmpPath = path;
    }

    private Path path;
    private FileHeader header;
    private FileHeader lastHeader;

    /**
     * File instances are hold in FilesTree and should be created
     * via FilesTreeFactory.
     *
     * @param path Path on filesystem.
     * @param header Header instance.
     * @param alternatingParts Real values of alternating parts in header.
     */
    protected File(Path path, 
            FileHeader header,
            Map<String, List<String>> alternatingParts) {
        this.path = path;
        this.header = header;
        this.lastHeader = new FileHeader(header);
        this.alternatingParts = alternatingParts;
    }

    /**
     * @return The filesystem path of this file.
     */
    public Path getPath() {
        return this.path;
    }

    /**
     * @return Name of file.
     */
    public String getName() {
        return this.path.getName();
    }

    /**
     * @return File extemsion.
     */
    public String getExtension() {
        return this.path.getExtension();
    }

    /**
     * @return The header of this file.
     */
    public FileHeader getHeader() {
        return this.header;
    }

    /**
     * Sets new header.
     *
     * New header must not use any alternating part which content
     * is not in this.alternatingParts map, otherwise method update
     * will generate exception.
     *
     * @param header
     */
    public void setHeader(FileHeader header) {
        this.header = header;
    }

    /**
     * Updates data to physical file on filesystem.
     */
    public void update() throws BackupCannotBeCareatedException,
            FileCannotBeUpdateException, FileHeader.ContentSyntaxErrorException {
        if (this.lastHeader.equals(this.header)) {
            return;
        }

        try {
            this.createBackup();
            this.crateTmpFileWithNewContent();
            this.replaceWithTmpFile();
        }
        catch (IOException exception) {
            throw new FileCannotBeUpdateException(exception);
        }
        finally {
            this.getTmpFilePath().delete();
            this.getBackupFilePath().delete();
        }

        this.lastHeader = new FileHeader(this.header);
    }

    /**
     * @return unique name for tmp file.
     */
    private String getTmpFilename() {
        String result = this.path.getAbsolutePath()
                .replace("\\", "_")
                .replace("/", "_")
                .replace(":", "-");
        return result;
    }

    /**
     * @return path to tmp file.
     */
    private java.io.File getTmpFilePath() {
        //return new Path(tmpPath, this.getTmpFilename());
        return this.tmpFile;
    }

    private java.io.File getBackupFilePath() {
        //return new Path(tmpPath, this.getTmpFilename() + ".bac");
        return this.backupFile;
    }

    /**
     * Creates tmp file with current file content, that is current header
     * via this.getHeader() and other content from filesystem.
     */
    private void crateTmpFileWithNewContent()
            throws FileNotFoundException, IOException, FileHeader.ContentSyntaxErrorException {
        this.tmpFile =
                java.io.File.createTempFile(this.getTmpFilename(), "new");
        this.copyHeaderTo(this.getTmpFilePath());
        // copy content of previous file to new one, without old header.
        // I have to skip newlines count + 1, because eg. one line header
        // has no newlines.
        Filesystem.copyText(this.getPath(), this.getTmpFilePath(),
                this.lastHeader.getNewlinesCount(this.alternatingParts)+1);
    }

    /**
     * Replaces this file with content from tmp file.
     * @throws IOException
     */
    private void replaceWithTmpFile() throws IOException {
        assert this.getTmpFilePath() != null;
        try {
            Filesystem.copyText(this.getTmpFilePath(),this.getPath(), 0, false);
        }
        catch(IOException exception) {
            try {
                this.getPath().delete();
                Filesystem.copyText(this.getBackupFilePath(),this.getPath());
            }
            catch(IOException ex) {}

            throw exception;
        }
    }

    /**
     * Created backup of this file.
     * @throws sourceheader.core.File.BackupCannotBeCareatedException
     */
    private void createBackup() throws BackupCannotBeCareatedException {
        try {
            this.backupFile =
                    java.io.File.createTempFile(this.getTmpFilename(), "back");
            Filesystem.copyText(this.getPath(), this.getBackupFilePath());
        }
        catch(IOException ex) {
            throw new BackupCannotBeCareatedException(ex);
        }
    }

    /**
     * Copies current header to given file.
     * @param path
     * @throws IOException
     */
    private void copyHeaderTo(java.io.File path) 
            throws IOException, FileHeader.ContentSyntaxErrorException {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(path, false));
            writer.write(this.getHeader().getRawContent(this.alternatingParts,
                    this.getPath().getName()));
            writer.write('\n');
        }
        finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    @Override
    public String toString() {
        return this.getPath().getName();
    }        

    public class FileCannotBeUpdateException extends IOException {
        public FileCannotBeUpdateException(Throwable exception) {
            super("File header cannot be updated.", exception);
        }
    }

    public class BackupCannotBeCareatedException extends IOException {
        public BackupCannotBeCareatedException(Throwable exception) {
            super("Backup file cannot be ceated.", exception);
        }
    }
}
