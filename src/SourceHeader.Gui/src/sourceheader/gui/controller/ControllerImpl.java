/*
 * file ControllerImpl.java
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

package sourceheader.gui.controller;

import java.io.FileFilter;
import java.io.IOException;
import java.util.*;
import sourceheader.core.*;
import sourceheader.core.File.BackupCannotBeCareatedException;
import sourceheader.core.File.FileCannotBeUpdateException;
import sourceheader.core.HeaderParser.SyntaxErrorException;
import sourceheader.core.parsers.*;
import sourceheader.core.util.Filesystem;
import sourceheader.gui.*;
import sourceheader.gui.util.preferences.ApplicationPreferences;

/**
 *
 * @author steve
 */
public class ControllerImpl implements Controller {

    private final String newHeaderContent =
            "/** \n" +
            " * Welcome to SourceHeader!\n" +
            " * \n" +
            " * This is content of a new header. You can change this text and than \n" +
            " * upload it as a new header of selected files or you can prepend or append it to \n" +
            " * all headers of selected files. \n" +
            " * \n" +
            " * When creating header's content you may use variable %filename%.\n" +
            " * There is also concept of alternating blocks:\n" +
            " * These are blocks of content that may vary in the same headers,\n" +
            " * for example javaDoc tag @category might be different, but if all\n" +
            " * other contents are the same, SourceHeader will identify such headers as same.\n" +
            " * Alternating blocks are replaced with %alt-block-name%{number}%.\n" +
            " * \n" +
            " * Of course you cannot use an alternating block which content is not aviable.\n" +
            " * (in other words: it was not extracted from header of existing file).\n" +
            " * This especially means that you cannot use alternating blocks\n" +
            " * here in content of new header.\n" +
            " * \n" +
            " * When you update files somehow in program their headers are not again\n" +
            " * parsed for alternating blocks. So it is recommended to open folder again\n" +
            " * after an extensive work with alternating blocks.\n" +
            " **/ ";

    private final ViewData viewData;
    private final IView view;
    private final FileHeaderFactory headerFactory;
    private final FilesTreeFactory treeFactory;
    private volatile boolean isWorking = false;
    private Thread workingThread = null;
    private final ApplicationPreferences preferences;

    public ControllerImpl(ViewData viewData, IView view, ApplicationPreferences preferences) {
        this.viewData = viewData;
        this.view = view;

        ParsersConfig config = new ParsersConfig(
                preferences.getSpecialCharacter(),
                preferences.getAlternatingParts());
         this.headerFactory = new FileHeaderFactory(
                    config,
                    new HeaderParser[] {
                        new CppParser(config),
                        new ScriptsParser(config),
                        new PhpParser(config)
                  });
         this.treeFactory = new FilesTreeFactory(headerFactory);

        this.viewData.setNewHeader(
                this.headerFactory.create(this.newHeaderContent));
        this.preferences = preferences;
    }

    public boolean isWorking() {
        return this.isWorking;
    }

    public void currentHeaderSelectChanged(FileHeader header) {
        this.viewData.setCurrentHeader(header);
    }

    public void chooseRootButtonClicked() {
        if (this.isWorking) {
            this.view.warningDialog("Please wait until update is done.", "Updating...");
            return;
        }

        final Path path = this.view.getPathFromUser();
        if (path != null) {
            long filesCount =
                    Filesystem.getFilesCount(path, 400, new FileFilter() {
                            public boolean accept(java.io.File pathname) {
                                return !pathname.isHidden() &&
                                    !pathname.getName().startsWith(".");
                            }
                        });

            if (filesCount >= 100) {
                String warning = "Directory contains " + filesCount +
                        " files. Processing it may take some time. Are you sure?";
                if (filesCount == 400) {
                    warning = "Directory contains more that 400 files. " +
                        "Processing it may take some time. Are you sure?";
                }
                boolean result =
                    this.view.questionDialog(warning, "Lot of files.");
                if (!result) {
                    return;
                }
            }

            this.isWorking = true;
            this.view.setStatusText("Parsing headers....");
            this.workingThread = new BackgroundThread(new Runnable() {
                public void run() {
                    try {
                        FilesTree tree = 
                                treeFactory.create(path,
                                    view.getProgressReportConsumer());
                        viewData.setFilesTree(tree);
                        view.setStatusText("");
                        isWorking = false;
                    } catch (IOException ex) {
                        view.warningDialog("An IO problem occured. Exception: " +
                                ex.getMessage(), "Ooops...");
                    } catch (SyntaxErrorException ex) {
                        view.warningDialog("There is syntax error in file " + path + ". " +
                                "Does it's extension correspond to it's content?",
                                "Ooops...");
                    }
                }
            });
            this.workingThread.start();
        }
    }

    public void updateHeaderButtonClicked() {
        if (checkFilesTree()) {
            return;
        }

        this.viewData.getCurrentHeader().setContent(this.view.getCurrentHeaderContent());
        this.runTreeUpdate();
    }

    public void uploadHeaderButtonClicked() {
        if (checkFilesTree()) {
            return;
        }

        FileHeader newHeader = this.headerFactory.create(this.view.getNewHeaderContent());
        List<File> files = this.view.getSelectedFiles();
        for (File file : files) {
            file.setHeader(newHeader);
        }

        this.runTreeUpdate();
    }

    public void appendHeaderButtonClicked() {
        if (checkFilesTree()) {
            return;
        }
        
        this.insertToHeaders(true);
    }

    public void prependHeaderButtonClicked() {
        if (checkFilesTree()) {
            return;
        }

        this.insertToHeaders(false);
    }

    public void stopWorking() {
        if (this.isWorking) {
            this.workingThread.suspend();
        }
    }

    private boolean checkFilesTree() {
        if (this.viewData.getFilesTree() == null) {
            this.view.warningDialog("Please first open some folder.", "No files");
            return true;
        }

        if (this.isWorking) {
            this.view.warningDialog("Please wait until update is done.", "Updating...");
            return true;
        }
        
        return false;
    }

    private void insertToHeaders(boolean append) {
        String toBeInserted;
        if (this.viewData.isNewHeaderVisible()) {
            toBeInserted = this.view.getNewHeaderContent();
        }
        else {
            toBeInserted = this.view.getCurrentHeaderContent();
        }

        List<File> files = this.view.getSelectedFiles();
        for (File file : files) {
            FileHeader header = file.getHeader();
            String content = header.getContent();

            if (append) {
                content += "\n" + toBeInserted;
            }
            else {
                content = toBeInserted + "\n" + content;
            }

            file.setHeader(this.headerFactory.create(content));
        }

        this.runTreeUpdate();
    }

    private void runTreeUpdate() {
        this.view.setStatusText("Updating...");
        this.workingThread = new BackgroundThread(new Runnable() {

            public void run() {
                isWorking = true;
                try {
                    viewData.getFilesTree().update(view.getProgressReportConsumer());
                } catch (BackupCannotBeCareatedException ex) {
                    view.warningDialog("Backup files cannot be created.", "Ooops...");
                } catch (FileCannotBeUpdateException ex) {
                    view.warningDialog("Error occured: " + ex.getMessage(), "Ooops...");
                } catch (FileHeader.ContentSyntaxErrorException ex) {
                    view.warningDialog("Content header contains some syntax error.\n" +
                            "Didn't you use content of alternating part, that does not exist?", 
                            "Error");
                }
                view.updateTree();
                view.setStatusText("");                
                isWorking = false;
            }
        });
        this.workingThread.start();
    }

    private static class BackgroundThread extends Thread {
        public BackgroundThread(Runnable target) {
            super(target);
            super.setDaemon(true);
        }
    }
}
