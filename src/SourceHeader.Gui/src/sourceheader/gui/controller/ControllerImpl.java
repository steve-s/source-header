/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sourceheader.gui.controller;

import java.io.FileFilter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import sourceheader.core.*;
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
                        new ScriptsParser(new ParsersConfig('$'))
                  });
         this.treeFactory = new FilesTreeFactory(headerFactory);
    }

    public void currentHeaderSelectChanged(FileHeader header) {
        this.viewData.setCurrentHeader(header);
    }

    public void chooseRootButtonClicked() {
        Path path = this.view.getPathFromUser();
        if (path != null) {
            try {
                long filesCount =
                        Filesystem.getFilesCount(path, 40, new FileFilter() {
                                public boolean accept(java.io.File pathname) {
                                    return !pathname.isHidden() &&
                                        !pathname.getName().startsWith(".");
                                }
                            });

                if (filesCount >= 40) {
                    boolean result =
                        this.view.questionDialog("Directory contains too many files. Processing it may take some time. Are you sure?",
                            "Too many files.");
                    if (!result) {
                        return;
                    }
                }

                FilesTree tree = this.treeFactory.create(path);
                this.viewData.setFilesTree(tree);
            } catch (IOException ex) {
                this.view.warningDialog("TODO", "TODO");
            } catch (SyntaxErrorException ex) {
                this.view.warningDialog("TODO", "TODO");
            }
        }
    }
}
