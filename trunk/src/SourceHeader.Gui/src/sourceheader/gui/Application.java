/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sourceheader.gui;

import javax.swing.*;

/**
 * The main class of the application.
 */
public class Application {
    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new View();
            }
        });
    }
}
