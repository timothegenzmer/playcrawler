package org.ogham.play.exceptions;

/**
 * @author Timothe Genzmer 546765
 */
public class ContentForbidden extends PlayException {
  public ContentForbidden() {
    super(403);
  }

  public ContentForbidden(String message) {
    super(403, message);
  }
}
