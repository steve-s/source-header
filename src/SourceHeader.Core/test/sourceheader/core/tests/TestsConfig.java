/*
 * file TestsConfig.java
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

package sourceheader.core.tests;

import sourceheader.core.Path;

/**
 *
 * @author steve
 */
public class TestsConfig {
    public static Path getRootForTests() {
        return new Path("./test-data");
    }
}
