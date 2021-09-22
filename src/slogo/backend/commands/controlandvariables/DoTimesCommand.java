package slogo.backend.commands.controlandvariables;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import slogo.backend.BackEndUtil;
import slogo.backend.Command;
import slogo.backend.BackEndInternal;
import slogo.backend.CommandResultBuilder;
import slogo.backend.Interpreter;
import slogo.backend.ParseException;
import slogo.CommandResult;

public class DoTimesCommand extends Command {

  public DoTimesCommand(){
    NUM_ARGS = 1;
    NUM_VARS = 1;
  }

  @Override
  public List<CommandResult> execute(List<Double> arguments, List<String> vars, String[] tokens,
      BackEndInternal backEnd, Interpreter interpreter)
      throws ParseException {
    double limit = arguments.get(0);
    String var = vars.get(1);
    double returnVal = 0;
    List<CommandResult> results = new ArrayList<>();
    int listLength = BackEndUtil.distanceToEndBracket(Arrays.copyOfRange(tokens,2,tokens.length));
    for (double i = 1; i <= limit; i ++) {
      backEnd.setVariable(var,i);
      BackEndUtil.printRemainingTokens(Arrays.copyOfRange(tokens,2,listLength+1),0);
      results.addAll(interpreter.parseCommandsList(Arrays.copyOfRange(tokens,2,listLength+1)));
      returnVal = results.get(results.size()-1).getReturnVal();
    }
    CommandResultBuilder builder = backEnd.startCommandResult(returnVal);
    builder.setTokensParsed(listLength+2);
    results.add(builder.buildCommandResult());
    return results;
  }

  @Override
  public List<String> findVars(String[] tokenList) {
    return List.of(tokenList[0],tokenList[1].substring(1));
  }

  @Override
  public int getTokensParsed(String[] tokens) {
    int listLength = BackEndUtil.distanceToEndBracket(Arrays.copyOfRange(tokens,2,tokens.length));
    return listLength + 2;
  }
}
