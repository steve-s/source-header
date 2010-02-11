/*
 * file MD5.java
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

package sourceheader.core.util;

import java.security.*;

/**
 *
 * @author steve
 */
public class MD5 {
    public static String create(String content) throws MD5IsNotSupported {
        String result = "";
        try{
            byte[] defaultBytes = content.getBytes();
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(defaultBytes);
            byte messageDigest[] = algorithm.digest();

            StringBuffer hexString = new StringBuffer();
            for (int i=0;i<messageDigest.length;i++) {
		hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            }
            result = hexString.toString();
        }catch(NoSuchAlgorithmException ex){
            throw new MD5IsNotSupported(ex);
        }

        assert !result.isEmpty();
        return result;
    }

    public static class MD5IsNotSupported extends NoSuchAlgorithmException {
        public MD5IsNotSupported(Throwable exception) {
            super("MD5 algorithm is not implemented in this java.", exception);
        }
    }
}
