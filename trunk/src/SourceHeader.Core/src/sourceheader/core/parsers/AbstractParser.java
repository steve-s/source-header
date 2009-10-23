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

    public FileHeader parse(Path path, FileHeaderFactory headerFactory) 
            throws IOException, SyntaxErrorException {
        try {
            return this.parse(new FileReader(path), headerFactory);
        }
        catch(SyntaxErrorException ex) {
            throw new SyntaxErrorException(path.getAbsolutePath());
        }
    }

    public FileHeader parse(Reader reader, FileHeaderFactory headerFactory)
            throws IOException, SyntaxErrorException {
        StringBuilder result = new StringBuilder(); // the new header content.
        boolean wasComment = false; // indicates whether first comment was found.
        char c = ' '; // current character read from file.

        while (reader.ready()) {

            StringBuilder whitespace = new StringBuilder();
            c = this.skipWitespace(reader, c, whitespace, wasComment, !wasComment);

            if (c == ' ') {
                return headerFactory.create(result.toString());
            }

            // check for comment start
            CharAndBlock readResult = this.readTillStartOfComment(reader, c);
            c = readResult.c;
            Block comment = readResult.block;

            if (comment == null) {  // this covers even end of file
                // no comment starts here, we are done.
                return headerFactory.create(result.toString());
            }

            // we have start of commet in found variable.
            result.append(whitespace);
            result.append(comment.getStartSequence());

            // read characters till the end of comment
            StringBuilder commentContent = new StringBuilder();
            c = this.readTillEndOfComment(reader, c, comment, commentContent);

            result.append(commentContent);
            wasComment = true;
        }

        return headerFactory.create(result.toString());
    }

    private char skipWitespace(
            Reader reader,
            char previousInput,
            StringBuilder whitespace,
            boolean checkForDoubleNewlines,
            boolean ignorePreviousOutput) throws IOException {

            // last character from whitespace is anyway removed so
            // we dont have to check, if previousInput is white
            if (!ignorePreviousOutput) {
                whitespace.append(previousInput);
            }

            // char variable for reading the file
            char c = previousInput;                

            // indicates whether the character before was \n
            boolean firstNewline = false;   

            // read till end or till c is white
            while (reader.ready() && Character.isWhitespace(c)) {
                c = (char)reader.read();
                whitespace.append(c);
                if (checkForDoubleNewlines && c == '\n' && firstNewline) {
                    // returned character cant be whitespace
                    // because of semantic function of this method
                    // so ' ' may indicate special state
                    return ' ';
                }
                firstNewline = c == '\n';
            }
            whitespace.setLength(whitespace.length()-1); // last char is not white

            return c;
    }

    private char readTillEndOfComment(
            Reader reader,
            char previousInput,
            Block comment,
            StringBuilder commentContent)
            throws IOException, SyntaxErrorException {

        char c = previousInput;
        SearchSequence search = new SearchSequence(null, comment.getEndSequence());        
        while (reader.ready() && !search.found()) {
            search.next(c);
            commentContent.append(c);
            c = (char) reader.read();
        }

        if (!reader.ready()) {
            search.next(c);
            if (!search.found()) {
                throw new SyntaxErrorException();
            }
            commentContent.append(c);
        }

        return c;
    }

    private CharAndBlock readTillStartOfComment(
            Reader reader,
            char previousInput) throws IOException {

            char c = previousInput;
            List<SearchSequence> starts = this.getAllStartSequences();
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

            CharAndBlock result = new CharAndBlock();
            result.block = found;
            result.c = c;
            return result;
    }

    private class CharAndBlock {
        public char c;
        public Block block;
    }

    private List<SearchSequence> getAllStartSequences() {        
        final Vector<SearchSequence> result = new Vector<SearchSequence>();
        for(Block block : this.getCommentBlocks()) {
            result.add(new SearchSequence(
                    block,
                    block.getStartSequence()));
        }
        return result;
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
