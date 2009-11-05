/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sourceheader.gui.util;

import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.*;

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

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, 2));

        JCheckBox checkBox = new JCheckBox();
        panel.add(checkBox);

        JLabel label = new JLabel(value.toString());
        panel.add(label);

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
