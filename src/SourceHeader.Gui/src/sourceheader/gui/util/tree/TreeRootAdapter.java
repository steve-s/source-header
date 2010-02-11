/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sourceheader.gui.util.tree;

import javax.swing.tree.*;
import sourceheader.core.*;

/**
 *
 * @author steve
 */
public class TreeRootAdapter extends DefaultMutableTreeNode {

    FilesTree filesTree;

    public TreeRootAdapter(FilesTree filesTree) {
        super(filesTree);
        this.filesTree = filesTree;
        this.update();
    }
    
    public final void update() {
        this.update((Folder)filesTree, this);
    }

    private void update(Folder folder, DefaultMutableTreeNode node) {
        for (Folder childFolder : folder.getFolders()) {
            DefaultMutableTreeNode childNode =
                    new DefaultMutableTreeNode(childFolder);
            this.update(childFolder, childNode);
            node.add(childNode);
        }

        for (File childFile : folder.getFiles()) {
            DefaultMutableTreeNode childNode =
                    new DefaultMutableTreeNode(childFile);
            node.add(childNode);
        }
    }
}
