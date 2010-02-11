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
 *
 * @author steve
 */
public interface PreferencesPersister {
    public Map<String, Block> getAlternatingParts() throws Exception;
    public void saveAlternatingParts(Map<String, Block> parts) throws Exception;
}
