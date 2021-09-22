package slogo.backend.commands.turtlecommands;

import java.util.List;
import slogo.backend.Command;
import slogo.backend.BackEndInternal;
import slogo.CommandResult;
import slogo.backend.Interpreter;

public class PenUpCommand extends Command {
    @Override
    public List<CommandResult> execute(List<Double> arguments, List<String> vars, String[] tokens,
        BackEndInternal backEnd, Interpreter interpreter) {
        backEnd.setPenUp(true);
        return List.of(backEnd.startCommandResult(0).buildCommandResult());
    }

    @Override
    public List<String> findVars(String[] tokenList) {
        return null;
    }
}
