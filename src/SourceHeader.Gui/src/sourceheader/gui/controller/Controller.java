/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sourceheader.gui.controller;

import sourceheader.core.FileHeader;

/**
 *
 * @author steve
 */
public interface Controller {
    void chooseRootButtonClicked();
    void currentHeaderSelectChanged(FileHeader header);
}
