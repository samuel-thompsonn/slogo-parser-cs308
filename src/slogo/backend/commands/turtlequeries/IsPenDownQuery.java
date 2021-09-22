package slogo.backend.commands.turtlequeries;

import java.util.List;
import slogo.backend.Command;
import slogo.backend.BackEndInternal;
import slogo.CommandResult;
import slogo.backend.CommandResultBuilder;
import slogo.backend.Interpreter;

public class IsPenDownQuery extends Command {

    @Override
    public List<CommandResult> execute(List<Double> arguments, List<String> vars, String[] tokens,
        BackEndInternal backEnd, Interpreter interpreter) {
        double retVal = (backEnd.getPenUp()) ? 0 : 1;
        CommandResultBuilder builder = backEnd.startCommandResult(retVal);
        return List.of(builder.buildCommandResult());
    }

    @Override
    public List<String> findVars(String[] tokenList) {
        return null;
    }

}
