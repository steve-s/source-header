/*
 * This file is part of SourceHeader project.
 * licence: FreeBSD licence.
 *
 * @category Gui
 *
 * file: IView.java
 */

package sourceheader.gui;

import sourceheader.core.Path;
import sourceheader.core.ProgressReportConsumer;

/**
 *
 * @author steve
 */
public interface IView {
    Path getPathFromUser();
    void warningDialog(String content, String title);
    boolean questionDialog(String content, String title);

    String getCurrentHeaderContent();
    String getNewHeaderContent();

    void setStatusText(String text);
    ProgressReportConsumer getProgressReportConsumer();
}
