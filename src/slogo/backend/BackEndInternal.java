package slogo.backend;
import java.util.Collection;
import java.util.List;
import slogo.CommandResult;

/**
 * @author Austin Odell, James Rumsey, Cary Shindell, Sam Thompson
 * Classes inside the view with access to the model may assume that it has these functions, which allow
 * the model to store state information that affects language processing.
 */
public interface BackEndInternal {

  /**
   * Creates or overrides a variable (:[a-zA-Z_]+) to store a double value so that
   * the next time it is referenced and used by a command that value is substituted in for it.
   * @param name The name of the variable, excluding the colon.
   * @param value The value to be stored in the variable.
   */
  void setVariable(String name, double value);

  /**
   * Returns the value of a variable if it has been set, otherwise throws an error
   * that is caught by the parser so that it can report that it doesn't know about
   * the variable.
   * @param name The name of the variable (excluding the colon) to look for and return
   *             the value of.
   */
  double getVariable(String name) throws ParseException;

  /**
   * Removes all (variable-name,value) pairings previously set in the model. Should be hidden from
   * overzealous Compsci 201 students.
   */
  void clearVariables();

  /**
   * Adds a user-defined command (Which was defined using TO-END) to the list of user-defined
   * commands so that the command can be recognized and parsed. Throws an exception when the user
   * attempts to overwrite a command that cannot be modified, like Forward.
   * Assumes that the user-defined command is able to run when passed in.
   * @param name The name to give the user-defined command to be used in the future.
   * @param parameters The local variables that are set by calls to the command and are used within
   *                   the command to execute it.
   * @param commands The contents of the command, which are parsed and executed whenever the
   *               command is called.
   */
  void setUserCommand(String name, List<String> parameters, String[] commands) throws ParseException;

  /**
   * Returns a list of named arguments whose size is equal to the number of
   * arguments for the user defined command.
   * @param name The name of the command to find arguments for.
   * @return The argument list of the command.
   */
  Collection<String> getUserCommandArgs(String name);

  /**
   * Returns the String contents of a user-defined command that were used to define the command
   * in the first place.
   * @param name The name of the String to return the script for.
   * @return The code that makes up the command's definition, as a String, referencing its
   * named constants.
   */
  Collection<String> getUserCommandScript(String name);

  /**
   * @param name The name of the user command to get the executable userCommand object for.
   * @return The userCommand object associated with the given command name, or null if the
   * name parameter doesn't match any known user command. This should result in an error message
   * by the time the front end gets information.
   */
  Command getUserCommand(String name);

  /**
   * @return A list of all Turtles in this instance of the model.
   */
  List<Turtle> getTurtles();

  /**
   * Set a new list of turtles.
   * @param t A list of Turtles that will be tied to this model.
   */
  void setTurtles(List<Turtle> t);

  /**
   * Remove all turtles from the model.
   */
  void clearTurtles();

  /**
   * Makes a command result object that contains no new information
   * except the return value of the command run and the number of tokens parsed by the command
   * being executed.
   * @param retVal The return value of the executed command.
   * @param tokensParsed The number of tokens that the command parsed in executing, if it is
   *                     a control command.
   * @return A commandResult containing the information inside the model plus the return value
   * and tokens parsed count. retVal is used for further calculations, and tokensParsed is used
   * to advance the program counter accordingly.
   */
  CommandResult makeCommandResult(double retVal, int tokensParsed);

  /**
   * Only "active" turtles will be changed when a command is run that affects all turtles.
   * This sets the active turtles to match the IDs in the list until the list of active turtles
   * is next updated.
   * @param turtleIDs The list of Integer turtle IDs that should be active.
   */
  void setActiveTurtles(List<Integer> turtleIDs);

  /**
   * Adds a color to the list of registered palette colors, so that commands which require a
   * color ID can reference the color.
   * @param index The index to assign to this color so it can be referenced in commands.
   * @param rgbColor A list of three integers between 0 and 255 inclusive that will be used
   *                 as the RGB values for the color.
   */
  void addPaletteColor(int index, List<Integer> rgbColor);

  /**
   * @param ids a list of IDs corresponding to turtles
   * @return List of turtles with these IDs
   */
  List<Turtle> getTurtles(List<Integer> ids);

  /**
   * @return The instances of the turtles currently active, in no particular order.
   */
  List<Turtle> getActiveTurtles();

  /**
   * @return The IDs of the turtles currently active, in no particular order.
   */
  List<Integer> getActiveTurtleNumbers();

