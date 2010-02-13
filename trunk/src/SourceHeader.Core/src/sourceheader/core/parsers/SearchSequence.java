package sourceheader.core.parsers;

/**
 * This class consumes characters from input and indicates whether
 * the searcher character sequence occurred.
 *
 * Implemented using Knuth–Morris–Pratt algorithm.
 */
public class SearchSequence {

    /**
     * Additional information about sequence for consumer.
     */
    private Object data;
    private String sequence;
    private int currentIndex = -1;
    private int[] backEdges;

    public SearchSequence(Object data, String sequence) {
        super();
        this.data = data;
        this.sequence = sequence;
        this.backEdges = new int[sequence.length()];

        // create automat
        if (this.sequence.length() > 0) {
            this.backEdges[0] = -1;
            for (int i = 1; i < sequence.length(); i++) {
                this.nextState(this.sequence.charAt(i));
                this.backEdges[i] = this.currentIndex;
            }
            this.reset();
        }
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

    /**
     * Resets search.
     */
    public void reset() {
        this.currentIndex = -1;
    }

    /**
     * @return True if the sequence was found.
     * That means if there was it's occurrence in past and reset was not called.
     */
    public boolean found() {
        boolean result = this.currentIndex == this.sequence.length()-1;
        return result;
    }

    /**
     * @return The length of current prefix of search sequence that has been found.
     */
    public int currentLenght() {
        return this.currentIndex+1;
    }

    /**
     * Moves to next state in automat.
     * @param c
     */
    private void nextState(char c) {
        while(this.currentIndex != -1 &&
                this.sequence.charAt(this.currentIndex+1) != c) {
            this.currentIndex = this.backEdges[this.currentIndex];
        }
        if (c == this.sequence.charAt(this.currentIndex+1)) {
            this.currentIndex++;
        }
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

        this.nextState(c);
        return this.found();
    }
}
