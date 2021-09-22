package slogo.frontEnd;

import java.io.File;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Queue;
import java.util.ResourceBundle;
import java.util.TreeMap;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import slogo.CommandResult;

import javax.imageio.ImageIO;


import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.function.Consumer;

@SuppressWarnings({"unused", "StringEquality"})
public class Visualizer extends Application implements FrontEndExternal{

  private static final String RESOURCE_LOCATION = "slogo/frontEnd/Resources.";
  private static final ResourceBundle myResources = ResourceBundle.getBundle(RESOURCE_LOCATION + "config");
  private static final List<String> MENU_TYPES = Arrays.asList(myResources.getString("MenuTypes").split(","));
  private static final double HEIGHT = Double.parseDouble(myResources.getString("WindowHeight"));
  private static final double ASPECT_RATIO = (16.0/9.0);
  private static final double WIDTH = HEIGHT * ASPECT_RATIO;
  private static final Paint BACKGROUND = Color.WHITE;
  private static final Rectangle COMMAND_BOX_SHAPE = new Rectangle(650, 125);
  private static final Rectangle TURTLE_VIEW_SHAPE = new Rectangle(300*ASPECT_RATIO,325);
  private static final Rectangle HISTORY_VIEW_SHAPE = new Rectangle(225, 125);
  private static final Rectangle UDC_VIEW_SHAPE = new Rectangle(225, 125);
  private static final Rectangle VARIABLES_VIEW_SHAPE = new Rectangle(225, 125);
  private static final Rectangle RUN_BUTTON_SHAPE = new Rectangle(60, 30);
  private static final Rectangle CLEAR_HISTORY_BUTTON_SHAPE = new Rectangle(30, 30);
  private static final Rectangle CLEAR_UDC_BUTTON_SHAPE = new Rectangle(30, 30);
  private static final Rectangle CLEAR_VARIABLES_BUTTON_SHAPE = new Rectangle(30, 30);
  private static final Rectangle TOP_RIGHT_BUTTON_SHAPE = new Rectangle(75, 20);
  private static final Rectangle TURTLE_BUTTON_SHAPE = new Rectangle(60, 30);
  private static final Rectangle HELP_WINDOW_SHAPE = new Rectangle(600, 600);
  private static final Rectangle TURTLE_MOVEMENT_LABEL_SHAPE = new Rectangle(20, 5);
  private static final Rectangle TURTLE_INFO_SHAPE = new Rectangle(275 ,75);
  private static final double SPACING = 10;
  private static final double MARGIN = 5;
  private static final double BOTTOM_INSET = 0.15;
  private static final double MENU_LABEL_SIZE = 20;
  private static final int NUM_TURTLE_MOVE_BUTTONS = 4;
  private static final double SMALLER_FONT_SIZE = 12;
  private static final double PEN_TEXT_WIDTH = 300;
  private static final Map<String, String> HELP_CATEGORIES = new HashMap<>(){{
    put("Basic Syntax", "Basic_Syntax");
    put("Turtle Commands", "Turtle_Commands");
    put("Turtle Queries", "Turtle_Queries");
    put("Math Operations", "Math");
    put("Boolean Operations", "Booleans");
    put("Variables, Control Structures, and User-Defined Commands", "Variables_Control_UDC");
    put("Display Commands", "Display_Commands");
    put("Multiple Turtles", "Multiple_Turtle_Commands");
  }}; //TODO: put this into the config file. A bit pointless though since all the help images are in english.
  private static final String[] BOTTOM_BUTTON_METHOD_NAMES = new String[]{"runButton", "clearButton", "undoButton", "redoButton"};
  private static final String[] BOTTOM_BUTTON_HOVER_NAMES = new String[]{"RunHover", "ClearHover", "UndoHover", "RedoHover"};
  private static final List<List<Integer>> BOTTOM_BUTTON_POSITIONS = List.of(List.of(0,0), List.of(0,1), List.of(1,0), List.of(1,1), List.of(2,1));
  private static final String[] TOP_RIGHT_BUTTON_METHODS = new String[]{"displayHelp", "setTurtleImage", "newWorkspace", "savePrefs","loadPrefs"};
  private static final String[] TOP_CENTER_BUTTON_METHODS = new String[]{"moveForward", "moveBackward", "rotateRight",
          "rotateLeft", "endPause", "setPause", "resetAnimation", "singleStep"};

  private static final String DEFAULT_HELP_CATEGORY_FILE = "Basic_Syntax";
  private static final double FPS = 24;
  private static final double MILLISECOND_DELAY = 1000/FPS;
  private static final double SIGNIFICANT_DIFFERENCE = 0.001;
  private static final double MIN_SPEED = 0.1;
  private static final double MAX_SPEED = 50;
  private static final double DEFAULT_SPEED = 1;
  private static final String LANGUAGE_INSTRUCTION_STRING = "language: ";
  private static final int PEN_SLIDER_TICKS = 10;
  private static final String DEFAULT_MOVE_BUTTON_VALUE = "45";
  public static final String XML_PREFS_FILE_NAME = "/saved.xml";
  private final int myFileNum;

