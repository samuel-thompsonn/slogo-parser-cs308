package slogo.backend.commands.controlandvariables;

import java.util.List;
import slogo.backend.Command;
import slogo.backend.BackEndInternal;
import slogo.backend.CommandResultBuilder;
import slogo.backend.Interpreter;
import slogo.backend.ParseException;
import slogo.CommandResult;

public class MakeCommand extends Command {

  public MakeCommand(){
    NUM_ARGS = 1;
    NUM_VARS = 1;
  }

  @Override
  public List<CommandResult> execute(List<Double> arguments, List<String> vars, String[] tokens,
      BackEndInternal backEnd, Interpreter interpreter) throws ParseException {
    System.out.println(vars.get(0));
    backEnd.setVariable(vars.get(0),arguments.get(0));
    CommandResultBuilder builder = backEnd.startCommandResult(arguments.get(0));
    return List.of(builder.buildCommandResult());
  }

  @Override
  public List<String> findVars(String[] tokenList) {return List.of(tokenList[0]);}
}
