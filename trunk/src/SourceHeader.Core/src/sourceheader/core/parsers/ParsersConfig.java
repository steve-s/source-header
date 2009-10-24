/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sourceheader.core.parsers;

import java.util.*;

/**
 *
 * @author steve
 */
public class ParsersConfig {

    private char specialCharacter;

    private Map<String, Block> alternatingParts;

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

    public static ParsersConfig getDefault() {
        return new ParsersConfig('%');
    }
}
