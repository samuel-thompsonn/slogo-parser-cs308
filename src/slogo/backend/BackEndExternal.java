package slogo.backend;

import slogo.CommandResult;

import java.util.List;

/**
 * @author Austin Odell, James Rumsey, Cary Shindell, Sam Thompson
 * The public side of the model of the SLogo parser. View and controller classes that interact
 * with the model can only assume that it has the public methods listed.
 */
public interface BackEndExternal {

  /**
   * Takes in a script (a block of SLogo for this project) as a String, interprets the script,
   * updates the back end state accordingly, and produces a List of CommandResults that describes
   * what affect each command had on the turtle, the paths, the variables, and the user defined
   * commands.
   * @param script The SLogo script to be parsed. Other implementations could use other languages
   *               and return other CommandResult types.
   * @return A List of CommandResults describing the effect of each command run, in the order they
   * were run.
   */
  List<CommandResult> parseScript(String script);


  /**
   * Redoes the parsing of the most recent script, restoring the state of the model
   * from before it was last undone.
   * Does nothing and returns an empty list if there is nothing to redo.
   * @return A list of CommandResults with enough information to update to the previous state.
   */
  List<CommandResult> redo();

  /**
   * Undoes the parsing of the most recent script, restoring the state of the model
   * from before it was run.
   * Does nothing and returns an empty list if there is nothing to undo.
   * @return A list of CommandResults with enough information to update to the redone state.
   */
  List<CommandResult> undo();

  /**
   * loads user-defined commands and variables from the file at the given path and
   * overwrites the current active library of variables and commands to match the one
   * in the file.
   * @param filePath The relative filepath (including the filename and suffix) where the
   *                 library is stored.
   * @return A list of CommandResults containing the updated commands and variables.
   */
  List<CommandResult> loadLibraryFile(String filePath);

  /**
   * Writes all currently defined variables and user-defined commands to a file with the
   * specified filepath.
   * @param filePath The filepath (including filename and suffix) where the library is stored.
   *                 Should have the appropriate filetype ot match the implementation of
   *                 this method.
   */
  void writeLibraryFile(String filePath);

  /**
   * Applies a Changer to modify the back end.
   * Example: SLogoLanguageChanger can be passed in to change the language to whatever the
   * SLogoLanguageChanger's target language has been set to.
   * @param changer the Changer to use on the back end.
   */
  void applyChanger(Changer changer);
}