  private ResourceBundle myLanguageResources;
  private ResourceBundle myWorkSpaceResources;
  private ResourceBundle myUserConfigurableResources;
  private final List<Image> imageList = new ArrayList<>() {{
    add(new Image(myResources.getString("DefaultTurtle")));
    add(new Image(myResources.getString("Duke")));
    add(new Image(myResources.getString("Duval")));
  }};
  private List<String> myMenuNames;
  private Map<String, Color> myColorPalette;
  private CommandBox myCommandBox;
  private History myHistory;
  private ClearableEntriesBox myUserDefinedCommands;
  private VariableBox myVariables;
  private TurtleView myTurtleView;
  private final ObservableList<String> myInstructionQueue;
  private Stage myStage;
  private VBox myLeftVBox;
  private VBox myCenterVBox;
  private VBox myRightVBox;
  private Text myErrorMessage;
  private Point2D myDesiredTurtlePosition;
  private Point2D myCurrentTurtlePosition = new Point2D(0, 0);
  private double xIncrement;
  private double yIncrement;
  private Point2D myStartPos = null;
  private boolean isReady = true;
  private boolean paused = false;
  private final Queue<CommandResult> resultQueue = new LinkedList<>();
  private List<String> undoCommandsIssued = new ArrayList<>();
  private int numUndoCommandsIssued = 0;
  private String myCurrentlyHighlighted = null;
  private String myCurrentInstruction = null;
  private Timeline animation;
  private List<TextArea> turtleMovementButtons = new ArrayList<>();
  private int myCurrentTurtleID;
  private Text myPenText;
  private TextFlow myTurtleInfo = new TextFlow();
  private MenuBar myMenuBar;
  private Consumer<Integer> myOnNewWorkSpaceClicked;
  private final DisplayableTextHolder myDisplayableTextHolder = new DisplayableTextHolder();
  private final String myStartingLanguage;
  private final int myStartingNumTurtles;
  private final int myStartingPenColor;
  private final int myStartingBackgroundColor;
  private final List<String> myScripts;
  private final List<String> myStartingVariables;
  private final int myStartingImage;
  private boolean clearedAtStart = true;
  private boolean clearScreenScheduled;

