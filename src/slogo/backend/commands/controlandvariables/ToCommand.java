package slogo.backend.commands.controlandvariables;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import slogo.backend.BackEndUtil;
import slogo.backend.Command;
import slogo.backend.BackEndInternal;
import slogo.backend.CommandFactory;
import slogo.backend.CommandResultBuilder;
import slogo.backend.Interpreter;
import slogo.backend.ParseException;
import slogo.CommandResult;

public class ToCommand extends Command {

  @Override
  public List<CommandResult> execute(List<Double> arguments, List<String> vars, String[] tokens,
      BackEndInternal backEnd, Interpreter interpreter) throws ParseException {
    int programCounter = 0;
    int numVars = BackEndUtil.distanceToEndBracket(Arrays.copyOfRange(tokens,programCounter+2,tokens.length)) - 1;
    List<String> toVars = new ArrayList<>();
    for (programCounter = 2; programCounter < 2 + numVars; programCounter ++) {
      toVars.add(tokens[programCounter].substring(1));
    }
    programCounter += 2;
    if (programCounter >= tokens.length) {
      throw new ParseException("Expected instructions in brackets ([ ... ])");
    }
    int numCommands = BackEndUtil.distanceToEndBracket(Arrays.copyOfRange(tokens,programCounter,tokens.length)) - 1;
    String[] commandTokens = Arrays.copyOfRange(tokens,programCounter,programCounter + numCommands);
    if (interpreter.hasPrimitiveCommand(tokens[0])) {
      throw new ParseException("Can't redefine primitive " + tokens[0].toUpperCase());
    }
    backEnd.setUserCommand(tokens[0],toVars,commandTokens);
    CommandResultBuilder builder = backEnd.startCommandResult(1.0);
    builder.setTokensParsed(programCounter+numCommands+1);
    return List.of(builder.buildCommandResult());
  }

  @Override
  public List<String> findVars(String[] tokenList) {
    List<String> vars = new ArrayList<>();
    int numVars = BackEndUtil.distanceToEndBracket(Arrays.copyOfRange(tokenList,1,tokenList.length));
    Collections.addAll(vars,Arrays.copyOfRange(tokenList,0,numVars));
    return vars;
  }
}
