/*
 * file ParsersConfig.java
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

package sourceheader.core.parsers;

import java.util.*;

/**
 * Holds information about parser configuration that
 * is specialCharacter used for alternating parts placeholders
 * and list of supported alternating parts.
 *
 * @author steve
 */
public class ParsersConfig {
    private char specialCharacter;
    private Map<String, Block> alternatingParts;

    /**
     * Creates configuration.
     * @param specialCharacter
     * @param alternatingParts
     */
    public ParsersConfig(char specialCharacter, Map<String, Block> alternatingParts) {
        this.specialCharacter = specialCharacter;
        this.alternatingParts = alternatingParts;
    }

    public ParsersConfig(char specialCharacter) {
        this.specialCharacter = specialCharacter;
        this.alternatingParts = new HashMap<String, Block>();
    }

    public char getSpecialCharacter() {
        return this.specialCharacter;
    }

    public Map<String, Block> getAlternatingParts() {
        return this.alternatingParts;
    }

    public Block getAlternatingPart(String name) {
        return this.alternatingParts.get(name);
    }

    public String getAlternatingPartName(Block block) {
        for(String key : this.alternatingParts.keySet()) {
            if (this.alternatingParts.get(key).equals(block)) {
                return key;
            }
        }
        
        return null;
    }

    /**
     * Creates simple, default configuration.
     * @return Config instance.
     */
    public static ParsersConfig getDefault() {
        return new ParsersConfig('%');
    }
}
