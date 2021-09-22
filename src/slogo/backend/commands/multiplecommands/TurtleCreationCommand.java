package slogo.backend.commands.multiplecommands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import slogo.CommandResult;
import slogo.backend.BackEndInternal;
import slogo.backend.BackEndUtil;
import slogo.backend.Command;
import slogo.backend.CommandResultBuilder;
import slogo.backend.Turtle;

public abstract class TurtleCreationCommand extends Command {
  protected CommandResult initialTurtleResult(Turtle newlyActive, List<Integer> activeTurtleNums, BackEndInternal backEnd) {
    CommandResultBuilder builder = backEnd.startCommandResult(
        newlyActive.getHeading(),
        newlyActive.getPosition(),
        newlyActive.getVisible());
    builder.setRetVal(0);
    builder.activeTurtleIDs(activeTurtleNums);
    builder.setTurtleID(newlyActive.getId());
    return builder.buildCommandResult();
  }

  protected List<CommandResult> activateTurtles(BackEndInternal backEnd,
      List<Integer> activeTurtleNums) {
    List<CommandResult> results = new ArrayList<>();
    for (Turtle newlyActive : backEnd.getActiveTurtles()) {
      results.add(initialTurtleResult(newlyActive,activeTurtleNums,backEnd));
    }
    return results;
  }

  protected List<Integer> findTurtlesAsked(String[] tokens) {
    int numTurtles = BackEndUtil.distanceToEndBracket(
        Arrays.copyOfRange(tokens,1,tokens.length));
    String[] newActives = Arrays.copyOfRange(tokens,1, numTurtles);
    return intStringToList(newActives);
  }

  protected List<Integer> intStringToList(String[] tokens) {
    List<Integer> activeTurtleNums = new ArrayList<>();
    for (int i = 0; i < tokens.length; i ++) {
      activeTurtleNums.add(Integer.parseInt(tokens[i]));
    }
    return activeTurtleNums;
  }
}
