package slogo.backend.commands.turtlecommands;

import java.util.List;
import slogo.backend.Command;
import slogo.backend.BackEndInternal;
import slogo.backend.CommandResultBuilder;
import slogo.CommandResult;
import slogo.backend.Turtle;

public class ForwardCommand extends TurtleCommand {
  public ForwardCommand(){
    NUM_ARGS = 1;
    NUM_VARS = 0;
  }
  @Override
  protected void applyToTurtle(Turtle turtle, List<Double> args) {
    myRetVal = args.get(0);
    turtle.moveForward(args.get(0));
  }

  @Override
  protected CommandResult createCommandResult(Turtle turtle, List<Double> arguments,
      List<Double> prevPos, BackEndInternal backEnd) {
    CommandResultBuilder builder = backEnd.startCommandResult(turtle.getId(),myRetVal);
    builder.setPathStart(prevPos);
    return builder.buildCommandResult();
  }

  @Override
  public List<String> findVars(String[] tokenList) {
    return null;
  }

}
