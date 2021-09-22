package slogo.frontEnd;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

/**
 * The TurtleView class encapsulated the view of the turtle and allows for the current state of the
 * Turtle and its taken paths to be displayed to the user.
 */
public class TurtleView extends Group{
    private static final String RESOURCE_LOCATION = "slogo/frontEnd/Resources.config";
    private static final ResourceBundle myResources = ResourceBundle.getBundle(RESOURCE_LOCATION);
    private static final Image myActiveTurtleImage = new Image(myResources.getString("DefaultTurtle"));
    private static final double TURTLE_SIZE = 50;
    private static final double SIGNIFICANT_DIFFERENCE = 0.001;
    private static final double NOT_HIGHLIGHTED_OPACITY = 0.4;
    private static final double HIGHLIGHTED_OPACITY = 1.0;
    private static final double MIN_PEN_SIZE = 0.1;
    private static final double MAX_PEN_SIZE = 50.0;

    private final Map<Integer, Turtle> myTurtles = new HashMap<>();
    private final Map<Integer, Point2D> unalteredTurtlePositions = new HashMap<>();
    private final List<List<Integer>> existingTurtleTimeline = new ArrayList<>(){{add(new ArrayList<>());}};
    private final List<List<Path>> pathsTimeline = new ArrayList<>(){{add(new ArrayList<>());}};
    private int currentTimelineIndex = 0;
    private boolean pathCreateMode = true;
    private Color myPenColor = Color.BLACK;
    private int myPenColorIndex;
    private double myPenThickness = 1;
    private final Rectangle myBackground;
    private boolean isPenUp = false;
    private final double myWidth;
    private final double myHeight;
    private final double xOffset;
    private final double yOffset;

    public TurtleView(double width, double height){
        myWidth = width - TURTLE_SIZE;
        myHeight = height - TURTLE_SIZE;
        xOffset = myWidth/2 - TURTLE_SIZE/2;
        yOffset = myHeight/2 - TURTLE_SIZE/2;
        myBackground = new Rectangle(width, height);
        myBackground.setFill(Color.WHITE);
        myBackground.setStroke(Color.BLACK);
        myBackground.setStrokeType(StrokeType.OUTSIDE);
        myBackground.setStyle("-fx-border-color: black");
        this.getChildren().add(myBackground);
    }

    protected void makeTurtle(int id, Consumer<Boolean> onClicked){
        Turtle myTurtle = new Turtle(myActiveTurtleImage);
        myTurtle.setPreserveRatio(true);
        myTurtle.setCache(true);
        myTurtle.setFitWidth(TURTLE_SIZE);
        myTurtle.setFitHeight(TURTLE_SIZE);
        myTurtles.put(id, myTurtle);
        resetTurtle(id);
        myTurtle.setOnMouseClicked(event -> toggleActive(id, onClicked));
        this.getChildren().add(myTurtle);
        existingTurtleTimeline.get(currentTimelineIndex).add(id);
    }

    /**
     * increments or decrements the path history index. Limits it to list range bounds.
     * @param value what to add to the index
     */
    protected void incrementHistoryIndex(int value){
        currentTimelineIndex += value;
        assert currentTimelineIndex >= 0;
        assert currentTimelineIndex <= pathsTimeline.size()-1;
    }

    /**
     * we need this so that we don't New paths when we are undoing or redoing
     * @param value whether path create mode is on
     */
    protected void setPathCreateMode(boolean value){
        pathCreateMode = value;
    }