  /**
   * Constructor for the visualizer class, which manages the display components and state
   * @param instructionQueueListener listener for the instruction queue
   * @param onNewWorkSpaceClicked what happens when the New workspace button is clicked
   * @param configFileNum this indicates we will set defaults for this workspace using file workspaceX.properties
   *                      default to workspace 0 if file not found
   */
  public Visualizer(ListChangeListener<String> instructionQueueListener, Consumer<Integer> onNewWorkSpaceClicked, int configFileNum) {
    myInstructionQueue = new ObservableQueue();
    myInstructionQueue.addListener(instructionQueueListener);
    myOnNewWorkSpaceClicked = onNewWorkSpaceClicked;
    myFileNum = configFileNum;
    try {
      myWorkSpaceResources = ResourceBundle.getBundle("slogo/frontEnd/Resources.workspace" + configFileNum);
    } catch(MissingResourceException ex){
      myWorkSpaceResources = ResourceBundle.getBundle("slogo/frontEnd/Resources.workspace0");
    }
    myStartingLanguage = myWorkSpaceResources.getString("Language");
    myLanguageResources = ResourceBundle.getBundle(RESOURCE_LOCATION + myStartingLanguage + "config");
    try {
      myUserConfigurableResources = ResourceBundle.getBundle(RESOURCE_LOCATION + "UserConfigurable"+ configFileNum);
    } catch(MissingResourceException ex){
      myUserConfigurableResources = ResourceBundle.getBundle(RESOURCE_LOCATION + "UserConfigurable0");
    }
    myStartingNumTurtles = Integer.parseInt(myWorkSpaceResources.getString("numTurtles"));
    myStartingPenColor = Integer.parseInt(myWorkSpaceResources.getString("startingPenColor"));
    myStartingBackgroundColor = Integer.parseInt(myWorkSpaceResources.getString("startingBGColor"));
    myScripts = Arrays.asList(myWorkSpaceResources.getString("Scripts").split(","));
    myStartingVariables = Arrays.asList(myWorkSpaceResources.getString("Variables").split(","));
    myStartingImage = Integer.parseInt(myWorkSpaceResources.getString("startingImage"));
    setOriginalColorPalette();
  }
  private void setOriginalColorPalette() {
    String[] defaultColors = myUserConfigurableResources.getString("DefaultPalette").split(" ");
    int myPaletteSize = defaultColors.length;
    myColorPalette = new TreeMap<>(new SortByVal());
    for (String colorString : defaultColors){
      String[] parts = colorString.split(",");
      Color color = Color.rgb(Integer.parseInt(parts[1]),Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
      myColorPalette.put(parts[0], color);
    }
  }
  static class SortByVal implements Comparator<String> {
    public int compare(String a, String b)
    {
      return Integer.compare(Integer.parseInt(a), Integer.parseInt(b));
    }
  }

  @Override
  public void start(Stage primaryStage) {
    myStage = primaryStage;
    Scene display = setUpDisplay();
    myStage.setScene(display);
    myStage.setTitle(myLanguageResources.getString("AppTitle"));
    myStage.show();
  }

  /**
   * Pops the first element of the instruction queue, which contains strings that are either scripts taken directly
   *      from the command box, or special instructions generated by buttons which need to interact with model
   * Relevant Features:
   * React to the text and update the model
   * Choose a language in which slogo commands are understood (with a button/menu)
   * @return the instruction string, uninterpreted
   */
  public String popInstructionQueue(){
    return myInstructionQueue.remove(0);
  }

  /**
   * Takes in a command result for the visualizer to process (after all other queued command results finish)
   * @param result a CommandResult from the controller, OR null if this is called by the step function
   */
  public void processResult(CommandResult result){
    if(!isReady){
      if(result != null) {
        resultQueue.add(result);
        if(result.isMyScreenClear()){
          clearScreenScheduled = true;
        }
      }
    }
    else{
      isReady = false;
      if(result == null){
        result = resultQueue.poll();
      }
      assert result != null; // intellij wants us to do this but it's not really necessary
      dissectCommand(result);
    }
  }

  private void dissectCommand(CommandResult result) {
    Point2D startPos = null;
    if(result.getPathStart() != null){
      startPos = new Point2D(result.getPathStart().get(0), -result.getPathStart().get(1));
    }
    interpretResult(result.getTurtleHeading(), new Point2D(result.getTurtlePosition().get(0), -result.getTurtlePosition().get(1)),
            startPos, result.getVariables(), result.getUserDefinedCommands(), result.isMyScreenClear(),
            result.isMyPenUp(), result.isMyTurtleVisible(), result.getErrorMessage(), result.getMyOriginalInstruction(),
            result.getTurtleID(), result.getActiveTurtleIDs(), result.getPaletteIndex(), result.getPenColor(),
            result.getBackgroundColor(), result.getNewPaletteColor(), result.getShapeIndex(), result.getPenSize(),
            result.isUndo(), result.isRedo());
  }

  /**
   * Interpret result of CommandResults object, update everything that is updatable
   * Relevant Features:
   * React to the text and update the model
   * See the results of the turtle executing commands displayed visually
   * See resulting errors in user friendly way
   * see user defined commands currently available
   * @param turtleRotate new angle to set turtle to
   * @param turtlePos new coordinates for turtle
   * @param startPos start of path to draw
   * @param variables map of variable names and values
   * @param userDefinedCommands map of user defined command names and scripts
   * @param clearScreen whether or not the turtle view should be cleared
   * @param isPenUp whether or not the pen is up
   * @param turtleVisibility whether or not to show the turtle
   * @param originalInstruction the original instruction text that this command result corresponds to
   * @param errorMessage error message string, if any
   * @param activeTurtles list of active turtle IDs
   * @param turtleID which turtle this command is for
   * @param paletteIndex if a new color is being created, what is its index
   * @param penColorIndex color index to set the pen color to (for all turtles)
   * @param backgroundColorIndex color index to set the background color to
   * @param newColorRGB the rgb values for a new color that's being created
   * @param imageIndex what image to set all turtles to
   * @param penSize what thickness to set the pen to (for all turtles)
   * @param isUndoCommand is this command part of an undo instruction
   * @param isRedoCommand is this command part of a redo instruction
   */
  private void interpretResult(double turtleRotate, Point2D turtlePos, Point2D startPos, Map<String, Double> variables,
                               Map<String, String> userDefinedCommands, boolean clearScreen, boolean isPenUp,
                               boolean turtleVisibility, String errorMessage, String originalInstruction, int turtleID,
                               List<Integer> activeTurtles, int paletteIndex, int penColorIndex,
                               int backgroundColorIndex, List<Integer> newColorRGB, int imageIndex, double penSize,
                               boolean isUndoCommand, boolean isRedoCommand) {
    updateTurtleImage(imageIndex);
    handleUndoRedo(clearScreen, originalInstruction, isUndoCommand, isRedoCommand);
    myCurrentTurtleID = turtleID;
    if(!myTurtleView.getExistingTurtleIDs().contains(turtleID)){
      createTurtle(turtlePos, turtleID);
    }
    myTurtleView.activateTurtles(activeTurtles);
    for(int id : myTurtleView.getExistingTurtleIDs()){
      updateTurtleInfo(id);
    }
    updateTurtleDisplay(turtleRotate, turtlePos, startPos, turtleID);
    updateUserDefineInfo(variables, userDefinedCommands);
    clearTurtleView(clearScreen);
    myTurtleView.setTurtleVisibility(turtleVisibility, turtleID);
    myTurtleView.setIsPenUp(isPenUp);
    myTurtleView.setPenThickness(penSize);
    updateColors(paletteIndex, penColorIndex, backgroundColorIndex, newColorRGB);
    setPenText();
    highlightInHistory(originalInstruction, originalInstruction != myCurrentlyHighlighted);
    displayErrorMessage(errorMessage);
    myRightVBox.requestLayout(); // make sure everything is updated graphically
  }

  private void clearTurtleView(boolean isClearScreen) {
    if(isClearScreen) {
      myTurtleView.clearPaths();
      clearScreenScheduled = false;
    }
  }

  private void updateTurtleImage(int imageIndex) {
    Image image = imageList.get(imageIndex);
    if(myTurtleView.getTurtleImage() != image) {
      myTurtleView.setTurtleImage(image);
    }
  }

  private void highlightInHistory(String originalInstruction, boolean isHighlightingPrevious) {
    if (isHighlightingPrevious) {
      myHistory.highlightNext();
      myCurrentlyHighlighted = originalInstruction;
    }
  }

  private void handleUndoRedo(boolean clearScreen, String originalInstruction,
      boolean isUndoCommand, boolean isRedoCommand) {
    if(!undoCommandsIssued.isEmpty() && undoCommandsIssued.get(0) == originalInstruction) {
      undoCommandsIssued.remove(0);
      if (isUndoCommand) {
        myTurtleView.clearPaths();
        myTurtleView.clearTurtles();
        myTurtleView.incrementHistoryIndex(-1);
        setPathsAndTurtles();
      } else if (isRedoCommand) {
        myTurtleView.clearPaths();
        myTurtleView.clearTurtles();
        myTurtleView.incrementHistoryIndex(1);
        setPathsAndTurtles();
      }
    }
    else if(originalInstruction != myCurrentInstruction){
      myTurtleView.setPathCreateMode(true);
      myTurtleView.addTimelineElement(!clearScreen);
      myCurrentInstruction = originalInstruction;
      // trim everything in pathHistory beyond the current path history index
      // add a new empty list to pathHistory. Copy pathHistory[index] to it, unless clearscreen is true
    }
  }

  private void updateColors(int paletteIndex, int penColorIndex, int backgroundColorIndex,
      List<Integer> newColorRGB) {
    if (newColorRGB != null){
      updateColorMenus(paletteIndex, Color.rgb(newColorRGB.get(0), newColorRGB.get(1),newColorRGB.get(2) ) );
    }
    // nothing happens if the requested color is not in color palette
    if(myColorPalette.containsKey(Integer.toString(backgroundColorIndex))) {
      myTurtleView.setBackGroundColor(myColorPalette.get(Integer.toString(backgroundColorIndex)));
    }
    if(myColorPalette.containsKey(Integer.toString(penColorIndex))) {
      myTurtleView.setPenColor(myColorPalette.get(Integer.toString(penColorIndex)), penColorIndex);
    }
  }

  private void updateUserDefineInfo(Map<String, Double> variables,
      Map<String, String> userDefinedCommands) {
    for(Map.Entry<String, Double> variable : variables.entrySet()){
      addVariable(variable.getKey(), variable.getValue());
    }
    for(Map.Entry<String, String> UDC : userDefinedCommands.entrySet()){
      addUserDefinedCommand(UDC.getKey(), UDC.getValue());
    }
  }

  private void updateTurtleDisplay(double turtleRotate, Point2D turtlePos, Point2D startPos,
      int turtleID) {
    myTurtleView.setTurtleHeading(turtleRotate, turtleID);
    myDesiredTurtlePosition = turtlePos;
    myCurrentTurtlePosition = myTurtleView.getUnalteredTurtlePositions().get(turtleID);
    xIncrement = (myDesiredTurtlePosition.getX()-myCurrentTurtlePosition.getX())/FPS;
    yIncrement = (myDesiredTurtlePosition.getY()-myCurrentTurtlePosition.getY())/FPS;
    myStartPos = startPos;
  }

  private void setPathsAndTurtles() {
    myTurtleView.setPathCreateMode(false);
    myTurtleView.setDisplayedPathsAndTurtles();
    myTurtleInfo.getChildren().clear();
    for(int id : myTurtleView.getExistingTurtleIDs()){
      myTurtleInfo.getChildren().add(new Text(buildTurtleInfoString(id)));
    }
  }

  private void updateColorMenus(int paletteIndex, Color newColor) {
    myColorPalette.put(Integer.toString(paletteIndex), newColor);
    addMenuItem(MENU_TYPES.indexOf("Background"),  Integer.toString(paletteIndex));
    addMenuItem(myMenuNames.indexOf("PenColor"), Integer.toString(paletteIndex));
  }

  private void createTurtle(Point2D turtlePos, int turtleID) {
    myTurtleView.makeTurtle(turtleID, this::activateTurtle);
    myTurtleView.getUnalteredTurtlePositions().put(turtleID, turtlePos);
    myTurtleInfo.getChildren().add(new Text(buildTurtleInfoString(myCurrentTurtleID)));
  }

  private String buildTurtleInfoString(int turtleID){
    String[] activityAndHeading = myTurtleView.getTurtleInfo(turtleID);
    String turtleString = myLanguageResources.getString("Turtle");
    String activeString = myLanguageResources.getString("Active");
    String positionString = myLanguageResources.getString("Position");
    String headingString = myLanguageResources.getString("Heading");
    return turtleString + " " + turtleID + ": \n" + activeString + ": " +
            activityAndHeading[0] + "  " + positionString + ": ("
            + String.format("%.2f", myTurtleView.getUnalteredTurtlePositions().get(turtleID).getX()) + ","
            + String.format("%.2f", -myTurtleView.getUnalteredTurtlePositions().get(turtleID).getY())
            + ")  " + headingString + ": " + activityAndHeading[1] + "\n";
  }

  private Scene setUpDisplay() {
    Group myRoot = new Group();
    HBox myLayout = new HBox(SPACING * 2);

    myLayout.setMaxSize(WIDTH, HEIGHT);
    myLayout.setMinSize(WIDTH,HEIGHT);

    myLeftVBox = new VBox(SPACING);
    myLeftVBox.setMaxSize(WIDTH/2, HEIGHT);
    myLeftVBox.setMinSize(myLeftVBox.getMaxWidth(), myLeftVBox.getMaxHeight());

    myRightVBox = new VBox(SPACING);
    myRightVBox.setMaxSize(WIDTH/3, HEIGHT);

    setUpLeftPane();
    setUpCenterPane();
    setUpRightPane();

    KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> {
      try {
        step(false);
      } catch (Exception ex) {
        // note that this should ideally never be thrown
        showError("Animation Error", myLanguageResources);
        myErrorMessage.setText(myLanguageResources.getString("IOError"));
      }
    });

    myLayout.getChildren().addAll(myLeftVBox,myCenterVBox,myRightVBox);
    HBox.setMargin(myLeftVBox, new Insets(SPACING, 0, 0, MARGIN));
    HBox.setMargin(myRightVBox, new Insets(SPACING,MARGIN,0,0));
    myLayout.setStyle("-fx-border-color: black");
    myRoot.getChildren().add(myLayout);
    animation = new Timeline();
    animation.setCycleCount(Timeline.INDEFINITE);
    animation.getKeyFrames().add(frame);
    animation.play();

    setUpDefaults();

    return new Scene(myRoot, WIDTH, HEIGHT , BACKGROUND);
  }

