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
    Path getPathFromUser();
    void warningDialog(String content, String title);
    boolean questionDialog(String content, String title);
    void updateTree();

    String getCurrentHeaderContent();
    String getNewHeaderContent();
    List<File> getSelectedFiles();

    void setStatusText(String text);
    ProgressReportConsumer getProgressReportConsumer();
}
