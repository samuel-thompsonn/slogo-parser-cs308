package slogo.backend.commands.controlandvariables;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import slogo.backend.BackEndUtil;
import slogo.backend.Command;
import slogo.backend.BackEndInternal;
import slogo.backend.Interpreter;
import slogo.backend.ParseException;
import slogo.CommandResult;

public class ForLoopCommand extends Command {

  public ForLoopCommand(){
    NUM_ARGS = 3;
    NUM_VARS = 1;
  }

  @Override
  public List<CommandResult> execute(List<Double> arguments, List<String> vars, String[] tokens,
      BackEndInternal backEnd, Interpreter interpreter)
      throws ParseException {
    double start = arguments.get(0);
    double end = arguments.get(1);
    double increment = arguments.get(2);
    String var = vars.get(1);
    double returnVal = 0;
    List<CommandResult> results = new ArrayList<>();
    int listLength = BackEndUtil.distanceToEndBracket(Arrays.copyOfRange(tokens,2,tokens.length));
    for (double i = start; i <= end; i += increment) {
      backEnd.setVariable(var,i);
      results.addAll(interpreter.parseCommandsList(Arrays.copyOfRange(tokens,2,listLength+1)));
      returnVal = results.get(results.size()-1).getReturnVal();
    }
    results.add(backEnd.makeCommandResult(returnVal,listLength+2));
    return results;
  }

  @Override
  public List<String> findVars(String[] tokenList) {
    return List.of(tokenList[0],tokenList[1].substring(1));
  }
}
