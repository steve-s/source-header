/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sourceheader.core;

import java.io.*;
import sourceheader.core.util.Filesystem;

/**
 *
 * @author steve
 */
public class File {

    private static Path tmpPath = new Path(".");
    private java.io.File tmpFile;
    private java.io.File backupFile;

    public static void setTmpPath(Path path) {
        tmpPath = path;
    }

    private Path path;
    private FileHeader header;
    private FileHeader lastHeader;

    protected File(Path path, FileHeader header) {
        this.path = path;
        this.header = header;
        this.lastHeader = new FileHeader(header);
    }

    public Path getPath() {
        return this.path;
    }
    
    public String getName() {
        return this.path.getName();
    }
    
    public String getExtension() {
        return this.path.getExtension();
    }

    public FileHeader getHeader() {
        return this.header;
    }

    public void setHeader(FileHeader header) {
        this.header = header;
    }

    /**
     * Updates data to physical file on filesystem.
     */
    public void update() throws BackupCannotBeCareatedException,
            FileCannotBeUpdateException {
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
            throws FileNotFoundException, IOException {        
        this.tmpFile =
                java.io.File.createTempFile(this.getTmpFilename(), "new");
        this.copyHeaderTo(this.getTmpFilePath());
        // copy content of previous file to new one, without old header.
        // I have to skip newlines count + 1, because eg. one line header
        // has no newlines.
        Filesystem.copyText(this.getPath(), this.getTmpFilePath(),
                this.lastHeader.getNewlinesCount()+1);
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
    private void copyHeaderTo(java.io.File path) throws IOException {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(path, false));
            writer.write(this.getHeader().getContent());
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
