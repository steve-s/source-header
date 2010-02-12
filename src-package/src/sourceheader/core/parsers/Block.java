/*
 * file Block.java
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

/**
 * Class that encapsulates information about block of content.
 * Block of content consists of startSequence and endSequence,
 * for example start sequence for comment is / * and endSequence is * /
 *
 * @author steve
 */
public class Block {
        private String startSequence;
        private String endSequence;

        public Block(String startSequence, String endSequence) {
            this.startSequence = startSequence;
            this.endSequence = endSequence;
        }

        public String getStartSequence() {
            return startSequence;
        }

        public String getEndSequence() {
            return this.endSequence;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }

            if (obj instanceof Block) {
                Block block = (Block)obj;
                return this.startSequence.equals(block.startSequence) &&
                        this.endSequence.equals(block.endSequence);
            }

            return false;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 47 * hash + (this.startSequence != null ?
                this.startSequence.hashCode() : 0);
            hash = 47 * hash + (this.endSequence != null ?
                this.endSequence.hashCode() : 0);
            return hash;
        }
}
