package slogo.backend.commands.turtlecommands;

import java.util.List;
import slogo.backend.Command;
import slogo.backend.BackEndInternal;
import slogo.CommandResult;
import slogo.backend.Interpreter;

public class PenDownCommand extends Command {
    @Override
    public List<CommandResult> execute(List<Double> arguments, List<String> vars, String[] tokens,
        BackEndInternal backEnd, Interpreter interpreter) {
        backEnd.setPenUp(false);
        return List.of(backEnd.startCommandResult(1).buildCommandResult());
    }

    @Override
    public List<String> findVars(String[] tokenList) {
        return null;
    }
}
