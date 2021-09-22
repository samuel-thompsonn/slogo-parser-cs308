package slogo.backend;

/**
 * @author Sam Thompson
 * Encompasses exceptions that occur when trying to parse a script using Interpreter.
 */
public class ParseException extends Exception {

  private final String myMessage;

  /**
   * The constructor for ParseException, where the message is set.
   * @param msg The message contained in the exception about what caused the exception.
   */
  public ParseException(String msg) {
    myMessage = msg;
  }

  /**
   * @deprecated
   * Generic constructor, with a general message
   */
  public ParseException() {
    myMessage = "Error in parse.";
  }

  /**
   * @return The message set when the exception was initialized. Used for
   * transmitting error messages to the front end through CommandResults.
   */
  public String getMessage() {
    return myMessage;
  }
}
