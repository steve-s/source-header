/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sourceheader.gui.util;

import sourceheader.core.*;
import javax.swing.tree.*;

/**
 *
 * @author steve
 */
public class TreeNodeAdapter extends DefaultMutableTreeNode {

    private Folder folder;

    public TreeNodeAdapter(Folder folder) {
        this.folder = folder;
    }

    public void refresh() {
        for(Folder subfolder : this.folder.getFolders()) {
            this.add(new TreeNodeAdapter(subfolder));
        }

        for(File file : this.folder.getFiles()) {
            this.add(new Leaf(file));
        }
    }

    @Override
    public String toString() {
        return this.folder.getName();
    }

    private class Leaf extends DefaultMutableTreeNode {
        private File file;

        public Leaf(File file) {
            this.file = file;
        }

        @Override
        public String toString() {
            return this.file.getName();
        }
    }
}
