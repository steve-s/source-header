/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sourceheader.gui.util.tree;

import sourceheader.gui.util.*;
import java.awt.*;
import java.awt.image.*;
import java.util.*;
import javax.swing.*;
import javax.swing.tree.*;
import sourceheader.core.File;
import sourceheader.core.FileHeader;

/**
 *
 * @author steve
 */
public class FilesTreeCellRenderer implements TreeCellRenderer {
    private Icon getLeafIcon() {
        Icon icon = UIManager.getIcon("Tree.leafIcon");
        return icon;
    }

    private Icon getOpenIcon() {
        return UIManager.getIcon("Tree.openIcon");
    }

    private Icon getClosedIcon() {
        return UIManager.getIcon("Tree.closedIcon");
    }

    public Component getTreeCellRendererComponent(
            JTree tree,
            Object value,
            boolean selected,
            boolean expanded,
            boolean leaf,
            int row,
            boolean hasFocus) {

        JLabel label = new JLabel(value.toString());

        Icon leafIcon = this.getOpenIcon();
        if (value instanceof DefaultMutableTreeNode &&
            ((DefaultMutableTreeNode)value).getUserObject() instanceof File) {
            File file = (File)((DefaultMutableTreeNode)value).getUserObject();
            leafIcon = ColorsMap.getInstance().getIconForHeader(file.getHeader());
        }

        if (leaf) {
            label.setIcon(leafIcon);
        }
        else if (expanded) {
            label.setIcon(this.getOpenIcon());
        }
        else {
            label.setIcon(this.getClosedIcon());
        }

        return label;
    }
}