    /**
     * defines procedure for adding a new entry to the paths history. First we must trim everything past the current
     *      index to prevent the user from redoing after a non-undo command
     * @param copyPrevious whether or not to maintain the current state or start fresh
     */
    protected void addTimelineElement(boolean copyPrevious){
        if(currentTimelineIndex < pathsTimeline.size()-1) {
            pathsTimeline.subList(currentTimelineIndex + 1, pathsTimeline.size()).clear();
            existingTurtleTimeline.subList(currentTimelineIndex + 1, existingTurtleTimeline.size()).clear();
        }
        if(copyPrevious) {
          pathsTimeline.add(new ArrayList<>(pathsTimeline.get(currentTimelineIndex)));
        }
        else{
          pathsTimeline.add(new ArrayList<>());
        }
        existingTurtleTimeline.add(new ArrayList<>(existingTurtleTimeline.get(currentTimelineIndex)));
        currentTimelineIndex++;
    }

    /**
     * Removes all of the turtles displayed on the screen
     */
    protected void clearTurtles() {
        this.getChildren().removeIf(node -> node.getClass().equals(Turtle.class));
    }

    /**
     * Removes all of the taken paths displayed on the screen
     */
    protected void clearPaths(){
        this.getChildren().removeIf(node -> node.getClass().equals(Path.class));
    }

    /**
     * set the displayed paths and turtles to what's at the current timeline index
     */
    protected void setDisplayedPathsAndTurtles(){
        this.getChildren().addAll(pathsTimeline.get(currentTimelineIndex));
        for(Integer id : existingTurtleTimeline.get(currentTimelineIndex)){
            this.getChildren().add(myTurtles.get(id));
        }
    }

    /**
     * get the list of actual turtle coordinates that have not been altered to fit the screen
     * @return map of turtle ids to positions
     */
    protected Map<Integer, Point2D> getUnalteredTurtlePositions(){
        return unalteredTurtlePositions;
    }

    /**
     * give the visualizer a list of existing turtle ids
     * @return list of ids
     */
    protected List<Integer> getExistingTurtleIDs(){
        return existingTurtleTimeline.get(currentTimelineIndex);
    }

    /**
     * set the pen color (color of paths)
     * @param color color to set to
     */
    protected void setPenColor(Color color, int index){
        myPenColor = color;
        myPenColorIndex = index;
    }

    /**
     * Updates the position of the turtle in the Display to the desired set of coordinates. Offsets so that 0, 0 is center of screen
     * @param x the new x coordinate for the turtle
     * @param y the new y coordinate for the turtle
     */
    protected void setTurtlePosition(double x, double y, int id){
        myTurtles.get(id).setX(boundX(x));
        myTurtles.get(id).setY(boundY(y));
    }

    /**
     * Rotate the turtle
     * @param angle angle to rotate by
     */
    protected void setTurtleHeading(double angle, int id){
        myTurtles.get(id).setRotate(angle);
    }

    /**
     * Change the background color of the turtleView section of the display to a desired color.
     * Will be controlled through a lambda from a drop-down menu
     * @param color the desired color for the background
     */
    protected void setBackGroundColor(Color color){
        myBackground.setFill(color);
    }

    /**
     * This method tells the TurtleView it must add a new path to the display by drawing it, in the color
     * specified by the Path object
     * @param startPos the turtle previous location
     * @param turtlePos the turtle current position
     */
    protected void addPath(Point2D startPos, Point2D turtlePos){
        if(!isPenUp && pathCreateMode) {

            double turtleX = boundX(turtlePos.getX()) + TURTLE_SIZE/2;
            double turtleY = boundY(turtlePos.getY()) + TURTLE_SIZE/2;
            double startX = boundX(startPos.getX())+TURTLE_SIZE/2;
            double startY = boundY(startPos.getY())+TURTLE_SIZE/2;
            if(notAlmostEqual(turtleX - startX, turtlePos.getX() - startPos.getX()) || notAlmostEqual(turtleY - startY, turtlePos.getY() - startPos.getY())){
                return; // don't draw the path if the turtle is wrapping around in this step
            }
            MoveTo moveTo = new MoveTo(turtleX , turtleY);
            LineTo line = new LineTo(startX, startY);
            Path path = new Path();
            path.getElements().add(moveTo);
            path.getElements().add(line);
            path.setStroke(myPenColor);
            path.setStrokeWidth(myPenThickness);
            this.getChildren().add(path);
            pathsTimeline.get(currentTimelineIndex).add(path);
        }
    }

