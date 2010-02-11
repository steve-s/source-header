package sourceheader.core.parsers;

/**
 * This class consumes characters from input and indicates whether
 * the searcher character sequence occurred.
 */
class SearchSequence {

    /**
     * Additional information about sequence for consumer.
     */
    private Object data;
    private String sequence;
    private StringBuilder current;
    AbstractParser outer;

    public SearchSequence(Object data, String sequence, AbstractParser outer) {
        super();
        this.outer = outer;
        this.data = data;
        this.sequence = sequence;
        this.current = new StringBuilder();
    }

    /**
     * @return The searched sequence.
     */
    public String getSequence() {
        return this.sequence;
    }    

    /**
     * Data means user object associated with this instance,
     * this is read-only initialized in constructor.
     * @return
     */
    public Object getData() {
        return this.data;
    }

    private char nextCharacter() {
        int index = this.current.length();
        return this.sequence.charAt(index);
    }

    /**
     * Resets search.
     */
    public void reset() {
        this.current.setLength(0);
    }

    /**
     * @return True if the sequence was found.
     * That means if there was it's occurrence in past and reset was not called.
     */
    public boolean found() {
        return this.current.length() == this.sequence.length();
    }

    /**
     * @return The length of current prefix of search sequence that has been found.
     */
    public int currentLenght() {
        return this.current.length();
    }

    /**
     * Consumes next character from input.
     * @param c Input.
     * @return True if it was last characted of search sequence.
     */
    public boolean next(char c) {
        if (this.found()) {
            return true;
        }
        if (c == this.nextCharacter()) {
            this.current.append(c);
            return this.found();
        }
        this.current.setLength(0);
        // 'c' might be first character of sequence
        if (c == this.nextCharacter()) {
            this.current.append(c);
            return this.found();
        }
        return false;
    }
}
