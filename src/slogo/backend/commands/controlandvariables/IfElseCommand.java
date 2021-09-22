package slogo.backend.commands.controlandvariables;

import java.util.Arrays;
import java.util.List;
import slogo.backend.BackEndUtil;
import slogo.backend.Command;
import slogo.backend.BackEndInternal;
import slogo.backend.Interpreter;
import slogo.backend.ParseException;
import slogo.CommandResult;

public class IfElseCommand extends Command {

  public IfElseCommand(){
    NUM_ARGS = 1;
    NUM_VARS = 0;
  }

  @Override
  public List<CommandResult> execute(List<Double> arguments, List<String> vars, String[] tokens,
      BackEndInternal backEnd, Interpreter interpreter) throws ParseException {
    double returnVal;
    List<CommandResult> results;
    int firstListLength = BackEndUtil.distanceToEndBracket(Arrays.copyOfRange(tokens,1,tokens.length));
    int secondListLength = BackEndUtil.distanceToEndBracket(Arrays.copyOfRange(tokens,firstListLength+2,tokens.length));
    if (arguments.get(0) != 0) {
      BackEndUtil.printRemainingTokens(Arrays.copyOfRange(tokens,1,firstListLength),0);
      results = interpreter.parseCommandsList(Arrays.copyOfRange(tokens,1,firstListLength));
    }
    else {
      BackEndUtil.printRemainingTokens(Arrays.copyOfRange(tokens,2+firstListLength,firstListLength+secondListLength+1),0);
      results = interpreter.parseCommandsList(Arrays.copyOfRange(tokens,2+firstListLength,firstListLength+secondListLength+1));
    }
    returnVal = results.get(results.size()-1).getReturnVal();
    results.add(backEnd.makeCommandResult(returnVal,firstListLength+secondListLength+2));
    return results;
  }

  @Override
  public List<String> findVars(String[] tokenList) {
    return null;
  }
}
