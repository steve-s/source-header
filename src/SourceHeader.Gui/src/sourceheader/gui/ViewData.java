/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sourceheader.gui;

import sourceheader.core.*;

/**
 *
 * @author steve
 */
public class ViewData {
    
    private FileHeader currentHeader;
    private ViewDataObserver observer;
    private FilesTree filesTree;

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
        void filesTreeChanged();
    }
}
