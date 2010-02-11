/*
 * This file is part of SourceHeader project.
 * licence: FreeBSD licence.
 *
 * @category Utils
 *
 * file: ApplicationPreferences.java
 */

package sourceheader.gui.util;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.swing.JFrame;
import sourceheader.gui.Application;

/**
 * Helper class for saving and retrieving application cofiguration.
 *
 * @author steve
 */
public class ApplicationPreferences {

    Preferences prefs;

    public ApplicationPreferences() {
        this.prefs = Preferences.userNodeForPackage(Application.class);
    }

    public int getWindowX() {
        return this.prefs.getInt("window_x", 250);
    }

    public int getWindowY() {
        return this.prefs.getInt("window_y", 250);
    }

    public int getWindowWidth() {
        return this.prefs.getInt("window_width", 720);
    }

    public int getWindowHeight() {
        return this.prefs.getInt("window_height", 465);
    }

    public int getSplitPaneDividerPosition() {
        return this.prefs.getInt("split_pane_pos", 207);
    }

    public void setSplitPaneDividerPosition(int value) {
        this.prefs.putInt("split_pane_pos", value);
    }

    public void updateWindowPreferences(JFrame frame) {
        this.prefs.putInt("window_x", frame.getX());
        this.prefs.putInt("window_y", frame.getY());
        this.prefs.putInt("window_width", frame.getWidth());
        this.prefs.putInt("window_height", frame.getHeight());
        try {
            this.prefs.flush();
        } catch (BackingStoreException ex) {}
    }
}
