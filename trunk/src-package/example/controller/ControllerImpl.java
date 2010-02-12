/*
 * This file is part of SourceHeader project.
 * licence: FreeBSD licence.
 *
 * @category Controller
 *
 * file: ControllerImpl.java
 */

package sourceheader.gui.controller;

import java.io.FileFilter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import sourceheader.core.*;
import sourceheader.core.File.BackupCannotBeCareatedException;
import sourceheader.core.File.FileCannotBeUpdateException;
import sourceheader.core.HeaderParser.SyntaxErrorException;
import sourceheader.core.parsers.*;
import sourceheader.core.util.Filesystem;
import sourceheader.gui.IView;
import sourceheader.gui.ViewData;

/**
 *
 * @author steve
 */
public class ControllerImpl implements Controller {

    private final String newHeaderContent =
            "/** \n" +
            " * This is content of new header. \n" +
            " * You can change this text and than \n" +
            " * upload it as new header of selected files. \n" +
            " * or you can prepend or append it to \n" +
            " * all headers of selected files. \n" +
            " **/ ";

    private final ViewData viewData;
    private final IView view;
    private final FileHeaderFactory headerFactory;
    private final FilesTreeFactory treeFactory;

    public ControllerImpl(ViewData viewData, IView view) {
        this.viewData = viewData;
        this.view = view;

         this.headerFactory = new FileHeaderFactory(
                    new ParsersConfig('$'),
                    new HeaderParser[] {
                        new CppParser(new ParsersConfig('$')),
                        new ScriptsParser(new ParsersConfig('$')),
                        new PhpParser(new ParsersConfig('$'))
                  });
         this.treeFactory = new FilesTreeFactory(headerFactory);

        this.viewData.setNewHeader(
                this.headerFactory.create(this.newHeaderContent));
    }

    public void currentHeaderSelectChanged(FileHeader header) {
        this.viewData.setCurrentHeader(header);
    }

    public void chooseRootButtonClicked() {
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

            this.view.setStatusText("Parsing headers....");
            new Thread(new Runnable() {
                public void run() {
                    try {
                        FilesTree tree = 
                                treeFactory.create(path,
                                    view.getProgressReportConsumer());
                        viewData.setFilesTree(tree);
                        view.setStatusText("");
                    } catch (IOException ex) {
                        view.warningDialog("An IO problem occured. Exception: " +
                                ex.getMessage(), "Ooops...");
                    } catch (SyntaxErrorException ex) {
                        view.warningDialog("There is syntax error in file " + path + ". " +
                                "Does it's extension correspond to it's content?",
                                "Ooops...");
                    }
                }
            }).start();
        }
    }

    public void updateHeaderButtonClicked() {
        this.view.setStatusText("Updating...");
        this.viewData.getCurrentHeader().setContent(this.view.getCurrentHeaderContent());
        new Thread(new Runnable() {
            public void run() {
                try {
                    viewData.getFilesTree().update(view.getProgressReportConsumer());
                } catch (BackupCannotBeCareatedException ex) {
                    view.warningDialog("Backup files cannot be created.", "Ooops...");
                } catch (FileCannotBeUpdateException ex) {
                    view.warningDialog("Error occured: " + ex.getMessage(), "Ooops...");
                }
                view.setStatusText("");
            }
        }).start();
    }

    public void uploadHeaderButtonClicked() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void appendHeaderButtonClicked() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    public void prependHeaderButtonClicked() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
