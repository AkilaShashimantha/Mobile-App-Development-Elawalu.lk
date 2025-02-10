package lk.payhere.androidsdk.util;

import java.security.NoSuchAlgorithmException;

import lk.payhere.androidsdk.PHConstants;

/**
 * Created by chamika on 7/8/2016.
 */
public class SecurityUtils
{
	 public static String generateMD5Value(String source)
	 {
		 try
		 {
			 java.security.MessageDigest md  = java.security.MessageDigest.getInstance("MD5");
			 byte[] array = md.digest(source.getBytes());
			 StringBuffer sb = new StringBuffer();
			 for (int i = 0; i < array.length; i++) {
				 sb.append( String.format( "%02x", array[i]));
			 }
			 return sb.toString().toUpperCase( PHConstants.DEFAULT_LOCALE );
		 }
		 catch ( NoSuchAlgorithmException e )
		 {
			 e.printStackTrace();
		 }
		 return null;
	 }
}
