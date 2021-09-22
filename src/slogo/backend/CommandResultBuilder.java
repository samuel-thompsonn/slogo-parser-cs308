package slogo.backend;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import slogo.CommandResult;

public class CommandResultBuilder {

  private double myRetVal = 0;
  private int myTokensParsed;
  private int turtleID;
  private double turtleHeading;
  private List<Double> turtlePos;
  private List<Double> startPos;
  private int pathColorIndex;
  private Map<String, Double> variables;
  private Map<String, String> userCommands;
  private boolean clear;
  private boolean penUp;
  private boolean turtleVis;
  private boolean turtleReset;
  private int bgColorIndex;
  private List<Integer> newColor;
  private double penSize;
  private List<Integer> activeTurtles;
  private int shapeIndex;
  private String errorMessage;
  private int newPaletteIndex;
  private boolean actualCommand;
  private boolean isUndo;
  private boolean isRedo;

  /**
   * Creates a CommandResultBuilder with starting settings that can be
   * used to construct CommandResults.
   *
   * Has a large number of parameters because of there are many
   * relevant pieces of information that need to be brought to the
   * front end.
   * @param turtleNumber
   * @param turtleFacing
   * @param turtlePosition
   * @param activeTurtleNumbers
   * @param visible
   * @param pathColor
   * @param bgColor
   * @param shape
   * @param size
   * @param isUp
   * @param varMap
   * @param commandMap
   */
  public CommandResultBuilder(int turtleNumber, double turtleFacing, List<Double> turtlePosition,
      List<Integer> activeTurtleNumbers, boolean visible,
      int pathColor, int bgColor, int shape, double size, boolean isUp,
      Map<String, Double> varMap, Map<String, String> commandMap) {
    //interpreter info (not used in front end)
    myRetVal = 0;
    myTokensParsed = 0;

    //turtle info
    turtleID = turtleNumber;
    turtlePos = new ArrayList<>(turtlePosition);
    turtleHeading = turtleFacing;
    turtleVis = visible;

    //path info
    startPos = null;
    pathColorIndex = pathColor;

    //state info
    variables = varMap;
    userCommands = commandMap;
    turtleReset = false;
    bgColorIndex = bgColor;
    newColor = null;
    shapeIndex = shape;
    penSize = size;
    newPaletteIndex = 0;
    activeTurtles = new ArrayList<>(activeTurtleNumbers);
    penUp = isUp;

    //meta info
    isUndo = false;
    isRedo = false;
    clear = false;
    actualCommand = true;
    errorMessage = "";
  }

  public void setTokensParsed(int val) {
    myTokensParsed = val;
  }

  public void setRetVal(double val) {
    myRetVal = val;
  }

  public void setTurtleID (int val) {
    turtleID = val;
  }

  public void setTurtlePos(List<Double> pos) {
    turtlePos = new ArrayList<>(pos);
  }

  public void setPathStart(List<Double> pos) {
    startPos = new ArrayList<>(pos);
  }

  public void setPathColor(int index) {
    pathColorIndex = index;
  }

  public void setPenSize(double size) { penSize = size; }

  public void activeTurtleIDs(List<Integer> turtles) {
    activeTurtles = new ArrayList<>(turtles);
  }

  public void setErrorMessage(String message) {
    errorMessage = message;
  }

  public void setIsActualCommand(boolean isCommand) {
    actualCommand = isCommand;
  }

  public void setTurtleReset(boolean isReset){ turtleReset = isReset; }

  public void setMyScreenClear(boolean value){ clear = value; }

  public void setPenUp(boolean penIsUp) {
    penUp = penIsUp;
  }

  public void setBackgroundColor(int index){ bgColorIndex = index; }

  public void setColor(List<Integer> color){ newColor = new ArrayList<>(color); }

  public void setShapeIndex(int index){ shapeIndex = index; }

  public void setPaletteIndex(int index){ newPaletteIndex = index; }

  public void setIsUndo(boolean undo) {
    isUndo = undo;
  }

  public void setIsRedo(boolean redo) {
    isRedo = redo;
  }

  public void setVariables(Map<String, Double> vars) {
    variables = vars;
  }

  public void setUserCommands(Map<String,String> commands) {
    userCommands = commands;
  }

  public void setVisible(boolean vis){ turtleVis = vis; }


  public CommandResult buildCommandResult() {
    return new CommandResult(myRetVal, myTokensParsed, turtleID, turtleHeading, turtlePos,
        startPos, pathColorIndex, variables, userCommands,
        clear, penUp, turtleVis, turtleReset, bgColorIndex, newColor, penSize, activeTurtles, shapeIndex,
            newPaletteIndex, errorMessage, actualCommand, isUndo, isRedo);
  }
}