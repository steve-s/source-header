/*
 * file ApplicationPreferences.java
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

package sourceheader.gui.util.preferences;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.*;
import javax.swing.JFrame;
import sourceheader.core.parsers.Block;
import sourceheader.gui.Application;

/**
 * Helper class for saving and retrieving application cofiguration.
 *
 * It uses java.util.prefs for basic preferences and configuration
 * file (optional) for alternating parts configuration.
 * Path to this configuration file is stored again using java.util.prefs.
 *
 * All preferences are provided with meaningful default values.
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
        return this.prefs.getInt("window_width", 882);
    }

    public int getWindowHeight() {
        return this.prefs.getInt("window_height", 485);
    }

    public int getSplitPaneDividerPosition() {
        return this.prefs.getInt("split_pane_pos", 207);
    }

    public void setSplitPaneDividerPosition(int value) {
        this.prefs.putInt("split_pane_pos", value);
    }

    public char getSpecialCharacter() {
        return (char)this.prefs.getInt("spec_char", '%');
    }

    public void setSpecialCharacter(char ch) {
        this.prefs.putInt("spec_char", (int)ch);
    }

    public String getAlternatingPartsConfigFile() {
        return this.prefs.get("config_file", "");
    }

    public void setAlternatingPartsConfigFile(String filename) {
        this.prefs.put("config_file", filename);
    }

    public Map<String, Block> getAlternatingParts() {
        if (this.getAlternatingPartsConfigFile().isEmpty()) {
            return AlternatingPartsHelper.getDefaultAlternatingParts();
        }

        FilePersister persister = new FilePersister(this.getAlternatingPartsConfigFile());
        try {
            return persister.getAlternatingParts();
        }
        catch (Exception ex) {
            return new HashMap<String, Block>();
        }
    }

    /**
     * Updates alle window related pref with values from given window.
     * @param frame
     */
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
