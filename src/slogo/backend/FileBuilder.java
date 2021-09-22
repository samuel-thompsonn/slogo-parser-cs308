package slogo.backend;

import java.util.List;
import java.util.Map;

/**
 * @author Sam Thompson
 * Creates a file that has all the information about variables and user-defined commands
 * saved in it so that it can be loaded later,
 * and loads files so that the user can build a library of commands and values.
 */
public interface FileBuilder {

  /**
   * Creates and populates a file at the specified location that contains the given
   * variables and user-defined commands. Follows a built-in structure (see outputTest.xml in
   * data/userlibraries for one implementation)
   * @param filePath The relative filepath of the target file, including the filename.
   * @param variables The map of variables and their values to be stored in the file.
   * @param commandArgs The map from user-defined command names to the number of arguments
   *                    commands have.
   * @param commandContents The map from user-defined command names to the scripts that commands
   *                        will run.
   */
  void makeLibraryFile(String filePath, Map<String, Double> variables, Map<String, List<String>> commandArgs, Map<String,String> commandContents);

  /**
   * @param filepath A file containing variable names mapped to values.
   * @return A Map that maps variable names to their values that matches the file.
   */
  Map<String,Double> loadVariablesFromFile(String filepath);

  /**
   * @param filepath The file containing user-defined commands mapped to their
   *                 argument names.
   * @return A Map that maps user-defined command names to their list of argument names.
   */
  Map<String,List<String>> loadCommandArguments(String filepath);

  /**
   * @param filepath The file containing user-defined commands mapped to the scripts that
   *                 run when they are executed.
   * @return A Map that maps user-defined command names to their scripts, matching the file.
   */
  Map<String,String> loadCommandInstructions(String filepath);
}
