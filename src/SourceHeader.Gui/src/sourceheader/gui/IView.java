/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
