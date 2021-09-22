package slogo.backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import slogo.backend.commands.controlandvariables.UserCommand;

/**
 * @author Sam Thompson
 * Keeps track of and handles the manipulation of user-defined commands.
 * In SLogo, these are made using the 'to' command.
 */
public class UserCommandManager {

  private Map<String, UserCommand> myCommands;

  /**
   * Default constructor. Initializes an empty HashMap of commands.
   */
  public UserCommandManager() {
    myCommands = new HashMap<>();
  }

  /**
   * The two Maps describing user commands should have the same commands listed in order
   * to produce a UserCommandManager with data that can be used for running commands.
   * @param argMap A Map associating names of commands with lists of named arguments they
   *               require.
   * @param instructionMap A Map associating names of commands with the scripts run when
   *                       they are executed.
   */
  public UserCommandManager(Map<String,List<String>> argMap, Map<String,String> instructionMap) {
    //Looping through keySet() because there are two maps with matching keys
    // (the file builder ensures that the keys match before they are stored).
    myCommands = new HashMap<>();
    for (String cmdName : argMap.keySet()) {
      addUserCommand(cmdName,argMap.get(cmdName),BackEndUtil.getTokenList((instructionMap.get(cmdName))));
    }
  }

  /**
   * Copies another UserCommandManager.
   * @param original The UserCommandManager to be copied.
   */
  public UserCommandManager(UserCommandManager original) {
    myCommands = original.myCommands;
  }

  /**
   * @param name The command name to check to see if it exists.
   * @return True if this manager already has a command with the given name.
   */
  public boolean containsCommand(String name) {
    return myCommands.containsKey(name);
  }

  /**
   * Adds a new user-defined command to the UserCommandManager's list of commands.
   * @param name The name of the new command.
   * @param arguments The list of named arguments the command has, in the order they
   *                  are required.
   * @param commands The list of tokens that will be executed by an interpreter when the
   *                 command is run.
   */
  public void addUserCommand(String name, List<String> arguments,List<String> commands) {
    UserCommand newCommand = new UserCommand(arguments,commands);
    myCommands.put(name,newCommand);
  }

  /**
   * @param name The name of the command to get the arguments for.
   * @return A list of the named arguments the command requires, in the order in which
   * they are expected when the command is run.
   * Returns null if the command doesn't exist.
   */
  public List<String> getArguments(String name) {
    return new ArrayList<>(myCommands.get(name).getArguments());
  }

  /**
   * @return A Map of all stored mappings from names of commands to their ordered parameter
   * names.
   */
  public Map<String, List<String>> getArgumentsMap() {
    Map<String,List<String>> argMap = new HashMap<>();
    for (String commandName : myCommands.keySet()) {
      argMap.put(commandName,getArguments(commandName));
    }
    return argMap;
  }

  /**
   * @param name The name of the command to get the script for.
   * @return The script that an interpreter runs when the command is executed.
   * Returns null if the command doesn't exist.
   */
  public String getCommandScript(String name) {
    return myCommands.get(name).getInstructions();
  }

  /**
   * @return A Map of all stored mappings from names of commands to the scripts that
   * are run by an interpreter when they are executed.
   */
  public Map<String, String> getScriptMap() {
    Map<String,String> scriptMap = new HashMap<>();
    for (String commandName : myCommands.keySet()) {
      scriptMap.put(commandName,getCommandScript(commandName));
    }
    return scriptMap;
  }

  /**
   * @param name The name of the command to get an executable Command object for.
   * @return An executable UserCommand that runs the appropriate script with the appropriate
   * arguments, as was set when the command was most recently updated.
   */
  public Command getCommand(String name) {
    return myCommands.get(name);
  }
}
