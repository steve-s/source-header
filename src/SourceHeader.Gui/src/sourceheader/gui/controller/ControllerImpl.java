/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sourceheader.gui.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import sourceheader.core.*;
import sourceheader.core.HeaderParser.SyntaxErrorException;
import sourceheader.core.parsers.*;
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

    public void currentHeaderSelectChanged() {
        
    }

    public void chooseRootButtonClicked() {
        Path path = this.view.getPathFromUser();
        if (path != null) {
            try {
                FilesTree tree = this.treeFactory.create(path);
                this.viewData.setFilesTree(tree);
            } catch (IOException ex) {
                this.view.warningDialog("TODO", "TODO");
                Logger.getLogger(ControllerImpl.class.getName())
                        .log(Level.SEVERE, null, ex);
            } catch (SyntaxErrorException ex) {
                this.view.warningDialog("TODO", "TODO");
                Logger.getLogger(ControllerImpl.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }
    }
}