  /**
   * execute instructions to make the starting setup much the setup specified by the workspace config file
   * note that we don't use the config file because we change the language at the last step
   */
  private void setUpDefaults(){
    executeInstruction("setbackground " + myStartingBackgroundColor);
    executeInstruction("setpencolor " + myStartingPenColor);
    StringBuilder instruction = new StringBuilder("tell" + " [ ");
    for(int id=0; id<myStartingNumTurtles; id++){
      instruction.append(id).append(" ");
    }
    instruction.append("]");
    executeInstruction(instruction.toString());
    executeInstruction("setshape " + myStartingImage);

    loadNewUserProperties(myFileNum, true);

    myInstructionQueue.add(LANGUAGE_INSTRUCTION_STRING + myStartingLanguage);
    // now schedule clear history so the user isn't confused by the commands we used to set up defaults
    clearedAtStart = false;
  }

  private void setUpCenterPane() {
    myCenterVBox = new VBox(SPACING);
    myCenterVBox.setPrefHeight(HEIGHT);
    setUpTopCenterButtons();
    setUpBottomButtons();
    myCenterVBox.setAlignment(Pos.BOTTOM_CENTER);
    int lastIndex = myCenterVBox.getChildren().size();
    VBox.setMargin(myCenterVBox.getChildren().get(lastIndex-1), new Insets(0,0,HEIGHT * BOTTOM_INSET,0));
  }

