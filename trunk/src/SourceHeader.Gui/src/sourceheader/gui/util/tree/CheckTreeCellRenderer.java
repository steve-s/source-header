/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sourceheader.gui.util.tree;

import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.*;
import javax.swing.tree.*;

public class CheckTreeCellRenderer extends JPanel implements TreeCellRenderer{
    private CheckTreeSelectionModel selectionModel;
    private TreeCellRenderer delegate;
    private JCheckBox checkBox = new JCheckBox();

    public CheckTreeCellRenderer(TreeCellRenderer delegate, CheckTreeSelectionModel selectionModel){
        this.delegate = delegate;
        this.selectionModel = selectionModel;
        setLayout(new BorderLayout());
        setOpaque(false);
        checkBox.setOpaque(false);
    }

    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus){
        Component renderer = delegate.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

        TreePath path = tree.getPathForRow(row);
        if(path!=null){
            checkBox.setSelected(selectionModel.isPathSelected(path, true));
        }
        removeAll();
        add(checkBox, BorderLayout.WEST);
        add(renderer, BorderLayout.CENTER);
        return this;
    }
}
