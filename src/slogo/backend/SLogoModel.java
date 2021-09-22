package slogo.backend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import slogo.CommandResult;

/**
 * @author Sam Thompson,
 *
 * Holds information about the SLogo model: turtle information, user-defined variables,
 * and commands. Handles the manipulation of this information.
 */
public class SLogoModel implements BackEndInternal {

  public static final int INITIAL_BG_COLOR = 5;
  private Map<String, Double> myVariables;
  private UserCommandManager myUserCommandManager;
  private List<Turtle> myTurtles;
  private List<Turtle> myActiveTurtles;
  private Map<Integer, List<Integer>> myPalette;
  private int myPathColor = 0;
  private int myBackgroundColor;
  private int myShapeIndex = 0;
  private double myPenSize = 1;
  private boolean penUp = false;
  private Integer myActiveTurtleID;


  /**
   * Constructor, initializes default starting values.
   */
  public SLogoModel() {
    myVariables = new HashMap<>();
    myUserCommandManager = new UserCommandManager();
    myTurtles = new ArrayList<>();
    myTurtles.add(new SLogoTurtle(0));
    myActiveTurtles = List.of(myTurtles.get(0));
    myActiveTurtleID = null;
    myPalette = new HashMap<>();
    myBackgroundColor = INITIAL_BG_COLOR;
  }

  /**
   *
   * @param name The name of the variable, excluding the colon.
   * @param value The value to be stored in the variable.
   */
  @Override
  public void setVariable(String name, double value) {
    myVariables.put(name, value);
  }

  /**
   *
   * @param name The name of the variable (excluding the colon) to look for and return
   * @return The value associated with the given variable
   * @throws ParseException
   */
  @Override
  public double getVariable(String name) throws ParseException {
    if (myVariables.containsKey(name)) {
      return myVariables.get(name);
    }
    throw new ParseException("Don't know about variable " + name);
  }

  /**
   * Clears all mappings from variables to their values.
   */
  @Override
  public void clearVariables() {
    myVariables.clear();
  }

  /**
   * Sets the given name, arguments, and instructions to a user-defined command. Assumes
   * that the program has already checked if the name belongs to a primitive.
   * @param name The name to give the user-defined command to be used in the future.
   * @param parameters The local variables that are set by calls to the command and are used within
   *                   the command to execute it.
   * @param commands The contents of the command, which are parsed and executed whenever the
   */
  @Override
  public void setUserCommand(String name, List<String> parameters, String[] commands) {
    myUserCommandManager.addUserCommand(name, parameters, Arrays.asList(commands));
  }

  /**
   * @param name The name of the command to find arguments for.
   * @return The list of argument names (in order) for the command.
   */
  @Override
  public Collection<String> getUserCommandArgs(String name) {
    if (myUserCommandManager.containsCommand(name)) {
      return myUserCommandManager.getArguments(name);
    }
    return null;
  }

  /**
   * @param name The name of the String to return the script for.
   * @return The script that is run when the given command is executed. Returns null
   * if no such command exists.
   */
  @Override
  public Collection<String> getUserCommandScript(String name) {
    return BackEndUtil.getTokenList(myUserCommandManager.getCommandScript(name));
  }

  /**
   * @param name The name of the user command to get the executable userCommand object for.
   * @return The executable Command object that runs the user-defined command specified.
   */
  @Override
  public Command getUserCommand(String name) {
    if (myUserCommandManager.containsCommand(name)) {
      return myUserCommandManager.getCommand(name);
    }
    return null;
  }

  /**
   * @return A list of all Turtles in this instance of the model.
   */
  @Override
  public List<Turtle> getTurtles() {
    return new ArrayList<>(myTurtles);
  }

  /**
   * Set a new list of turtles.
   * @param t A list of Turtles that will be tied to this model.
   */
  @Override
  public void setTurtles(List<Turtle> t) {
    myTurtles = new ArrayList<>(t);
  }