  private void setUpLeftPane() {
    setUpMenus();
    myTurtleView = new TurtleView(TURTLE_VIEW_SHAPE.getWidth(), TURTLE_VIEW_SHAPE.getHeight());
    createTurtle(new Point2D(0,0), 0);
    myErrorMessage = new Text(myLanguageResources.getString("DefaultErrorMessage"));
    myErrorMessage.setFill(Color.RED);
    myDisplayableTextHolder.addText(myErrorMessage, "DefaultErrorMessage");
    myCommandBox = new CommandBox(COMMAND_BOX_SHAPE, myLanguageResources);
    myLeftVBox.getChildren().addAll(myTurtleView, myErrorMessage, myCommandBox);
  }

  private void activateTurtle(boolean dummy) {
    StringBuilder instruction = new StringBuilder(myLanguageResources.getString("tell") + " [ ");
    for(int id : myTurtleView.getActiveTurtles()){
      instruction.append(id).append(" ");
    }
    instruction.append("]");
    executeInstruction(instruction.toString());
  }

  private void setUpRightPane() {
    setUpTopButtons();
    myHistory = new History(HISTORY_VIEW_SHAPE, CLEAR_HISTORY_BUTTON_SHAPE, "HistoryLabel", myLanguageResources);
    myUserDefinedCommands = new ClearableEntriesBox(UDC_VIEW_SHAPE, CLEAR_UDC_BUTTON_SHAPE, "UDCLabel", myLanguageResources);
    myVariables = new VariableBox(VARIABLES_VIEW_SHAPE, CLEAR_VARIABLES_BUTTON_SHAPE,"VariablesLabel", myLanguageResources);
    myPenText = new Text();
    myPenText.setFont(new Font(SMALLER_FONT_SIZE));
    myPenText.setWrappingWidth(PEN_TEXT_WIDTH);
    setPenText();
    myTurtleInfo.setPrefSize(TURTLE_INFO_SHAPE.getWidth(), TURTLE_INFO_SHAPE.getHeight());
    myTurtleInfo.setMaxSize(Control.USE_PREF_SIZE, Control.USE_PREF_SIZE);
    ScrollPane turtleInfoPane = new ScrollPane();
    turtleInfoPane.setContent(myTurtleInfo);
    turtleInfoPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    turtleInfoPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    myRightVBox.getChildren().addAll(myHistory, myUserDefinedCommands, myVariables, myPenText, turtleInfoPane);
  }

  private void setPenText(){
    String[] penState = myTurtleView.getPenState();
    double thick = Double.parseDouble(penState[2]);
    String penThick = String.format("%.2f", thick);
    String penUpString = myLanguageResources.getString("PenUpString");
    String penColorString = myLanguageResources.getString("PenColorString");
    String penThicknessString = myLanguageResources.getString("PenThicknessString");
    myPenText.setText(penUpString + ": " + penState[0] + " " + penColorString + ": " + penState[1] + " " +
            penThicknessString + ": " + penThick);
  }

  private void endPause(){
    paused = false;
  }
  private void setPause(){
    paused = true;
  }

  private void singleStep(){
    step(true);
  }
  private void setUpTopCenterButtons() {
    List<Button> buttons = new ArrayList<>();
    for(String buttonName : TOP_CENTER_BUTTON_METHODS){
      Button button = makeButton(buttonName, TURTLE_BUTTON_SHAPE, this, myLanguageResources);
      buttons.add(button);
      myDisplayableTextHolder.addButton(button, buttonName);
    }
    makeTurtleMoveButtons(buttons);
    Slider speedSlider = new Slider(MIN_SPEED, MAX_SPEED, DEFAULT_SPEED);
    speedSlider.valueProperty().addListener((ov, old_val, new_val) -> animation.setRate(speedSlider.getValue()));
    speedSlider.setShowTickMarks(true);
    speedSlider.setShowTickLabels(true);
    Text sliderLabel = new Text(myLanguageResources.getString("AnimationSpeed"));
    myDisplayableTextHolder.addText(sliderLabel, "AnimationSpeed");
    sliderLabel.setUnderline(true);
    Slider penSlider = makePenSlider();
    Text penSliderLabel = new Text(myLanguageResources.getString("PenThicknessString"));
    myDisplayableTextHolder.addText(penSliderLabel, "PenThicknessString");
    penSliderLabel.setUnderline(true);
    myCenterVBox.getChildren().addAll(sliderLabel, speedSlider, penSliderLabel, penSlider);
  }

  private void makeTurtleMoveButtons(List<Button> buttons) {
    for(int i=0; i<NUM_TURTLE_MOVE_BUTTONS; i++){
      HBox hbox = new HBox(SPACING);
      TextArea valueSetter = new TextArea(DEFAULT_MOVE_BUTTON_VALUE);
      valueSetter.setMaxSize(TURTLE_MOVEMENT_LABEL_SHAPE.getWidth(), TURTLE_MOVEMENT_LABEL_SHAPE.getHeight());
      turtleMovementButtons.add(valueSetter);
      hbox.getChildren().addAll(buttons.get(i), valueSetter);
      myCenterVBox.getChildren().add(hbox);
    }
    for(Button button : buttons.subList(NUM_TURTLE_MOVE_BUTTONS, buttons.size())){
      myCenterVBox.getChildren().add(button);
    }
  }

