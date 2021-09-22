package slogo.backend;

import java.util.List;

/**
 * The methods that allow interacting with the turtle and causing it
 * to execute its behavior. Allows multiple turtles.
 */
public interface Turtle {

  /**
   * @return The X coordinate of the turtle (0 is the middle)
   */
  public double getX();

  /**
   * @return The Y coordinate of the turtle (1 is the middle)
   */
  public double getY();

  public List<Double> getPosition();


    /**
     * Set the X and Y position of the turtle. (0,0) is the center (depending on how the view draws
     * things)
     * @param x The new X position of the turtle
     * @param y The new Y position of the turtle
     */
  public double setPos(double x, double y);

  /**
   * Moves the turtle forward in the direction of its heading.
   * @param distance The distance to move in the heading direction
   * @return distance moved in pixels
   */
  public double moveForward(double distance);

  /**
   * Moves the turtle backward in the direction of its heading.
   * @param distance The distance to move in the opposite of the heading direction
   * @return distance moved in pixels
   */
  public double moveBack(double distance);

  /**
   * Turns heading clockwise by the specified number of degrees
   * @param degrees The number of degrees to turn clockwise. Enter a negative number to turn
   *                counter-clockwise.
   * @return degrees Number of degrees turned
   */
  public double turn(double degrees);

  /**
   * Sets the turtle to point in the given direction.
   * @param direction The direction to face, in degrees clockwise from facing upward.
   * @return direction in degrees turned
   */
  public double setHeading(double direction);

  /**
   * Sets the turtle to point in the direction of the given point.
   * @param x The x-coordinate of the point to face
   * @param y The y-coordinate of the point to face
   * @return
   */
  public double moveTowards(double x, double y);

  /**
   * @return The direction the turtle is facing, in degrees clockwise from directly up/north.
   */
  public double getHeading();

  /**
   * @return Whether the pen is up (inactive). When the pen is up, no paths are drawn.
   */
  public boolean getPenUp();

  /**
   * @param up True for setting the pen to up/inactive, False to set the pen to down/active.
   * @return 1 if set to up, 0 if set to down
   */
  public int setPenUp(boolean up);

  /**
   * @return whether turtle is visible
   */
  public boolean getVisible();

  /**
   * Change visibility of the turtle
   * @param vis True for setting turtle to visible. False if not visible
   * @return 1 if set to visible, 0 if set to not visible
   */
  public int setVisible(boolean vis);

  public int getId();

  Turtle getClone();

}
