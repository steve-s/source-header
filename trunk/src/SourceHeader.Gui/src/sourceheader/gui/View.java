/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sourceheader.gui;

import java.awt.*;
import java.awt.event.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import sourceheader.core.*;
import sourceheader.gui.controller.*;
import sourceheader.gui.util.*;

/**
 *
 * @author steve
 */
public class View extends JFrame
        implements IView, ViewData.ViewDataObserver {

    private final JTree filesTree = new JTree();
    private final Controller controller;
    private final JTextArea souceTextArea = new JTextArea();
    private final ViewData viewData;

    public View() {
        super("Source header v0.1");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {}
        this.setLayout(new BorderLayout(10, 3));

        this.viewData = new ViewData();
        this.controller = new ControllerImpl(this.viewData, this);
        this.viewData.setObserver(this);

        this.initCentralSplitPane();
        this.initTopPanel();

        this.pack();
        this.setVisible(true);
    }

    private void initTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout(10,10));

        JButton openButton = new JButton("Open...");
        openButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                View.this.controller.chooseRootButtonClicked();
            }
        });
        topPanel.add(openButton, BorderLayout.WEST);

        JPanel sourceControlPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        
        this.add(topPanel, BorderLayout.NORTH);
    }

    private void initCentralSplitPane() {
        JSplitPane centralSplitPane = new JSplitPane();
        centralSplitPane.setDividerSize(10);
        centralSplitPane.setDividerLocation(0.3f);

        this.filesTree.setCellRenderer(new FilesTreeCellRenderer());
        this.filesTree.addTreeSelectionListener(new FilesTreeSelectionListener());

        centralSplitPane.setLeftComponent(this.filesTree);
        centralSplitPane.setRightComponent(this.souceTextArea);
        this.add(centralSplitPane, BorderLayout.CENTER);
    }

    public Path getPathFromUser() {
        JFileChooser dialog = new JFileChooser();
        dialog.setMultiSelectionEnabled(false);
        dialog.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (dialog.showOpenDialog(View.this) == JFileChooser.APPROVE_OPTION) {
            return new Path(dialog.getSelectedFile());
        }

        return null;
    }

    public void warningDialog(String content, String title) {
        JOptionPane.showMessageDialog(this, content, title, JOptionPane.WARNING_MESSAGE);
    }

    public boolean questionDialog(String content, String title) {
        int result = JOptionPane.showConfirmDialog(
                            this, content, title,
                            JOptionPane.YES_NO_CANCEL_OPTION);
        return result == JOptionPane.YES_OPTION;
    }

    public void indeterminateProgress() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void determinateProgress() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void currentHeaderChanged() {
        this.souceTextArea.setText(
                this.viewData.getCurrentHeader().getContent());
    }

    public void filesTreeChanged() {
        this.filesTree.setModel(
            new DefaultTreeModel(
                new TreeRootAdapter(this.viewData.getFilesTree())));
        this.filesTree.setCellRenderer(new FilesTreeCellRenderer());
    }

    private class FilesTreeSelectionListener implements TreeSelectionListener {
        public void valueChanged(TreeSelectionEvent e) {
            Object obj = e.getPath().getLastPathComponent();
            if (obj instanceof DefaultMutableTreeNode) {
                if (((DefaultMutableTreeNode) obj).getUserObject() instanceof File) {
                    File file = (File) ((DefaultMutableTreeNode)obj).getUserObject();
                    View.this.controller.currentHeaderSelectChanged(file.getHeader());
                }
            }
        }
    }
}
