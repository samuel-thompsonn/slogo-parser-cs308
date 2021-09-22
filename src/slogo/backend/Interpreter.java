package slogo.backend;

import java.util.List;
import slogo.CommandResult;

/**
 * @author Sam Thompson
 * Parses scripts in languages like SLogo. It can manage a model that is affected
 * by the running of these scripts.
 */
public interface Interpreter {

  /**
   *
   * @param tokenList The tokens to parse, in the order that they appear in the script
   * @return A list of CommandResults reflecting every change in the model that was caused by
   * the script, in the order that they happened.
   */
  List<CommandResult> parseCommandsList(String[] tokenList);

  /**
   * Similar to parseCommandsList, but when an expression is evaluated, instead of moving
   * on to the next expression, the method returns the floating return value and all of the
   * side effects of the commands run.
   * Does NOT always execute the whole script given.
   * @see Interpreter#parseCommandsList(String[])
   */
  List<CommandResult> parseForRetVal(String[] tokenList) throws ParseException;

  /**
   * @param language The name of the properties file that should be used to determine
   *                 what language will be used for parsing. Ex. "Chinese"
   */
  void setLanguage(String language);

  /**
   * @param command The String identifier for the command, as seen in the left hand column
   *                of command resource files.
   * @return True if the given string is the name of a built-in command that cannot be over-
   * written by user-defined commands.
   */
  boolean hasPrimitiveCommand(String command);
}
