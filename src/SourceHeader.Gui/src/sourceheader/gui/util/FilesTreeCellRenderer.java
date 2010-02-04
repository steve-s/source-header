/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sourceheader.gui.util;

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
    private Dictionary<FileHeader, Color> headerColors = new Hashtable<FileHeader, Color>();
    private final java.util.List<Color> colorsList =
            new ArrayList<Color>() {
                {
                    add(Color.RED);
                    add(Color.BLUE);
                    add(Color.YELLOW);
                    add(Color.GREEN);
                }};

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
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, 2));

        JCheckBox checkBox = new JCheckBox();
        panel.add(checkBox);

        JLabel label = new JLabel(value.toString());
        panel.add(label);

        if (value instanceof DefaultMutableTreeNode &&
            ((DefaultMutableTreeNode)value).getUserObject() instanceof File) {
            File file = (File)((DefaultMutableTreeNode)value).getUserObject();

            Color color = this.headerColors.get(file.getHeader());
            if (color == null && this.colorsList.size() > 0) {
                color = this.colorsList.get(this.colorsList.size()-1);
                this.colorsList.remove(color);
                this.headerColors.put(file.getHeader(), color);
            }

            if (color != null) {
                label.setForeground(color);
            }
        }

        if (leaf) {
            label.setIcon(this.getLeafIcon());
        }
        else if (expanded) {
            label.setIcon(this.getOpenIcon());
        }
        else {
            label.setIcon(this.getClosedIcon());
        }

        return panel;
    }

}
