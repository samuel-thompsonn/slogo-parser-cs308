package slogo.backend;

import java.util.List;
import slogo.CommandResult;

/**
 * @author Austin Odell, James Rumsey, Cary Shindell, Sam Thompson
 * Carries out the functionality of individual instructions
 * in the programming language used (SLogo in this case).
 * Implemented/Extended by concrete commands like "Forward".
 */
public abstract class Command {

  protected int NUM_ARGS = 0;
  protected int NUM_VARS = 0;

  /**
   * Carries out the command, changing the relevant data in the model according to the
   * effects of the command. Currently, commands can effect the turtle, the paths list,
   * the variables, and the user defined commands. The effects are documented and returned.
   * @return The effects on the model of this individual command, bundled into a CommandResult
   * instance.
   */
  public abstract List<CommandResult> execute(List<Double> arguments, List<String> vars, String[] tokens,
      BackEndInternal backEnd, Interpreter interpreter)
      throws ParseException;

  /**
   * Often does nothing, but this is needed for commands that require an identified command name,
   * such as the 'make' command. Reads the tokens passed to it and comes up with a list of tokens
   * to be skipped that include the names of variables that must be read. It is up to the execute
   * method to manage this list.
   * @param tokenList The list of remaining tokens, with the first token being the one following
   *                  this command.
   * @return A list of all relevant tokens for finding the names of variables that need to be
   * managed in the execute method.
   */
  public abstract List<String> findVars(String[] tokenList);

  /**
   * @return The number of argument values that the command requires.
   */
  public int getNumArgs(){ return NUM_ARGS;}

  /**
   * @return The number of variable names that the command requires.
   */
  public int getNumVars(){ return NUM_VARS; }

  /**
   * @return true if the command is run by each active turtle.
   */
  public boolean runsPerTurtle() {
    return false;
  }

  /**
   * @param tokens The remaining tokens after this command.
   *               Example: "repeat 3 [ fd 50 ]" requires {"3","[","fd","50","]"}.
   * @return
   */
  public int getTokensParsed(String[] tokens) {
    return 0;
  }
}
