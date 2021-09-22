package slogo.backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Sam Thompson
 * Saves a snapshot of the state of a SLogo model so that it can be used later for
 * restoring previous states.
 */
public class SLogoMemento {

  private List<Turtle> myTurtles;
  private List<Integer> myActiveTurtleIDs;
  private Map<Integer, List<Integer>> myPalette;
  private int myBackGroundIndex;
  private int myPenColorIndex;
  private int myShapeIndex;
  private double myPenSize;
  private Map<String, Double> myVariables;
  private UserCommandManager myUserCommandManager;

  /**
   * Creates a memento that owns a previous model state.
   *
   * The large number of arguments owes to the fact that a model has
   * many pieces of internal state info.
   * @param turtles A list of all Turtles in the model.
   * @param activeTurtles A list of the active turtle indices in the model.
   * @param palette The palette of colors stored in the model.
   * @param bgIndex The index of the current active background in the model.
   * @param pcIndex The index of the current pen color in the model.
   * @param shapeIndex The index of the current turtle shape.
   * @param penSize The size of the pen, in pixels.
   * @param variables The Map associating variable names to their current Double values.
   * @param manager A UserCommandManager holding the current user-defined commands.
   */
  public SLogoMemento(List<Turtle> turtles, List<Integer> activeTurtles, Map<Integer,List<Integer>> palette,
                      int bgIndex, int pcIndex, int shapeIndex, double penSize,
                      Map<String,Double> variables, UserCommandManager manager) {
    myTurtles = new ArrayList<>(turtles);
    myActiveTurtleIDs = new ArrayList<>(activeTurtles);
    myPalette = palette;
    myBackGroundIndex = bgIndex;
    myPenColorIndex = pcIndex;
    myShapeIndex = shapeIndex;
    myPenSize = penSize;
    myVariables = variables;
    myUserCommandManager = manager;
  }

  /**
   * @return The Turtles stored in the memento, as they were when it was created.
   */
  public List<Turtle> getTurtles() {
    List<Turtle> retList = new ArrayList<>();
    for (Turtle turtle : myTurtles) {
      retList.add(turtle.getClone());
    }
    return retList;
  }

  /**
   * @return The list of acive turtle IDs from when the memento was created.
   */
  public List<Integer> getActiveTurtles() {
    return new ArrayList<>(myActiveTurtleIDs);
  }

  /**
   * @return The palette of color-index mappings from when the memento was created.
   */
  public Map<Integer,List<Integer>> getPalette() {
    return new HashMap<>(myPalette);
  }

  /**
   * @return The color index of the background from when the memento was created.
   */
  public int getBackgroundIndex() {
    return myBackGroundIndex;
  }

  /**
   * @return The color index of the pen from when the memento was created.
   */
  public int getPenColorIndex() {
    return myPenColorIndex;
  }

  /**
   * @return The index in the shapes list of the turtles from when the memento was created.
   */
  public int getShapeIndex() {
    return myShapeIndex;
  }

  /**
   * @return The size of the pen in pixels from when the memento was created.
   */
  public double getPenSize() {
    return myPenSize;
  }

  /**
   * @return The String-value mappings representing variable names and their values
   *  from when the memento was created.
   */
  public Map<String, Double> getVariables() {
    return new HashMap<>(myVariables);
  }

  /**
   * @return The UesrCommandManager holding the user-defined commands from
   * when the memento was created.
   */
  public UserCommandManager getUserCommandManager() {
    return new UserCommandManager(myUserCommandManager);
  }
}
