/*
 * file AbstractParser.java
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

import java.io.*;
import java.util.*;
import sourceheader.core.*;

/**
 * Provides basic infrastructure for header parsers, implementation
 * of Template method pattern.
 *
 * Concrete parser must provide only list of comment blocks - start and end
 * sequences of comments and supported extensions.
 *
 * @author steve
 */
public abstract class AbstractParser implements HeaderParser {

    /**
     * @return Inherited class should return list of comment block according to syntax
     * of supported files.
     */
    protected abstract Iterable<Block> getCommentBlocks();

    /**
     * @return Inherited class should return list of supported extensions.
     */
    public abstract String[] getExtensions();

    private ParsersConfig config;

    /**
     * Creates parser with given config.
     * @param config
     */
    public AbstractParser(ParsersConfig config) {
        this.config = config;
    }

    /**
     * @inheritDoc
     */
    public HeaderAndAlternatingParts parse(Path path, FileHeaderFactory headerFactory)
            throws IOException, SyntaxErrorException {
        try {
            return this.parse(new FileReader(path), headerFactory, path.getName());
        }
        catch(SyntaxErrorException ex) {
            throw new SyntaxErrorException(path, ex);
        }
    }

    /**
     * @inheritDoc
     */
    public HeaderParser.HeaderAndAlternatingParts parse(Reader reader, FileHeaderFactory headerFactory)
            throws IOException, SyntaxErrorException {
        return this.parse(reader, headerFactory, "");
    }

    /**
     * Unlike the two other parse methods this one takes filename as an argument.
     * Filename is than used as content for alternating part 'filename'.
     * @param reader
     * @param headerFactory
     * @param filename
     * @return Instance of class that ecapsulates header instance and map of
     * content of alternating parts.
     * @throws IOException
     * @throws sourceheader.core.HeaderParser.SyntaxErrorException
     */
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

    /**
     * Skips whitespace character from input.
     * @param reader
     * @param previousInput
     * @param whitespace
     * @param checkForDoubleNewlines
     * @param ignorePreviousOutput
     * @return First character that was not white.
     * @throws IOException
     */
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

    /**
     * Reads input until end of comment
     * that is until endSequence of given comment occurres.
     * @param reader
     * @param previousInput
     * @param comment
     * @param commentContent
     * @param alternatingPartsContent
     * @param filename
     * @return Last character that was read from input but was not processed.
     * @throws IOException
     * @throws sourceheader.core.HeaderParser.SyntaxErrorException
     */
    private char readTillEndOfComment(
            Reader reader,
            char previousInput,
            Block comment,
            StringBuilder commentContent,
            Map<String, List<String>> alternatingPartsContent,
            String filename)
            throws IOException, SyntaxErrorException {
        
        AlternatingPartsHandler alternatingParts =
                new AlternatingPartsHandler(alternatingPartsContent, this.config);
        SearchSequence filenameSequence = new SearchSequence(null, filename);

        char c = previousInput;
        SearchSequence search = new SearchSequence(null, comment.getEndSequence());
        while (reader.ready() && !search.found()) {
            if (!alternatingParts.inAlternatingPart()) {
                // handle special case of 'filename' alternating part
                if (!filename.isEmpty() && filenameSequence.next(c)) {
                    filenameSequence.reset();
                    commentContent.delete(
                            commentContent.length()-filename.length()+1,
                            commentContent.length());
                    commentContent.append( this.config.getSpecialCharacter() +
                            "filename" + this.config.getSpecialCharacter());
                }
                else {
                    // otherwise we are not in any alternating part,
                    // not in 'filename', so just append c
                    commentContent.append(c);
                }
            }
            commentContent.append(alternatingParts.next(c));
            search.next(c); // check end of comment
            c = (char) reader.read();
        }

        if (!reader.ready() && !search.found()) {
            // the file is at the end, but the last characted may
            // be last character of searched sequence...
            search.next(c);
            if (!search.found()) {
                throw new SyntaxErrorException(
                    "Sequence " + search.getSequence() + " that should finish" +
                    "the comment was not found but the end of file was reached.");
            }
            commentContent.append(c);
        }

        return c;
    }

    /**
     * Reads input until some start of comment block.
     * @param reader
     * @param previousInput
     * @return The comment block and last character encapsulated in one class.
     * @throws IOException
     */
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

    /**
     * Return type of readTillStartOfComment method.
     */
    private class CharAndBlock {
        public char c;
        public Block block;
    }

    /**
     * Helper method - generates list of SearchSequence class instances for
     * searching start sequence.
     * @return List of SearchSequence instances.
     */
    private List<SearchSequence> getAllStartSequences() {        
        final Vector<SearchSequence> result = new Vector<SearchSequence>();
        for(Block block : this.getCommentBlocks()) {
            result.add(new SearchSequence(block, block.getStartSequence()));
        }
        return result;
    }
}
