/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sourceheader.gui.util.preferences;

import java.util.Map;
import sourceheader.core.parsers.Block;

/**
 *
 * @author steve
 */
public interface PreferencesPersister {
    public Map<String, Block> getAlternatingParts() throws Exception;
    public void saveAlternatingParts(Map<String, Block> parts) throws Exception;
}
