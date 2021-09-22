package slogo.backend.commands.mathcommands;

import java.util.List;
import slogo.backend.Command;
import slogo.backend.BackEndInternal;
import slogo.backend.Interpreter;
import slogo.backend.ParseException;
import slogo.CommandResult;

public class PiCommand extends Command {

    @Override
    public List<CommandResult> execute(List<Double> arguments, List<String> vars, String[] tokens,
        BackEndInternal backEnd, Interpreter interpreter) throws ParseException {
        return List.of(backEnd.makeCommandResult(Math.PI,0));
    }

    @Override
    public List<String> findVars(String[] tokenList) {
        return null;
    }
}