  private Slider makePenSlider() {
    Slider penSlider = new Slider(MIN_SPEED, MAX_SPEED, DEFAULT_SPEED);
    penSlider.valueProperty().addListener((ov, old_val, new_val) -> {
      myTurtleView.setPenThickness(penSlider.getValue()); //TODO: keep an eye on this
      setPenText();
      myRightVBox.requestLayout(); // make sure everything is updated graphically
    });
    // update the displayed value as slider is dragged, but only send command to change it when slider is dropped
    penSlider.valueChangingProperty().addListener((val, wasChanging, changing) -> {
      if(wasChanging) {
        executeInstruction(myLanguageResources.getString("setPenSize") + " " + String
            .format("%.2f", penSlider.getValue()));
      }});
    penSlider.setMinorTickCount(PEN_SLIDER_TICKS);
    penSlider.setShowTickMarks(true);
    penSlider.setShowTickLabels(true);
    return penSlider;
  }

  private void setUpTopButtons() {
    GridPane topButtons = new GridPane();
    topButtons.setVgap(SPACING/2);
    topButtons.setHgap(SPACING);
    for(int i=0; i<TOP_RIGHT_BUTTON_METHODS.length; i++){
      Button button = makeButton(TOP_RIGHT_BUTTON_METHODS[i], TOP_RIGHT_BUTTON_SHAPE, this, myLanguageResources);
      topButtons.add(button, BOTTOM_BUTTON_POSITIONS.get(i).get(0), BOTTOM_BUTTON_POSITIONS.get(i).get(1));
      myDisplayableTextHolder.addButton(button, TOP_RIGHT_BUTTON_METHODS[i]);
    }
    myRightVBox.getChildren().add(topButtons);
  }

  protected static Button makeButton(String text, Rectangle shape, Object clazz, ResourceBundle languageResources){
    Method method = null;
    try {
      method = clazz.getClass().getDeclaredMethod(text);
    }
    catch (NoSuchMethodException e) {
      showError(e.getMessage(), languageResources);
    }
    Button button = new Button(languageResources.getString(text));
    button.setLayoutY(shape.getY());
    button.setLayoutX(shape.getX());
    button.setMinSize(shape.getWidth(), shape.getHeight());
    Method finalMethod = method;
    button.setOnAction(event -> {
      try {
        assert finalMethod != null;
        finalMethod.invoke(clazz);
      } catch (IllegalAccessException | InvocationTargetException | NullPointerException e) {
        showError(e.getMessage(), languageResources);
      }
    });
    return button;
  }

  private void moveForward(){
    executeInstruction(myLanguageResources.getString("fd") + " " + turtleMovementButtons.get(0).getText());
  }

  private void moveBackward(){
    executeInstruction(myLanguageResources.getString("bk") + " " + turtleMovementButtons.get(1).getText());
  }

  private void rotateRight(){
    executeInstruction(myLanguageResources.getString("rt") + " " + turtleMovementButtons.get(2).getText());
  }

  private void rotateLeft(){
    executeInstruction(myLanguageResources.getString("lt") + " " + turtleMovementButtons.get(3).getText());
  }

  private void resetAnimation() {
    executeInstruction(myLanguageResources.getString("clearscreen") + "");
  }

  private void newWorkspace(){
    myOnNewWorkSpaceClicked.accept(0);
  }

  private void undoButton(){
    numUndoCommandsIssued++;
    String instruction = myLanguageResources.getString("undo") + "";
    undoCommandsIssued.add(instruction);
    executeInstruction(instruction);
  }

  private void redoButton(){
    if(numUndoCommandsIssued > 0) {
      numUndoCommandsIssued--;
      String instruction = myLanguageResources.getString("redo") + "";
      undoCommandsIssued.add(instruction);
      executeInstruction(instruction);
    }
  }

  private void setPenColor(String colorIndex){
    executeInstruction(myLanguageResources.getString("setpencolor") + " " + colorIndex);
    //myTurtleView.setPenColor(myColorPalette.get(colorIndex), Integer.parseInt(colorIndex));
    setPenText();
  }

  private void setPenUp(String menuName){
    executeInstruction(myLanguageResources.getString(menuName) + ""); // need the blank string so it registers as a new distinct string object
  }

  private void setBackGroundColor(String colorIndex){
    executeInstruction(myLanguageResources.getString("setbackground") + " " + colorIndex);
    //myTurtleView.setBackGroundColor(myColorPalette.get(colorIndex));
  }

  /**
   * changes the language. doesn't use executeInstruction because we don't want this showing up in history, since no
   *    command results will be returned
   * @param language the language to change to, IN ENGLISH
   */
  private void setLanguage(String language){
    setDisplayableTexts(language);
    myInstructionQueue.add(LANGUAGE_INSTRUCTION_STRING + language);
  }

  private void setTurtleImageIndex(String num){
    //Image image = imageList.get(Integer.parseInt(num));
    //myTurtleView.setTurtleImage(image);
    executeInstruction(myLanguageResources.getString("setshape") + " " + Integer.parseInt(num));
  }

  private void runButton(){
    String instruction = myCommandBox.getContents() + "";
    executeInstruction(instruction);
  }

  private void clearButton(){
    myCommandBox.clearContents();
  }

  private Node getColorLabel(String index){
    return new Rectangle(MENU_LABEL_SIZE, MENU_LABEL_SIZE, myColorPalette.get(index));
  }

  private Node getTurtleImageLabel(String index){
    ImageView imageView = new ImageView(imageList.get(Integer.parseInt(index)));
    imageView.setFitHeight(MENU_LABEL_SIZE);
    imageView.setFitWidth(MENU_LABEL_SIZE);
    return imageView;
  }

