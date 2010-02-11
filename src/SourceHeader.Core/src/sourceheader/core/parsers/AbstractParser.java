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

    private ParsersConfig config;

    public AbstractParser(ParsersConfig config) {
        this.config = config;
    }

    private Path path;

    public HeaderAndAlternatingParts parse(Path path, FileHeaderFactory headerFactory)
            throws IOException, SyntaxErrorException {
        this.path = path;
        try {
            return this.parse(new FileReader(path), headerFactory, path.getName());
        }
        catch(SyntaxErrorException ex) {
            throw new SyntaxErrorException(path, ex);
        }
    }

    public HeaderParser.HeaderAndAlternatingParts parse(Reader reader, FileHeaderFactory headerFactory)
            throws IOException, SyntaxErrorException {
        return this.parse(reader, headerFactory, "");
    }

    public HeaderAndAlternatingParts parse(
            Reader reader,
            FileHeaderFactory headerFactory,
            String filename)
            throws IOException, SyntaxErrorException {
        StringBuilder result = new StringBuilder(); // the new header content.
        boolean wasComment = false; // indicates whether first comment was found.
        char c = ' '; // current character read from file.
        Map<String, List<String>> alternatingPartsContent =
                new HashMap<String, List<String>>();

        while (reader.ready()) {

            StringBuilder whitespace = new StringBuilder();
            c = this.skipWitespace(reader, c, whitespace, wasComment, !wasComment);

            if (c == ' ') {
                break;
            }

            // check for comment start
            CharAndBlock readResult = this.readTillStartOfComment(reader, c);
            c = readResult.c;
            Block comment = readResult.block;

            if (comment == null) {  // this covers even end of file
                // no comment starts here, we are done.
                break;
            }

            // we have start of commet in found variable.
            result.append(whitespace);
            result.append(comment.getStartSequence());

            // read characters till the end of comment
            StringBuilder commentContent = new StringBuilder();
            c = this.readTillEndOfComment(reader, c, comment, 
                    commentContent, alternatingPartsContent, filename);

            result.append(commentContent);
            wasComment = true;
        }

        HeaderAndAlternatingParts resultMessenger = new HeaderAndAlternatingParts();
        resultMessenger.header = headerFactory.create(
                this.createHeaderHook(result.toString()));
        resultMessenger.alternatingParts = alternatingPartsContent;
        return resultMessenger;
    }

    /**
     * Hook point for changing header content before it is created via factory.
     * @param content Content parsed from file/reader.
     * @return New content, default implementation return the same content.
     */
    protected String createHeaderHook(String content) {
        return content;
    }

    private char skipWitespace(
            Reader reader,
            char previousInput,
            StringBuilder whitespace,
            boolean checkForDoubleNewlines,
            boolean ignorePreviousOutput) throws IOException {

            if (!ignorePreviousOutput && Character.isWhitespace(previousInput)) {
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

            // Last character needn't to be non-white, when loop is not proced
            // and first if is positive in method.
            if (whitespace.length() > 0 &&
                !Character.isWhitespace(whitespace.charAt(whitespace.length()-1))) {
                whitespace.setLength(whitespace.length()-1); // last char is not white
            }

            return c;
    }

    private char readTillEndOfComment(
            Reader reader,
            char previousInput,
            Block comment,
            StringBuilder commentContent,
            Map<String, List<String>> alternatingPartsContent,
            String filename)
            throws IOException, SyntaxErrorException {
        
        AlternatingPartsHandler alternatingParts =
                new AlternatingPartsHandler(alternatingPartsContent);
        SearchSequence filenameSequence = new SearchSequence(null, filename);

        char c = previousInput;
        SearchSequence search = new SearchSequence(null, comment.getEndSequence());        
        while (reader.ready() && !search.found()) {
            if (!alternatingParts.inAlternatingPart()) {
                if (!filename.isEmpty() && filenameSequence.next(c)) {
                    filenameSequence.reset();
                    commentContent.delete(
                            commentContent.length()-filename.length()+1,
                            commentContent.length());
                    commentContent.append( this.config.getSpecialCharacter() +
                            "filename" + this.config.getSpecialCharacter());
                }
                else {
                    commentContent.append(c);
                }
            }
            commentContent.append(alternatingParts.next(c));
            search.next(c);
            c = (char) reader.read();
        }

        if (!reader.ready() && !search.found()) {
            // the file is at the end, but the last characted may
            // be last character of searched sequence...
            search.next(c);
            if (!search.found()) {
                throw new SyntaxErrorException(
                    "Sequence " + search.sequence + " that should finish" +
                    "the comment was not found but the end of file was reached.");
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

    private class AlternatingPartsHandler {
        Map<String, List<String>> parts;
        List<SearchSequence> searchSequences;
        Map<Block, Integer> indexes = new HashMap<Block, Integer>();
        Block currentPart = null;
        SearchSequence currentPartSearchSequence = null;
        StringBuilder currentPartContent = new StringBuilder();

        public AlternatingPartsHandler(Map<String, List<String>> parts) {
            this.parts = parts;
            this.setUpAlternatingPartsSearchSequences();
        }

        private void setUpAlternatingPartsSearchSequences() {
            this.searchSequences =
                    new Vector<SearchSequence>();

            for (String name : config.getAlternatingParts().keySet()) {
                Block block = config.getAlternatingParts().get(name);
                this.searchSequences.add(new SearchSequence(block, block.getStartSequence()));
            }
        }

        public String next(char c) {
            if (this.currentPart != null) {
                return this.nextInCurrentPart(c);
            }

            this.nextInSearchForPart(c);
            return "";
        }

        private void nextInSearchForPart(char c) {
            for (SearchSequence sequence : this.searchSequences) {
                if (sequence.next(c)) {
                    sequence.reset();
                    this.currentPart = (Block) sequence.getData();
                    this.currentPartSearchSequence = new SearchSequence(
                                sequence.getData(),
                                this.currentPart.getEndSequence());
                    Integer i = this.indexes.get((Block)sequence.getData());
                    if (i == null) {
                        i = new Integer(-1);
                    }
                    i++;
                    this.indexes.put((Block)sequence.getData(), i);
                }
            }
        }

        private String nextInCurrentPart(char c) {
            StringBuilder result = new StringBuilder();
            if (this.currentPartSearchSequence.next(c)) {
                // add id with index into result
                this.appendIdOfCurrentPart(result);

                this.putCurrentPartContentIntoContentsMap();

                // reset everything
                this.currentPartContent.setLength(0);
                this.currentPart = null;
                this.currentPartSearchSequence = null;
            }
            else {
                this.currentPartContent.append(c);
            }

            return result.toString();
        }

        private void appendIdOfCurrentPart(StringBuilder result) {
            result.append(config.getSpecialCharacter());
            result.append(this.getCurrentPartName());
            result.append(config.getSpecialCharacter());
            result.append(this.indexes.get(this.currentPart).toString());
            result.append(config.getSpecialCharacter());
            result.append(this.currentPart.getEndSequence());
        }

        private void putCurrentPartContentIntoContentsMap() {
                // put the content into list of contents
                if (!this.parts.containsKey(this.getCurrentPartName())) {
                    this.parts.put(this.getCurrentPartName(),
                            new ArrayList<String>());
                }
                //cut-off block end sequence:
                this.currentPartContent.setLength(
                        this.currentPartContent.length() -
                        this.currentPart.getEndSequence().length() + 1);
                this.parts.get(this.getCurrentPartName())
                        .add(this.currentPartContent.toString());
        }

        private String getCurrentPartName() {
            return config.getAlternatingPartName(this.currentPart);
        }

        public boolean inAlternatingPart() {
            return this.currentPart != null;
        }
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

        public void reset() {
            this.current.setLength(0);
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

            // 'c' might be first character of sequence
            if (c == this.nextCharacter()) {
                this.current.append(c);
                return this.found();
            }

            return false;    
        }
    }
}
