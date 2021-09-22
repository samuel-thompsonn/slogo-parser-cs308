package slogo.backend.commands.booleancommands;

import java.util.List;
import slogo.backend.Command;
import slogo.backend.BackEndInternal;
import slogo.backend.Interpreter;
import slogo.backend.ParseException;
import slogo.CommandResult;

public class GreaterCommand extends Command {

  public GreaterCommand(){
    NUM_ARGS = 2;
    NUM_VARS = 0;
  }

  @Override
  public List<CommandResult> execute(List<Double> arguments, List<String> vars, String[] tokens,
      BackEndInternal backEnd, Interpreter interpreter) throws ParseException {
    double retValue = 0;
    if (arguments.get(0) > arguments.get(1)) {
      retValue = 1;
    }
    return List.of(backEnd.makeCommandResult(retValue,0));
  }

  @Override
  public List<String> findVars(String[] tokenList) {
    return null;
  }
}
