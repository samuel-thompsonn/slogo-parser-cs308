package slogo.backend.commands.mathcommands;

import java.util.List;
import slogo.backend.Command;
import slogo.backend.BackEndInternal;
import slogo.backend.CommandResultBuilder;
import slogo.backend.Interpreter;
import slogo.backend.ParseException;
import slogo.CommandResult;

public class SumCommand extends Command {

  public SumCommand(){
    NUM_ARGS = 2;
    NUM_VARS = 0;
  }

  @Override
  public List<CommandResult> execute(List<Double> arguments, List<String> vars, String[] tokens,
      BackEndInternal backEnd, Interpreter interpreter) throws ParseException {
    CommandResultBuilder builder = backEnd.startCommandResult(arguments.get(0) + arguments.get(1));
    return List.of(builder.buildCommandResult());
  }

  @Override
  public List<String> findVars(String[] tokenList) {
    return null;
  }
}
