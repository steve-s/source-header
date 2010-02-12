/*
 * file PreferencesPersister.java
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

import java.util.Map;
import sourceheader.core.parsers.Block;

/**
 * Interface for class that persists configuration of alternating parts.
 * This configuration is slightly complex so custom storage system
 * was used instead for java.util.pref.
 *
 * @author steve
 */
public interface PreferencesPersister {
    /**
     * Loads alternating parts.
     * @return Alternating parts map.
     * @throws Exception
     */
    public Map<String, Block> getAlternatingParts() throws Exception;
    /**
     * Saves alternating parts to storage.
     * @param parts
     * @throws Exception
     */
    public void saveAlternatingParts(Map<String, Block> parts) throws Exception;
}
