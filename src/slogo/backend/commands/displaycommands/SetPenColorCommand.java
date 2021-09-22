package slogo.backend.commands.displaycommands;

import java.util.List;
import slogo.CommandResult;
import slogo.backend.BackEndInternal;
import slogo.backend.Command;
import slogo.backend.CommandResultBuilder;
import slogo.backend.Interpreter;
import slogo.backend.ParseException;

public class SetPenColorCommand extends Command {

    public SetPenColorCommand(){
        NUM_ARGS = 1;
        NUM_VARS = 0;
    }

    @Override
    public List<CommandResult> execute(List<Double> arguments, List<String> vars, String[] tokens,
        BackEndInternal backEnd, Interpreter interpreter) throws ParseException {
        int index = (int) Math.round(arguments.get(0));
        backEnd.setPathColor(index);
        CommandResultBuilder builder = backEnd.startCommandResult(backEnd.getTurtles().get(0).getHeading(), backEnd.getTurtles().get(0).getPosition(), backEnd.getTurtles().get(0).getVisible());
        builder.setRetVal(index);
        builder.setPathColor(backEnd.getPathColor());
        return List.of(builder.buildCommandResult());
    }

    @Override
    public List<String> findVars(String[] tokenList) {
        return null;
    }
}
