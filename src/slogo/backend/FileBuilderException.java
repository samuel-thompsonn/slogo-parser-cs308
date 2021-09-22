package slogo.backend;

/**
 * @author Sam Thompson
 * Represents an error in building a library file from a model's
 * user-defined commands and variable values.
 */
public class FileBuilderException extends Exception {
  private String myMessage;

  /**
   * Standard constructor.
   * @param message The message to store in the exception describing why it occurred
   *                or how to manage it.
   */
  public FileBuilderException(String message) {
    myMessage = message;
  }

  /**
   * @return The message stored in the exception when it was created.
   */
  public String getMessage() {
    return myMessage;
  }
}
