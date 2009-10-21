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

    private static List<Block> alternatingParts =
            new Vector<Block>();

    public static void addAlternatingBlock(Block block) {
        alternatingParts.add(block);
    }

    public static void removeAlternatingBlock(Block block) {
        alternatingParts.remove(block);
    }
}
