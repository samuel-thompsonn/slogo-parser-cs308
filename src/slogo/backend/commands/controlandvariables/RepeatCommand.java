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

public class RepeatCommand extends Command {

  public RepeatCommand(){
    NUM_ARGS = 1;
    NUM_VARS = 0;
  }

  @Override
  public List<CommandResult> execute(List<Double> arguments, List<String> vars, String[] tokens,
      BackEndInternal backEnd, Interpreter interpreter)
      throws ParseException {
    double numLoops = arguments.get(0);
    double returnVal = 0;
    List<CommandResult> results = new ArrayList<>();
    int listLength = BackEndUtil.distanceToEndBracket(Arrays.copyOfRange(tokens,1,tokens.length));
    for (double i = 1; i <= numLoops; i ++) {
      backEnd.setVariable("repcount",i);
      results.addAll(interpreter.parseCommandsList(Arrays.copyOfRange(tokens,1,listLength)));
      returnVal = results.get(results.size()-1).getReturnVal();
    }
    results.add(backEnd.makeCommandResult(returnVal,listLength+1));
    return results;
  }

  @Override
  public List<String> findVars(String[] tokenList) {
    return null;
  }
}
