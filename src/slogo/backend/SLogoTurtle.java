package slogo.backend;

import java.util.List;

public class SLogoTurtle implements Turtle {

    private int myId;
    private double myX = 0;
    private double myY = 0;
    private double myHeading = 0;
    private boolean myPenUp = false;
    private boolean myVisible = true;

    private static final double MAX_DEGREES = 360;
    private static final double TO_RADIANS = Math.PI / 180;
    private static final double TO_DEGREES = 180 / Math.PI;
    private static final double OFFSET = 180;

    public SLogoTurtle(int id){
        myId = id;
    }

    public SLogoTurtle(double x, double y, double heading){
        myX = x; myY = y;
        myHeading = heading;
    }

    @Override
    public double getX() { return myX; }

    @Override
    public double getY() { return myY; }

    /**
     * @return Current position as a list containing x then y
     */
    public List<Double> getPosition() {
        return List.of(myX,myY);
    }

    @Override
    public double setPos(double x, double y) {
        double distanceTraveled = Math.sqrt(Math.pow(myX - x, 2) + Math.pow(myY - y, 2));
        myX = x; myY = y;
        return distanceTraveled;
    }

    @Override
    public double moveForward(double distance) {
        myX += Math.sin(myHeading * TO_RADIANS) * distance;
        myY += Math.cos(myHeading * TO_RADIANS) * distance;
        return Math.abs(distance);
    }

    @Override
    public double moveBack(double distance) {
        return this.moveForward(-distance);
    }

    @Override
    public double turn(double degrees) {
        double newHeading = myHeading + degrees;
        newHeading = newHeading % MAX_DEGREES;
        if(newHeading < 0)
            newHeading += MAX_DEGREES;
        myHeading = newHeading;
        return Math.abs(degrees);
    }

    @Override
    public double setHeading(double direction) {
        double normalizedDir = direction % MAX_DEGREES;
        if(normalizedDir < 0)
            normalizedDir += MAX_DEGREES;
        double temp = myHeading;
        myHeading = normalizedDir;
        return Math.abs(temp - normalizedDir);
    }

    @Override
    public double moveTowards(double x, double y){
        double deltaX = x - myX;
        double deltaY = y - myY;

        double offset = 0;
        if(deltaX < 0) {
            offset = OFFSET;
        }

        double newDir = OFFSET/2 - Math.atan(deltaY/deltaX) * TO_DEGREES + offset;
        if(newDir < 0) {
            newDir = MAX_DEGREES + newDir;
        }
        double temp = myHeading;
        myHeading = newDir;
        return Math.abs(temp - newDir);
    }

    @Override
    public double getHeading() { return myHeading; }

    @Override
    public boolean getPenUp() { return myPenUp; }

    @Override
    public int setPenUp(boolean up) {
        myPenUp = up;
        if(up)
            return 1;
        return 0;
    }

    @Override
    public boolean getVisible(){ return myVisible; }

    @Override
    public int setVisible(boolean vis){
        myVisible = vis;
        if(vis)
            return 1;
        return 0;
    }

    @Override
    public int getId() {
        return myId;
    }

    @Override
    public Turtle getClone() {
        Turtle ret = new SLogoTurtle(myId);
        ret.setPos(myX,myY);
        ret.setHeading(myHeading);
        ret.setPenUp(myPenUp);
        return ret;
    }
}