    /**
     * change the turtle image. Image is determined by the file chooser
     * @param newTurtleImage image object to set the turtle image to
     */
    protected void setTurtleImage(Image newTurtleImage){
        for(Turtle turtle : myTurtles.values()) {
            turtle.setImage(newTurtleImage);
        }
    }

    protected void setTurtleVisibility(boolean turtleVisibility, int id) {
        Turtle myTurtle = myTurtles.get(id);
        myTurtle.setVisible(turtleVisibility);
    }

    /**
     * set the pen status (up/down)
     * @param up true if the pen is up
     */
    protected void setIsPenUp(boolean up){
        isPenUp = up;
    }

    protected void setPenThickness(double value) {
        assert value <= MAX_PEN_SIZE;
        assert value >= MIN_PEN_SIZE;
        myPenThickness = value;
    }

    /**
     * get a list of ids for the active turtles
     * @return list of ids for the active turtles
     */
    protected List<Integer> getActiveTurtles(){
        List<Integer> activeTurtles = new ArrayList<>();
        for(Map.Entry<Integer, Turtle> turtleEntry : myTurtles.entrySet()){
            if(turtleEntry.getValue().isActive()){
              activeTurtles.add(turtleEntry.getKey());
            }
        }
        return activeTurtles;
    }

    protected void activateTurtles(List<Integer> activeTurtles) {
        for(Turtle turtle : myTurtles.values()){
            turtle.setActive(false);
            turtle.setOpacity(NOT_HIGHLIGHTED_OPACITY);
        }
        for(int id : activeTurtles){
            if(myTurtles.containsKey(id)) {
                myTurtles.get(id).setActive(true);
                myTurtles.get(id).setOpacity(HIGHLIGHTED_OPACITY);
            }
        }
    }

    protected String[] getPenState(){
        return new String[]{Boolean.toString(isPenUp), Integer.toString(myPenColorIndex), Double.toString(myPenThickness)};
    }

    protected String[] getTurtleInfo(int turtleID) {
        Turtle turtle = myTurtles.get(turtleID);
        return new String[]{Boolean.toString(turtle.isActive()), Integer.toString((int)turtle.getRotate())};
    }

    /**
     * Return the image of a turtle. Assumes turtle 0 always exists and all turtles have same image.
     * @return Image object of the turtle
     */
    protected Image getTurtleImage() {
        return myTurtles.get(0).getImage();
    }

    private void toggleActive(int id, Consumer<Boolean> onClicked) {
        if(myTurtles.get(id).isActive()){
            myTurtles.get(id).setActive(false);
            myTurtles.get(id).setOpacity(NOT_HIGHLIGHTED_OPACITY);
            onClicked.accept(false);
        }else{
            myTurtles.get(id).setActive(true);
            myTurtles.get(id).setOpacity(HIGHLIGHTED_OPACITY);
            onClicked.accept(true);
        }
    }

    /**
     * moves the turtle back to home position
     */
    private void resetTurtle(int id){
        myTurtles.get(id).setY(yOffset);
        myTurtles.get(id).setX(xOffset);
        myTurtles.get(id).setRotate(0);
        unalteredTurtlePositions.put(id, new Point2D(0.0, 0.0));
    }

    private double boundX(double x){
        x = x + xOffset;
        while(x > myWidth){
          x -= myWidth;
        }
        while(x < 0){
          x += myWidth;
        }
        return x;
    }

    private double boundY(double y){
        y = y + yOffset;
        while(y > myHeight) {
          y -= myHeight;
        }
        while(y < 0){
          y += myHeight;
        }
        return y;
    }

    private boolean notAlmostEqual(double a, double b){
        return Math.abs(a - b) > SIGNIFICANT_DIFFERENCE;
    }
}