  @SuppressWarnings("SameReturnValue")
  private Node getLanguageLabel(String irrelevant){
    return null;
  }

  @SuppressWarnings("SameReturnValue")
  private Node getPenUpLabel(String irrelevant){
    return null;
  }

  /**
   * this method updates all displayable text to the new language. It tries to use the DisplayableTextHolder as much
   *  as possible but it is difficult to do for some items, like those with text that needs to be assembled from pieces
   * @param language the language to translate to (language name must be in english)
   */
  private void setDisplayableTexts(String language){
    myLanguageResources = ResourceBundle.getBundle("slogo/frontEnd/Resources." + language + "config");
    myDisplayableTextHolder.setDisplayableTexts(myLanguageResources);
    setPenText();
    for(int id : myTurtleView.getExistingTurtleIDs()){
      updateTurtleInfo(id);
    }
    myStage.setTitle(myLanguageResources.getString("AppTitle"));
    myHistory.setDisplayableTexts(myLanguageResources);
    myVariables.setDisplayableTexts(myLanguageResources);
    myUserDefinedCommands.setDisplayableTexts(myLanguageResources);
    myCommandBox.setDisplayableTexts(myLanguageResources);
  }

  protected static boolean isNonNumeric(String str) {
    return !str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
  }

  private void setUpMenus(){
    myMenuNames = Arrays.asList(myLanguageResources.getString("MenuNames").split(","));
    List<List<String>> myMenuOptions = new ArrayList<>();
    for(String menuType : MENU_TYPES){
      myMenuOptions.add(Arrays.asList(myWorkSpaceResources.getString(menuType+"Options").split(",")));
    }
    int penIndex = MENU_TYPES.indexOf("PenColor");
    int backIndex = MENU_TYPES.indexOf("Background");
    List<String> colorIndices = new ArrayList<>();
    colorIndices.addAll(myColorPalette.keySet());
    myMenuOptions.set(penIndex,colorIndices);
    myMenuOptions.set(backIndex,colorIndices);
    myMenuBar = new MenuBar();
    myLeftVBox.getChildren().add(myMenuBar);
    for(int i=0; i<myMenuNames.size(); i++){
      Menu menu = new Menu(myMenuNames.get(i));
      myMenuBar.getMenus().add(menu);
      myDisplayableTextHolder.addMenu(menu, MENU_TYPES.get(i));
      for(String entry : myMenuOptions.get(i)){
        addMenuItem(i, entry);
      }
    }
  }

  private void addMenuItem(int menuNameIndex, String menuItemName){
    Menu menu = myMenuBar.getMenus().get(menuNameIndex);
    String menuType = MENU_TYPES.get(menuNameIndex);
    String menuItemNameTranslation = menuItemName;
    if(isNonNumeric(menuItemName)) {
      menuItemNameTranslation = myLanguageResources.getString(menuItemName);
    }
    MenuItem menuItem = new MenuItem(menuItemNameTranslation);
    myDisplayableTextHolder.addMenuItem(menuItem, menuItemName);
    String methodName = myResources.getString(menuType);
    String labelGetterName = myResources.getString(menuType + "Label");
    // get another method name that will give us the label corresponding to this menu name
    // the method should return a node object
    try {
      Method method = this.getClass().getDeclaredMethod(methodName, String.class);
      Method labelGetter = this.getClass().getDeclaredMethod(labelGetterName, String.class);
      menuItem.setGraphic((Node) labelGetter.invoke(this, menuItemName));
      menuItem.setOnAction(event -> {
        try {
          method.invoke(this, menuItemName);
        } catch (IllegalAccessException | InvocationTargetException e) {
          showError(myLanguageResources.getString("InvokeError"), myLanguageResources);
        }
      });
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      showError(myLanguageResources.getString("NoMethodError"), myLanguageResources);
    }
    String finalMenuItemNameTranslation = menuItemNameTranslation; // intellij makes us put this in a variable. Don't delete it.
    menu.getItems().removeIf(oldMenuItem -> oldMenuItem.getText().equals(finalMenuItemNameTranslation));
    menu.getItems().add(menuItem);
  }

  private void setTurtleImage() {
    final FileChooser fileChooser = new FileChooser();
    File file = fileChooser.showOpenDialog(myStage);
    if(file != null) {
      try {
        BufferedImage buffImage = ImageIO.read(file);
        WritableImage fximage = new WritableImage(buffImage.getWidth(), buffImage.getHeight());
        Image image = SwingFXUtils.toFXImage(buffImage, fximage);
        imageList.add(image);
        addMenuItem(myMenuNames.indexOf(myLanguageResources.getString("TurtleImageMenu")), Integer.toString(imageList.size()-1));
        setTurtleImageIndex(Integer.toString(imageList.size()-1));
      } catch (IOException | NullPointerException ex) {
        showError(myLanguageResources.getString("LoadTurtle"), myLanguageResources);
      }
    }
  }

  protected static void showError(String message, ResourceBundle languageResources) {
    Alert alert = new Alert(AlertType.ERROR);
    alert.setTitle(languageResources.getString("IOError"));
    alert.setContentText(message);
    alert.showAndWait();
  }

  private void setUpBottomButtons() {
    GridPane buttonGrid = new GridPane();
    buttonGrid.setHgap(SPACING);
    buttonGrid.setVgap(SPACING);
    for(int i=0; i<BOTTOM_BUTTON_METHOD_NAMES.length; i++){
      Button button = makeButton(BOTTOM_BUTTON_METHOD_NAMES[i], RUN_BUTTON_SHAPE, this, myLanguageResources);
      button.setTooltip(new Tooltip(myLanguageResources.getString(BOTTOM_BUTTON_HOVER_NAMES[i])));
      buttonGrid.add(button, BOTTOM_BUTTON_POSITIONS.get(i).get(0), BOTTOM_BUTTON_POSITIONS.get(i).get(1));
      myDisplayableTextHolder.addButton(button, BOTTOM_BUTTON_METHOD_NAMES[i]);
    }
    myCenterVBox.getChildren().add(buttonGrid);
  }

