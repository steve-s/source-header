/*
 * file Controller.java
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

import sourceheader.core.FileHeader;

/**
 * Controller handles input from user.
 * These method should be invoked by View.
 *
 * @author steve
 */
public interface Controller {
    /**
     * User clicked 'open folder' button.
     */
    void chooseRootButtonClicked();
    /**
     * Simple selection of header changed.
     * @param header
     */
    void currentHeaderSelectChanged(FileHeader header);
    /**
     * User clicked 'save changes' button.
     */
    void updateHeaderButtonClicked();
    /**
     * User clicked 'upload header to selected files' button.
     */
    void uploadHeaderButtonClicked();
    /**
     * User clicked 'append header to selected files' button.
     */
    void appendHeaderButtonClicked();
    /**
     * User clicked 'prepend header to selected files' button.
     */
    void prependHeaderButtonClicked();

    /**
     * Indicates wheter controller runs some working thread on background.
     * @return True if there is thread working.
     */
    boolean isWorking();
    /**
     * Suspends any thead that controller has started.
     * This action should be called before exiting application.
     */
    void stopWorking();
}
