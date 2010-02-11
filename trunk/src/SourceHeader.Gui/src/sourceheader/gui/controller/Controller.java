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
    boolean isWorking();
    void stopWorking();
}