  /**
   * Remove all turtles from the model.
   */
  @Override
  public void clearTurtles() {
    myTurtles.clear();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CommandResult makeCommandResult(double retVal, int tokensParsed) {
    CommandResultBuilder builder = startCommandResult(retVal);
    builder.setTokensParsed(tokensParsed);
    return builder.buildCommandResult();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setActiveTurtles(List<Integer> turtleIDs) {
    ArrayList<Turtle> active = new ArrayList<>();
    for (Integer num : turtleIDs) {
      active.add(getTurtleWithID(num));
    }
    myActiveTurtles = active;
  }

  /**
   * Adds a color to the list of registered palette colors, so that commands which require a
   * color ID can reference the color.
   * NOTE that the palette in the model is not used in the current implementation, and may
   * not be faithful to the state expected, since the front end handles colors.
   * @param index The index to assign to this color so it can be referenced in commands.
   * @param rgbColor A list of three integers between 0 and 255 inclusive that will be used
   *                 as the RGB values for the color.
   */
  @Override
  public void addPaletteColor(int index, List<Integer> rgbColor) {
    myPalette.put(index, rgbColor);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Turtle> getTurtles(List<Integer> ids) {
    List<Turtle> ret = new ArrayList<>();
    for (int num : ids) {
      ret.add(getTurtleWithID(num));
    }
    return ret;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Turtle> getActiveTurtles() {
    return new ArrayList<>(myActiveTurtles);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Integer> getActiveTurtleNumbers() {
    List<Integer> ret = new ArrayList<>();
    for (Turtle active : myActiveTurtles) {
      ret.add(active.getId());
    }
    return ret;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CommandResultBuilder startCommandResult(double turtleFacing, List<Double> turtlePosition, boolean turtleVisible) {
    return new CommandResultBuilder(0, turtleFacing,turtlePosition,getActiveTurtleNumbers(), turtleVisible,  myPathColor, myBackgroundColor, myShapeIndex, myPenSize, penUp, myVariables,myUserCommandManager.getScriptMap());

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CommandResultBuilder startCommandResult(int turtleID, double retVal) {
    Turtle turtle = getTurtleWithID(turtleID);
    CommandResultBuilder ret =  new CommandResultBuilder(turtleID, turtle.getHeading(),turtle.getPosition(),getActiveTurtleNumbers(), turtle.getVisible(), myPathColor, myBackgroundColor, myShapeIndex, myPenSize, penUp, myVariables,myUserCommandManager.getScriptMap());
    ret.setRetVal(retVal);
    return ret;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CommandResultBuilder startCommandResult(double retVal) {
    CommandResultBuilder ret = new CommandResultBuilder(0,myTurtles.get(0).getHeading(),myTurtles.get(0).getPosition(),
                                    getActiveTurtleNumbers(), myTurtles.get(0).getVisible() ,myPathColor,myBackgroundColor,myShapeIndex,myPenSize,penUp, myVariables, myUserCommandManager.getScriptMap());
    ret.setRetVal(retVal);
    return ret;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getPathColor(){ return myPathColor; }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPathColor(int index){ myPathColor = index; }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getBackgroundColor(){ return myBackgroundColor; }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setBackgroundColor(int index){ myBackgroundColor = index; }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getShapeIndex(){ return myShapeIndex; }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setShapeIndex(int index){ myShapeIndex = index; }

  /**
   * {@inheritDoc}
   */
  @Override
  public double getPenSize(){ return myPenSize; }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPenSize(double size){ myPenSize = size; }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean getPenUp(){ return penUp; }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPenUp(boolean isUp){ penUp = isUp; }

  /**
   * {@inheritDoc}
   */
  @Override
  public Integer getActiveTurtleID() {
    return myActiveTurtleID;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SLogoMemento saveStateToMemento() {
    ArrayList<Turtle> turtleCopy = new ArrayList<>();
    for (Turtle turtle : myTurtles) {
      turtleCopy.add(turtle.getClone());
    }
    return new SLogoMemento(turtleCopy,getActiveTurtleNumbers(),new HashMap<>(myPalette),
                            myBackgroundColor,myPathColor,
                            myShapeIndex, myPenSize,
                            new HashMap<>(myVariables),new UserCommandManager(myUserCommandManager));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<CommandResult> loadStateFromMemento(SLogoMemento memento, boolean isUndo, boolean isRedo) {
    myTurtles = memento.getTurtles();
    setActiveTurtles(memento.getActiveTurtles());
    myPalette = memento.getPalette();
    myBackgroundColor = memento.getBackgroundIndex();
    myPathColor = memento.getPenColorIndex();
    myPenSize = memento.getPenSize();
    myShapeIndex = memento.getShapeIndex();
    myVariables = memento.getVariables();
    myUserCommandManager = memento.getUserCommandManager();

    ArrayList<CommandResult> results = new ArrayList<>();
    for (Turtle turtle : myTurtles) {
      CommandResultBuilder builder = startCommandResult(turtle.getHeading(),turtle.getPosition(), turtle.getVisible());
      builder.setTurtleID(turtle.getId());
      builder.setIsUndo(isUndo);
      builder.setIsRedo(isRedo);
      results.add(builder.buildCommandResult());
    }
    return results;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setActiveTurtleID(Integer id) {
    myActiveTurtleID = id;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<CommandResult> loadLibraryFile(String filePath) {
    FileBuilder fileBuilder = new SLogoFileBuilder();
    myVariables = new HashMap<>(fileBuilder.loadVariablesFromFile(filePath));
    myUserCommandManager = new UserCommandManager(fileBuilder.loadCommandArguments(filePath),fileBuilder.loadCommandInstructions(filePath));
    CommandResultBuilder builder = startCommandResult(0);
    return List.of(builder.buildCommandResult());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void writeLibraryFile(String filename) {
    FileBuilder fileBuilder = new SLogoFileBuilder();
    fileBuilder.makeLibraryFile(filename,new HashMap<>(myVariables),
        myUserCommandManager.getArgumentsMap(),
        myUserCommandManager.getScriptMap());
  }

  private Turtle getTurtleWithID(Integer num) {
    for (Turtle turtle : myTurtles) {
      if (turtle.getId() == num) {
        return turtle;
      }
    }
    Turtle newTurtle = new SLogoTurtle(num);
    myTurtles.add(newTurtle);
    return newTurtle;
  }
}