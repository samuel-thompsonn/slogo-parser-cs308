package slogo.backend.commands.turtlecommands;

import java.util.List;
import slogo.backend.Command;
import slogo.backend.BackEndInternal;
import slogo.CommandResult;
import slogo.backend.CommandResultBuilder;
import slogo.backend.Turtle;

public class HideTurtleCommand extends TurtleCommand {

    @Override
    protected void applyToTurtle(Turtle turtle, List<Double> args) {
        myRetVal = 0;
        turtle.setVisible(false);
    }

    @Override
    protected CommandResult createCommandResult(Turtle turtle, List<Double> arguments,
        List<Double> prevPos, BackEndInternal backEnd) {
        CommandResultBuilder builder = backEnd.startCommandResult(turtle.getId(),myRetVal);
        builder.setVisible(turtle.getVisible());
        return builder.buildCommandResult();
    }

    @Override
    public List<String> findVars(String[] tokenList) {
        return null;
    }
}
