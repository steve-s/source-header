/*
 * This file is part of SourceHeader project.
 * licence: FreeBSD licence.
 *
 * @category Gui
 *
 * file: ViewData.java
 */

package sourceheader.gui;

import sourceheader.core.*;

/**
 * Encapsulates data shared between controller and view.
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

    public interface ViewDataObserver {
        void currentHeaderChanged();
        void newHeaderChanged();
        void filesTreeChanged();
    }
}
