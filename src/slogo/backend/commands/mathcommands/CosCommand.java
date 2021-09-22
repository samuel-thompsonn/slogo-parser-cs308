package slogo.backend.commands.mathcommands;

import java.util.List;

import slogo.backend.Command;
import slogo.backend.BackEndInternal;
import slogo.backend.Interpreter;
import slogo.backend.ParseException;
import slogo.CommandResult;

public class CosCommand extends Command {

    private static final double TO_RADIANS = Math.PI/180;

    public CosCommand(){
        NUM_ARGS = 1;
        NUM_VARS = 0;
    }

    @Override
    public List<CommandResult> execute(List<Double> arguments, List<String> vars, String[] tokens,
        BackEndInternal backEnd, Interpreter interpreter) throws ParseException {
        return List.of(backEnd.makeCommandResult(Math.cos(arguments.get(0) * TO_RADIANS), 0));
    }

    @Override
    public List<String> findVars(String[] tokenList) {
        return null;
    }
}
