/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sourceheader.gui.util;

import java.awt.Component;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.*;

/**
 *
 * @author steve
 */
public class FilesTreeCellRenderer implements TreeCellRenderer {

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

        return panel;
    }

}
