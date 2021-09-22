package slogo.backend.commands.multiplecommands;

import java.lang.reflect.Array;
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

public class AskCommand extends TurtleCreationCommand {

  public static final int TOTAL_NUM_BRACKETS = 4;
  public static final int NUM_BRACKETS_BEFORE_COMMANDS = 3;

  @Override
  public List<CommandResult> execute(List<Double> arguments, List<String> vars, String[] tokens,
      BackEndInternal backEnd, Interpreter interpreter) throws ParseException {
    List<Integer> turtlesAsked = findTurtlesAsked(tokens);
    String[] tokensToParse = findTokensToParse(tokens);

    List<Integer> originalActives = backEnd.getActiveTurtleNumbers();
    backEnd.setActiveTurtles(turtlesAsked);

    List<CommandResult> results = activateTurtles(backEnd, backEnd.getActiveTurtleNumbers());
    results.addAll(interpreter.parseCommandsList(tokensToParse));
    backEnd.setActiveTurtles(originalActives);

    CommandResultBuilder builder = backEnd.startCommandResult(backEnd.getTurtles().get(0).getHeading(),backEnd.getTurtles().get(0).getPosition(), backEnd.getTurtles().get(0).getVisible());
    if (!results.isEmpty()) {
      builder.setRetVal(results.get(results.size()-1).getReturnVal());
    }
    builder.setTokensParsed(turtlesAsked.size() + tokensToParse.length + TOTAL_NUM_BRACKETS);
    results.add(builder.buildCommandResult());
    return results;
  }

  private String[] findTokensToParse(String[] tokens) {
    int start = findTurtlesAsked(tokens).size() + NUM_BRACKETS_BEFORE_COMMANDS;
    String[] remaining = Arrays.copyOfRange(tokens,start,tokens.length);
    int end = start + BackEndUtil.distanceToEndBracket(remaining) - 1;
    return Arrays.copyOfRange(tokens,start,end);
  }

  @Override
  public List<String> findVars(String[] tokenList) {
    return null;
  }
}
