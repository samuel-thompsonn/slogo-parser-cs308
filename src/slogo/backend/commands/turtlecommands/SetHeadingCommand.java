package slogo.backend.commands.turtlecommands;

import java.util.List;
import slogo.backend.Command;
import slogo.backend.BackEndInternal;
import slogo.CommandResult;
import slogo.backend.CommandResultBuilder;
import slogo.backend.Turtle;

public class SetHeadingCommand extends TurtleCommand {

    public SetHeadingCommand(){
        NUM_ARGS = 1;
        NUM_VARS = 0;
    }

    @Override
    protected void applyToTurtle(Turtle turtle, List<Double> args) {
        myRetVal = turtle.setHeading(args.get(0));
    }

    @Override
    protected CommandResult createCommandResult(Turtle turtle, List<Double> arguments,
                                                List<Double> prevPos, BackEndInternal backEnd) {
        CommandResultBuilder builder = backEnd.startCommandResult(turtle.getId(),myRetVal);
        return builder.buildCommandResult();
    }

    @Override
    public List<String> findVars(String[] tokenList) {
        return null;
    }

    @Override
    public boolean runsPerTurtle() {
        return true;
    }
}