  private void initialHistoryClear(){
    myCurrentlyHighlighted = null;
    myHistory.clearEntryBox();
    myErrorMessage.setText(myLanguageResources.getString("DefaultErrorMessage"));
    clearedAtStart = true;
  }

  private void step(boolean overridePause){
    if(!clearedAtStart && isReady){
      initialHistoryClear();
      // clear the history after setting up defaults
    }
    if(clearScreenScheduled){
      myCurrentTurtlePosition = myDesiredTurtlePosition;
      // skip the animations if a clear screen command is coming (so that reset can interrupt animation)
    }
    if(!paused || overridePause) {
      moveTurtle();
    }
  }

  private void moveTurtle() {
    boolean isYNotAlmostEqual = notAlmostEqual(myCurrentTurtlePosition.getY(), myDesiredTurtlePosition.getY());
    boolean isXNotAlmostEqual = notAlmostEqual(myCurrentTurtlePosition.getX(), myDesiredTurtlePosition.getX());
    if (myDesiredTurtlePosition != null && (isXNotAlmostEqual || isYNotAlmostEqual)) {
      myCurrentTurtlePosition = new Point2D(myCurrentTurtlePosition.getX() + xIncrement, myCurrentTurtlePosition.getY() + yIncrement);
      myTurtleView.getUnalteredTurtlePositions().put(myCurrentTurtleID, myCurrentTurtlePosition);
      myTurtleView.setTurtlePosition(myCurrentTurtlePosition.getX(), myCurrentTurtlePosition.getY(), myCurrentTurtleID);
      if (myStartPos != null) {
        myTurtleView.addPath(myStartPos, myCurrentTurtlePosition);
        myStartPos = myCurrentTurtlePosition;
      }
      updateTurtleInfo(myCurrentTurtleID);
    } else if (!isReady) {
      isReady = true;
      if (!resultQueue.isEmpty()) {
        processResult(null);
      }
    }
  }

  private boolean notAlmostEqual(double a, double b){
    return Math.abs(a - b) >= SIGNIFICANT_DIFFERENCE;
  }

  private void updateTurtleInfo(int id) {
    Text turtleInfo = (Text) myTurtleInfo.getChildren().get(myTurtleView.getExistingTurtleIDs().indexOf(id));
    String[] activityAndHeading = myTurtleView.getTurtleInfo(id);
    turtleInfo.setText(buildTurtleInfoString(id));
  }

  private void displayErrorMessage(String message){
    myErrorMessage.setText(message);
  }

  private void addVariable(String name, double value){
    myVariables.addEntry(name + " : " + value, name, newValue->executeInstruction(myLanguageResources.getString("make")
            + " :"+name+" "+newValue));
  }

  private void addUserDefinedCommand(String name, String command){
    myUserDefinedCommands.addEntry(name + ":\n" + command, name, e->myCommandBox.setText(name));
  }

  private void executeInstruction(String instruction) {
    myHistory.addEntry(instruction, null, e->myCommandBox.setText(instruction));
    highlightInHistory(instruction, instruction != myCurrentlyHighlighted && isReady);
    myRightVBox.requestLayout(); // make sure etopverything is updated graphically
    myInstructionQueue.add(instruction);
  }

  private void displayHelp(){
    Stage stage = new Stage();
    stage.setTitle(myLanguageResources.getString("HelpWindowTitle"));
    VBox vBox = new VBox(SPACING);
    MenuBar menuBar = new MenuBar();
    vBox.getChildren().add(menuBar);
    Menu menu = new Menu(myLanguageResources.getString("HelpMenu"));
    menuBar.getMenus().add(menu);
    for(Map.Entry<String, String> helpCategory : HELP_CATEGORIES.entrySet()){
      MenuItem menuItem = new Menu(helpCategory.getKey());
      menuItem.setOnAction(event -> changeHelpImage(helpCategory.getValue(), vBox));
      menu.getItems().add(menuItem);
    }
    vBox.getChildren().add(new ImageView("slogo/frontEnd/Resources/" + DEFAULT_HELP_CATEGORY_FILE + ".png"));
    ScrollPane scrollPane = new ScrollPane();
    scrollPane.setContent(vBox);
    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    stage.setScene(new Scene(scrollPane, HELP_WINDOW_SHAPE.getWidth(), HELP_WINDOW_SHAPE.getHeight()));
    stage.show();
  }

  private void changeHelpImage(String imageName, VBox vBox){
    vBox.getChildren().remove(1);
    vBox.getChildren().add(new ImageView("slogo/frontEnd/Resources/" + imageName + ".png"));
  }
  private void loadNewUserProperties(int fileNum, boolean isStartUp){
    if (isStartUp){
      executeInstruction("Load " + "src/" + RESOURCE_LOCATION + XML_PREFS_FILE_NAME);
    }
    else {
      FileChooser fileChooser = new FileChooser();
      File file = fileChooser.showOpenDialog(myStage);
      String filePath = "";
      try {
        filePath = file.getCanonicalPath();
      } catch (IOException e) {
        showError(e.getMessage(), myLanguageResources);
      }
      executeInstruction("Load " + filePath);
    }
  }
  private void loadPrefs(){
      loadNewUserProperties(myFileNum, false);
  }
  private void savePrefs(){
    String instruction = "Save src/" + RESOURCE_LOCATION;
    String inst = instruction.substring(0,instruction.length() -1);
    executeInstruction(inst + XML_PREFS_FILE_NAME);
  }
}