  /**
   * Begins a configurable CommandResultBuilder based on the information about a specific turtle
   * that wil be updated in the view when the CommandResult is read.
   * @param turtleFacing The heading of the turtle.
   * @param turtlePosition The XY position of the turtle.
   * @param turtleVisible Whether the turtle is visible onscreen.
   * @return A CommandResultBuilder containing information about the model and the given turtle
   * information. This CommandResultBuilder can be configured with further information before
   * it is 'built' into a CommandResult.
   */
  CommandResultBuilder startCommandResult(double turtleFacing, List<Double> turtlePosition, boolean turtleVisible);

  /**
   * Begins a configurable CommandResultBuilder based on the information about a specific turtle
   * that wil be updated in the view when the CommandResult is read.
   * @param turtleID The ID of the turtle to return updated information about.
   * @param retVal The return value of the command that was run.
   * @return A CommandResultBuilder containing information about the model and the given turtle
   * information. This CommandResultBuilder can be configured with further information before
   * it is 'built' into a CommandResult.
   */
  CommandResultBuilder startCommandResult(int turtleID, double retVal);

  /**
   * Begins a CommandResultBuilder based on the given returnValue, with all other information
   * set to report that nothing has changed in the model. Used for commands that have no
   * side effects, like sum.
   * @param retVal The return value of the command run.
   * @return A CommandResultBuilder containing information about the model and the given return
   * value. This can be further configured before being turned into an immutable CommandResult.
   */
  CommandResultBuilder startCommandResult(double retVal);

  /**
   * @return The index in the palette of the current path color left by turtles.
   */
  int getPathColor();

  /**
   * @param index The index in the model's palette of the path color that turtles will leave
   *              when they move. Switches to the default when an invalid index is given.
   */
  void setPathColor(int index);

  /**
   * @return The index in the palette of the current background color.
   */
  int getBackgroundColor();

  /**
   * @param index The index in the palette of the new background color. Switches to the default
   *              when an invalid index is given.
   */
  void setBackgroundColor(int index);

  /**
   * @return The index in the list of stored turtle shapes of the shape that turtles are
   * currently taking.
   */
  int getShapeIndex();

  /**
   * @param index The index in the list of stored turtle shapes of the shape that turtles
   *              should take onscreen. Switches to the default shape when no shape exists for
   *              the given index.
   */
  void setShapeIndex(int index);

  /**
   * @return The current width of trails left by turtles, in pixels.
   */
  double getPenSize();

  /**
   * @param size The width of trails that will be left by turtles, in pixels.
   */
  void setPenSize(double size);

  /**
   * @return True if turtles have their pens up (and are NOT leaving a path), false otherwise.
   */
  boolean getPenUp();

  /**
   * @param isUp Whether turtles will have their pen up (doesn't leave a visible path) until
   *             further updated.
   */
  void setPenUp(boolean isUp);

  /**
   * Used within the execution of a turtle-based command so that commands (like ID)
   * whose effect varies per turtle can read information about the current turtle being moved.
   * @return The ID of the one turtle that is currently being manipulated by a command. If
   * no command is modifying a turtle when this is called, this returns null.
   */
  Integer getActiveTurtleID();

  /**
   * Used within the execution of a turtle-based command so that commands (like ID)
   * whose effect varies per turtle can read information about the current turtle being moved.
   * @param id The ID of the individual turtle that will next move during a turtle movement command.
   */
  void setActiveTurtleID(Integer id);

  /**
   * Generates a memento that stores the current state of the model so that it can be stored
   * then accessed later to restore the model to its former state.
   * @return A SLogoMemento that can be used to load the state of the program as it was when
   * this method was called.
   */
  SLogoMemento saveStateToMemento();

  /**
   * Changes the internal state of the model to what it was when the given SLogoMemento
   * was generated, and produces a list of CommandResults that allows a visualizer to
   * show the changes onscreen.
   * @param memento The memento that holds the target state of the model.
   * @param isUndo Whether the method was called as an 'undo' of a command.
   * @param isRedo Whether the method was called as a 'redo' of a command.
   * @return
   */
  List<CommandResult> loadStateFromMemento(SLogoMemento memento, boolean isUndo, boolean isRedo);

  /**
   * @param filename The name of the XML file in the userLibraries folder that will be read to
   *                 load user-defined commands and variables.
   * @return A CommandResult that either reflects the new user commands and variables or has
   * an error message saying that the file doesn't exist or is invalid.
   */
  List<CommandResult> loadLibraryFile(String filename);

  /**
   * Writes the current user-defined commands and variables as an XML file that can be loaded
   * later. This can overwrite files.
   * @param filename The name of the XML file that will be written in the userLibraries folder.
   *                 This file will contain all current user-defined commands and variables so
   *                 they can be loaded later.
   */
  void writeLibraryFile(String filename);
}

