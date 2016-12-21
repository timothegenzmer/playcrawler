package org.ogham.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * @author Timothe Genzmer 546765
 */
public class Decoder {

  public static String decode(String string) {
    try {
      return URLDecoder.decode(string, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }
}
