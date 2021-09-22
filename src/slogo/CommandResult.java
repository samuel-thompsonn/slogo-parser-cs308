package slogo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CommandResult {
  private double returnVal;
  private int myTokensParsed;
  private String myErrorMessage;
  private double myRotation;
  private List<Double> myPosition;
  private Map<String,Double> myVariables;
  private Map<String, String> myUserCommands;
  private boolean myScreenClear;
  private boolean myPenUp;
  private boolean myTurtleVisible;
  private boolean myTurtleReset;
  private List<Double> myPathStart;
  private int myPenColor;
  private int myBackgroundColor;
  private List<Integer> myNewPaletteColor;
  private double myPenSize;
  private List<Integer> myActiveTurtleIDs;
  private int myShapeIndex;
  private int myTurtleID;
  private String myOriginalInstruction;
  private boolean actualCommand;
  private int paletteIndex;
  boolean isUndo;
  boolean isRedo;

  public CommandResult(double retVal, int tokensParsed, int turtleID, double heading, List<Double> pos, List<Double> pathStart, int penColor,
                       Map<String, Double> variables, Map<String,String> userCommands,
                       boolean clearScreen, boolean penUp, boolean turtleVisible, boolean turtleReset,
                       int backgroundColor, List<Integer> newPaletteColor, double penSize, List<Integer> activeTurtles,
                       int shapeIndex, int newPaletteIndex, String errorMessage, boolean doesSomething, boolean undo, boolean redo) {
    returnVal = retVal;
    myTokensParsed = tokensParsed;
    myErrorMessage = errorMessage;
    myRotation = heading;
    myPosition = pos;
    myVariables = variables;
    myUserCommands = userCommands;
    myScreenClear = clearScreen;
    myPenUp = penUp;
    myTurtleVisible = turtleVisible;
    myTurtleReset = turtleReset;
    myPathStart = pathStart;
    myPenColor = penColor;
    myBackgroundColor = backgroundColor;
    myNewPaletteColor = newPaletteColor;
    myPenSize = penSize;
    myActiveTurtleIDs = activeTurtles;
    myShapeIndex = shapeIndex;
    myTurtleID = turtleID;
    paletteIndex = newPaletteIndex;
    actualCommand = doesSomething;
    isUndo = undo;
    isRedo = redo;
  }

  public void setErrorMessage(String msg) {
    myErrorMessage = msg;
  }

  public String getErrorMessage() {
    return myErrorMessage;
  }

  public double getReturnVal() {
    return returnVal;
  }

  public int getTokensParsed() {
    return myTokensParsed;
  }

  public double getTurtleHeading() {
    return myRotation;
  }

  public List<Double> getTurtlePosition() {
    return myPosition;
  }

  public List<Double> getPathStart(){
      return myPathStart;
  }

  public boolean isMyScreenClear() {
    return myScreenClear;
  }

  public boolean isMyPenUp() {
    return myPenUp;
  }

  public boolean isMyTurtleVisible() {
    return myTurtleVisible;
  }

  public boolean isMyTurtleReset() {
    return myTurtleReset;
  }

  public int getPenColor(){
    return myPenColor;
  }

  public int getBackgroundColor(){
    return myBackgroundColor;
  }

  public List<Integer> getNewPaletteColor(){
    return myNewPaletteColor;
  }

  public double getPenSize(){
    return myPenSize;
  }

  public List<Integer> getActiveTurtleIDs(){
    return myActiveTurtleIDs;
  }

  public int getShapeIndex(){
    return myShapeIndex;
  }

  public int getTurtleID(){
    return myTurtleID;
  }

  public void setMyOriginalInstruction(String instruction){
    myOriginalInstruction = instruction;
  }

  public String getMyOriginalInstruction(){
    return myOriginalInstruction;
  }

  public boolean isActualCommand(){
    return actualCommand;
  }

  public int getPaletteIndex(){
    return paletteIndex;
  }

  public void setMyScreenClear(boolean value){
    myScreenClear = value;
  }

  public Map<String, Double> getVariables() {
    return myVariables;
  }

  public Map<String, String> getUserDefinedCommands() {
    return myUserCommands;
  }

  public void setTokensParsed(int tokensParsed) {
    myTokensParsed = tokensParsed;
  }

  public boolean isUndo() {
    return isUndo;
  }

  public boolean isRedo() {
    return isRedo;
  }
}
