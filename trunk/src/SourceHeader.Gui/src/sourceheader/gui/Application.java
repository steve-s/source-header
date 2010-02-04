/*
 * SourceHeaderApplication.java
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
