/*
 * file IView.java
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

import java.util.List;
import sourceheader.core.File;
import sourceheader.core.Path;
import sourceheader.core.ProgressReportConsumer;

/**
 * Interface of view, trought this interface the controller access the view.
 *
 * @author steve
 */
public interface IView {
    /**
     * Shows open folder dialog and lets user choose one path.
     * @return The chosen path.
     */
    Path getPathFromUser();
    /**
     * Shows warning dialog. Thread safe.
     * @param content
     * @param title
     */
    void warningDialog(String content, String title);
    /**
     * Shows question dialog.
     * Not thread safe, can be called only from swing thread.
     * @param content
     * @param title
     * @return True if user has chosen 'yes'.
     */
    boolean questionDialog(String content, String title);
    /**
     * Repaints files tree.
     */
    void updateTree();

    /**
     * @return Content of current header text area.
     */
    String getCurrentHeaderContent();
    /**
     * @return Content of new header text area.
     */
    String getNewHeaderContent();
    /**
     * @return List of selected files.
     */
    List<File> getSelectedFiles();

    /**
     * Sets the status bar text.
     * @param text
     */
    void setStatusText(String text);
    /**
     * @return Returns consumer of progress that should report progress in gui.
     */
    ProgressReportConsumer getProgressReportConsumer();
}
