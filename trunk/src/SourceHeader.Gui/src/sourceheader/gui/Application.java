/*
 * SourceHeaderApplication.java
 */

package sourceheader.gui;

import org.jdesktop.application.SingleFrameApplication;

/**
 * The main class of the application.
 */
public class Application extends SingleFrameApplication {

    /**
     * At startup create and show the main frame of the application.
     */
    @Override protected void startup() {
        show(new View(this));
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override protected void configureWindow(java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of SourceHeaderApplication
     */
    public static org.jdesktop.application.Application getApplication() {
        return org.jdesktop.application.Application
                .getInstance(sourceheader.gui.Application.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        launch(sourceheader.gui.Application.class, args);
    }
}
