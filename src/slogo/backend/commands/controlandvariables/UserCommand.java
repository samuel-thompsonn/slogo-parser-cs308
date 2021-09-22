package slogo.backend.commands.controlandvariables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import slogo.backend.BackEndUtil;
import slogo.backend.Command;
import slogo.backend.BackEndInternal;
import slogo.backend.Interpreter;
import slogo.backend.ParseException;
import slogo.CommandResult;

public class UserCommand extends Command {

  private List<String> myArguments;
  private List<String> myInstructions;

  public UserCommand(List<String> arguments, List<String> commands) {
    myArguments = new ArrayList<>(arguments);
    myInstructions = new ArrayList<>(commands);
    NUM_ARGS = myArguments.size();
    NUM_VARS = 0;
  }

  public Collection<String> getCommands() {
    return new ArrayList<>(myInstructions);
  }

  @Override
  public List<CommandResult> execute(List<Double> arguments, List<String> vars, String[] tokens,
      BackEndInternal backEnd, Interpreter interpreter) throws ParseException {
    double returnVal;
    List<CommandResult> results = new ArrayList<>();
    for (int i = 0; i < arguments.size(); i++) {
      backEnd.setVariable(myArguments.get(i),arguments.get(i));
    }
    results.addAll(interpreter.parseCommandsList(myInstructions.toArray(new String[0])));
    return results;
  }

  @Override
  public List<String> findVars(String[] tokenList) {
    return null;
  }

  public List<String> getArguments() {
    return new ArrayList<>(myArguments);
  }

  public String getInstructions() {
    return (BackEndUtil.concatStringArray(myInstructions.toArray(new String[0])));
  }
}
