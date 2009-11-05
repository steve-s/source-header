/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sourceheader.gui;

import sourceheader.core.Path;

/**
 *
 * @author steve
 */
public interface IView {
    Path getPathFromUser();
    void warningDialog(String content, String title);

    void indeterminateProgress();
    void determinateProgress();
}
