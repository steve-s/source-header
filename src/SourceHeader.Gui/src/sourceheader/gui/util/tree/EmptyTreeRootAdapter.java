/*
 * file EmptyTreeRootAdapter.java
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

import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author steve
 */
public class EmptyTreeRootAdapter extends DefaultMutableTreeNode {

    public EmptyTreeRootAdapter() {
        super("To open some folder");

        DefaultMutableTreeNode node1 = new DefaultMutableTreeNode("click open button");
        this.add(node1);
        node1.add(new DefaultMutableTreeNode("On the right ->"));

        DefaultMutableTreeNode node2 = new DefaultMutableTreeNode("or you can also");
        this.add(node2);
        node2.add(new DefaultMutableTreeNode("click file menu->open"));
    }
}
