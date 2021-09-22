package slogo.backend.commands.multiplecommands;

import java.util.List;
import slogo.CommandResult;
import slogo.backend.BackEndInternal;
import slogo.backend.Command;
import slogo.backend.Interpreter;
import slogo.backend.ParseException;

public class IdCommand extends Command {

  @Override
  public List<CommandResult> execute(List<Double> arguments, List<String> vars, String[] tokens,
      BackEndInternal backEnd, Interpreter interpreter) throws ParseException {
    if (backEnd.getActiveTurtleID() == null) {
      throw new ParseException("Can't use ID unless issuing turtle commands");
    }
    return List.of(backEnd.makeCommandResult(backEnd.getActiveTurtleID(),0));
  }

  @Override
  public List<String> findVars(String[] tokenList) {
    return null;
  }
}
