package slogo.backend.commands.displaycommands;

import java.util.List;
import slogo.CommandResult;
import slogo.backend.BackEndInternal;
import slogo.backend.Command;
import slogo.backend.CommandResultBuilder;
import slogo.backend.Interpreter;
import slogo.backend.ParseException;

public class SetPenSizeCommand extends Command {

    public SetPenSizeCommand(){
        NUM_ARGS = 1;
        NUM_VARS = 0;
    }

    @Override
    public List<CommandResult> execute(List<Double> arguments, List<String> vars, String[] tokens,
        BackEndInternal backEnd, Interpreter interpreter) throws ParseException {
        double size = arguments.get(0);
        backEnd.setPenSize(size);
        CommandResultBuilder builder = backEnd.startCommandResult(backEnd.getTurtles().get(0).getHeading(), backEnd.getTurtles().get(0).getPosition(), backEnd.getTurtles().get(0).getVisible());
        builder.setRetVal(size);
        builder.setPenSize(backEnd.getPenSize());
        return List.of(builder.buildCommandResult());
    }

    @Override
    public List<String> findVars(String[] tokenList) {
        return null;
    }
}
