package slogo.frontEnd;

import javafx.scene.shape.Path;

public interface FrontEndInternal {

  /**
   * Constructor for visualizer, which houses all of the display components together
   * @return new instance of Visualizer
   */
   //Visualizer visualizer();

  /**
   * Constructor for a view panel that displays the turtle and the paths the turtle has taken
   * @return new instance of turtleView
   */
  public TurtleView turtleView();

  /**
   * Constructor for a commandBox, which is where the user enters their text
   * @return a new instance of a CommandBox
   */
  CommandBox commandBox();

  /**
   * Constructor for a view panel that shows the history of Slogo commands entered/interpreted
   * @return a new instance of History
   */
  ClearableEntriesBox history();


  /**
   * Create a new path object so that the visualizer can draw the path the turtle takes for one command.
   * Contains start and end point and also the line Color for that path
   * We will look into whether the JFX Path class is useful here or we will create our own.
   * @return
   */
  Path path();
}
