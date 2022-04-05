package giada.josetta.util;

/**
 * The exception
 *
 * @author gianpiero.di.blasi
 */
public class JosettaException extends Exception {

  private static final long serialVersionUID = 1L;

  /**
   * Creates an exception
   */
  public JosettaException() {
  }

  /**
   * Creates an exception
   *
   * @param message The message
   */
  public JosettaException(String message) {
    super(message);
  }

  /**
   * Creates an exception
   *
   * @param message The message
   * @param cause The cause
   */
  public JosettaException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Creates an exception
   *
   * @param cause The cause
   */
  public JosettaException(Throwable cause) {
    super(cause);
  }
}
