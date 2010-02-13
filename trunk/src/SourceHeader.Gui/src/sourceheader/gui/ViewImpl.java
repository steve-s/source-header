/*
 * file ViewImpl.java
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
import java.util.Arrays;
import java.util.Collections;
import java.util.Stack;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import sourceheader.core.*;
import sourceheader.gui.controller.*;
import sourceheader.gui.dialogs.*;

/**
 * Main window class.
 *
 * @author steve
 */
public class ViewImpl extends JFrame
        implements View, ViewData.ViewDataObserver {

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

    /**
     * Initializes gui, sets itself visible.
     */
    public ViewImpl() {
        super("Source header v0.1");
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new ClosingWindowAdapter());
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

    /**
     * Initializes central split pane that divides files tree and rest of gui.
     */
    private void initCentralSplitPane() {
        JSplitPane centralSplitPane = this.splitPane;
        centralSplitPane.setDividerSize(10);        
        centralSplitPane.setLeftComponent(this.initLeftPanel());
        centralSplitPane.setRightComponent(this.initRightPanel());
        this.add(centralSplitPane, BorderLayout.CENTER);
        centralSplitPane.setDividerLocation(this.preferences.getSplitPaneDividerPosition());
    }

    /**
     * Initializes left panel that consists only of JTree.
     * @return The panel instance.
     */
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

    /**
     * Initializes right panel of split pane.
     * This panel consists of top right, right content and right bottom panels.
     * @return The panel instance.
     */
    private JPanel initRightPanel() {
        JPanel rightPanel = new JPanel(new BorderLayout(2, 3));
        rightPanel.add(this.initTopRightPanel(), BorderLayout.NORTH);
        rightPanel.add(this.initRightContentPanel(), BorderLayout.CENTER);
        rightPanel.add(this.initRightBottomPanel(), BorderLayout.SOUTH);
        return rightPanel;
    }

    /**
     * Initializes top right panel. This panel is 'toolbox' with buttons.
     * @return The panel instance.
     */
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
                ViewImpl.this.controller.updateHeaderButtonClicked();
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

    /**
     * Initializes right content panel.
     * This panel is JTabbedPane with new header text area and current
     * header text area.
     * @return The panel instance.
     */
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
                ViewImpl.this.viewData.setNewHeaderVisible(pane.getSelectedIndex() == 1);
                ViewImpl.this.saveButton.setEnabled(!ViewImpl.this.viewData.isNewHeaderVisible());
            }
        });
        pane.add("Selected header", currentHeaderScrollPane);
        pane.add("New header", newHeaderScrollPane);
        pane.setSelectedIndex(1);
        return pane;
    }

    /**
     * Initializes right bottom panel that is something like status bar.
     * @return The panel instance.
     */
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

    /**
     * Initializes the window menu and it's actions.
     */
    private void initMenu() {
        JMenuItem open = new JMenuItem("Open folder");
        open.addActionListener(new OpenFolderListener());
        JMenuItem end = new JMenuItem("Exit");
        end.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ViewImpl.this.dispose();
            }
        });
        JMenuItem prefs = new JMenuItem("Preferences");
        prefs.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JDialog dialog = new PreferencesDialog(ViewImpl.this, ViewImpl.this.preferences);
            }
        });

        JMenu fileMenu = new JMenu("File");
        fileMenu.add(open);
        fileMenu.add(prefs);
        fileMenu.add(end);

        JMenuItem about = new JMenuItem("About");
        about.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JDialog dialog = new AboutDialog(ViewImpl.this);
            }
        });
        JMenuItem help = new JMenuItem("Help");
        help.addActionListener(new HelpActionListener());

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

    /**
     * @iheritDoc
     */
    public Path getPathFromUser() {
        JFileChooser dialog = new JFileChooser();
        dialog.setMultiSelectionEnabled(false);
        dialog.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (dialog.showOpenDialog(ViewImpl.this) == JFileChooser.APPROVE_OPTION) {
            return new Path(dialog.getSelectedFile());
        }

        return null;
    }

    /**
     * @iheritDoc
     */
    public void warningDialog(final String content, final String title) {
        this.runSafely(new Runnable() {
            public void run() {
                JOptionPane.showMessageDialog(ViewImpl.this,
                        content, title, JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    /**
     * @iheritDoc
     */
    public boolean questionDialog(String content, String title) {
        int result = JOptionPane.showConfirmDialog(
                            this, content, title,
                            JOptionPane.YES_NO_CANCEL_OPTION);
        return result == JOptionPane.YES_OPTION;
    }

    /**
     * @iheritDoc
     */
    public void setStatusText(final String text) {
        this.runSafely(new Runnable() {
            public void run() {
                ViewImpl.this.statusLabel.setText(text);
            }
        });
    }

    /**
     * @iheritDoc
     */
    public void updateTree() {
        this.runSafely(new Runnable() {
            public void run() {
                ViewImpl.this.filesTree.repaint();
            }
        });
    }

    /**
     * @iheritDoc
     */
    public ProgressReportConsumer getProgressReportConsumer() {
        return new ProgressReportConsumer() {
            public void progress() {
                runSafely(new Runnable() {
                    public void run() {
                        if (ViewImpl.this.progressBar.getValue() == ViewImpl.this.progressBar.getMaximum()) {
                            ViewImpl.this.progressBar.setValue(0);
                        }
                        else {
                            ViewImpl.this.progressBar.setValue(ViewImpl.this.progressBar.getValue()+1);
                        }
                    }
                });
            }
            public void done() {
                runSafely(new Runnable() {
                    public void run() {
                        ViewImpl.this.progressBar.setValue(0);
                    }
                });
            }
        };
    }

    /**
     * @iheritDoc
     */
    public String getCurrentHeaderContent() {
        return this.currentHeaderTextArea.getText();
    }

    /**
     * @iheritDoc
     */
    public String getNewHeaderContent() {
        return this.newHeaderTextArea.getText();
    }

    /**
     * @iheritDoc
     */
    public java.util.List<File> getSelectedFiles() {
        TreePath[] paths = this.checkTreeManager.getSelectionModel().getSelectionPaths();
        Stack stack = new Stack();
        stack.addAll(Arrays.asList(paths));
        java.util.List<File> result = new java.util.ArrayList<File>();

        while (!stack.isEmpty()) {
            Object current = stack.pop();
            if (current instanceof TreePath) {
                stack.push(((TreePath)current).getLastPathComponent());
            }
            else if (current instanceof DefaultMutableTreeNode) {
                stack.push(((DefaultMutableTreeNode)current).getUserObject());
            }
            else if (current instanceof File) {
                result.add((File)current);
            }
            else if (current instanceof Folder) {
                stack.addAll(((Folder)current).getFolders());
                stack.addAll(((Folder)current).getFiles());
            }
        }
        return result;
    }

    // ------------------------------------------------------------ //
    // ----------------- Observation of ViewData ------------------ //

    /**
     * When the {@link ViewData#getCurrentHeader() current header} is changed,
     * the content of current header text area should be changed to it's new value.
     */
    public void currentHeaderChanged() {
        this.runSafely(new Runnable() {
            public void run() {
                ViewImpl.this.currentHeaderTextArea.setText(
                        ViewImpl.this.viewData.getCurrentHeader().getContent());
            }
        });
    }

    /**
     * When {@link ViewData#getFilesTree() files tree instance} is changed,
     * than JTree model should be updated.
     */
    public void filesTreeChanged() {
        this.runSafely(new Runnable() {
            public void run() {
                ViewImpl.this.filesTree.setModel(
                    new DefaultTreeModel(
                        new TreeRootAdapter(ViewImpl.this.viewData.getFilesTree())));
                ViewImpl.this.filesTree.setCellRenderer(new FilesTreeCellRenderer());
                ViewImpl.this.checkTreeManager = new CheckTreeManager(filesTree);
            }
        });
    }

    /**
     * When the {@link ViewData#getNewHeader() new header} is changed,
     * the content of new header text area should be changed to it's new value.
     */
    public void newHeaderChanged() {
        this.runSafely(new Runnable() {
            public void run() {
                ViewImpl.this.newHeaderTextArea.setText(
                        ViewImpl.this.viewData.getNewHeader().getContent());
            }
        });
    }

    /**
     * Helper method checks wheter invokeLater is required and runs given work.
     * @param work
     */
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

    /**
     * Listens to simple tree selection and delegates action to 
     * {@link Controller controller}.
     */
    private class FilesTreeSelectionListener implements TreeSelectionListener {
        public void valueChanged(TreeSelectionEvent e) {
            Object obj = e.getPath().getLastPathComponent();
            if (obj instanceof DefaultMutableTreeNode) {
                if (((DefaultMutableTreeNode) obj).getUserObject() instanceof File) {
                    File file = (File) ((DefaultMutableTreeNode)obj).getUserObject();
                    ViewImpl.this.controller.currentHeaderSelectChanged(file.getHeader());
                }
            }
        }
    }

    /**
     * Listener delegates action to {@link Controller controller}.
     */
    private class OpenFolderListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            ViewImpl.this.controller.chooseRootButtonClicked();
        }
    }
    
    /**
     * Listener delegates action to {@link Controller controller}.
     */
    private class UpdateHeaderListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            ViewImpl.this.controller.uploadHeaderButtonClicked();
        }
    }

    /**
     * Listener delegates action to {@link Controller controller}.
     */
    private class AppendHeaderListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            ViewImpl.this.controller.appendHeaderButtonClicked();
        }
    }

    /**
     * Listener delegates action to {@link Controller controller}.
     */
    private class PrependHeaderListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            ViewImpl.this.controller.prependHeaderButtonClicked();
        }
    }

    /**
     * When disposing, preferences should be saved.
     */
    @Override
    public void dispose() {
        this.preferences.updateWindowPreferences(this);
        this.preferences.setSplitPaneDividerPosition(this.splitPane.getDividerLocation());
        this.controller.stopWorking();
        super.dispose();
    }

    /**
     * If user tries to close window when some {@link Controller#isWorking() work is not done},
     * he should be warned.
     */
    private class ClosingWindowAdapter extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            if (ViewImpl.this.controller.isWorking()) {
                int result = JOptionPane.showConfirmDialog(ViewImpl.this, "There is operation that is not complete. Are you sure to close application?", "Operation is not complete.", JOptionPane.YES_NO_CANCEL_OPTION);
                if (result != JOptionPane.YES_OPTION) {
                    return;
                }
            }
            dispose();
        }
    }

    /**
     * Help action ones browser with manual.
     */
    private class HelpActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (!Desktop.isDesktopSupported() || !Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                ViewImpl.this.warningDialog("Your desktor does not support browse url. " +
                        "Manual can be found at http://code.google.com/p/source-header/wiki/Manual.",
                        "Browse not supported");
            } else {
                try {
                    Desktop.getDesktop().browse(
                            new URI("http://code.google.com/p/source-header/wiki/Manual"));
                } catch (IOException ex) {}
                catch (URISyntaxException ex) {}
            }
        }
    }
}
