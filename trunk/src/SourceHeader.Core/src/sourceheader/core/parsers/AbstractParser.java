/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sourceheader.core.parsers;

import java.io.*;
import java.util.*;
import sourceheader.core.*;

/**
 *
 * @author steve
 */
public abstract class AbstractParser implements HeaderParser {

    protected abstract Iterable<Block> getCommentBlocks();
    public abstract String[] getExtensions();

    public AbstractParser() {
    }

    public FileHeader parse(Path path, FileHeaderFactory headerFactory) throws IOException {
        return this.parse(new FileReader(path), headerFactory);
    }

    public FileHeader parse(Reader reader, FileHeaderFactory headerFactory) throws IOException {

        // constant list of sequences of starts of blocks.
        final Vector<SearchSequence> allStarts = new Vector<SearchSequence>();
        for(Block block : this.getCommentBlocks()) {
            allStarts.add(new SearchSequence(
                    block,
                    block.getStartSequence()));
        }

        StringBuilder result = new StringBuilder(); // the new header content.
        boolean wasComment = false; // indicates whether first comment was found.
        char c = ' '; // current character read from file.

        while (reader.ready()) {

            // skip whitespaces
            boolean firstNewline = false;
            StringBuilder whitespace = new StringBuilder();
            if (wasComment) {
                //so this is at least second iteration
                //and there is something in c that was not parsed
                whitespace.append(c);
                // if it is not whitespace, than it will be cut off
                // after following loop, see comment "last char is not white".
            }
            while (reader.ready() && Character.isWhitespace(c)) {
                c = (char)reader.read();
                whitespace.append(c);
                if (wasComment && c == '\n' && firstNewline) {
                    return headerFactory.create(result.toString());
                }
                firstNewline = c == '\n';
            }
            whitespace.setLength(whitespace.length()-1); // last char is not white

            // check for comment start
            Vector<SearchSequence> starts = new Vector<SearchSequence>(allStarts);
            Block found = null;
            while (starts.size() > 0 && found == null && reader.ready()) {
                Vector<SearchSequence> toBeRemoved = new Vector<SearchSequence>();
                for(SearchSequence s : starts) {
                    if (s.next(c)) {
                        found = (Block)s.getData();
                        break;
                    }

                    if (s.currentLenght() == 0) {
                        toBeRemoved.add(s);
                    }
                }
                starts.removeAll(toBeRemoved);
                c = (char)reader.read();
            }

            if (found == null) {
                // no comment starts here, we are done.
                return headerFactory.create(result.toString());
            }

            // we have start of commet in found variable.
            result.append(whitespace);
            result.append(found.getStartSequence());

            // read characters till the end of comment
            SearchSequence search = new SearchSequence(null, found.getEndSequence());
            StringBuilder commentContent = new StringBuilder();
            while (reader.ready() && !search.found()) {
                search.next(c);
                commentContent.append(c);
                c = (char) reader.read();
            }

            result.append(commentContent);
            wasComment = true;
        }

        return headerFactory.create(result.toString());
    }

    private class SearchSequence {
        /**
         * Additional information about sequence for consumer.
         */
        private Object data;
        private String sequence;
        private StringBuilder current;

        public SearchSequence(Object data, String sequence) {
            this.data = data;
            this.sequence = sequence;
            this.current = new StringBuilder();
        }

        public Object getData() {
            return this.data;
        }

        private char nextCharacter() {
            int index = this.current.length();
            return this.sequence.charAt(index);
        }

        public boolean found() {
            return this.current.length() == this.sequence.length();
        }

        public int currentLenght() {
            return this.current.length();
        }

        public boolean next(char c) {
            if (this.found()) {
                return true;
            }

            if (c == this.nextCharacter()) {
                this.current.append(c);
                return this.found();
            }

            this.current.setLength(0);
            return false;
        }
    }
}
