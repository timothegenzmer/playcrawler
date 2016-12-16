package org.ogham.play.exceptions;

/**
 * @author Timothe Genzmer 546765
 */
public class ContentNotFound extends PlayException {
  public ContentNotFound() {
    super(404);
  }

  public ContentNotFound(String message) {
    super(404, message);
  }
}
