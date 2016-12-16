package org.ogham.play.exceptions;

/**
 * @author Timothe Genzmer 546765
 */
public class PlayException extends Exception {

  private int statuscode;

  public PlayException(int statuscode) {
    this.statuscode = statuscode;
  }

  public PlayException(int statuscode, String message) {
    super(message);
    this.statuscode = statuscode;
  }

  public int getStatuscode() {
    return statuscode;
  }
}
