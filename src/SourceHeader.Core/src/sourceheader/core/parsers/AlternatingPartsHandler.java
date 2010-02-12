/*
 * file AlternatingPartsHandler.java
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
 * During reading the input of header, this class takes care of
 * handling alternating parts.
 */
class AlternatingPartsHandler {
    Map<String, List<String>> parts;
    List<SearchSequence> searchSequences;
    Map<Block, Integer> indexes = new HashMap<Block, Integer>();
    Block currentPart = null;
    SearchSequence currentPartSearchSequence = null;
    StringBuilder currentPartContent = new StringBuilder();
    ParsersConfig config;

    public AlternatingPartsHandler(Map<String, List<String>> parts, ParsersConfig config) {
        super();
        this.parts = parts;
        this.config = config;
        this.setUpAlternatingPartsSearchSequences();        
    }

    private void setUpAlternatingPartsSearchSequences() {
        this.searchSequences = new Vector<SearchSequence>();
        for (String name : this.config.getAlternatingParts().keySet()) {
            Block block = this.config.getAlternatingParts().get(name);
            this.searchSequences.add(new SearchSequence(block, block.getStartSequence()));
        }
    }

    /**
     * Consumes next character from input.
     * @param c Character.
     * @return String that should be appended to header content.
     * That will be an id of alternating part when the end of some alternating part
     * is reached and empty string in other cases.
     */
    public String next(char c) {
        if (this.currentPart != null) {
            return this.nextInCurrentPart(c);
        }
        this.nextInSearchForPart(c);
        return "";
    }

    /**
     * Searches for begin of some alternating part.
     * @param c Input.
     */
    private void nextInSearchForPart(char c) {
        for (SearchSequence sequence : this.searchSequences) {
            if (sequence.next(c)) {
                sequence.reset();
                this.currentPart = (Block) sequence.getData();
                this.currentPartSearchSequence = new SearchSequence(sequence.getData(), this.currentPart.getEndSequence());
                Integer i = this.indexes.get((Block) sequence.getData());
                if (i == null) {
                    i = new Integer(-1);
                }
                i++;
                this.indexes.put((Block) sequence.getData(), i);
            }
        }
    }

    /**
     * Searcher for end of current alternating part.
     * @param c Input.
     * @return String that should be appended to header content.
     * That will be id of alternating part and empty string in other cases.
     */
    private String nextInCurrentPart(char c) {
        StringBuilder result = new StringBuilder();
        if (this.currentPartSearchSequence.next(c)) {
            this.appendIdOfCurrentPart(result);
            this.putCurrentPartContentIntoContentsMap();
            this.currentPartContent.setLength(0);
            this.currentPart = null;
            this.currentPartSearchSequence = null;
        } else {
            this.currentPartContent.append(c);
        }
        return result.toString();
    }

    /**
     * Generated 'placeholder' id of current alternating part.
     * That is {specChar}part-name{specChar}part-index{specChar}
     * @param result Result is appended to given StringBuilder.
     */
    private void appendIdOfCurrentPart(StringBuilder result) {
        result.append(config.getSpecialCharacter());
        result.append(this.getCurrentPartName());
        result.append(config.getSpecialCharacter());
        result.append(this.indexes.get(this.currentPart).toString());
        result.append(config.getSpecialCharacter());
        result.append(this.currentPart.getEndSequence());
    }

    /**
     * Puts current alternating part data into alternating parts map.
     */
    private void putCurrentPartContentIntoContentsMap() {
        // put the content into list of contents
        if (!this.parts.containsKey(this.getCurrentPartName())) {
            this.parts.put(this.getCurrentPartName(), new ArrayList<String>());
        }
        //cut-off block end sequence:
        this.currentPartContent.setLength(this.currentPartContent.length() - this.currentPart.getEndSequence().length() + 1);
        this.parts.get(this.getCurrentPartName()).add(this.currentPartContent.toString());
    }

    private String getCurrentPartName() {
        return config.getAlternatingPartName(this.currentPart);
    }

    /**
     * @return Indicates wheter start of some alternating part was found
     * and so characters from input should considered as content of
     * some alternating part.
     */
    public boolean inAlternatingPart() {
        return this.currentPart != null;
    }
}