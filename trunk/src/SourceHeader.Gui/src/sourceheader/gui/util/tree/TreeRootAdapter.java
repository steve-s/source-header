/*
 * file TreeRootAdapter.java
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
