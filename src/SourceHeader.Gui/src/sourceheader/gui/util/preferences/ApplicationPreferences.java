/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sourceheader.gui.util.preferences;

import java.util.*;
import java.util.prefs.*;
import javax.swing.JFrame;
import sourceheader.core.parsers.Block;
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
            return new HashMap<String, Block>();
        }

        FilePersister persister = new FilePersister(this.getAlternatingPartsConfigFile());
        try {
            return persister.getAlternatingParts();
        }
        catch (Exception ex) {
            return new HashMap<String, Block>();
        }
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
