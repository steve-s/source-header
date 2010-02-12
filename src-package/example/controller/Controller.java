/*
 * This file is part of SourceHeader project.
 * licence: FreeBSD licence.
 *
 * @category Controller
 *
 * file: Controller.java
 */
 
package sourceheader.gui.controller;

import sourceheader.core.FileHeader;

/**
 *
 * @author steve
 */
public interface Controller {
    void chooseRootButtonClicked();
    void currentHeaderSelectChanged(FileHeader header);
    void updateHeaderButtonClicked();
    void uploadHeaderButtonClicked();
    void appendHeaderButtonClicked();
    void prependHeaderButtonClicked();
}
