/*
 * file ViewData.java
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

package sourceheader.gui;

import sourceheader.core.*;
import sourceheader.gui.controller.Controller;

/**
 * Encapsulates data shared between {@link Controller controller} and
 * {@lin View view}.
 * Changes can be observed by class implements {@link ViewDataObserver ViewDataObserver}.
 *
 * @author steve
 */
public class ViewData {
    
    private FileHeader currentHeader;
    private FileHeader newHeader;
    private ViewDataObserver observer;
    private FilesTree filesTree;
    private boolean newHeaderVisible = true;

    public void setNewHeader(FileHeader newHeader) {
        this.newHeader = newHeader;
        if (this.observer != null) {
            this.observer.newHeaderChanged();
        }
    }

    public boolean isNewHeaderVisible() {
        return newHeaderVisible;
    }

    public void setNewHeaderVisible(boolean newHeaderVisible) {
        this.newHeaderVisible = newHeaderVisible;
    }

    public FileHeader getNewHeader() {
        return this.newHeader;
    }

    public FilesTree getFilesTree() {
        return this.filesTree;
    }

    public void setFilesTree(FilesTree filesTree) {
        this.filesTree = filesTree;
        if (this.observer != null) {
            this.observer.filesTreeChanged();
        }
    }

    public void setObserver(ViewDataObserver observer) {
        this.observer = observer;
    }
    
    public FileHeader getCurrentHeader() {
        return this.currentHeader;
    }
    
    public void setCurrentHeader(FileHeader header) {
        this.currentHeader = header;
        if (this.observer != null ) {
            this.observer.currentHeaderChanged();
        }
    }

    /**
     * Interface for class that wants to observer changes of {@link ViewData ViewData}.
     */
    public interface ViewDataObserver {
        void currentHeaderChanged();
        void newHeaderChanged();
        void filesTreeChanged();
    }
}
