/*
 * file View.java
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

package sourceheader.gui;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URI;
import sourceheader.gui.util.tree.*;
import sourceheader.gui.util.preferences.ApplicationPreferences;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import sourceheader.core.*;
import sourceheader.gui.controller.*;
import sourceheader.gui.dialogs.*;

/**
 *
 * @author steve
 */
public class View extends JFrame
        implements IView, ViewData.ViewDataObserver {

    private final JTree filesTree = new JTree();
    private final Controller controller;
    private final JTextArea currentHeaderTextArea = new JTextArea();
    private final JTextArea newHeaderTextArea = new JTextArea();
    private final ApplicationPreferences preferences = new ApplicationPreferences();
    private final JSplitPane splitPane = new JSplitPane();
    private final JLabel statusLabel = new JLabel("");
    private final JProgressBar progressBar = new JProgressBar(0,10000);
    private JButton saveButton = new JButton("Update changes");
    private CheckTreeManager checkTreeManager;
    private final ViewData viewData;

    public View() {
        super("Source header v0.1");
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (View.this.controller.isWorking()) {
                    int result = JOptionPane.showConfirmDialog(
                                        View.this,
                                        "There is operation that is not complete. Are you sure to close application?",
                                        "Operation is not complete.",
                                        JOptionPane.YES_NO_CANCEL_OPTION);
                    if (result != JOptionPane.YES_OPTION) {
                        return;
                    }
                }
                dispose();
            }        
        });
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {}
        this.setLayout(new BorderLayout(10, 3));

        this.viewData = new ViewData();
        this.controller = new ControllerImpl(this.viewData, this, this.preferences);
        this.viewData.setObserver(this);

        this.setSize(this.preferences.getWindowWidth(),
                this.preferences.getWindowHeight());
        this.setLocation(this.preferences.getWindowX(), 
                this.preferences.getWindowY());

        this.initCentralSplitPane();
        this.initMenu();
        
        this.setVisible(true);
    }

    private void initCentralSplitPane() {
        JSplitPane centralSplitPane = this.splitPane;
        centralSplitPane.setDividerSize(10);        
        centralSplitPane.setLeftComponent(this.initLeftPanel());
        centralSplitPane.setRightComponent(this.initRightPanel());
        this.add(centralSplitPane, BorderLayout.CENTER);
        centralSplitPane.setDividerLocation(this.preferences.getSplitPaneDividerPosition());
    }

    private JPanel initLeftPanel() {
        JPanel leftPanel = new JPanel(new BorderLayout(5, 3));

        this.filesTree.setModel(new DefaultTreeModel(new EmptyTreeRootAdapter()));
        this.filesTree.setBorder(BorderFactory.createEmptyBorder(10, 3, 3, 3));
        this.filesTree.setCellRenderer(new FilesTreeCellRenderer());
        this.filesTree.addTreeSelectionListener(new FilesTreeSelectionListener());
        this.checkTreeManager = new CheckTreeManager(this.filesTree);

        JScrollPane scrollPane = new JScrollPane(this.filesTree,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        leftPanel.add(scrollPane, BorderLayout.CENTER);

        return leftPanel;
    }

    private JPanel initRightPanel() {
        JPanel rightPanel = new JPanel(new BorderLayout(2, 3));
        rightPanel.add(this.initTopRightPanel(), BorderLayout.NORTH);
        rightPanel.add(this.initRightContentPanel(), BorderLayout.CENTER);
        rightPanel.add(this.initRightBottomPanel(), BorderLayout.SOUTH);
        return rightPanel;
    }

    private JPanel initTopRightPanel() {
        JButton updateButton = new JButton("Insert header");
        updateButton.addActionListener(new UpdateHeaderListener());
        JButton appendButton = new JButton("Append header");
        appendButton.addActionListener(new AppendHeaderListener());
        JButton prependButton = new JButton("Prepend header");
        prependButton.addActionListener(new PrependHeaderListener());

        JPanel headerButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        headerButtonsPanel.setBorder(BorderFactory.createTitledBorder("Action on selected files"));
        headerButtonsPanel.add(updateButton);
        headerButtonsPanel.add(appendButton);
        headerButtonsPanel.add(prependButton);

        JButton openFolderButton = new JButton("Open folder");
        openFolderButton.addActionListener(new OpenFolderListener());

        JPanel filesystemPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        filesystemPanel.setBorder(BorderFactory.createTitledBorder("Files tree"));
        filesystemPanel.add(openFolderButton);

        saveButton = new JButton("Update changes");
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                View.this.controller.updateHeaderButtonClicked();
            }
        });
        saveButton.setEnabled(false);

        JPanel headerPanel = new JPanel();
        headerPanel.setBorder(BorderFactory.createTitledBorder("Existing header"));
        headerPanel.add(saveButton);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 1, 1));
        buttonsPanel.add(filesystemPanel);
        buttonsPanel.add(headerPanel);
        buttonsPanel.add(headerButtonsPanel);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(buttonsPanel, BorderLayout.CENTER);
        panel.add(new JSeparator(JSeparator.HORIZONTAL), BorderLayout.SOUTH);
        return panel;
    }

    private JComponent initRightContentPanel() {
        String text = this.viewData.getNewHeader().getContent();
        this.newHeaderTextArea.setText(text);

        JScrollPane newHeaderScrollPane = new JScrollPane(
                this.newHeaderTextArea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.newHeaderTextArea.setCaretPosition(0);

        JScrollPane currentHeaderScrollPane = new JScrollPane(
                this.currentHeaderTextArea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        final JTabbedPane pane = new JTabbedPane();
        pane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                View.this.viewData.setNewHeaderVisible(pane.getSelectedIndex() == 1);
                View.this.saveButton.setEnabled(!View.this.viewData.isNewHeaderVisible());
            }
        });
        pane.add("Selected header", currentHeaderScrollPane);
        pane.add("New header", newHeaderScrollPane);
        pane.setSelectedIndex(1);
        return pane;
    }

    private JComponent initRightBottomPanel() {
        JPanel flow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        flow.add(this.progressBar);
        flow.add(this.statusLabel);
        this.statusLabel.setVerticalAlignment(SwingConstants.CENTER);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JSeparator(JSeparator.HORIZONTAL), BorderLayout.NORTH);
        panel.add(flow, BorderLayout.CENTER);
        return panel;
    }

    private void initMenu() {
        JMenuItem open = new JMenuItem("Open folder");
        open.addActionListener(new OpenFolderListener());
        JMenuItem end = new JMenuItem("Exit");
        end.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                View.this.dispose();
            }
        });
        JMenuItem prefs = new JMenuItem("Preferences");
        prefs.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JDialog dialog = new PreferencesDialog(View.this, View.this.preferences);
            }
        });

        JMenu fileMenu = new JMenu("File");
        fileMenu.add(open);
        fileMenu.add(prefs);
        fileMenu.add(end);

        JMenuItem about = new JMenuItem("About");
        about.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JDialog dialog = new AboutDialog(View.this);
            }
        });
        JMenuItem help = new JMenuItem("Help");
        help.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!Desktop.isDesktopSupported() ||
                    !Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    View.this.warningDialog(
                            "Your desktor does not support browse url. " +
                            "Manual can be found at http://code.google.com/p/source-header/wiki/Manual.",
                            "Browse not supported");
                }
                else {
                    try {
                            Desktop.getDesktop().browse(new URI("http://code.google.com/p/source-header/wiki/Manual"));
                    } catch (IOException ex) {}
                    catch (URISyntaxException ex) {}
                }
            }
        });

        JMenu helpMenu = new JMenu("Help");
        helpMenu.add(about);
        helpMenu.add(help);

        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
    }

    // ------------------------------------------------------------ //
    // ----------------- IView implementation --------------------- //

    public Path getPathFromUser() {
        JFileChooser dialog = new JFileChooser();
        dialog.setMultiSelectionEnabled(false);
        dialog.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (dialog.showOpenDialog(View.this) == JFileChooser.APPROVE_OPTION) {
            return new Path(dialog.getSelectedFile());
        }

        return null;
    }

    public void warningDialog(final String content, final String title) {
        this.runSafely(new Runnable() {
            public void run() {
                JOptionPane.showMessageDialog(View.this,
                        content, title, JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    public boolean questionDialog(String content, String title) {
        int result = JOptionPane.showConfirmDialog(
                            this, content, title,
                            JOptionPane.YES_NO_CANCEL_OPTION);
        return result == JOptionPane.YES_OPTION;
    }

    public void setStatusText(final String text) {
        this.runSafely(new Runnable() {
            public void run() {
                View.this.statusLabel.setText(text);
            }
        });
    }

    public void updateTree() {
        this.runSafely(new Runnable() {
            public void run() {
                View.this.filesTree.repaint();
            }
        });
    }

    public ProgressReportConsumer getProgressReportConsumer() {
        return new ProgressReportConsumer() {
            public void progress() {
                runSafely(new Runnable() {
                    public void run() {
                        if (View.this.progressBar.getValue() == View.this.progressBar.getMaximum()) {
                            View.this.progressBar.setValue(0);
                        }
                        else {
                            View.this.progressBar.setValue(View.this.progressBar.getValue()+1);
                        }
                    }
                });
            }
            public void done() {
                runSafely(new Runnable() {
                    public void run() {
                        View.this.progressBar.setValue(0);
                    }
                });
            }
        };
    }

    public String getCurrentHeaderContent() {
        return this.currentHeaderTextArea.getText();
    }

    public String getNewHeaderContent() {
        return this.newHeaderTextArea.getText();
    }

    public java.util.List<File> getSelectedFiles() {
        TreePath[] paths = this.checkTreeManager.getSelectionModel().getSelectionPaths();
        java.util.List<File> result = new java.util.ArrayList<File>();

        for (TreePath path : paths) {
            Object obj = path.getLastPathComponent();
            if (obj instanceof DefaultMutableTreeNode) {
                if (((DefaultMutableTreeNode)obj).getUserObject() instanceof File) {
                    result.add((File)((DefaultMutableTreeNode)obj).getUserObject());
                }
            }
        }
        return result;
    }

    // ------------------------------------------------------------ //
    // ----------------- Observation of ViewData ------------------ //

    public void currentHeaderChanged() {
        this.runSafely(new Runnable() {
            public void run() {
                View.this.currentHeaderTextArea.setText(
                        View.this.viewData.getCurrentHeader().getContent());
            }
        });
    }

    public void filesTreeChanged() {
        this.runSafely(new Runnable() {
            public void run() {
                View.this.filesTree.setModel(
                    new DefaultTreeModel(
                        new TreeRootAdapter(View.this.viewData.getFilesTree())));
                View.this.filesTree.setCellRenderer(new FilesTreeCellRenderer());
                View.this.checkTreeManager = new CheckTreeManager(filesTree);
            }
        });
    }

    public void newHeaderChanged() {
        this.runSafely(new Runnable() {
            public void run() {
                View.this.newHeaderTextArea.setText(
                        View.this.viewData.getNewHeader().getContent());
            }
        });
    }

    private void runSafely(Runnable work) {
        if (SwingUtilities.isEventDispatchThread()) {
            work.run();
        }
        else {
            SwingUtilities.invokeLater(work);
        }
    }

    // ------------------------------------------------------------ //
    // ------------------------ Listeners ------------------------- //

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

    private class OpenFolderListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            View.this.controller.chooseRootButtonClicked();
        }
    }

    private class UpdateHeaderListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            View.this.controller.uploadHeaderButtonClicked();
        }
    }

    private class AppendHeaderListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            View.this.controller.appendHeaderButtonClicked();
        }
    }

    private class PrependHeaderListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            View.this.controller.prependHeaderButtonClicked();
        }
    }

    @Override
    public void dispose() {
        this.preferences.updateWindowPreferences(this);
        this.preferences.setSplitPaneDividerPosition(this.splitPane.getDividerLocation());
        this.controller.stopWorking();
        super.dispose();
    }
}
