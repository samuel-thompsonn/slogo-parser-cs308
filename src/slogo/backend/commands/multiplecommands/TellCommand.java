package slogo.backend.commands.multiplecommands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import slogo.CommandResult;
import slogo.backend.BackEndInternal;
import slogo.backend.BackEndUtil;
import slogo.backend.Command;
import slogo.backend.CommandResultBuilder;
import slogo.backend.Interpreter;
import slogo.backend.ParseException;
import slogo.backend.Turtle;

public class TellCommand extends TurtleCreationCommand {
  public static final int TOTAL_NUM_BRACKETS = 2;

  @Override
  public List<CommandResult> execute(List<Double> arguments, List<String> vars, String[] tokens,
      BackEndInternal backEnd, Interpreter interpreter) throws ParseException {
    List<Integer> activeTurtleNums = findTurtlesAsked(tokens);
    double lastTurtleNum = 0;
    if (!activeTurtleNums.isEmpty()) {
      lastTurtleNum = activeTurtleNums.get(activeTurtleNums.size()-1);
    }
    backEnd.setActiveTurtles(activeTurtleNums);
    List<CommandResult> results = new ArrayList<>();
    for (Turtle newlyActive : backEnd.getActiveTurtles()) {
      results.add(initialTurtleResult(newlyActive,activeTurtleNums,backEnd));
    }
    CommandResultBuilder builder = backEnd.startCommandResult(backEnd.getTurtles().get(0).getHeading(),backEnd.getTurtles().get(0).getPosition(), backEnd.getTurtles().get(0).getVisible());
    if (!results.isEmpty()) {
      builder.setRetVal(lastTurtleNum);
    }
    builder.setTokensParsed(activeTurtleNums.size() + TOTAL_NUM_BRACKETS);
    results.add(builder.buildCommandResult());
    return results;
  }

  @Override
  public List<String> findVars(String[] tokenList) {
    return null;
  }
}
